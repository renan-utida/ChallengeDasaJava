package dasa.controller.dao.jdbc;

import dasa.config.OracleConnectionFactory;
import dasa.controller.dao.InsumoDao;
import dasa.modelo.Insumo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcInsumoDao implements InsumoDao {

    @Override
    public Insumo buscarPorId(int id) {
        String sql = "SELECT * FROM dasa_insumos WHERE id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearInsumo(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar insumo por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Insumo buscarPorCodigoBarras(int codigoBarras) {
        String sql = "SELECT * FROM dasa_insumos WHERE codigo_barras = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigoBarras);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearInsumo(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar por código de barras: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Insumo> listarTodos() {
        String sql = "SELECT * FROM dasa_insumos ORDER BY id";
        List<Insumo> insumos = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                insumos.add(mapearInsumo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar insumos: " + e.getMessage(), e);
        }
        return insumos;
    }

    @Override
    public List<Insumo> listarPorTipo(String tipo) {
        String sql = "SELECT * FROM dasa_insumos WHERE nome LIKE ? ORDER BY id";
        List<Insumo> insumos = new ArrayList<>();

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    insumos.add(mapearInsumo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar por tipo: " + e.getMessage(), e);
        }
        return insumos;
    }

    @Override
    public List<Insumo> listarPorExame(String nomeExame) {
        List<Insumo> insumosExame = new ArrayList<>();

        switch (nomeExame) {
            case "Hemograma Completo":
                insumosExame.addAll(listarPorTipo("Tubo de Coleta"));
                insumosExame.addAll(listarPorTipo("Agulha"));
                insumosExame.addAll(listarPorTipo("Seringa"));
                break;

            case "Exame de Urina":
                insumosExame.addAll(listarPorTipo("Recipiente Estéril"));
                insumosExame.addAll(listarPorTipo("Tira Reagente"));
                insumosExame.addAll(listarPorTipo("Lâmina Análise"));
                break;

            case "Exame de Glicemia":
                insumosExame.addAll(listarPorTipo("Tubo sem Anticoagulante"));
                insumosExame.addAll(listarPorTipo("Agulha"));
                insumosExame.addAll(listarPorTipo("Seringa"));
                insumosExame.addAll(listarPorTipo("Tira Reagente"));
                break;
        }

        return insumosExame;
    }

    @Override
    public void atualizarQuantidade(int id, int novaQuantidade) {
        String sql = "UPDATE dasa_insumos SET quantidade_disponivel = ? WHERE id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, novaQuantidade);
            ps.setInt(2, id);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Insumo não encontrado: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar quantidade: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean adicionarQuantidade(int id, int quantidade) {
        // Primeiro verifica se não vai ultrapassar o máximo
        Insumo insumo = buscarPorId(id);
        if (insumo == null) {
            return false;
        }

        int novaQuantidade = insumo.getQuantidadeDisponivel() + quantidade;
        if (novaQuantidade > insumo.getQuantidadeMaxima()) {
            return false;
        }

        String sql = "UPDATE dasa_insumos SET quantidade_disponivel = quantidade_disponivel + ? " +
                "WHERE id = ? AND (quantidade_disponivel + ?) <= quantidade_maxima";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantidade);
            ps.setInt(2, id);
            ps.setInt(3, quantidade);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar quantidade: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean removerQuantidade(int id, int quantidade) {
        String sql = "UPDATE dasa_insumos SET quantidade_disponivel = quantidade_disponivel - ? " +
                "WHERE id = ? AND quantidade_disponivel >= ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantidade);
            ps.setInt(2, id);
            ps.setInt(3, quantidade);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover quantidade: " + e.getMessage(), e);
        }
    }

    @Override
    public int calcularQuantidadeMaximaAdicao(int id) {
        String sql = "SELECT quantidade_maxima - quantidade_disponivel as disponivel " +
                "FROM dasa_insumos WHERE id = ?";

        try (Connection conn = OracleConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("disponivel");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao calcular quantidade máxima: " + e.getMessage(), e);
        }
        return 0;
    }

    // Metodo auxiliar para mapear ResultSet para Insumo
    private Insumo mapearInsumo(ResultSet rs) throws SQLException {
        return new Insumo(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getInt("codigo_barras"),
                rs.getInt("quantidade_disponivel"),
                rs.getInt("quantidade_maxima")
        );
    }
}
