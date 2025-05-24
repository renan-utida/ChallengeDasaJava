package dasa;

import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import dasa.modelo.Estoque;
import dasa.setores.Recepcao;
import dasa.setores.Almoxarifado;
import dasa.setores.Enfermaria;

import java.util.*;

/**
 * Classe principal do sistema do laboratório
 */
public class SistemaLaboratorio {
    private static List<TecnicoLaboratorio> tecnicos;
    private static List<Enfermeiro> enfermeiros;
    private static Estoque estoque;
    private static Scanner scanner;
    private static TecnicoLaboratorio tecnicoLogado;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        inicializarTecnicos();
        inicializarEnfermeiros();
        estoque = new Estoque();

        try {
            exibirBoasVindas();
            exibirTecnicosCadastrados();

            tecnicoLogado = autenticarTecnico();
            if (tecnicoLogado != null) {
                tecnicoLogado.acessarSistema();
                exibirMenuPrincipal(tecnicoLogado);
            }

        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void exibirBoasVindas() {
        System.out.println("=================================================");
        System.out.println("    BEM-VINDO AO LABORATÓRIO DASA");
        System.out.println("=================================================");
        System.out.println();
    }

    private static void exibirTecnicosCadastrados() {
        System.out.println("Técnicos Cadastrados:");
        System.out.println("----------------------");

        for (TecnicoLaboratorio tecnico : tecnicos) {
            tecnico.apresentar();
            System.out.println();
        }
    }

    /**
     * Inicializa a lista com 3 técnicos pré-definidos
     */
    private static void inicializarTecnicos() {
        tecnicos = new ArrayList<>();
        tecnicos.add(new TecnicoLaboratorio("João Silva", 12345));
        tecnicos.add(new TecnicoLaboratorio("Maria Santos", 67890));
        tecnicos.add(new TecnicoLaboratorio("Pedro Oliveira", 11223));
    }

    /**
     * Autentica o técnico pelo CRBM
     * @return TecnicoLaboratorio logado ou null se não encontrado
     */
    private static TecnicoLaboratorio autenticarTecnico() {
        while (true) {
            try {
                System.out.print("Digite o número do seu CRBM: ");
                String crbmInput = scanner.nextLine().trim();

                if (crbmInput.isEmpty()) {
                    System.out.println("ERRO: CRBM não pode estar vazio!");
                    continue;
                }

                // Busca o técnico pelo CRBM
                for (TecnicoLaboratorio tecnico : tecnicos) {
                    int crbmInt = Integer.parseInt(crbmInput);
                    if (tecnico.getCrbm() == crbmInt) {
                        return tecnico;
                    }
                }

                System.out.println("ERRO: CRBM não encontrado! Tente novamente.");
                System.out.println();

            } catch (Exception e) {
                System.out.println("ERRO: Entrada inválida! " + e.getMessage());
                scanner.nextLine(); // Limpa o buffer
            }
        }
    }

    /**
     * Inicializa a lista com 6 enfermeiros pré-definidos (2 para cada especialidade)
     */
    private static void inicializarEnfermeiros() {
        enfermeiros = new ArrayList<>();

        // Enfermeiros especializados em Hemograma Completo
        enfermeiros.add(new Enfermeiro("Ana Carolina Silva", 741321, "Hemograma Completo"));
        enfermeiros.add(new Enfermeiro("Roberto Fernandes", 741322, "Hemograma Completo"));

        // Enfermeiros especializados em Exame de Urina
        enfermeiros.add(new Enfermeiro("Mariana Costa", 852431, "Exame de Urina"));
        enfermeiros.add(new Enfermeiro("Carlos Eduardo", 852432, "Exame de Urina"));

        // Enfermeiros especializados em Exame de Glicemia
        enfermeiros.add(new Enfermeiro("Juliana Santos", 963541, "Exame de Glicemia"));
        enfermeiros.add(new Enfermeiro("Fernando Lima", 963542, "Exame de Glicemia"));
    }

    /**
     * Exibe o menu principal e processa as opções
     * @param tecnico Técnico logado no sistema
     */
    private static void exibirMenuPrincipal(TecnicoLaboratorio tecnico) {
        // Cria instâncias das classes de funcionalidade
        Recepcao recepcao = new Recepcao(scanner);
        Almoxarifado almoxarifado = new Almoxarifado(scanner, estoque, enfermeiros, tecnicoLogado);
        Enfermaria enfermaria = new Enfermaria(scanner, enfermeiros);

        while (true) {
            try {
                System.out.println();
                System.out.println("Digite a opção desejada:");
                System.out.println("1 - Recepção");
                System.out.println("2 - Almoxarifado");
                System.out.println("3 - Enfermaria");
                System.out.println("4 - Sair");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha

                switch (opcao) {
                    case 1:
                        recepcao.exibirMenuRecepcao();
                        break;
                    case 2:
                        almoxarifado.exibirMenuAlmoxarifado();
                        break;
                    case 3:
                        enfermaria.exibirMenuEnfermaria();
                        break;
                    case 4:
                        System.out.println();
                        System.out.println("=================================================");
                        System.out.println("Obrigado por usar o Sistema do Laboratório DASA!");
                        System.out.println("Até logo, " + tecnico.getNome() + "!");
                        System.out.println("=================================================");
                        return;
                    default:
                        System.out.println("ERRO: Opção inválida! Digite um número de 1 a 4.");
                }

            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine(); // Limpa o buffer do scanner
            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
            }
        }
    }
}