package dasa.funcionarios;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Funcionario")
public class FuncionarioTest {
    private Funcionario funcionario;

    @BeforeEach
    public void setUp() {
        funcionario = new Funcionario("João Silva", "12345");
    }

    @Test
    @DisplayName("Deve criar funcionário com parâmetros corretos")
    public void testConstrutorComParametros() {
        assertEquals("João Silva", funcionario.getNome());
        assertEquals("12345", funcionario.getRegistro());
    }

    @Test
    @DisplayName("Deve criar funcionário vazio com construtor padrão")
    public void testConstrutorVazio() {
        Funcionario funcionarioVazio = new Funcionario();
        assertNull(funcionarioVazio.getNome());
        assertNull(funcionarioVazio.getRegistro());
    }

    @Test
    @DisplayName("Deve alterar nome e registro através dos setters")
    public void testSettersGetters() {
        funcionario.setNome("Maria Santos");
        funcionario.setRegistro("67890");

        assertEquals("Maria Santos", funcionario.getNome());
        assertEquals("67890", funcionario.getRegistro());
    }
}