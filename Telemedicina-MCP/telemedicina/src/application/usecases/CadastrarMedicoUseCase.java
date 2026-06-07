package application.usecases;

import domain.doctor.Medico;
import domain.doctor.MedicoRepository;
import domain.shared.Crm;
import domain.shared.Email;

/**
 * Caso de Uso: Cadastrar Médico
 * Garante unicidade de CRM no sistema.
 */
public class CadastrarMedicoUseCase {

    private final MedicoRepository medicoRepository;

    public CadastrarMedicoUseCase(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public Medico executar(String nome, String crm, String especialidade, String email) {
        Crm crmVO = new Crm(crm);
        Email emailVO = new Email(email);

        medicoRepository.buscarPorCrm(crmVO.getValor()).ifPresent(m -> {
            throw new IllegalStateException("Já existe um médico cadastrado com o CRM: " + crmVO.getValor());
        });

        Medico medico = new Medico(nome, crmVO, especialidade, emailVO);
        medicoRepository.salvar(medico);
        return medico;
    }
}
