package com.dasa.view.ui.console;

import com.dasa.service.*;
import com.dasa.view.ui.console.setores.*;
import com.dasa.model.funcionarios.TecnicoLaboratorio;
import java.util.*;

/**
 * Classe principal do sistema do laboratório - Console UI
 */
public class ConsoleMain {
    private static Scanner scanner;
    private static TecnicoLaboratorio tecnicoLogado;

    // Services
    private static RecepcaoService recepcaoService;
    private static AlmoxarifadoService almoxarifadoService;
    private static EnfermariaService enfermariaService;
    private static EstoqueService estoqueService;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        // Inicializa os services
        inicializarServices();

        try {
            exibirBoasVindas();

            tecnicoLogado = autenticarTecnico();
            if (tecnicoLogado != null) {
                tecnicoLogado.acessarSistema();
                exibirMenuPrincipal();
            }
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void inicializarServices() {
        recepcaoService = new RecepcaoService();
        almoxarifadoService = new AlmoxarifadoService();
        enfermariaService = new EnfermariaService();
        estoqueService = new EstoqueService();
    }

    private static void exibirBoasVindas() {
        System.out.println("=================================================");
        System.out.println("    BEM-VINDO AO LABORATÓRIO DASA");
        System.out.println("=================================================");
        System.out.println();
    }

    private static TecnicoLaboratorio autenticarTecnico() {
        // Lista técnicos disponíveis
        System.out.println("Técnicos Cadastrados:");
        System.out.println("----------------------");

        List<TecnicoLaboratorio> tecnicos = Arrays.asList(
                new TecnicoLaboratorio("João Silva", 12345),
                new TecnicoLaboratorio("Maria Santos", 67890),
                new TecnicoLaboratorio("Pedro Oliveira", 11223)
        );

        for (TecnicoLaboratorio tecnico : tecnicos) {
            tecnico.apresentar();
            System.out.println();
        }

        while (true) {
            try {
                System.out.print("Digite o número do seu CRBM: ");
                String crbmInput = scanner.nextLine().trim();

                if (crbmInput.isEmpty()) {
                    System.out.println("ERRO: CRBM não pode estar vazio!");
                    continue;
                }

                int crbmInt = Integer.parseInt(crbmInput);

                for (TecnicoLaboratorio tecnico : tecnicos) {
                    if (tecnico.getCrbm() == crbmInt) {
                        return tecnico;
                    }
                }

                System.out.println("ERRO: CRBM não encontrado! Tente novamente.");
                System.out.println();

            } catch (NumberFormatException e) {
                System.out.println("ERRO: Digite apenas números!");
            }
        }
    }

    private static void exibirMenuPrincipal() {
        // Cria instâncias dos consoles
        ConsoleRecepcao consoleRecepcao = new ConsoleRecepcao(scanner, recepcaoService);
        ConsoleAlmoxarifado consoleAlmoxarifado = new ConsoleAlmoxarifado(
                scanner, almoxarifadoService, estoqueService, tecnicoLogado
        );
        ConsoleEnfermaria consoleEnfermaria = new ConsoleEnfermaria(scanner, enfermariaService, almoxarifadoService);

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
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        consoleRecepcao.exibirMenu();
                        break;
                    case 2:
                        consoleAlmoxarifado.exibirMenu();
                        break;
                    case 3:
                        consoleEnfermaria.exibirMenu();
                        break;
                    case 4:
                        System.out.println();
                        System.out.println("=================================================");
                        System.out.println("Obrigado por usar o Sistema do Laboratório DASA!");
                        System.out.println("Até logo, " + tecnicoLogado.getNome() + "!");
                        System.out.println("=================================================");
                        return;
                    default:
                        System.out.println("ERRO: Opção inválida! Digite um número de 1 a 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Digite apenas números!");
                scanner.nextLine();
            }
        }
    }
}
