package com.dasa.repository;

import com.dasa.model.domain.Atendimento;
import java.util.List;

public interface AtendimentoRepository {
    // CREATE
    Long salvar(Atendimento atendimento);

    // READ
    Atendimento buscarPorId(int id);
    List<Atendimento> listarTodos();
    List<Atendimento> listarPorPaciente(int pacienteId);
    List<Atendimento> listarPorStatus(String status);
    List<Atendimento> listarPorEnfermeiro(int coren);

    // UPDATE
    void atualizarStatus(int id, String status, int enfermeiroCoren, int tecnicoCrbm);

    // DELETE
    // void excluir(int id);
}
