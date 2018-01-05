package com.databasefrontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 23/03/17.
 */
public class InventoryPanel extends JPanel {
    private UserClass user;
    JTable inventoryTable;
    public InventoryPanel(UserClass user){
        this.user = user;
        reload();
    }

    private void populateTable() {
        DBCalls dbCalls = new DBCalls(sqlCall_enum.viewInventory, new String[] {this.user.getSiteId()});
        TableData data = dbCalls.executeReadCall();
        if (data == null) {
            JTextArea ta = new JTextArea(2, 5);
            ta.setEditable(false);
            ta.setText("No Record Found!");
            add(ta, BorderLayout.CENTER);
        }
        else {
            data.addColumn("Edit", "Edit Data");
            inventoryTable = new JTable(data.getData(), data.getColumnNames());
            ActionListener editAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    EditInventoryDialog eid = new EditInventoryDialog(inventoryTable, table.getValueAt(row, 0).toString(),
                            table.getValueAt(row, 1).toString(), table.getValueAt(row, 5).toString(), row);

                }
            };

            inventoryTable.getColumn("Edit Data").setCellEditor(new ButtonEditorAndRenderer(editAL));
            inventoryTable.getColumn("Edit Data").setCellRenderer(new ButtonEditorAndRenderer(editAL));

            setColumnEditors();
            setSorting();
            add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        }
    }

    private void setSorting() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(inventoryTable.getModel());
        inventoryTable.setRowSorter(sorter);
        sorter.setComparator(0, new TableIntComparator());
        sorter.setComparator(5, new TableIntComparator());
    }
    private void setColumnEditors() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        for (int i = 0; i < inventoryTable.getColumnCount() - 1 /*Ignore the buttons' Column*/; i++) {
            inventoryTable.getColumn(inventoryTable.getColumnName(i)).setCellEditor(new DefaultCellEditor(tf));
        }
    }
    public void reload() {
        removeAll();
        setSize(new Dimension(600, 50));
        setLayout(new BorderLayout());

        populateTable();

        setVisible(true);
    }
}
