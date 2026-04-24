package it.unina.prog.gui.panels.employee;

import it.unina.prog.controller.ConcessionarioController;
import it.unina.prog.gui.common.UiSupport;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class OrdiniPanel extends JPanel {
    private final DefaultTableModel venditeModel;
    private final DefaultTableModel testDriveModel;
    private final ConcessionarioController controller;

    public OrdiniPanel(ConcessionarioController ctrl) {
        this.controller = ctrl;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane tabs = new JTabbedPane();
        venditeModel = new DefaultTableModel(new String[]{"ID", "Data", "Veicolo", "Cliente", "Dipendente", "Prezzo Finale"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable vendite = new JTable(venditeModel);

        testDriveModel = new DefaultTableModel(new String[]{"ID", "Data", "Cliente", "Veicolo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable testDrive = new JTable(testDriveModel);

        tabs.addTab("Vendite", new JScrollPane(vendite));
        tabs.addTab("Test Drive", new JScrollPane(testDrive));

        JButton refresh = new JButton("Aggiorna");
        JButton eliminaVendita = new JButton("Elimina Vendita Selezionata");
        JButton eliminaTestDrive = new JButton("Elimina Test Drive Selezionato");
        JPanel top = UiSupport.wrapTitled(new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6)), "Storico Ordini");
        top.add(refresh);
        top.add(eliminaVendita);
        top.add(eliminaTestDrive);

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        Runnable load = () -> {
            venditeModel.setRowCount(0);
            testDriveModel.setRowCount(0);
            try {
                for (Object[] row : this.controller.getOrdiniVendite()) {
                    venditeModel.addRow(row);
                }
                for (Object[] row : this.controller.getOrdiniTestDrive()) {
                    testDriveModel.addRow(row);
                }
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        };

        load.run();
        refresh.addActionListener(e -> load.run());

        eliminaVendita.addActionListener(e -> {
            int row = vendite.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Seleziona una vendita da eliminare");
                return;
            }

            int modelRow = vendite.convertRowIndexToModel(row);
            int idVendita = (int) venditeModel.getValueAt(modelRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Eliminare la vendita selezionata?",
                    "Conferma eliminazione",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            try {
                this.controller.eliminaVendita(idVendita);
                load.run();
                JOptionPane.showMessageDialog(this, "Vendita eliminata con successo");
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });

        eliminaTestDrive.addActionListener(e -> {
            int row = testDrive.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Seleziona un test drive da eliminare");
                return;
            }

            int modelRow = testDrive.convertRowIndexToModel(row);
            int idTestDrive = (int) testDriveModel.getValueAt(modelRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Eliminare il test drive selezionato?",
                    "Conferma eliminazione",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            try {
                this.controller.eliminaTestDrive(idTestDrive);
                load.run();
                JOptionPane.showMessageDialog(this, "Test drive eliminato con successo");
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });
    }
}