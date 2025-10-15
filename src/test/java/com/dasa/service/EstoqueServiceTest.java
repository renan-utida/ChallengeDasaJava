package com.dasa.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - EstoqueService")
public class EstoqueServiceTest {

    private EstoqueService service;

    @BeforeEach
    public void setUp() {
        service = new EstoqueService();
    }

    @Test
    @DisplayName("Deve verificar disponibilidade retorna false para ID inexistente")
    public void testVerificarDisponibilidadeIdInexistente() {
        boolean resultado = service.verificarDisponibilidade(-1, 10);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção ao calcular percentual de ID inexistente")
    public void testCalcularPercentualIdInexistente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.calcularPercentualEstoque(-1);
        });
        assertTrue(exception.getMessage().contains("não encontrado"));
    }

    @Test
    @DisplayName("Deve gerar relatório de estoque")
    public void testGerarRelatorio() {
        String relatorio = service.gerarRelatorioEstoque();

        assertNotNull(relatorio);
        assertTrue(relatorio.contains("RELATÓRIO DE ESTOQUE"));
        assertTrue(relatorio.contains("Data:"));
        assertTrue(relatorio.contains("RESUMO"));
    }

    @Test
    @DisplayName("Deve buscar insumo com exceção para ID inexistente")
    public void testBuscarInsumoInexistente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.buscarInsumo(-9999);
        });
        assertTrue(exception.getMessage().contains("não encontrado"));
    }

    @Test
    @DisplayName("Deve criar instância com construtor padrão")
    public void testConstrutorPadrao() {
        EstoqueService s = new EstoqueService();
        assertNotNull(s);
    }

    @Test
    @DisplayName("Deve verificar estrutura do relatório")
    public void testEstruturaRelatorio() {
        String relatorio = service.gerarRelatorioEstoque();

        assertTrue(relatorio.contains("Total de itens"));
        assertTrue(relatorio.contains("Itens com estoque baixo"));
        assertTrue(relatorio.contains("Itens próximos ao máximo"));
    }
}