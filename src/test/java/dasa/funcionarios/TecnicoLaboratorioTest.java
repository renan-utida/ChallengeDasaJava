package dasa.funcionarios;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe TecnicoLaboratorio")
public class TecnicoLaboratorioTest {
    private TecnicoLaboratorio tecnico;

    @BeforeEach
    public void setUp() {
        tecnico = new TecnicoLaboratorio("João Silva", 12345);
    }

    @Test
    @DisplayName("Deve criar técnico com nome e CRBM corretos")
    public void testConstrutorComParametros() {
        assertEquals("João Silva", tecnico.getNome());
        assertEquals(12345, tecnico.getCrbm());
        assertEquals("12345", tecnico.getRegistro());
    }

    @Test
    @DisplayName("Deve alterar CRBM e sincronizar com registro")
    public void testSetCrbm() {
        tecnico.setCrbm(99999);
        assertEquals(99999, tecnico.getCrbm());
        assertEquals("99999", tecnico.getRegistro());
    }

    @Test
    @DisplayName("Deve criar técnico vazio com construtor padrão")
    public void testConstrutorVazio() {
        TecnicoLaboratorio tecnicoVazio = new TecnicoLaboratorio();
        assertNull(tecnicoVazio.getNome());
        assertEquals(0, tecnicoVazio.getCrbm());
    }

    @Test
    @DisplayName("Deve herdar corretamente da classe Funcionario")
    public void testHerancaFuncionario() {
        assertTrue(tecnico instanceof Funcionario);
    }
}