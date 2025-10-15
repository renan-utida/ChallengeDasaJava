package com.dasa.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO para criação de Atendimento
 */
@Data
public class AtendimentoRequestDTO {

    @NotNull(message = "ID do paciente é obrigatório")
    @Positive(message = "ID do paciente deve ser positivo")
    private Integer pacienteId;

    @NotBlank(message = "Nome do exame é obrigatório")
    @Pattern(
            regexp = "^(Hemograma Completo|Exame de Urina|Exame de Glicemia)$",
            message = "Exame deve ser: Hemograma Completo, Exame de Urina ou Exame de Glicemia"
    )
    private String nomeExame;

    @NotNull(message = "Jejum deve ser informado")
    private Boolean jejum;
}