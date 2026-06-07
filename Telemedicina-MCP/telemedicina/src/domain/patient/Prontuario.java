package domain.patient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade: Prontuario Eletrônico
 * Pertence ao Aggregate Root Paciente. Só pode ser acessado/modificado
 * através do Paciente (DDD - consistência do Aggregate).
 * Encapsula o histórico médico de forma imutável por registro.
 */
public class Prontuario {

    private final String id;
    private final String pacienteId;
    private final List<RegistroMedico> registros;

    public Prontuario(String pacienteId) {
        if (pacienteId == null || pacienteId.isBlank()) {
            throw new IllegalArgumentException("ID do paciente é obrigatório para criar o prontuário.");
        }
        this.id = UUID.randomUUID().toString();
        this.pacienteId = pacienteId;
        this.registros = new ArrayList<>();
    }

    public void adicionarRegistro(String descricao, String medicoId, String medicoNome) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição do registro médico não pode ser vazia.");
        }
        if (medicoId == null || medicoId.isBlank()) {
            throw new IllegalArgumentException("ID do médico é obrigatório no registro.");
        }
        RegistroMedico registro = new RegistroMedico(descricao, medicoId, medicoNome);
        this.registros.add(registro);
    }

    public List<RegistroMedico> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    public String getId() { return id; }
    public String getPacienteId() { return pacienteId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prontuario)) return false;
        Prontuario that = (Prontuario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    /**
     * Value Object interno: RegistroMedico (imutável após criação)
     */
    public static final class RegistroMedico {
        private final String descricao;
        private final String medicoId;
        private final String medicoNome;
        private final LocalDateTime dataHora;

        private RegistroMedico(String descricao, String medicoId, String medicoNome) {
            this.descricao = descricao;
            this.medicoId = medicoId;
            this.medicoNome = medicoNome;
            this.dataHora = LocalDateTime.now();
        }

        public String getDescricao() { return descricao; }
        public String getMedicoId() { return medicoId; }
        public String getMedicoNome() { return medicoNome; }
        public LocalDateTime getDataHora() { return dataHora; }
    }
}
