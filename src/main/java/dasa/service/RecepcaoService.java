package dasa.service;

import dasa.controller.dao.PacienteDao;
import dasa.controller.dao.ExameDao;
import dasa.controller.dao.jdbc.JdbcPacienteDao;
import dasa.controller.dao.jdbc.JdbcExameDao;
import dasa.modelo.Paciente;
import dasa.modelo.Exame;

import java.util.List;
import java.util.regex.Pattern;

public class RecepcaoService {

    private PacienteDao pacienteDao;
    private ExameDao exameDao;
    private static final Pattern NOME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]+$");

    public RecepcaoService() {
        this.pacienteDao = new JdbcPacienteDao();
        this.exameDao = new JdbcExameDao();
    }

    // Construtor para injeção de dependência (útil para testes)
    public RecepcaoService(PacienteDao pacienteDao, ExameDao exameDao) {
        this.pacienteDao = pacienteDao;
        this.exameDao = exameDao;
    }

    /**
     * Cadastra um novo paciente com todas as validações
     */
    public Long cadastrarPaciente(String nomeCompleto, long cpf, String dataNascimento,
                                  boolean convenio, boolean preferencial, boolean jejum,
                                  String nomeExame) {

        // Validações
        validarNomeCompleto(nomeCompleto);
        validarCPF(cpf);
        validarDataNascimento(dataNascimento);
        validarExame(nomeExame);

        // Verifica se CPF já existe
        if (pacienteDao.buscarPorCpf(cpf) != null) {
            throw new IllegalArgumentException("CPF já cadastrado no sistema!");
        }

        // Cria o paciente
        Paciente novoPaciente = new Paciente(
                nomeCompleto.trim(), cpf, dataNascimento,
                convenio, preferencial, jejum, nomeExame
        );

        // Salva no banco
        return pacienteDao.salvar(novoPaciente);
    }

    /**
     * Lista todos os pacientes
     */
    public List<Paciente> listarTodosPacientes() {
        return pacienteDao.listarTodos();
    }

    /**
     * Lista pacientes por status
     */
    public List<Paciente> listarPacientesPorStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode estar vazio!");
        }

        if (!status.equals("Em espera") && !status.equals("Atendido") && !status.equals("Cancelado")) {
            throw new IllegalArgumentException("Status inválido! Use: Em espera, Atendido ou Cancelado");
        }

        return pacienteDao.listarPorStatus(status);
    }

    /**
     * Lista todos os exames disponíveis
     */
    public List<Exame> listarExamesDisponiveis() {
        return exameDao.listarTodos();
    }

    /**
     * Busca paciente por ID
     */
    public Paciente buscarPacientePorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido!");
        }

        Paciente paciente = pacienteDao.buscarPorId(id);
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado com ID: " + id);
        }

        return paciente;
    }

    // VALIDAÇÕES PRIVADAS

    private void validarNomeCompleto(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode estar vazio!");
        }

        if (nome.trim().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres!");
        }

        if (nome.trim().length() > 120) {
            throw new IllegalArgumentException("Nome não pode exceder 120 caracteres!");
        }

        if (!NOME_PATTERN.matcher(nome).matches()) {
            throw new IllegalArgumentException("Nome deve conter apenas letras e espaços!");
        }
    }

    private void validarCPF(long cpf) {
        String cpfStr = String.valueOf(cpf);
        if (cpfStr.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter exatamente 11 dígitos!");
        }

        // Validação básica de CPF inválido (todos dígitos iguais)
        if (cpfStr.matches("(\\d)\\1{10}")) {
            throw new IllegalArgumentException("CPF inválido!");
        }
    }

    private void validarDataNascimento(String data) {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Data de nascimento não pode estar vazia!");
        }

        // Valida formato dd/MM/yyyy
        if (!data.matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new IllegalArgumentException("Data deve estar no formato dd/MM/yyyy!");
        }

        String[] partes = data.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);

        if (ano < 1900 || ano > 2024) {
            throw new IllegalArgumentException("Ano deve estar entre 1900 e 2024!");
        }

        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mês inválido!");
        }

        int[] diasPorMes = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (dia < 1 || dia > diasPorMes[mes - 1]) {
            throw new IllegalArgumentException("Dia inválido para o mês informado!");
        }
    }

    private void validarExame(String nomeExame) {
        if (nomeExame == null || nomeExame.trim().isEmpty()) {
            throw new IllegalArgumentException("Exame não pode estar vazio!");
        }

        Exame exame = exameDao.buscarPorNome(nomeExame);
        if (exame == null) {
            throw new IllegalArgumentException("Exame não encontrado: " + nomeExame);
        }
    }
}
