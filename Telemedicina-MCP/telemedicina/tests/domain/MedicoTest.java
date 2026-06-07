package domain;

import domain.doctor.Medico;
import domain.shared.Crm;
import domain.shared.Email;
import domain.shared.Periodo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Entidade: Medico - Regras de Negócio")
class MedicoTest {

    private Crm crmValido;
    private Email emailValido;
    private LocalDateTime base;

    @BeforeEach
    void setUp() {
        crmValido = new Crm("CRM/AL 12345");
        emailValido = new Email("dr.carlos@clinica.com");
        base = LocalDateTime.now().plusDays(1);
    }

    @Test
    @DisplayName("Deve criar médico válido")
    void deveCriarMedicoValido() {
        Medico medico = new Medico("Dr. Carlos", crmValido, "Cardiologia", emailValido);
        assertNotNull(medico.getId());
        assertEquals("Dr. Carlos", medico.getNome());
        assertEquals("Cardiologia", medico.getEspecialidade());
    }

    @Test
    @DisplayName("Deve adicionar disponibilidade ao médico")
    void deveAdicionarDisponibilidade() {
        Medico medico = new Medico("Dr. Carlos", crmValido, "Cardiologia", emailValido);
        Periodo periodo = new Periodo(base, base.plusHours(8));

        medico.adicionarDisponibilidade(periodo);

        assertEquals(1, medico.getDisponibilidades().size());
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar disponibilidade conflitante")
    void deveLancarExcecaoEmDisponibilidadeConflitante() {
        Medico medico = new Medico("Dr. Carlos", crmValido, "Cardiologia", emailValido);
        Periodo p1 = new Periodo(base, base.plusHours(4));
        Periodo p2 = new Periodo(base.plusHours(2), base.plusHours(6));

        medico.adicionarDisponibilidade(p1);

        assertThrows(IllegalStateException.class, () -> medico.adicionarDisponibilidade(p2));
    }

    @Test
    @DisplayName("Deve confirmar disponibilidade do médico em período válido")
    void deveConfirmarDisponibilidadeEmPeriodoValido() {
        Medico medico = new Medico("Dr. Carlos", crmValido, "Cardiologia", emailValido);
        Periodo disponivel = new Periodo(base, base.plusHours(8));
        medico.adicionarDisponibilidade(disponivel);

        Periodo consulta = new Periodo(base.plusHours(1), base.plusHours(2));
        assertTrue(medico.estaDisponivel(consulta));
    }

    @Test
    @DisplayName("Deve lançar exceção para especialidade vazia")
    void deveLancarExcecaoParaEspecialidadeVazia() {
        assertThrows(IllegalArgumentException.class,
                () -> new Medico("Dr. Carlos", crmValido, "", emailValido));
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar disponibilidade nula")
    void deveLancarExcecaoParaDisponibilidadeNula() {
        Medico medico = new Medico("Dr. Carlos", crmValido, "Cardiologia", emailValido);
        assertThrows(IllegalArgumentException.class, () -> medico.adicionarDisponibilidade(null));
    }
}
