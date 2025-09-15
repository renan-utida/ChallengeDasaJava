package dasa.view.ui.swing.setores;

import dasa.service.RecepcaoService;
import dasa.model.domain.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SwingRecepcao extends JPanel {

    private JPanel mainPanel;
    private RecepcaoService service;
    private JTable tabelaAtendimentos;
    private DefaultTableModel modeloTabela;

    public SwingRecepcao(JPanel mainPanel, RecepcaoService service) {
        this.mainPanel = mainPanel;
        this.service = service;
        configurarTela();
    }

    private void configurarTela() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Cabeçalho
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(52, 152, 219));

        JLabel titulo = new JLabel("RECEPÇÃO");
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

        tabbedPane.addTab("Cadastrar Paciente", criarPainelCadastro());
        tabbedPane.addTab("Atendimentos", criarPainelAtendimentos());
        tabbedPane.addTab("Histórico por CPF", criarPainelHistorico());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel criarPainelCadastro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Seção: Novo Paciente ou Existente
        JRadioButton rbNovoPaciente = new JRadioButton("Novo Paciente", true);
        JRadioButton rbPacienteExistente = new JRadioButton("Paciente Existente");
        ButtonGroup group = new ButtonGroup();
        group.add(rbNovoPaciente);
        group.add(rbPacienteExistente);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(rbNovoPaciente, gbc);
        gbc.gridx = 1;
        panel.add(rbPacienteExistente, gbc);

        // Campos do formulário
        JTextField txtNome = new JTextField(20);
        JTextField txtCpf = new JTextField(20);
        JTextField txtDataNasc = new JTextField(20);
        JCheckBox chkConvenio = new JCheckBox("Convênio");
        JCheckBox chkPreferencial = new JCheckBox("Preferencial");
        JCheckBox chkJejum = new JCheckBox("Em Jejum (min. 8 horas)");
        JComboBox<String> cmbExame = new JComboBox<>(new String[]{
                "Hemograma Completo", "Exame de Urina", "Exame de Glicemia"
        });

        int row = 1;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1;
        panel.add(txtNome, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCpf, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Data Nascimento (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        panel.add(txtDataNasc, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(chkConvenio, gbc);
        gbc.gridx = 1;
        panel.add(chkPreferencial, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Exame:"), gbc);
        gbc.gridx = 1;
        panel.add(cmbExame, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(chkJejum, gbc);

        // Botão Cadastrar
        JButton btnCadastrar = new JButton("Cadastrar Atendimento");
        btnCadastrar.setBackground(new Color(52, 152, 219));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 14));

        row++;
        gbc.gridy = row;
        panel.add(btnCadastrar, gbc);

        // Listeners
        rbPacienteExistente.addActionListener(e -> {
            txtNome.setEnabled(false);
            txtDataNasc.setEnabled(false);
            chkConvenio.setEnabled(false);
            chkPreferencial.setEnabled(false);
        });

        rbNovoPaciente.addActionListener(e -> {
            txtNome.setEnabled(true);
            txtDataNasc.setEnabled(true);
            chkConvenio.setEnabled(true);
            chkPreferencial.setEnabled(true);
        });

        btnCadastrar.addActionListener(e -> {
            try {
                String cpf = txtCpf.getText().trim();
                String exame = (String) cmbExame.getSelectedItem();
                boolean jejum = chkJejum.isSelected();

                if (rbNovoPaciente.isSelected()) {
                    // Novo paciente
                    String nome = txtNome.getText().trim();
                    String dataNasc = txtDataNasc.getText().trim();
                    boolean convenio = chkConvenio.isSelected();
                    boolean preferencial = chkPreferencial.isSelected();

                    Long id = service.cadastrarPaciente(nome, cpf, dataNasc,
                            convenio, preferencial,
                            jejum, exame);

                    JOptionPane.showMessageDialog(this,
                            "Atendimento cadastrado!\nID: " + id,
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Paciente existente
                    Paciente paciente = service.buscarPacientePorCpf(cpf);
                    if (paciente == null) {
                        throw new IllegalArgumentException("Paciente não encontrado!");
                    }

                    Long id = service.cadastrarNovoExameParaPaciente(
                            paciente.getId(), jejum, exame);

                    JOptionPane.showMessageDialog(this,
                            "Novo atendimento cadastrado!\nID: " + id,
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }

                // Limpar campos
                txtNome.setText("");
                txtCpf.setText("");
                txtDataNasc.setText("");
                chkConvenio.setSelected(false);
                chkPreferencial.setSelected(false);
                chkJejum.setSelected(false);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel criarPainelAtendimentos() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabela
        String[] colunas = {"ID", "Paciente", "CPF", "Exame", "Status", "Data"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAtendimentos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaAtendimentos);

        // Botões
        JPanel btnPanel = new JPanel(new FlowLayout());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarAtendimentos());

        JButton btnEmEspera = new JButton("Ver Em Espera");
        btnEmEspera.addActionListener(e -> carregarAtendimentosPorStatus("Em espera"));

        JButton btnAtendidos = new JButton("Ver Atendidos");
        btnAtendidos.addActionListener(e -> carregarAtendimentosPorStatus("Atendido"));

        btnPanel.add(btnAtualizar);
        btnPanel.add(btnEmEspera);
        btnPanel.add(btnAtendidos);

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Carregar dados iniciais
        carregarAtendimentos();

        return panel;
    }

    private JPanel criarPainelHistorico() {
        JPanel panel = new JPanel(new BorderLayout());

        // Painel de busca
        JPanel buscaPanel = new JPanel(new FlowLayout());
        JTextField txtCpfBusca = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar Histórico");

        buscaPanel.add(new JLabel("CPF:"));
        buscaPanel.add(txtCpfBusca);
        buscaPanel.add(btnBuscar);

        // Área de texto para histórico
        JTextArea txtHistorico = new JTextArea();
        txtHistorico.setEditable(false);
        txtHistorico.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtHistorico);

        btnBuscar.addActionListener(e -> {
            try {
                String cpf = txtCpfBusca.getText().trim();
                List<Atendimento> historico = service.listarHistoricoExamesPorCpf(cpf);

                StringBuilder sb = new StringBuilder();
                sb.append("=== HISTÓRICO DE ATENDIMENTOS ===\n\n");

                if (historico.isEmpty()) {
                    sb.append("Nenhum atendimento encontrado para este CPF.\n");
                } else {
                    for (Atendimento a : historico) {
                        sb.append("ID: #").append(a.getId()).append("\n");
                        sb.append("Paciente: ").append(a.getPaciente().getNomeCompleto()).append("\n");
                        sb.append("Exame: ").append(a.getExame()).append("\n");
                        sb.append("Status: ").append(a.getStatus()).append("\n");
                        sb.append("Data: ").append(a.getDataExame()).append("\n");
                        sb.append("----------------------------------------\n\n");
                    }
                }

                txtHistorico.setText(sb.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(buscaPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void carregarAtendimentos() {
        try {
            modeloTabela.setRowCount(0);
            List<Atendimento> atendimentos = service.listarTodosAtendimentos();

            for (Atendimento a : atendimentos) {
                Object[] linha = {
                        a.getId(),
                        a.getPaciente().getNomeCompleto(),
                        a.getPaciente().getCpf(),
                        a.getExame(),
                        a.getStatus(),
                        a.getDataExame().format(
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                };
                modeloTabela.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar atendimentos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarAtendimentosPorStatus(String status) {
        try {
            modeloTabela.setRowCount(0);
            List<Atendimento> atendimentos = service.listarAtendimentosPorStatus(status);

            for (Atendimento a : atendimentos) {
                Object[] linha = {
                        a.getId(),
                        a.getPaciente().getNomeCompleto(),
                        a.getPaciente().getCpf(),
                        a.getExame(),
                        a.getStatus(),
                        a.getDataExame().format(
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                };
                modeloTabela.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar atendimentos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
