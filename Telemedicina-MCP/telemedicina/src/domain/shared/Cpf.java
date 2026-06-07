package domain.shared;

import java.util.Objects;

/**
 * Value Object: CPF
 * Imutável, sem identidade própria, carrega regra de validação de negócio.
 */
public final class Cpf {

    private final String valor;

    public Cpf(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio.");
        }
        String apenasDigitos = valor.replaceAll("[^0-9]", "");
        if (!isValido(apenasDigitos)) {
            throw new IllegalArgumentException("CPF inválido: " + valor);
        }
        this.valor = formatarCpf(apenasDigitos);
    }

    private static boolean isValido(String cpf) {
        if (cpf.length() != 11) return false;
        if (cpf.chars().distinct().count() == 1) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * (10 - i);
        int primeiro = 11 - (soma % 11);
        if (primeiro >= 10) primeiro = 0;
        if (primeiro != (cpf.charAt(9) - '0')) return false;

        soma = 0;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * (11 - i);
        int segundo = 11 - (soma % 11);
        if (segundo >= 10) segundo = 0;
        return segundo == (cpf.charAt(10) - '0');
    }

    private static String formatarCpf(String digits) {
        return digits.substring(0, 3) + "." +
               digits.substring(3, 6) + "." +
               digits.substring(6, 9) + "-" +
               digits.substring(9);
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cpf)) return false;
        Cpf cpf = (Cpf) o;
        return Objects.equals(valor, cpf.valor);
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
