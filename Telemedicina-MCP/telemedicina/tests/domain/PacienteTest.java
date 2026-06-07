package domain;

import domain.patient.Paciente;
import domain.shared.Cpf;
import domain.shared.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Entidade: Paciente - Regras de Negócio")
class PacienteTest {

    private Cpf cpfValido;
    private Email emailValido;

    @BeforeEach
    void setUp() {
        cpfValido = new Cpf("529.982.247-25");
        emailValido = new Email("maria@teste.com");
    }

    // ========== CENÁRIOS DE SUCESSO ==========

    @Test
    @DisplayName("Deve criar paciente válido com ID gerado automaticamente")
    void deveCriarPacienteValido() {
        Paciente paciente = new Paciente("Maria Clara", cpfValido, emailValido,
                LocalDate.of(1995, 3, 15), "82999990000");

        assertNotNull(paciente.getId());
        assertEquals("Maria Clara", paciente.getNome());
        assertEquals(cpfValido, paciente.getCpf());
    }

    @Test
    @DisplayName("Dois pacientes distintos não devem ser iguais mesmo com mesmo CPF")
    void doisPacientesDisjuntosNaoDevemSerIguais() {
        Paciente p1 = new Paciente("Maria", cpfValido, emailValido,
                LocalDate.of(1990, 1, 1), "82988880000");
        Paciente p2 = new Paciente("Maria", cpfValido, emailValido,
                LocalDate.of(1990, 1, 1), "82988880000");
        // Entidades são iguais apenas pelo ID
        assertNotEquals(p1, p2);
    }

    @Test
    @DisplayName("Deve atualizar email do paciente")
    void deveAtualizarEmailDoPaciente() {
        Paciente paciente = new Paciente("Maria Clara", cpfValido, emailValido,
                LocalDate.of(1995, 3, 15), "82999990000");

        Email novoEmail = new Email("novo@email.com");
        paciente.atualizarEmail(novoEmail);

        assertEquals(novoEmail, paciente.getEmail());
    }

    // ========== CENÁRIOS DE FALHA ==========

    @Test
    @DisplayName("Deve lançar exceção para nome vazio")
    void deveLancarExcecaoParaNomeVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paciente("", cpfValido, emailValido, LocalDate.of(1995, 1, 1), "82999990000"));
    }

    @Test
    @DisplayName("Deve lançar exceção para nome muito curto")
    void deveLancarExcecaoParaNomeCurto() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paciente("AB", cpfValido, emailValido, LocalDate.of(1995, 1, 1), "82999990000"));
    }

    @Test
    @DisplayName("Deve lançar exceção para data de nascimento no futuro")
    void deveLancarExcecaoParaDataNascimentoFutura() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paciente("Maria Clara", cpfValido, emailValido,
                        LocalDate.now().plusDays(1), "82999990000"));
    }

    @Test
    @DisplayName("Deve lançar exceção para telefone inválido")
    void deveLancarExcecaoParaTelefoneInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paciente("Maria Clara", cpfValido, emailValido,
                        LocalDate.of(1995, 1, 1), "123"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar email com valor nulo")
    void deveLancarExcecaoAoAtualizarEmailNulo() {
        Paciente paciente = new Paciente("Maria Clara", cpfValido, emailValido,
                LocalDate.of(1995, 3, 15), "82999990000");
        assertThrows(IllegalArgumentException.class, () -> paciente.atualizarEmail(null));
    }
}
