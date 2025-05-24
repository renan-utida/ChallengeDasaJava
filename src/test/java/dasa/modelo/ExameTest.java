package dasa.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class ExameTest {
    private Exame exame;

    @BeforeEach
    public void setUp() {
        exame = new Exame(123, "Hemograma Completo");
    }

    @Test
    public void testConstrutorComParametros() {
        assertEquals(123, exame.getId());
        assertEquals("Hemograma Completo", exame.getNome());
    }

    @Test
    public void testSettersGetters() {
        exame.setId(456);
        exame.setNome("Exame de Urina");

        assertEquals(456, exame.getId());
        assertEquals("Exame de Urina", exame.getNome());
    }

    @Test
    public void testToString() {
        String resultado = exame.toString();
        assertTrue(resultado.contains(String.valueOf(123)));
        assertTrue(resultado.contains("Hemograma Completo"));
        assertTrue(resultado.contains("ID -"));
        assertTrue(resultado.contains("Exame:"));
    }
}