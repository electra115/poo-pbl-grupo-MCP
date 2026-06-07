package application.usecases;

import domain.patient.Paciente;
import domain.patient.PacienteRepository;
import domain.shared.Cpf;
import domain.shared.Email;

import java.time.LocalDate;

/**
 * Caso de Uso: Cadastrar Paciente
 * Garante que não haja duplicidade de CPF no sistema.
 */
public class CadastrarPacienteUseCase {

    private final PacienteRepository pacienteRepository;

    public CadastrarPacienteUseCase(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente executar(String nome, String cpf, String email, LocalDate dataNascimento, String telefone) {
        Cpf cpfVO = new Cpf(cpf);
        Email emailVO = new Email(email);

        pacienteRepository.buscarPorCpf(cpfVO.getValor()).ifPresent(p -> {
            throw new IllegalStateException("Já existe um paciente cadastrado com o CPF: " + cpfVO.getValor());
        });

        Paciente paciente = new Paciente(nome, cpfVO, emailVO, dataNascimento, telefone);
        pacienteRepository.salvar(paciente);
        return paciente;
    }
}
