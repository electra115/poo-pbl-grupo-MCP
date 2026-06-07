package domain.patient;

import domain.shared.Cpf;
import domain.shared.Email;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade: Paciente (Aggregate Root)
 * Possui identidade própria (id). Encapsula dados sensíveis com encapsulamento rigoroso.
 * Bounded Context isolado para garantir privacidade dos dados do paciente (DDD).
 */
public class Paciente {

    private final String id;
    private String nome;
    private final Cpf cpf;
    private Email email;
    private LocalDate dataNascimento;
    private String telefone;

    public Paciente(String nome, Cpf cpf, Email email, LocalDate dataNascimento, String telefone) {
        validarNome(nome);
        validarDataNascimento(dataNascimento);
        validarTelefone(telefone);

        this.id = UUID.randomUUID().toString();
        this.nome = nome.trim();
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
    }

    // Construtor com ID externo (para reconstituição a partir de persistência)
    public Paciente(String id, String nome, Cpf cpf, Email email, LocalDate dataNascimento, String telefone) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID do paciente não pode ser nulo.");
        validarNome(nome);
        validarDataNascimento(dataNascimento);
        validarTelefone(telefone);

        this.id = id;
        this.nome = nome.trim();
        this.cpf = cpf;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do paciente não pode ser vazio.");
        }
        if (nome.trim().length() < 3) {
            throw new IllegalArgumentException("Nome do paciente deve ter ao menos 3 caracteres.");
        }
    }

    private void validarDataNascimento(LocalDate data) {
        if (data == null) throw new IllegalArgumentException("Data de nascimento não pode ser nula.");
        if (data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser no futuro.");
        }
    }

    private void validarTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone não pode ser vazio.");
        }
        String digits = telefone.replaceAll("[^0-9]", "");
        if (digits.length() < 10 || digits.length() > 11) {
            throw new IllegalArgumentException("Telefone inválido: deve ter 10 ou 11 dígitos.");
        }
    }

    public void atualizarEmail(Email novoEmail) {
        if (novoEmail == null) throw new IllegalArgumentException("Novo e-mail não pode ser nulo.");
        this.email = novoEmail;
    }

    public void atualizarTelefone(String novoTelefone) {
        validarTelefone(novoTelefone);
        this.telefone = novoTelefone;
    }

    // Getters — sem setters diretos (encapsulamento)
    public String getId() { return id; }
    public String getNome() { return nome; }
    public Cpf getCpf() { return cpf; }
    public Email getEmail() { return email; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getTelefone() { return telefone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        Paciente paciente = (Paciente) o;
        return Objects.equals(id, paciente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Paciente{id='" + id + "', nome='" + nome + "', cpf=" + cpf + "}";
    }
}
