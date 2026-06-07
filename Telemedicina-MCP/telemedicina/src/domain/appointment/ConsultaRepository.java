package domain.appointment;

import java.util.List;
import java.util.Optional;

/**
 * Interface de Repositório: ConsultaRepository
 * Contrato definido no domínio. Implementação na infraestrutura.
 */
public interface ConsultaRepository {
    void salvar(Consulta consulta);
    Optional<Consulta> buscarPorId(String id);
    List<Consulta> buscarPorPaciente(String pacienteId);
    List<Consulta> buscarPorMedico(String medicoId);
    List<Consulta> buscarPorStatus(StatusConsulta status);
}
