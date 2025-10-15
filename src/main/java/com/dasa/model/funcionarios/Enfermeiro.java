package com.dasa.model.funcionarios;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Classe específica para Enfermeiros
 */
@Entity
@Table(name = "dasa_enfermeiros")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Enfermeiro extends Funcionario {

    @Id
    @Column(name = "coren")
    private int coren;

    @Column(name = "especialidade", length = 100, nullable = false)
    private String especialidade;

    /**
     * Construtor com parâmetros (mantido para JDBC/Console/Swing)
     */
    public Enfermeiro(String nome, int coren, String especialidade) {
        super(nome, coren); // Chama construtor do pai
        this.coren = coren;
        this.especialidade = especialidade;
    }

    /**
     * Getter/Setter customizados mantêm sincronização com registro
     * Importante para JDBC/Console/Swing
     */
    public void setCoren(int coren) {
        this.coren = coren;
        this.registro = coren; // Mantém sincronizado com a classe pai
    }

    /**
     * Sobrescrita do metodo apresentar (Polimorfismo) - (Console/Swing)
     */
    @Override
    public void apresentar() {
        System.out.println("COREN: " + coren);
        System.out.println("\tNome do(a) Enfermeiro(a): " + nome);
        System.out.println("\tEspecialidade: " + especialidade);
    }
}
