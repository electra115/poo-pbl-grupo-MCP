package domain;

import domain.shared.Cpf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VO: Cpf - Validações de Regra de Negócio")
class CpfTest {

    // ========== CENÁRIOS DE SUCESSO ==========

    @Test
    @DisplayName("Deve criar CPF válido com pontuação")
    void deveCriarCpfValidoComPontuacao() {
        Cpf cpf = new Cpf("529.982.247-25");
        assertEquals("529.982.247-25", cpf.getValor());
    }

    @Test
    @DisplayName("Deve criar CPF válido somente com dígitos")
    void deveCriarCpfValidoSomenteDigitos() {
        Cpf cpf = new Cpf("52998224725");
        assertEquals("529.982.247-25", cpf.getValor());
    }

    @Test
    @DisplayName("Dois CPFs com mesmo valor devem ser iguais (Value Object)")
    void doisCpfsComMesmoValorDevemSerIguais() {
        Cpf cpf1 = new Cpf("529.982.247-25");
        Cpf cpf2 = new Cpf("52998224725");
        assertEquals(cpf1, cpf2);
        assertEquals(cpf1.hashCode(), cpf2.hashCode());
    }

    // ========== CENÁRIOS DE FALHA ==========

    @Test
    @DisplayName("Deve lançar exceção para CPF nulo")
    void deveLancarExcecaoParaCpfNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Cpf(null));
        assertTrue(ex.getMessage().contains("nulo ou vazio"));
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF vazio")
    void deveLancarExcecaoParaCpfVazio() {
        assertThrows(IllegalArgumentException.class, () -> new Cpf(""));
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF com todos os dígitos iguais")
    void deveLancarExcecaoParaCpfComDigitosIguais() {
        assertThrows(IllegalArgumentException.class, () -> new Cpf("111.111.111-11"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123.456.789-00", "000.000.000-00", "12345678", "abcdefghijk"})
    @DisplayName("Deve lançar exceção para CPFs inválidos")
    void deveLancarExcecaoParaCpfsInvalidos(String cpfInvalido) {
        assertThrows(IllegalArgumentException.class, () -> new Cpf(cpfInvalido));
    }
}
