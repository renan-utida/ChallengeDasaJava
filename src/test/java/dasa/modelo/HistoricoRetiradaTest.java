package dasa.modelo;

import dasa.funcionarios.TecnicoLaboratorio;
import dasa.funcionarios.Enfermeiro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoricoRetiradaTest {
    private TecnicoLaboratorio tecnico;
    private Enfermeiro enfermeiro;
    private Paciente paciente;
    private List<ItemCesta> itens;

    @BeforeEach
    void setUp() {
        tecnico = new TecnicoLaboratorio("João Técnico", 12345);
        enfermeiro = new Enfermeiro("Ana Enfermeira", 741321, "Hemograma Completo");
        paciente = new Paciente("Maria Paciente", 12345678901L, "15/03/1990",
                true, false, true, "Hemograma Completo");

        // Cria itens da cesta
        itens = new ArrayList<>();
        Insumo insumo1 = new Insumo(1051, "Tubo de Coleta Pequeno", 1000051, 1500, 2000);
        Insumo insumo2 = new Insumo(2071, "Agulha 3mm", 2000071, 1200, 2000);
        itens.add(new ItemCesta(insumo1, 2));
        itens.add(new ItemCesta(insumo2, 1));
    }

    @AfterEach
    void tearDown() {
        // Limpa arquivo de teste após cada teste
        File arquivo = new File("historico.txt");
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }

    @Test
    void testSalvarRetirada() {
        // Teste que não lança exceção ao salvar
        assertDoesNotThrow(() -> {
            HistoricoRetirada.salvarRetirada(paciente, itens, tecnico, enfermeiro);
        });

        // Verifica se arquivo foi criado
        File arquivo = new File("historico.txt");
        assertTrue(arquivo.exists());
    }

    @Test
    void testExibirHistorico() {
        // Salva uma retirada primeiro
        HistoricoRetirada.salvarRetirada(paciente, itens, tecnico, enfermeiro);

        // Teste que não lança exceção ao exibir
        assertDoesNotThrow(() -> {
            HistoricoRetirada.exibirHistorico();
        });
    }

    @Test
    void testExibirHistoricoVazio() {
        // Teste com histórico vazio (arquivo não existe)
        assertDoesNotThrow(() -> {
            HistoricoRetirada.exibirHistorico();
        });
    }
}