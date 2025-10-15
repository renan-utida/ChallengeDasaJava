package com.dasa.view.ui.console.setores;

import com.dasa.service.*;
import com.dasa.model.domain. *;
import com.dasa.model.funcionarios. *;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Console UI para o Almoxarifado usando Services
 */
public class ConsoleAlmoxarifado {
    private Scanner scanner;
    private AlmoxarifadoService almoxarifadoService;
    private EstoqueService estoqueService;
    private TecnicoLaboratorio tecnicoLogado;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ConsoleAlmoxarifado(Scanner scanner, AlmoxarifadoService almoxarifadoService,
                               EstoqueService estoqueService, TecnicoLaboratorio tecnicoLogado) {
        this.scanner = scanner;
        this.almoxarifadoService = almoxarifadoService;
        this.estoqueService = estoqueService;
        this.tecnicoLogado = tecnicoLogado;
    }

    public void exibirMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("=== ALMOXARIFADO ===");
                System.out.println("1 - Retirar insumos para exame");
                System.out.println("2 - Verificar histórico de retirada");
                System.out.println("3 - Verificar Estoque");
                System.out.println("4 - Adicionar Estoque");
                System.out.println("5 - Voltar");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        retirarInsumos();
                        break;
                    case 2:
                        verificarHistorico();
                        break;
                    case 3:
                        verificarEstoque();
                        break;
                    case 4:
                        adicionarEstoque();
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

    private void retirarInsumos() {
        try {
            // Lista atendimentos em espera
            List<Atendimento> atendimentosEmEspera = almoxarifadoService.listarAtendimentosPorStatus("Em espera");


            if (atendimentosEmEspera.isEmpty()) {
                System.out.println("Não há atendimentos com status 'Em espera'.");
                return;
            }

            System.out.println("\n=== ATENDIMENTOS EM ESPERA ===");
            for (Atendimento a : atendimentosEmEspera) {
                a.exibirDados();
            }

            System.out.print("ID do atendimento a processar: ");
            int idAtendimento  = scanner.nextInt();
            scanner.nextLine();

            // Busca o atendimento
            Atendimento atendimento = null;
            for (Atendimento a : atendimentosEmEspera) {
                if (a.getId() == idAtendimento) {
                    atendimento = a;
                    break;
                }
            }

            if (atendimento == null) {
                System.out.println("ERRO: ID inválido!");
                return;
            }

            System.out.println("\nExame: " + atendimento.getExame());

            // Coleta insumos
            List<ItemCesta> cesta = coletarInsumos(atendimento.getExame());

            // Seleciona enfermeiro
            Enfermeiro enfermeiro = selecionarEnfermeiro(atendimento.getExame());
            if (enfermeiro == null) {
                return;
            }

            // Processa retirada via service
            almoxarifadoService.processarRetirada(
                    idAtendimento, cesta,
                    tecnicoLogado.getCrbm(),
                    enfermeiro.getCoren()
            );

            // Exibe confirmação detalhada
            System.out.println("\n✅ Retirada processada com sucesso para exame " + atendimento.getExame() + "!");
            for (ItemCesta item : cesta) {
                System.out.println(item.toString());
            }
            System.out.println("Insumos coletados por " + tecnicoLogado.getNome());
            System.out.println("Enfermeiro responsável pelo atendimento: " +
                    enfermeiro.getNome() + " - " + enfermeiro.getCoren());
            System.out.println("Disponibilidade de insumos atualizadas no SAP");
        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO ao processar retirada: " + e.getMessage());
        }
    }

    private List<ItemCesta> coletarInsumos(String nomeExame) {
        List<ItemCesta> cesta = new ArrayList<>();
        boolean continuar = true;

        while (continuar) {
            List<Insumo> insumosExame = almoxarifadoService.listarInsumosPorExame(nomeExame);

            System.out.println("\n=== INSUMOS PARA " + nomeExame.toUpperCase() + " ===");
            for (Insumo insumo : insumosExame) {
                insumo.exibirDados();
            }

            System.out.print("\nDigite o ID ou código de barras do insumo: ");
            int identificador = scanner.nextInt();
            scanner.nextLine();

            Insumo insumoSelecionado = almoxarifadoService.buscarInsumo(identificador);
            if (insumoSelecionado == null) {
                System.out.println("ERRO: Insumo não encontrado!");
                continue;
            }

            System.out.print("Digite a Quantidade: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            if (quantidade <= 0) {
                System.out.println("ERRO: Quantidade deve ser maior que zero!");
                continue;
            }

            if (quantidade > insumoSelecionado.getQuantidadeDisponivel()) {
                System.out.println("ERRO: Quantidade não disponível em estoque! Disponível: " +
                        insumoSelecionado.getQuantidadeDisponivel());
                continue;
            }

            cesta.add(new ItemCesta(insumoSelecionado, quantidade));
            System.out.println("✅ " + quantidade + "x " + insumoSelecionado.getNome() + " adicionado à cesta!");

            // Pergunta se deseja continuar com tratamento de exceção
            while (true) {
                System.out.print("Deseja adicionar mais Insumos à cesta (1 - Sim, 2 - Não): ");
                try {
                    int opcao = scanner.nextInt();
                    scanner.nextLine();

                    if (opcao == 1) {
                        continuar = true;
                        break;
                    } else if (opcao == 2) {
                        continuar = false;
                        break;
                    } else {
                        System.out.println("ERRO: Digite 1 para Sim ou 2 para Não!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("ERRO: Digite apenas números!");
                    scanner.nextLine();
                }
            }
        }

        return cesta;
    }

    private Enfermeiro selecionarEnfermeiro(String exame) {
        List<Enfermeiro> enfermeiros = almoxarifadoService.listarEnfermeirosPorEspecialidade(exame);

        System.out.println("\n=== ENFERMEIROS DISPONÍVEIS ===");
        for (Enfermeiro e : enfermeiros) {
            e.apresentar();
            System.out.println();
        }

        System.out.print("Digite COREN do enfermeiro responsável: ");
        int coren = scanner.nextInt();
        scanner.nextLine();

        for (Enfermeiro e : enfermeiros) {
            if (e.getCoren() == coren) {
                return e;
            }
        }

        System.out.println("ERRO: COREN inválido!");
        return null;
    }

    private void verificarHistorico() {
        List<Map<String, Object>> historico = almoxarifadoService.listarHistoricoCompleto();

        System.out.println("\n=== HISTÓRICO DE RETIRADA DE INSUMOS ===");

        if (historico.isEmpty()) {
            System.out.println("Nenhuma retirada de insumos foi realizada ainda.");
        } else {
            for (Map<String, Object> retirada : historico) {
                LocalDateTime dataRetirada = (LocalDateTime) retirada.get("data_retirada");

                System.out.println("ID Atendimento: #" + retirada.get("atendimento_id"));
                System.out.println("Data Retirada (" + dataRetirada.format(formatter) + ")");
                System.out.println("\tPaciente: " + retirada.get("paciente_nome"));
                System.out.println("\tExame: " + retirada.get("exame_nome"));

                // Exibe itens
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> itens = (List<Map<String, Object>>) retirada.get("itens");
                if (itens != null) {
                    for (Map<String, Object> item : itens) {
                        System.out.println("\t" + item.get("quantidade") + " - " + item.get("insumo_nome"));
                    }
                }

                System.out.println("\t" + retirada.get("tecnico_info"));
                System.out.println("\t" + retirada.get("enfermeiro_info"));
                System.out.println("========================================");
            }
        }
    }

    private void verificarEstoque() {
        List<Insumo> insumos = estoqueService.listarTodosInsumos();

        System.out.println("\n=== ESTOQUE DE INSUMOS ===");
        for (Insumo insumo : insumos) {
            insumo.exibirDados();
        }

        // Verifica estoque baixo
        List<Insumo> estoqueBaixo = estoqueService.listarInsumosEstoqueBaixo();
        if (!estoqueBaixo.isEmpty()) {
            System.out.println("\n⚠️ ALERTA: Insumos com estoque baixo:");
            for (Insumo i : estoqueBaixo) {
                System.out.println("- " + i.getNome() + ": " + i.getQuantidadeDisponivel());
            }
        }
    }

    private void adicionarEstoque() {
        try {
            verificarEstoque();

            System.out.print("\nDigite ID ou código de barras: ");
            int identificador = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Digite Quantidade a adicionar: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            boolean sucesso = almoxarifadoService.adicionarEstoque(identificador, quantidade);

            if (sucesso) {
                Insumo insumo = almoxarifadoService.buscarInsumo(identificador);
                System.out.println("\n✅ " + quantidade + " " + insumo.getNome() +
                        " adicionado com sucesso! Nova quantidade: " + insumo.getQuantidadeDisponivel());
            }

        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine();
        }
    }
}