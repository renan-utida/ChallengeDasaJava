package dasa.model.domain;

/**
 * Classe para representar materiais/insumos do laboratório
 */
public class Insumo {
    private int id;
    private String nome;
    private int codigoBarras;
    private int quantidadeDisponivel;
    private int quantidadeMaxima;

    // Construtor completo
    public Insumo(int id, String nome, int codigoBarras, int quantidadeDisponivel, int quantidadeMaxima) {
        this.id = id;
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.quantidadeMaxima = quantidadeMaxima;
    }

    // Construtor vazio
    public Insumo() {}

    /**
     * Adiciona quantidade ao estoque se não ultrapassar o máximo
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
     */
    public boolean removerQuantidade(int quantidade) {
        if (quantidadeDisponivel >= quantidade) {
            quantidadeDisponivel -= quantidade;
            return true;
        }
        return false;
    }

    /**
     * Exibe os dados do insumo formatados
     */
    public void exibirDados() {
        System.out.println("ID insumo: " + id + " - nome insumo: " + nome);
        System.out.println("\tCódigo de Barras Produto: " + codigoBarras);
        System.out.println("\tQuantidade Disponível: " + quantidadeDisponivel);
        System.out.println("\tQuantidade Máxima: " + quantidadeMaxima);
        System.out.println("=============================================================");
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(int codigoBarras) { this.codigoBarras = codigoBarras; }

    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public int getQuantidadeMaxima() { return quantidadeMaxima; }
    public void setQuantidadeMaxima(int quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }
}
