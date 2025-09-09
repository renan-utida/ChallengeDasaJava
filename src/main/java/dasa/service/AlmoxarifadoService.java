package dasa.service;

import dasa.controller.dao.*;
import dasa.controller.dao.jdbc.*;
import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import dasa.modelo.Insumo;
import dasa.modelo.ItemCesta;
import dasa.modelo.Paciente;

import java.time.LocalDateTime;
import java.util.*;

public class AlmoxarifadoService {

    private InsumoDao insumoDao;
    private PacienteDao pacienteDao;
    private FuncionarioDao funcionarioDao;
    private HistoricoDao historicoDao;

    public AlmoxarifadoService() {
        this.insumoDao = new JdbcInsumoDao();
        this.pacienteDao = new JdbcPacienteDao();
        this.funcionarioDao = new JdbcFuncionarioDao();
        this.historicoDao = new JdbcHistoricoRetiradaDao();
    }

    // Construtor para injeção de dependência
    public AlmoxarifadoService(InsumoDao insumoDao, PacienteDao pacienteDao,
                               FuncionarioDao funcionarioDao, HistoricoDao historicoDao) {
        this.insumoDao = insumoDao;
        this.pacienteDao = pacienteDao;
        this.funcionarioDao = funcionarioDao;
        this.historicoDao = historicoDao;
    }

    /**
     * Processa a retirada completa de insumos
     */
    public void processarRetirada(int pacienteId, List<ItemCesta> itens,
                                  int tecnicoCrbm, int enfermeiroCoren) {

        // Validações
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Lista de itens não pode estar vazia!");
        }

        // Verifica se o paciente existe e está em espera
        Paciente paciente = pacienteDao.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente não encontrado!");
        }

        if (!paciente.getStatus().equals("Em espera")) {
            throw new IllegalArgumentException("Paciente não está com status 'Em espera'!");
        }

        // Verifica se técnico e enfermeiro existem
        TecnicoLaboratorio tecnico = funcionarioDao.buscarTecnicoPorCrbm(tecnicoCrbm);
        if (tecnico == null) {
            throw new IllegalArgumentException("Técnico não encontrado!");
        }

        Enfermeiro enfermeiro = funcionarioDao.buscarEnfermeiroPorCoren(enfermeiroCoren);
        if (enfermeiro == null) {
            throw new IllegalArgumentException("Enfermeiro não encontrado!");
        }

        // Valida se enfermeiro tem a especialidade correta
        if (!enfermeiro.getEspecialidade().equals(paciente.getExame())) {
            throw new IllegalArgumentException(
                    "Enfermeiro não tem especialidade para este exame! " +
                            "Especialidade do enfermeiro: " + enfermeiro.getEspecialidade() +
                            ", Exame do paciente: " + paciente.getExame()
            );
        }

        // Valida e remove os insumos do estoque
        for (ItemCesta item : itens) {
            if (item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero!");
            }

            Insumo insumo = insumoDao.buscarPorId(item.getInsumo().getId());
            if (insumo == null) {
                throw new IllegalArgumentException("Insumo não encontrado: " + item.getInsumo().getId());
            }

            if (insumo.getQuantidadeDisponivel() < item.getQuantidade()) {
                throw new IllegalArgumentException(
                        "Quantidade insuficiente de " + insumo.getNome() +
                                ". Disponível: " + insumo.getQuantidadeDisponivel() +
                                ", Solicitado: " + item.getQuantidade()
                );
            }

            // Remove do estoque
            boolean removido = insumoDao.removerQuantidade(insumo.getId(), item.getQuantidade());
            if (!removido) {
                throw new RuntimeException("Erro ao remover quantidade do estoque!");
            }
        }

        // Salva o histórico
        LocalDateTime dataRetirada = LocalDateTime.now();
        historicoDao.salvarRetirada(pacienteId, dataRetirada, tecnicoCrbm, enfermeiroCoren, itens);

        // Atualiza status do paciente
        pacienteDao.atualizarStatus(pacienteId, "Atendido", enfermeiroCoren, tecnicoCrbm);
    }

    /**
     * Adiciona quantidade ao estoque
     */
    public boolean adicionarEstoque(int identificador, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero!");
        }

        // Busca o insumo por ID ou código de barras
        Insumo insumo = insumoDao.buscarPorId(identificador);
        if (insumo == null) {
            insumo = insumoDao.buscarPorCodigoBarras(identificador);
        }

        if (insumo == null) {
            throw new IllegalArgumentException("Insumo não encontrado!");
        }

        // Calcula quantidade máxima que pode adicionar
        int maxAdicao = insumoDao.calcularQuantidadeMaximaAdicao(insumo.getId());
        if (maxAdicao <= 0) {
            throw new IllegalArgumentException("Este insumo já está no estoque máximo!");
        }

        if (quantidade > maxAdicao) {
            throw new IllegalArgumentException(
                    "Quantidade excede o máximo permitido! Máximo que pode adicionar: " + maxAdicao
            );
        }

        return insumoDao.adicionarQuantidade(insumo.getId(), quantidade);
    }

    /**
     * Lista insumos disponíveis para um exame
     */
    public List<Insumo> listarInsumosPorExame(String nomeExame) {
        if (nomeExame == null || nomeExame.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do exame não pode estar vazio!");
        }

        return insumoDao.listarPorExame(nomeExame);
    }

    /**
     * Lista todo o historico de retiradas
     */
    public List<Map<String, Object>> listarHistoricoCompleto() {
        return historicoDao.listarHistoricoCompleto();
    }

    /**
     * Busca insumo por identificador (ID ou código de barras)
     */
    public Insumo buscarInsumo(int identificador) {
        Insumo insumo = insumoDao.buscarPorId(identificador);
        if (insumo == null) {
            insumo = insumoDao.buscarPorCodigoBarras(identificador);
        }
        return insumo;
    }

    /**
     * Lista enfermeiros por especialidade
     */
    public List<Enfermeiro> listarEnfermeirosPorEspecialidade(String especialidade) {
        return funcionarioDao.listarEnfermeirosPorEspecialidade(especialidade);
    }
}
