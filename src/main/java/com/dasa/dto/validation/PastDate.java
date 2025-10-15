package com.dasa.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * Validação para verificar se data (String) está no passado
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastDateValidator.class)
@Documented
public @interface PastDate {
    String message() default "Data deve ser no passado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}