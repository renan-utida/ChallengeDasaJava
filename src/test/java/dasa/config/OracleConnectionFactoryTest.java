package dasa.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import static org.junit.jupiter.api.Assertions.*;

public class OracleConnectionFactoryTest {

    @Test
    @DisplayName("Deve conectar com sucesso ao banco Oracle")
    public void testarConexaoOracle() throws Exception {
        // Act
        Connection conn = OracleConnectionFactory.getConnection();

        // Assert
        assertNotNull(conn, "Conexão não deve ser nula");
        assertFalse(conn.isClosed(), "Conexão deve estar aberta");

        // Informações adicionais
        DatabaseMetaData metaData = conn.getMetaData();
        System.out.println("✅ Conectado: " + metaData.getDatabaseProductName());
        System.out.println("Usuário: " + metaData.getUserName());

        // Cleanup
        conn.close();
    }

    @Test
    @DisplayName("Deve fechar a conexão corretamente")
    public void testarFechamentoConexao() throws Exception {
        // Arrange
        Connection conn = OracleConnectionFactory.getConnection();

        // Act
        conn.close();

        // Assert
        assertTrue(conn.isClosed(), "Conexão deve estar fechada");
    }
}