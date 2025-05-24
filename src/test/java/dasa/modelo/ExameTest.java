package dasa.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Exame")
public class ExameTest {

    private Exame exame;

    @BeforeEach
    public void setUp() {
        exame = new Exame(123, "Hemograma Completo");
    }

    @Test
    @DisplayName("Deve criar exame com construtor parametrizado")
    public void testConstrutorParametrizado() {
        assertEquals(123, exame.getId());
        assertEquals("Hemograma Completo", exame.getNome());
    }

    @Test
    @DisplayName("Deve definir e obter ID corretamente")
    public void testSetterGetterId() {
        exame.setId(456);
        assertEquals(456, exame.getId());
    }

    @Test
    @DisplayName("Deve definir e obter nome corretamente")
    public void testSetterGetterNome() {
        exame.setNome("Exame de Urina");
        assertEquals("Exame de Urina", exame.getNome());
    }

    @Test
    @DisplayName("Deve retornar string formatada no toString")
    public void testToString() {
        String expected = "ID - 123\n\tExame: Hemograma Completo";
        assertEquals(expected, exame.toString());
    }

    @Test
    @DisplayName("Deve aceitar todos os IDs de exames válidos")
    public void testIdsValidos() {
        int[] ids = {123, 456, 789};
        String[] nomes = {"Hemograma Completo", "Exame de Urina", "Exame de Glicemia"};

        for (int i = 0; i < ids.length; i++) {
            Exame ex = new Exame(ids[i], nomes[i]);
            assertEquals(ids[i], ex.getId());
            assertEquals(nomes[i], ex.getNome());
        }
    }

    @Test
    @DisplayName("Deve aceitar ID como string")
    public void testIdComoString() {
        exame.setId(001);
        assertEquals(001, exame.getId());
        assertTrue(Integer.class.isInstance(exame.getId()));
    }
}