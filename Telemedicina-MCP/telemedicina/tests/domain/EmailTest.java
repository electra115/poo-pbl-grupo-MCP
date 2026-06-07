package domain;

import domain.shared.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VO: Email - Validações de Regra de Negócio")
class EmailTest {

    @Test
    @DisplayName("Deve criar e-mail válido normalizando para minúsculo")
    void deveCriarEmailValidoNormalizandoParaMinusculo() {
        Email email = new Email("Maria@CLINICA.com.br");
        assertEquals("maria@clinica.com.br", email.getValor());
    }

    @Test
    @DisplayName("Dois emails com mesmo valor devem ser iguais (Value Object)")
    void doisEmailsIguaisDevemSerIguais() {
        Email e1 = new Email("teste@email.com");
        Email e2 = new Email("TESTE@EMAIL.COM");
        assertEquals(e1, e2);
    }

    @Test
    @DisplayName("Deve lançar exceção para email nulo")
    void deveLancarExcecaoParaEmailNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"semArroba", "@semdominio.com", "sem@ponto", "", "   "})
    @DisplayName("Deve lançar exceção para formatos inválidos de email")
    void deveLancarExcecaoParaEmailsInvalidos(String emailInvalido) {
        assertThrows(IllegalArgumentException.class, () -> new Email(emailInvalido));
    }
}
