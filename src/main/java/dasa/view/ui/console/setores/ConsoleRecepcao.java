package dasa.view.ui.console.setores;

import dasa.service.RecepcaoService;
import dasa.modelo.Paciente;
import dasa.modelo.Exame;
import java.util.*;

/**
 * Console UI para a Recepção usando Service
 */
public class ConsoleRecepcao {
    private Scanner scanner;
    private RecepcaoService service;

    public ConsoleRecepcao(Scanner scanner, RecepcaoService service) {
        this.scanner = scanner;
        this.service = service;
    }

    public void exibirMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("=== RECEPÇÃO ===");
                System.out.println("1 - Cadastrar Paciente");
                System.out.println("2 - Visualizar Todos Pacientes Registrados");
                System.out.println("3 - Relatório Resumido de Pacientes");
                System.out.println("4 - Voltar");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        cadastrarPaciente();
                        break;
                    case 2:
                        visualizarPacientes();
                        break;
                    case 3:
                        relatorioResumido();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("ERRO: Opção inválida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine();
            }
        }
    }

    private void cadastrarPaciente() {
        try {
            System.out.println();
            System.out.println("=== CADASTRAR PACIENTE ===");

            // Nome completo
            String nomeCompleto = solicitarNomeCompleto();

            // CPF
            long cpf = solicitarCPF();

            // Data de nascimento
            String dataNascimento = solicitarDataNascimento();

            // Convenio
            boolean convenio = solicitarOpcaoBoolean("Convênio");

            // Preferencial
            boolean preferencial = solicitarOpcaoBoolean("Preferencial");

            // Jejum
            boolean jejum = solicitarOpcaoBoolean("Em Jejum (min. 8 horas)");

            // Exame
            String exameEscolhido = escolherExame();

            // Usa o service para cadastrar
            Long id = service.cadastrarPaciente(
                    nomeCompleto, cpf, dataNascimento,
                    convenio, preferencial, jejum, exameEscolhido
            );

            System.out.println();
            System.out.println("✅ Paciente cadastrado com sucesso! ID: #" + id);
            System.out.println("Status: Em espera");

        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO ao cadastrar paciente: " + e.getMessage());
        }
    }

    private String solicitarNomeCompleto() {
        while (true) {
            System.out.print("Nome completo: ");
            String nome = scanner.nextLine().trim();

            if (!nome.isEmpty()) {
                return nome;
            }
            System.out.println("ERRO: Nome não pode estar vazio!");
        }
    }

    private long solicitarCPF() {
        while (true) {
            try {
                System.out.print("CPF (apenas números - 11 dígitos): ");
                long cpf = scanner.nextLong();
                scanner.nextLine();
                return cpf;
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine();
            }
        }
    }

    private String solicitarDataNascimento() {
        while (true) {
            try {
                System.out.print("Dia de nascimento (1-31): ");
                int dia = scanner.nextInt();

                System.out.print("Mês de nascimento (1-12): ");
                int mes = scanner.nextInt();

                System.out.print("Ano de nascimento (1900-2024): ");
                int ano = scanner.nextInt();
                scanner.nextLine();

                return String.format("%02d/%02d/%04d", dia, mes, ano);

            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine();
            }
        }
    }

    private boolean solicitarOpcaoBoolean(String campo) {
        while (true) {
            try {
                System.out.print(campo + " (1 - Sim, 2 - Não): ");
                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1) return true;
                if (opcao == 2) return false;

                System.out.println("ERRO: Digite 1 para Sim ou 2 para Não!");
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine();
            }
        }
    }

    private String escolherExame() {
        List<Exame> exames = service.listarExamesDisponiveis();

        System.out.println();
        System.out.println("=== ADICIONAR EXAME ===");
        System.out.println("Menu de Opções de Exames Laboratoriais:");

        for (Exame exame : exames) {
            System.out.println(exame.toString());
        }

        while (true) {
            try {
                System.out.print("Digite o ID do exame desejado: ");
                int idEscolhido = scanner.nextInt();
                scanner.nextLine();

                for (Exame exame : exames) {
                    if (exame.getId() == idEscolhido) {
                        return exame.getNome();
                    }
                }

                System.out.println("ERRO: ID inválido!");
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine();
            }
        }
    }

    private void visualizarPacientes() {
        System.out.println();
        System.out.println("=== TODOS OS PACIENTES REGISTRADOS ===");

        List<Paciente> pacientes = service.listarTodosPacientes();

        if (pacientes.isEmpty()) {
            System.out.println("Não há pacientes registrados.");
        } else {
            for (Paciente paciente : pacientes) {
                paciente.exibirDados();
            }
        }
    }

    private void relatorioResumido() {
        try {
            System.out.println();
            System.out.println("=== RELATÓRIO DE PACIENTES ===");
            System.out.println("1 - Relatório Resumido");
            System.out.println("2 - Relatório por Categoria");
            System.out.print("Escolha o tipo: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            List<Paciente> pacientes = service.listarTodosPacientes();

            if (pacientes.isEmpty()) {
                System.out.println("Não há pacientes registrados.");
                return;
            }

            switch (opcao) {
                case 1:
                    System.out.println("\n=== RELATÓRIO RESUMIDO ===");
                    for (Paciente p : pacientes) {
                        p.exibirDados(true);
                    }
                    break;

                case 2:
                    System.out.println("\nEscolha a categoria:");
                    System.out.println("1 - Dados Básicos");
                    System.out.println("2 - Informações Médicas");
                    System.out.println("3 - Dados Administrativos");
                    System.out.print("Categoria: ");

                    int cat = scanner.nextInt();
                    scanner.nextLine();

                    String tipo = "";
                    switch (cat) {
                        case 1: tipo = "basico"; break;
                        case 2: tipo = "medico"; break;
                        case 3: tipo = "administrativo"; break;
                        default:
                            System.out.println("Categoria inválida!");
                            return;
                    }

                    System.out.println("\n=== RELATÓRIO POR CATEGORIA ===");
                    for (Paciente p : pacientes) {
                        p.exibirDados(tipo);
                    }
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine();
        }
    }
}
