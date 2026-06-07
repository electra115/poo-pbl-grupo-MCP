package application.usecases;

import domain.appointment.Consulta;
import domain.appointment.ConsultaRepository;
import domain.appointment.TipoConsulta;
import domain.doctor.Medico;
import domain.doctor.MedicoRepository;
import domain.patient.Paciente;
import domain.patient.PacienteRepository;
import domain.shared.Periodo;

import java.util.List;

/**
 * Caso de Uso: Agendar Consulta
 * Orquestra as regras de negócio entre Paciente, Médico e Consulta.
 * Não contém lógica de domínio — delega ao domínio.
 */
public class AgendarConsultaUseCase {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public AgendarConsultaUseCase(
            ConsultaRepository consultaRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository
    ) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    public Consulta executar(String pacienteId, String medicoId, Periodo periodo, TipoConsulta tipo) {
        Paciente paciente = pacienteRepository.buscarPorId(pacienteId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado: " + pacienteId));

        Medico medico = medicoRepository.buscarPorId(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado: " + medicoId));

        if (!medico.estaDisponivel(periodo)) {
            throw new IllegalStateException("Médico " + medico.getNome() + " não está disponível no período solicitado.");
        }

        verificarConflitosDeAgenda(medicoId, periodo);

        Consulta consulta = new Consulta(paciente.getId(), medico.getId(), periodo, tipo);
        consultaRepository.salvar(consulta);
        return consulta;
    }

    private void verificarConflitosDeAgenda(String medicoId, Periodo periodo) {
        List<Consulta> consultasDoMedico = consultaRepository.buscarPorMedico(medicoId);
        boolean conflito = consultasDoMedico.stream()
                .filter(c -> c.getStatus() != domain.appointment.StatusConsulta.CANCELADA)
                .anyMatch(c -> c.getPeriodo().conflitaCom(periodo));

        if (conflito) {
            throw new IllegalStateException("O médico já possui uma consulta agendada nesse horário.");
        }
    }
}
