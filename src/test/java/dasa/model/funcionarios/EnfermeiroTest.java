package dasa.model.funcionarios;

import com.dasa.model.funcionarios.Enfermeiro;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@DisplayName("Testes - classe Enfermeiro")
public class EnfermeiroTest {

    private Enfermeiro enfermeiro;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        enfermeiro = new Enfermeiro("Ana Silva", 741321, "Hemograma Completo");
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Deve criar enfermeiro com construtor padrão")
    public void testConstrutorPadrao() {
        Enfermeiro e = new Enfermeiro();
        assertNotNull(e);
    }

    @Test
    @DisplayName("Deve criar enfermeiro com parâmetros")
    public void testConstrutorCompleto() {
        assertEquals("Ana Silva", enfermeiro.getNome());
        assertEquals(741321, enfermeiro.getCoren());
        assertEquals("Hemograma Completo", enfermeiro.getEspecialidade());
        assertEquals(741321, enfermeiro.getRegistro());
    }

    @Test
    @DisplayName("Deve sincronizar COREN com registro")
    public void testSincronizacaoCorenRegistro() {
        enfermeiro.setCoren(999999);
        assertEquals(999999, enfermeiro.getCoren());
        assertEquals(999999, enfermeiro.getRegistro());
    }

    @Test
    @DisplayName("Deve alterar especialidade")
    public void testAlterarEspecialidade() {
        enfermeiro.setEspecialidade("Exame de Urina");
        assertEquals("Exame de Urina", enfermeiro.getEspecialidade());
    }

    @Test
    @DisplayName("Deve validar especialidades permitidas")
    public void testEspecialidadesPermitidas() {
        String[] especialidadesValidas = {
                "Hemograma Completo",
                "Exame de Urina",
                "Exame de Glicemia"
        };

        for (String esp : especialidadesValidas) {
            enfermeiro.setEspecialidade(esp);
            assertEquals(esp, enfermeiro.getEspecialidade());
        }
    }

    @Test
    @DisplayName("Deve apresentar corretamente")
    public void testApresentar() {
        enfermeiro.apresentar();
        String output = outputStream.toString();

        assertTrue(output.contains("COREN: 741321"));
        assertTrue(output.contains("Ana Silva"));
        assertTrue(output.contains("Hemograma Completo"));
    }

    @Test
    @DisplayName("Deve manter herança de Funcionario")
    public void testHeranca() {
        enfermeiro.setNome("Maria Santos");
        assertEquals("Maria Santos", enfermeiro.getNome());

        enfermeiro.setRegistro(555555);
        assertEquals(555555, enfermeiro.getRegistro());
    }

    @Test
    @DisplayName("Deve testar diferentes CORENs")
    public void testDiferentesCoren() {
        Enfermeiro e1 = new Enfermeiro("Enfermeiro 1", 111111, "Exame de Urina");
        Enfermeiro e2 = new Enfermeiro("Enfermeiro 2", 222222, "Exame de Glicemia");

        assertNotEquals(e1.getCoren(), e2.getCoren());
        assertNotEquals(e1.getEspecialidade(), e2.getEspecialidade());
    }
}