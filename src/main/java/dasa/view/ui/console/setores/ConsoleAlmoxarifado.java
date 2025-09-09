package dasa.view.ui.console.setores;

import dasa.service.AlmoxarifadoService;
import dasa.service.EstoqueService;
import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import dasa.modelo.*;
import java.util.*;

/**
 * Console UI para o Almoxarifado usando Services
 */
public class ConsoleAlmoxarifado {
    private Scanner scanner;
    private AlmoxarifadoService almoxarifadoService;
    private EstoqueService estoqueService;
    private TecnicoLaboratorio tecnicoLogado;

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
            // Lista pacientes em espera
            List<Paciente> pacientesEmEspera = almoxarifadoService.listarPacientesPorStatus("Em espera");

            if (pacientesEmEspera.isEmpty()) {
                System.out.println("Não há pacientes com status 'Em espera'.");
                return;
            }

            System.out.println("\n=== PACIENTES EM ESPERA ===");
            for (Paciente p : pacientesEmEspera) {
                p.exibirDados();
            }

            System.out.print("ID do paciente: ");
            int idPaciente = scanner.nextInt();
            scanner.nextLine();

            // Busca o paciente
            Paciente paciente = null;
            for (Paciente p : pacientesEmEspera) {
                if (p.getId() == idPaciente) {
                    paciente = p;
                    break;
                }
            }

            if (paciente == null) {
                System.out.println("ERRO: ID inválido!");
                return;
            }

            System.out.println("\nExame: " + paciente.getExame());

            // Coleta insumos
            List<ItemCesta> cesta = coletarInsumos(paciente.getExame());

            // Seleciona enfermeiro
            Enfermeiro enfermeiro = selecionarEnfermeiro(paciente.getExame());
            if (enfermeiro == null) {
                return;
            }

            // Processa retirada via service
            almoxarifadoService.processarRetirada(
                    idPaciente, cesta,
                    tecnicoLogado.getCrbm(),
                    enfermeiro.getCoren()
            );

            System.out.println("\n✅ Retirada processada com sucesso!");
            System.out.println("Insumos coletados por " + tecnicoLogado.getNome());
            System.out.println("Enfermeiro responsável: " + enfermeiro.getNome());

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

            System.out.print("\nID ou código de barras do insumo: ");
            int identificador = scanner.nextInt();
            scanner.nextLine();

            Insumo insumoSelecionado = almoxarifadoService.buscarInsumo(identificador);
            if (insumoSelecionado == null) {
                System.out.println("ERRO: Insumo não encontrado!");
                continue;
            }

            System.out.print("Quantidade: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            cesta.add(new ItemCesta(insumoSelecionado, quantidade));
            System.out.println("✅ Adicionado à cesta!");

            System.out.print("Adicionar mais insumos? (1-Sim, 2-Não): ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            continuar = (opcao == 1);
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

        System.out.print("COREN do enfermeiro: ");
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

        System.out.println("\n=== HISTÓRICO DE RETIRADAS ===");

        if (historico.isEmpty()) {
            System.out.println("Nenhuma retirada realizada.");
        } else {
            for (Map<String, Object> retirada : historico) {
                System.out.println("ID Paciente: #" + retirada.get("paciente_id"));
                System.out.println("Data: " + retirada.get("data_retirada"));
                System.out.println("Técnico CRBM: " + retirada.get("tecnico_crbm"));
                System.out.println("Enfermeiro COREN: " + retirada.get("enfermeiro_coren"));
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

            System.out.print("\nID ou código de barras: ");
            int identificador = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Quantidade a adicionar: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine();

            boolean sucesso = almoxarifadoService.adicionarEstoque(identificador, quantidade);

            if (sucesso) {
                Insumo insumo = almoxarifadoService.buscarInsumo(identificador);
                System.out.println("✅ " + quantidade + " " + insumo.getNome() +
                        " adicionado! Nova quantidade: " + insumo.getQuantidadeDisponivel());
            }

        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine();
        }
    }
}