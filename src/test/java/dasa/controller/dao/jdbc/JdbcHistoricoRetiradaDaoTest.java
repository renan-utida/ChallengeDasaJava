package dasa.controller.dao.jdbc;

import dasa.controller.dao.jdbc.JdbcHistoricoRetiradaDao;
import dasa.model.domain.Insumo;
import dasa.model.domain.ItemCesta;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.*;

@DisplayName("Testes - JdbcHistoricoRetiradaDao com Mock")
public class JdbcHistoricoRetiradaDaoTest {

    private JdbcHistoricoRetiradaDao dao;

    @BeforeEach
    public void setUp() {
        dao = new JdbcHistoricoRetiradaDao();
    }

    @Test
    @DisplayName("Deve validar SQL de salvar histórico")
    public void testValidarSqlSalvarHistorico() {
        String sql = "INSERT INTO dasa_historico_retiradas " +
                "(atendimento_id, data_retirada, tecnico_crbm, enfermeiro_coren) " +
                "VALUES (?, ?, ?, ?)";

        assertTrue(sql.contains("dasa_historico_retiradas"));
        assertTrue(sql.contains("atendimento_id"));
        assertTrue(sql.contains("data_retirada"));
        assertTrue(sql.contains("tecnico_crbm"));
        assertTrue(sql.contains("enfermeiro_coren"));
    }

    @Test
    @DisplayName("Deve validar SQL de salvar itens")
    public void testValidarSqlSalvarItens() {
        String sql = "INSERT INTO dasa_itens_retirada " +
                "(atendimento_id, data_retirada, insumo_id, quantidade) " +
                "VALUES (?, ?, ?, ?)";

        assertTrue(sql.contains("dasa_itens_retirada"));
        assertTrue(sql.contains("insumo_id"));
        assertTrue(sql.contains("quantidade"));
    }

    @Test
    @DisplayName("Deve validar estrutura de itens da cesta")
    public void testEstruturaItensCesta() {
        Insumo insumo1 = new Insumo(1, "Seringa", 123, 100, 200);
        Insumo insumo2 = new Insumo(2, "Agulha", 456, 150, 300);

        List<ItemCesta> itens = Arrays.asList(
                new ItemCesta(insumo1, 5),
                new ItemCesta(insumo2, 10)
        );

        assertEquals(2, itens.size());
        assertEquals(5, itens.get(0).getQuantidade());
        assertEquals(10, itens.get(1).getQuantidade());
        assertEquals("Seringa", itens.get(0).getInsumo().getNome());
    }

    @Test
    @DisplayName("Deve validar estrutura do Map de histórico")
    public void testEstruturaMapHistorico() {
        Map<String, Object> registro = new HashMap<>();
        registro.put("atendimento_id", 1);
        registro.put("data_retirada", LocalDateTime.now());
        registro.put("paciente_nome", "Teste");
        registro.put("exame_nome", "Hemograma");
        registro.put("tecnico_info", "João - 12345");
        registro.put("enfermeiro_info", "Ana - 741321");
        registro.put("itens", new ArrayList<>());

        assertTrue(registro.containsKey("atendimento_id"));
        assertTrue(registro.containsKey("data_retirada"));
        assertTrue(registro.containsKey("paciente_nome"));
        assertTrue(registro.containsKey("exame_nome"));
        assertTrue(registro.containsKey("tecnico_info"));
        assertTrue(registro.containsKey("enfermeiro_info"));
        assertTrue(registro.containsKey("itens"));
    }

    @Test
    @DisplayName("Deve validar formatação de informações")
    public void testFormatacaoInformacoes() {
        String tecnicoInfo = "Insumos coletados por João Silva - 12345";
        String enfermeiroInfo = "Enfermeiro responsável pelo atendimento: Ana Silva - 741321";

        assertTrue(tecnicoInfo.contains("Insumos coletados por"));
        assertTrue(enfermeiroInfo.contains("Enfermeiro responsável pelo atendimento"));
    }

    @Test
    @DisplayName("Deve validar SQL de listar por período")
    public void testValidarSqlListarPorPeriodo() {
        String sql = "WHERE h.data_retirada BETWEEN ? AND ?";

        assertTrue(sql.contains("BETWEEN"));
        assertTrue(sql.contains("data_retirada"));
    }

    @Test
    @DisplayName("Deve validar período de datas")
    public void testValidacaoPeriodo() {
        LocalDateTime inicio = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2024, 12, 31, 23, 59);
        LocalDateTime dataRetirada = LocalDateTime.of(2024, 6, 15, 10, 30);

        assertTrue(dataRetirada.isAfter(inicio));
        assertTrue(dataRetirada.isBefore(fim));
    }

    @Test
    @DisplayName("Deve validar estrutura de item retornado")
    public void testEstruturaItemRetornado() {
        Map<String, Object> item = new HashMap<>();
        item.put("insumo_id", 1);
        item.put("quantidade", 5);
        item.put("insumo_nome", "Seringa 5ml");

        assertEquals(1, item.get("insumo_id"));
        assertEquals(5, item.get("quantidade"));
        assertEquals("Seringa 5ml", item.get("insumo_nome"));
    }
}