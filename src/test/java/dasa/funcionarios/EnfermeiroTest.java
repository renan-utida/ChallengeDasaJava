package dasa.funcionarios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Enfermeiro")
public class EnfermeiroTest {

    private Enfermeiro enfermeiro;

    @BeforeEach
    public void setUp() {
        enfermeiro = new Enfermeiro();
    }

    @Test
    @DisplayName("Deve criar enfermeiro com construtor padrão")
    public void testConstrutorPadrao() {
        assertNotNull(enfermeiro);
        assertNull(enfermeiro.getNome());
        assertEquals(0, enfermeiro.getCoren());
        assertNull(enfermeiro.getEspecialidade());
    }

    @Test
    @DisplayName("Deve criar enfermeiro com construtor parametrizado")
    public void testConstrutorParametrizado() {
        Enfermeiro enf = new Enfermeiro("Ana Silva", 741321, "Hemograma Completo");

        assertEquals("Ana Silva", enf.getNome());
        assertEquals(741321, enf.getCoren());
        assertEquals("Hemograma Completo", enf.getEspecialidade());
        assertEquals(741321, enf.getRegistro());
    }

    @Test
    @DisplayName("Deve definir e obter COREN corretamente")
    public void testSetterGetterCoren() {
        enfermeiro.setCoren(852431);
        assertEquals(852431, enfermeiro.getCoren());
        assertEquals(852431, enfermeiro.getRegistro()); // Deve sincronizar
    }

    @Test
    @DisplayName("Deve definir e obter especialidade corretamente")
    public void testSetterGetterEspecialidade() {
        enfermeiro.setEspecialidade("Exame de Urina");
        assertEquals("Exame de Urina", enfermeiro.getEspecialidade());
    }

    @Test
    @DisplayName("Deve sincronizar COREN com registro da classe pai")
    public void testSincronizacaoCorenRegistro() {
        enfermeiro.setCoren(963541);
        assertEquals(enfermeiro.getCoren(), enfermeiro.getRegistro());
    }

    @Test
    @DisplayName("Deve aceitar todas as especialidades válidas")
    public void testEspecialidadesValidas() {
        String[] especialidades = {"Hemograma Completo", "Exame de Urina", "Exame de Glicemia"};

        for (String esp : especialidades) {
            enfermeiro.setEspecialidade(esp);
            assertEquals(esp, enfermeiro.getEspecialidade());
        }
    }

    @Test
    @DisplayName("Deve aceitar COREN com 6 dígitos")
    public void testCorenSeisDigitos() {
        enfermeiro.setCoren(741321);
        assertEquals(741321, enfermeiro.getCoren());
    }
}