package domain.appointment;

import domain.shared.Periodo;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidade: Consulta (Aggregate Root)
 * Bounded Context de "Agenda Médica" / "Atendimento".
 * Gerencia o ciclo de vida de uma consulta médica (presencial ou telemedicina).
 * Garante a consistência das regras de negócio de agendamento.
 */
public class Consulta {

    private final String id;
    private final String pacienteId;
    private final String medicoId;
    private final Periodo periodo;
    private final TipoConsulta tipo;
    private StatusConsulta status;
    private LinkVideoconferencia linkVideoconferencia;
    private ReceitaMedica receita;
    private String observacoes;

    public Consulta(String pacienteId, String medicoId, Periodo periodo, TipoConsulta tipo) {
        validarIds(pacienteId, medicoId);
        if (periodo == null) throw new IllegalArgumentException("Período da consulta não pode ser nulo.");
        if (tipo == null) throw new IllegalArgumentException("Tipo de consulta não pode ser nulo.");
        if (!periodo.isNoFuturo()) {
            throw new IllegalArgumentException("Consulta deve ser agendada para uma data/hora futura.");
        }

        this.id = UUID.randomUUID().toString();
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.periodo = periodo;
        this.tipo = tipo;
        this.status = StatusConsulta.AGENDADA;

        if (tipo == TipoConsulta.TELEMEDICINA) {
            this.linkVideoconferencia = null; // deve ser adicionado antes da confirmação
        }
    }

    private void validarIds(String pacienteId, String medicoId) {
        if (pacienteId == null || pacienteId.isBlank()) {
            throw new IllegalArgumentException("ID do paciente é obrigatório.");
        }
        if (medicoId == null || medicoId.isBlank()) {
            throw new IllegalArgumentException("ID do médico é obrigatório.");
        }
    }

    /**
     * Confirma a consulta. Para telemedicina, exige link de videoconferência.
     */
    public void confirmar(LinkVideoconferencia link) {
        if (status != StatusConsulta.AGENDADA) {
            throw new IllegalStateException("Apenas consultas AGENDADAS podem ser confirmadas.");
        }
        if (tipo == TipoConsulta.TELEMEDICINA) {
            if (link == null) {
                throw new IllegalArgumentException("Consultas de telemedicina exigem link de videoconferência.");
            }
            this.linkVideoconferencia = link;
        }
        this.status = StatusConsulta.CONFIRMADA;
    }

    /**
     * Confirma consulta presencial sem link.
     */
    public void confirmar() {
        confirmar(null);
    }

    public void cancelar() {
        if (status == StatusConsulta.REALIZADA) {
            throw new IllegalStateException("Consulta já realizada não pode ser cancelada.");
        }
        if (status == StatusConsulta.CANCELADA) {
            throw new IllegalStateException("Consulta já está cancelada.");
        }
        this.status = StatusConsulta.CANCELADA;
    }

    public void marcarComoRealizada(String observacoes) {
        if (status != StatusConsulta.CONFIRMADA) {
            throw new IllegalStateException("Apenas consultas CONFIRMADAS podem ser marcadas como realizadas.");
        }
        this.status = StatusConsulta.REALIZADA;
        this.observacoes = observacoes;
    }

    public void emitirReceita(ReceitaMedica receita) {
        if (status != StatusConsulta.REALIZADA) {
            throw new IllegalStateException("Receita só pode ser emitida após a consulta ser realizada.");
        }
        if (receita == null) throw new IllegalArgumentException("Receita não pode ser nula.");
        this.receita = receita;
    }

    public String getId() { return id; }
    public String getPacienteId() { return pacienteId; }
    public String getMedicoId() { return medicoId; }
    public Periodo getPeriodo() { return periodo; }
    public TipoConsulta getTipo() { return tipo; }
    public StatusConsulta getStatus() { return status; }
    public LinkVideoconferencia getLinkVideoconferencia() { return linkVideoconferencia; }
    public ReceitaMedica getReceita() { return receita; }
    public String getObservacoes() { return observacoes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Consulta)) return false;
        Consulta consulta = (Consulta) o;
        return Objects.equals(id, consulta.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Consulta{id='" + id + "', tipo=" + tipo + ", status=" + status + ", periodo=" + periodo + "}";
    }
}
