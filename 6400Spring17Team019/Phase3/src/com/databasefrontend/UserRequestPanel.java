package com.databasefrontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 02/04/17.
 */
public class UserRequestPanel extends JPanel {
    private UserClass user;
    private JTable requests;
    private JTable inventoryitems;
    public UserRequestPanel(UserClass user) {
        this.user = user;
        reload();
    }

    private void layoutComponents() {
        removeAll();
        setSize(new Dimension(600, 40));
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 1.0;
        gc.weightx = 1.0;
        gc.fill = GridBagConstraints.BOTH;

        add(new JScrollPane(this.requests), gc);
    }

    public void reload() {
        DBCalls dbCalls = new DBCalls(sqlCall_enum.getUserRequests, new String[] {this.user.getUsername()});
        TableData td = dbCalls.executeReadCall();
        if (td != null) {
            DefaultTableModel model = new DefaultTableModel(td.getData(), td.getColumnNames());
            inventoryitems = new JTable(model);
            TableUtils.addButtonCol(inventoryitems, "Cancel", "Cancel Request");
            ActionListener editAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    String requestid = table.getValueAt(row, 0).toString();
                    if (!table.getValueAt(row, 6).equals("Canceled") &&
                        !table.getValueAt(row, 6).equals("closed")) {
                        DBCalls dbCalls = new DBCalls(sqlCall_enum.canceluserrequest, new String[]{requestid});
                        if (dbCalls.executeWriteCall() == 0) {
                            JOptionPane.showMessageDialog(null, "Successfully cancelled request!",
                                    "Cancel request", JOptionPane.INFORMATION_MESSAGE);
                            table.setValueAt("Canceled", row, 6);
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to cancel request",
                                    "Cancel request", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Request has already been Cancelled or Closed",
                                "Cancel Request", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            inventoryitems.getColumn("Cancel Request").setCellEditor(new ButtonEditorAndRenderer(editAL));
            inventoryitems.getColumn("Cancel Request").setCellRenderer(new ButtonEditorAndRenderer(editAL));
            this.requests = inventoryitems;
            setSorting();
        } else {
            this.requests = new JTable(3, 6);
        }
        setColumnEditors();
        layoutComponents();
    }

    private void setSorting() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(inventoryitems.getModel());
        inventoryitems.setRowSorter(sorter);
        sorter.setComparator(0, new TableIntComparator());
        sorter.setComparator(5, new TableIntComparator());
        sorter.setComparator(7, new TableIntComparator());

    }
    private void setColumnEditors() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        for (int i = 0; i < requests.getColumnCount() - 1 /*Ignore the buttons' Column*/; i++) {
            requests.getColumn(requests.getColumnName(i)).setCellEditor(new DefaultCellEditor(tf));
        }
    }
}
