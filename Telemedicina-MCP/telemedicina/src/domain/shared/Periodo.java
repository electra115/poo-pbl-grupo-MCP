package domain.shared;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object: Periodo
 * Representa um intervalo de tempo imutável com início e fim.
 * Encapsula as regras de validação de período.
 */
public final class Periodo {

    private final LocalDateTime inicio;
    private final LocalDateTime fim;

    public Periodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null) throw new IllegalArgumentException("Data/hora de início não pode ser nula.");
        if (fim == null) throw new IllegalArgumentException("Data/hora de fim não pode ser nula.");
        if (!fim.isAfter(inicio)) {
            throw new IllegalArgumentException("O fim do período deve ser posterior ao início.");
        }
        this.inicio = inicio;
        this.fim = fim;
    }

    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }

    public boolean conflitaCom(Periodo outro) {
        return this.inicio.isBefore(outro.fim) && this.fim.isAfter(outro.inicio);
    }

    public boolean isNoFuturo() {
        return inicio.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Periodo)) return false;
        Periodo periodo = (Periodo) o;
        return Objects.equals(inicio, periodo.inicio) && Objects.equals(fim, periodo.fim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inicio, fim);
    }

    @Override
    public String toString() {
        return "Período[" + inicio + " → " + fim + "]";
    }
}
