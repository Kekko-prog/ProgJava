package it.unina.prog.ui.panels.employee;

import it.unina.prog.DBManager;
import it.unina.prog.ui.common.UiSupport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OrdiniPanel extends JPanel {
    private final DefaultTableModel venditeModel;
    private final DefaultTableModel testDriveModel;

    public OrdiniPanel() {
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
            try (Connection conn = DBManager.getConnection(); Statement st = conn.createStatement()) {
                try (ResultSet rs = st.executeQuery("SELECT v.id, v.data, v.veicolo, c.nome as cliente, d.nome as dipendente, v.prezzo_finale FROM Vendita v JOIN Cliente c ON v.cliente = c.id JOIN Dipendente d ON v.dipendente = d.id ORDER BY v.data DESC")) {
                    while (rs.next()) {
                        venditeModel.addRow(new Object[]{
                                rs.getInt("id"),
                                rs.getString("data"),
                                rs.getString("veicolo"),
                                rs.getString("cliente"),
                                rs.getString("dipendente"),
                                rs.getDouble("prezzo_finale")
                        });
                    }
                }
                try (ResultSet rs = st.executeQuery("SELECT t.id, t.data, c.nome as cliente, t.veicolo FROM TestDrive t JOIN Cliente c ON t.cliente = c.id ORDER BY t.data DESC")) {
                    while (rs.next()) {
                        testDriveModel.addRow(new Object[]{
                                rs.getInt("id"),
                                rs.getString("data"),
                                rs.getString("cliente"),
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

            String sql = "DELETE FROM Vendita WHERE id = ?";
            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idVendita);
                ps.executeUpdate();
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

            String sql = "DELETE FROM TestDrive WHERE id = ?";
            try (Connection conn = DBManager.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idTestDrive);
                ps.executeUpdate();
                load.run();
                JOptionPane.showMessageDialog(this, "Test drive eliminato con successo");
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        });
    }
}
