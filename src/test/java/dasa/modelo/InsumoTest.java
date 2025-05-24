package dasa.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe Insumo")
public class InsumoTest {

    private Insumo insumo;

    @BeforeEach
    public void setUp() {
        insumo = new Insumo(1051, "Tubo de Coleta Pequeno", 1000051, 1500, 2000);
    }

    @Test
    @DisplayName("Deve criar insumo com construtor parametrizado")
    public void testConstrutorParametrizado() {
        assertEquals(1051, insumo.getId());
        assertEquals("Tubo de Coleta Pequeno", insumo.getNome());
        assertEquals(1000051, insumo.getCodigoBarras());
        assertEquals(1500, insumo.getQuantidadeDisponivel());
        assertEquals(2000, insumo.getQuantidadeMaxima());
    }

    @Test
    @DisplayName("Deve adicionar quantidade respeitando limite máximo")
    public void testAdicionarQuantidadeValida() {
        assertTrue(insumo.adicionarQuantidade(300)); // 1500 + 300 = 1800 <= 2000
        assertEquals(1800, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve rejeitar adição que ultrapasse limite máximo")
    public void testAdicionarQuantidadeInvalida() {
        assertFalse(insumo.adicionarQuantidade(600)); // 1500 + 600 = 2100 > 2000
        assertEquals(1500, insumo.getQuantidadeDisponivel()); // Deve manter valor original
    }

    @Test
    @DisplayName("Deve remover quantidade se houver disponível")
    public void testRemoverQuantidadeValida() {
        assertTrue(insumo.removerQuantidade(200)); // 1500 - 200 = 1300
        assertEquals(1300, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve rejeitar remoção maior que quantidade disponível")
    public void testRemoverQuantidadeInvalida() {
        assertFalse(insumo.removerQuantidade(1600)); // 1600 > 1500
        assertEquals(1500, insumo.getQuantidadeDisponivel()); // Deve manter valor original
    }

    @Test
    @DisplayName("Deve permitir adicionar até o limite exato")
    public void testAdicionarAteLimiteExato() {
        assertTrue(insumo.adicionarQuantidade(500)); // 1500 + 500 = 2000
        assertEquals(2000, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve permitir remover toda quantidade disponível")
    public void testRemoverTodaQuantidade() {
        assertTrue(insumo.removerQuantidade(1500));
        assertEquals(0, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve converter para string de arquivo corretamente")
    public void testParaStringArquivo() {
        String expected = "1051|Tubo de Coleta Pequeno|1000051|1500|2000";
        assertEquals(expected, insumo.paraStringArquivo());
    }
}