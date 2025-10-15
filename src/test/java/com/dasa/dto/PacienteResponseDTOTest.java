package com.dasa.dto;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - PacienteResponseDTO")
public class PacienteResponseDTOTest {

    private PacienteResponseDTO dto;

    @BeforeEach
    public void setUp() {
        dto = new PacienteResponseDTO();
    }

    @Test
    @DisplayName("Deve criar DTO com construtor vazio")
    public void testConstrutorVazio() {
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Deve criar DTO com construtor completo")
    public void testConstrutorCompleto() {
        dto = new PacienteResponseDTO(
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
    @DisplayName("Deve setar e obter id")
    public void testSetGetId() {
        dto.setId(123);
        assertEquals(123, dto.getId());
    }

    @Test
    @DisplayName("Deve setar e obter nomeCompleto")
    public void testSetGetNomeCompleto() {
        dto.setNomeCompleto("Maria Santos");
        assertEquals("Maria Santos", dto.getNomeCompleto());
    }

    @Test
    @DisplayName("Deve setar e obter cpfFormatado")
    public void testSetGetCpfFormatado() {
        dto.setCpfFormatado("987.654.321-00");
        assertEquals("987.654.321-00", dto.getCpfFormatado());
    }

    @Test
    @DisplayName("Deve setar e obter dataNascimento")
    public void testSetGetDataNascimento() {
        dto.setDataNascimento("20/05/1985");
        assertEquals("20/05/1985", dto.getDataNascimento());
    }

    @Test
    @DisplayName("Deve setar e obter convenio")
    public void testSetGetConvenio() {
        dto.setConvenio(true);
        assertTrue(dto.getConvenio());

        dto.setConvenio(false);
        assertFalse(dto.getConvenio());
    }

    @Test
    @DisplayName("Deve setar e obter preferencial")
    public void testSetGetPreferencial() {
        dto.setPreferencial(true);
        assertTrue(dto.getPreferencial());

        dto.setPreferencial(false);
        assertFalse(dto.getPreferencial());
    }

    @Test
    @DisplayName("Deve setar e obter statusPaciente")
    public void testSetGetStatusPaciente() {
        dto.setStatusPaciente("Ativo");
        assertEquals("Ativo", dto.getStatusPaciente());

        dto.setStatusPaciente("Inativo");
        assertEquals("Inativo", dto.getStatusPaciente());
    }

    @Test
    @DisplayName("Deve ter estrutura completa")
    public void testEstruturaCompleta() {
        dto = new PacienteResponseDTO(
                99,
                "Carlos Eduardo",
                "111.222.333-44",
                "10/10/2000",
                false,
                true,
                "Inativo"
        );

        assertNotNull(dto.getId());
        assertNotNull(dto.getNomeCompleto());
        assertNotNull(dto.getCpfFormatado());
        assertNotNull(dto.getDataNascimento());
        assertNotNull(dto.getConvenio());
        assertNotNull(dto.getPreferencial());
        assertNotNull(dto.getStatusPaciente());
    }
}