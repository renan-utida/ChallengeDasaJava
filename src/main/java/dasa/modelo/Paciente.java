package dasa.modelo;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para representar os pacientes
 */
public class Paciente {
    private static int contadorId = 1;
    private static String ARQUIVO_PACIENTES = "pacientes.txt";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private String nomeCompleto;
    private long cpf;
    private LocalDate dataNascimento;
    private LocalDateTime dataExame;
    private boolean convenio;
    private boolean preferencial;
    private boolean jejum;
    private String exame;
    private String status;
    private String enfermeiroResponsavel;
    private String responsavelColeta;

    // Bloco estático para inicializar o contador
    static {
        inicializarContador();
    }

    // Inicializa o contador baseado no último ID do arquivo
    private static void inicializarContador() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PACIENTES))) {
            String linha;
            int maiorId = 0;

            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split("\\|");
                    if (dados.length >= 1) {
                        try {
                            int id = Integer.parseInt(dados[0]);
                            if (id > maiorId) {
                                maiorId = id;
                            }
                        } catch (NumberFormatException e) {
                            // Ignora linhas com ID inválido
                        }
                    }
                }
            }

            if (maiorId > 0) {
                contadorId = maiorId + 1;
            }

        } catch (FileNotFoundException e) {
            // Arquivo ainda não existe, mantém contador = 1
            System.out.println("ℹ️ Arquivo de pacientes não encontrado. Iniciando do ID 1.");
        } catch (IOException e) {
            System.out.println("⚠️ Erro ao ler arquivo. Iniciando do ID 1.");
        }
    }

    // Construtor
    public Paciente(String nomeCompleto, long cpf, String dataNascimentoStr,
                    boolean convenio, boolean preferencial, boolean jejum, String exame) {
        this.id = obterProximoId();
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = LocalDate.parse(dataNascimentoStr, dateFormatter);;
        this.dataExame = LocalDateTime.now();
        this.convenio = convenio;
        this.preferencial = preferencial;
        this.jejum = jejum;
        this.exame = exame;
        this.status = "Em espera";
        this.enfermeiroResponsavel = "Em espera";
        this.responsavelColeta = "Em espera";
    }

    // Construtor para carregar do arquivo
    public Paciente(int id, String nomeCompleto, long cpf, String dataNascimentoStr,
                    String dataExameStr, boolean convenio, boolean preferencial, boolean jejum,
                    String exame, String status, String enfermeiroResponsavel, String responsavelColeta) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = LocalDate.parse(dataNascimentoStr, dateFormatter);
        this.dataExame = LocalDateTime.parse(dataExameStr, dateTimeFormatter);
        this.convenio = convenio;
        this.preferencial = preferencial;
        this.jejum = jejum;
        this.exame = exame;
        this.status = status;
        this.enfermeiroResponsavel = enfermeiroResponsavel;
        this.responsavelColeta = responsavelColeta;

        // Atualiza o contador se necessário
        if (id >= contadorId) {
            contadorId = id + 1;
        }
    }

    /**
     * Obtém o próximo ID disponível
     */
    private static synchronized int obterProximoId() {
        return contadorId++;
    }

    /**
     * Valida CPF (formato básico: 11 dígitos)
     */
    public static boolean validarCPF(long cpf) {
        String cpfStr = String.valueOf(cpf);
        return cpfStr.length() == 11;
    }

    /**
     * Formata CPF para exibição
     */
    public String getCpfFormatado() {
        String cpfStr = String.format("%011d", cpf);
        return cpfStr.substring(0, 3) + "." + cpfStr.substring(3, 6) + "." +
                cpfStr.substring(6, 9) + "-" + cpfStr.substring(9, 11);
    }

    /**
     * Salva um paciente no arquivo
     */
    public void salvarNoArquivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PACIENTES, true))) {
            writer.println(this.paraStringArquivo());
        } catch (IOException e) {
            System.out.println("Erro ao salvar paciente: " + e.getMessage());
        }
    }

    /**
     * Carrega todos os pacientes do arquivo
     */
    public static List<Paciente> carregarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PACIENTES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    Paciente paciente = fromStringArquivo(linha);
                    if (paciente != null) {
                        pacientes.add(paciente);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo ainda não existe, retorna lista vazia
        } catch (IOException e) {
            System.out.println("Erro ao carregar pacientes: " + e.getMessage());
        }

        return pacientes;
    }

    /**
     * Converte o paciente para string para salvar no arquivo
     */
    public String paraStringArquivo() {
        return id + "|" + nomeCompleto + "|" + cpf + "|" + dataNascimento.format(dateFormatter) + "|" +
                dataExame.format(dateTimeFormatter) + "|" + convenio + "|" + preferencial + "|" + jejum + "|" +
                exame + "|" + status + "|" + enfermeiroResponsavel + "|" + responsavelColeta;
    }

    /**
     * Cria um paciente a partir de uma string do arquivo
     */
    private static Paciente fromStringArquivo(String linha) {
        try {
            String[] dados = linha.split("\\|");
            if (dados.length == 12) {
                return new Paciente(
                        Integer.parseInt(dados[0]), // id
                        dados[1], // nomeCompleto
                        Long.parseLong(dados[2]), // cpf
                        dados[3], // dataNascimento (string que será convertida)
                        dados[4], // dataExame (string que será convertida)
                        Boolean.parseBoolean(dados[5]), // convenio
                        Boolean.parseBoolean(dados[6]), // preferencial
                        Boolean.parseBoolean(dados[7]), // jejum
                        dados[8], // exame
                        dados[9], // status
                        dados[10], // enfermeiroResponsavel
                        dados[11]  // responsavelColeta
                );
            }
        } catch (Exception e) {
            System.out.println("Erro ao processar linha do arquivo: " + linha);
        }
        return null;
    }

    /**
     * Exibe os dados do paciente formatados
     */
    public void exibirDados() {
        System.out.println("ID: #" + id);
        System.out.println("Status: " + status);
        System.out.println("\tNome Completo: " + nomeCompleto);
        System.out.println("\tCPF: " + getCpfFormatado());
        System.out.println("\tData Nascimento: " + dataNascimento.format(dateFormatter));
        System.out.println("\tConvenio: " + (convenio ? "Sim" : "Não"));
        System.out.println("\tPreferencial: " + (preferencial ? "Sim" : "Não"));
        System.out.println("\tJejum (min. 8 horas): " + (jejum ? "Sim" : "Não"));
        System.out.println("\tExame: " + exame);
        System.out.println("\tData de Realização de Entrada do Exame: " + dataExame.format(dateTimeFormatter));
        System.out.println("\tEnfermeiro Responsável: " + enfermeiroResponsavel);
        System.out.println("\tResponsável Coleta de Insumos: " + responsavelColeta);
        System.out.println("========================================================");
    }

    /**
     * Sobrecarga: Exibe dados do paciente de forma resumida ou completa
     * @param resumido true para exibição resumida, false para completa
     */
    public void exibirDados(boolean resumido) {
        if (resumido) {
            // Exibição resumida - apenas dados essenciais
            System.out.println("ID: #" + id + " | " + nomeCompleto + " | " + exame + " | Status: " + status);
        } else {
            // Exibição completa - chama o metodo principal
            exibirDados();
        }
    }

    /**
     * Sobrecarga: Exibe dados do paciente filtrados por categoria
     * @param categoria "basico", "medico", "administrativo"
     */
    public void exibirDados(String categoria) {
        System.out.println("ID: #" + id + " - " + nomeCompleto);
        System.out.println("Status: " + status);

        switch (categoria.toLowerCase()) {
            case "basico":
                System.out.println("\tCPF: " + getCpfFormatado());
                System.out.println("\tData Nascimento: " + dataNascimento.format(dateFormatter));
                break;

            case "medico":
                System.out.println("\tExame: " + exame);
                System.out.println("\tJejum (min. 8 horas): " + (jejum ? "Sim" : "Não"));
                System.out.println("\tData de Realização de Entrada: " + dataExame.format(dateTimeFormatter));
                System.out.println("\tEnfermeiro Responsável: " + enfermeiroResponsavel);
                break;

            case "administrativo":
                System.out.println("\tConvenio: " + (convenio ? "Sim" : "Não"));
                System.out.println("\tPreferencial: " + (preferencial ? "Sim" : "Não"));
                System.out.println("\tResponsável Coleta: " + responsavelColeta);
                break;

            default:
                exibirDados(); // Exibição completa como fallback
                return;
        }
        System.out.println("========================================");
    }

    // Getters e Setters
    public int getId() { return id; }

    public String getNomeCompleto() { return nomeCompleto; }

    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public long getCpf() { return cpf; }

    public void setCpf(long cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }

    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    // Metodo auxiliar para manter compatibilidade com string
    public String getDataNascimentoFormatada() { return dataNascimento.format(dateFormatter); }

    public LocalDateTime getDataExame() { return dataExame; }

    public void setDataExame(LocalDateTime dataExame) { this.dataExame = dataExame; }

    // Metodo auxiliar para manter compatibilidade com string
    public String getDataExameFormatada() { return dataExame.format(dateTimeFormatter); }

    public boolean isConvenio() { return convenio; }

    public void setConvenio(boolean convenio) { this.convenio = convenio; }

    public boolean isPreferencial() { return preferencial; }

    public void setPreferencial(boolean preferencial) { this.preferencial = preferencial; }

    public boolean isJejum() { return jejum; }

    public void setJejum(boolean jejum) { this.jejum = jejum; }

    public String getExame() { return exame; }

    public void setExame(String exame) { this.exame = exame; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getEnfermeiroResponsavel() { return enfermeiroResponsavel; }

    public void setEnfermeiroResponsavel(String enfermeiroResponsavel) { this.enfermeiroResponsavel = enfermeiroResponsavel; }

    public String getResponsavelColeta() { return responsavelColeta; }

    public void setResponsavelColeta(String responsavelColeta) { this.responsavelColeta = responsavelColeta; }

    /**
     * Atualiza um paciente específico no arquivo
     */
    public static void atualizarPacienteNoArquivo(Paciente pacienteAtualizado) {
        List<Paciente> todosPacientes = carregarPacientes();

        // Encontra e atualiza o paciente
        for (int i = 0; i < todosPacientes.size(); i++) {
            if (todosPacientes.get(i).getId() == pacienteAtualizado.getId()) {
                todosPacientes.set(i, pacienteAtualizado);
                break;
            }
        }

        // Reescreve o arquivo com todos os pacientes atualizados
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PACIENTES))) {
            for (Paciente paciente : todosPacientes) {
                writer.println(paciente.paraStringArquivo());
            }
        } catch (IOException e) {
            System.out.println("Erro ao atualizar paciente: " + e.getMessage());
        }
    }

    /**
     * Filtra pacientes por status
     */
    public static List<Paciente> filtrarPorStatus(String status) {
        List<Paciente> todosPacientes = carregarPacientes();
        List<Paciente> pacientesFiltrados = new ArrayList<>();

        for (Paciente paciente : todosPacientes) {
            if (paciente.getStatus().equals(status)) {
                pacientesFiltrados.add(paciente);
            }
        }

        return pacientesFiltrados;
    }

    /**
     * Filtra pacientes por enfermeiro responsável
     */
    public static List<Paciente> filtrarPorEnfermeiroResponsavel(String enfermeiroInfo) {
        List<Paciente> todosPacientes = carregarPacientes();
        List<Paciente> pacientesFiltrados = new ArrayList<>();

        for (Paciente paciente : todosPacientes) {
            if (paciente.getEnfermeiroResponsavel().contains(enfermeiroInfo) &&
                    paciente.getStatus().equals("Atendido")) {
                pacientesFiltrados.add(paciente);
            }
        }

        return pacientesFiltrados;
    }
}
