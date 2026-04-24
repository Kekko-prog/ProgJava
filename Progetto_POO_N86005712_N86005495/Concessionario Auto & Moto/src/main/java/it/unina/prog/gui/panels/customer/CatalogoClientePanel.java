package it.unina.prog.gui.panels.customer;

import it.unina.prog.controller.ConcessionarioController;
import it.unina.prog.model.Veicolo;
import it.unina.prog.gui.common.UiSupport;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class CatalogoClientePanel extends JPanel {
    private final DefaultTableModel model;
    private final ConcessionarioController controller;

    public CatalogoClientePanel(ConcessionarioController ctrl) {
        this.controller = ctrl;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = UiSupport.wrapTitled(new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6)), "Catalogo Veicoli");
        JButton refresh = new JButton("Aggiorna Catalogo");
        top.add(refresh);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Targa", "Marca", "Modello", "Tipo", "Prezzo", "Stato"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        Runnable load = () -> {
            try {
                model.setRowCount(0);
                for (Veicolo v : this.controller.getAllVeicoli()) {
                    model.addRow(new Object[]{v.getTarga(), v.getMarca(), v.getModello(), v.getTipo(), v.getPrezzo(), v.getStato()});
                }
            } catch (Exception ex) {
                UiSupport.showErr(this, ex);
            }
        };

        load.run();
        refresh.addActionListener(e -> load.run());
    }
}