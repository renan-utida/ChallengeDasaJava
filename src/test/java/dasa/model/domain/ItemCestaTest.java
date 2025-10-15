package dasa.model.domain;

import com.dasa.model.domain.Insumo;
import com.dasa.model.domain.ItemCesta;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - classe ItemCesta")
public class ItemCestaTest {

    private ItemCesta item;
    private Insumo insumo;

    @BeforeEach
    public void setUp() {
        insumo = new Insumo(1, "Agulha 3mm", 789456, 200, 2000);
        item = new ItemCesta(insumo, 5);
    }

    @Test
    @DisplayName("Deve criar item com insumo e quantidade")
    public void testCriacaoItem() {
        assertEquals(insumo, item.getInsumo());
        assertEquals(5, item.getQuantidade());
    }

    @Test
    @DisplayName("Deve alterar quantidade")
    public void testAlterarQuantidade() {
        item.setQuantidade(10);
        assertEquals(10, item.getQuantidade());
    }

    @Test
    @DisplayName("Deve formatar toString corretamente")
    public void testToString() {
        String resultado = item.toString();
        assertEquals("5 - Agulha 3mm", resultado);
    }

    @Test
    @DisplayName("Deve trocar insumo")
    public void testTrocarInsumo() {
        Insumo novoInsumo = new Insumo(2, "Seringa", 456789, 100, 2000);
        item.setInsumo(novoInsumo);
        assertEquals(novoInsumo, item.getInsumo());
    }
}