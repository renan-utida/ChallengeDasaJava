package dasa.funcionarios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Funcionario")
public class FuncionarioTest {

    private Funcionario funcionario;

    @BeforeEach
    public void setUp() {
        funcionario = new Funcionario();
    }

    @Test
    @DisplayName("Deve criar funcionário com construtor padrão")
    public void testConstrutorPadrao() {
        assertNotNull(funcionario);
        assertNull(funcionario.getNome());
        assertEquals(0, funcionario.getRegistro());
    }

    @Test
    @DisplayName("Deve criar funcionário com construtor parametrizado")
    public void testConstrutorParametrizado() {
        Funcionario func = new Funcionario("João Silva", 12345);

        assertEquals("João Silva", func.getNome());
        assertEquals(12345, func.getRegistro());
    }

    @Test
    @DisplayName("Deve definir e obter nome corretamente")
    public void testSetterGetterNome() {
        funcionario.setNome("Maria Santos");
        assertEquals("Maria Santos", funcionario.getNome());
    }

    @Test
    @DisplayName("Deve definir e obter registro corretamente")
    public void testSetterGetterRegistro() {
        funcionario.setRegistro(67890);
        assertEquals(67890, funcionario.getRegistro());
    }

    @Test
    @DisplayName("Deve aceitar nome com acentos")
    public void testNomeComAcentos() {
        funcionario.setNome("José António");
        assertEquals("José António", funcionario.getNome());
    }

    @Test
    @DisplayName("Deve aceitar registro com valor zero")
    public void testRegistroZero() {
        funcionario.setRegistro(0);
        assertEquals(0, funcionario.getRegistro());
    }
}