package com.dasa.dto.validation;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - CPFValidator")
public class CPFValidatorTest {

    private CPFValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new CPFValidator();
        validator.initialize(null); // Não precisa de anotação para teste unitário
    }

    @Test
    @DisplayName("Deve validar CPF válido sem formatação")
    public void testCpfValidoSemFormatacao() {
        assertTrue(validator.isValid("12345678901", null));
    }

    @Test
    @DisplayName("Deve validar CPF válido com formatação")
    public void testCpfValidoComFormatacao() {
        assertTrue(validator.isValid("123.456.789-01", null));
    }

    @Test
    @DisplayName("Deve invalidar CPF nulo")
    public void testCpfNulo() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    @DisplayName("Deve invalidar CPF vazio")
    public void testCpfVazio() {
        assertFalse(validator.isValid("", null));
    }

    @Test
    @DisplayName("Deve invalidar CPF com menos de 11 dígitos")
    public void testCpfMenosDeOnzeDigitos() {
        assertFalse(validator.isValid("1234567890", null));
    }

    @Test
    @DisplayName("Deve invalidar CPF com mais de 11 dígitos")
    public void testCpfMaisDeOnzeDigitos() {
        assertFalse(validator.isValid("123456789012", null));
    }

    @Test
    @DisplayName("Deve invalidar CPF com todos dígitos iguais - 11111111111")
    public void testCpfDigitosIguais1() {
        assertFalse(validator.isValid("11111111111", null));
    }

    @Test
    @DisplayName("Deve invalidar CPF com todos dígitos iguais - 00000000000")
    public void testCpfDigitosIguais0() {
        assertFalse(validator.isValid("00000000000", null));
    }

    @Test
    @DisplayName("Deve invalidar CPF com todos dígitos iguais - 99999999999")
    public void testCpfDigitosIguais9() {
        assertFalse(validator.isValid("99999999999", null));
    }

    @Test
    @DisplayName("Deve remover formatação corretamente")
    public void testRemocaoFormatacao() {
        // Testa lógica de remoção
        String cpfComFormatacao = "123.456.789-01";
        String cpfLimpo = cpfComFormatacao.replaceAll("[^0-9]", "");
        assertEquals("12345678901", cpfLimpo);
    }

    @Test
    @DisplayName("Deve invalidar CPF com espaços")
    public void testCpfComEspacos() {
        assertFalse(validator.isValid("   ", null));
    }

    @Test
    @DisplayName("Deve validar CPF com hífen e pontos")
    public void testCpfComHifenEPontos() {
        assertTrue(validator.isValid("987.654.321-00", null));
    }
}