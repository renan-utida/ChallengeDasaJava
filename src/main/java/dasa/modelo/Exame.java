package dasa.modelo;

/**
 * Classe para representar os exames laboratoriais
 */
public class Exame {
    private int id;
    private String nome;

    public Exame(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "ID - " + id + "\n\tExame: " + nome;
    }
}