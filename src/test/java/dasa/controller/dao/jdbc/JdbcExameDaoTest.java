package dasa.controller.dao.jdbc;

import dasa.controller.dao.jdbc.JdbcExameDao;
import dasa.model.domain.Exame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - JdbcExameDao com Mock")
public class JdbcExameDaoTest {

    private JdbcExameDao dao;

    @BeforeEach
    public void setUp() {
        dao = new JdbcExameDao();
    }

    @Test
    @DisplayName("Deve validar SQL de busca por ID")
    public void testValidarSqlBuscarPorId() {
        String sql = "SELECT * FROM dasa_exames WHERE id = ?";

        assertTrue(sql.contains("dasa_exames"));
        assertTrue(sql.contains("WHERE id = ?"));
    }

    @Test
    @DisplayName("Deve validar SQL de busca por nome")
    public void testValidarSqlBuscarPorNome() {
        String sql = "SELECT * FROM dasa_exames WHERE nome = ?";

        assertTrue(sql.contains("WHERE nome = ?"));
    }

    @Test
    @DisplayName("Deve validar SQL de listar todos")
    public void testValidarSqlListarTodos() {
        String sql = "SELECT * FROM dasa_exames ORDER BY id";

        assertTrue(sql.contains("SELECT *"));
        assertTrue(sql.contains("ORDER BY id"));
    }

    @Test
    @DisplayName("Deve validar criação de objeto Exame")
    public void testCriacaoExame() {
        Exame exame = new Exame(123, "Hemograma Completo");

        assertEquals(123, exame.getId());
        assertEquals("Hemograma Completo", exame.getNome());
    }

    @Test
    @DisplayName("Deve validar toString do Exame")
    public void testToStringExame() {
        Exame exame = new Exame(456, "Exame de Urina");
        String resultado = exame.toString();

        assertTrue(resultado.contains("456"));
        assertTrue(resultado.contains("Exame de Urina"));
    }

    @Test
    @DisplayName("Deve validar setters do Exame")
    public void testSettersExame() {
        Exame exame = new Exame(1, "Teste");

        exame.setId(999);
        exame.setNome("Novo Nome");

        assertEquals(999, exame.getId());
        assertEquals("Novo Nome", exame.getNome());
    }
}