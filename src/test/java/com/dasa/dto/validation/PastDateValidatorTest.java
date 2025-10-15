package com.dasa.dto.validation;

import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - PastDateValidator")
public class PastDateValidatorTest {

    private PastDateValidator validator;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeEach
    public void setUp() {
        validator = new PastDateValidator();
        validator.initialize(null);
    }

    @Test
    @DisplayName("Deve validar data no passado")
    public void testDataNoPassado() {
        assertTrue(validator.isValid("15/03/1990", null));
    }

    @Test
    @DisplayName("Deve invalidar data nula")
    public void testDataNula() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    @DisplayName("Deve invalidar data vazia")
    public void testDataVazia() {
        assertFalse(validator.isValid("", null));
    }

    @Test
    @DisplayName("Deve invalidar data de hoje")
    public void testDataHoje() {
        String hoje = LocalDate.now().format(FORMATTER);
        assertFalse(validator.isValid(hoje, null));
    }

    @Test
    @DisplayName("Deve invalidar data no futuro")
    public void testDataFuturo() {
        String futuro = LocalDate.now().plusDays(1).format(FORMATTER);
        assertFalse(validator.isValid(futuro, null));
    }

    @Test
    @DisplayName("Deve validar data de ontem")
    public void testDataOntem() {
        String ontem = LocalDate.now().minusDays(1).format(FORMATTER);
        assertTrue(validator.isValid(ontem, null));
    }

    @Test
    @DisplayName("Deve invalidar ano antes de 1900")
    public void testAnoAntesDe1900() {
        assertFalse(validator.isValid("15/03/1899", null));
    }

    @Test
    @DisplayName("Deve validar ano 1900")
    public void testAno1900() {
        assertTrue(validator.isValid("01/01/1900", null));
    }

    @Test
    @DisplayName("Deve validar ano atual")
    public void testAnoAtual() {
        int anoAtual = LocalDate.now().getYear();
        String dataAnoAtual = "01/01/" + anoAtual;
        // Será inválida porque precisa estar no passado
        assertFalse(validator.isValid(dataAnoAtual, null));
    }

    @Test
    @DisplayName("Deve invalidar formato inválido")
    public void testFormatoInvalido() {
        assertFalse(validator.isValid("1990-03-15", null)); // ISO format
    }

    @Test
    @DisplayName("Deve invalidar data com espaços")
    public void testDataComEspacos() {
        assertFalse(validator.isValid("   ", null));
    }

    @Test
    @DisplayName("Deve validar data válida nos limites")
    public void testDataValidaLimites() {
        assertTrue(validator.isValid("31/12/2023", null));
    }
}