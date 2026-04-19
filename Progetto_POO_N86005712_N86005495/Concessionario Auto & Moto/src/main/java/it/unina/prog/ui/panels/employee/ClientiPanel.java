package it.unina.prog.ui.panels.employee;

import it.unina.prog.dao.ClienteDAO;
import it.unina.prog.model.Cliente;
import it.unina.prog.ui.common.UiSupport;
import it.unina.prog.ui.validation.InputValidator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientiPanel extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;

    private final JTextField nome = new JTextField(18);
    private final JComboBox<String> tipo = new JComboBox<>(new String[]{"privato", "azienda"});
    private final JTextField email = new JTextField(18);
    private final JTextField telefono = new JTextField(18);
    private final JPasswordField password = new JPasswordField(18);

    public ClientiPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = UiSupport.wrapTitled(new JPanel(new GridBagLayout()), "Gestione Clienti");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nome*"), gbc);
        gbc.gridx = 1; form.add(nome, gbc);
        gbc.gridx = 2; form.add(new JLabel("Tipo*"), gbc);
        gbc.gridx = 3; form.add(tipo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Email*"), gbc);
        gbc.gridx = 1; form.add(email, gbc);
        gbc.gridx = 2; form.add(new JLabel("Telefono*"), gbc);
        gbc.gridx = 3; form.add(telefono, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Password*"), gbc);
        gbc.gridx = 1; form.add(password, gbc);
        gbc.gridx = 2; form.add(new JLabel("(necessaria in creazione)"), gbc);

        JButton salva = new JButton("Salva");
        JButton modifica = new JButton("Modifica");
        JButton elimina = new JButton("Elimina");
        JButton pulisci = new JButton("Pulisci");
        JButton refresh = new JButton("Aggiorna");

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(salva);
        actions.add(modifica);
        actions.add(elimina);
        actions.add(pulisci);
        actions.add(refresh);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        form.add(actions, gbc);

        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Nome", "Tipo", "Email", "Telefono"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        Runnable load = () -> {
            try {
                model.setRowCount(0);
                List<Cliente> clienti = ClienteDAO.getClienti();
                for (Cliente c : clienti) {
                    model.addRow(new Object[]{c.getId(), c.getNome(), c.getTipo(), c.getEmail(), c.getTelefono()});
                }
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        };

        load.run();

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = table.getSelectedRow();
            if (row < 0) return;
            int mr = table.convertRowIndexToModel(row);
            nome.setText(String.valueOf(model.getValueAt(mr, 1)));
            tipo.setSelectedItem(String.valueOf(model.getValueAt(mr, 2)));
            email.setText(String.valueOf(model.getValueAt(mr, 3)));
            telefono.setText(String.valueOf(model.getValueAt(mr, 4)));
            password.setText("");
        });

        salva.addActionListener(e -> {
            try {
                validateClienteInput(null, true);
                ClienteDAO.inserisciCliente(
                        nome.getText().trim(),
                        String.valueOf(tipo.getSelectedItem()),
                        email.getText().trim(),
                        telefono.getText().trim(),
                        new String(password.getPassword()).trim()
                );
                load.run();
                clear();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        modifica.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Seleziona un cliente");
                return;
            }
            int mr = table.convertRowIndexToModel(row);
            int id = (int) model.getValueAt(mr, 0);
            try {
                validateClienteInput(id, false);
                String nuovaPassword = new String(password.getPassword()).trim();
                if (nuovaPassword.isEmpty()) {
                    ClienteDAO.aggiornaCliente(id, nome.getText().trim(), String.valueOf(tipo.getSelectedItem()), email.getText().trim(), telefono.getText().trim());
                } else {
                    ClienteDAO.aggiornaClienteConPassword(id, nome.getText().trim(), String.valueOf(tipo.getSelectedItem()), email.getText().trim(), telefono.getText().trim(), nuovaPassword);
                }
                load.run();
                clear();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        elimina.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Seleziona un cliente");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Eliminare cliente selezionato?", "Conferma", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            int mr = table.convertRowIndexToModel(row);
            int id = (int) model.getValueAt(mr, 0);
            try {
                ClienteDAO.eliminaCliente(id);
                load.run();
                clear();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        pulisci.addActionListener(e -> clear());
        refresh.addActionListener(e -> load.run());
    }

    private void validateClienteInput(Integer currentId, boolean requirePassword) throws Exception {
        String nomeVal = nome.getText().trim();
        String tipoVal = String.valueOf(tipo.getSelectedItem());
        String emailVal = email.getText().trim();
        String telefonoVal = telefono.getText().trim();
        String passwordVal = new String(password.getPassword()).trim();

        if (nomeVal.isEmpty()) {
            throw new IllegalArgumentException("Il nome non puo essere vuoto");
        }
        if (tipoVal == null || tipoVal.isBlank()) {
            throw new IllegalArgumentException("Il tipo cliente e obbligatorio");
        }
        if (!InputValidator.isValidEmail(emailVal)) {
            throw new IllegalArgumentException("Email non valida: usa un formato del tipo nome@dominio.it");
        }
        if (!InputValidator.isValidPhone(telefonoVal)) {
            throw new IllegalArgumentException("Il numero di telefono deve essere valido");
        }
        if (requirePassword && passwordVal.isEmpty()) {
            throw new IllegalArgumentException("La password e obbligatoria per creare il cliente");
        }

        List<Cliente> clienti = ClienteDAO.getClienti();
        for (Cliente c : clienti) {
            String existingEmail = c.getEmail();
            if (existingEmail != null && existingEmail.equalsIgnoreCase(emailVal)) {
                if (currentId == null || c.getId() != currentId) {
                    throw new IllegalArgumentException("Email gia utilizzata da un altro cliente");
                }
            }
        }
    }

    private void clear() {
        nome.setText("");
        tipo.setSelectedIndex(0);
        email.setText("");
        telefono.setText("");
        password.setText("");
        table.clearSelection();
    }
}
