package com.dasa.model.converters;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - BooleanToSimNaoConverter")
public class BooleanToSimNaoConverterTest {

    private BooleanToSimNaoConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new BooleanToSimNaoConverter();
    }

    @Test
    @DisplayName("Deve converter true para S")
    public void testConvertTrueParaS() {
        String resultado = converter.convertToDatabaseColumn(true);
        assertEquals("S", resultado);
    }

    @Test
    @DisplayName("Deve converter false para N")
    public void testConvertFalseParaN() {
        String resultado = converter.convertToDatabaseColumn(false);
        assertEquals("N", resultado);
    }

    @Test
    @DisplayName("Deve converter null para N")
    public void testConvertNullParaN() {
        String resultado = converter.convertToDatabaseColumn(null);
        assertEquals("N", resultado);
    }

    @Test
    @DisplayName("Deve converter S para true")
    public void testConvertSParaTrue() {
        Boolean resultado = converter.convertToEntityAttribute("S");
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve converter N para false")
    public void testConvertNParaFalse() {
        Boolean resultado = converter.convertToEntityAttribute("N");
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve converter s minúsculo para true")
    public void testConvertSMinusculoParaTrue() {
        Boolean resultado = converter.convertToEntityAttribute("s");
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve converter n minúsculo para false")
    public void testConvertNMinusculoParaFalse() {
        Boolean resultado = converter.convertToEntityAttribute("n");
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve converter null para false")
    public void testConvertNullParaFalse() {
        Boolean resultado = converter.convertToEntityAttribute(null);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve converter string vazia para false")
    public void testConvertVazioParaFalse() {
        Boolean resultado = converter.convertToEntityAttribute("");
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve converter string com espaços para false")
    public void testConvertEspacosParaFalse() {
        Boolean resultado = converter.convertToEntityAttribute("   ");
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve converter S com espaços para true")
    public void testConvertSComEspacosParaTrue() {
        Boolean resultado = converter.convertToEntityAttribute(" S ");
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve converter qualquer valor diferente de S para false")
    public void testConvertValorInvalidoParaFalse() {
        Boolean resultado = converter.convertToEntityAttribute("X");
        assertFalse(resultado);
    }
}