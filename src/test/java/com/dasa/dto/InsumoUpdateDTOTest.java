package com.dasa.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - InsumoUpdateDTO")
public class InsumoUpdateDTOTest {

    private Validator validator;
    private InsumoUpdateDTO dto;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        dto = new InsumoUpdateDTO();
    }

    @Test
    @DisplayName("Deve validar DTO com quantidade válida")
    public void testQuantidadeValida() {
        dto.setQuantidade(100);

        Set<ConstraintViolation<InsumoUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando quantidade é nula")
    public void testQuantidadeNula() {
        dto.setQuantidade(null);

        Set<ConstraintViolation<InsumoUpdateDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("obrigatória")));
    }

    @Test
    @DisplayName("Deve aceitar quantidade zero")
    public void testQuantidadeZero() {
        dto.setQuantidade(0);

        Set<ConstraintViolation<InsumoUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando quantidade é negativa")
    public void testQuantidadeNegativa() {
        dto.setQuantidade(-1);

        Set<ConstraintViolation<InsumoUpdateDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("negativa")));
    }

    @Test
    @DisplayName("Deve aceitar quantidade máxima 2000")
    public void testQuantidadeMaxima() {
        dto.setQuantidade(2000);

        Set<ConstraintViolation<InsumoUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando quantidade excede 2000")
    public void testQuantidadeExcede2000() {
        dto.setQuantidade(2001);

        Set<ConstraintViolation<InsumoUpdateDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("2000")));
    }

    @Test
    @DisplayName("Deve aceitar quantidade 1")
    public void testQuantidadeUm() {
        dto.setQuantidade(1);

        Set<ConstraintViolation<InsumoUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar quantidade intermediária")
    public void testQuantidadeIntermediaria() {
        dto.setQuantidade(500);

        Set<ConstraintViolation<InsumoUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertEquals(500, dto.getQuantidade());
    }
}