package dasa.controller.dao.jdbc;

import dasa.config.OracleConnectionFactory;
import dasa.controller.dao.PacienteDao;
import dasa.modelo.Paciente;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcPacienteDao implements PacienteDao {

    @Override
    public Long salvar(Paciente paciente) {
        String sql = "INSERT INTO dasa_pacientes (nome_completo, cpf, data_nascimento, " +
                "convenio, preferencial, jejum, exame_id, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"})) {

            ps.setString(1, paciente.getNomeCompleto());
            ps.setLong(2, paciente.getCpf());
            ps.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            ps.setString(4, paciente.isConvenio() ? "S" : "N");
            ps.setString(5, paciente.isPreferencial() ? "S" : "N");
            ps.setString(6, paciente.isJejum() ? "S" : "N");
            ps.setInt(7, obterIdExamePorNome(paciente.getExame()));
            ps.setString(8, paciente.getStatus());

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
        String sql = "SELECT p.*, e.nome as exame_nome FROM dasa_pacientes p " +
                "JOIN dasa_exames e ON p.exame_id = e.id WHERE p.id = ?";

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
    public List<Paciente> listarTodos() {
        String sql = "SELECT p.*, e.nome as exame_nome, " +
                "enf.nome || ' - ' || enf.coren as enfermeiro_info, " +
                "tec.nome || ' - ' || tec.crbm as tecnico_info " +
                "FROM dasa_pacientes p " +
                "JOIN dasa_exames e ON p.exame_id = e.id " +
                "LEFT JOIN dasa_enfermeiros enf ON p.enfermeiro_coren = enf.coren " +
                "LEFT JOIN dasa_tecnicos tec ON p.tecnico_crbm = tec.crbm " +
                "ORDER BY p.id";

        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapearPacienteCompleto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes: " + e.getMessage(), e);
        }
        return pacientes;
    }

    @Override
    public List<Paciente> listarPorStatus(String status) {
        String sql = "SELECT p.*, e.nome as exame_nome, " +
                "enf.nome || ' - ' || enf.coren as enfermeiro_info, " +
                "tec.nome || ' - ' || tec.crbm as tecnico_info " +
                "FROM dasa_pacientes p " +
                "JOIN dasa_exames e ON p.exame_id = e.id " +
                "LEFT JOIN dasa_enfermeiros enf ON p.enfermeiro_coren = enf.coren " +
                "LEFT JOIN dasa_tecnicos tec ON p.tecnico_crbm = tec.crbm " +
                "WHERE p.status = ?";

        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pacientes.add(mapearPacienteCompleto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar por status: " + e.getMessage(), e);
        }
        return pacientes;
    }

    @Override
    public void atualizarStatus(int id, String status, int enfermeiroCoren, int tecnicoCrbm) {
        String sql = "UPDATE dasa_pacientes SET status = ?, enfermeiro_coren = ?, " +
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
    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        // Mapeia ResultSet para objeto Paciente
        int id = rs.getInt("id");
        String nomeCompleto = rs.getString("nome_completo");
        long cpf = rs.getLong("cpf");
        LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
        LocalDateTime dataExame = rs.getTimestamp("data_exame").toLocalDateTime();
        boolean convenio = "S".equals(rs.getString("convenio"));
        boolean preferencial = "S".equals(rs.getString("preferencial"));
        boolean jejum = "S".equals(rs.getString("jejum"));
        String exameNome = rs.getString("exame_nome");
        String status = rs.getString("status");

        // Criar o paciente usando o construtor para carregar do arquivo (adaptado)
        Paciente p = new Paciente(
                id, nomeCompleto, cpf,
                dataNascimento.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                dataExame.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                convenio, preferencial, jejum, exameNome, status,
                "Em espera", "Em espera"
        );

        return p;
    }

    private Paciente mapearPacienteCompleto(ResultSet rs) throws SQLException {
        Paciente p = mapearPaciente(rs);

        // Adiciona informações de enfermeiro e técnico se existirem
        String enfermeiroInfo = rs.getString("enfermeiro_info");
        String tecnicoInfo = rs.getString("tecnico_info");

        if (enfermeiroInfo != null) {
            p.setEnfermeiroResponsavel(enfermeiroInfo);
        }
        if (tecnicoInfo != null) {
            p.setResponsavelColeta(tecnicoInfo);
        }

        return p;
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
    public Paciente buscarPorCpf(long cpf) {
        String sql = "SELECT p.*, e.nome as exame_nome FROM dasa_pacientes p " +
                "JOIN dasa_exames e ON p.exame_id = e.id WHERE p.cpf = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, cpf);

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
    public List<Paciente> listarPorEnfermeiro(int coren) {
        String sql = "SELECT p.*, e.nome as exame_nome, " +
                "enf.nome || ' - ' || enf.coren as enfermeiro_info, " +
                "tec.nome || ' - ' || tec.crbm as tecnico_info " +
                "FROM dasa_pacientes p " +
                "JOIN dasa_exames e ON p.exame_id = e.id " +
                "LEFT JOIN dasa_enfermeiros enf ON p.enfermeiro_coren = enf.coren " +
                "LEFT JOIN dasa_tecnicos tec ON p.tecnico_crbm = tec.crbm " +
                "WHERE p.enfermeiro_coren = ? AND p.status = 'Atendido'";

        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, coren);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pacientes.add(mapearPacienteCompleto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar por enfermeiro: " + e.getMessage(), e);
        }
        return pacientes;
    }

    @Override
    public void atualizar(Paciente paciente) {
        String sql = "UPDATE dasa_pacientes SET nome_completo = ?, cpf = ?, " +
                "data_nascimento = ?, convenio = ?, preferencial = ?, " +
                "jejum = ?, exame_id = ?, status = ?, enfermeiro_coren = ?, " +
                "tecnico_crbm = ? WHERE id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getNomeCompleto());
            ps.setLong(2, paciente.getCpf());
            ps.setDate(3, Date.valueOf(paciente.getDataNascimento()));
            ps.setString(4, paciente.isConvenio() ? "S" : "N");
            ps.setString(5, paciente.isPreferencial() ? "S" : "N");
            ps.setString(6, paciente.isJejum() ? "S" : "N");
            ps.setInt(7, obterIdExamePorNome(paciente.getExame()));
            ps.setString(8, paciente.getStatus());

            // Extrair IDs do enfermeiro e técnico das strings
            if (!paciente.getEnfermeiroResponsavel().equals("Em espera")) {
                String[] partes = paciente.getEnfermeiroResponsavel().split(" - ");
                if (partes.length > 1) {
                    ps.setInt(9, Integer.parseInt(partes[partes.length - 1]));
                } else {
                    ps.setNull(9, Types.INTEGER);
                }
            } else {
                ps.setNull(9, Types.INTEGER);
            }

            if (!paciente.getResponsavelColeta().equals("Em espera")) {
                String[] partes = paciente.getResponsavelColeta().split(" - ");
                if (partes.length > 1) {
                    ps.setInt(10, Integer.parseInt(partes[partes.length - 1]));
                } else {
                    ps.setNull(10, Types.INTEGER);
                }
            } else {
                ps.setNull(10, Types.INTEGER);
            }

            ps.setInt(11, paciente.getId());

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
}
