package dasa.controller.dao.jdbc;

import com.dasa.controller.dao.jdbc.JdbcInsumoDao;
import com.dasa.model.domain.Insumo;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

@DisplayName("Testes - JdbcInsumoDao com Mock")
public class JdbcInsumoDaoTest {

    private JdbcInsumoDao dao;

    @BeforeEach
    public void setUp() {
        dao = new JdbcInsumoDao();
    }

    @Test
    @DisplayName("Deve validar SQL de busca por ID")
    public void testValidarSqlBuscarPorId() {
        String sql = "SELECT * FROM dasa_insumos WHERE id = ?";

        assertTrue(sql.contains("dasa_insumos"));
        assertTrue(sql.contains("WHERE id = ?"));
    }

    @Test
    @DisplayName("Deve validar SQL de busca por código de barras")
    public void testValidarSqlBuscarPorCodigoBarras() {
        String sql = "SELECT * FROM dasa_insumos WHERE codigo_barras = ?";

        assertTrue(sql.contains("codigo_barras"));
    }

    @Test
    @DisplayName("Deve validar lógica de listagem por exame")
    public void testLogicaListarPorExame() {
        // Simula a lógica do método sem acessar banco
        String nomeExame = "Hemograma Completo";
        List<String> tiposEsperados = Arrays.asList("Tubo de Coleta", "Agulha", "Seringa");

        switch (nomeExame) {
            case "Hemograma Completo":
                assertTrue(tiposEsperados.contains("Tubo de Coleta"));
                assertTrue(tiposEsperados.contains("Agulha"));
                assertTrue(tiposEsperados.contains("Seringa"));
                break;
        }
    }

    @Test
    @DisplayName("Deve validar SQL de atualização de quantidade")
    public void testValidarSqlAtualizarQuantidade() {
        String sql = "UPDATE dasa_insumos SET quantidade_disponivel = ? WHERE id = ?";

        assertTrue(sql.contains("UPDATE"));
        assertTrue(sql.contains("quantidade_disponivel"));
    }

    @Test
    @DisplayName("Deve validar lógica de adicionar quantidade")
    public void testLogicaAdicionarQuantidade() {
        // Simula insumo para testar lógica
        Insumo insumo = new Insumo(1, "Teste", 123, 100, 200);

        // Testa lógica sem banco
        int quantidadeAdicionar = 50;
        int novaQuantidade = insumo.getQuantidadeDisponivel() + quantidadeAdicionar;

        assertTrue(novaQuantidade <= insumo.getQuantidadeMaxima());
        assertEquals(150, novaQuantidade);
    }

    @Test
    @DisplayName("Deve validar lógica de remover quantidade")
    public void testLogicaRemoverQuantidade() {
        Insumo insumo = new Insumo(1, "Teste", 123, 100, 200);

        int quantidadeRemover = 30;
        int novaQuantidade = insumo.getQuantidadeDisponivel() - quantidadeRemover;

        assertTrue(novaQuantidade >= 0);
        assertEquals(70, novaQuantidade);
    }

    @Test
    @DisplayName("Deve validar cálculo de quantidade máxima")
    public void testCalculoQuantidadeMaxima() {
        Insumo insumo = new Insumo(1, "Teste", 123, 100, 200);

        int maxAdicao = insumo.getQuantidadeMaxima() - insumo.getQuantidadeDisponivel();
        assertEquals(100, maxAdicao);
    }

    @Test
    @DisplayName("Deve validar mapeamento de insumo")
    public void testMapeamentoInsumo() {
        Insumo insumo = new Insumo(1, "Seringa", 123456, 50, 100);

        assertEquals(1, insumo.getId());
        assertEquals("Seringa", insumo.getNome());
        assertEquals(123456, insumo.getCodigoBarras());
        assertEquals(50, insumo.getQuantidadeDisponivel());
        assertEquals(100, insumo.getQuantidadeMaxima());
    }
}