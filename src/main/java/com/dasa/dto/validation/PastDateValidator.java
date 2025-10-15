package com.dasa.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Valida se a data (formato dd/MM/yyyy) está no passado
 */
public class PastDateValidator implements ConstraintValidator<PastDate, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public boolean isValid(String data, ConstraintValidatorContext context) {
        if (data == null || data.trim().isEmpty()) {
            return false;
        }

        try {
            LocalDate dataNascimento = LocalDate.parse(data, FORMATTER);

            // Verifica se está no passado
            if (!dataNascimento.isBefore(LocalDate.now())) {
                return false;
            }

            // Verifica se o ano está entre 1900 e ano atual
            int ano = dataNascimento.getYear();
            if (ano < 1900 || ano > LocalDate.now().getYear()) {
                return false;
            }

            return true;

        } catch (DateTimeParseException e) {
            return false;
        }
    }
}