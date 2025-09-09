package dasa.controller.dao;

import dasa.modelo.ItemCesta;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface HistoricoDao {
    // CREATE
    void salvarRetirada(int pacienteId, LocalDateTime dataRetirada,
                        int tecnicoCrbm, int enfermeiroCoren,
                        List<ItemCesta> itens);

    // READ
    List<Map<String, Object>> listarHistoricoCompleto();
    List<Map<String, Object>> listarHistoricoPorPaciente(int pacienteId);
    List<Map<String, Object>> listarHistoricoPorData(LocalDateTime dataInicio, LocalDateTime dataFim);

    // Utility
    Map<String, Object> obterDetalhesRetirada(int pacienteId, LocalDateTime dataRetirada);
}
