package dasa.modelo;

/**
 * Classe para representar um item na cesta de retirada
 */
public class ItemCesta {
    private Insumo insumo;
    private int quantidade;

    public ItemCesta(Insumo insumo, int quantidade) {
        this.insumo = insumo;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String toString() {
        return quantidade + " - " + insumo.getNome();
    }
}


