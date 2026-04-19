package it.unina.prog.ui.panels.employee;

import it.unina.prog.dao.ClienteDAO;
import it.unina.prog.dao.VeicoloDAO;
import it.unina.prog.dao.VenditaDAO;
import it.unina.prog.model.Cliente;
import it.unina.prog.model.Veicolo;
import it.unina.prog.ui.common.UiSupport;
import it.unina.prog.ui.model.UiModels.ClienteItem;
import it.unina.prog.ui.model.UiModels.VeicoloItem;

import javax.swing.*;
import java.awt.*;

public class VenditaPanel extends JPanel {
    private final JComboBox<VeicoloItem> veicolo = new JComboBox<>();
    private final JComboBox<ClienteItem> cliente = new JComboBox<>();
    private final JTextField dipendente = new JTextField("1", 8);
    private final JTextField prezzo = new JTextField(10);

    public VenditaPanel(int dipendenteId) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dipendente.setText(String.valueOf(dipendenteId));
        dipendente.setEditable(false);
        dipendente.setBackground(Color.WHITE);

        JPanel form = UiSupport.wrapTitled(new JPanel(new GridBagLayout()), "Effettua Vendita");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Veicolo*"), gbc);
        gbc.gridx = 1; form.add(veicolo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Cliente*"), gbc);
        gbc.gridx = 1; form.add(cliente, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("ID Dipendente*"), gbc);
        gbc.gridx = 1; form.add(dipendente, gbc);
        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Prezzo Finale*"), gbc);
        gbc.gridx = 1; form.add(prezzo, gbc);
        prezzo.setEditable(false);
        prezzo.setBackground(Color.WHITE);

        JButton conferma = new JButton("Conferma Vendita");
        JButton refresh = new JButton("Aggiorna Liste");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(conferma);
        actions.add(refresh);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        form.add(actions, gbc);

        add(form, BorderLayout.NORTH);

        JTextArea note = new JTextArea("Flusso semplificato: seleziona veicolo disponibile, cliente e completa la vendita.");
        note.setEditable(false);
        note.setLineWrap(true);
        note.setWrapStyleWord(true);
        note.setBackground(new Color(245, 245, 245));
        note.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(note, BorderLayout.CENTER);

        Runnable load = () -> {
            try {
                veicolo.removeAllItems();
                cliente.removeAllItems();
                for (Veicolo v : VeicoloDAO.getVeicoliDisponibili()) {
                    veicolo.addItem(new VeicoloItem(v.getTarga(), v.getModello(), v.getPrezzo()));
                }
                for (Cliente c : ClienteDAO.getClienti()) {
                    cliente.addItem(new ClienteItem(c.getId(), c.getNome()));
                }
                VeicoloItem selected = (VeicoloItem) veicolo.getSelectedItem();
                prezzo.setText(selected != null ? String.valueOf(selected.prezzo) : "");
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        };
        load.run();

        veicolo.addActionListener(e -> {
            VeicoloItem selected = (VeicoloItem) veicolo.getSelectedItem();
            prezzo.setText(selected != null ? String.valueOf(selected.prezzo) : "");
        });

        conferma.addActionListener(e -> {
            try {
                VeicoloItem v = (VeicoloItem) veicolo.getSelectedItem();
                ClienteItem c = (ClienteItem) cliente.getSelectedItem();
                if (v == null || c == null) {
                    throw new IllegalArgumentException("Seleziona veicolo e cliente");
                }
                prezzo.setText(String.valueOf(v.prezzo));
                VenditaDAO.effettuaVendita(v.targa, c.id, Integer.parseInt(dipendente.getText().trim()), v.prezzo);
                JOptionPane.showMessageDialog(this, "Vendita registrata con successo");
                load.run();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        refresh.addActionListener(e -> load.run());
    }
}
