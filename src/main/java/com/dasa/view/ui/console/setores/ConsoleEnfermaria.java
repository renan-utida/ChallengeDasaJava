package dasa.view.ui.console.setores;

import dasa.model.domain.*;
import dasa.service.AlmoxarifadoService;
import dasa.service.EnfermariaService;
import dasa.model.funcionarios.Enfermeiro;
import java.util.*;

/**
 * Console UI para a Enfermaria usando Service
 */
public class ConsoleEnfermaria {
    private Scanner scanner;
    private EnfermariaService service;
    private AlmoxarifadoService almoxarifadoService;

    public ConsoleEnfermaria(Scanner scanner, EnfermariaService service, AlmoxarifadoService almoxarifadoService) {
        this.scanner = scanner;
        this.service = service;
        this.almoxarifadoService = almoxarifadoService;
    }

    public void exibirMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("=== ENFERMARIA ===");
                System.out.println("1 - Listar todos os Enfermeiros");
                System.out.println("2 - Exames feitos por enfermeiro específico");
                System.out.println("3 - Estatísticas de atendimento");
                System.out.println("4 - Voltar");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        listarEnfermeiros();
                        break;
                    case 2:
                        examesPorEnfermeiro();
                        break;
                    case 3:
                        estatisticasAtendimento();
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

    private void listarEnfermeiros() {
        System.out.println("\n=== TODOS OS ENFERMEIROS CADASTRADOS ===");

        List<Enfermeiro> enfermeiros = service.listarTodosEnfermeiros();

        if (enfermeiros.isEmpty()) {
            System.out.println("Nenhum enfermeiro cadastrado.");
        } else {
            for (Enfermeiro e : enfermeiros) {
                e.apresentar();
                System.out.println();
            }
        }
    }

    private void examesPorEnfermeiro() {
        try {
            // Lista enfermeiros que já atenderam
            List<Enfermeiro> enfermeirosAtivos = service.listarEnfermeirosQueAtenderam();

            if (enfermeirosAtivos.isEmpty()) {
                System.out.println("Nenhum enfermeiro atendeu pacientes ainda.");
                return;
            }

            System.out.println("\n=== ENFERMEIROS QUE JÁ ATENDERAM ===");
            for (Enfermeiro e : enfermeirosAtivos) {
                e.apresentar();
                int atendimentos = service.contarAtendimentosPorEnfermeiro(e.getCoren());
                System.out.println("\tTotal de atendimentos: " + atendimentos);
                System.out.println();
            }

            System.out.print("Digite o COREN do enfermeiro: ");
            int coren = scanner.nextInt();
            scanner.nextLine();

            List<Atendimento> atendimentos = service.listarAtendimentosPorEnfermeiro(coren);

            System.out.println("\n=== ATENDIMENTOS REALIZADOS ===");
            for (Atendimento a : atendimentos) {
                a.exibirDados();
            }

        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("ERRO: Digite apenas números!");
            scanner.nextLine();
        }
    }

    private void estatisticasAtendimento() {
        System.out.println("\n=== ESTATÍSTICAS DE ATENDIMENTO ===");
        System.out.println();

        // Por especialidade
        String[] especialidades = {"Hemograma Completo", "Exame de Urina", "Exame de Glicemia"};

        for (String esp : especialidades) {
            System.out.println("Especialidade: " + esp);

            List<Enfermeiro> enfermeiros = service.listarEnfermeirosPorEspecialidade(esp);
            System.out.println("  Enfermeiros disponíveis: " + enfermeiros.size());

            int totalAtendimentos = 0;
            for (Enfermeiro e : enfermeiros) {
                try {
                    int atend = service.contarAtendimentosPorEnfermeiro(e.getCoren());
                    if (atend > 0) {
                        System.out.println("    - " + e.getNome() + ": " + atend + " atendimentos");
                        totalAtendimentos += atend;
                    }
                } catch (Exception ex) {
                    // Enfermeiro não atendeu ainda
                }
            }
            System.out.println("  Total de atendimentos: " + totalAtendimentos);
            System.out.println();
        }

        // Seção adicional: Enfermeiros com atendimentos
        System.out.println("=== ENFERMEIROS COM ATENDIMENTOS ===");
        List<Enfermeiro> enfermeirosAtivos = service.listarEnfermeirosQueAtenderam();

        if (enfermeirosAtivos.isEmpty()) {
            System.out.println("  Nenhum enfermeiro realizou atendimentos ainda.");
        } else {
            for (Enfermeiro enf : enfermeirosAtivos) {
                int count = service.contarAtendimentosPorEnfermeiro(enf.getCoren());
                System.out.println("  " + enf.getNome() + " (COREN: " + enf.getCoren() +
                        ") - " + enf.getEspecialidade() + ": " + count + " atendimentos");
            }
        }
    }
}
