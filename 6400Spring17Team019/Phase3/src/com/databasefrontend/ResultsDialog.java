package com.databasefrontend;

import javax.swing.*;
import java.awt.*;

class ResultsDialog extends JDialog {
    private JTable results;

    public ResultsDialog(Component parent, TableData data, String title) {
        super();
        setLocationRelativeTo(parent);
        setModalityType(ModalityType.APPLICATION_MODAL);
        results = new JTable(data.getData(), data.getColumnNames());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(title);
        layoutComponents();
    }

    private void layoutComponents() {
        setSize(new Dimension(500, 400));
        setLayout(new BorderLayout());
        add(new JScrollPane(this.results), BorderLayout.CENTER);
        setVisible(true);
    }
}