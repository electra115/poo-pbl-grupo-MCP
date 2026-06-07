package infrastructure.repository;

import domain.patient.Paciente;
import domain.patient.PacienteRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação em memória do PacienteRepository.
 * Camada de infraestrutura — isolada do domínio (DDD).
 */
public class InMemoryPacienteRepository implements PacienteRepository {

    private final Map<String, Paciente> armazenamento = new HashMap<>();

    @Override
    public void salvar(Paciente paciente) {
        armazenamento.put(paciente.getId(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPorId(String id) {
        return Optional.ofNullable(armazenamento.get(id));
    }

    @Override
    public Optional<Paciente> buscarPorCpf(String cpf) {
        return armazenamento.values().stream()
                .filter(p -> p.getCpf().getValor().equals(cpf))
                .findFirst();
    }

    @Override
    public List<Paciente> listarTodos() {
        return List.copyOf(armazenamento.values());
    }

    @Override
    public void remover(String id) {
        armazenamento.remove(id);
    }
}
