package com.dasa.model.funcionarios;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe pai para todos os funcionários do laboratório
 * @MappedSuperclass = Herança de código (não cria tabela própria)
 * Funciona com JDBC (Console/Swing) E JPA (REST API)
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Funcionario {
    protected String nome;
    protected int registro;

    /**
     * Metodo que pode ser sobrescrito pelas classes filhas (Polimorfismo) - (Console/Swing)
     */
    public void apresentar() {
        System.out.println("Funcionário: " + nome);
    }
}