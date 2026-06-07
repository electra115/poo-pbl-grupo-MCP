package domain;

import domain.shared.Periodo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VO: Periodo - Validações de Regra de Negócio")
class PeriodoTest {

    private final LocalDateTime BASE = LocalDateTime.now().plusDays(1);

    @Test
    @DisplayName("Deve criar período válido")
    void deveCriarPeriodoValido() {
        Periodo periodo = new Periodo(BASE, BASE.plusHours(1));
        assertEquals(BASE, periodo.getInicio());
        assertEquals(BASE.plusHours(1), periodo.getFim());
    }

    @Test
    @DisplayName("Deve lançar exceção se fim não for após início")
    void deveLancarExcecaoSeFimNaoForAposInicio() {
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> new Periodo(BASE, BASE)
        );
        assertTrue(ex.getMessage().contains("posterior ao início"));
    }

    @Test
    @DisplayName("Deve lançar exceção se início for nulo")
    void deveLancarExcecaoSeInicioNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Periodo(null, BASE));
    }

    @Test
    @DisplayName("Deve detectar conflito entre períodos sobrepostos")
    void deveDetectarConflito() {
        Periodo p1 = new Periodo(BASE, BASE.plusHours(2));
        Periodo p2 = new Periodo(BASE.plusHours(1), BASE.plusHours(3));
        assertTrue(p1.conflitaCom(p2));
    }

    @Test
    @DisplayName("Não deve detectar conflito entre períodos sequenciais")
    void naoDeveDetectarConflitoEmPeriodosSequenciais() {
        Periodo p1 = new Periodo(BASE, BASE.plusHours(1));
        Periodo p2 = new Periodo(BASE.plusHours(1), BASE.plusHours(2));
        assertFalse(p1.conflitaCom(p2));
    }

    @Test
    @DisplayName("Deve identificar período no futuro")
    void deveIdentificarPeriodoNoFuturo() {
        Periodo periodo = new Periodo(BASE, BASE.plusHours(1));
        assertTrue(periodo.isNoFuturo());
    }
}
