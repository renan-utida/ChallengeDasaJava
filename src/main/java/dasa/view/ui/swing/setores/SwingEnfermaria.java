package dasa.view.ui.swing.setores;

import dasa.service.EnfermariaService;
import dasa.model.funcionarios.Enfermeiro;
import dasa.model.domain.Atendimento;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SwingEnfermaria extends JPanel {

    private JPanel mainPanel;
    private EnfermariaService service;
    private JTable tabelaEnfermeiros;
    private DefaultTableModel modeloEnfermeiros;
    private JTable tabelaAtendimentos;
    private DefaultTableModel modeloAtendimentos;

    public SwingEnfermaria(JPanel mainPanel, EnfermariaService service) {
        this.mainPanel = mainPanel;
        this.service = service;
        configurarTela();
    }

    private void configurarTela() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Cabeçalho
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(41, 0, 102));

        JLabel titulo = new JLabel("ENFERMARIA");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);

        add(headerPanel, BorderLayout.NORTH);

        // Painel Central com Abas
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Enfermeiros", criarPainelEnfermeiros());
        tabbedPane.addTab("Atendimentos por Enfermeiro", criarPainelAtendimentos());
        tabbedPane.addTab("Estatísticas", criarPainelEstatisticas());

        add(tabbedPane, BorderLayout.CENTER);

        // Rodapé com botão Voltar
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(new Color(41, 0, 102));
        footerPanel.setPreferredSize(new Dimension(800, 40));

        JLabel btnVoltar = new JLabel("← Voltar");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 18));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "MENU");
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVoltar.setForeground(new Color(200, 200, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVoltar.setForeground(Color.WHITE);
            }
        });

        footerPanel.add(btnVoltar);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel criarPainelEnfermeiros() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabela de enfermeiros
        String[] colunas = {"COREN", "Nome", "Especialidade"};
        modeloEnfermeiros = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaEnfermeiros = new JTable(modeloEnfermeiros);
        tabelaEnfermeiros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tabelaEnfermeiros);

        // Painel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout());

        JComboBox<String> cmbEspecialidade = new JComboBox<>(new String[]{
                "Todos",
                "Hemograma Completo",
                "Exame de Urina",
                "Exame de Glicemia"
        });
        cmbEspecialidade.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnTodos = new JButton("Mostrar Todos");
        btnTodos.setCursor(new Cursor(Cursor.HAND_CURSOR));

        filterPanel.add(new JLabel("Especialidade:"));
        filterPanel.add(cmbEspecialidade);
        filterPanel.add(btnFiltrar);
        filterPanel.add(btnTodos);

        btnFiltrar.addActionListener(e -> {
            String especialidade = (String) cmbEspecialidade.getSelectedItem();
            if ("Todos".equals(especialidade)) {
                carregarTodosEnfermeiros();
            } else {
                carregarEnfermeirosPorEspecialidade(especialidade);
            }
        });

        btnTodos.addActionListener(e -> carregarTodosEnfermeiros());

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        carregarTodosEnfermeiros();

        return panel;
    }

    private JPanel criarPainelAtendimentos() {
        JPanel panel = new JPanel(new BorderLayout());

        // Painel superior - seleção de enfermeiro
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Selecionar Enfermeiro"));

        JComboBox<ComboItem> cmbEnfermeiros = new JComboBox<>();
        cmbEnfermeiros.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnCarregar = new JButton("Carregar Enfermeiros");
        btnCarregar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnBuscar = new JButton("Buscar Atendimentos");
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        topPanel.add(btnCarregar);
        topPanel.add(new JLabel("Enfermeiro:"));
        topPanel.add(cmbEnfermeiros);
        topPanel.add(btnBuscar);

        // Tabela de atendimentos
        String[] colunas = {"ID Atendimento", "Paciente", "Exame", "Data", "Status"};
        modeloAtendimentos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAtendimentos = new JTable(modeloAtendimentos);
        JScrollPane scrollPane = new JScrollPane(tabelaAtendimentos);

        // Label para total
        JLabel lblTotal = new JLabel("Total de atendimentos: 0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(lblTotal);

        btnCarregar.addActionListener(e -> {
            try {
                cmbEnfermeiros.removeAllItems();
                List<Enfermeiro> enfermeiros = service.listarEnfermeirosQueAtenderam();

                for (Enfermeiro enf : enfermeiros) {
                    String texto = enf.getNome() + " - " + enf.getCoren() +
                            " (" + enf.getEspecialidade() + ")";
                    cmbEnfermeiros.addItem(new ComboItem(enf.getCoren(), texto));
                }

                if (enfermeiros.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Nenhum enfermeiro realizou atendimentos ainda.",
                            "Informação", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao carregar enfermeiros: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBuscar.addActionListener(e -> {
            try {
                ComboItem item = (ComboItem) cmbEnfermeiros.getSelectedItem();
                if (item == null) {
                    throw new IllegalArgumentException("Selecione um enfermeiro!");
                }

                modeloAtendimentos.setRowCount(0);
                List<Atendimento> atendimentos = service.listarAtendimentosPorEnfermeiro(item.id);

                for (Atendimento a : atendimentos) {
                    Object[] linha = {
                            a.getId(),
                            a.getPaciente().getNomeCompleto(),
                            a.getExame(),
                            a.getDataExame().format(
                                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                            a.getStatus()
                    };
                    modeloAtendimentos.addRow(linha);
                }

                lblTotal.setText("Total de atendimentos: " + atendimentos.size());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel criarPainelEstatisticas() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea txtEstatisticas = new JTextArea();
        txtEstatisticas.setEditable(false);
        txtEstatisticas.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(txtEstatisticas);

        JButton btnGerar = new JButton("Gerar Estatísticas");
        btnGerar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGerar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btnGerar);

        btnGerar.addActionListener(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("=== ESTATÍSTICAS DE ATENDIMENTO ===\n\n");

                String[] especialidades = {
                        "Hemograma Completo",
                        "Exame de Urina",
                        "Exame de Glicemia"
                };

                for (String esp : especialidades) {
                    sb.append("Especialidade: ").append(esp).append("\n");

                    List<Enfermeiro> enfermeiros = service.listarEnfermeirosPorEspecialidade(esp);
                    sb.append("  Enfermeiros disponíveis: ").append(enfermeiros.size()).append("\n");

                    int totalAtendimentos = 0;
                    for (Enfermeiro enf : enfermeiros) {
                        try {
                            int atend = service.contarAtendimentosPorEnfermeiro(enf.getCoren());
                            totalAtendimentos += atend;
                            if (atend > 0) {
                                sb.append("    - ").append(enf.getNome())
                                        .append(": ").append(atend).append(" atendimentos\n");
                            }
                        } catch (Exception ex) {
                            // Enfermeiro sem atendimentos
                        }
                    }

                    sb.append("  Total de atendimentos: ").append(totalAtendimentos).append("\n");
                    sb.append("\n");
                }

                // Enfermeiros mais ativos
                sb.append("\n=== ENFERMEIROS COM ATENDIMENTOS ===\n");
                List<Enfermeiro> ativos = service.listarEnfermeirosQueAtenderam();
                for (Enfermeiro enf : ativos) {
                    int count = service.contarAtendimentosPorEnfermeiro(enf.getCoren());
                    sb.append(String.format("  %s (COREN: %d) - %s: %d atendimentos\n",
                            enf.getNome(), enf.getCoren(), enf.getEspecialidade(), count));
                }

                if (ativos.isEmpty()) {
                    sb.append("  Nenhum enfermeiro realizou atendimentos ainda.\n");
                }

                txtEstatisticas.setText(sb.toString());
                txtEstatisticas.setCaretPosition(0);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao gerar estatísticas: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void carregarTodosEnfermeiros() {
        try {
            modeloEnfermeiros.setRowCount(0);
            List<Enfermeiro> enfermeiros = service.listarTodosEnfermeiros();

            for (Enfermeiro e : enfermeiros) {
                Object[] linha = {
                        e.getCoren(),
                        e.getNome(),
                        e.getEspecialidade()
                };
                modeloEnfermeiros.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar enfermeiros: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarEnfermeirosPorEspecialidade(String especialidade) {
        try {
            modeloEnfermeiros.setRowCount(0);
            List<Enfermeiro> enfermeiros = service.listarEnfermeirosPorEspecialidade(especialidade);

            for (Enfermeiro e : enfermeiros) {
                Object[] linha = {
                        e.getCoren(),
                        e.getNome(),
                        e.getEspecialidade()
                };
                modeloEnfermeiros.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar enfermeiros: " + e.getMessage(),
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