package dasa.service;

import dasa.controller.dao.*;
import dasa.controller.dao.jdbc.*;
import dasa.model.domain.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RecepcaoService {

    private PacienteDao pacienteDao;
    private AtendimentoDao atendimentoDao;
    private ExameDao exameDao;
    private static final Pattern NOME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1\\s]+$");

    public RecepcaoService() {
        this.pacienteDao = new JdbcPacienteDao();
        this.atendimentoDao = new JdbcAtendimentoDao();
        this.exameDao = new JdbcExameDao();
    }

    // Construtor para injeção de dependência (útil para testes)
    public RecepcaoService(PacienteDao pacienteDao, AtendimentoDao atendimentoDao, ExameDao exameDao) {
        this.pacienteDao = pacienteDao;
        this.atendimentoDao = atendimentoDao;
        this.exameDao = exameDao;
    }

    /**
     * Cadastra um novo paciente com todas as validações
     */
    public Long cadastrarPaciente(String nomeCompleto, String cpf, String dataNascimento,
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

        // Cria e salva o paciente (apenas dados básicos)
        Paciente novoPaciente = new Paciente();
        novoPaciente.setNomeCompleto(nomeCompleto.trim());
        novoPaciente.setCpf(cpf);
        novoPaciente.setDataNascimento(LocalDate.parse(dataNascimento,
                DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        novoPaciente.setConvenio(convenio);
        novoPaciente.setPreferencial(preferencial);

        Long pacienteId = pacienteDao.salvar(novoPaciente);

        // Cria o primeiro atendimento
        Atendimento atendimento = new Atendimento(pacienteId.intValue(), nomeExame, jejum);
        // Salva no banco
        Long atendimentoId = atendimentoDao.salvar(atendimento);

        return atendimentoId; // Retorna o ID do atendimento
    }

    /**
     * Cadastra novo atendimento para paciente existente
     */
    public Long cadastrarNovoExameParaPaciente(int pacienteId, boolean jejum, String nomeExame) {
        Paciente paciente = pacienteDao.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado!");
        }

        // Cria novo registro com os dados do paciente existente
        Paciente novoExame = new Paciente(
                paciente.getNomeCompleto(),
                paciente.getCpf(),
                paciente.getDataNascimentoFormatada(),
                paciente.isConvenio(),
                paciente.isPreferencial(),
                jejum,
                nomeExame
        );

        validarExame(nomeExame);

        // Cria novo atendimento
        Atendimento atendimento = new Atendimento(pacienteId, nomeExame, jejum);
        return atendimentoDao.salvar(atendimento);
    }

    /**
     * Lista todos os atendimentos
     */
    public List<Atendimento> listarTodosAtendimentos() {
        return atendimentoDao.listarTodos();
    }

    /**
     * Lista todos os pacientes
     */
    public List<Paciente> listarTodosPacientes() {
        return pacienteDao.listarTodos();
    }

    /**
     * Lista atendimentos por status
     */
    public List<Atendimento> listarAtendimentosPorStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status não pode estar vazio!");
        }

        if (!status.equals("Em espera") && !status.equals("Atendido") && !status.equals("Cancelado")) {
            throw new IllegalArgumentException("Status inválido!");
        }

        return atendimentoDao.listarPorStatus(status);
    }

    /**
     * Busca Atendimento por ID
     */
    public Atendimento buscarAtendimentoPorId(int id) {
        return atendimentoDao.buscarPorId(id);
    }

    /**
     * Lista histórico de atendimentos por CPF
     */
    public List<Atendimento> listarHistoricoExamesPorCpf(String cpf) {
        Paciente paciente = pacienteDao.buscarPorCpf(cpf);
        if (paciente == null) {
            return new ArrayList<>();
        }
        return atendimentoDao.listarPorPaciente(paciente.getId());
    }

    public Paciente buscarPacientePorCpf(String cpf) {
        return pacienteDao.buscarPorCpf(cpf);
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

    /**
     * Atualiza dados do paciente
     */
    public void atualizarPaciente(Paciente paciente) {
        if (paciente == null || paciente.getId() <= 0) {
            throw new IllegalArgumentException("Paciente inválido!");
        }

        // Validações
        validarNomeCompleto(paciente.getNomeCompleto());
        validarDataNascimento(paciente.getDataNascimentoFormatada());

        pacienteDao.atualizar(paciente);
    }

    /**
     * Alterna status de convênio do paciente
     */
    public void alternarConvenio(int pacienteId) {
        Paciente paciente = pacienteDao.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado!");
        }

        paciente.setConvenio(!paciente.isConvenio());
        pacienteDao.atualizar(paciente);
    }

    /**
     * Alterna status preferencial do paciente
     */
    public void alternarPreferencial(int pacienteId) {
        Paciente paciente = pacienteDao.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado!");
        }

        paciente.setPreferencial(!paciente.isPreferencial());
        pacienteDao.atualizar(paciente);
    }

    /**
     * Exclui paciente e cancela atendimentos em espera
     */
    public void excluirPaciente(int pacienteId) {
        Paciente paciente = pacienteDao.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado!");
        }

        // Buscar atendimentos do paciente
        List<Atendimento> atendimentos = atendimentoDao.listarPorPaciente(pacienteId);

        // Cancelar atendimentos "Em espera"
        for (Atendimento atendimento : atendimentos) {
            if ("Em espera".equals(atendimento.getStatus())) {
                atendimentoDao.atualizarStatus(atendimento.getId(), "Cancelado", 0, 0);
            }
        }

        pacienteDao.excluir(pacienteId);
    }

    /**
     * Exclui paciente por CPF
     */
    public void excluirPacientePorCpf(String cpf) {
        Paciente paciente = pacienteDao.buscarPorCpf(cpf);
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado com CPF: " + cpf);
        }

        excluirPaciente(paciente.getId());
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

    private void validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF não pode estar vazio!");
        }

        // Remove pontos e traços se houver
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter exatamente 11 dígitos!");
        }

        // Validação básica de CPF inválido (todos dígitos iguais)
        if (cpf.matches("(\\d)\\1{10}")) {
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
