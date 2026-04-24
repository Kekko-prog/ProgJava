package it.unina.prog.gui.panels.employee;

import it.unina.prog.controller.ConcessionarioController;
import it.unina.prog.model.Veicolo;
import it.unina.prog.gui.common.UiSupport;
import it.unina.prog.gui.validation.InputValidator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VeicoliPanel extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;

    private final JTextField targa = new JTextField(14);
    private final JTextField marca = new JTextField(14);
    private final JTextField modello = new JTextField(14);
    private final JComboBox<String> tipo = new JComboBox<>(new String[]{"auto", "moto"});
    private final JTextField prezzo = new JTextField(10);
    private final ConcessionarioController controller;

    public VeicoliPanel(ConcessionarioController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = UiSupport.wrapTitled(new JPanel(new GridBagLayout()), "Gestione Veicoli");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Targa*"), gbc);
        gbc.gridx = 1; form.add(targa, gbc);
        gbc.gridx = 2; form.add(new JLabel("Marca* (nuova o esistente)"), gbc);
        gbc.gridx = 3; form.add(marca, gbc);

        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Modello*"), gbc);
        gbc.gridx = 1; form.add(modello, gbc);
        gbc.gridx = 2; form.add(new JLabel("Tipo*"), gbc);
        gbc.gridx = 3; form.add(tipo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Prezzo*"), gbc);
        gbc.gridx = 1; form.add(prezzo, gbc);

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

        model = new DefaultTableModel(new String[]{"Targa", "Marca", "Modello", "Tipo", "Prezzo", "Stato"}, 0) {
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
                List<Veicolo> veicoli = controller.getAllVeicoli();
                for (Veicolo v : veicoli) {
                    model.addRow(new Object[]{v.getTarga(), v.getMarca(), v.getModello(), v.getTipo(), v.getPrezzo(), v.getStato()});
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
            targa.setText(String.valueOf(model.getValueAt(mr, 0)));
            marca.setText(String.valueOf(model.getValueAt(mr, 1)));
            modello.setText(String.valueOf(model.getValueAt(mr, 2)));
            tipo.setSelectedItem(String.valueOf(model.getValueAt(mr, 3)));
            prezzo.setText(String.valueOf(model.getValueAt(mr, 4)));
            targa.setEnabled(false);
        });

        salva.addActionListener(e -> {
            try {
                double prezzoVal = validateVeicoloInput();
                String targaCanonica = InputValidator.normalizePlate(targa.getText());
                if (targaExistsIgnoringCaseAndSpaces(targaCanonica)) {
                    throw new IllegalArgumentException("Targa gia presente: usa una targa diversa");
                }
                ensureMarcaExistsOrCreate();
                controller.inserisciVeicolo(targaCanonica, marca.getText().trim(), modello.getText().trim(), String.valueOf(tipo.getSelectedItem()), prezzoVal);
                load.run();
                clear();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        modifica.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Seleziona un veicolo");
                return;
            }
            try {
                double prezzoVal = validateVeicoloInput();
                String targaCanonica = InputValidator.normalizePlate(targa.getText());
                ensureMarcaExistsOrCreate();
                controller.aggiornaVeicolo(targaCanonica, marca.getText().trim(), modello.getText().trim(), String.valueOf(tipo.getSelectedItem()), prezzoVal);
                load.run();
                clear();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        elimina.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Seleziona un veicolo");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Eliminare veicolo selezionato?", "Conferma", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                controller.eliminaVeicolo(targa.getText().trim());
                load.run();
                clear();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        pulisci.addActionListener(e -> clear());
        refresh.addActionListener(e -> load.run());
    }

    private double validateVeicoloInput() {
        String targaVal = targa.getText().trim();
        String marcaVal = marca.getText().trim();
        String modelloVal = modello.getText().trim();
        String tipoVal = String.valueOf(tipo.getSelectedItem());
        String prezzoVal = prezzo.getText().trim();

        if (!InputValidator.isValidPlate(targaVal)) {
            throw new IllegalArgumentException("La targa deve avere 5-10 caratteri alfanumerici senza spazi");
        }
        if (marcaVal.isEmpty()) {
            throw new IllegalArgumentException("La marca e obbligatoria");
        }
        if (modelloVal.isEmpty()) {
            throw new IllegalArgumentException("Il modello e obbligatorio");
        }
        if (tipoVal == null || tipoVal.isBlank()) {
            throw new IllegalArgumentException("Il tipo veicolo e obbligatorio");
        }
        if (prezzoVal.isEmpty()) {
            throw new IllegalArgumentException("Il prezzo e obbligatorio");
        }

        double prezzoNum;
        try {
            prezzoNum = Double.parseDouble(prezzoVal);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Il prezzo deve essere un numero valido");
        }
        if (prezzoNum <= 0) {
            throw new IllegalArgumentException("Il prezzo deve essere maggiore di 0");
        }

        targa.setText(InputValidator.normalizePlate(targaVal));
        return prezzoNum;
    }

    private boolean targaExistsIgnoringCaseAndSpaces(String targaInput) {
        try {
            return controller.targaExistsIgnoringCaseAndSpaces(targaInput);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Impossibile verificare la targa inserita: " + UiSupport.toUserMessage(ex));
        }
    }

    private void ensureMarcaExistsOrCreate() {
        String nomeMarca = marca.getText().trim();
        if (nomeMarca.isEmpty()) {
            throw new IllegalArgumentException("La marca e obbligatoria");
        }

        if (marcaExists(nomeMarca)) {
            return;
        }

        String nazione = JOptionPane.showInputDialog(this,
                "Marca non presente. Inserisci la nazione per creare la nuova marca:",
                "Nuova marca",
                JOptionPane.QUESTION_MESSAGE);

        if (nazione == null) {
            throw new IllegalArgumentException("Inserimento nuova marca annullato");
        }

        nazione = nazione.trim();
        if (nazione.isEmpty()) {
            throw new IllegalArgumentException("La nazione della nuova marca e obbligatoria");
        }

        try {
            controller.creaMarca(nomeMarca, nazione);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Impossibile creare la nuova marca: " + UiSupport.toUserMessage(ex));
        }
    }

    private boolean marcaExists(String nomeMarca) {
        try {
            return controller.marcaExists(nomeMarca);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Impossibile verificare la marca inserita: " + UiSupport.toUserMessage(ex));
        }
    }

    private void clear() {
        targa.setText("");
        marca.setText("");
        modello.setText("");
        tipo.setSelectedIndex(0);
        prezzo.setText("");
        targa.setEnabled(true);
        table.clearSelection();
    }
}