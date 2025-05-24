package dasa.funcionarios;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe Enfermeiro")
public class EnfermeiroTest {
    private Enfermeiro enfermeiro;

    @BeforeEach
    public void setUp() {
        enfermeiro = new Enfermeiro("Ana Silva", 741321, "Hemograma Completo");
    }

    @Test
    @DisplayName("Deve criar enfermeiro com nome, COREN e especialidade corretos")
    public void testConstrutorComParametros() {
        assertEquals("Ana Silva", enfermeiro.getNome());
        assertEquals(741321, enfermeiro.getCoren());
        assertEquals("Hemograma Completo", enfermeiro.getEspecialidade());
        assertEquals("741321", enfermeiro.getRegistro());
    }

    @Test
    @DisplayName("Deve alterar COREN e sincronizar com registro")
    public void testSetCoren() {
        enfermeiro.setCoren(999999);
        assertEquals(999999, enfermeiro.getCoren());
        assertEquals("999999", enfermeiro.getRegistro());
    }

    @Test
    @DisplayName("Deve alterar especialidade do enfermeiro")
    public void testSetEspecialidade() {
        enfermeiro.setEspecialidade("Exame de Urina");
        assertEquals("Exame de Urina", enfermeiro.getEspecialidade());
    }

    @Test
    @DisplayName("Deve herdar corretamente da classe Funcionario")
    public void testHerancaFuncionario() {
        assertTrue(enfermeiro instanceof Funcionario);
    }

    @Test
    @DisplayName("Deve criar enfermeiro vazio com construtor padrão")
    public void testConstrutorVazio() {
        Enfermeiro enfermeiroVazio = new Enfermeiro();
        assertNull(enfermeiroVazio.getNome());
        assertEquals(0, enfermeiroVazio.getCoren());
        assertNull(enfermeiroVazio.getEspecialidade());
    }
}