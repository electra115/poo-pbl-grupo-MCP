package domain.appointment;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value Object: ReceitaMedica (Digital)
 * Imutável após emissão. Encapsula os dados de uma receita médica digital.
 */
public final class ReceitaMedica {

    private final String medicoId;
    private final String medicoNome;
    private final String pacienteNome;
    private final List<String> medicamentos;
    private final String instrucoes;
    private final LocalDate dataEmissao;
    private final LocalDate dataValidade;

    public ReceitaMedica(
            String medicoId,
            String medicoNome,
            String pacienteNome,
            List<String> medicamentos,
            String instrucoes,
            LocalDate dataValidade
    ) {
        if (medicoId == null || medicoId.isBlank())
            throw new IllegalArgumentException("ID do médico é obrigatório na receita.");
        if (medicoNome == null || medicoNome.isBlank())
            throw new IllegalArgumentException("Nome do médico é obrigatório na receita.");
        if (pacienteNome == null || pacienteNome.isBlank())
            throw new IllegalArgumentException("Nome do paciente é obrigatório na receita.");
        if (medicamentos == null || medicamentos.isEmpty())
            throw new IllegalArgumentException("Receita deve conter ao menos um medicamento.");
        if (instrucoes == null || instrucoes.isBlank())
            throw new IllegalArgumentException("Instruções de uso não podem ser vazias.");
        if (dataValidade == null || dataValidade.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Data de validade deve ser hoje ou futura.");

        this.medicoId = medicoId;
        this.medicoNome = medicoNome;
        this.pacienteNome = pacienteNome;
        this.medicamentos = Collections.unmodifiableList(List.copyOf(medicamentos));
        this.instrucoes = instrucoes;
        this.dataEmissao = LocalDate.now();
        this.dataValidade = dataValidade;
    }

    public String getMedicoId() { return medicoId; }
    public String getMedicoNome() { return medicoNome; }
    public String getPacienteNome() { return pacienteNome; }
    public List<String> getMedicamentos() { return medicamentos; }
    public String getInstrucoes() { return instrucoes; }
    public LocalDate getDataEmissao() { return dataEmissao; }
    public LocalDate getDataValidade() { return dataValidade; }

    public boolean isValida() {
        return !LocalDate.now().isAfter(dataValidade);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceitaMedica)) return false;
        ReceitaMedica that = (ReceitaMedica) o;
        return Objects.equals(medicoId, that.medicoId) &&
               Objects.equals(pacienteNome, that.pacienteNome) &&
               Objects.equals(dataEmissao, that.dataEmissao) &&
               Objects.equals(medicamentos, that.medicamentos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicoId, pacienteNome, dataEmissao, medicamentos);
    }
}
