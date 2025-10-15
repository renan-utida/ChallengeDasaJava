package com.dasa.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CriarAtendimentoDTO {

    @NotBlank(message = "Exame é obrigatório")
    private String exame;

}
