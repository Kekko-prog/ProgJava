package it.unina.prog.ui.panels.employee;

import it.unina.prog.dao.ClienteDAO;
import it.unina.prog.dao.TestDriveDAO;
import it.unina.prog.dao.VeicoloDAO;
import it.unina.prog.model.Cliente;
import it.unina.prog.model.Veicolo;
import it.unina.prog.ui.common.UiSupport;
import it.unina.prog.ui.model.UiModels.ClienteItem;
import it.unina.prog.ui.model.UiModels.VeicoloItem;
import it.unina.prog.ui.validation.InputValidator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class TestDrivePanel extends JPanel {
    private final JComboBox<VeicoloItem> veicolo = new JComboBox<>();
    private final JComboBox<ClienteItem> cliente = new JComboBox<>();
    private final JTextField data = new JTextField(LocalDate.now().toString(), 12);

    public TestDrivePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = UiSupport.wrapTitled(new JPanel(new GridBagLayout()), "Richiedi Test Drive");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Cliente*"), gbc);
        gbc.gridx = 1; form.add(cliente, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Veicolo*"), gbc);
        gbc.gridx = 1; form.add(veicolo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Data (YYYY-MM-DD)*"), gbc);
        gbc.gridx = 1; form.add(data, gbc);

        JButton conferma = new JButton("Conferma Test Drive");
        JButton refresh = new JButton("Aggiorna Liste");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        actions.add(conferma);
        actions.add(refresh);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(actions, gbc);

        add(form, BorderLayout.NORTH);

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
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        };
        load.run();

        conferma.addActionListener(e -> {
            try {
                VeicoloItem v = (VeicoloItem) veicolo.getSelectedItem();
                ClienteItem c = (ClienteItem) cliente.getSelectedItem();
                if (v == null || c == null) {
                    throw new IllegalArgumentException("Seleziona cliente e veicolo");
                }

                String dataInput = data.getText().trim();
                LocalDate dataTestDrive = InputValidator.parseDateOrThrow(dataInput);
                InputValidator.requireTodayOrFuture(dataTestDrive);

                TestDriveDAO.richiediTestDrive(c.id, v.targa, dataInput);
                JOptionPane.showMessageDialog(this, "Test drive registrato con successo");
                load.run();
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        refresh.addActionListener(e -> load.run());
    }
}
