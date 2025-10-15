package com.dasa.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO para atualização de quantidade de Insumo
 */
@Data
public class InsumoUpdateDTO {

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    @Max(value = 2000, message = "Quantidade não pode exceder 2000")
    private Integer quantidade;
}