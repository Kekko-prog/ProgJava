package it.unina.prog.gui.panels.customer;

import it.unina.prog.controller.ConcessionarioController;
import it.unina.prog.model.Veicolo;
import it.unina.prog.gui.common.UiSupport;
import it.unina.prog.gui.model.UiModels.VeicoloItem;
import it.unina.prog.gui.validation.InputValidator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class TestDriveClientePanel extends JPanel {
    private final JComboBox<VeicoloItem> veicolo = new JComboBox<>();
    private final JTextField data = new JTextField(LocalDate.now().toString(), 12);
    private final ConcessionarioController controller;

    public TestDriveClientePanel(ConcessionarioController ctrl, int clienteId) {
        this.controller = ctrl;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = UiSupport.wrapTitled(new JPanel(new GridBagLayout()), "Prenotazione Test Drive");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Veicolo*"), gbc);
        gbc.gridx = 1; form.add(veicolo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Data (YYYY-MM-DD)*"), gbc);
        gbc.gridx = 1; form.add(data, gbc);

        JButton conferma = new JButton("Conferma Test Drive");
        JButton refresh = new JButton("Aggiorna Veicoli");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(conferma);
        actions.add(refresh);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        form.add(actions, gbc);

        add(form, BorderLayout.NORTH);

        Runnable load = () -> {
            try {
                veicolo.removeAllItems();
                for (Veicolo v : this.controller.getVeicoliDisponibili()) {
                    veicolo.addItem(new VeicoloItem(v.getTarga(), v.getModello(), v.getPrezzo()));
                }
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        };

        load.run();

        conferma.addActionListener(e -> {
            try {
                VeicoloItem v = (VeicoloItem) veicolo.getSelectedItem();
                if (v == null) {
                    throw new IllegalArgumentException("Seleziona un veicolo");
                }
                String dataInput = data.getText().trim();
                LocalDate dataTestDrive = InputValidator.parseDateOrThrow(dataInput);
                InputValidator.requireTodayOrFuture(dataTestDrive);

                this.controller.richiediTestDrive(clienteId, v.targa, dataInput);
                JOptionPane.showMessageDialog(this, "Test drive registrato con successo");
                load.run();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        refresh.addActionListener(e -> load.run());
    }
}