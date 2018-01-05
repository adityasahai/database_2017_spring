package com.databasefrontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 26/03/17.
 */
public class TableButton extends JButton{
    private JTable table;
    private int row;
    private int column;

    public TableButton(ActionListener actionListener) {
        addActionListener(actionListener);
    }

    public void setData(JTable table, int row, int column, String text) {
        this.table = table;
        this.row = row;
        this.column = column;
        setText(text);
    }

    public JTable getTable() {
        return this.table;
    }

    public int getRow() {
        return this.row;
    }
}

