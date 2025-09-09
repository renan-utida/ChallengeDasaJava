package dasa.controller.dao;

import dasa.modelo.Insumo;
import java.util.List;

public interface InsumoDao {
    // READ
    Insumo buscarPorId(int id);
    Insumo buscarPorCodigoBarras(int codigoBarras);
    List<Insumo> listarTodos();
    List<Insumo> listarPorTipo(String tipo);
    List<Insumo> listarPorExame(String nomeExame);

    // UPDATE (apenas quantidade)
    void atualizarQuantidade(int id, int novaQuantidade);
    boolean adicionarQuantidade(int id, int quantidade);
    boolean removerQuantidade(int id, int quantidade);

    // Utility
    int calcularQuantidadeMaximaAdicao(int id);
}
