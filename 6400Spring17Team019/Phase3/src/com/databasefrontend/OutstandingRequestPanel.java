package com.databasefrontend;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

/**
 * Created by ggoutham on 23/03/17.
 */
public class OutstandingRequestPanel extends JPanel {
    private UserClass user;
    private JTable requests;
    private Hashtable<String, Integer> items;

    public OutstandingRequestPanel(UserClass user){
        this.user = user;
        reload();
    }

    private void layoutComponents() {
        removeAll();
        setSize(new Dimension(600, 40));
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridy = 0;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.weighty = 1.0;
        gc.weightx = 1.0;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.fill = GridBagConstraints.BOTH;

        add(new JScrollPane(this.requests), gc);
    }

    public void reload() {
        DBCalls dbCalls = new DBCalls(sqlCall_enum.getOutStandingReqs, new String[] {this.user.getSiteId()});
        TableData td = dbCalls.executeReadCall();
        DefaultTableModel model;
        this.items = new Hashtable<>();

        if (td != null) {
            model = new DefaultTableModel(td.getData(), td.getColumnNames());
            this.requests = new JTable(model);
            TableUtils.addButtonCol(requests, "Accept", "Accept Request");
            TableUtils.addButtonCol(requests, "Decline", "Decline Request");
            TableUtils.addButtonCol(requests, "Edit", "Edit Request");

            ActionListener acceptBtnAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    if (!table.getValueAt(row, 9).toString().equals("closed")) {
                        DBCalls dbCalls = new DBCalls(sqlCall_enum.acceptIncomingRequest,
                                new String[]{table.getValueAt(row, 8).toString(),
                                        table.getValueAt(row, 1).toString(),
                                        table.getValueAt(row, 8).toString(),
                                        table.getValueAt(row, 0).toString()});
                        if (dbCalls.executeWriteCall() == 0) {
                            JOptionPane.showMessageDialog(table, "Updated request",
                                    "Accept request", JOptionPane.INFORMATION_MESSAGE);
                            reload();
                        } else {
                            if (Integer.parseInt(  table.getValueAt(row, 6).toString()) <
                                    Integer.parseInt(  table.getValueAt(row, 8).toString())) {
                                JOptionPane.showMessageDialog(table, "We donot have so many items",
                                        "Accept  request", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(table, "Failed to update request",
                                        "Accept  request", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(table, "Request already closed",
                                "Accept request", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            };

            ActionListener declineBtnAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    if (!table.getValueAt(row, 9).toString().equals("closed")) {
                        DBCalls dbcalls = new DBCalls(sqlCall_enum.acceptIncomingRequest,
                                new String[]{"0", table.getValueAt(row, 1).toString(),
                                        "0", table.getValueAt(row, 0).toString()});
                        if (dbcalls.executeWriteCall() == 0) {
                            JOptionPane.showMessageDialog(table, "Updated request",
                                    "Declined request", JOptionPane.INFORMATION_MESSAGE);
                            table.setValueAt("0", row, 8);
                            table.setValueAt("closed", row, 9);
                        } else {
                            JOptionPane.showMessageDialog(table, "Failed to update request",
                                    "Declined  request", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(table, "Request Already Closed",
                                "Decline request", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            };

            ActionListener editBtnAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    if (!table.getValueAt(row, 9).toString().equals("closed")) {
                        EditRequestDialog erd = new EditRequestDialog(table, table.getValueAt(row, 0).toString(), row);
                        System.out.println(erd.isUpdated());
                        if (erd.isUpdated()) {
                            // count was updated
                            table.setValueAt(erd.getUpdatedValue(), row, 8);
                        }
                    } else {
                        JOptionPane.showMessageDialog(table, "Request Already Closed",
                                "Edit request", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            };

            requests.getColumn("Accept Request").setCellRenderer(new ButtonEditorAndRenderer(acceptBtnAL));
            requests.getColumn("Accept Request").setCellEditor(new ButtonEditorAndRenderer(acceptBtnAL));
            requests.getColumn("Decline Request").setCellRenderer(new ButtonEditorAndRenderer(declineBtnAL));
            requests.getColumn("Decline Request").setCellEditor(new ButtonEditorAndRenderer(declineBtnAL));
            requests.getColumn("Edit Request").setCellRenderer(new ButtonEditorAndRenderer(editBtnAL));
            requests.getColumn("Edit Request").setCellEditor(new ButtonEditorAndRenderer(editBtnAL));
            requests.getColumn("Units Requested").setCellRenderer(new RedCellRenderer(items));
            setColumnEditors();
            setSorting();
        } else {
            this.requests = new JTable(5, 6);
        }

        layoutComponents();
    }

    private void setSorting() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(requests.getModel());
        requests.setRowSorter(sorter);
        sorter.setComparator(0, new TableIntComparator());
        sorter.setComparator(1, new TableIntComparator());
        sorter.setComparator(6, new TableIntComparator());
        sorter.setComparator(8, new TableIntComparator());
    }

    private void setColumnEditors() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        for (int i = 0; i < requests.getColumnCount() - 3 /*Ignore the buttons' Column*/; i++) {
            requests.getColumn(requests.getColumnName(i)).setCellEditor(new DefaultCellEditor(tf));
        }
    }
}

class RedCellRenderer extends DefaultTableCellRenderer {
    private Hashtable<String, Integer> items;

    public RedCellRenderer(Hashtable items) {
        this.items = items;
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        renderer.setText(value.toString());
        // Find total count for this Item
        int itemCount = Integer.parseInt(table.getValueAt(row, 6).toString());
        String itemID = table.getValueAt(row, 1).toString();
        int reqCount = 0;
        if (this.items.containsKey(itemID)) {
            reqCount = this.items.get(itemID);
        } else {
            for (int i = 0; i < table.getRowCount(); i++) {
                if (table.getValueAt(i, 1).toString().equals(itemID) && !table.getValueAt(i, 9).toString().equals("closed")) {
                    reqCount = reqCount + Integer.parseInt(table.getValueAt(i, 8).toString());
                }
            }
            this.items.put(itemID, reqCount);
        }
        if (reqCount > itemCount && !(table.getValueAt(row, 9).toString().equals("closed"))) {
            renderer.setForeground(Color.RED);
        } else {
            renderer.setForeground(Color.BLACK);
        }
        return renderer;
    }
}
