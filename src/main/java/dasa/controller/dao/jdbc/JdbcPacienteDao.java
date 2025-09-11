package dasa.controller.dao.jdbc;

import dasa.config.OracleConnectionFactory;
import dasa.controller.dao.PacienteDao;
import dasa.model.domain.Paciente;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcPacienteDao implements PacienteDao {

    @Override
    public Long salvar(Paciente paciente) {
        String sql = "INSERT INTO dasa_pacientes (nome_completo, cpf, data_nascimento, " +
                "convenio, preferencial) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"})) {

            ps.setString(1, paciente.getNomeCompleto());
            ps.setString(2, paciente.getCpf());
            ps.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            ps.setString(4, paciente.isConvenio() ? "S" : "N");
            ps.setString(5, paciente.isPreferencial() ? "S" : "N");

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar paciente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Paciente buscarPorId(int id) {
        String sql = "SELECT * FROM dasa_pacientes WHERE id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPaciente(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar paciente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Paciente buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM dasa_pacientes WHERE cpf = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPaciente(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por CPF: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM dasa_pacientes ORDER BY nome_completo";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapearPaciente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes: " + e.getMessage(), e);
        }
        return pacientes;
    }

    @Override
    public void atualizar(Paciente paciente) {
        String sql = "UPDATE dasa_pacientes SET nome_completo = ?, cpf = ?, " +
                "data_nascimento = ?, convenio = ?, preferencial = ? WHERE id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getNomeCompleto());
            ps.setString(2, paciente.getCpf());
            ps.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            ps.setString(4, paciente.isConvenio() ? "S" : "N");
            ps.setString(5, paciente.isPreferencial() ? "S" : "N");
            ps.setInt(6, paciente.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM dasa_pacientes WHERE id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public int obterProximoId() {
        String sql = "SELECT seq_paciente_id.NEXTVAL FROM DUAL";
        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter próximo ID: " + e.getMessage(), e);
        }
        return 0;
    }

    // Metodo auxiliar
    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNomeCompleto(rs.getString("nome_completo"));
        p.setCpf(rs.getString("cpf"));
        p.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        p.setConvenio("S".equals(rs.getString("convenio")));
        p.setPreferencial("S".equals(rs.getString("preferencial")));
        return p;
    }
}
