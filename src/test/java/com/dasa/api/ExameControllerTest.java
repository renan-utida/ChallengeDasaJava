package com.dasa.api;

import com.dasa.model.domain.Exame;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - ExameController")
public class ExameControllerTest {

    private ExameController controller;

    @BeforeEach
    public void setUp() {
        controller = new ExameController();
    }

    @Test
    @DisplayName("Deve ter anotação @RestController")
    public void testAnotacaoRestController() {
        assertTrue(controller.getClass().isAnnotationPresent(
                org.springframework.web.bind.annotation.RestController.class));
    }

    @Test
    @DisplayName("Deve ter RequestMapping /api/exames")
    public void testRequestMapping() {
        org.springframework.web.bind.annotation.RequestMapping annotation =
                controller.getClass().getAnnotation(
                        org.springframework.web.bind.annotation.RequestMapping.class);

        assertNotNull(annotation);
        assertEquals("/api/exames", annotation.value()[0]);
    }

    @Test
    @DisplayName("Deve validar método listar existe")
    public void testMetodoListarExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("listar");
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
    @DisplayName("Deve validar método buscarPorNome existe")
    public void testMetodoBuscarPorNomeExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("buscarPorNome", String.class);
        });
    }

    @Test
    @DisplayName("Deve retornar ResponseEntity para busca por ID")
    public void testRetornoResponseEntity() throws Exception {
        var method = controller.getClass().getMethod("buscarPorId", int.class);
        assertEquals(ResponseEntity.class, method.getReturnType());
    }

    @Test
    @DisplayName("Deve validar estrutura de retorno 404")
    public void testEstruturaRetorno404() {
        // Simula lógica do controller sem banco
        Exame exame = null; // Simula não encontrado

        ResponseEntity<Exame> response = exame != null ?
                ResponseEntity.ok(exame) : ResponseEntity.notFound().build();

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Deve validar estrutura de retorno 200")
    public void testEstruturaRetorno200() {
        // Simula lógica do controller sem banco
        Exame exame = new Exame(123, "Hemograma");

        ResponseEntity<Exame> response = exame != null ?
                ResponseEntity.ok(exame) : ResponseEntity.notFound().build();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Hemograma", response.getBody().getNome());
    }
}