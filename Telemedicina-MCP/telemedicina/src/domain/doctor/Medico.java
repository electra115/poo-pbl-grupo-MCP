package domain.doctor;

import domain.shared.Crm;
import domain.shared.Email;
import domain.shared.Periodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade: Medico (Aggregate Root)
 * Bounded Context de "Agenda Médica".
 * Controla a disponibilidade e agenda de um médico.
 */
public class Medico {

    private final String id;
    private String nome;
    private final Crm crm;
    private String especialidade;
    private Email email;
    private final List<Periodo> disponibilidades;

    public Medico(String nome, Crm crm, String especialidade, Email email) {
        validarNome(nome);
        validarEspecialidade(especialidade);

        this.id = UUID.randomUUID().toString();
        this.nome = nome.trim();
        this.crm = crm;
        this.especialidade = especialidade.trim();
        this.email = email;
        this.disponibilidades = new ArrayList<>();
    }

    public Medico(String id, String nome, Crm crm, String especialidade, Email email) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID do médico não pode ser nulo.");
        validarNome(nome);
        validarEspecialidade(especialidade);

        this.id = id;
        this.nome = nome.trim();
        this.crm = crm;
        this.especialidade = especialidade.trim();
        this.email = email;
        this.disponibilidades = new ArrayList<>();
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do médico não pode ser vazio.");
        }
    }

    private void validarEspecialidade(String especialidade) {
        if (especialidade == null || especialidade.isBlank()) {
            throw new IllegalArgumentException("Especialidade não pode ser vazia.");
        }
    }

    public void adicionarDisponibilidade(Periodo periodo) {
        if (periodo == null) throw new IllegalArgumentException("Período não pode ser nulo.");
        for (Periodo existente : disponibilidades) {
            if (existente.conflitaCom(periodo)) {
                throw new IllegalStateException("O período conflita com uma disponibilidade já cadastrada.");
            }
        }
        this.disponibilidades.add(periodo);
    }

    public boolean estaDisponivel(Periodo periodo) {
        return disponibilidades.stream().anyMatch(d ->
            !d.getInicio().isAfter(periodo.getInicio()) &&
            !d.getFim().isBefore(periodo.getFim())
        );
    }

    public List<Periodo> getDisponibilidades() {
        return Collections.unmodifiableList(disponibilidades);
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public Crm getCrm() { return crm; }
    public String getEspecialidade() { return especialidade; }
    public Email getEmail() { return email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medico)) return false;
        Medico medico = (Medico) o;
        return Objects.equals(id, medico.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Medico{id='" + id + "', nome='" + nome + "', crm=" + crm + ", especialidade='" + especialidade + "'}";
    }
}
