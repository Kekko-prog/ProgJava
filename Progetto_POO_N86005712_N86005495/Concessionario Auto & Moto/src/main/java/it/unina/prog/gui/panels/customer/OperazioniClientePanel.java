package it.unina.prog.gui.panels.customer;

import it.unina.prog.controller.ConcessionarioController;
import it.unina.prog.gui.common.UiSupport;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class OperazioniClientePanel extends JPanel {
    private final DefaultTableModel venditeModel;
    private final DefaultTableModel testDriveModel;
    private final ConcessionarioController controller;

    public OperazioniClientePanel(ConcessionarioController ctrl, int clienteId) {
        this.controller = ctrl;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane tabs = new JTabbedPane();
        venditeModel = new DefaultTableModel(new String[]{"ID", "Data", "Veicolo", "Prezzo Finale", "Dipendente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable vendite = new JTable(venditeModel);

        testDriveModel = new DefaultTableModel(new String[]{"ID", "Data", "Veicolo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable testDrive = new JTable(testDriveModel);

        tabs.addTab("I miei Acquisti", new JScrollPane(vendite));
        tabs.addTab("I Miei Test Drive", new JScrollPane(testDrive));

        JButton refresh = new JButton("Aggiorna");
        JPanel top = UiSupport.wrapTitled(new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6)), "Storico Operazioni Cliente");
        top.add(refresh);

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        Runnable load = () -> {
            venditeModel.setRowCount(0);
            testDriveModel.setRowCount(0);
            try {
                for (Object[] row : this.controller.getStoricoVenditeCliente(clienteId)) {
                    venditeModel.addRow(row);
                }
                for (Object[] row : this.controller.getStoricoTestDriveCliente(clienteId)) {
                    testDriveModel.addRow(row);
                }
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        };

        load.run();
        refresh.addActionListener(e -> load.run());
    }
}