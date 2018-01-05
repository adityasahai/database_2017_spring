package com.databasefrontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 25/03/17.
 */
public class WaitlistPanel extends JPanel {
    private UserClass user;
    JScrollPane scp = null;
    JTable waitlistTable;
    public WaitlistPanel(UserClass user) {
        this.user = user;
        reload();
    }

    public void populateTable() {
        removeAll();
        setSize(new Dimension(800, 50));
        setLayout(new BorderLayout());
        DBCalls dbCalls = new DBCalls(sqlCall_enum.viewWaitlist, new String[] {this.user.getSiteId()});
        TableData data = dbCalls.executeReadCall();
        if (data == null) {
            JTextArea ta = new JTextArea(2, 5);
            ta.setEditable(false);
            ta.setText("No Waitlist Found!");
            add(ta, BorderLayout.CENTER);
        }
        else {
            DefaultTableModel model = new DefaultTableModel(data.getData(), data.getColumnNames());
            waitlistTable = new JTable(model);
            TableUtils.addButtonCol(waitlistTable, "Edit", "Edit Waitlist");
            TableUtils.addButtonCol(waitlistTable, "Delete", "Delete from Waitlist");
            TableUtils.addButtonCol(waitlistTable, "Allocate room", "Allocate Room");


            ActionListener editAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    EditWaitlistDialog eid = new EditWaitlistDialog(table, table.getValueAt(row, 2).toString(), user.getSiteId());
                    populateTable();
                }
            };

            ActionListener deleteAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    DBCalls dbCalls = new DBCalls(sqlCall_enum.deleteFromWaitlist, new String[]
                            {table.getValueAt(row, 2).toString(), user.getSiteId()});
                    if (dbCalls.executeWriteCall() == 0) {
                        JOptionPane.showMessageDialog(null, "Delete Successful!",
                                "Delete", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Delete Failed!",
                                "Delete", JOptionPane.ERROR_MESSAGE);
                    }
                    populateTable();
                }
            };
            ActionListener allocAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    DBCalls dbCalls = new DBCalls(sqlCall_enum.deleteFromWaitlist, new String[]
                            {table.getValueAt(row, 2).toString(), user.getSiteId()});
                    if (dbCalls.executeWriteCall() == 0) {
                        dbCalls = new DBCalls(sqlCall_enum.allocroom, new String[]{ user.getSiteId()});
                        dbCalls.executeWriteCall();
                        JOptionPane.showMessageDialog(null, "Allocation Successful!",
                                "Allot Room", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Allocation Failed!",
                                "Allot Room", JOptionPane.ERROR_MESSAGE);
                    }
                    populateTable();
                }
            };
            waitlistTable.getColumn("Edit Waitlist").setCellRenderer(new ButtonEditorAndRenderer(editAL));
            waitlistTable.getColumn("Edit Waitlist").setCellEditor(new ButtonEditorAndRenderer(editAL));

            waitlistTable.getColumn("Delete from Waitlist").setCellRenderer(new ButtonEditorAndRenderer(deleteAL));
            waitlistTable.getColumn("Delete from Waitlist").setCellEditor(new ButtonEditorAndRenderer(deleteAL));

            waitlistTable.getColumn("Allocate Room").setCellRenderer(new ButtonEditorAndRenderer(allocAL));
            waitlistTable.getColumn("Allocate Room").setCellEditor(new ButtonEditorAndRenderer(allocAL));


            setColumnEditors();
            add(new JScrollPane(waitlistTable) , BorderLayout.CENTER);
        }
    }
    private void setColumnEditors() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        for (int i = 0; i < waitlistTable.getColumnCount() - 3 /*Ignore the buttons' Column*/; i++) {
            waitlistTable.getColumn(waitlistTable.getColumnName(i)).setCellEditor(new DefaultCellEditor(tf));
        }
    }
    public void reload() {
        removeAll();
        populateTable();
        setVisible(true);
    }
}
