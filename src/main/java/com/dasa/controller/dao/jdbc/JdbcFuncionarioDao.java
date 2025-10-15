package com.dasa.controller.dao.jdbc;

import com.dasa.config.OracleConnectionFactory;
import com.dasa.controller.dao.FuncionarioDao;
import com.dasa.model.funcionarios.TecnicoLaboratorio;
import com.dasa.model.funcionarios.Enfermeiro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcFuncionarioDao implements FuncionarioDao {

    @Override
    public TecnicoLaboratorio buscarTecnicoPorCrbm(int crbm) {
        String sql = "SELECT * FROM dasa_tecnicos WHERE crbm = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, crbm);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TecnicoLaboratorio(
                            rs.getString("nome"),
                            rs.getInt("crbm")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar técnico: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<TecnicoLaboratorio> listarTodosTecnicos() {
        String sql = "SELECT * FROM dasa_tecnicos ORDER BY nome";
        List<TecnicoLaboratorio> tecnicos = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tecnicos.add(new TecnicoLaboratorio(
                        rs.getString("nome"),
                        rs.getInt("crbm")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar técnicos: " + e.getMessage(), e);
        }
        return tecnicos;
    }

    @Override
    public Enfermeiro buscarEnfermeiroPorCoren(int coren) {
        String sql = "SELECT * FROM dasa_enfermeiros WHERE coren = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, coren);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Enfermeiro(
                            rs.getString("nome"),
                            rs.getInt("coren"),
                            rs.getString("especialidade")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar enfermeiro: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Enfermeiro> listarTodosEnfermeiros() {
        String sql = "SELECT * FROM dasa_enfermeiros ORDER BY coren ASC";
        List<Enfermeiro> enfermeiros = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                enfermeiros.add(new Enfermeiro(
                        rs.getString("nome"),
                        rs.getInt("coren"),
                        rs.getString("especialidade")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar enfermeiros: " + e.getMessage(), e);
        }
        return enfermeiros;
    }

    @Override
    public List<Enfermeiro> listarEnfermeirosPorEspecialidade(String especialidade) {
        String sql = "SELECT * FROM dasa_enfermeiros WHERE especialidade = ? ORDER BY nome";
        List<Enfermeiro> enfermeiros = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, especialidade);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enfermeiros.add(new Enfermeiro(
                            rs.getString("nome"),
                            rs.getInt("coren"),
                            rs.getString("especialidade")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar por especialidade: " + e.getMessage(), e);
        }
        return enfermeiros;
    }
}
