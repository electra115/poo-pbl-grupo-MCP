package application;

import application.usecases.AgendarConsultaUseCase;
import application.usecases.CadastrarMedicoUseCase;
import application.usecases.CadastrarPacienteUseCase;
import domain.appointment.Consulta;
import domain.appointment.StatusConsulta;
import domain.appointment.TipoConsulta;
import domain.doctor.Medico;
import domain.patient.Paciente;
import domain.shared.Periodo;
import infrastructure.repository.InMemoryConsultaRepository;
import infrastructure.repository.InMemoryMedicoRepository;
import infrastructure.repository.InMemoryPacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Casos de Uso: Integração Domain + Application")
class AgendarConsultaUseCaseTest {

    private InMemoryPacienteRepository pacienteRepo;
    private InMemoryMedicoRepository medicoRepo;
    private InMemoryConsultaRepository consultaRepo;

    private CadastrarPacienteUseCase cadastrarPaciente;
    private CadastrarMedicoUseCase cadastrarMedico;
    private AgendarConsultaUseCase agendarConsulta;

    private Paciente paciente;
    private Medico medico;
    private LocalDateTime base;

    @BeforeEach
    void setUp() {
        pacienteRepo = new InMemoryPacienteRepository();
        medicoRepo = new InMemoryMedicoRepository();
        consultaRepo = new InMemoryConsultaRepository();

        cadastrarPaciente = new CadastrarPacienteUseCase(pacienteRepo);
        cadastrarMedico = new CadastrarMedicoUseCase(medicoRepo);
        agendarConsulta = new AgendarConsultaUseCase(consultaRepo, pacienteRepo, medicoRepo);

        paciente = cadastrarPaciente.executar(
                "Maria Clara", "529.982.247-25", "maria@teste.com",
                LocalDate.of(1995, 3, 15), "82999990000"
        );

        medico = cadastrarMedico.executar(
                "Dr. João Silva", "CRM/AL 12345", "Clínica Geral", "drjoao@clinica.com"
        );

        base = LocalDateTime.now().plusDays(1);
        Periodo disponibilidade = new Periodo(base, base.plusHours(8));
        medico.adicionarDisponibilidade(disponibilidade);
        medicoRepo.salvar(medico);
    }

    @Test
    @DisplayName("Deve agendar consulta presencial com sucesso")
    void deveAgendarConsultaPresencial() {
        Periodo periodo = new Periodo(base.plusHours(1), base.plusHours(2));
        Consulta consulta = agendarConsulta.executar(
                paciente.getId(), medico.getId(), periodo, TipoConsulta.PRESENCIAL
        );

        assertNotNull(consulta.getId());
        assertEquals(StatusConsulta.AGENDADA, consulta.getStatus());
        assertEquals(TipoConsulta.PRESENCIAL, consulta.getTipo());
    }

    @Test
    @DisplayName("Deve agendar consulta de telemedicina com sucesso")
    void deveAgendarConsultaTelemedicina() {
        Periodo periodo = new Periodo(base.plusHours(2), base.plusHours(3));
        Consulta consulta = agendarConsulta.executar(
                paciente.getId(), medico.getId(), periodo, TipoConsulta.TELEMEDICINA
        );

        assertEquals(TipoConsulta.TELEMEDICINA, consulta.getTipo());
        assertEquals(StatusConsulta.AGENDADA, consulta.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao agendar com paciente inexistente")
    void deveLancarExcecaoParaPacienteInexistente() {
        Periodo periodo = new Periodo(base.plusHours(1), base.plusHours(2));
        assertThrows(IllegalArgumentException.class,
                () -> agendarConsulta.executar("id-invalido", medico.getId(), periodo, TipoConsulta.PRESENCIAL));
    }

    @Test
    @DisplayName("Deve lançar exceção ao agendar com médico fora de disponibilidade")
    void deveLancarExcecaoMedicoForaDisponibilidade() {
        // Período fora da janela de disponibilidade do médico
        LocalDateTime amanha = LocalDateTime.now().plusDays(2);
        Periodo fora = new Periodo(amanha, amanha.plusHours(1));

        assertThrows(IllegalStateException.class,
                () -> agendarConsulta.executar(paciente.getId(), medico.getId(), fora, TipoConsulta.PRESENCIAL));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar paciente com CPF duplicado")
    void deveLancarExcecaoParaCpfDuplicado() {
        assertThrows(IllegalStateException.class,
                () -> cadastrarPaciente.executar(
                        "Outro Nome", "529.982.247-25", "outro@email.com",
                        LocalDate.of(1990, 1, 1), "82988880000"
                ));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar médico com CRM duplicado")
    void deveLancarExcecaoParaCrmDuplicado() {
        assertThrows(IllegalStateException.class,
                () -> cadastrarMedico.executar(
                        "Outro Médico", "CRM/AL 12345", "Dermatologia", "outro@clinica.com"
                ));
    }

    @Test
    @DisplayName("Deve lançar exceção ao agendar em horário com conflito")
    void deveLancarExcecaoParaConflitoDAgenda() {
        Periodo periodo = new Periodo(base.plusHours(1), base.plusHours(2));
        agendarConsulta.executar(paciente.getId(), medico.getId(), periodo, TipoConsulta.PRESENCIAL);

        // Mesma janela, mesmo médico
        assertThrows(IllegalStateException.class,
                () -> agendarConsulta.executar(paciente.getId(), medico.getId(), periodo, TipoConsulta.PRESENCIAL));
    }
}
