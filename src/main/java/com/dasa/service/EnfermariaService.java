package dasa.service;

import dasa.controller.dao.*;
import dasa.controller.dao.jdbc.*;
import dasa.model.funcionarios.Enfermeiro;
import dasa.model.domain.Atendimento;

import java.util.ArrayList;
import java.util.List;

public class EnfermariaService {

    private FuncionarioDao funcionarioDao;
    private AtendimentoDao atendimentoDao;

    public EnfermariaService() {
        this.funcionarioDao = new JdbcFuncionarioDao();
        this.atendimentoDao = new JdbcAtendimentoDao();
    }

    // Construtor para injeção de dependência
    public EnfermariaService(FuncionarioDao funcionarioDao, AtendimentoDao atendimentoDao) {
        this.funcionarioDao = funcionarioDao;
        this.atendimentoDao = atendimentoDao;
    }

    /**
     * Lista todos os enfermeiros
     */
    public List<Enfermeiro> listarTodosEnfermeiros() {
        return funcionarioDao.listarTodosEnfermeiros();
    }

    /**
     * Lista enfermeiros por especialidade
     */
    public List<Enfermeiro> listarEnfermeirosPorEspecialidade(String especialidade) {
        validarEspecialidade(especialidade);
        return funcionarioDao.listarEnfermeirosPorEspecialidade(especialidade);
    }

    /**
     * Lista atendimentos realizados por um enfermeiro
     */
    public List<Atendimento> listarAtendimentosPorEnfermeiro(int coren) {
        // Verifica se o enfermeiro existe
        Enfermeiro enfermeiro = funcionarioDao.buscarEnfermeiroPorCoren(coren);
        if (enfermeiro == null) {
            throw new IllegalArgumentException("Enfermeiro não encontrado com COREN: " + coren);
        }

        return atendimentoDao.listarPorEnfermeiro(coren);
    }

    /**
     * Lista enfermeiros que já atenderam pacientes
     */
     public List<Enfermeiro> listarEnfermeirosQueAtenderam() {
        List<Enfermeiro> todosEnfermeiros = funcionarioDao.listarTodosEnfermeiros();
        List<Enfermeiro> enfermeirosQueAtenderam = new ArrayList<>();

        for (Enfermeiro enfermeiro : todosEnfermeiros) {
            List<Atendimento> atendimentosRealizados = atendimentoDao.listarPorEnfermeiro(enfermeiro.getCoren());
            if (!atendimentosRealizados.isEmpty()) {
                enfermeirosQueAtenderam.add(enfermeiro);
            }
        }

        return enfermeirosQueAtenderam;
    }

    /**
     * Busca enfermeiro por COREN
     */
    public Enfermeiro buscarEnfermeiroPorCoren(int coren) {
        if (coren <= 0) {
            throw new IllegalArgumentException("COREN inválido!");
        }

        Enfermeiro enfermeiro = funcionarioDao.buscarEnfermeiroPorCoren(coren);
        if (enfermeiro == null) {
            throw new IllegalArgumentException("Enfermeiro não encontrado com COREN: " + coren);
        }

        return enfermeiro;
    }

    /**
     * Conta total de atendimentos por enfermeiro
     */
    public int contarAtendimentosPorEnfermeiro(int coren) {
        return listarAtendimentosPorEnfermeiro(coren).size();
    }

    // Validação privada
    private void validarEspecialidade(String especialidade) {
        if (especialidade == null || especialidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidade não pode estar vazia!");
        }

        if (!especialidade.equals("Hemograma Completo") &&
                !especialidade.equals("Exame de Urina") &&
                !especialidade.equals("Exame de Glicemia")) {
            throw new IllegalArgumentException(
                    "Especialidade inválida! Use: Hemograma Completo, Exame de Urina ou Exame de Glicemia"
            );
        }
    }
}
