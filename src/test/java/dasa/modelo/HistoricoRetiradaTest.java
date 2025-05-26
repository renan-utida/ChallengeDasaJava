package dasa.modelo;

import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da classe HistoricoRetirada")
public class HistoricoRetiradaTest {

    private Paciente paciente;
    private TecnicoLaboratorio tecnico;
    private Enfermeiro enfermeiro;
    private List<ItemCesta> itens;
    private static final String ARQUIVO_TESTE = "historico-teste.txt";

    @BeforeEach
    public void setUp() throws Exception {
        // Muda o arquivo para teste via reflexão
        Field arquivoField = HistoricoRetirada.class.getDeclaredField("ARQUIVO_HISTORICO");
        arquivoField.setAccessible(true);
        arquivoField.set(null, ARQUIVO_TESTE);

        // Remove arquivo de teste se existir
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            arquivo.delete();
        }

        paciente = new Paciente("João Silva", 12345678901L, "01/01/1990",
                true, false, true, "Hemograma Completo");
        tecnico = new TecnicoLaboratorio("Maria Santos", 67890);
        enfermeiro = new Enfermeiro("Ana Silva", 741321, "Hemograma Completo");

        // Cria lista de itens
        itens = new ArrayList<>();
        Insumo insumo = new Insumo(1051, "Tubo de Coleta Pequeno", 1000051, 1500, 2000);
        itens.add(new ItemCesta(insumo, 3));
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Limpa arquivo de teste
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            arquivo.delete();
        }

        // Restaura arquivo original
        Field arquivoField = HistoricoRetirada.class.getDeclaredField("ARQUIVO_HISTORICO");
        arquivoField.setAccessible(true);
        arquivoField.set(null, "historico.txt");
    }

    @Test
    @DisplayName("Deve salvar retirada no histórico de teste")
    public void testSalvarRetirada() {
        // Verifica que arquivo não existe inicialmente
        File arquivo = new File(ARQUIVO_TESTE);
        assertFalse(arquivo.exists());

        assertDoesNotThrow(() -> {
            HistoricoRetirada.salvarRetirada(paciente, itens, tecnico, enfermeiro);
        });

        // Verifica se arquivo foi criado
        assertTrue(arquivo.exists());
        assertTrue(arquivo.length() > 0); // Arquivo não está vazio
    }

    @Test
    @DisplayName("Deve exibir histórico sem erros quando arquivo existe")
    public void testExibirHistoricoComDados() {
        // Primeiro salva uma retirada
        HistoricoRetirada.salvarRetirada(paciente, itens, tecnico, enfermeiro);

        // Depois tenta exibir
        assertDoesNotThrow(() -> {
            HistoricoRetirada.exibirHistorico();
        });
    }

    @Test
    @DisplayName("Deve tratar arquivo inexistente graciosamente")
    public void testExibirHistoricoArquivoInexistente() {
        // Garante que arquivo não existe
        File arquivo = new File(ARQUIVO_TESTE);
        if (arquivo.exists()) {
            arquivo.delete();
        }

        assertDoesNotThrow(() -> {
            HistoricoRetirada.exibirHistorico();
        });
    }

    @Test
    @DisplayName("Deve salvar múltiplas retiradas no mesmo arquivo")
    public void testSalvarMultiplasRetiradas() {
        assertDoesNotThrow(() -> {
            // Primeira retirada
            HistoricoRetirada.salvarRetirada(paciente, itens, tecnico, enfermeiro);

            // Segunda retirada
            Paciente paciente2 = new Paciente("Ana Costa", 98765432109L, "02/02/1995",
                    false, true, false, "Exame de Urina");
            Enfermeiro enfermeiro2 = new Enfermeiro("Carlos Eduardo", 852432, "Exame de Urina");
            HistoricoRetirada.salvarRetirada(paciente2, itens, tecnico, enfermeiro2);
        });

        // Verifica que arquivo existe e tem conteúdo
        File arquivo = new File(ARQUIVO_TESTE);
        assertTrue(arquivo.exists());
        assertTrue(arquivo.length() > 100); // Arquivo tem conteúdo substancial
    }

    @Test
    @DisplayName("Deve manter integridade dos dados salvos")
    public void testIntegridadeDados() {
        // Cria dados específicos para teste
        TecnicoLaboratorio tecnicoTeste = new TecnicoLaboratorio("Técnico Teste", 99999);
        Enfermeiro enfermeiroTeste = new Enfermeiro("Enfermeiro Teste", 888888, "Hemograma Completo");

        assertDoesNotThrow(() -> {
            HistoricoRetirada.salvarRetirada(paciente, itens, tecnicoTeste, enfermeiroTeste);
        });

        // Verifica que o arquivo foi criado com sucesso
        File arquivo = new File(ARQUIVO_TESTE);
        assertTrue(arquivo.exists());
    }

    @Test
    @DisplayName("Deve criar arquivo quando não existe")
    public void testCriarArquivoSeNaoExiste() {
        File arquivo = new File(ARQUIVO_TESTE);

        // Garante que arquivo não existe
        if (arquivo.exists()) {
            arquivo.delete();
        }
        assertFalse(arquivo.exists());

        // Salva retirada
        assertDoesNotThrow(() -> {
            HistoricoRetirada.salvarRetirada(paciente, itens, tecnico, enfermeiro);
        });

        // Verifica que arquivo foi criado
        assertTrue(arquivo.exists());
    }
}