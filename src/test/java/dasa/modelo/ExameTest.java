package dasa.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe Exame")
public class ExameTest {
    private Exame exame;

    @BeforeEach
    public void setUp() {
        exame = new Exame(123, "Hemograma Completo");
    }

    @Test
    @DisplayName("Deve criar exame com ID e nome corretos")
    public void testConstrutorComParametros() {
        assertEquals(123, exame.getId());
        assertEquals("Hemograma Completo", exame.getNome());
    }

    @Test
    @DisplayName("Deve alterar ID e nome do exame através dos setters")
    public void testSettersGetters() {
        exame.setId(456);
        exame.setNome("Exame de Urina");

        assertEquals(456, exame.getId());
        assertEquals("Exame de Urina", exame.getNome());
    }

    @Test
    @DisplayName("Deve formatar exame corretamente no toString")
    public void testToString() {
        String resultado = exame.toString();
        assertTrue(resultado.contains(String.valueOf(123)));
        assertTrue(resultado.contains("Hemograma Completo"));
        assertTrue(resultado.contains("ID -"));
        assertTrue(resultado.contains("Exame:"));
    }
}