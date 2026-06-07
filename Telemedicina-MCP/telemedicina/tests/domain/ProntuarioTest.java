package domain;

import domain.patient.Prontuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Entidade: Prontuario - Regras de Negócio")
class ProntuarioTest {

    @Test
    @DisplayName("Deve criar prontuário associado ao paciente")
    void deveCriarProntuario() {
        Prontuario prontuario = new Prontuario("pac-001");
        assertNotNull(prontuario.getId());
        assertEquals("pac-001", prontuario.getPacienteId());
        assertTrue(prontuario.getRegistros().isEmpty());
    }

    @Test
    @DisplayName("Deve adicionar registro médico ao prontuário")
    void deveAdicionarRegistroMedico() {
        Prontuario prontuario = new Prontuario("pac-001");
        prontuario.adicionarRegistro("Paciente com pressão 12x8", "med-001", "Dr. Carlos");

        assertEquals(1, prontuario.getRegistros().size());
        assertEquals("Paciente com pressão 12x8", prontuario.getRegistros().get(0).getDescricao());
        assertNotNull(prontuario.getRegistros().get(0).getDataHora());
    }

    @Test
    @DisplayName("Lista de registros deve ser imutável")
    void listaDeRegistrosDeveSerImutavel() {
        Prontuario prontuario = new Prontuario("pac-001");
        prontuario.adicionarRegistro("Registro 1", "med-001", "Dr. Carlos");

        assertThrows(UnsupportedOperationException.class,
                () -> prontuario.getRegistros().clear());
    }

    @Test
    @DisplayName("Deve lançar exceção para pacienteId nulo no prontuário")
    void deveLancarExcecaoParaPacienteIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Prontuario(null));
    }

    @Test
    @DisplayName("Deve lançar exceção para descrição de registro vazia")
    void deveLancarExcecaoParaDescricaoVazia() {
        Prontuario prontuario = new Prontuario("pac-001");
        assertThrows(IllegalArgumentException.class,
                () -> prontuario.adicionarRegistro("", "med-001", "Dr. Carlos"));
    }

    @Test
    @DisplayName("Deve lançar exceção para medicoId nulo no registro")
    void deveLancarExcecaoParaMedicoIdNulo() {
        Prontuario prontuario = new Prontuario("pac-001");
        assertThrows(IllegalArgumentException.class,
                () -> prontuario.adicionarRegistro("Descrição", null, "Dr. Carlos"));
    }
}
