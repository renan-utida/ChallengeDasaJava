package dasa.controller.dao;

import dasa.modelo.Paciente;
import java.util.List;

public interface PacienteDao {
    // CREATE
    Long salvar(Paciente paciente);

    // READ
    Paciente buscarPorId(int id);
    Paciente buscarPorCpf(long cpf);
    List<Paciente> listarTodos();
    List<Paciente> listarPorStatus(String status);
    List<Paciente> listarPorEnfermeiro(int coren);

    // UPDATE
    void atualizar(Paciente paciente);
    void atualizarStatus(int id, String status, int enfermeiroCoren, int tecnicoCrbm);

    // DELETE
    void excluir(int id);

    // Utility
    int obterProximoId();
}