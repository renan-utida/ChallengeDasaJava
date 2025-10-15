package com.dasa.repository;

import com.dasa.model.domain.Exame;
import java.util.List;

public interface ExameRepository {
    Exame buscarPorId(int id);
    Exame buscarPorNome(String nome);
    List<Exame> listarTodos();
}
