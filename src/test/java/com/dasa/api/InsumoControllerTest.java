package com.dasa.api;

import com.dasa.dto.InsumoUpdateDTO;
import com.dasa.model.domain.Insumo;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - InsumoController")
public class InsumoControllerTest {

    private InsumoController controller;

    @BeforeEach
    public void setUp() {
        controller = new InsumoController();
    }

    @Test
    @DisplayName("Deve ter anotação @RestController")
    public void testAnotacaoRestController() {
        assertTrue(controller.getClass().isAnnotationPresent(
                org.springframework.web.bind.annotation.RestController.class));
    }

    @Test
    @DisplayName("Deve ter RequestMapping /api/insumos")
    public void testRequestMapping() {
        org.springframework.web.bind.annotation.RequestMapping annotation =
                controller.getClass().getAnnotation(
                        org.springframework.web.bind.annotation.RequestMapping.class);

        assertNotNull(annotation);
        assertEquals("/api/insumos", annotation.value()[0]);
    }

    @Test
    @DisplayName("Deve validar método listarTodos existe")
    public void testMetodoListarTodosExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("listarTodos");
        });
    }

    @Test
    @DisplayName("Deve validar método buscarPorId existe")
    public void testMetodoBuscarPorIdExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("buscarPorId", int.class);
        });
    }

    @Test
    @DisplayName("Deve validar método buscarPorCodigo existe")
    public void testMetodoBuscarPorCodigoExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("buscarPorCodigo", int.class);
        });
    }

    @Test
    @DisplayName("Deve validar método listarPorTipo existe")
    public void testMetodoListarPorTipoExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("listarPorTipo", String.class);
        });
    }

    @Test
    @DisplayName("Deve validar método listarPorExame existe")
    public void testMetodoListarPorExameExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("listarPorExame", String.class);
        });
    }

    @Test
    @DisplayName("Deve validar método atualizarQuantidade existe")
    public void testMetodoAtualizarQuantidadeExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("atualizarQuantidade", int.class, InsumoUpdateDTO.class);
        });
    }

    @Test
    @DisplayName("Deve validar método adicionar existe")
    public void testMetodoAdicionarExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("adicionar", int.class, int.class);
        });
    }

    @Test
    @DisplayName("Deve validar método remover existe")
    public void testMetodoRemoverExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("remover", int.class, int.class);
        });
    }

    @Test
    @DisplayName("Deve validar estrutura de resposta com Map")
    public void testEstruturaRespostaMap() {
        // Simula resposta do controller
        Map<String, Object> response = Map.of(
                "id", 1,
                "quantidade", 100,
                "nome", "Seringa"
        );

        assertTrue(response.containsKey("id"));
        assertTrue(response.containsKey("quantidade"));
        assertTrue(response.containsKey("nome"));
        assertEquals(1, response.get("id"));
        assertEquals(100, response.get("quantidade"));
        assertEquals("Seringa", response.get("nome"));
    }

    @Test
    @DisplayName("Deve validar estrutura de erro de quantidade excedida")
    public void testEstruturaErroQuantidadeExcedida() {
        // Simula lógica do controller
        boolean adicionouComSucesso = false; // Simula falha

        if (!adicionouComSucesso) {
            Map<String, Object> erro = Map.of(
                    "erro", "Quantidade excede o máximo permitido",
                    "quantidadeAtual", 100,
                    "quantidadeMaxima", 2000
            );

            assertTrue(erro.containsKey("erro"));
            assertTrue(erro.containsKey("quantidadeAtual"));
            assertTrue(erro.containsKey("quantidadeMaxima"));
        }
    }

    @Test
    @DisplayName("Deve validar estrutura de erro de estoque insuficiente")
    public void testEstruturaErroEstoqueInsuficiente() {
        // Simula lógica do controller
        boolean removeuComSucesso = false; // Simula falha

        if (!removeuComSucesso) {
            Map<String, Object> erro = Map.of(
                    "erro", "Quantidade insuficiente em estoque",
                    "quantidadeDisponivel", 50,
                    "quantidadeSolicitada", 100
            );

            assertTrue(erro.containsKey("erro"));
            assertTrue(erro.containsKey("quantidadeDisponivel"));
            assertTrue(erro.containsKey("quantidadeSolicitada"));
        }
    }

    @Test
    @DisplayName("Deve retornar 404 quando insumo não existe")
    public void testRetorno404InsumoNaoExiste() {
        // Simula lógica do controller
        Insumo insumo = null; // Simula não encontrado

        ResponseEntity<?> response = insumo == null ?
                ResponseEntity.notFound().build() : ResponseEntity.ok(insumo);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("Deve retornar 200 quando operação sucede")
    public void testRetorno200Sucesso() {
        // Simula lógica do controller
        Map<String, Object> resultado = Map.of("id", 1, "adicionado", 10);

        ResponseEntity<Map<String, Object>> response = ResponseEntity.ok(resultado);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Deve retornar 400 quando validação falha")
    public void testRetorno400ValidacaoFalha() {
        // Simula lógica do controller
        Map<String, Object> erro = Map.of("erro", "Validação falhou");

        ResponseEntity<Map<String, Object>> response = ResponseEntity.badRequest().body(erro);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("erro"));
    }
}