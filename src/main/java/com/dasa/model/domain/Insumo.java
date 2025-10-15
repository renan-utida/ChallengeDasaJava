package com.dasa.model.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Classe para representar materiais/insumos do laboratório
 * Funciona com JDBC (Console/Swing) E JPA (REST API)
 */
@Entity
@Table(name = "dasa_insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insumo {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "codigo_barras", unique = true, nullable = false)
    private int codigoBarras;

    @Column(name = "quantidade_disponivel", nullable = false)
    private int quantidadeDisponivel;

    @Column(name = "quantidade_maxima", nullable = false)
    private int quantidadeMaxima;

    /**
     * Adiciona quantidade ao estoque se não ultrapassar o máximo
     * Lógica de negócio mantida (JDBC/Console/Swing)
     */
    public boolean adicionarQuantidade(int quantidade) {
        if (quantidadeDisponivel + quantidade <= quantidadeMaxima) {
            quantidadeDisponivel += quantidade;
            return true;
        }
        return false;
    }

    /**
     * Remove quantidade do estoque se houver disponível
     * Lógica de negócio mantida (JDBC/Console/Swing)
     */
    public boolean removerQuantidade(int quantidade) {
        if (quantidadeDisponivel >= quantidade) {
            quantidadeDisponivel -= quantidade;
            return true;
        }
        return false;
    }

    /**
     * Exibe os dados do insumo formatados (Console/Swing)
     */
    public void exibirDados() {
        System.out.println("ID Insumo: " + id + " - Nome Insumo: " + nome);
        System.out.println("\tCódigo de Barras Produto: " + codigoBarras);
        System.out.println("\tQuantidade Disponível: " + quantidadeDisponivel);
        System.out.println("\tQuantidade Máxima: " + quantidadeMaxima);
        System.out.println("=============================================================");
    }
}
