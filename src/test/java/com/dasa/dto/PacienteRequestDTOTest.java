package com.dasa.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes - PacienteRequestDTO")
public class PacienteRequestDTOTest {

    private Validator validator;
    private PacienteRequestDTO dto;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        dto = new PacienteRequestDTO();
    }

    @Test
    @DisplayName("Deve validar DTO com dados válidos")
    public void testDtoValido() {
        dto.setNomeCompleto("João Silva");
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando nome é nulo")
    public void testNomeNulo() {
        dto.setNomeCompleto(null);
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando nome é muito curto")
    public void testNomeMuitoCurto() {
        dto.setNomeCompleto("AB");
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("3 e 120")));
    }

    @Test
    @DisplayName("Deve falhar quando nome contém números")
    public void testNomeComNumeros() {
        dto.setNomeCompleto("João Silva123");
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("letras e espaços")));
    }

    @Test
    @DisplayName("Deve aceitar nome com acentos")
    public void testNomeComAcentos() {
        dto.setNomeCompleto("José María Ñuñez");
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando CPF é nulo")
    public void testCpfNulo() {
        dto.setNomeCompleto("João Silva");
        dto.setCpf(null);
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar CPF válido sem formatação")
    public void testCpfValidoSemFormatacao() {
        dto.setNomeCompleto("João Silva");
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar CPF válido com formatação")
    public void testCpfValidoComFormatacao() {
        dto.setNomeCompleto("João Silva");
        dto.setCpf("123.456.789-01");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando data está em formato errado")
    public void testDataFormatoErrado() {
        dto.setNomeCompleto("João Silva");
        dto.setCpf("12345678901");
        dto.setDataNascimento("1990-03-15"); // ISO format
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("dd/MM/yyyy")));
    }

    @Test
    @DisplayName("Deve aceitar data em formato correto")
    public void testDataFormatoCorreto() {
        dto.setNomeCompleto("João Silva");
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando convênio é nulo")
    public void testConvenioNulo() {
        dto.setNomeCompleto("João Silva");
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(null);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando preferencial é nulo")
    public void testPreferencialNulo() {
        dto.setNomeCompleto("João Silva");
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(null);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve aceitar convênio true e preferencial false")
    public void testConvenioTruePreferencialFalse() {
        dto.setNomeCompleto("João Silva");
        dto.setCpf("12345678901");
        dto.setDataNascimento("15/03/1990");
        dto.setConvenio(true);
        dto.setPreferencial(false);

        Set<ConstraintViolation<PacienteRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertTrue(dto.getConvenio());
        assertFalse(dto.getPreferencial());
    }
}