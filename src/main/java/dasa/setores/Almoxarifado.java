package dasa.setores;

import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import dasa.modelo.Paciente;
import dasa.modelo.Estoque;
import dasa.modelo.Insumo;
import dasa.modelo.ItemCesta;
import dasa.modelo.HistoricoRetirada;
import java.util.*;

/**
 * Classe responsável pelas funcionalidades do Almoxarifado
 */
public class Almoxarifado {
    private Scanner scanner;
    private Estoque estoque;
    private List<Enfermeiro> enfermeiros;
    private TecnicoLaboratorio tecnicoLogado;

    public Almoxarifado(Scanner scanner, Estoque estoque, List<Enfermeiro> enfermeiros, TecnicoLaboratorio tecnicoLogado) {
        this.scanner = scanner;
        this.estoque = estoque;
        this.enfermeiros = enfermeiros;
        this.tecnicoLogado = tecnicoLogado;
    }

    /**
     * Menu do Almoxarifado
     */
    public void exibirMenuAlmoxarifado() {
        while (true) {
            try {
                System.out.println();
                System.out.println("=== ALMOXARIFADO ===");
                System.out.println("1 - Retirar insumos para exame");
                System.out.println("2 - Verificar histórico de retirada de insumos");
                System.out.println("3 - Verificar Estoque");
                System.out.println("4 - Adicionar Estoque");
                System.out.println("5 - Voltar");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha

                switch (opcao) {
                    case 1:
                        retirarInsumosExame();
                        break;
                    case 2:
                        verificarHistoricoRetiradas();
                        break;
                    case 3:
                        verificarEstoque();
                        break;
                    case 4:
                        adicionarEstoque();
                        break;
                    case 5:
                        return; // Volta ao menu anterior
                    default:
                        System.out.println("ERRO: Opção inválida! Digite um número de 1 a 5.");
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
     * Verifica e exibe todo o estoque de insumos
     */
    private void verificarEstoque() {
        estoque.exibirEstoque();
    }

    /**
     * Adiciona quantidade ao estoque de insumos
     */
    private void adicionarEstoque() {
        try {
            // Exibe todos os insumos disponíveis
            estoque.exibirEstoque();

            System.out.print("Digite o ID ou o código de barras: ");
            int identificador = scanner.nextInt();
            scanner.nextLine(); // Consome a quebra de linha

            // Busca o insumo
            Insumo insumo = estoque.buscarInsumo(identificador);
            if (insumo == null) {
                System.out.println("ERRO: ID ou Código de Barras inválido!");
                return;
            }

            // Calcula quantidade máxima que pode ser adicionada
            int quantidadeMaximaAdicao = estoque.calcularQuantidadeMaximaAdicao(identificador);

            if (quantidadeMaximaAdicao <= 0) {
                System.out.println("ERRO: Este insumo já está no estoque máximo!");
                return;
            }

            System.out.print("Digite a quantidade que gostaria de adicionar: ");
            int quantidadeAdicionar = scanner.nextInt();
            scanner.nextLine(); // Consome a quebra de linha

            // Validações
            if (quantidadeAdicionar <= 0) {
                System.out.println("ERRO: Quantidade deve ser maior que zero!");
                return;
            }

            if (quantidadeAdicionar > quantidadeMaximaAdicao) {
                System.out.println("ERRO: A quantidade disponível + quantidade adicionada não pode ultrapassar 2000!");
                System.out.println("Quantidade máxima que pode ser adicionada: " + quantidadeMaximaAdicao);
                return;
            }

            // Adiciona a quantidade
            boolean sucesso = estoque.adicionarQuantidade(identificador, quantidadeAdicionar);

            if (sucesso) {
                // Busca novamente o insumo para obter os dados atualizados
                insumo = estoque.buscarInsumo(identificador);
                System.out.println();
                System.out.println("✅ " + quantidadeAdicionar + " " + insumo.getNome() +
                        " adicionado com sucesso! Nova quantidade disponível deste insumo: " +
                        insumo.getQuantidadeDisponivel());
            } else {
                System.out.println("ERRO: Não foi possível adicionar a quantidade ao estoque!");
            }

        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine(); // Limpa o buffer
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    /**
     * Retira insumos para um exame específico
     */
    private void retirarInsumosExame() {
        try {
            // Busca pacientes com status "Em espera"
            List<Paciente> pacientesEmEspera = Paciente.filtrarPorStatus("Em espera");

            if (pacientesEmEspera.isEmpty()) {
                System.out.println("Não há pacientes cadastrados com status 'Em espera'.");
                return;
            }

            // Exibe pacientes em espera
            System.out.println();
            System.out.println("=== PACIENTES EM ESPERA ===");
            for (Paciente paciente : pacientesEmEspera) {
                paciente.exibirDados();
            }

            // Solicita ID do paciente
            System.out.print("Qual o ID do paciente a retirar o Exame: ");
            int idPaciente = scanner.nextInt();
            scanner.nextLine();

            // Busca o paciente
            Paciente pacienteSelecionado = null;
            for (Paciente paciente : pacientesEmEspera) {
                if (paciente.getId() == idPaciente) {
                    pacienteSelecionado = paciente;
                    break;
                }
            }

            if (pacienteSelecionado == null) {
                System.out.println("ERRO: ID inválido!");
                return;
            }

            // Exibe exame do paciente
            System.out.println("\n\nID Exame - " + pacienteSelecionado.getExame());

            // Inicia processo de coleta de insumos
            List<ItemCesta> cesta = new ArrayList<>();
            boolean continuarColetando = true;

            while (continuarColetando) {
                // Exibe insumos do exame
                List<Insumo> insumosExame = estoque.getInsumosPorExame(pacienteSelecionado.getExame());

                System.out.println();
                System.out.println("=== INSUMOS PARA " + pacienteSelecionado.getExame().toUpperCase() + " ===");
                for (Insumo insumo : insumosExame) {
                    System.out.println("ID " + insumo.getId() + " - " + insumo.getNome());
                    System.out.println("\tCódigo de Barras Produto: " + insumo.getCodigoBarras());
                    System.out.println("\tQuantidade Disponível: " + insumo.getQuantidadeDisponivel());
                    System.out.println();
                }

                // Solicita insumo
                Insumo insumoSelecionado = null;
                while (insumoSelecionado == null) {
                    try {
                        System.out.print("\nDigite o ID ou o código de barras do insumo que gostaria de adicionar à cesta: ");
                        int identificador = scanner.nextInt();
                        scanner.nextLine();

                        // Busca o insumo entre os do exame
                        for (Insumo insumo : insumosExame) {
                            if (insumo.getId() == identificador || insumo.getCodigoBarras() == identificador) {
                                insumoSelecionado = insumo;
                                break;
                            }
                        }

                        if (insumoSelecionado == null) {
                            System.out.println("ERRO: ID ou Código de Barras inválido!");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("ERRO: Entrada inválida!");
                        scanner.nextLine();
                        return;
                    }
                }

                // Solicita quantidade
                int quantidade = 0;
                while (quantidade <= 0) {
                    try {
                        System.out.print("Digite a quantidade: ");
                        quantidade = scanner.nextInt();
                        scanner.nextLine();

                        if (quantidade <= 0) {
                            System.out.println("ERRO: Quantidade deve ser maior que zero!");
                        } else if (quantidade > insumoSelecionado.getQuantidadeDisponivel()) {
                            System.out.println("ERRO: Quantidade não disponível em estoque! Disponível: " +
                                    insumoSelecionado.getQuantidadeDisponivel());
                            quantidade = 0;
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("ERRO: Entrada inválida!");
                        scanner.nextLine();
                        return;
                    }
                }

                // Adiciona à cesta
                cesta.add(new ItemCesta(insumoSelecionado, quantidade));
                System.out.println("✅ " + insumoSelecionado.getNome() + " adicionado à cesta!");

                // Pergunta se deseja adicionar mais insumos
                int opcaoContinuar = 0;
                while (opcaoContinuar != 1 && opcaoContinuar != 2) {
                    try {
                        System.out.print("Deseja adicionar mais Insumos à cesta (1 - Sim, 2 - Não): ");
                        opcaoContinuar = scanner.nextInt();
                        scanner.nextLine();

                        if (opcaoContinuar == 1) {
                            continuarColetando = true;
                        } else if (opcaoContinuar == 2) {
                            continuarColetando = false;
                        } else {
                            System.out.println("ERRO: Digite 1 para Sim ou 2 para Não!");
                        }

                    } catch (InputMismatchException e) {
                        System.out.println("ERRO: Entrada inválida!");
                        scanner.nextLine();
                        return;
                    }
                }
            }

            // Seleciona enfermeiro responsável
            Enfermeiro enfermeiroSelecionado = selecionarEnfermeiro(pacienteSelecionado.getExame());
            if (enfermeiroSelecionado == null) {
                return;
            }

            // Processa a retirada
            finalizarRetirada(pacienteSelecionado, cesta, enfermeiroSelecionado);

        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    /**
     * Seleciona o enfermeiro responsável pelo exame
     */
    private Enfermeiro selecionarEnfermeiro(String exame) {
        try {
            // Filtra enfermeiros pela especialidade
            List<Enfermeiro> enfermeirosEspecialidade = new ArrayList<>();
            for (Enfermeiro enfermeiro : enfermeiros) {
                if (enfermeiro.getEspecialidade().equals(exame)) {
                    enfermeirosEspecialidade.add(enfermeiro);
                }
            }

            System.out.println();
            System.out.println("=== ENFERMEIROS DISPONÍVEIS ===");
            for (Enfermeiro enfermeiro : enfermeirosEspecialidade) {
                enfermeiro.apresentar();
                System.out.println();
            }

            // Solicita COREN
            while (true) {
                try {
                    System.out.print("Digite o COREN do enfermeiro responsável: ");
                    String corenInput = scanner.nextLine().trim();

                    // Converte para int
                    int corenInt = Integer.parseInt(corenInput);

                    // Busca enfermeiro pelo COREN
                    for (Enfermeiro enfermeiro : enfermeirosEspecialidade) {
                        if (enfermeiro.getCoren() == corenInt) {
                            return enfermeiro;
                        }
                    }

                    System.out.println("ERRO: COREN inválido!");

                } catch (Exception e) {
                    System.out.println("ERRO: Entrada inválida!");
                    return null;
                }
            }

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Finaliza a retirada de insumos
     */
    private void finalizarRetirada(Paciente paciente, List<ItemCesta> cesta, Enfermeiro enfermeiro) {
        try {
            // Atualiza estoque
            for (ItemCesta item : cesta) {
                estoque.removerQuantidade(item.getInsumo().getId(), item.getQuantidade());
            }

            // Atualiza status do paciente
            paciente.setStatus("Atendido");
            paciente.setEnfermeiroResponsavel(enfermeiro.getNome() + " - " + enfermeiro.getCoren());
            paciente.setResponsavelColeta(tecnicoLogado.getNome() + " - " + tecnicoLogado.getCrbm());

            // Atualiza paciente no arquivo
            Paciente.atualizarPacienteNoArquivo(paciente);

            // Salva no histórico
            HistoricoRetirada.salvarRetirada(paciente, cesta, tecnicoLogado, enfermeiro);

            // Exibe confirmação
            System.out.println();
            System.out.println("✅ Insumos coletados por " + tecnicoLogado.getNome() + " para exame " + paciente.getExame() + ",");
            for (ItemCesta item : cesta) {
                System.out.println(item.toString());
            }
            System.out.println("Enfermeiro responsável pelo atendimento: " + enfermeiro.getNome() + " - " + enfermeiro.getCoren());
            System.out.println("Disponibilidade de insumos atualizadas no SAP");

        } catch (Exception e) {
            System.out.println("ERRO ao finalizar retirada: " + e.getMessage());
        }
    }

    /**
     * Verifica o histórico de retiradas
     */
    private void verificarHistoricoRetiradas() {
        HistoricoRetirada.exibirHistorico();
    }
}
