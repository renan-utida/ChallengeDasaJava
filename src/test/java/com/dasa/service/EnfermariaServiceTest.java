package com.dasa.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - EnfermariaService")
public class EnfermariaServiceTest {

    private EnfermariaService service;

    @BeforeEach
    public void setUp() {
        service = new EnfermariaService();
    }

    @Test
    @DisplayName("Deve validar especialidade vazia")
    public void testValidarEspecialidadeVazia() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.listarEnfermeirosPorEspecialidade("");
        });
        assertTrue(exception.getMessage().contains("não pode estar vazia"));
    }

    @Test
    @DisplayName("Deve validar especialidade nula")
    public void testValidarEspecialidadeNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.listarEnfermeirosPorEspecialidade(null);
        });
        assertTrue(exception.getMessage().contains("não pode estar vazia"));
    }

    @Test
    @DisplayName("Deve validar especialidade inválida")
    public void testValidarEspecialidadeInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.listarEnfermeirosPorEspecialidade("Especialidade Inexistente");
        });
        assertTrue(exception.getMessage().contains("Especialidade inválida"));
    }

    @Test
    @DisplayName("Deve validar COREN inválido")
    public void testValidarCorenInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.buscarEnfermeiroPorCoren(0);
        });
        assertTrue(exception.getMessage().contains("COREN inválido"));
    }

    @Test
    @DisplayName("Deve validar COREN negativo")
    public void testValidarCorenNegativo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.buscarEnfermeiroPorCoren(-1);
        });
        assertTrue(exception.getMessage().contains("COREN inválido"));
    }

    @Test
    @DisplayName("Deve aceitar especialidades válidas")
    public void testEspecialidadesValidas() {
        // Não deve lançar exceção para especialidades válidas
        assertDoesNotThrow(() -> {
            // Isso só funcionaria se houvesse enfermeiros cadastrados
            service.listarEnfermeirosPorEspecialidade("Hemograma Completo");
            service.listarEnfermeirosPorEspecialidade("Exame de Urina");
            service.listarEnfermeirosPorEspecialidade("Exame de Glicemia");
        });
    }
}