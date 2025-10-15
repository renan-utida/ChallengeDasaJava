package com.dasa.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - AtendimentoRequestDTO")
public class AtendimentoRequestDTOTest {

    private Validator validator;
    private AtendimentoRequestDTO dto;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        dto = new AtendimentoRequestDTO();
    }

    @Test
    @DisplayName("Deve validar DTO com dados válidos")
    public void testDtoValido() {
        dto.setPacienteId(1);
        dto.setNomeExame("Hemograma Completo");
        dto.setJejum(true);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando pacienteId é nulo")
    public void testPacienteIdNulo() {
        dto.setPacienteId(null);
        dto.setNomeExame("Hemograma Completo");
        dto.setJejum(true);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("obrigatório")));
    }

    @Test
    @DisplayName("Deve falhar quando pacienteId não é positivo")
    public void testPacienteIdNaoPositivo() {
        dto.setPacienteId(0);
        dto.setNomeExame("Hemograma Completo");
        dto.setJejum(true);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando nomeExame é nulo")
    public void testNomeExameNulo() {
        dto.setPacienteId(1);
        dto.setNomeExame(null);
        dto.setJejum(true);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando nomeExame é vazio")
    public void testNomeExameVazio() {
        dto.setPacienteId(1);
        dto.setNomeExame("");
        dto.setJejum(true);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar Hemograma Completo")
    public void testExameHemogramaCompleto() {
        dto.setPacienteId(1);
        dto.setNomeExame("Hemograma Completo");
        dto.setJejum(true);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar Exame de Urina")
    public void testExameUrina() {
        dto.setPacienteId(1);
        dto.setNomeExame("Exame de Urina");
        dto.setJejum(false);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar Exame de Glicemia")
    public void testExameGlicemia() {
        dto.setPacienteId(1);
        dto.setNomeExame("Exame de Glicemia");
        dto.setJejum(true);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar com exame inválido")
    public void testExameInvalido() {
        dto.setPacienteId(1);
        dto.setNomeExame("Exame Inexistente");
        dto.setJejum(true);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Hemograma Completo")));
    }

    @Test
    @DisplayName("Deve falhar quando jejum é nulo")
    public void testJejumNulo() {
        dto.setPacienteId(1);
        dto.setNomeExame("Hemograma Completo");
        dto.setJejum(null);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar jejum true")
    public void testJejumTrue() {
        dto.setPacienteId(1);
        dto.setNomeExame("Hemograma Completo");
        dto.setJejum(true);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertTrue(dto.getJejum());
    }

    @Test
    @DisplayName("Deve aceitar jejum false")
    public void testJejumFalse() {
        dto.setPacienteId(1);
        dto.setNomeExame("Exame de Urina");
        dto.setJejum(false);

        Set<ConstraintViolation<AtendimentoRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertFalse(dto.getJejum());
    }
}