package domain.shared;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object: CRM (Conselho Regional de Medicina)
 * Imutável, representa o número de registro do médico.
 * Formato: CRM/UF XXXXXX (ex: CRM/SP 123456)
 */
public final class Crm {

    private static final Pattern PADRAO_CRM =
            Pattern.compile("^CRM/[A-Z]{2}\\s\\d{4,6}$");

    private final String valor;

    public Crm(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("CRM não pode ser nulo ou vazio.");
        }
        String normalizado = valor.trim().toUpperCase();
        if (!PADRAO_CRM.matcher(normalizado).matches()) {
            throw new IllegalArgumentException(
                "CRM inválido. Formato esperado: CRM/UF XXXXXX (ex: CRM/SP 123456). Recebido: " + valor
            );
        }
        this.valor = normalizado;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Crm)) return false;
        Crm crm = (Crm) o;
        return Objects.equals(valor, crm.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
