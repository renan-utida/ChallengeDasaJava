package dasa.controller.dao;

import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import java.util.List;

public interface FuncionarioDao {
    // Técnicos
    TecnicoLaboratorio buscarTecnicoPorCrbm(int crbm);
    List<TecnicoLaboratorio> listarTodosTecnicos();

    // Enfermeiros
    Enfermeiro buscarEnfermeiroPorCoren(int coren);
    List<Enfermeiro> listarTodosEnfermeiros();
    List<Enfermeiro> listarEnfermeirosPorEspecialidade(String especialidade);

    // Não precisamos de insert/update/delete pois são pré-cadastrados
}
