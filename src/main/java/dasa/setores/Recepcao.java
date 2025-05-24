package dasa.setores;

import dasa.modelo.Exame;
import dasa.modelo.Paciente;
import java.util.*;

/**
 * Classe responsável pelas funcionalidades da Recepção
 */
public class Recepcao {
    private Scanner scanner;

    public Recepcao(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Menu da Recepção
     */
    public void exibirMenuRecepcao() {
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
                scanner.nextLine(); // Consome a quebra de linha

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
                        return; // Volta ao menu anterior
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

    /**
     * Cadastra um novo paciente
     */
    private void cadastrarPaciente() {
        try {
            System.out.println();
            System.out.println("=== CADASTRAR PACIENTE ===");

            // Coleta e valida nome completo
            String nomeCompleto = solicitarNomeCompleto();

            // CPF com validação
            long cpf = solicitarCPF();

            // Data de nascimento com validação
            String dataNascimento = solicitarDataNascimento();

            // Convenio
            boolean convenio = solicitarOpcaoBoolean("Convênio");

            // Preferencial
            boolean preferencial = solicitarOpcaoBoolean("Preferencial");

            // Jejum
            boolean jejum = solicitarOpcaoBoolean("Em Jejum (min. 8 horas)");

            // Escolha do exame
            String exameEscolhido = adicionarExame();
            if (exameEscolhido == null) {
                return; // Retorna ao menu da recepção se houve erro na escolha do exame
            }

            // Cria e salva o paciente
            Paciente novoPaciente = new Paciente(nomeCompleto, cpf, dataNascimento,
                    convenio, preferencial, jejum, exameEscolhido);
            novoPaciente.salvarNoArquivo();

            System.out.println();
            System.out.println("✅ Paciente cadastrado com sucesso!");
            System.out.println("Status: Em espera");

        } catch (Exception e) {
            System.out.println("ERRO ao cadastrar paciente: " + e.getMessage());
        }
    }

    /**
     * Solicita e valida nome completo (apenas letras e espaços)
     */
    private String solicitarNomeCompleto() {
        while (true) {
            System.out.print("Nome completo: ");
            String nome = scanner.nextLine().trim();

            if (nome.isEmpty()) {
                System.out.println("ERRO: Nome não pode estar vazio!");
                continue;
            }

            if (validarNome(nome)) {
                return nome;
            } else {
                System.out.println("ERRO: Nome deve conter apenas letras e espaços!");
            }
        }
    }

    /**
     * Valida se o nome contém apenas letras e espaços
     */
    private boolean validarNome(String nome) {
        return nome.matches("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]+$");
    }

    /**
     * Solicita e valida CPF
     */
    private long solicitarCPF() {
        while (true) {
            try {
                System.out.print("CPF (apenas números - 11 dígitos): ");
                long cpf = scanner.nextLong();
                scanner.nextLine(); // Consome quebra de linha

                if (Paciente.validarCPF(cpf)) {
                    return cpf;
                } else {
                    System.out.println("ERRO: CPF deve ter exatamente 11 dígitos!");
                }

            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números para o CPF!");
                scanner.nextLine(); // Limpa buffer
            }
        }
    }

    /**
     * Solicita e valida data de nascimento
     */
    private String solicitarDataNascimento() {
        while (true) {
            try {
                System.out.print("Dia de nascimento (1-31): ");
                int dia = scanner.nextInt();

                System.out.print("Mês de nascimento (1-12): ");
                int mes = scanner.nextInt();

                System.out.print("Ano de nascimento (1900-2024): ");
                int ano = scanner.nextInt();
                scanner.nextLine(); // Consome quebra de linha

                if (validarData(dia, mes, ano)) {
                    return String.format("%02d/%02d/%04d", dia, mes, ano);
                } else {
                    System.out.println("ERRO: Data inválida! Verifique os valores informados.");
                }

            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números para a data!");
                scanner.nextLine(); // Limpa buffer
            }
        }
    }

    /**
     * Valida data de nascimento
     */
    private boolean validarData(int dia, int mes, int ano) {
        if (ano < 1900 || ano > 2024) return false;
        if (mes < 1 || mes > 12) return false;
        if (dia < 1 || dia > 31) return false;

        // Validação básica de dias por mês
        int[] diasPorMes = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return dia <= diasPorMes[mes - 1];
    }

    /**
     * Solicita opção boolean (Sim/Não)
     */
    private boolean solicitarOpcaoBoolean(String campo) {
        while (true) {
            try {
                System.out.print(campo + " (1 - Sim, 2 - Não): ");
                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1) {
                    return true;
                } else if (opcao == 2) {
                    return false;
                } else {
                    System.out.println("ERRO: Digite 1 para Sim ou 2 para Não!");
                }
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine();
            }
        }
    }

    /**
     * Menu para adicionar exame ao paciente
     * @return Nome do exame escolhido ou null se houve erro
     */
    private String adicionarExame() {
        // Lista de exames disponíveis
        List<Exame> examesDisponiveis = new ArrayList<>();
        examesDisponiveis.add(new Exame(123, "Hemograma Completo"));
        examesDisponiveis.add(new Exame(456, "Exame de Urina"));
        examesDisponiveis.add(new Exame(789, "Exame de Glicemia"));

        while (true) {
            try {
                System.out.println();
                System.out.println("=== ADICIONAR EXAME ===");
                System.out.println("Menu de Opções de Exames Laboratoriais:");

                // Exibe os exames disponíveis
                for (Exame exame : examesDisponiveis) {
                    System.out.println(exame.toString());
                }

                System.out.print("Digite o ID do exame desejado: ");
                String input = scanner.nextLine().trim();

                // Converter para int
                int idEscolhido = Integer.parseInt(input);

                // Busca o exame pelo ID
                for (Exame exame : examesDisponiveis) {
                    if (exame.getId() == idEscolhido) {
                        return exame.getNome();
                    }
                }

                System.out.println("ERRO: ID inválido!");
                return null; // Retorna ao menu da recepção

            } catch (Exception e) {
                System.out.println("ERRO: " + e.getMessage());
                return null;
            }
        }
    }

    /**
     * Visualiza todos os pacientes registrados
     */
    private void visualizarPacientes() {
        System.out.println();
        System.out.println("=== TODOS OS PACIENTES REGISTRADOS ===");

        List<Paciente> pacientes = Paciente.carregarPacientes();

        if (pacientes.isEmpty()) {
            System.out.println("Não há pacientes registrados.");
        } else {
            for (Paciente paciente : pacientes) {
                paciente.exibirDados();
            }
        }
    }

    /**
     * Demonstra o uso de sobrecarga - Relatório resumido de pacientes
     */
    private void relatorioResumido() {
        try {
            System.out.println();
            System.out.println("=== RELATÓRIO DE PACIENTES ===");
            System.out.println("1 - Relatório Resumido (uma linha por paciente)");
            System.out.println("2 - Relatório por Categoria");
            System.out.print("Escolha o tipo de relatório: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            List<Paciente> pacientes = Paciente.carregarPacientes();

            if (pacientes.isEmpty()) {
                System.out.println("Não há pacientes registrados.");
                return;
            }

            switch (opcao) {
                case 1:
                    // Demonstra sobrecarga exibirDados(boolean)
                    System.out.println();
                    System.out.println("=== RELATÓRIO RESUMIDO ===");
                    for (Paciente paciente : pacientes) {
                        paciente.exibirDados(true); // Sobrecarga com boolean
                    }
                    break;

                case 2:
                    // Demonstra sobrecarga exibirDados(String)
                    System.out.println();
                    System.out.println("Escolha a categoria:");
                    System.out.println("1 - Dados Básicos");
                    System.out.println("2 - Informações Médicas");
                    System.out.println("3 - Dados Administrativos");
                    System.out.print("Categoria: ");

                    int categoria = scanner.nextInt();
                    scanner.nextLine();

                    String tipoRelatorio = "";
                    switch (categoria) {
                        case 1: tipoRelatorio = "basico"; break;
                        case 2: tipoRelatorio = "medico"; break;
                        case 3: tipoRelatorio = "administrativo"; break;
                        default:
                            System.out.println("Categoria inválida!");
                            return;
                    }

                    System.out.println();
                    System.out.println("=== RELATÓRIO POR CATEGORIA ===");
                    for (Paciente paciente : pacientes) {
                        paciente.exibirDados(tipoRelatorio); // Sobrecarga com String
                    }
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
}
