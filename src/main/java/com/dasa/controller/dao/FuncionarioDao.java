package com.dasa.repository;

import com.dasa.model.funcionarios.TecnicoLaboratorio;
import com.dasa.model.funcionarios.Enfermeiro;
import java.util.List;

public interface FuncionarioRepository {
    // Técnicos
    TecnicoLaboratorio buscarTecnicoPorCrbm(int crbm);
    List<TecnicoLaboratorio> listarTodosTecnicos();

    // Enfermeiros
    Enfermeiro buscarEnfermeiroPorCoren(int coren);
    List<Enfermeiro> listarTodosEnfermeiros();
    List<Enfermeiro> listarEnfermeirosPorEspecialidade(String especialidade);

    // Não precisamos de insert/update/delete pois são pré-cadastrados
}
