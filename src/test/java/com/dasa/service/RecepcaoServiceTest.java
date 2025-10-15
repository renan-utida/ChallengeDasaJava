package com.dasa.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - RecepcaoService")
public class RecepcaoServiceTest {

    private RecepcaoService service;

    @BeforeEach
    public void setUp() {
        service = new RecepcaoService();
    }

    @Test
    @DisplayName("Deve validar nome vazio")
    public void testValidarNomeVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarPaciente("", "12345678901", "01/01/1990",
                    false, false, false, "Hemograma Completo");
        });
        assertTrue(exception.getMessage().contains("Nome não pode estar vazio"));
    }

    @Test
    @DisplayName("Deve validar nome muito curto")
    public void testValidarNomeCurto() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarPaciente("AB", "12345678901", "01/01/1990",
                    false, false, false, "Hemograma Completo");
        });
        assertTrue(exception.getMessage().contains("pelo menos 3 caracteres"));
    }

    @Test
    @DisplayName("Deve validar CPF inválido")
    public void testValidarCpfInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarPaciente("João Silva", "123", "01/01/1990",
                    false, false, false, "Hemograma Completo");
        });
        assertTrue(exception.getMessage().contains("11 dígitos"));
    }

    @Test
    @DisplayName("Deve validar CPF com todos dígitos iguais")
    public void testValidarCpfDigitosIguais() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarPaciente("João Silva", "11111111111", "01/01/1990",
                    false, false, false, "Hemograma Completo");
        });
        assertTrue(exception.getMessage().contains("CPF inválido"));
    }

    @Test
    @DisplayName("Deve validar formato de data inválido")
    public void testValidarDataFormatoInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarPaciente("João Silva", "12345678901", "01-01-1990",
                    false, false, false, "Hemograma Completo");
        });
        assertTrue(exception.getMessage().contains("dd/MM/yyyy"));
    }

    @Test
    @DisplayName("Deve validar ano fora do intervalo")
    public void testValidarAnoInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarPaciente("João Silva", "12345678901", "01/01/1899",
                    false, false, false, "Hemograma Completo");
        });
        assertTrue(exception.getMessage().contains("entre 1900 e 2024"));
    }

    @Test
    @DisplayName("Deve validar mês inválido")
    public void testValidarMesInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarPaciente("João Silva", "12345678901", "01/13/2000",
                    false, false, false, "Hemograma Completo");
        });
        assertTrue(exception.getMessage().contains("Mês inválido"));
    }

    @Test
    @DisplayName("Deve validar dia inválido")
    public void testValidarDiaInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarPaciente("João Silva", "12345678901", "32/01/2000",
                    false, false, false, "Hemograma Completo");
        });
        assertTrue(exception.getMessage().contains("Dia inválido"));
    }

    @Test
    @DisplayName("Deve validar status inválido")
    public void testValidarStatusInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.listarAtendimentosPorStatus("StatusInvalido");
        });
        assertTrue(exception.getMessage().contains("Status inválido"));
    }
}