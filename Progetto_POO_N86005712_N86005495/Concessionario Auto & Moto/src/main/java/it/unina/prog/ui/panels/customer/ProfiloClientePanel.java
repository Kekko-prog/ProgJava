package it.unina.prog.ui.panels.customer;

import it.unina.prog.DBManager;
import it.unina.prog.ui.common.UiSupport;
import it.unina.prog.ui.validation.InputValidator;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;

public class ProfiloClientePanel extends JPanel {
    private final JTextField nome = new JTextField(18);
    private final JTextField tipo = new JTextField(12);
    private final JTextField email = new JTextField(18);
    private final JTextField telefono = new JTextField(18);

    public ProfiloClientePanel(int clienteId) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = UiSupport.wrapTitled(new JPanel(new GridBagLayout()), "Il Mio Profilo");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nome.setEditable(false);
        tipo.setEditable(false);
        nome.setBackground(Color.WHITE);
        tipo.setBackground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nome"), gbc);
        gbc.gridx = 1; form.add(nome, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Tipo"), gbc);
        gbc.gridx = 1; form.add(tipo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Email*"), gbc);
        gbc.gridx = 1; form.add(email, gbc);
        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Telefono*"), gbc);
        gbc.gridx = 1; form.add(telefono, gbc);

        JButton salva = new JButton("Salva Modifiche");
        JButton refresh = new JButton("Ricarica");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(salva);
        actions.add(refresh);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(actions, gbc);

        add(form, BorderLayout.NORTH);

        Runnable load = () -> {
            String sql = "SELECT nome, tipo, email, telefono FROM Cliente WHERE id = ?";
            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, clienteId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException("Cliente non trovato");
                    }
                    nome.setText(rs.getString("nome"));
                    tipo.setText(rs.getString("tipo"));
                    email.setText(rs.getString("email"));
                    telefono.setText(rs.getString("telefono"));
                }
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        };

        load.run();

        salva.addActionListener(e -> {
            try {
                validateEmailTelefono();
                String sql = "UPDATE Cliente SET email = ?, telefono = ? WHERE id = ?";
                try (Connection conn = DBManager.getConnection();
                     java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, email.getText().trim());
                    ps.setString(2, telefono.getText().trim());
                    ps.setInt(3, clienteId);
                    ps.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Profilo aggiornato con successo");
                load.run();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        refresh.addActionListener(e -> load.run());
    }

    private void validateEmailTelefono() {
        String emailVal = email.getText().trim();
        String telefonoVal = telefono.getText().trim();

        if (!InputValidator.isValidEmail(emailVal)) {
            throw new IllegalArgumentException("Email non valida: usa un formato del tipo nome@dominio.it");
        }
        if (!InputValidator.isValidPhone(telefonoVal)) {
            throw new IllegalArgumentException("Il numero di telefono deve essere valido");
        }
    }
}
