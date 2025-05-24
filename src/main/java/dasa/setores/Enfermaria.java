package dasa.setores;

import dasa.funcionarios.Enfermeiro;
import dasa.modelo.Paciente;
import java.util.*;

/**
 * Classe responsável pelas funcionalidades da Enfermaria
 */
public class Enfermaria {
    private Scanner scanner;
    private List<Enfermeiro> enfermeiros;

    public Enfermaria(Scanner scanner, List<Enfermeiro> enfermeiros) {
        this.scanner = scanner;
        this.enfermeiros = enfermeiros;
    }

    /**
     * Menu da Enfermaria
     */
    public void exibirMenuEnfermaria() {
        while (true) {
            try {
                System.out.println();
                System.out.println("=== ENFERMARIA ===");
                System.out.println("1 - Listar todos os Enfermeiros");
                System.out.println("2 - Exames feitos por enfermeiro específico");
                System.out.println("3 - Voltar");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha

                switch (opcao) {
                    case 1:
                        listarEnfermeiros();
                        break;
                    case 2:
                        examesPorEnfermeiroEspecifico();
                        break;
                    case 3:
                        return; // Volta ao menu anterior
                    default:
                        System.out.println("ERRO: Opção inválida! Digite um número de 1 a 3.");
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
     * Lista todos os enfermeiros cadastrados
     */
    private void listarEnfermeiros() {
        System.out.println();
        System.out.println("=== TODOS OS ENFERMEIROS CADASTRADOS ===");

        for (Enfermeiro enfermeiro : enfermeiros) {
            enfermeiro.apresentar();
            System.out.println();
        }
    }

    /**
     * Exibe exames realizados por enfermeiro específico
     */
    private void examesPorEnfermeiroEspecifico() {
        try {
            // Verifica se há pacientes atendidos
            List<Paciente> pacientesAtendidos = Paciente.filtrarPorStatus("Atendido");

            if (pacientesAtendidos.isEmpty()) {
                System.out.println("Não há pacientes com status 'Atendido' no sistema.");
                return;
            }

            // Busca enfermeiros que já atenderam pacientes
            List<Enfermeiro> enfermeirosQueAtenderam = buscarEnfermeirosQueAtenderam();

            if (enfermeirosQueAtenderam.isEmpty()) {
                System.out.println("Nenhum enfermeiro atendeu pacientes ainda.");
                return;
            }

            // Exibe enfermeiros que já atenderam
            System.out.println();
            System.out.println("=== ENFERMEIROS QUE JÁ ATENDERAM PACIENTES ===");
            for (Enfermeiro enfermeiro : enfermeirosQueAtenderam) {
                enfermeiro.apresentar();
                System.out.println();
            }

            // Solicita COREN do enfermeiro
            System.out.print("Digite o COREN do enfermeiro que gostaria de verificar exames: ");
            String corenBusca = scanner.nextLine().trim();

            // Verifica se o COREN é válido e se o enfermeiro atendeu pacientes
            Enfermeiro enfermeiroSelecionado = null;
            for (Enfermeiro enfermeiro : enfermeirosQueAtenderam) {
                int corenInt = Integer.parseInt(corenBusca);
                if (enfermeiro.getCoren() == corenInt) {
                    enfermeiroSelecionado = enfermeiro;
                    break;
                }
            }

            if (enfermeiroSelecionado == null) {
                System.out.println("ERRO: COREN inválido ou enfermeiro não atendeu nenhum paciente!");
                return;
            }

            // Busca pacientes atendidos por este enfermeiro
            List<Paciente> pacientesDoEnfermeiro = Paciente.filtrarPorEnfermeiroResponsavel(corenBusca);

            if (pacientesDoEnfermeiro.isEmpty()) {
                System.out.println("Este enfermeiro não atendeu nenhum paciente ainda.");
            } else {
                System.out.println();
                System.out.println("=== EXAMES REALIZADOS POR " + enfermeiroSelecionado.getNome().toUpperCase() + " ===");

                for (Paciente paciente : pacientesDoEnfermeiro) {
                    exibirDadosPacienteAtendido(paciente);
                }
            }

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    /**
     * Busca enfermeiros que já atenderam pelo menos um paciente
     */
    private List<Enfermeiro> buscarEnfermeirosQueAtenderam() {
        List<Enfermeiro> enfermeirosQueAtenderam = new ArrayList<>();
        List<Paciente> pacientesAtendidos = Paciente.filtrarPorStatus("Atendido");

        for (Enfermeiro enfermeiro : enfermeiros) {
            // Verifica se este enfermeiro atendeu algum paciente
            for (Paciente paciente : pacientesAtendidos) {
                if (paciente.getEnfermeiroResponsavel().contains(String.valueOf(enfermeiro.getCoren()))) {
                    // Adiciona o enfermeiro à lista se ainda não estiver
                    boolean jaAdicionado = false;
                    for (Enfermeiro e : enfermeirosQueAtenderam) {
                        if (e.getCoren() == enfermeiro.getCoren()) {
                            jaAdicionado = true;
                            break;
                        }
                    }
                    if (!jaAdicionado) {
                        enfermeirosQueAtenderam.add(enfermeiro);
                    }
                    break; // Já encontrou pelo menos um paciente para este enfermeiro
                }
            }
        }

        return enfermeirosQueAtenderam;
    }

    /**
     * Exibe dados de um paciente atendido com formato específico
     */
    private void exibirDadosPacienteAtendido(Paciente paciente) {
        System.out.println("ID: #" + paciente.getId());
        System.out.println("Status: " + paciente.getStatus());
        System.out.println("\tNome Completo: " + paciente.getNomeCompleto());
        System.out.println("\tCPF: " + paciente.getCpf());
        System.out.println("\tData Nascimento: " + paciente.getDataNascimento());
        System.out.println("\tConvenio: " + (paciente.isConvenio() ? "Sim" : "Não"));
        System.out.println("\tPreferencial: " + (paciente.isPreferencial() ? "Sim" : "Não"));
        System.out.println("\tJejum (min. 8 horas): " + (paciente.isJejum() ? "Sim" : "Não"));
        System.out.println("\tExame: " + paciente.getExame());
        System.out.println("\tData de Realização de Entrada do Exame: " + paciente.getDataExame());
        System.out.println("\tEnfermeiro Responsável: " + paciente.getEnfermeiroResponsavel());
        System.out.println("\tResponsável Coleta de Insumos: " + paciente.getResponsavelColeta());
        System.out.println("===============================================");
    }
}
