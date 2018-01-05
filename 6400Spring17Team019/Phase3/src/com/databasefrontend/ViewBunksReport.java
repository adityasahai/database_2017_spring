package com.databasefrontend;

import javax.swing.*;
import java.awt.*;

/**
 * Created by adityasahai on 17/03/17.
 */
public class ViewBunksReport extends JDialog {
    private JPanel mainPanel;
    private JTable table;
    private boolean isTableNull;

    public ViewBunksReport(JFrame frame) {
        super(frame, "Available Rooms and Bunks Report", true);

        // Table
        table = getTable();
        if (table == null) {
            setTableNull(true);
            setVisible(false);
        }
        else {
            // Panel
            mainPanel = new JPanel();
            this.setResizable(false);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setLayout(new BorderLayout());
            table.setEnabled(false);

            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

            setSize(new Dimension(1200, 175));
            add(mainPanel, BorderLayout.CENTER);
        }
    }

    public boolean isTableNull() {
        return isTableNull;
    }

    public void setTableNull(boolean tableNull) {
        isTableNull = tableNull;
    }

    private JTable getTable() {
        DBCalls dbcalls = new DBCalls(sqlCall_enum.viewBunks);
        TableData ab = dbcalls.executeReadCall();
        if (ab != null) {
            return new JTable(ab.getData(), ab.getColumnNames());
        }
        else {
            return null;
        }
    }

}
