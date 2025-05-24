package dasa.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe Insumo")
public class InsumoTest {
    private Insumo insumo;

    @BeforeEach
    public void setUp() {
        insumo = new Insumo(1051, "Tubo de Coleta Pequeno", 1000051, 1500, 2000);
    }

    @Test
    @DisplayName("Deve criar insumo com todos os parâmetros corretos")
    public void testConstrutorComParametros() {
        assertEquals(1051, insumo.getId());
        assertEquals("Tubo de Coleta Pequeno", insumo.getNome());
        assertEquals(1000051, insumo.getCodigoBarras());
        assertEquals(1500, insumo.getQuantidadeDisponivel());
        assertEquals(2000, insumo.getQuantidadeMaxima());
    }

    @Test
    @DisplayName("Deve adicionar quantidade quando dentro do limite")
    public void testAdicionarQuantidadeValida() {
        boolean resultado = insumo.adicionarQuantidade(300);
        assertTrue(resultado);
        assertEquals(1800, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Não deve adicionar quantidade quando ultrapassar limite máximo")
    public void testAdicionarQuantidadeInvalida() {
        boolean resultado = insumo.adicionarQuantidade(600); // Ultrapassaria 2000
        assertFalse(resultado);
        assertEquals(1500, insumo.getQuantidadeDisponivel()); // Não deve alterar
    }

    @Test
    @DisplayName("Deve remover quantidade quando há estoque suficiente")
    public void testRemoverQuantidadeValida() {
        boolean resultado = insumo.removerQuantidade(500);
        assertTrue(resultado);
        assertEquals(1000, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Não deve remover quantidade quando não há estoque suficiente")
    public void testRemoverQuantidadeInvalida() {
        boolean resultado = insumo.removerQuantidade(2000); // Mais que disponível
        assertFalse(resultado);
        assertEquals(1500, insumo.getQuantidadeDisponivel()); // Não deve alterar
    }

    @Test
    @DisplayName("Deve alterar todas as propriedades através dos setters")
    public void testSettersGetters() {
        insumo.setId(9999);
        insumo.setNome("Teste Material");
        insumo.setCodigoBarras(9999999);
        insumo.setQuantidadeDisponivel(100);
        insumo.setQuantidadeMaxima(500);

        assertEquals(9999, insumo.getId());
        assertEquals("Teste Material", insumo.getNome());
        assertEquals(9999999, insumo.getCodigoBarras());
        assertEquals(100, insumo.getQuantidadeDisponivel());
        assertEquals(500, insumo.getQuantidadeMaxima());
    }

    @Test
    @DisplayName("Deve serializar insumo corretamente para arquivo")
    public void testParaStringArquivo() {
        String resultado = insumo.paraStringArquivo();
        assertEquals("1051|Tubo de Coleta Pequeno|1000051|1500|2000", resultado);
    }

    @Test
    @DisplayName("Deve deserializar insumo corretamente do arquivo")
    public void testFromStringArquivo() {
        String linha = "2071|Agulha 3mm|2000072|1200|2000";
        Insumo insumoCarregado = Insumo.fromStringArquivo(linha);

        assertNotNull(insumoCarregado);
        assertEquals(2071, insumoCarregado.getId());
        assertEquals("Agulha 3mm", insumoCarregado.getNome());
        assertEquals(2000072, insumoCarregado.getCodigoBarras());
        assertEquals(1200, insumoCarregado.getQuantidadeDisponivel());
        assertEquals(2000, insumoCarregado.getQuantidadeMaxima());
    }

    @Test
    @DisplayName("Deve retornar null para linha de arquivo inválida")
    public void testFromStringArquivoInvalida() {
        String linha = "dados|incompletos";
        Insumo resultado = Insumo.fromStringArquivo(linha);
        assertNull(resultado);
    }
}