package infrastructure.repository;

import domain.appointment.Consulta;
import domain.appointment.ConsultaRepository;
import domain.appointment.StatusConsulta;

import java.util.*;

/**
 * Implementação em memória do ConsultaRepository.
 */
public class InMemoryConsultaRepository implements ConsultaRepository {

    private final Map<String, Consulta> armazenamento = new HashMap<>();

    @Override
    public void salvar(Consulta consulta) {
        armazenamento.put(consulta.getId(), consulta);
    }

    @Override
    public Optional<Consulta> buscarPorId(String id) {
        return Optional.ofNullable(armazenamento.get(id));
    }

    @Override
    public List<Consulta> buscarPorPaciente(String pacienteId) {
        return armazenamento.values().stream()
                .filter(c -> c.getPacienteId().equals(pacienteId))
                .toList();
    }

    @Override
    public List<Consulta> buscarPorMedico(String medicoId) {
        return armazenamento.values().stream()
                .filter(c -> c.getMedicoId().equals(medicoId))
                .toList();
    }

    @Override
    public List<Consulta> buscarPorStatus(StatusConsulta status) {
        return armazenamento.values().stream()
                .filter(c -> c.getStatus() == status)
                .toList();
    }
}
