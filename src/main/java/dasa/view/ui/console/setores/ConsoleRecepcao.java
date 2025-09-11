package dasa.view.ui.console.setores;

import dasa.service.RecepcaoService;
import dasa.model.domain.*;
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
                System.out.println("1 - Cadastrar Paciente/Atendimento");
                System.out.println("2 - Visualizar Todos Atendimentos");
                System.out.println("3 - Relatório de Atendimentos");
                System.out.println("4 - Histórico de Exames por Paciente");
                System.out.println("5 - Voltar");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        menuCadastrar();
                        break;
                    case 2:
                        visualizarAtendimentos();
                        break;
                    case 3:
                        relatorioAtendimentos();
                        break;
                    case 4:
                        historicoExamesPaciente();
                        break;
                    case 5:
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

    private void menuCadastrar() {
        System.out.println();
        System.out.println("=== CADASTRAR ===");
        System.out.println("1 - Cadastrar Novo Paciente");
        System.out.println("2 - Cadastrar Atendimento para Paciente Existente");
        System.out.println("3 - Voltar");
        System.out.print("Opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarNovoPaciente();
                    break;
                case 2:
                    cadastrarAtendimentoPacienteExistente();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("ERRO: Opção inválida!");
            }
        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine();
        }
    }

    private void cadastrarNovoPaciente() {
        try {
            System.out.println();
            System.out.println("=== CADASTRAR NOVO PACIENTE ===");

            // Nome completo
            String nomeCompleto = solicitarNomeCompleto();

            // CPF
            String cpf = solicitarCPF();

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
            Long atendimentoId = service.cadastrarPaciente(
                    nomeCompleto, cpf, dataNascimento,
                    convenio, preferencial, jejum, exameEscolhido
            );

            System.out.println();
            System.out.println("✅ Paciente e atendimento cadastrados com sucesso!");
            System.out.println("ID do Atendimento: #" + atendimentoId);
            System.out.println("Status: Em espera");

        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO ao cadastrar paciente: " + e.getMessage());
        }
    }

    private void cadastrarAtendimentoPacienteExistente() {
        try {
            // Lista pacientes resumidos
            List<Paciente> pacientes = service.listarTodosPacientes();

            if (pacientes.isEmpty()) {
                System.out.println("Não há pacientes cadastrados.");
                return;
            }

            // Ordena por CPF
            pacientes.sort((p1, p2) -> p1.getCpf().compareTo(p2.getCpf()));

            System.out.println("\n=== PACIENTES CADASTRADOS ===");
            for (Paciente p : pacientes) {
                System.out.printf("CPF: %s | Nome: %s | Status: %s\n",
                        p.getCpfFormatado(), p.getNomeCompleto(), p.getStatus());
            }

            System.out.print("\nDigite o CPF do paciente (apenas números): ");
            String cpf = scanner.nextLine().trim();
            cpf = cpf.replaceAll("[^0-9]", "");

            Paciente paciente = service.buscarPacientePorCpf(cpf);
            if (paciente == null) {
                System.out.println("ERRO: Paciente não encontrado!");
                return;
            }

            System.out.println("\nPaciente encontrado: " + paciente.getNomeCompleto());
            System.out.println("CPF: " + paciente.getCpfFormatado());

            // Solicita dados do novo exame
            boolean jejum = solicitarOpcaoBoolean("Em Jejum (min. 8 horas)");
            String exameEscolhido = escolherExame();

            // Cadastra novo exame
            Long atendimentoId = service.cadastrarNovoExameParaPaciente(
                    paciente.getId(), jejum, exameEscolhido
            );

            System.out.println("\n✅ Novo atendimento cadastrado com sucesso!");
            System.out.println("ID do Atendimento: #" + atendimentoId);

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private void visualizarAtendimentos() {
        System.out.println();
        System.out.println("=== TODOS OS ATENDIMENTOS REGISTRADOS ===");

        List<Atendimento> atendimentos = service.listarTodosAtendimentos();

        if (atendimentos.isEmpty()) {
            System.out.println("Não há atendimentos registrados.");
        } else {
            for (Atendimento atendimento : atendimentos) {
                atendimento.exibirDados();
            }
        }
    }

    private void relatorioAtendimentos() {
        try {
            System.out.println();
            System.out.println("=== RELATÓRIO DE ATENDIMENTOS ===");
            System.out.println("1 - Todos os Atendimentos");
            System.out.println("2 - Atendimentos Em Espera");
            System.out.println("3 - Atendimentos Realizados");
            System.out.println("4 - Voltar");
            System.out.print("Opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            List<Atendimento> atendimentos;

            switch (opcao) {
                case 1:
                    atendimentos = service.listarTodosAtendimentos();
                    System.out.println("\n=== TODOS OS ATENDIMENTOS ===");
                    break;
                case 2:
                    atendimentos = service.listarAtendimentosPorStatus("Em espera");
                    System.out.println("\n=== ATENDIMENTOS EM ESPERA ===");
                    break;
                case 3:
                    atendimentos = service.listarAtendimentosPorStatus("Atendido");
                    System.out.println("\n=== ATENDIMENTOS REALIZADOS ===");
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opção inválida!");
                    return;
            }

            if (atendimentos.isEmpty()) {
                System.out.println("Nenhum atendimento encontrado.");
            } else {
                for (Atendimento a : atendimentos) {
                    a.exibirDados();
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine();
        }
    }

//    private void relatorioResumido() {
//        try {
//            System.out.println();
//            System.out.println("=== RELATÓRIO DE PACIENTES ===");
//            System.out.println("1 - Relatório Resumido");
//            System.out.println("2 - Relatório por Categoria");
//            System.out.print("Escolha o tipo: ");
//
//            int opcao = scanner.nextInt();
//            scanner.nextLine();
//
//            List<Paciente> pacientes = service.listarTodosPacientes();
//
//            if (pacientes.isEmpty()) {
//                System.out.println("Não há pacientes registrados.");
//                return;
//            }
//
//            switch (opcao) {
//                case 1:
//                    System.out.println("\n=== RELATÓRIO RESUMIDO ===");
//                    for (Paciente p : pacientes) {
//                        p.exibirDados(true);
//                    }
//                    break;
//
//                case 2:
//                    System.out.println("\nEscolha a categoria:");
//                    System.out.println("1 - Dados Básicos");
//                    System.out.println("2 - Informações Médicas");
//                    System.out.println("3 - Dados Administrativos");
//                    System.out.print("Categoria: ");
//
//                    int cat = scanner.nextInt();
//                    scanner.nextLine();
//
//                    String tipo = "";
//                    switch (cat) {
//                        case 1: tipo = "basico"; break;
//                        case 2: tipo = "medico"; break;
//                        case 3: tipo = "administrativo"; break;
//                        default:
//                            System.out.println("Categoria inválida!");
//                            return;
//                    }
//
//                    System.out.println("\n=== RELATÓRIO POR CATEGORIA ===");
//                    for (Paciente p : pacientes) {
//                        p.exibirDados(tipo);
//                    }
//                    break;
//
//                default:
//                    System.out.println("Opção inválida!");
//            }
//        } catch (InputMismatchException e) {
//            System.out.println("ERRO: Digite apenas números!");
//            scanner.nextLine();
//        }
//    }

    private void historicoExamesPaciente() {
        try {
            List<Paciente> pacientes = service.listarTodosPacientes();

            if (pacientes.isEmpty()) {
                System.out.println("Não há pacientes cadastrados.");
                return;
            }

            // Ordena por CPF
            pacientes.sort((p1, p2) -> p1.getCpf().compareTo(p2.getCpf()));

            System.out.println("\n=== PACIENTES CADASTRADOS ===");
            for (Paciente p : pacientes) {
                System.out.printf("CPF: %s | Nome: %s | Status: %s\n",
                        p.getCpfFormatado(), p.getNomeCompleto(), p.getStatus());
            }

            System.out.print("\nDigite o CPF do paciente (apenas números): ");
            String cpf = scanner.nextLine().trim();
            cpf = cpf.replaceAll("[^0-9]", "");

            List<Atendimento> historico = service.listarHistoricoExamesPorCpf(cpf);

            if (historico.isEmpty()) {
                System.out.println("Nenhum atendimento encontrado para este CPF.");
                return;
            }

            System.out.println("\n=== HISTÓRICO DE ATENDIMENTOS ===");
            for (Atendimento atendimento : historico) {
                atendimento.exibirDados();
            }

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    // Métodos auxiliares

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

    private String solicitarCPF() {
        while (true) {
            try {
                System.out.print("CPF (apenas números - 11 dígitos): ");
                String cpf = scanner.nextLine().trim();

                // Remove pontos e traços se o usuário digitar
                cpf = cpf.replaceAll("[^0-9]", "");

                if (cpf.length() == 11) {
                    return cpf;
                } else {
                    System.out.println("ERRO: CPF deve ter exatamente 11 dígitos!");
                }
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
}
