package domain.patient;

import java.util.List;
import java.util.Optional;

/**
 * Interface de Repositório: PacienteRepository
 * Contrato definido no domínio (DDD). A implementação fica na camada de infraestrutura.
 * O domínio não depende de detalhes de persistência.
 */
public interface PacienteRepository {
    void salvar(Paciente paciente);
    Optional<Paciente> buscarPorId(String id);
    Optional<Paciente> buscarPorCpf(String cpf);
    List<Paciente> listarTodos();
    void remover(String id);
}
