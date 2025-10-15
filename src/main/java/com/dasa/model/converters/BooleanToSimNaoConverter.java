package com.dasa.model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converte Boolean (Java) <-> 'S'/'N' (Oracle)
 * Funciona automaticamente com JPA
 */
@Converter
public class BooleanToSimNaoConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return "N";
        }
        return attribute ? "S" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return false;
        }
        return "S".equalsIgnoreCase(dbData.trim());
    }
}