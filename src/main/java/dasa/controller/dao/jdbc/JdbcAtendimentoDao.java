package dasa.controller.dao.jdbc;

import dasa.config.OracleConnectionFactory;
import dasa.controller.dao.AtendimentoDao;
import dasa.controller.dao.PacienteDao;
import dasa.model.domain.Atendimento;
import dasa.model.domain.Paciente;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcAtendimentoDao implements AtendimentoDao {

    private PacienteDao pacienteDao = new JdbcPacienteDao();

    @Override
    public Long salvar(Atendimento atendimento) {
        String sql = "INSERT INTO dasa_atendimentos (paciente_id, exame_id, jejum, status) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"})) {

            ps.setInt(1, atendimento.getPacienteId());
            ps.setInt(2, obterIdExamePorNome(atendimento.getExame()));
            ps.setString(3, atendimento.isJejum() ? "S" : "N");
            ps.setString(4, atendimento.getStatus());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar atendimento: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Atendimento buscarPorId(int id) {
        String sql = "SELECT a.*, e.nome as exame_nome, " +
                "p.nome_completo, p.cpf, p.data_nascimento, p.convenio, p.preferencial " +
                "FROM dasa_atendimentos a " +
                "JOIN dasa_exames e ON a.exame_id = e.id " +
                "JOIN dasa_pacientes p ON a.paciente_id = p.id " +
                "WHERE a.id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAtendimentoCompleto(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar atendimento: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Atendimento> listarTodos() {
        String sql = "SELECT a.*, e.nome as exame_nome, " +
                "p.nome_completo, p.cpf, p.data_nascimento, p.convenio, p.preferencial, " +
                "CASE WHEN enf.nome IS NOT NULL THEN enf.nome || ' - COREN: ' || enf.coren " +
                "ELSE NULL END as enfermeiro_info, " +
                "CASE WHEN tec.nome IS NOT NULL THEN tec.nome || ' - CRBM: ' || tec.crbm " +
                "ELSE NULL END as tecnico_info " +
                "FROM dasa_atendimentos a " +
                "JOIN dasa_exames e ON a.exame_id = e.id " +
                "JOIN dasa_pacientes p ON a.paciente_id = p.id " +
                "LEFT JOIN dasa_enfermeiros enf ON a.enfermeiro_coren = enf.coren " +
                "LEFT JOIN dasa_tecnicos tec ON a.tecnico_crbm = tec.crbm " +
                "ORDER BY a.id DESC";

        List<Atendimento> atendimentos = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                atendimentos.add(mapearAtendimentoCompletoInfo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar atendimentos: " + e.getMessage(), e);
        }
        return atendimentos;
    }

    @Override
    public List<Atendimento> listarPorPaciente(int pacienteId) {
        String sql = "SELECT a.*, e.nome as exame_nome, " +
                "p.nome_completo, p.cpf, p.data_nascimento, p.convenio, p.preferencial " +
                "FROM dasa_atendimentos a " +
                "JOIN dasa_exames e ON a.exame_id = e.id " +
                "JOIN dasa_pacientes p ON a.paciente_id = p.id " +
                "WHERE a.paciente_id = ? " +
                "ORDER BY a.data_exame DESC";

        List<Atendimento> atendimentos = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pacienteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    atendimentos.add(mapearAtendimentoCompleto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar por paciente: " + e.getMessage(), e);
        }
        return atendimentos;
    }

    @Override
    public List<Atendimento> listarPorStatus(String status) {
        String sql = "SELECT a.*, e.nome as exame_nome, " +
                "p.nome_completo, p.cpf, p.data_nascimento, p.convenio, p.preferencial, " +
                "CASE WHEN enf.nome IS NOT NULL THEN enf.nome || ' - COREN: ' || enf.coren " +
                "ELSE NULL END as enfermeiro_info, " +
                "CASE WHEN tec.nome IS NOT NULL THEN tec.nome || ' - CRBM: ' || tec.crbm " +
                "ELSE NULL END as tecnico_info " +
                "FROM dasa_atendimentos a " +
                "JOIN dasa_exames e ON a.exame_id = e.id " +
                "JOIN dasa_pacientes p ON a.paciente_id = p.id " +
                "LEFT JOIN dasa_enfermeiros enf ON a.enfermeiro_coren = enf.coren " +
                "LEFT JOIN dasa_tecnicos tec ON a.tecnico_crbm = tec.crbm " +
                "WHERE a.status = ? " +
                "ORDER BY a.data_exame";

        List<Atendimento> atendimentos = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    atendimentos.add(mapearAtendimentoCompletoInfo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar por status: " + e.getMessage(), e);
        }
        return atendimentos;
    }

    @Override
    public void atualizarStatus(int id, String status, int enfermeiroCoren, int tecnicoCrbm) {
        String sql = "UPDATE dasa_atendimentos SET status = ?, enfermeiro_coren = ?, " +
                "tecnico_crbm = ? WHERE id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, enfermeiroCoren);
            ps.setInt(3, tecnicoCrbm);
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status: " + e.getMessage(), e);
        }
    }

    // Métodos auxiliares
    private Atendimento mapearAtendimentoCompleto(ResultSet rs) throws SQLException {
        Atendimento atendimento = new Atendimento(
                rs.getInt("id"),
                rs.getInt("paciente_id"),
                rs.getString("exame_nome"),
                rs.getTimestamp("data_exame").toLocalDateTime(),
                "S".equals(rs.getString("jejum")),
                rs.getString("status"),
                "Em espera",
                "Em espera"
        );

        // Cria e associa o paciente
        Paciente paciente = new Paciente();
        paciente.setId(rs.getInt("paciente_id"));
        paciente.setNomeCompleto(rs.getString("nome_completo"));
        paciente.setCpf(rs.getString("cpf"));
        paciente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        paciente.setConvenio("S".equals(rs.getString("convenio")));
        paciente.setPreferencial("S".equals(rs.getString("preferencial")));

        atendimento.setPaciente(paciente);

        return atendimento;
    }

    private Atendimento mapearAtendimentoCompletoInfo(ResultSet rs) throws SQLException {
        Atendimento atendimento = mapearAtendimentoCompleto(rs);

        // Adiciona informações de enfermeiro e técnico
        String enfermeiroInfo = rs.getString("enfermeiro_info");
        String tecnicoInfo = rs.getString("tecnico_info");

        // Se não houver info, mantém "Em espera"
        if (enfermeiroInfo != null && !enfermeiroInfo.contains("null")) {
            atendimento.setEnfermeiroResponsavel(enfermeiroInfo);
        } else {
            atendimento.setEnfermeiroResponsavel("Em espera");
        }

        if (tecnicoInfo != null && !tecnicoInfo.contains("null")) {
            atendimento.setResponsavelColeta(tecnicoInfo);
        } else {
            atendimento.setResponsavelColeta("Em espera");
        }

        return atendimento;
    }

    private int obterIdExamePorNome(String nomeExame) throws SQLException {
        String sql = "SELECT id FROM dasa_exames WHERE nome = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomeExame);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Exame não encontrado: " + nomeExame);
    }

    @Override
    public List<Atendimento> listarPorEnfermeiro(int coren) {
        String sql = "SELECT a.*, e.nome as exame_nome, " +
                "p.nome_completo, p.cpf, p.data_nascimento, p.convenio, p.preferencial " +
                "FROM dasa_atendimentos a " +
                "JOIN dasa_exames e ON a.exame_id = e.id " +
                "JOIN dasa_pacientes p ON a.paciente_id = p.id " +
                "WHERE a.enfermeiro_coren = ? AND a.status = 'Atendido' " +
                "ORDER BY a.data_exame DESC";

        List<Atendimento> atendimentos = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, coren);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    atendimentos.add(mapearAtendimentoCompleto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar por enfermeiro: " + e.getMessage(), e);
        }
        return atendimentos;
    }


}