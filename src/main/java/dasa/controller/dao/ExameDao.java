package dasa.controller.dao;

import dasa.model.domain.Exame;
import java.util.List;

public interface ExameDao {
    Exame buscarPorId(int id);
    Exame buscarPorNome(String nome);
    List<Exame> listarTodos();
}
