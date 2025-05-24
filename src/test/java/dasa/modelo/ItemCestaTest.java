package dasa.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe ItemCesta")
public class ItemCestaTest {
    private ItemCesta itemCesta;
    private Insumo insumo;

    @BeforeEach
    public void setUp() {
        insumo = new Insumo(1051, "Tubo de Coleta Pequeno", 1000051, 1500, 2000);
        itemCesta = new ItemCesta(insumo, 5);
    }

    @Test
    @DisplayName("Deve criar item da cesta com insumo e quantidade corretos")
    public void testConstrutorComParametros() {
        assertEquals(insumo, itemCesta.getInsumo());
        assertEquals(5, itemCesta.getQuantidade());
    }

    @Test
    @DisplayName("Deve alterar insumo e quantidade através dos setters")
    public void testSettersGetters() {
        Insumo novoInsumo = new Insumo(2071, "Agulha 3mm", 2000072, 1200, 2000);
        itemCesta.setInsumo(novoInsumo);
        itemCesta.setQuantidade(10);

        assertEquals(novoInsumo, itemCesta.getInsumo());
        assertEquals(10, itemCesta.getQuantidade());
    }

    @Test
    @DisplayName("Deve formatar item da cesta corretamente no toString")
    public void testToString() {
        String resultado = itemCesta.toString();
        assertEquals("5 - Tubo de Coleta Pequeno", resultado);
    }
}