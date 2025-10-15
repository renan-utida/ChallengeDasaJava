package dasa.model.funcionarios;

/**
 * Classe específica para Técnicos em Laboratório
 */
public class TecnicoLaboratorio extends Funcionario {
    private int crbm;

    // Construtor padrão
    public TecnicoLaboratorio() {
        super();
    }

    // Construtor com parâmetros
    public TecnicoLaboratorio(String nome, int crbm) {
        super(nome, crbm);
        this.crbm = crbm;
    }

    // Getter e Setter específicos
    public int getCrbm() { return crbm; }
    public void setCrbm(int crbm) {
        this.crbm = crbm;
        this.registro = crbm; // Mantém sincronizado com a classe pai
    }

    // Sobrescrita do metodo apresentar (Polimorfismo)
    @Override
    public void apresentar() {
        System.out.println("CRBM: " + crbm);
        System.out.println("\tNome do(a) Técnico(a): " + nome);
    }

    // Metodo específico do técnico
    public void acessarSistema() {
        System.out.println("Olá, " + nome + ", seja bem vindo ao nosso sistema!");
    }
}