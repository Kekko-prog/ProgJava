package it.unina.prog.ui;

import it.unina.prog.dao.ClienteDAO;
import it.unina.prog.dao.DipendenteDAO;
import it.unina.prog.model.Cliente;
import it.unina.prog.model.Dipendente;
import it.unina.prog.ui.common.UiSupport;
import it.unina.prog.ui.panels.customer.CatalogoClientePanel;
import it.unina.prog.ui.panels.customer.OperazioniClientePanel;
import it.unina.prog.ui.panels.customer.ProfiloClientePanel;
import it.unina.prog.ui.panels.customer.TestDriveClientePanel;
import it.unina.prog.ui.panels.employee.ClientiPanel;
import it.unina.prog.ui.panels.employee.OrdiniPanel;
import it.unina.prog.ui.panels.employee.TestDrivePanel;
import it.unina.prog.ui.panels.employee.VeicoliPanel;
import it.unina.prog.ui.panels.employee.VenditaPanel;

import javax.swing.*;
import java.awt.*;

public class ConcessionarioFrame extends JFrame {

    private static final String CARD_LOGIN = "LOGIN";
    private static final String CARD_DIPENDENTE = "DIPENDENTE";
    private static final String CARD_CLIENTE = "CLIENTE";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final JLabel sessionLabel = new JLabel(" ");
    private final JButton logoutButton = new JButton("Logout");

    // Viste create dinamicamente in base all'utente autenticato.
    private JPanel dipendenteView;
    private JPanel clienteView;

    public ConcessionarioFrame() {
        setTitle("Sistema Concessionario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(createHeader(), BorderLayout.NORTH);

        cardPanel.add(createLoginPanel(), CARD_LOGIN);
        root.add(cardPanel, BorderLayout.CENTER);
        setContentPane(root);

        switchToLogin();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(70, 130, 180));
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel title = new JLabel("Concessionario Auto & Moto");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        sessionLabel.setForeground(new Color(230, 240, 255));
        sessionLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        logoutButton.setFocusable(false);
        logoutButton.addActionListener(e -> switchToLogin());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(sessionLabel);
        right.add(logoutButton);

        header.add(title, BorderLayout.CENTER);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel createLoginPanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(236, 243, 252));

        JPanel form = UiSupport.wrapTitled(new JPanel(new GridBagLayout()), "Accesso");
        form.setPreferredSize(new Dimension(540, 280));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> ruolo = new JComboBox<>(new String[]{"cliente", "dipendente"});
        JTextField credenziale1 = new JTextField(22);
        JPasswordField credenziale2 = new JPasswordField(22);
        JLabel label1 = new JLabel("Email cliente*");
        JLabel label2 = new JLabel("Password*");
        JTextArea note = new JTextArea("Cliente: inserisci email e password.\nDipendente: inserisci ID e password.\nLa finestra si aggiorna senza aprirne una nuova.");
        note.setEditable(false);
        note.setLineWrap(true);
        note.setWrapStyleWord(true);
        note.setBackground(new Color(245, 245, 245));
        note.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Cambia il testo del primo campo in base al ruolo scelto.
        ruolo.addActionListener(e -> {
            String r = String.valueOf(ruolo.getSelectedItem());
            if ("dipendente".equalsIgnoreCase(r)) {
                label1.setText("ID dipendente*");
                credenziale1.setText("");
                credenziale2.setText("");
            } else {
                label1.setText("Email cliente*");
                credenziale1.setText("");
                credenziale2.setText("");
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Ruolo*"), gbc);
        gbc.gridx = 1; form.add(ruolo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(label1, gbc);
        gbc.gridx = 1; form.add(credenziale1, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(label2, gbc);
        gbc.gridx = 1; form.add(credenziale2, gbc);

        JButton loginBtn = new JButton("Accedi");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(loginBtn);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(actions, gbc);

        gbc.gridy = 4;
        form.add(note, gbc);

        // Smista il login sul flusso corretto: cliente o dipendente.
        loginBtn.addActionListener(e -> {
            String r = String.valueOf(ruolo.getSelectedItem());
            try {
                if ("cliente".equalsIgnoreCase(r)) {
                    handleClienteLogin(credenziale1.getText().trim(), new String(credenziale2.getPassword()));
                } else {
                    handleDipendenteLogin(credenziale1.getText().trim(), new String(credenziale2.getPassword()));
                }
            } catch (Exception ex) {
                UiSupport.showErr(container, ex);
            }
        });

        container.add(form);
        return container;
    }

    private JPanel buildDipendenteArea(int dipendenteId) {
        // Area completa per il personale interno.
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Clienti", new ClientiPanel());
        tabs.addTab("Veicoli", new VeicoliPanel());
        tabs.addTab("Vendita", new VenditaPanel(dipendenteId));
        tabs.addTab("Test Drive", new TestDrivePanel());
        tabs.addTab("Ordini", new OrdiniPanel());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(tabs, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildClienteArea(Cliente cliente) {
        // Area limitata alle operazioni disponibili per il cliente.
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Profilo", new ProfiloClientePanel(cliente.getId()));
        tabs.addTab("Catalogo", new CatalogoClientePanel());
        tabs.addTab("Prenota Test Drive", new TestDriveClientePanel(cliente.getId()));
        tabs.addTab("Le Mie Operazioni", new OperazioniClientePanel(cliente.getId()));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(tabs, BorderLayout.CENTER);
        return wrapper;
    }

    private void switchToLogin() {
        // Ripristina lo stato iniziale dell'interfaccia.
        sessionLabel.setText(" ");
        logoutButton.setVisible(false);
        cardLayout.show(cardPanel, CARD_LOGIN);
    }

    private void switchToDipendente(Dipendente dipendente) {
        if (dipendenteView != null) {
            // Rimuove la vecchia vista per evitare duplicati nella CardLayout.
            cardPanel.remove(dipendenteView);
        }
        dipendenteView = buildDipendenteArea(dipendente.getId());
        cardPanel.add(dipendenteView, CARD_DIPENDENTE);
        sessionLabel.setText("Dipendente: " + dipendente.getNome() + " - " + dipendente.getRuolo() + " (ID " + dipendente.getId() + ")");
        logoutButton.setVisible(true);
        cardLayout.show(cardPanel, CARD_DIPENDENTE);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void switchToCliente(Cliente cliente) {
        if (clienteView != null) {
            // Rimuove la vecchia vista per evitare duplicati nella CardLayout.
            cardPanel.remove(clienteView);
        }
        clienteView = buildClienteArea(cliente);
        cardPanel.add(clienteView, CARD_CLIENTE);
        sessionLabel.setText("Cliente: " + cliente.getNome() + " (ID " + cliente.getId() + ")");
        logoutButton.setVisible(true);
        cardLayout.show(cardPanel, CARD_CLIENTE);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void handleClienteLogin(String email, String password) throws Exception {
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Inserisci l'email del cliente");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Inserisci la password del cliente");
        }
        Cliente cliente = ClienteDAO.autenticaCliente(email, password);
        if (cliente == null) {
            throw new IllegalArgumentException("Email o password cliente non validi");
        }
        switchToCliente(cliente);
    }

    private void handleDipendenteLogin(String idText, String password) throws Exception {
        if (idText.isEmpty()) {
            throw new IllegalArgumentException("Inserisci l'ID del dipendente");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Inserisci la password del dipendente");
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("ID dipendente non valido");
        }

        Dipendente dip = DipendenteDAO.autenticaDipendente(id, password);
        if (dip == null) {
            throw new IllegalArgumentException("ID o password dipendente non validi");
        }
        switchToDipendente(dip);
    }

}
