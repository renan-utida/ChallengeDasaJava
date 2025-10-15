package dasa.model.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Atendimento {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private int pacienteId;
    private Paciente paciente; // Referência ao paciente
    private String exame;
    private LocalDateTime dataExame;
    private boolean jejum;
    private String status;
    private String enfermeiroResponsavel;
    private String responsavelColeta;

    // Construtor para novo atendimento
    public Atendimento(int pacienteId, String exame, boolean jejum) {
        this.pacienteId = pacienteId;
        this.exame = exame;
        this.jejum = jejum;
        this.dataExame = LocalDateTime.now();
        this.status = "Em espera";
        this.enfermeiroResponsavel = "Em espera";
        this.responsavelColeta = "Em espera";
    }

    // Construtor completo
    public Atendimento(int id, int pacienteId, String exame, LocalDateTime dataExame,
                       boolean jejum, String status, String enfermeiroResponsavel,
                       String responsavelColeta) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.exame = exame;
        this.dataExame = dataExame;
        this.jejum = jejum;
        this.status = status;
        this.enfermeiroResponsavel = enfermeiroResponsavel;
        this.responsavelColeta = responsavelColeta;
    }

    public void exibirDados() {
        System.out.println("ID Atendimento: #" + id);
        System.out.println("Status: " + status);
        if (paciente != null) {
            System.out.println("\tPaciente: " + paciente.getNomeCompleto());
            System.out.println("\tCPF: " + paciente.getCpfFormatado());
            System.out.println("\tData Nascimento: " +
                    paciente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("\tConvênio: " + (paciente.isConvenio() ? "Sim" : "Não"));
            System.out.println("\tPreferencial: " + (paciente.isPreferencial() ? "Sim" : "Não"));
        }
        System.out.println("\tExame: " + exame);
        System.out.println("\tJejum (min. 8 horas): " + (jejum ? "Sim" : "Não"));
        System.out.println("\tData do Exame: " + dataExame.format(dateTimeFormatter));
        System.out.println("\tEnfermeiro Responsável: " + enfermeiroResponsavel);
        System.out.println("\tResponsável Coleta de Insumos: " + responsavelColeta);
        System.out.println("========================================================");
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public String getExame() { return exame; }
    public void setExame(String exame) { this.exame = exame; }

    public LocalDateTime getDataExame() { return dataExame; }
    public void setDataExame(LocalDateTime dataExame) { this.dataExame = dataExame; }

    public boolean isJejum() { return jejum; }
    public void setJejum(boolean jejum) { this.jejum = jejum; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEnfermeiroResponsavel() { return enfermeiroResponsavel; }
    public void setEnfermeiroResponsavel(String enfermeiroResponsavel) {
        this.enfermeiroResponsavel = enfermeiroResponsavel;
    }

    public String getResponsavelColeta() { return responsavelColeta; }
    public void setResponsavelColeta(String responsavelColeta) {
        this.responsavelColeta = responsavelColeta;
    }
}