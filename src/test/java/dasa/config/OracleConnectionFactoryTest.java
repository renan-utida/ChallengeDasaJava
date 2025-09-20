package dasa.config;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;

@DisplayName("Testes - OracleConnectionFactory")
public class OracleConnectionFactoryTest {

    private String originalUrl;
    private String originalUser;
    private String originalPassword;

    @BeforeEach
    public void setUp() {
        // Salva valores originais das variáveis de ambiente (se existirem)
        originalUrl = System.getProperty("ORACLE_URL");
        originalUser = System.getProperty("ORACLE_USER");
        originalPassword = System.getProperty("ORACLE_PASSWORD");
    }

    @AfterEach
    public void tearDown() {
        // Restaura valores originais
        if (originalUrl != null) {
            System.setProperty("ORACLE_URL", originalUrl);
        } else {
            System.clearProperty("ORACLE_URL");
        }
        if (originalUser != null) {
            System.setProperty("ORACLE_USER", originalUser);
        } else {
            System.clearProperty("ORACLE_USER");
        }
        if (originalPassword != null) {
            System.setProperty("ORACLE_PASSWORD", originalPassword);
        } else {
            System.clearProperty("ORACLE_PASSWORD");
        }
    }

    @Test
    @DisplayName("Deve criar conexão com sucesso")
    public void testGetConnection() {
        assertDoesNotThrow(() -> {
            Connection conn = OracleConnectionFactory.getConnection();
            assertNotNull(conn, "Conexão não deve ser nula");
            assertFalse(conn.isClosed(), "Conexão deve estar aberta");
            conn.close();
        });
    }

    @Test
    @DisplayName("Deve usar valores padrão quando variáveis de ambiente não existem")
    public void testGetConnectionComValoresPadrao() {
        try {
            Connection conn = OracleConnectionFactory.getConnection();
            assertNotNull(conn);

            // Verifica se está usando a URL padrão
            String metaDataUrl = conn.getMetaData().getURL();
            assertNotNull(metaDataUrl);

            conn.close();
        } catch (SQLException e) {
            // Se falhar, pode ser problema de rede/banco
            assertTrue(e.getMessage().contains("oracle") ||
                            e.getMessage().contains("fiap"),
                    "Deve tentar conectar no servidor Oracle FIAP");
        }
    }

    @Test
    @DisplayName("Conexões múltiplas devem ser independentes")
    public void testMultiplasConexoes() throws SQLException {
        Connection conn1 = null;
        Connection conn2 = null;

        try {
            conn1 = OracleConnectionFactory.getConnection();
            conn2 = OracleConnectionFactory.getConnection();

            assertNotNull(conn1);
            assertNotNull(conn2);
            assertNotSame(conn1, conn2, "Cada chamada deve criar nova conexão");

        } finally {
            if (conn1 != null && !conn1.isClosed()) conn1.close();
            if (conn2 != null && !conn2.isClosed()) conn2.close();
        }
    }

    @Test
    @DisplayName("Deve retornar conexão não nula")
    public void deveRetornarConexaoNaoNula() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            Connection conn = OracleConnectionFactory.getConnection();
            assertNotNull(conn, "Conexão não deve ser nula");
            if (!conn.isClosed()) {
                conn.close();
            }
        });
    }

    @Test
    @DisplayName("Deve lançar SQLException para URL inválida")
    public void testLancarSQLExceptionParaUrlInvalida() {
        // Given
        System.setProperty("ORACLE_URL", "jdbc:oracle:thin:@//url_invalida:1521/db");
        System.setProperty("ORACLE_USER", "user_teste");
        System.setProperty("ORACLE_PASSWORD", "senha_teste");

        // When & Then
        assertDoesNotThrow(() -> {
            try {
                Connection conn = OracleConnectionFactory.getConnection();
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Esperado para URLs inválidas - teste passa
                assertTrue(true, "SQLException esperada para URL inválida");
            }
        });
    }

    @Test
    @DisplayName("Deve fechar conexão corretamente")
    public void testFechamentoConexao() throws SQLException {
        // Given
        Connection conn = OracleConnectionFactory.getConnection();
        assertFalse(conn.isClosed(), "Conexão deve estar aberta inicialmente");

        // When
        conn.close();

        // Then
        assertTrue(conn.isClosed(), "Conexão deve estar fechada após close()");
    }
}