package dasa.funcionarios;

/**
 * Classe pai para todos os funcionários do laboratório
 */
public class Funcionario {
    protected String nome;
    protected int registro;

    // Construtor padrão
    public Funcionario() {
    }

    // Construtor com parâmetros
    public Funcionario(String nome, int registro) {
        this.nome = nome;
        this.registro = registro;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getRegistro() {
        return registro;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }

    // Metodo que pode ser sobrescrito pelas classes filhas
    public void apresentar() {
        System.out.println("Funcionário: " + nome);
    }
}

