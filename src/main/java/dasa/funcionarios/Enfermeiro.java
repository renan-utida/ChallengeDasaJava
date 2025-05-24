package dasa.funcionarios;

/**
 * Classe específica para Enfermeiros
 */
public class Enfermeiro extends Funcionario {
    private int coren;
    private String especialidade;

    // Construtor padrão
    public Enfermeiro() {
        super();
    }

    // Construtor com parâmetros
    public Enfermeiro(String nome, int coren, String especialidade) {
        super(nome, String.valueOf(coren));
        this.coren = coren;
        this.especialidade = especialidade;
    }

    // Getters e Setters específicos
    public int getCoren() {
        return coren;
    }

    public void setCoren(int coren) {
        this.coren = coren;
        this.registro = String.valueOf(coren); // Mantém sincronizado com a classe pai
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    // Sobrescrita do metodo apresentar (Polimorfismo)
    @Override
    public void apresentar() {
        System.out.println("COREN: " + coren);
        System.out.println("\tNome do(a) Enfermeiro(a): " + nome);
        System.out.println("\tEspecialidade: " + especialidade);
    }

    // Metodo específico do enfermeiro (sobrecarga)
    public void apresentar(boolean comDetalhes) {
        if (comDetalhes) {
            System.out.println("=== DADOS DO ENFERMEIRO ===");
            apresentar();
            System.out.println("Registro: " + registro);
        } else {
            apresentar();
        }
    }
}
