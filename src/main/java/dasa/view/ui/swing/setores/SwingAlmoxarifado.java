package dasa.view.ui.swing.setores;

import dasa.service.*;
import dasa.model.domain.*;
import dasa.model.funcionarios.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SwingAlmoxarifado extends JPanel {

    private JPanel mainPanel;
    private AlmoxarifadoService almoxarifadoService;
    private EstoqueService estoqueService;
    private TecnicoLaboratorio tecnicoLogado;
    private JTable tabelaEstoque;
    private DefaultTableModel modeloEstoque;
    private JTable tabelaHistorico;
    private DefaultTableModel modeloHistorico;

    public SwingAlmoxarifado(JPanel mainPanel, AlmoxarifadoService almoxarifadoService,
                             EstoqueService estoqueService, TecnicoLaboratorio tecnicoLogado) {
        this.mainPanel = mainPanel;
        this.almoxarifadoService = almoxarifadoService;
        this.estoqueService = estoqueService;
        this.tecnicoLogado = tecnicoLogado;
        configurarTela();
    }

    private void configurarTela() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Cabeçalho
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(46, 204, 113));

        JLabel titulo = new JLabel("ALMOXARIFADO");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);

        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.addActionListener(e ->
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "MENU"));
        headerPanel.add(btnVoltar);

        add(headerPanel, BorderLayout.NORTH);

        // Painel Central com Abas
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Retirar Insumos", criarPainelRetirada());
        tabbedPane.addTab("Estoque", criarPainelEstoque());
        tabbedPane.addTab("Histórico de Retiradas", criarPainelHistorico());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel criarPainelRetirada() {
        JPanel panel = new JPanel(new BorderLayout());

        // Painel superior - Seleção de atendimento
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Selecionar Atendimento"));

        JComboBox<ComboItem> cmbAtendimentos = new JComboBox<>();
        JButton btnCarregar = new JButton("Carregar Atendimentos Em Espera");
        JButton btnProcessar = new JButton("Processar Retirada");
        btnProcessar.setEnabled(false);

        topPanel.add(btnCarregar);
        topPanel.add(new JLabel("Atendimento:"));
        topPanel.add(cmbAtendimentos);
        topPanel.add(btnProcessar);

        // Painel central - Lista de insumos
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Insumos para Retirada"));

        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        JList<String> listaInsumos = new JList<>(modeloLista);
        JScrollPane scrollInsumos = new JScrollPane(listaInsumos);

        // Painel para adicionar insumos
        JPanel addPanel = new JPanel(new FlowLayout());
        JComboBox<ComboItem> cmbInsumos = new JComboBox<>();
        JSpinner spnQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton btnAdicionar = new JButton("Adicionar à Cesta");

        addPanel.add(new JLabel("Insumo:"));
        addPanel.add(cmbInsumos);
        addPanel.add(new JLabel("Quantidade:"));
        addPanel.add(spnQuantidade);
        addPanel.add(btnAdicionar);

        centerPanel.add(scrollInsumos, BorderLayout.CENTER);
        centerPanel.add(addPanel, BorderLayout.SOUTH);

        // Painel inferior - Seleção de enfermeiro
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Enfermeiro Responsável"));

        JComboBox<ComboItem> cmbEnfermeiros = new JComboBox<>();
        bottomPanel.add(new JLabel("Enfermeiro:"));
        bottomPanel.add(cmbEnfermeiros);

        // Lista para armazenar itens da cesta
        List<ItemCesta> cestaItens = new ArrayList<>();

        // Listeners
        btnCarregar.addActionListener(e -> {
            try {
                cmbAtendimentos.removeAllItems();
                List<Atendimento> atendimentos = almoxarifadoService.listarAtendimentosPorStatus("Em espera");

                for (Atendimento a : atendimentos) {
                    String texto = String.format("#%d - %s - %s",
                            a.getId(), a.getPaciente().getNomeCompleto(), a.getExame());
                    cmbAtendimentos.addItem(new ComboItem(a.getId(), texto));
                }

                if (atendimentos.size() > 0) {
                    btnProcessar.setEnabled(true);
                    cmbAtendimentos.setSelectedIndex(0);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao carregar atendimentos: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        cmbAtendimentos.addActionListener(e -> {
            if (cmbAtendimentos.getSelectedItem() != null) {
                ComboItem item = (ComboItem) cmbAtendimentos.getSelectedItem();
                carregarDadosAtendimento(item.id, cmbInsumos, cmbEnfermeiros);
            }
        });

        btnAdicionar.addActionListener(e -> {
            try {
                ComboItem insumoItem = (ComboItem) cmbInsumos.getSelectedItem();
                if (insumoItem == null) return;

                Insumo insumo = almoxarifadoService.buscarInsumo(insumoItem.id);
                int quantidade = (int) spnQuantidade.getValue();

                if (quantidade > insumo.getQuantidadeDisponivel()) {
                    throw new IllegalArgumentException("Quantidade indisponível!");
                }

                cestaItens.add(new ItemCesta(insumo, quantidade));
                modeloLista.addElement(quantidade + "x " + insumo.getNome());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnProcessar.addActionListener(e -> {
            try {
                if (cestaItens.isEmpty()) {
                    throw new IllegalArgumentException("Adicione insumos à cesta!");
                }

                ComboItem atendItem = (ComboItem) cmbAtendimentos.getSelectedItem();
                ComboItem enfItem = (ComboItem) cmbEnfermeiros.getSelectedItem();

                if (atendItem == null || enfItem == null) {
                    throw new IllegalArgumentException("Selecione atendimento e enfermeiro!");
                }

                almoxarifadoService.processarRetirada(
                        atendItem.id, cestaItens,
                        tecnicoLogado.getCrbm(), enfItem.id
                );

                JOptionPane.showMessageDialog(this,
                        "Retirada processada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Limpar
                cestaItens.clear();
                modeloLista.clear();
                btnCarregar.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void carregarDadosAtendimento(int atendimentoId,
                                          JComboBox<ComboItem> cmbInsumos,
                                          JComboBox<ComboItem> cmbEnfermeiros) {
        try {
            Atendimento atend = almoxarifadoService.buscarAtendimentoPorId(atendimentoId);

            // Carregar insumos do exame
            cmbInsumos.removeAllItems();
            List<Insumo> insumos = almoxarifadoService.listarInsumosPorExame(atend.getExame());
            for (Insumo i : insumos) {
                String texto = String.format("%s (Disp: %d)",
                        i.getNome(), i.getQuantidadeDisponivel());
                cmbInsumos.addItem(new ComboItem(i.getId(), texto));
            }

            // Carregar enfermeiros da especialidade
            cmbEnfermeiros.removeAllItems();
            List<Enfermeiro> enfermeiros = almoxarifadoService
                    .listarEnfermeirosPorEspecialidade(atend.getExame());
            for (Enfermeiro e : enfermeiros) {
                String texto = e.getNome() + " - " + e.getCoren();
                cmbEnfermeiros.addItem(new ComboItem(e.getCoren(), texto));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar dados: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel criarPainelEstoque() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabela de estoque
        String[] colunas = {"ID", "Nome", "Código Barras", "Disponível", "Máximo", "Status"};
        modeloEstoque = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaEstoque = new JTable(modeloEstoque);
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);

        // Painel de controle
        JPanel controlPanel = new JPanel(new FlowLayout());

        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnAdicionar = new JButton("Adicionar Estoque");
        JButton btnRelatorio = new JButton("Gerar Relatório");

        controlPanel.add(btnAtualizar);
        controlPanel.add(btnAdicionar);
        controlPanel.add(btnRelatorio);

        btnAtualizar.addActionListener(e -> carregarEstoque());

        btnAdicionar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this,
                    "Digite o ID ou código de barras do insumo:");
            if (idStr == null) return;

            String qtdStr = JOptionPane.showInputDialog(this,
                    "Digite a quantidade a adicionar:");
            if (qtdStr == null) return;

            try {
                int id = Integer.parseInt(idStr);
                int qtd = Integer.parseInt(qtdStr);

                almoxarifadoService.adicionarEstoque(id, qtd);
                JOptionPane.showMessageDialog(this,
                        "Estoque adicionado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarEstoque();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRelatorio.addActionListener(e -> {
            String relatorio = estoqueService.gerarRelatorioEstoque();
            JTextArea textArea = new JTextArea(relatorio);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scroll = new JScrollPane(textArea);
            scroll.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(this, scroll,
                    "Relatório de Estoque", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        carregarEstoque();

        return panel;
    }

    private JPanel criarPainelHistorico() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabela de histórico
        String[] colunas = {"ID Atend.", "Data", "Paciente", "Exame", "Técnico", "Enfermeiro"};
        modeloHistorico = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaHistorico = new JTable(modeloHistorico);
        JScrollPane scrollPane = new JScrollPane(tabelaHistorico);

        // Botão atualizar
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnAtualizar = new JButton("Atualizar Histórico");
        btnAtualizar.addActionListener(e -> carregarHistorico());
        btnPanel.add(btnAtualizar);

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        carregarHistorico();

        return panel;
    }

    private void carregarEstoque() {
        try {
            modeloEstoque.setRowCount(0);
            List<Insumo> insumos = estoqueService.listarTodosInsumos();

            for (Insumo i : insumos) {
                String status = "";
                double percentual = estoqueService.calcularPercentualEstoque(i.getId());

                if (i.getQuantidadeDisponivel() <= 100) {
                    status = "BAIXO";
                } else if (percentual >= 90) {
                    status = "MÁXIMO";
                } else {
                    status = "OK";
                }

                Object[] linha = {
                        i.getId(),
                        i.getNome(),
                        i.getCodigoBarras(),
                        i.getQuantidadeDisponivel(),
                        i.getQuantidadeMaxima(),
                        status
                };
                modeloEstoque.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar estoque: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarHistorico() {
        try {
            modeloHistorico.setRowCount(0);
            List<Map<String, Object>> historico = almoxarifadoService.listarHistoricoCompleto();

            for (Map<String, Object> h : historico) {
                Object[] linha = {
                        h.get("atendimento_id"),
                        h.get("data_retirada").toString(),
                        h.get("paciente_nome"),
                        h.get("exame_nome"),
                        h.get("tecnico_info"),
                        h.get("enfermeiro_info")
                };
                modeloHistorico.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar histórico: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Classe auxiliar para ComboBox
    private class ComboItem {
        int id;
        String texto;

        ComboItem(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        @Override
        public String toString() {
            return texto;
        }
    }
}