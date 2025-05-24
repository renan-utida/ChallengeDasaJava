package dasa.modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para gerenciar o estoque de insumos do laboratório
 */
public class Estoque {
    private static String ARQUIVO_ESTOQUE = "estoque.txt";
    private static final int QUANTIDADE_INICIAL = 1500;
    private static final int QUANTIDADE_MAXIMA = 2000;
    private List<Insumo> insumos;

    public Estoque() {
        insumos = new ArrayList<>();
        carregarEstoque();
    }

    /**
     * Inicializa o estoque com todos os insumos dos exames
     */
    private void inicializarEstoque() {
        // Tubos de coleta (Hemograma Completo)
        insumos.add(new Insumo(1051, "Tubo de Coleta Pequeno", 1000051, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(1052, "Tubo de Coleta Médio", 1000052, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(1053, "Tubo de Coleta Grande", 1000053, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Agulhas (Hemograma Completo e Glicemia)
        insumos.add(new Insumo(2071, "Agulha 2mm", 2000071, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(2072, "Agulha 3mm", 2000072, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(2073, "Agulha 5mm", 2000073, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Seringas (Hemograma Completo e Glicemia)
        insumos.add(new Insumo(3081, "Seringa 5ml", 3000081, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(3082, "Seringa 10ml", 3000082, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(3083, "Seringa 20ml", 3000083, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Recipiente estéril (Exame de Urina)
        insumos.add(new Insumo(4091, "Recipiente Estéril Pequeno", 4000091, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(4092, "Recipiente Estéril Médio", 4000092, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(4093, "Recipiente Estéril Grande", 4000093, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Tiras reagentes (Exame de Urina e Glicemia)
        insumos.add(new Insumo(5001, "Tira Reagente Tipo A", 5000001, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(5002, "Tira Reagente Tipo B", 5000002, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(5003, "Tira Reagente Tipo C", 5000003, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Lâmina para análise (Exame de Urina)
        insumos.add(new Insumo(6011, "Lâmina Análise Simples", 6000011, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(6012, "Lâmina Análise Dupla", 6000012, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(6013, "Lâmina Análise Tripla", 6000013, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        // Tubo sem anticoagulante (Exame de Glicemia)
        insumos.add(new Insumo(7021, "Tubo sem Anticoagulante Pequeno", 7000021, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(7022, "Tubo sem Anticoagulante Médio", 7000022, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));
        insumos.add(new Insumo(7023, "Tubo sem Anticoagulante Grande", 7000023, QUANTIDADE_INICIAL, QUANTIDADE_MAXIMA));

        salvarEstoque();
    }

    /**
     * Carrega o estoque do arquivo ou inicializa se não existir
     */
    private void carregarEstoque() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_ESTOQUE))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    Insumo insumo = Insumo.fromStringArquivo(linha);
                    if (insumo != null) {
                        insumos.add(insumo);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo não existe, inicializa estoque
            inicializarEstoque();
        } catch (IOException e) {
            System.out.println("Erro ao carregar estoque: " + e.getMessage());
            inicializarEstoque();
        }
    }

    /**
     * Salva o estoque no arquivo
     */
    public void salvarEstoque() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ESTOQUE))) {
            for (Insumo insumo : insumos) {
                writer.println(insumo.paraStringArquivo());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar estoque: " + e.getMessage());
        }
    }

    /**
     * Exibe todos os insumos do estoque
     */
    public void exibirEstoque() {
        System.out.println();
        System.out.println("=== ESTOQUE DE INSUMOS ===");

        for (Insumo insumo : insumos) {
            insumo.exibirDados();
        }
    }

    /**
     * Busca insumo por ID ou código de barras
     */
    public Insumo buscarInsumo(int identificador) {
        for (Insumo insumo : insumos) {
            if (insumo.getId() == identificador || insumo.getCodigoBarras() == identificador) {
                return insumo;
            }
        }
        return null;
    }

    /**
     * Adiciona quantidade a um insumo
     */
    public boolean adicionarQuantidade(int identificador, int quantidade) {
        Insumo insumo = buscarInsumo(identificador);
        if (insumo != null) {
            boolean sucesso = insumo.adicionarQuantidade(quantidade);
            if (sucesso) {
                salvarEstoque();
            }
            return sucesso;
        }
        return false;
    }

    /**
     * Remove quantidade de um insumo
     */
    public boolean removerQuantidade(int identificador, int quantidade) {
        Insumo insumo = buscarInsumo(identificador);
        if (insumo != null) {
            boolean sucesso = insumo.removerQuantidade(quantidade);
            if (sucesso) {
                salvarEstoque();
            }
            return sucesso;
        }
        return false;
    }

    /**
     * Calcula a quantidade máxima que pode ser adicionada a um insumo
     */
    public int calcularQuantidadeMaximaAdicao(int identificador) {
        Insumo insumo = buscarInsumo(identificador);
        if (insumo != null) {
            return insumo.getQuantidadeMaxima() - insumo.getQuantidadeDisponivel();
        }
        return 0;
    }

    public List<Insumo> getInsumos() {
        return insumos;
    }

    /**
     * Retorna os insumos específicos para um exame
     */
    public List<Insumo> getInsumosPorExame(String nomeExame) {
        List<Insumo> insumosExame = new ArrayList<>();

        switch (nomeExame) {
            case "Hemograma Completo":
                // Tubos de coleta
                insumosExame.addAll(buscarInsumosPorTipo("Tubo de Coleta"));
                // Agulhas
                insumosExame.addAll(buscarInsumosPorTipo("Agulha"));
                // Seringas
                insumosExame.addAll(buscarInsumosPorTipo("Seringa"));
                break;

            case "Exame de Urina":
                // Recipientes estéril
                insumosExame.addAll(buscarInsumosPorTipo("Recipiente Estéril"));
                // Tiras reagentes
                insumosExame.addAll(buscarInsumosPorTipo("Tira Reagente"));
                // Lâminas
                insumosExame.addAll(buscarInsumosPorTipo("Lâmina Análise"));
                break;

            case "Exame de Glicemia":
                // Tubos sem anticoagulante
                insumosExame.addAll(buscarInsumosPorTipo("Tubo sem Anticoagulante"));
                // Agulhas
                insumosExame.addAll(buscarInsumosPorTipo("Agulha"));
                // Seringas
                insumosExame.addAll(buscarInsumosPorTipo("Seringa"));
                // Tiras reagentes
                insumosExame.addAll(buscarInsumosPorTipo("Tira Reagente"));
                break;
        }

        return insumosExame;
    }

    /**
     * Busca insumos que contenham determinado tipo no nome
     */
    private List<Insumo> buscarInsumosPorTipo(String tipo) {
        List<Insumo> insumosTipo = new ArrayList<>();
        for (Insumo insumo : insumos) {
            if (insumo.getNome().startsWith(tipo)) {
                insumosTipo.add(insumo);
            }
        }
        return insumosTipo;
    }
}