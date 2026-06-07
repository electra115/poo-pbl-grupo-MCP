package domain.appointment;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object: LinkVideoconferencia
 * Imutável. Encapsula e valida o link de chamada para telemedicina.
 */
public final class LinkVideoconferencia {

    private static final Pattern PADRAO_URL =
            Pattern.compile("^(https?://)([\\w\\-]+\\.)+[\\w]{2,}(/.*)?$");

    private final String url;

    public LinkVideoconferencia(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Link de videoconferência não pode ser vazio.");
        }
        if (!PADRAO_URL.matcher(url.trim()).matches()) {
            throw new IllegalArgumentException("Link de videoconferência inválido: " + url);
        }
        this.url = url.trim();
    }

    public String getUrl() { return url; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkVideoconferencia)) return false;
        LinkVideoconferencia that = (LinkVideoconferencia) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() { return Objects.hash(url); }

    @Override
    public String toString() { return url; }
}
