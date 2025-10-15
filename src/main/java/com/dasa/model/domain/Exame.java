package com.dasa.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Classe para representar os exames laboratoriais
 * Funciona com JDBC (Console/Swing) E JPA (REST API)
 */
@Entity
@Table(name = "dasa_exames")
@Data
@NoArgsConstructor // Construtor vazio (JPA precisa)
@AllArgsConstructor // Construtor com todos os campos
public class Exame {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "nome", length = 100, unique = true, nullable = false)
    private String nome;

    /**
     * ToString customizado (sobrescreve o do @Data) - (Console/Swing)
     */
    @Override
    public String toString() {
        return "ID - " + id + "\n\tExame: " + nome;
    }
}