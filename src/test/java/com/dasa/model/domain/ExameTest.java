package com.dasa.model.domain;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - classe Exame")
public class ExameTest {

    private Exame exame;

    @BeforeEach
    public void setUp() {
        exame = new Exame(123, "Hemograma Completo");
    }

    @Test
    @DisplayName("Deve criar exame com ID e nome")
    public void testCriacaoExame() {
        assertEquals(123, exame.getId());
        assertEquals("Hemograma Completo", exame.getNome());
    }

    @Test
    @DisplayName("Deve alterar ID do exame")
    public void testSetId() {
        exame.setId(456);
        assertEquals(456, exame.getId());
    }

    @Test
    @DisplayName("Deve alterar nome do exame")
    public void testSetNome() {
        exame.setNome("Exame de Urina");
        assertEquals("Exame de Urina", exame.getNome());
    }

    @Test
    @DisplayName("Deve formatar toString corretamente")
    public void testToString() {
        String resultado = exame.toString();
        assertTrue(resultado.contains("123"));
        assertTrue(resultado.contains("Hemograma Completo"));
        assertTrue(resultado.contains("ID"));
    }
}