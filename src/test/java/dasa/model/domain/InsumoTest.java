package dasa.model.domain;

import com.dasa.model.domain.Insumo;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - classe Insumo")
public class InsumoTest {

    private Insumo insumo;

    @BeforeEach
    public void setUp() {
        insumo = new Insumo(1, "Seringa 5ml", 123456, 100, 2000);
    }

    @Test
    @DisplayName("Deve criar insumo com valores corretos")
    public void testCriacaoInsumo() {
        assertEquals(1, insumo.getId());
        assertEquals("Seringa 5ml", insumo.getNome());
        assertEquals(123456, insumo.getCodigoBarras());
        assertEquals(100, insumo.getQuantidadeDisponivel());
        assertEquals(2000, insumo.getQuantidadeMaxima());
    }

    @Test
    @DisplayName("Deve adicionar quantidade quando não excede máximo")
    public void testAdicionarQuantidadeValida() {
        assertTrue(insumo.adicionarQuantidade(50));
        assertEquals(150, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Não deve adicionar quantidade quando excede máximo")
    public void testAdicionarQuantidadeInvalida() {
        assertFalse(insumo.adicionarQuantidade(2000));
        assertEquals(100, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve remover quantidade quando disponível")
    public void testRemoverQuantidadeValida() {
        assertTrue(insumo.removerQuantidade(50));
        assertEquals(50, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Não deve remover quantidade maior que disponível")
    public void testRemoverQuantidadeInvalida() {
        assertFalse(insumo.removerQuantidade(150));
        assertEquals(100, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve permitir remover toda quantidade")
    public void testRemoverTudo() {
        assertTrue(insumo.removerQuantidade(100));
        assertEquals(0, insumo.getQuantidadeDisponivel());
    }

    @Test
    @DisplayName("Deve criar insumo vazio")
    public void testConstrutorVazio() {
        Insumo i = new Insumo();
        assertNotNull(i);
    }
}