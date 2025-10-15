package com.dasa.model.funcionarios;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@DisplayName("Testes - classe TecnicoLaboratorio")
public class TecnicoLaboratorioTest {

    private TecnicoLaboratorio tecnico;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        tecnico = new TecnicoLaboratorio("João Silva", 12345);
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Deve criar técnico com construtor padrão")
    public void testConstrutorPadrao() {
        TecnicoLaboratorio t = new TecnicoLaboratorio();
        assertNotNull(t);
    }

    @Test
    @DisplayName("Deve criar técnico com parâmetros")
    public void testConstrutorCompleto() {
        assertEquals("João Silva", tecnico.getNome());
        assertEquals(12345, tecnico.getCrbm());
        assertEquals(12345, tecnico.getRegistro());
    }

    @Test
    @DisplayName("Deve sincronizar CRBM com registro")
    public void testSincronizacaoCrbmRegistro() {
        tecnico.setCrbm(99999);
        assertEquals(99999, tecnico.getCrbm());
        assertEquals(99999, tecnico.getRegistro());
    }

    @Test
    @DisplayName("Deve apresentar corretamente")
    public void testApresentar() {
        tecnico.apresentar();
        String output = outputStream.toString();

        assertTrue(output.contains("CRBM: 12345"));
        assertTrue(output.contains("João Silva"));
        assertTrue(output.contains("Técnico"));
    }

    @Test
    @DisplayName("Deve acessar sistema corretamente")
    public void testAcessarSistema() {
        tecnico.acessarSistema();
        String output = outputStream.toString();

        assertTrue(output.contains("Olá"));
        assertTrue(output.contains("João Silva"));
        assertTrue(output.contains("bem vindo"));
    }

    @Test
    @DisplayName("Deve manter herança de Funcionario")
    public void testHeranca() {
        tecnico.setNome("Pedro Oliveira");
        assertEquals("Pedro Oliveira", tecnico.getNome());

        tecnico.setRegistro(777777);
        assertEquals(777777, tecnico.getRegistro());
    }

    @Test
    @DisplayName("Deve testar diferentes CRBMs")
    public void testDiferentesCrbm() {
        TecnicoLaboratorio t1 = new TecnicoLaboratorio("Técnico 1", 11111);
        TecnicoLaboratorio t2 = new TecnicoLaboratorio("Técnico 2", 22222);
        TecnicoLaboratorio t3 = new TecnicoLaboratorio("Técnico 3", 33333);

        assertNotEquals(t1.getCrbm(), t2.getCrbm());
        assertNotEquals(t2.getCrbm(), t3.getCrbm());
        assertNotEquals(t1.getCrbm(), t3.getCrbm());
    }

    @Test
    @DisplayName("Deve testar mensagem personalizada de acesso")
    public void testMensagemAcesso() {
        TecnicoLaboratorio t = new TecnicoLaboratorio("Maria Santos", 67890);
        t.acessarSistema();
        String output = outputStream.toString();

        assertTrue(output.contains("Maria Santos"));
        assertTrue(output.contains("sistema"));
    }

    @Test
    @DisplayName("Deve alterar nome e refletir no acesso ao sistema")
    public void testAlterarNomeEAcessar() {
        tecnico.setNome("Carlos Eduardo");
        tecnico.acessarSistema();
        String output = outputStream.toString();

        assertTrue(output.contains("Carlos Eduardo"));
    }
}