package dasa.view.ui.swing.setores;

import dasa.service.RecepcaoService;
import dasa.model.domain.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SwingRecepcao extends JPanel {

    private JPanel mainPanel;
    private RecepcaoService service;
    private JTable tabelaAtendimentos;
    private DefaultTableModel modeloTabela;
    private JTable tabelaPacientes;
    private DefaultTableModel modeloPacientes;
    private DefaultTableModel modeloPacientesBasicos;
    private DefaultTableModel modeloPacientesAdmin;
    private static final DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
        headerPanel.setBackground(new Color(0, 90, 102));

        JLabel titulo = new JLabel("RECEPÇÃO");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);

        add(headerPanel, BorderLayout.NORTH);

        // Painel Central com Abas
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Cadastrar Paciente", criarPainelCadastro());
        tabbedPane.addTab("Atendimentos", criarPainelAtendimentos());
        tabbedPane.addTab("Pacientes", criarPainelPacientes());
        tabbedPane.addTab("Histórico por CPF", criarPainelHistorico());

        add(tabbedPane, BorderLayout.CENTER);

        // Rodapé com botão Voltar
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(new Color(0, 90, 102));
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

    private JPanel criarPainelCadastro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Seção: Novo Paciente ou Existente
        JRadioButton rbNovoPaciente = new JRadioButton("Novo Paciente", true);
        JRadioButton rbPacienteExistente = new JRadioButton("Paciente Existente");
        rbNovoPaciente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rbPacienteExistente.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        // Cursor para checkboxes
        chkConvenio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chkPreferencial.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chkJejum.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JComboBox<String> cmbExame = new JComboBox<>(new String[]{
                "Hemograma Completo", "Exame de Urina", "Exame de Glicemia"
        });
        cmbExame.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        btnCadastrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
                    String nome = txtNome.getText().trim();
                    String dataNasc = txtDataNasc.getText().trim();
                    boolean convenio = chkConvenio.isSelected();
                    boolean preferencial = chkPreferencial.isSelected();

                    Long id = service.cadastrarPaciente(nome, cpf, dataNasc,
                            convenio, preferencial, jejum, exame);

                    JOptionPane.showMessageDialog(this,
                            "Atendimento cadastrado!\nID: " + id,
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Paciente paciente = service.buscarPacientePorCpf(cpf);
                    if (paciente == null) {
                        throw new IllegalArgumentException("Paciente não encontrado!");
                    }

                    if ("Inativo".equals(paciente.getStatusPaciente())) {
                        JOptionPane.showMessageDialog(this,
                                "Paciente está inativo!\n\n" +
                                        "É necessário reativar o paciente antes de criar novos atendimentos.\n" +
                                        "Vá em: Aba Pacientes > Reativar Paciente",
                                "Paciente Inativo",
                                JOptionPane.WARNING_MESSAGE);
                        return;
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

        // Tabela com mais colunas
        String[] colunas = {"ID Atendimento", "Paciente", "CPF", "Exame", "Jejum", "Status",
                "Data", "Técnico", "Enfermeiro"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAtendimentos = new JTable(modeloTabela);
        tabelaAtendimentos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tabelaAtendimentos);

        // Botões
        JPanel btnPanel = new JPanel(new FlowLayout());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.addActionListener(e -> carregarAtendimentos());

        JButton btnEmEspera = new JButton("Ver Em Espera");
        btnEmEspera.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEmEspera.addActionListener(e -> carregarAtendimentosPorStatus("Em espera"));

        JButton btnAtendidos = new JButton("Ver Atendidos");
        btnAtendidos.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    private JPanel criarPainelPacientes() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tabela de pacientes - adicionar coluna Status
        String[] colunasCompletas = {"ID Paciente", "Nome", "CPF", "Data Nascimento", "Convênio", "Preferencial", "Status Paciente"};
        modeloPacientes = new DefaultTableModel(colunasCompletas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] colunasBasicas = {"ID Paciente", "Nome", "CPF", "Data Nascimento"};
        modeloPacientesBasicos = new DefaultTableModel(colunasBasicas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] colunasAdmin = {"ID Paciente", "Nome", "Convênio", "Preferencial", "Status Paciente"};
        modeloPacientesAdmin = new DefaultTableModel(colunasAdmin, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaPacientes = new JTable(modeloPacientes);
        JScrollPane scrollPane = new JScrollPane(tabelaPacientes);

        // Painel de botões superior
        JPanel btnPanel = new JPanel(new FlowLayout());

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtualizar.addActionListener(e -> {
            tabelaPacientes.setModel(modeloPacientes);
            carregarTodosPacientes();
        });

        JButton btnDadosBasicos = new JButton("Dados Básicos");
        btnDadosBasicos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDadosBasicos.addActionListener(e -> {
            tabelaPacientes.setModel(modeloPacientesBasicos);
            carregarPacientesDadosBasicos();
        });

        JButton btnDadosAdmin = new JButton("Dados Administrativos");
        btnDadosAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDadosAdmin.addActionListener(e -> {
            tabelaPacientes.setModel(modeloPacientesAdmin);
            carregarPacientesDadosAdmin();
        });

        JButton btnCorrigir = new JButton("Corrigir Dados");
        btnCorrigir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnDesativar = new JButton("Desativar Paciente");
        btnDesativar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDesativar.setForeground(Color.RED);

        JButton btnReativar = new JButton("Reativar Paciente");
        btnReativar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReativar.setForeground(new Color(0, 130, 0)); // Verde

        btnPanel.add(btnAtualizar);
        btnPanel.add(btnDadosBasicos);
        btnPanel.add(btnDadosAdmin);
        btnPanel.add(btnCorrigir);
        btnPanel.add(btnDesativar);
        btnPanel.add(btnReativar);

        // Painel de operações (oculto inicialmente)
        JPanel operacoesPanel = new JPanel();
        operacoesPanel.setBorder(BorderFactory.createTitledBorder(""));
        operacoesPanel.setVisible(false);

        // Listener para botão Corrigir
        btnCorrigir.addActionListener(e -> {
            operacoesPanel.removeAll();
            operacoesPanel.setBorder(BorderFactory.createTitledBorder("Corrigir Dados do Paciente"));

            JPanel correcaoPanel = new JPanel(new FlowLayout());

            JComboBox<String> cmbTipoDado = new JComboBox<>(new String[]{
                    "Nome", "Data de Nascimento", "Convênio", "Preferencial"
            });
            cmbTipoDado.setCursor(new Cursor(Cursor.HAND_CURSOR));

            correcaoPanel.add(new JLabel("Tipo de Dado:"));
            correcaoPanel.add(cmbTipoDado);

            correcaoPanel.add(new JLabel("ID ou CPF:"));
            JTextField txtId = new JTextField(10);
            correcaoPanel.add(txtId);

            correcaoPanel.add(new JLabel("Novo Valor:"));
            JTextField txtNovoValor = new JTextField(15);
            correcaoPanel.add(txtNovoValor);

            cmbTipoDado.addActionListener(ev -> {
                String selecionado = (String) cmbTipoDado.getSelectedItem();
                boolean mostrarNovoValor = !selecionado.equals("Convênio") &&
                        !selecionado.equals("Preferencial");
                txtNovoValor.setVisible(mostrarNovoValor);
                correcaoPanel.getComponent(4).setVisible(mostrarNovoValor);
            });

            JButton btnConfirmarCorrecao = new JButton("Corrigir Dados");
            btnConfirmarCorrecao.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnConfirmarCorrecao.addActionListener(ev -> {
                try {
                    String id = txtId.getText().trim();
                    if (id.isEmpty()) {
                        throw new IllegalArgumentException("Digite o ID ou CPF!");
                    }

                    Paciente paciente = null;
                    if (id.matches("\\d+") && id.length() <= 5) {
                        paciente = service.buscarPacientePorId(Integer.parseInt(id));
                    } else {
                        paciente = service.buscarPacientePorCpf(id.replaceAll("[^0-9]", ""));
                    }

                    if (paciente == null) {
                        throw new IllegalArgumentException("Paciente não encontrado!");
                    }

                    String tipoDado = (String) cmbTipoDado.getSelectedItem();

                    switch (tipoDado) {
                        case "Nome":
                            String novoNome = txtNovoValor.getText().trim();
                            if (novoNome.isEmpty()) {
                                throw new IllegalArgumentException("Digite o novo nome!");
                            }
                            paciente.setNomeCompleto(novoNome);
                            service.atualizarPaciente(paciente);
                            JOptionPane.showMessageDialog(this, "Nome atualizado com sucesso!",
                                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            break;

                        case "Data de Nascimento":
                            String novaData = txtNovoValor.getText().trim();
                            if (!novaData.matches("\\d{2}/\\d{2}/\\d{4}")) {
                                throw new IllegalArgumentException("Data deve estar no formato dd/MM/yyyy!");
                            }
                            paciente.setDataNascimento(LocalDate.parse(novaData, formatadorData));
                            service.atualizarPaciente(paciente);
                            JOptionPane.showMessageDialog(this, "Data atualizada com sucesso!",
                                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            break;

                        case "Convênio":
                            service.alternarConvenio(paciente.getId());
                            JOptionPane.showMessageDialog(this,
                                    "Convênio alterado para: " + (!paciente.isConvenio() ? "SIM" : "NÃO"),
                                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            break;

                        case "Preferencial":
                            service.alternarPreferencial(paciente.getId());
                            JOptionPane.showMessageDialog(this,
                                    "Preferencial alterado para: " + (!paciente.isPreferencial() ? "SIM" : "NÃO"),
                                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            break;
                    }

                    btnAtualizar.doClick();
                    txtId.setText("");
                    txtNovoValor.setText("");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Erro: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });

            correcaoPanel.add(btnConfirmarCorrecao);
            operacoesPanel.add(correcaoPanel);
            operacoesPanel.setVisible(true);
            operacoesPanel.revalidate();
            operacoesPanel.repaint();
        });

        // Listener para botão Desativar
        btnDesativar.addActionListener(e -> {

            // Carrega apenas pacientes ativos na tabela
            tabelaPacientes.setModel(modeloPacientes);
            carregarPacientesAtivos();

            operacoesPanel.removeAll();
            operacoesPanel.setBorder(BorderFactory.createTitledBorder("Desativar Paciente"));

            JPanel desativarPanel = new JPanel(new FlowLayout());

            desativarPanel.add(new JLabel("ID ou CPF do Paciente:"));
            JTextField txtIdDesativar = new JTextField(15);
            desativarPanel.add(txtIdDesativar);

            JButton btnConfirmarDesativar = new JButton("Desativar Paciente");
            btnConfirmarDesativar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnConfirmarDesativar.setForeground(Color.RED);

            btnConfirmarDesativar.addActionListener(ev -> {
                try {
                    String id = txtIdDesativar.getText().trim();
                    if (id.isEmpty()) {
                        throw new IllegalArgumentException("Digite o ID ou CPF!");
                    }

                    Paciente paciente = null;
                    if (id.matches("\\d+") && id.length() <= 5) {
                        paciente = service.buscarPacientePorId(Integer.parseInt(id));
                    } else {
                        paciente = service.buscarPacientePorCpf(id.replaceAll("[^0-9]", ""));
                    }

                    if (paciente == null) {
                        throw new IllegalArgumentException("Paciente não encontrado!");
                    }

                    if ("Inativo".equals(paciente.getStatusPaciente())) {
                        throw new IllegalArgumentException("Paciente já está inativo!");
                    }

                    String mensagem = String.format(
                            "Confirmar desativação do paciente?\n\n" +
                                    "ID Paciente: %d\nNome: %s\nCPF: %s\nData Nasc: %s\n" +
                                    "Convênio: %s\nPreferencial: %s\nStatus: %s\n\n" +
                                    "Atendimentos 'Em espera' serão cancelados.",
                            paciente.getId(),
                            paciente.getNomeCompleto(),
                            paciente.getCpfFormatado(),
                            paciente.getDataNascimento().format(formatadorData),
                            paciente.isConvenio() ? "Sim" : "Não",
                            paciente.isPreferencial() ? "Sim" : "Não",
                            paciente.getStatusPaciente()
                    );

                    int opcao = JOptionPane.showConfirmDialog(this, mensagem,
                            "Confirmar Desativação", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (opcao == JOptionPane.YES_OPTION) {
                        service.desativarPaciente(paciente.getId());
                        JOptionPane.showMessageDialog(this,
                                "Paciente desativado com sucesso!\nAtendimentos em espera foram cancelados.",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        txtIdDesativar.setText("");
                        btnAtualizar.doClick();
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Erro: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });

            desativarPanel.add(btnConfirmarDesativar);
            operacoesPanel.add(desativarPanel);
            operacoesPanel.setVisible(true);
            operacoesPanel.revalidate();
            operacoesPanel.repaint();
        });

        // Listener para botão Reativar
        btnReativar.addActionListener(e -> {

            // Carrega apenas pacientes inativos na tabela
            tabelaPacientes.setModel(modeloPacientes);
            carregarPacientesInativos();

            operacoesPanel.removeAll();
            operacoesPanel.setBorder(BorderFactory.createTitledBorder("Reativar Paciente"));

            JPanel reativarPanel = new JPanel(new FlowLayout());

            reativarPanel.add(new JLabel("ID ou CPF do Paciente:"));
            JTextField txtIdReativar = new JTextField(15);
            reativarPanel.add(txtIdReativar);

            JButton btnConfirmarReativar = new JButton("Reativar Paciente");
            btnConfirmarReativar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnConfirmarReativar.setForeground(new Color(0, 128, 0));

            btnConfirmarReativar.addActionListener(ev -> {
                try {
                    String id = txtIdReativar.getText().trim();
                    if (id.isEmpty()) {
                        throw new IllegalArgumentException("Digite o ID ou CPF!");
                    }

                    Paciente paciente = null;
                    if (id.matches("\\d+") && id.length() <= 5) {
                        paciente = service.buscarPacientePorId(Integer.parseInt(id));
                    } else {
                        paciente = service.buscarPacientePorCpf(id.replaceAll("[^0-9]", ""));
                    }

                    if (paciente == null) {
                        throw new IllegalArgumentException("Paciente não encontrado!");
                    }

                    if ("Ativo".equals(paciente.getStatusPaciente())) {
                        throw new IllegalArgumentException("Paciente já está ativo!");
                    }

                    String mensagem = String.format(
                            "Confirmar reativação do paciente?\n\n" +
                                    "ID: %d\nNome: %s\nCPF: %s\nData Nascimento: %s\n" +
                                    "Status Atual: %s",
                            paciente.getId(),
                            paciente.getNomeCompleto(),
                            paciente.getCpfFormatado(),
                            paciente.getDataNascimento().format(formatadorData),
                            paciente.getStatusPaciente()
                    );

                    int opcao = JOptionPane.showConfirmDialog(this, mensagem,
                            "Confirmar Reativação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (opcao == JOptionPane.YES_OPTION) {
                        service.reativarPaciente(paciente.getId());
                        JOptionPane.showMessageDialog(this,
                                "Paciente reativado com sucesso!",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        txtIdReativar.setText("");
                        btnAtualizar.doClick();
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Erro: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });

            reativarPanel.add(btnConfirmarReativar);
            operacoesPanel.add(reativarPanel);
            operacoesPanel.setVisible(true);
            operacoesPanel.revalidate();
            operacoesPanel.repaint();
        });

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(operacoesPanel, BorderLayout.SOUTH);

        carregarTodosPacientes();
        return panel;
    }

    private JPanel criarPainelHistorico() {
        JPanel panel = new JPanel(new BorderLayout());

        // Painel de busca
        JPanel buscaPanel = new JPanel(new FlowLayout());
        JTextField txtCpfBusca = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar Histórico");
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

                // Buscar atendimento completo pelo ID para ter as informações corretas
                List<Atendimento> historico = service.listarHistoricoExamesPorCpf(cpf);

                StringBuilder sb = new StringBuilder();
                sb.append("=== HISTÓRICO DE ATENDIMENTOS ===\n\n");

                if (historico.isEmpty()) {
                    sb.append("Nenhum atendimento encontrado para este CPF.\n");
                } else {
                    for (Atendimento a : historico) {
                        sb.append("ID Atendimento: #").append(a.getId()).append("\n");
                        sb.append("Status: ").append(a.getStatus()).append("\n");
                        sb.append("\tPaciente: ").append(a.getPaciente().getNomeCompleto()).append("\n");
                        sb.append("\tCPF: ").append(a.getPaciente().getCpfFormatado()).append("\n");
                        sb.append("\tData Nascimento: ").append(
                                a.getPaciente().getDataNascimento().format(formatadorData)).append("\n");
                        sb.append("\tConvênio: ").append(a.getPaciente().isConvenio() ? "Sim" : "Não").append("\n");
                        sb.append("\tPreferencial: ").append(a.getPaciente().isPreferencial() ? "Sim" : "Não").append("\n");
                        sb.append("\tExame: ").append(a.getExame()).append("\n");
                        sb.append("\tJejum (min. 8 horas): ").append(a.isJejum() ? "Sim" : "Não").append("\n");
                        sb.append("\tData do Exame: ").append(a.getDataExame().format(formatadorDataHora)).append("\n");
                        sb.append("\tEnfermeiro Responsável: ").append(a.getEnfermeiroResponsavel()).append("\n");
                        sb.append("\tTécnico Responsável Coleta de Insumos: ").append(a.getResponsavelColeta()).append("\n");
                        sb.append("------------------------------------------\n\n");
                    }
                }

                txtHistorico.setText(sb.toString());
                txtHistorico.setCaretPosition(0);

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

    // Métodos de carregamento

    private void carregarAtendimentos() {
        try {
            modeloTabela.setRowCount(0);
            List<Atendimento> atendimentos = service.listarTodosAtendimentos();

            for (Atendimento a : atendimentos) {
                String tecnico = "Em espera";
                String enfermeiro = "Em espera";

                if ("Cancelado".equals(a.getStatus())) {
                    tecnico = "Cancelado";
                    enfermeiro = "Cancelado";
                } else if ("Atendido".equals(a.getStatus())) {
                    String respColeta = a.getResponsavelColeta();
                    if (respColeta != null && !respColeta.equals("Em espera")) {
                        tecnico = respColeta.split(" - ")[0];
                    }

                    String enfResp = a.getEnfermeiroResponsavel();
                    if (enfResp != null && !enfResp.equals("Em espera")) {
                        enfermeiro = enfResp.split(" - ")[0];
                    }
                }

                Object[] linha = {
                        a.getId(),
                        a.getPaciente().getNomeCompleto(),
                        a.getPaciente().getCpf(),
                        a.getExame(),
                        a.isJejum() ? "Sim" : "Não",
                        a.getStatus(),
                        a.getDataExame().format(formatadorDataHora),
                        tecnico,
                        enfermeiro
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
                String tecnico = "Em espera";
                String enfermeiro = "Em espera";

                if ("Atendido".equals(a.getStatus())) {
                    String respColeta = a.getResponsavelColeta();
                    if (respColeta != null && !respColeta.equals("Em espera")) {
                        tecnico = respColeta.split(" - ")[0];
                    }

                    String enfResp = a.getEnfermeiroResponsavel();
                    if (enfResp != null && !enfResp.equals("Em espera")) {
                        enfermeiro = enfResp.split(" - ")[0];
                    }
                }

                Object[] linha = {
                        a.getId(),
                        a.getPaciente().getNomeCompleto(),
                        a.getPaciente().getCpf(),
                        a.getExame(),
                        a.isJejum() ? "Sim" : "Não",
                        a.getStatus(),
                        a.getDataExame().format(formatadorDataHora),
                        tecnico,
                        enfermeiro
                };
                modeloTabela.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar atendimentos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTodosPacientes() {
        try {
            modeloPacientes.setRowCount(0);
            List<Paciente> pacientes = service.listarTodosPacientes();

            for (Paciente p : pacientes) {
                Object[] linha = {
                        p.getId(),
                        p.getNomeCompleto(),
                        p.getCpf(),
                        p.getDataNascimento().format(formatadorData),
                        p.isConvenio() ? "Sim" : "Não",
                        p.isPreferencial() ? "Sim" : "Não",
                        p.getStatusPaciente()
                };
                modeloPacientes.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pacientes: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarPacientesDadosBasicos() {
        try {
            modeloPacientesBasicos.setRowCount(0);
            List<Paciente> pacientes = service.listarTodosPacientes();

            for (Paciente p : pacientes) {
                Object[] linha = {
                        p.getId(),
                        p.getNomeCompleto(),
                        p.getCpf(),
                        p.getDataNascimento().format(formatadorData)
                };
                modeloPacientesBasicos.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pacientes: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarPacientesDadosAdmin() {
        try {
            modeloPacientesAdmin.setRowCount(0);
            List<Paciente> pacientes = service.listarTodosPacientes();

            for (Paciente p : pacientes) {
                Object[] linha = {
                        p.getId(),
                        p.getNomeCompleto(),
                        p.isConvenio() ? "Sim" : "Não",
                        p.isPreferencial() ? "Sim" : "Não",
                        p.getStatusPaciente()
                };
                modeloPacientesAdmin.addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pacientes: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarPacientesAtivos() {
        try {
            modeloPacientes.setRowCount(0);
            List<Paciente> pacientes = service.listarTodosPacientes();

            for (Paciente p : pacientes) {
                if ("Ativo".equals(p.getStatusPaciente())) {
                    Object[] linha = {
                            p.getId(),
                            p.getNomeCompleto(),
                            p.getCpf(),
                            p.getDataNascimento().format(formatadorData),
                            p.isConvenio() ? "Sim" : "Não",
                            p.isPreferencial() ? "Sim" : "Não",
                            p.getStatusPaciente()
                    };
                    modeloPacientes.addRow(linha);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pacientes: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarPacientesInativos() {
        try {
            modeloPacientes.setRowCount(0);
            List<Paciente> pacientes = service.listarTodosPacientes();

            for (Paciente p : pacientes) {
                if ("Inativo".equals(p.getStatusPaciente())) {
                    Object[] linha = {
                            p.getId(),
                            p.getNomeCompleto(),
                            p.getCpf(),
                            p.getDataNascimento().format(formatadorData),
                            p.isConvenio() ? "Sim" : "Não",
                            p.isPreferencial() ? "Sim" : "Não",
                            p.getStatusPaciente()
                    };
                    modeloPacientes.addRow(linha);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar pacientes: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}