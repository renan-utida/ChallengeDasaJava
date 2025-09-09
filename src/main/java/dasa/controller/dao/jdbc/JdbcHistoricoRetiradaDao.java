package dasa.controller.dao.jdbc;

import dasa.config.OracleConnectionFactory;
import dasa.controller.dao.HistoricoDao;
import dasa.modelo.ItemCesta;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcHistoricoRetiradaDao implements HistoricoDao {

    @Override
    public void salvarRetirada(int pacienteId, LocalDateTime dataRetirada,
                               int tecnicoCrbm, int enfermeiroCoren,
                               List<ItemCesta> itens) {

        Connection conn = null;
        PreparedStatement psHistorico = null;
        PreparedStatement psItens = null;

        try {
            conn = OracleConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Inicia transação

            // 1. Inserir na tabela de histórico
            String sqlHistorico = "INSERT INTO dasa_historico_retiradas " +
                    "(paciente_id, data_retirada, tecnico_crbm, enfermeiro_coren) " +
                    "VALUES (?, ?, ?, ?)";

            psHistorico = conn.prepareStatement(sqlHistorico);
            psHistorico.setInt(1, pacienteId);
            psHistorico.setTimestamp(2, Timestamp.valueOf(dataRetirada));
            psHistorico.setInt(3, tecnicoCrbm);
            psHistorico.setInt(4, enfermeiroCoren);
            psHistorico.executeUpdate();

            // 2. Inserir os itens da retirada
            String sqlItens = "INSERT INTO dasa_itens_retirada " +
                    "(paciente_id, data_retirada, insumo_id, quantidade) " +
                    "VALUES (?, ?, ?, ?)";

            psItens = conn.prepareStatement(sqlItens);

            for (ItemCesta item : itens) {
                psItens.setInt(1, pacienteId);
                psItens.setTimestamp(2, Timestamp.valueOf(dataRetirada));
                psItens.setInt(3, item.getInsumo().getId());
                psItens.setInt(4, item.getQuantidade());
                psItens.addBatch();
            }

            psItens.executeBatch();

            conn.commit(); // Confirma a transação

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz a transação em caso de erro
                } catch (SQLException ex) {
                    throw new RuntimeException("Erro ao desfazer transação: " + ex.getMessage(), ex);
                }
            }
            throw new RuntimeException("Erro ao salvar retirada: " + e.getMessage(), e);
        } finally {
            // Fecha os recursos
            try {
                if (psItens != null) psItens.close();
                if (psHistorico != null) psHistorico.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar recursos: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<Map<String, Object>> listarHistoricoCompleto() {
        String sql = "SELECT h.paciente_id, h.data_retirada, " +
                "p.nome_completo as paciente_nome, " +
                "e.nome as exame_nome, " +
                "t.nome || ' - ' || t.crbm as tecnico_info, " +
                "enf.nome || ' - ' || enf.coren as enfermeiro_info " +
                "FROM dasa_historico_retiradas h " +
                "JOIN dasa_pacientes p ON h.paciente_id = p.id " +
                "JOIN dasa_exames e ON p.exame_id = e.id " +
                "JOIN dasa_tecnicos t ON h.tecnico_crbm = t.crbm " +
                "JOIN dasa_enfermeiros enf ON h.enfermeiro_coren = enf.coren " +
                "ORDER BY h.data_retirada DESC";

        List<Map<String, Object>> historico = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> registro = new HashMap<>();
                registro.put("paciente_id", rs.getInt("paciente_id"));
                registro.put("data_retirada", rs.getTimestamp("data_retirada").toLocalDateTime());
                registro.put("paciente_nome", rs.getString("paciente_nome"));
                registro.put("exame_nome", rs.getString("exame_nome"));
                registro.put("tecnico_info", rs.getString("tecnico_info"));
                registro.put("enfermeiro_info", rs.getString("enfermeiro_info"));

                // Buscar os itens desta retirada
                registro.put("itens", buscarItensRetirada(
                        rs.getInt("paciente_id"),
                        rs.getTimestamp("data_retirada").toLocalDateTime()
                ));

                historico.add(registro);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar histórico: " + e.getMessage(), e);
        }
        return historico;
    }

    @Override
    public List<Map<String, Object>> listarHistoricoPorPaciente(int pacienteId) {
        String sql = "SELECT h.paciente_id, h.data_retirada, " +
                "p.nome_completo as paciente_nome, " +
                "e.nome as exame_nome, " +
                "t.nome || ' - ' || t.crbm as tecnico_info, " +
                "enf.nome || ' - ' || enf.coren as enfermeiro_info " +
                "FROM dasa_historico_retiradas h " +
                "JOIN dasa_pacientes p ON h.paciente_id = p.id " +
                "JOIN dasa_exames e ON p.exame_id = e.id " +
                "JOIN dasa_tecnicos t ON h.tecnico_crbm = t.crbm " +
                "JOIN dasa_enfermeiros enf ON h.enfermeiro_coren = enf.coren " +
                "WHERE h.paciente_id = ? " +
                "ORDER BY h.data_retirada DESC";

        List<Map<String, Object>> historico = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacienteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> registro = new HashMap<>();
                    registro.put("paciente_id", rs.getInt("paciente_id"));
                    registro.put("data_retirada", rs.getTimestamp("data_retirada").toLocalDateTime());
                    registro.put("paciente_nome", rs.getString("paciente_nome"));
                    registro.put("exame_nome", rs.getString("exame_nome"));
                    registro.put("tecnico_info", rs.getString("tecnico_info"));
                    registro.put("enfermeiro_info", rs.getString("enfermeiro_info"));

                    // Buscar os itens desta retirada
                    registro.put("itens", buscarItensRetirada(
                            rs.getInt("paciente_id"),
                            rs.getTimestamp("data_retirada").toLocalDateTime()
                    ));

                    historico.add(registro);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar por paciente: " + e.getMessage(), e);
        }
        return historico;
    }

    @Override
    public List<Map<String, Object>> listarHistoricoPorData(LocalDateTime dataInicio, LocalDateTime dataFim) {
        String sql = "SELECT h.paciente_id, h.data_retirada, " +
                "p.nome_completo as paciente_nome, " +
                "e.nome as exame_nome, " +
                "t.nome || ' - ' || t.crbm as tecnico_info, " +
                "enf.nome || ' - ' || enf.coren as enfermeiro_info " +
                "FROM dasa_historico_retiradas h " +
                "JOIN dasa_pacientes p ON h.paciente_id = p.id " +
                "JOIN dasa_exames e ON p.exame_id = e.id " +
                "JOIN dasa_tecnicos t ON h.tecnico_crbm = t.crbm " +
                "JOIN dasa_enfermeiros enf ON h.enfermeiro_coren = enf.coren " +
                "WHERE h.data_retirada BETWEEN ? AND ? " +
                "ORDER BY h.data_retirada DESC";

        List<Map<String, Object>> historico = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(dataInicio));
            ps.setTimestamp(2, Timestamp.valueOf(dataFim));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> registro = new HashMap<>();
                    registro.put("paciente_id", rs.getInt("paciente_id"));
                    registro.put("data_retirada", rs.getTimestamp("data_retirada").toLocalDateTime());
                    registro.put("paciente_nome", rs.getString("paciente_nome"));
                    registro.put("exame_nome", rs.getString("exame_nome"));
                    registro.put("tecnico_info", rs.getString("tecnico_info"));
                    registro.put("enfermeiro_info", rs.getString("enfermeiro_info"));

                    // Buscar os itens desta retirada
                    registro.put("itens", buscarItensRetirada(
                            rs.getInt("paciente_id"),
                            rs.getTimestamp("data_retirada").toLocalDateTime()
                    ));

                    historico.add(registro);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar por data: " + e.getMessage(), e);
        }
        return historico;
    }

    @Override
    public Map<String, Object> obterDetalhesRetirada(int pacienteId, LocalDateTime dataRetirada) {
        String sql = "SELECT h.paciente_id, h.data_retirada, " +
                "p.nome_completo as paciente_nome, " +
                "e.nome as exame_nome, " +
                "t.nome || ' - ' || t.crbm as tecnico_info, " +
                "enf.nome || ' - ' || enf.coren as enfermeiro_info " +
                "FROM dasa_historico_retiradas h " +
                "JOIN dasa_pacientes p ON h.paciente_id = p.id " +
                "JOIN dasa_exames e ON p.exame_id = e.id " +
                "JOIN dasa_tecnicos t ON h.tecnico_crbm = t.crbm " +
                "JOIN dasa_enfermeiros enf ON h.enfermeiro_coren = enf.coren " +
                "WHERE h.paciente_id = ? AND h.data_retirada = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacienteId);
            ps.setTimestamp(2, Timestamp.valueOf(dataRetirada));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> detalhes = new HashMap<>();
                    detalhes.put("paciente_id", rs.getInt("paciente_id"));
                    detalhes.put("data_retirada", rs.getTimestamp("data_retirada").toLocalDateTime());
                    detalhes.put("paciente_nome", rs.getString("paciente_nome"));
                    detalhes.put("exame_nome", rs.getString("exame_nome"));
                    detalhes.put("tecnico_info", rs.getString("tecnico_info"));
                    detalhes.put("enfermeiro_info", rs.getString("enfermeiro_info"));

                    // Buscar os itens desta retirada
                    detalhes.put("itens", buscarItensRetirada(pacienteId, dataRetirada));

                    return detalhes;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter detalhes: " + e.getMessage(), e);
        }
        return null;
    }

    // Método auxiliar para buscar itens de uma retirada específica
    private List<Map<String, Object>> buscarItensRetirada(int pacienteId, LocalDateTime dataRetirada) {
        String sql = "SELECT ir.insumo_id, ir.quantidade, i.nome as insumo_nome " +
                "FROM dasa_itens_retirada ir " +
                "JOIN dasa_insumos i ON ir.insumo_id = i.id " +
                "WHERE ir.paciente_id = ? AND ir.data_retirada = ? " +
                "ORDER BY i.nome";

        List<Map<String, Object>> itens = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacienteId);
            ps.setTimestamp(2, Timestamp.valueOf(dataRetirada));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("insumo_id", rs.getInt("insumo_id"));
                    item.put("quantidade", rs.getInt("quantidade"));
                    item.put("insumo_nome", rs.getString("insumo_nome"));
                    itens.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens: " + e.getMessage(), e);
        }
        return itens;
    }
}