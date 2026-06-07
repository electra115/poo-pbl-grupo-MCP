package domain.doctor;

import java.util.List;
import java.util.Optional;

/**
 * Interface de Repositório: MedicoRepository
 * Contrato definido no domínio. Implementação na infraestrutura.
 */
public interface MedicoRepository {
    void salvar(Medico medico);
    Optional<Medico> buscarPorId(String id);
    Optional<Medico> buscarPorCrm(String crm);
    List<Medico> listarPorEspecialidade(String especialidade);
    List<Medico> listarTodos();
}
