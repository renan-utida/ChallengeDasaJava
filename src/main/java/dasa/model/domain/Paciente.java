package dasa.model.domain;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Classe para representar os pacientes - Entidade de domínio
 */
public class Paciente {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private String nomeCompleto;
    private String cpf;
    private LocalDate dataNascimento;
    private String statusPaciente = "Ativo";
    private boolean convenio;
    private boolean preferencial;

    // Construtor para novo paciente
    public Paciente(String nomeCompleto, String cpf, String dataNascimentoStr,
                    boolean convenio, boolean preferencial) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = LocalDate.parse(dataNascimentoStr, dateFormatter);
        this.convenio = convenio;
        this.preferencial = preferencial;
        this.statusPaciente = "Ativo";
    }

//    // Construtor completo (para carregar do banco)
//    public Paciente(int id, String nomeCompleto, String cpf, String dataNascimentoStr,
//                    String dataExameStr, boolean convenio, boolean preferencial, boolean jejum,
//                    String exame, String status, String enfermeiroResponsavel, String responsavelColeta) {
//        this.id = id;
//        this.nomeCompleto = nomeCompleto;
//        this.cpf = cpf;
//        this.dataNascimento = LocalDate.parse(dataNascimentoStr, dateFormatter);
//        this.dataExame = LocalDateTime.parse(dataExameStr, dateTimeFormatter);
//        this.convenio = convenio;
//        this.preferencial = preferencial;
//        this.jejum = jejum;
//        this.exame = exame;
//        this.status = status;
//        this.enfermeiroResponsavel = enfermeiroResponsavel;
//        this.responsavelColeta = responsavelColeta;
//    }

    // Construtor vazio
    public Paciente() {
        this.statusPaciente = "Ativo";
    }

    /**
     * Formata CPF para exibição
     */
    public String getCpfFormatado() {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    /**
     * Formata data de nascimento para manter compatibilidade com string
    */
    public String getDataNascimentoFormatada() { return dataNascimento.format(dateFormatter); }

    /**
     * Exibe os dados do paciente formatados
     */
    public void exibirDados() {
        System.out.println("ID Paciente: #" + id);
        System.out.println("\tNome Completo: " + nomeCompleto);
        System.out.println("\tCPF: " + getCpfFormatado());
        System.out.println("\tData Nascimento: " + dataNascimento.format(dateFormatter));
        System.out.println("\tConvenio: " + (convenio ? "Sim" : "Não"));
        System.out.println("\tPreferencial: " + (preferencial ? "Sim" : "Não"));
        System.out.println("\tStatus Paciente: " + statusPaciente);
        System.out.println("========================================================");
    }

    /**
     * Sobrecarga: Exibe dados do paciente filtrados por categoria
     * @param categoria "basico", "administrativo"
     */
    public void exibirDados(String categoria) {
        System.out.println("ID Paciente: #" + id + " -  Nome: " + nomeCompleto);

        switch (categoria.toLowerCase()) {
            case "basico":
                System.out.println("\tCPF: " + getCpfFormatado());
                System.out.println("\tData Nascimento: " + dataNascimento.format(dateFormatter));
                break;

            case "administrativo":
                System.out.println("\tConvenio: " + (convenio ? "Sim" : "Não"));
                System.out.println("\tPreferencial: " + (preferencial ? "Sim" : "Não"));
                System.out.println("\tStatus Paciente: " + statusPaciente);
                break;

            default:
                exibirDados(); // Exibição completa como fallback
                return;
        }
        System.out.println("========================================");
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getStatusPaciente(){ return statusPaciente; }
    public void setStatusPaciente(String statusPaciente) { this.statusPaciente = statusPaciente; }

    public boolean isConvenio() { return convenio; }
    public void setConvenio(boolean convenio) { this.convenio = convenio; }

    public boolean isPreferencial() { return preferencial; }
    public void setPreferencial(boolean preferencial) { this.preferencial = preferencial; }
}
