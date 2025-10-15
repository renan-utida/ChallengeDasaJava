package com.dasa.model.domain;

import com.dasa.model.converters.BooleanToSimNaoConverter;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Classe para representar os pacientes - Entidade de domínio
 * Funciona com JDBC (Console/Swing) E JPA (REST API)
 */
@Entity
@Table(name = "dasa_pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Paciente {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_paciente")
    @SequenceGenerator(
            name = "seq_paciente",
            sequenceName = "seq_paciente_id",
            allocationSize = 1
    )
    @Column(name = "id")
    private int id;

    @Column(name = "nome_completo", length = 120, nullable = false)
    private String nomeCompleto;

    @Column(name = "cpf", length = 11, unique = true, nullable = false)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "status_paciente", length = 10)
    private String statusPaciente = "Ativo";

    @Column(name = "convenio", length = 1, nullable = false)
    @Convert(converter = BooleanToSimNaoConverter.class)
    private boolean convenio;

    @Column(name = "preferencial", length = 1, nullable = false)
    @Convert(converter = BooleanToSimNaoConverter.class)
    private boolean preferencial;

    /**
     * Construtor para novo paciente (Jdbc/Console/Swing)
     */
    public Paciente(String nomeCompleto, String cpf, String dataNascimentoStr,
                    boolean convenio, boolean preferencial) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = LocalDate.parse(dataNascimentoStr, dateFormatter);
        this.convenio = convenio;
        this.preferencial = preferencial;
        this.statusPaciente = "Ativo";
    }

    /**
     * Formata CPF para exibição
     * @Transient = NÃO persiste no banco (metodo calculado)
     */
    @Transient
    public String getCpfFormatado() {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
                cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    /**
     * Formata data de nascimento para manter compatibilidade com string
     * @Transient = metodo auxiliar, não é coluna do banco
    */
    @Transient
    public String getDataNascimentoFormatada() { return dataNascimento.format(dateFormatter); }

    /**
     * Exibe os dados do paciente formatados (Console/Swing)
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
     * Sobrecarga: Exibe dados do paciente filtrados por categoria (Console/Swing)
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
}
