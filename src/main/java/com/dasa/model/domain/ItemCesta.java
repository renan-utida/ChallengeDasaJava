package com.dasa.model.domain;

import lombok.*;

/**
 * Classe para representar um item na cesta de retirada
 * NÃO é @Entity - usado apenas em memória
 * Os dados são salvos em dasa_itens_retirada via HistoricoDao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCesta {
    private Insumo insumo;
    private int quantidade;

    @Override
    public String toString() {
        return quantidade + " - " + insumo.getNome();
    }
}
