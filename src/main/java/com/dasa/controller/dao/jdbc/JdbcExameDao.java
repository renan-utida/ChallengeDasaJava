package com.dasa.controller.dao.jdbc;

import com.dasa.config.OracleConnectionFactory;
import com.dasa.controller.dao.ExameDao;
import com.dasa.model.domain.Exame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcExameDao implements ExameDao {

    @Override
    public Exame buscarPorId(int id) {
        String sql = "SELECT * FROM dasa_exames WHERE id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Exame(
                            rs.getInt("id"),
                            rs.getString("nome")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar exame: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Exame buscarPorNome(String nome) {
        String sql = "SELECT * FROM dasa_exames WHERE nome = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nome);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Exame(
                            rs.getInt("id"),
                            rs.getString("nome")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar exame por nome: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Exame> listarTodos() {
        String sql = "SELECT * FROM dasa_exames ORDER BY id";
        List<Exame> exames = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                exames.add(new Exame(
                        rs.getInt("id"),
                        rs.getString("nome")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar exames: " + e.getMessage(), e);
        }
        return exames;
    }
}