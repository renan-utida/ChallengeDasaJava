package com.dasa.model.funcionarios;

import jakarta.persistence.*;
import lombok.*;

/**
 * Classe específica para Técnicos em Laboratório
 * Funciona com JDBC (Console/Swing) E JPA (REST API)
 */
@Entity
@Table(name = "dasa_tecnicos")
@Data
@EqualsAndHashCode(callSuper = true) // Inclui campos da classe pai no equals/hashCode
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoLaboratorio extends Funcionario {

    @Id
    @Column(name = "crbm")
    private int crbm;

    /**
     * Construtor com parâmetros (mantido para JDBC/Console/Swing)
     */
    public TecnicoLaboratorio(String nome, int crbm) {
        super(nome, crbm);
        this.crbm = crbm;
    }

    /**
     * Getter/Setter customizados mantêm sincronização com registro
     * Importante para JDBC/Console/Swing
     */
    public void setCrbm(int crbm) {
        this.crbm = crbm;
        this.registro = crbm; // Mantém sincronizado com a classe pai
    }

    /**
     * Sobrescrita do metodo apresentar (Polimorfismo) - (Console/Swing)
     */
    @Override
    public void apresentar() {
        System.out.println("CRBM: " + crbm);
        System.out.println("\tNome do(a) Técnico(a): " + nome);
    }

    /**
     * Metodo específico do técnico (Console/Swing)
     */
    public void acessarSistema() {
        System.out.println("Olá, " + nome + ", seja bem vindo ao nosso sistema!");
    }
}