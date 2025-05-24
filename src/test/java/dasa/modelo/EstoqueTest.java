package dasa.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.List;

public class EstoqueTest {
    private Estoque estoque;
    private static final String ARQUIVO_TESTE = "estoque_teste.txt";

    @BeforeEach
    void setUp() {
        // Limpa arquivo de teste se existir
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            arquivo.delete();
        }
        estoque = new Estoque();
    }

    @AfterEach
    void tearDown() {
        // Limpa arquivo de teste após cada teste
        File arquivo = new File("estoque-teste.txt");
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }

    @Test
    void testInicializacaoEstoque() {
        List<Insumo> insumos = estoque.getInsumos();
        assertNotNull(insumos);
        assertEquals(21, insumos.size()); // 21 insumos diferentes

        // Verifica se alguns insumos específicos existem
        boolean temTuboColeta = insumos.stream()
                .anyMatch(i -> i.getNome().equals("Tubo de Coleta Pequeno"));
        assertTrue(temTuboColeta);

        boolean temAgulha = insumos.stream()
                .anyMatch(i -> i.getNome().equals("Agulha 3mm"));
        assertTrue(temAgulha);
    }

    @Test
    void testBuscarInsumo() {
        // Busca por ID
        Insumo insumo = estoque.buscarInsumo(1051);
        assertNotNull(insumo);
        assertEquals("Tubo de Coleta Pequeno", insumo.getNome());

        // Busca por código de barras
        Insumo insumo2 = estoque.buscarInsumo(1000051);
        assertNotNull(insumo2);
        assertEquals("Tubo de Coleta Pequeno", insumo2.getNome());

        // Busca inexistente
        Insumo insumoInexistente = estoque.buscarInsumo(99999);
        assertNull(insumoInexistente);
    }

    @Test
    void testAdicionarQuantidade() {
        boolean resultado = estoque.adicionarQuantidade(1051, 200);
        assertTrue(resultado);

        Insumo insumo = estoque.buscarInsumo(1051);
        assertEquals(1700, insumo.getQuantidadeDisponivel()); // 1500 + 200
    }

    @Test
    void testAdicionarQuantidadeInvalida() {
        boolean resultado = estoque.adicionarQuantidade(1051, 600); // 1500 + 600 = 2100 > 2000
        assertFalse(resultado);

        Insumo insumo = estoque.buscarInsumo(1051);
        assertEquals(1500, insumo.getQuantidadeDisponivel()); // Não deve alterar
    }

    @Test
    void testRemoverQuantidade() {
        boolean resultado = estoque.removerQuantidade(1051, 300);
        assertTrue(resultado);

        Insumo insumo = estoque.buscarInsumo(1051);
        assertEquals(1200, insumo.getQuantidadeDisponivel()); // 1500 - 300
    }

    @Test
    void testRemoverQuantidadeInvalida() {
        boolean resultado = estoque.removerQuantidade(1051, 2000); // Mais que disponível
        assertFalse(resultado);

        Insumo insumo = estoque.buscarInsumo(1051);
        assertEquals(1500, insumo.getQuantidadeDisponivel()); // Não deve alterar
    }

    @Test
    void testCalcularQuantidadeMaximaAdicao() {
        int quantidadeMaxima = estoque.calcularQuantidadeMaximaAdicao(1051);
        assertEquals(500, quantidadeMaxima); // 2000 - 1500

        // Teste com insumo inexistente
        int quantidadeInexistente = estoque.calcularQuantidadeMaximaAdicao(99999);
        assertEquals(0, quantidadeInexistente);
    }

    @Test
    void testGetInsumosPorExame() {
        // Teste Hemograma Completo
        List<Insumo> insumosHemograma = estoque.getInsumosPorExame("Hemograma Completo");
        assertFalse(insumosHemograma.isEmpty());

        boolean temTuboColeta = insumosHemograma.stream()
                .anyMatch(i -> i.getNome().startsWith("Tubo de Coleta"));
        assertTrue(temTuboColeta);

        boolean temAgulha = insumosHemograma.stream()
                .anyMatch(i -> i.getNome().startsWith("Agulha"));
        assertTrue(temAgulha);

        boolean temSeringa = insumosHemograma.stream()
                .anyMatch(i -> i.getNome().startsWith("Seringa"));
        assertTrue(temSeringa);

        // Teste Exame de Urina
        List<Insumo> insumosUrina = estoque.getInsumosPorExame("Exame de Urina");
        assertFalse(insumosUrina.isEmpty());

        boolean temRecipiente = insumosUrina.stream()
                .anyMatch(i -> i.getNome().startsWith("Recipiente Estéril"));
        assertTrue(temRecipiente);

        boolean temTiraReagente = insumosUrina.stream()
                .anyMatch(i -> i.getNome().startsWith("Tira Reagente"));
        assertTrue(temTiraReagente);

        boolean temLamina = insumosUrina.stream()
                .anyMatch(i -> i.getNome().startsWith("Lâmina Análise"));
        assertTrue(temLamina);

        // Teste Exame de Glicemia
        List<Insumo> insumosGlicemia = estoque.getInsumosPorExame("Exame de Glicemia");
        assertFalse(insumosGlicemia.isEmpty());

        boolean temTuboSemAnticoagulante = insumosGlicemia.stream()
                .anyMatch(i -> i.getNome().startsWith("Tubo sem Anticoagulante"));
        assertTrue(temTuboSemAnticoagulante);

        // Teste exame inexistente
        List<Insumo> insumosInexistente = estoque.getInsumosPorExame("Exame Inexistente");
        assertTrue(insumosInexistente.isEmpty());
    }
}