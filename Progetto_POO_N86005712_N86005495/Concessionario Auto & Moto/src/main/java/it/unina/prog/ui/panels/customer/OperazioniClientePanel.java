package it.unina.prog.ui.panels.customer;

import it.unina.prog.DBManager;
import it.unina.prog.ui.common.UiSupport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;

public class OperazioniClientePanel extends JPanel {
    private final DefaultTableModel venditeModel;
    private final DefaultTableModel testDriveModel;

    public OperazioniClientePanel(int clienteId) {
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
            String venditeSql = "SELECT v.id, v.data, v.veicolo, v.prezzo_finale, d.nome AS dipendente " +
                    "FROM Vendita v JOIN Dipendente d ON v.dipendente = d.id " +
                    "WHERE v.cliente = ? ORDER BY v.data DESC";
            String testDriveSql = "SELECT id, data, veicolo FROM TestDrive WHERE cliente = ? ORDER BY data DESC";

            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement venditePs = conn.prepareStatement(venditeSql);
                 java.sql.PreparedStatement testPs = conn.prepareStatement(testDriveSql)) {

                venditePs.setInt(1, clienteId);
                try (ResultSet rs = venditePs.executeQuery()) {
                    while (rs.next()) {
                        venditeModel.addRow(new Object[]{
                                rs.getInt("id"),
                                rs.getString("data"),
                                rs.getString("veicolo"),
                                rs.getDouble("prezzo_finale"),
                                rs.getString("dipendente")
                        });
                    }
                }

                testPs.setInt(1, clienteId);
                try (ResultSet rs = testPs.executeQuery()) {
                    while (rs.next()) {
                        testDriveModel.addRow(new Object[]{
                                rs.getInt("id"),
                                rs.getString("data"),
                                rs.getString("veicolo")
                        });
                    }
                }
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        };

        load.run();
        refresh.addActionListener(e -> load.run());
    }
}
