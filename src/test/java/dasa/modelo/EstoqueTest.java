package dasa.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Estoque")
public class EstoqueTest {

    private Estoque estoque;
    private static final String ARQUIVO_TESTE = "estoque-teste.txt";

    @BeforeEach
    public void setUp() throws Exception {
        // Muda o arquivo para teste via reflexão
        Field arquivoField = Estoque.class.getDeclaredField("ARQUIVO_ESTOQUE");
        arquivoField.setAccessible(true);
        arquivoField.set(null, ARQUIVO_TESTE);

        // Remove arquivo de teste se existir
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            arquivo.delete();
        }

        // Cria novo estoque para teste
        estoque = new Estoque();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Limpa arquivo de teste
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            arquivo.delete();
        }

        // Restaura arquivo original
        Field arquivoField = Estoque.class.getDeclaredField("ARQUIVO_ESTOQUE");
        arquivoField.setAccessible(true);
        arquivoField.set(null, "estoque.txt");
    }

    @Test
    @DisplayName("Deve criar estoque inicializado")
    public void testEstoqueInicializado() {
        assertNotNull(estoque);
        assertFalse(estoque.getInsumos().isEmpty());
        assertEquals(21, estoque.getInsumos().size()); // 21 insumos no total
    }

    @Test
    @DisplayName("Deve buscar insumo por ID")
    public void testBuscarInsumoPorId() {
        Insumo insumo = estoque.buscarInsumo(1051);
        assertNotNull(insumo);
        assertEquals("Tubo de Coleta Pequeno", insumo.getNome());
        assertEquals(1500, insumo.getQuantidadeDisponivel()); // Quantidade inicial
    }

    @Test
    @DisplayName("Deve buscar insumo por código de barras")
    public void testBuscarInsumoPorCodigoBarras() {
        Insumo insumo = estoque.buscarInsumo(1000051);
        assertNotNull(insumo);
        assertEquals("Tubo de Coleta Pequeno", insumo.getNome());
        assertEquals(1500, insumo.getQuantidadeDisponivel()); // Quantidade inicial
    }

    @Test
    @DisplayName("Deve retornar null para ID inexistente")
    public void testBuscarInsumoInexistente() {
        Insumo insumo = estoque.buscarInsumo(9999);
        assertNull(insumo);
    }

    @Test
    @DisplayName("Deve adicionar quantidade ao estoque com valores iniciais")
    public void testAdicionarQuantidade() {
        // Verifica estado inicial
        Insumo insumo = estoque.buscarInsumo(1051);
        assertEquals(1500, insumo.getQuantidadeDisponivel());

        // Adiciona quantidade
        assertTrue(estoque.adicionarQuantidade(1051, 100));

        // Verifica nova quantidade
        insumo = estoque.buscarInsumo(1051);
        assertEquals(1600, insumo.getQuantidadeDisponivel()); // 1500 + 100
    }

    @Test
    @DisplayName("Deve remover quantidade do estoque com valores iniciais")
    public void testRemoverQuantidade() {
        // Verifica estado inicial
        Insumo insumo = estoque.buscarInsumo(1051);
        assertEquals(1500, insumo.getQuantidadeDisponivel());

        // Remove quantidade
        assertTrue(estoque.removerQuantidade(1051, 200));

        // Verifica nova quantidade
        insumo = estoque.buscarInsumo(1051);
        assertEquals(1300, insumo.getQuantidadeDisponivel()); // 1500 - 200
    }

    @Test
    @DisplayName("Deve calcular quantidade máxima para adição")
    public void testCalcularQuantidadeMaximaAdicao() {
        int maxAdicao = estoque.calcularQuantidadeMaximaAdicao(1051);
        assertEquals(500, maxAdicao); // 2000 - 1500 = 500
    }

    @Test
    @DisplayName("Deve retornar insumos para Hemograma Completo")
    public void testGetInsumosPorExameHemograma() {
        List<Insumo> insumos = estoque.getInsumosPorExame("Hemograma Completo");
        assertFalse(insumos.isEmpty());

        // Deve conter tubos de coleta, agulhas e seringas
        boolean temTubos = insumos.stream().anyMatch(i -> i.getNome().startsWith("Tubo de Coleta"));
        boolean temAgulhas = insumos.stream().anyMatch(i -> i.getNome().startsWith("Agulha"));
        boolean temSeringas = insumos.stream().anyMatch(i -> i.getNome().startsWith("Seringa"));

        assertTrue(temTubos);
        assertTrue(temAgulhas);
        assertTrue(temSeringas);
    }

    @Test
    @DisplayName("Deve retornar insumos para Exame de Urina")
    public void testGetInsumosPorExameUrina() {
        List<Insumo> insumos = estoque.getInsumosPorExame("Exame de Urina");
        assertFalse(insumos.isEmpty());

        // Deve conter recipientes, tiras reagentes e lâminas
        boolean temRecipientes = insumos.stream().anyMatch(i -> i.getNome().startsWith("Recipiente Estéril"));
        boolean temTiras = insumos.stream().anyMatch(i -> i.getNome().startsWith("Tira Reagente"));
        boolean temLaminas = insumos.stream().anyMatch(i -> i.getNome().startsWith("Lâmina Análise"));

        assertTrue(temRecipientes);
        assertTrue(temTiras);
        assertTrue(temLaminas);
    }

    @Test
    @DisplayName("Deve retornar insumos para Exame de Glicemia")
    public void testGetInsumosPorExameGlicemia() {
        List<Insumo> insumos = estoque.getInsumosPorExame("Exame de Glicemia");
        assertFalse(insumos.isEmpty());

        // Deve conter tubos sem anticoagulante, agulhas, seringas e tiras reagentes
        boolean temTubosSem = insumos.stream().anyMatch(i -> i.getNome().startsWith("Tubo sem Anticoagulante"));
        boolean temAgulhas = insumos.stream().anyMatch(i -> i.getNome().startsWith("Agulha"));
        boolean temSeringas = insumos.stream().anyMatch(i -> i.getNome().startsWith("Seringa"));
        boolean temTiras = insumos.stream().anyMatch(i -> i.getNome().startsWith("Tira Reagente"));

        assertTrue(temTubosSem);
        assertTrue(temAgulhas);
        assertTrue(temSeringas);
        assertTrue(temTiras);
    }

    @Test
    @DisplayName("Deve preservar estado entre operações múltiplas")
    public void testOperacoesMultiplas() {
        // Estado inicial
        Insumo insumo = estoque.buscarInsumo(1051);
        assertEquals(1500, insumo.getQuantidadeDisponivel());

        // Primeira operação
        assertTrue(estoque.adicionarQuantidade(1051, 300));
        insumo = estoque.buscarInsumo(1051);
        assertEquals(1800, insumo.getQuantidadeDisponivel());

        // Segunda operação
        assertTrue(estoque.removerQuantidade(1051, 100));
        insumo = estoque.buscarInsumo(1051);
        assertEquals(1700, insumo.getQuantidadeDisponivel());
    }
}