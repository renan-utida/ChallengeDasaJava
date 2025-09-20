package dasa.view.ui.swing;

import dasa.service.*;
import dasa.model.funcionarios.TecnicoLaboratorio;
import javax.swing.*;
import java.awt.*;

import dasa.view.ui.swing.setores.*;

public class SwingMain {

    private static TecnicoLaboratorio tecnicoLogado;
    private static RecepcaoService recepcaoService;
    private static AlmoxarifadoService almoxarifadoService;
    private static EnfermariaService enfermariaService;
    private static EstoqueService estoqueService;

    public static void main(String[] args) {
        inicializarServices();
        configurarLookAndFeel();
        SwingUtilities.invokeLater(() -> exibirTelaLogin());
    }

    private static void inicializarServices() {
        recepcaoService = new RecepcaoService();
        almoxarifadoService = new AlmoxarifadoService();
        enfermariaService = new EnfermariaService();
        estoqueService = new EstoqueService();
    }

    private static void configurarLookAndFeel() {
        try {
            // Usar o Nimbus look and feel para melhor renderização
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void exibirTelaLogin() {
        JFrame loginFrame = new JFrame("SECAI - Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 350);
        loginFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Logo/Título
        JLabel titulo = new JLabel("LABORATÓRIO DASA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(0, 51, 102));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Sistema de Gestão Laboratorial", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 14));
        gbc.gridy = 1;
        panel.add(subtitulo, gbc);

        // Campo CRBM
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Digite o CRBM:"), gbc);

        JTextField txtCrbm = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtCrbm, gbc);

        // Técnicos disponíveis
        JLabel infoLabel = new JLabel("<html><center><b>Técnicos disponíveis:</b><br>" +
                "12345 - João Silva<br>" +
                "67890 - Maria Santos<br>" +
                "11223 - Pedro Oliveira</center></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(infoLabel, gbc);

        // Botão Login
        JButton btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(0, 51, 102));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        gbc.gridy = 4;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> {
            try {
                int crbm = Integer.parseInt(txtCrbm.getText().trim());

                if (crbm == 12345) {
                    tecnicoLogado = new TecnicoLaboratorio("João Silva", 12345);
                } else if (crbm == 67890) {
                    tecnicoLogado = new TecnicoLaboratorio("Maria Santos", 67890);
                } else if (crbm == 11223) {
                    tecnicoLogado = new TecnicoLaboratorio("Pedro Oliveira", 11223);
                } else {
                    throw new IllegalArgumentException("CRBM não encontrado!");
                }

                // Popup de boas-vindas
                JDialog welcomeDialog = new JDialog(loginFrame,
                        "Sistema de Escaneamento e Controle Automático de Insumos - SECAI", true);
                welcomeDialog.setSize(500, 150);
                welcomeDialog.setLocationRelativeTo(loginFrame);

                JPanel welcomePanel = new JPanel(new BorderLayout());
                welcomePanel.setBackground(Color.WHITE);
                welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel welcomeMsg = new JLabel("Bem-Vindo ao SECAI, " + tecnicoLogado.getNome() + "!");
                welcomeMsg.setFont(new Font("Arial", Font.PLAIN, 14));
                welcomeMsg.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JButton btnFechar = new JButton("Fechar");
                btnFechar.setBackground(new Color(0, 51, 102));
                btnFechar.setForeground(Color.WHITE);
                btnFechar.addActionListener(ev -> {
                    welcomeDialog.dispose();
                    loginFrame.dispose();
                    exibirMenuPrincipal();
                });

                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                btnPanel.setBackground(Color.WHITE);
                btnPanel.add(btnFechar);

                welcomePanel.add(welcomeMsg, BorderLayout.WEST);
                welcomePanel.add(btnPanel, BorderLayout.SOUTH);

                welcomeDialog.setContentPane(welcomePanel);
                welcomeDialog.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(loginFrame,
                        "CRBM inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.setContentPane(panel);
        loginFrame.setVisible(true);
    }

    private static void exibirMenuPrincipal() {
        JFrame frame = new JFrame("SECAI - Sistema de Gestão Laboratorial");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLocationRelativeTo(null);

        // CardLayout para trocar entre telas
        JPanel mainPanel = new JPanel(new CardLayout());

        // Menu Principal
        JPanel menuPanel = criarMenuPrincipal(mainPanel);
        mainPanel.add(menuPanel, "MENU");

        // Adicionar as telas
        mainPanel.add(new SwingRecepcao(mainPanel, recepcaoService), "RECEPCAO");
        mainPanel.add(new SwingAlmoxarifado(mainPanel, almoxarifadoService,
                    estoqueService, tecnicoLogado), "ALMOXARIFADO");
        mainPanel.add(new SwingEnfermaria(mainPanel, enfermariaService, almoxarifadoService), "ENFERMARIA");

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel criarMenuPrincipal(JPanel mainPanel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Cabeçalho
        JLabel titulo = new JLabel("SECAI - DASA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(0, 51, 102));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Sistema de Escaneamento e Controle Automático de Insumos",
                SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 16));
        gbc.gridy = 1;
        panel.add(subtitulo, gbc);

        JLabel bemVindo = new JLabel(tecnicoLogado.getNome() + " | CRBM: " + tecnicoLogado.getCrbm(),
                SwingConstants.CENTER);
        bemVindo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 2;
        panel.add(bemVindo, gbc);

        // Botões do Menu
        JButton btnRecepcao = criarBotaoMenu("RECEPÇÃO",
                new Color(0, 90, 102), "Cadastro e gestão de pacientes");
        btnRecepcao.addActionListener(e ->
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "RECEPCAO"));
        gbc.gridy = 3;
        panel.add(btnRecepcao, gbc);

        JButton btnAlmoxarifado = criarBotaoMenu("ALMOXARIFADO",
                new Color(0, 51, 102), "Controle de estoque e retiradas");
        btnAlmoxarifado.addActionListener(e ->
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "ALMOXARIFADO"));
        gbc.gridy = 4;
        panel.add(btnAlmoxarifado, gbc);

        JButton btnEnfermaria = criarBotaoMenu("ENFERMARIA",
                new Color(41, 0, 102), "Gestão de enfermeiros e atendimentos");
        btnEnfermaria.addActionListener(e ->
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "ENFERMARIA"));
        gbc.gridy = 5;
        panel.add(btnEnfermaria, gbc);

        JButton btnSair = criarBotaoMenu("SAIR",
                new Color(117, 15, 5), "Encerrar o sistema");
        btnSair.addActionListener(e -> {
            int opcao = JOptionPane.showConfirmDialog(panel,
                    "Deseja realmente sair?", "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION);
            if (opcao == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        gbc.gridy = 6;
        panel.add(btnSair, gbc);

        return panel;
    }

    private static JButton criarBotaoMenu(String texto, Color cor, String tooltip) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(300, 50));
        botao.setToolTipText(tooltip);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setOpaque(true);
        botao.setBorderPainted(true);

        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });

        return botao;
    }
}