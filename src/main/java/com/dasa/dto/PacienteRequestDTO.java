package com.dasa.dto;

import com.dasa.dto.validation.CPF;
import com.dasa.dto.validation.PastDate;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO para criação e atualização de Paciente
 * Validações Bean Validation aplicadas aqui
 */
@Data
public class PacienteRequestDTO {

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 120, message = "Nome deve ter entre 3 e 120 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]+$",
            message = "Nome deve conter apenas letras e espaços"
    )
    private String nomeCompleto;

    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    private String cpf;

    @NotBlank(message = "Data de nascimento é obrigatória")
    @Pattern(
            regexp = "\\d{2}/\\d{2}/\\d{4}",
            message = "Data deve estar no formato dd/MM/yyyy"
    )
    @PastDate(message = "Data de nascimento deve ser no passado")
    private String dataNascimento;

    @NotNull(message = "Convênio deve ser informado")
    private Boolean convenio;

    @NotNull(message = "Preferencial deve ser informado")
    private Boolean preferencial;
}
