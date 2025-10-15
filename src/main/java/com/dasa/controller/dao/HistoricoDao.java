package com.dasa.repository;

import com.dasa.model.domain.ItemCesta;
import java.time.LocalDateTime;
import java.util.*;

public interface HistoricoRepository {

    // CREATE
    void salvarRetirada(int atendimentoId, LocalDateTime dataRetirada,
                        int tecnicoCrbm, int enfermeiroCoren,
                        List<ItemCesta> itens);

    // READ
    List<Map<String, Object>> listarHistoricoCompleto();
    List<Map<String, Object>> listarHistoricoPorAtendimento(int atendimentoId);
    List<Map<String, Object>> listarHistoricoPorData(LocalDateTime dataInicio, LocalDateTime dataFim);

    // Utility
    Map<String, Object> obterDetalhesRetirada(int atendimentoId, LocalDateTime dataRetirada);
}
