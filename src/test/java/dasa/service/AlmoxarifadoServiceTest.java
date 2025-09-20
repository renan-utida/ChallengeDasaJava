package dasa.service;

import dasa.model.domain.ItemCesta;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Testes - AlmoxarifadoService")
public class AlmoxarifadoServiceTest {

    private AlmoxarifadoService service;

    @BeforeEach
    public void setUp() {
        service = new AlmoxarifadoService();
    }

    @Test
    @DisplayName("Deve validar lista de itens vazia")
    public void testValidarListaVazia() {
        List<ItemCesta> listaVazia = new ArrayList<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.processarRetirada(1, listaVazia, 12345, 741321);
        });
        assertTrue(exception.getMessage().contains("não pode estar vazia"));
    }

    @Test
    @DisplayName("Deve validar lista de itens nula")
    public void testValidarListaNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.processarRetirada(1, null, 12345, 741321);
        });
        assertTrue(exception.getMessage().contains("não pode estar vazia"));
    }

    @Test
    @DisplayName("Deve validar quantidade ao adicionar estoque")
    public void testValidarQuantidadeAdicionar() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.adicionarEstoque(1, 0);
        });
        assertTrue(exception.getMessage().contains("maior que zero"));
    }

    @Test
    @DisplayName("Deve validar quantidade negativa")
    public void testValidarQuantidadeNegativa() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.adicionarEstoque(1, -10);
        });
        assertTrue(exception.getMessage().contains("maior que zero"));
    }

    @Test
    @DisplayName("Deve validar status vazio")
    public void testValidarStatusVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.listarAtendimentosPorStatus("");
        });
        assertTrue(exception.getMessage().contains("não pode estar vazio"));
    }

    @Test
    @DisplayName("Deve validar ID inválido")
    public void testValidarIdInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.buscarAtendimentoPorId(0);
        });
        assertTrue(exception.getMessage().contains("ID inválido"));
    }

    @Test
    @DisplayName("Deve validar nome do exame vazio")
    public void testValidarNomeExameVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.listarInsumosPorExame("");
        });
        assertTrue(exception.getMessage().contains("não pode estar vazio"));
    }
}