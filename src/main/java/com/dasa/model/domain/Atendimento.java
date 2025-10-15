package com.dasa.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe para representar os atendimentos realizados pelos pacientes
 * Funciona com JDBC (Console/Swing) E JPA (REST API)
 */
@Entity
@Table(name = "dasa_atendimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_atendimento")
    @SequenceGenerator(
            name = "seq_atendimento",
            sequenceName = "seq_atendimento_id",
            allocationSize = 1
    )
    @Column(name = "id")
    private int id;

    @Column(name = "paciente_id", nullable = false)
    private int pacienteId;

    /**
     * Relacionamento ManyToOne com Paciente
     * FetchType.LAZY = carrega apenas quando acessar getPaciente()
     * insertable/updatable = false porque pacienteId já controla isso
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", insertable = false, updatable = false)
    private Paciente paciente; // Referência ao paciente

    /**
     * ATENÇÃO: exame_id no banco, mas usamos String exame no código
     * Mantém String para compatibilidade JDBC
     */
    @Column(name = "exame_id", insertable = false, updatable = false)
    @Transient
    private String exame;

    @Column(name = "data_exame", nullable = false)
    private LocalDateTime dataExame;

    @Column(name = "jejum", length = 1, nullable = false)
    private boolean jejum;

    @Column(name = "status_atendimento", length = 20)
    private String status;

    @Column(name = "enfermeiro_coren")
    private String enfermeiroResponsavel;

    @Column(name = "tecnico_crbm")
    private String responsavelColeta;


    // Construtores mantidos para compatibilidade JDBC/Console/Swing

    /**
     * Construtor para novo atendimento (Console/Swing)
     */
    public Atendimento(int pacienteId, String exame, boolean jejum) {
        this.pacienteId = pacienteId;
        this.exame = exame;
        this.jejum = jejum;
        this.dataExame = LocalDateTime.now();
        this.status = "Em espera";
        this.enfermeiroResponsavel = "Em espera";
        this.responsavelColeta = "Em espera";
    }

    /**
     * Construtor completo (JDBC)
     */
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

    /**
     * Exibe dados formatados (Console/Swing)
     */
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
}