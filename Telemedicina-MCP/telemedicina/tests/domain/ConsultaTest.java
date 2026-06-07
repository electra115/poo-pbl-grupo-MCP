package domain;

import domain.appointment.*;
import domain.shared.Periodo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Entidade: Consulta - Ciclo de Vida e Regras de Negócio")
class ConsultaTest {

    private String pacienteId;
    private String medicoId;
    private Periodo periodoFuturo;

    @BeforeEach
    void setUp() {
        pacienteId = "pac-001";
        medicoId = "med-001";
        LocalDateTime amanha = LocalDateTime.now().plusDays(1);
        periodoFuturo = new Periodo(amanha, amanha.plusMinutes(30));
    }

    // ========== CRIAÇÃO ==========

    @Test
    @DisplayName("Deve criar consulta presencial AGENDADA com ID gerado")
    void deveCriarConsultaPresencial() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL);
        assertNotNull(consulta.getId());
        assertEquals(StatusConsulta.AGENDADA, consulta.getStatus());
        assertEquals(TipoConsulta.PRESENCIAL, consulta.getTipo());
    }

    @Test
    @DisplayName("Deve lançar exceção para consulta no passado")
    void deveLancarExcecaoParaConsultaNoPasado() {
        LocalDateTime passado = LocalDateTime.now().minusDays(1);
        Periodo periodoPasado = new Periodo(passado, passado.plusHours(1));
        assertThrows(IllegalArgumentException.class,
                () -> new Consulta(pacienteId, medicoId, periodoPasado, TipoConsulta.PRESENCIAL));
    }

    @Test
    @DisplayName("Deve lançar exceção para pacienteId nulo")
    void deveLancarExcecaoParaPacienteIdNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Consulta(null, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL));
    }

    // ========== CONFIRMAÇÃO ==========

    @Test
    @DisplayName("Deve confirmar consulta presencial sem link")
    void deveConfirmarConsultaPresencial() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL);
        consulta.confirmar();
        assertEquals(StatusConsulta.CONFIRMADA, consulta.getStatus());
    }

    @Test
    @DisplayName("Deve confirmar consulta de telemedicina com link")
    void deveConfirmarTelemedicinaComLink() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.TELEMEDICINA);
        LinkVideoconferencia link = new LinkVideoconferencia("https://meet.google.com/abc-def-ghi");
        consulta.confirmar(link);

        assertEquals(StatusConsulta.CONFIRMADA, consulta.getStatus());
        assertEquals(link, consulta.getLinkVideoconferencia());
    }

    @Test
    @DisplayName("Deve lançar exceção ao confirmar telemedicina sem link")
    void deveLancarExcecaoAoConfirmarTelemedicinaSemLink() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.TELEMEDICINA);
        assertThrows(IllegalArgumentException.class, () -> consulta.confirmar(null));
    }

    @Test
    @DisplayName("Não deve confirmar consulta já cancelada")
    void naoDeveConfirmarConsultaCancelada() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL);
        consulta.cancelar();
        assertThrows(IllegalStateException.class, consulta::confirmar);
    }

    // ========== CANCELAMENTO ==========

    @Test
    @DisplayName("Deve cancelar consulta agendada")
    void deveCancelarConsultaAgendada() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL);
        consulta.cancelar();
        assertEquals(StatusConsulta.CANCELADA, consulta.getStatus());
    }

    @Test
    @DisplayName("Não deve cancelar consulta já realizada")
    void naoDeveCancelarConsultaRealizada() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL);
        consulta.confirmar();
        consulta.marcarComoRealizada("Consulta de rotina.");
        assertThrows(IllegalStateException.class, consulta::cancelar);
    }

    // ========== REALIZAÇÃO E RECEITA ==========

    @Test
    @DisplayName("Deve marcar consulta como realizada após confirmação")
    void deveMarcarConsultaComoRealizada() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL);
        consulta.confirmar();
        consulta.marcarComoRealizada("Paciente apresentou melhora.");
        assertEquals(StatusConsulta.REALIZADA, consulta.getStatus());
    }

    @Test
    @DisplayName("Não deve marcar como realizada sem estar confirmada")
    void naoDeveMarcarRealizadaSemConfirmacao() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL);
        assertThrows(IllegalStateException.class,
                () -> consulta.marcarComoRealizada("Observação qualquer."));
    }

    @Test
    @DisplayName("Deve emitir receita após consulta realizada")
    void deveEmitirReceitaAposConsultaRealizada() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL);
        consulta.confirmar();
        consulta.marcarComoRealizada("Prescrito medicamento.");

        ReceitaMedica receita = new ReceitaMedica(
                medicoId, "Dr. João", "Maria Clara",
                List.of("Amoxicilina 500mg - 1 cp 8h/8h por 7 dias"),
                "Tomar com água, após refeições.",
                LocalDate.now().plusDays(30)
        );
        consulta.emitirReceita(receita);

        assertNotNull(consulta.getReceita());
        assertTrue(consulta.getReceita().isValida());
    }

    @Test
    @DisplayName("Não deve emitir receita sem a consulta ter sido realizada")
    void naoDeveEmitirReceitaSemConsultaRealizada() {
        Consulta consulta = new Consulta(pacienteId, medicoId, periodoFuturo, TipoConsulta.PRESENCIAL);
        ReceitaMedica receita = new ReceitaMedica(
                medicoId, "Dr. João", "Maria Clara",
                List.of("Paracetamol 750mg"),
                "1 comprimido de 6 em 6 horas.",
                LocalDate.now().plusDays(30)
        );
        assertThrows(IllegalStateException.class, () -> consulta.emitirReceita(receita));
    }
}
