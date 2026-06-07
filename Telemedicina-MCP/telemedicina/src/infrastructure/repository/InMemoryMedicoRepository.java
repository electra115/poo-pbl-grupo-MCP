package infrastructure.repository;

import domain.doctor.Medico;
import domain.doctor.MedicoRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação em memória do MedicoRepository.
 */
public class InMemoryMedicoRepository implements MedicoRepository {

    private final Map<String, Medico> armazenamento = new HashMap<>();

    @Override
    public void salvar(Medico medico) {
        armazenamento.put(medico.getId(), medico);
    }

    @Override
    public Optional<Medico> buscarPorId(String id) {
        return Optional.ofNullable(armazenamento.get(id));
    }

    @Override
    public Optional<Medico> buscarPorCrm(String crm) {
        return armazenamento.values().stream()
                .filter(m -> m.getCrm().getValor().equals(crm))
                .findFirst();
    }

    @Override
    public List<Medico> listarPorEspecialidade(String especialidade) {
        return armazenamento.values().stream()
                .filter(m -> m.getEspecialidade().equalsIgnoreCase(especialidade))
                .toList();
    }

    @Override
    public List<Medico> listarTodos() {
        return List.copyOf(armazenamento.values());
    }
}
