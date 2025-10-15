package com.dasa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de Paciente
 * Retorna apenas dados necessários (não expõe toda a entidade)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponseDTO {
    private Integer id;
    private String nomeCompleto;
    private String cpfFormatado; // 123.456.789-00
    private String dataNascimento;
    private Boolean convenio;
    private Boolean preferencial;
    private String statusPaciente;
}