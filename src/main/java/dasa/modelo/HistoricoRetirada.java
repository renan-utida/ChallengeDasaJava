package dasa.modelo;

import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe para gerenciar o histórico de retiradas de insumos
 */
public class HistoricoRetirada {
    private static final String ARQUIVO_HISTORICO = "historico.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Salva uma retirada no arquivo de histórico
     */
    public static void salvarRetirada(Paciente paciente, List<ItemCesta> itens,
                                      TecnicoLaboratorio tecnico, Enfermeiro enfermeiro) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_HISTORICO, true))) {
            String dataRetirada = LocalDateTime.now().format(formatter);

            writer.println("ID: #" + paciente.getId());
            writer.println("Data Retirada (" + dataRetirada + ")");
            writer.println("\tPaciente: " + paciente.getNomeCompleto());
            writer.println("\tExame: " + paciente.getExame());

            // Lista os itens retirados
            for (ItemCesta item : itens) {
                writer.println("\t" + item.toString());
            }

            writer.println("\tInsumos coletados por " + tecnico.getNome() + " - " + tecnico.getCrbm());
            writer.println("\tEnfermeiro responsável pelo atendimento: " + enfermeiro.getNome() + " - " + enfermeiro.getCoren());
            writer.println("======================================================================");

        } catch (IOException e) {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    /**
     * Exibe todo o historico de retiradas
     */
    public static void exibirHistorico() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_HISTORICO))) {
            String linha;
            boolean temHistorico = false;

            System.out.println();
            System.out.println("=== HISTÓRICO DE RETIRADA DE INSUMOS ===");

            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
                temHistorico = true;
            }

            if (!temHistorico) {
                System.out.println("Nenhuma retirada de insumos foi realizada ainda.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Nenhuma retirada de insumos foi realizada ainda.");
        } catch (IOException e) {
            System.out.println("Erro ao ler histórico: " + e.getMessage());
        }
    }
}
