package dasa.service;

import dasa.controller.dao.InsumoDao;
import dasa.controller.dao.jdbc.JdbcInsumoDao;
import dasa.model.domain.Insumo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EstoqueService {

    private InsumoDao insumoDao;
    private static final int QUANTIDADE_MAXIMA = 2000;
    private static final int QUANTIDADE_MINIMA = 100; // Definir limite mínimo para alertas

    public EstoqueService() {
        this.insumoDao = new JdbcInsumoDao();
    }

    // Construtor para injeção de dependência
    public EstoqueService(InsumoDao insumoDao) {
        this.insumoDao = insumoDao;
    }

    /**
     * Lista todos os insumos do estoque
     */
    public List<Insumo> listarTodosInsumos() {
        return insumoDao.listarTodos();
    }

    /**
     * Verifica insumos com estoque baixo
     */
    public List<Insumo> listarInsumosEstoqueBaixo() {
        List<Insumo> todosInsumos = insumoDao.listarTodos();
        List<Insumo> estoqueBaixo = new ArrayList<>();

        for (Insumo insumo : todosInsumos) {
            if (insumo.getQuantidadeDisponivel() <= QUANTIDADE_MINIMA) {
                estoqueBaixo.add(insumo);
            }
        }

        return estoqueBaixo;
    }

    /**
     * Verifica insumos próximos do máximo
     */
    public List<Insumo> listarInsumosProximosMaximo() {
        List<Insumo> todosInsumos = insumoDao.listarTodos();
        List<Insumo> proximosMaximo = new ArrayList<>();

        for (Insumo insumo : todosInsumos) {
            if (insumo.getQuantidadeDisponivel() >= (QUANTIDADE_MAXIMA * 0.9)) {
                proximosMaximo.add(insumo);
            }
        }

        return proximosMaximo;
    }

    /**
     * Busca insumo por identificador
     */
    public Insumo buscarInsumo(int identificador) {
        Insumo insumo = insumoDao.buscarPorId(identificador);
        if (insumo == null) {
            insumo = insumoDao.buscarPorCodigoBarras(identificador);
        }

        if (insumo == null) {
            throw new IllegalArgumentException("Insumo não encontrado com identificador: " + identificador);
        }

        return insumo;
    }

    /**
     * Verifica disponibilidade de um insumo
     */
    public boolean verificarDisponibilidade(int insumoId, int quantidadeNecessaria) {
        Insumo insumo = insumoDao.buscarPorId(insumoId);
        if (insumo == null) {
            return false;
        }

        return insumo.getQuantidadeDisponivel() >= quantidadeNecessaria;
    }

    /**
     * Calcula percentual de estoque
     */
    public double calcularPercentualEstoque(int insumoId) {
        Insumo insumo = insumoDao.buscarPorId(insumoId);
        if (insumo == null) {
            throw new IllegalArgumentException("Insumo não encontrado!");
        }

        return (double) insumo.getQuantidadeDisponivel() / QUANTIDADE_MAXIMA * 100;
    }

    /**
     * Gera relatório de estoque
     */
    public String gerarRelatorioEstoque() {
        List<Insumo> insumos = insumoDao.listarTodos();
        StringBuilder relatorio = new StringBuilder();

        relatorio.append("=== RELATÓRIO DE ESTOQUE ===\n");
        relatorio.append("Data: ").append(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n\n");

        int totalItens = 0;
        int itensEstoqueBaixo = 0;
        int itensEstoqueAlto = 0;

        for (Insumo insumo : insumos) {
            totalItens++;

            if (insumo.getQuantidadeDisponivel() <= QUANTIDADE_MINIMA) {
                itensEstoqueBaixo++;
            } else if (insumo.getQuantidadeDisponivel() >= (QUANTIDADE_MAXIMA * 0.9)) {
                itensEstoqueAlto++;
            }

            relatorio.append(String.format("%-40s | Qtd: %4d | Max: %4d | %.1f%%\n",
                    insumo.getNome(),
                    insumo.getQuantidadeDisponivel(),
                    insumo.getQuantidadeMaxima(),
                    calcularPercentualEstoque(insumo.getId())
            ));
        }

        relatorio.append("\n=== RESUMO ===\n");
        relatorio.append("Total de itens: ").append(totalItens).append("\n");
        relatorio.append("Itens com estoque baixo (<=").append(QUANTIDADE_MINIMA).append("): ")
                .append(itensEstoqueBaixo).append("\n");
        relatorio.append("Itens próximos ao máximo (>=90%): ").append(itensEstoqueAlto).append("\n");

        return relatorio.toString();
    }
}
