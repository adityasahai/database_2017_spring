package com.databasefrontend;

import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * Created by adityasahai on 23/03/17.
 */
public class TableUtils {
    public static void addButtonCol(JTable table, String buttonText, String header) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String [] eachRow = new String[table.getRowCount()];
        int numCols = model.getColumnCount();
        for (int i = 0; i < table.getRowCount(); i++) {
            eachRow[i] = buttonText;
        }
        model.addColumn(header, eachRow);
    }
}
