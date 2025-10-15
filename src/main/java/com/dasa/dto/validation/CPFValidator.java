package com.dasa.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de CPF
 * Remove pontos/traços, verifica tamanho e se não é sequência repetida
 */
public class CPFValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        // Remove pontos, traços e espaços
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se não é sequência repetida (111.111.111-11, etc)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // CPF válido (aceita qualquer CPF com 11 dígitos diferentes de sequência)
        // Para validação completa com dígitos verificadores, adicionar algoritmo
        return true;
    }
}