package com.dasa.controller.dao;

import com.dasa.model.domain.Paciente;
import java.util.List;

public interface PacienteDao {
    // CREATE
    Long salvar(Paciente paciente);

    // READ
    Paciente buscarPorId(int id);
    Paciente buscarPorCpf(String cpf);
    List<Paciente> listarTodos();

    // UPDATE
    void atualizar(Paciente paciente);

    // DELETE
    void excluir(int id);

    // Utility
    int obterProximoId();
}