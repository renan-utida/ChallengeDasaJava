package dasa.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe ItemCesta")
public class ItemCestaTest {

    private ItemCesta itemCesta;
    private Insumo insumo;

    @BeforeEach
    public void setUp() {
        insumo = new Insumo(1051, "Tubo de Coleta Pequeno", 1000051, 1500, 2000);
        itemCesta = new ItemCesta(insumo, 5);
    }

    @Test
    @DisplayName("Deve criar item cesta com construtor parametrizado")
    public void testConstrutorParametrizado() {
        assertEquals(insumo, itemCesta.getInsumo());
        assertEquals(5, itemCesta.getQuantidade());
    }

    @Test
    @DisplayName("Deve definir e obter insumo corretamente")
    public void testSetterGetterInsumo() {
        Insumo novoInsumo = new Insumo(2071, "Agulha 3mm", 2000072, 1200, 2000);
        itemCesta.setInsumo(novoInsumo);
        assertEquals(novoInsumo, itemCesta.getInsumo());
    }

    @Test
    @DisplayName("Deve definir e obter quantidade corretamente")
    public void testSetterGetterQuantidade() {
        itemCesta.setQuantidade(10);
        assertEquals(10, itemCesta.getQuantidade());
    }

    @Test
    @DisplayName("Deve retornar string formatada no toString")
    public void testToString() {
        String expected = "5 - Tubo de Coleta Pequeno";
        assertEquals(expected, itemCesta.toString());
    }

    @Test
    @DisplayName("Deve aceitar quantidade zero")
    public void testQuantidadeZero() {
        itemCesta.setQuantidade(0);
        assertEquals(0, itemCesta.getQuantidade());
    }

    @Test
    @DisplayName("Deve aceitar quantidade maior")
    public void testQuantidadeMaior() {
        itemCesta.setQuantidade(100);
        assertEquals(100, itemCesta.getQuantidade());
        assertEquals("100 - Tubo de Coleta Pequeno", itemCesta.toString());
    }
}