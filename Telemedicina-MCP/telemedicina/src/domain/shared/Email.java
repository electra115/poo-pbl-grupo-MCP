package domain.shared;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object: Email
 * Imutável, encapsula regra de validação de formato de e-mail.
 */
public final class Email {

    private static final Pattern PADRAO_EMAIL =
            Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

    private final String valor;

    public Email(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser nulo ou vazio.");
        }
        if (!PADRAO_EMAIL.matcher(valor.trim()).matches()) {
            throw new IllegalArgumentException("E-mail inválido: " + valor);
        }
        this.valor = valor.trim().toLowerCase();
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return Objects.equals(valor, email.valor);
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
