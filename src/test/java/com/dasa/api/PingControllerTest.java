package com.dasa.api;

import org.junit.jupiter.api.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - PingController")
public class PingControllerTest {

    private PingController controller;

    @BeforeEach
    public void setUp() {
        controller = new PingController();
    }

    @Test
    @DisplayName("Deve ter anotação @RestController")
    public void testAnotacaoRestController() {
        assertTrue(controller.getClass().isAnnotationPresent(
                org.springframework.web.bind.annotation.RestController.class));
    }

    @Test
    @DisplayName("Deve validar método ping existe")
    public void testMetodoPingExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("ping");
        });
    }

    @Test
    @DisplayName("Deve retornar Map com service e status")
    public void testRetornoMapComCampos() {
        Map<String, Object> response = controller.ping();

        assertNotNull(response);
        assertTrue(response.containsKey("service"));
        assertTrue(response.containsKey("status"));
    }

    @Test
    @DisplayName("Deve retornar status ok")
    public void testStatusOk() {
        Map<String, Object> response = controller.ping();

        assertEquals("ok", response.get("status"));
    }

    @Test
    @DisplayName("Deve retornar nome do serviço correto")
    public void testNomeServico() {
        Map<String, Object> response = controller.ping();

        assertEquals("Challenge Dasa Java API", response.get("service"));
    }

    @Test
    @DisplayName("Deve validar estrutura do response")
    public void testEstruturaResponse() {
        Map<String, Object> response = controller.ping();

        assertEquals(2, response.size());
        assertInstanceOf(String.class, response.get("service"));
        assertInstanceOf(String.class, response.get("status"));
    }
}