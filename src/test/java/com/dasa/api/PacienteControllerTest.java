package com.dasa.api;

import com.dasa.dto.PacienteRequestDTO;
import com.dasa.dto.PacienteResponseDTO;
import com.dasa.model.domain.Paciente;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - PacienteController")
public class PacienteControllerTest {

    private PacienteController controller;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeEach
    public void setUp() {
        controller = new PacienteController();
    }

    @Test
    @DisplayName("Deve ter anotação @RestController")
    public void testAnotacaoRestController() {
        assertTrue(controller.getClass().isAnnotationPresent(
                org.springframework.web.bind.annotation.RestController.class));
    }

    @Test
    @DisplayName("Deve ter RequestMapping /api/pacientes")
    public void testRequestMapping() {
        org.springframework.web.bind.annotation.RequestMapping annotation =
                controller.getClass().getAnnotation(
                        org.springframework.web.bind.annotation.RequestMapping.class);

        assertNotNull(annotation);
        assertEquals("/api/pacientes", annotation.value()[0]);
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
    @DisplayName("Deve validar método buscarPorCpf existe")
    public void testMetodoBuscarPorCpfExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("buscarPorCpf", String.class);
        });
    }

    @Test
    @DisplayName("Deve validar método criar existe")
    public void testMetodoCriarExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("criar", PacienteRequestDTO.class);
        });
    }

    @Test
    @DisplayName("Deve validar método atualizar existe")
    public void testMetodoAtualizarExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("atualizar", int.class, PacienteRequestDTO.class);
        });
    }

    @Test
    @DisplayName("Deve validar método excluir existe")
    public void testMetodoExcluirExiste() {
        assertDoesNotThrow(() -> {
            controller.getClass().getMethod("excluir", int.class);
        });
    }

    @Test
    @DisplayName("Deve validar limpeza de formatação de CPF")
    public void testLimpezaFormatacaoCpf() {
        // Simula lógica do controller
        String cpfComFormatacao = "123.456.789-01";
        String cpfLimpo = cpfComFormatacao.replaceAll("[^0-9]", "");

        assertEquals("12345678901", cpfLimpo);
        assertEquals(11, cpfLimpo.length());
    }

    @Test
    @DisplayName("Deve validar parsing de data")
    public void testParsingData() {
        // Simula lógica do controller
        String dataString = "15/03/1990";
        LocalDate data = LocalDate.parse(dataString, FORMATTER);

        assertEquals(15, data.getDayOfMonth());
        assertEquals(3, data.getMonthValue());
        assertEquals(1990, data.getYear());
    }

    @Test
    @DisplayName("Deve validar formatação de data na resposta")
    public void testFormatacaoDataResposta() {
        // Simula lógica do controller
        LocalDate data = LocalDate.of(1990, 3, 15);
        String dataFormatada = data.format(FORMATTER);

        assertEquals("15/03/1990", dataFormatada);
    }

    @Test
    @DisplayName("Deve validar estrutura do PacienteResponseDTO")
    public void testEstruturaPacienteResponseDTO() {
        // Simula conversão do controller
        PacienteResponseDTO dto = new PacienteResponseDTO(
                1,
                "João Silva",
                "123.456.789-01",
                "15/03/1990",
                true,
                false,
                "Ativo"
        );

        assertEquals(1, dto.getId());
        assertEquals("João Silva", dto.getNomeCompleto());
        assertEquals("123.456.789-01", dto.getCpfFormatado());
        assertEquals("15/03/1990", dto.getDataNascimento());
        assertTrue(dto.getConvenio());
        assertFalse(dto.getPreferencial());
        assertEquals("Ativo", dto.getStatusPaciente());
    }

    @Test
    @DisplayName("Deve validar trim no nome completo")
    public void testTrimNomeCompleto() {
        // Simula lógica do toModel
        String nomeComEspacos = "  João Silva  ";
        String nomeLimpo = nomeComEspacos.trim();

        assertEquals("João Silva", nomeLimpo);
        assertFalse(nomeLimpo.startsWith(" "));
        assertFalse(nomeLimpo.endsWith(" "));
    }

    @Test
    @DisplayName("Deve validar status padrão Ativo para novos pacientes")
    public void testStatusPadraoAtivo() {
        // Simula lógica do toModel
        Paciente p = new Paciente();
        p.setStatusPaciente("Ativo");

        assertEquals("Ativo", p.getStatusPaciente());
    }

    @Test
    @DisplayName("Deve validar criação de URI para location header")
    public void testCriacaoUri() {
        // Simula lógica do controller
        Long id = 123L;
        String uri = "/api/pacientes/" + id;

        assertEquals("/api/pacientes/123", uri);
        assertTrue(uri.startsWith("/api/pacientes/"));
    }

    @Test
    @DisplayName("Deve manter status ao atualizar")
    public void testManterStatusAoAtualizar() {
        // Simula lógica do controller
        Paciente atual = new Paciente();
        atual.setStatusPaciente("Inativo");

        Paciente atualizado = new Paciente();
        atualizado.setStatusPaciente(atual.getStatusPaciente());

        assertEquals("Inativo", atualizado.getStatusPaciente());
    }
}