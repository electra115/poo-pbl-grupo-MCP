package domain;

import domain.shared.Crm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VO: Crm - Validações de Regra de Negócio")
class CrmTest {

    @Test
    @DisplayName("Deve criar CRM válido e normalizar para maiúsculo")
    void deveCriarCrmValido() {
        Crm crm = new Crm("crm/sp 123456");
        assertEquals("CRM/SP 123456", crm.getValor());
    }

    @Test
    @DisplayName("Dois CRMs com mesmo valor devem ser iguais (Value Object)")
    void doisCrmsIguaisDevemSerIguais() {
        Crm c1 = new Crm("CRM/AL 9999");
        Crm c2 = new Crm("crm/al 9999");
        assertEquals(c1, c2);
    }

    @Test
    @DisplayName("Deve lançar exceção para CRM nulo")
    void deveLancarExcecaoParaCrmNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Crm(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "SP 123456", "CRM-SP-123456", "CRM/SPP 123"})
    @DisplayName("Deve lançar exceção para formatos inválidos de CRM")
    void deveLancarExcecaoParaCrmsInvalidos(String crmInvalido) {
        assertThrows(IllegalArgumentException.class, () -> new Crm(crmInvalido));
    }
}
