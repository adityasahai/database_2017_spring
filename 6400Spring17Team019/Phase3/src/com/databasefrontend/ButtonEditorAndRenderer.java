package com.databasefrontend;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class ButtonEditorAndRenderer extends AbstractCellEditor implements
        TableCellEditor, TableCellRenderer {

    private TableButton buttonEditor;
    private TableButton buttonRenderer;

    public ButtonEditorAndRenderer(ActionListener actionListener) {
        buttonEditor = new TableButton(actionListener);
        buttonRenderer = new TableButton(actionListener);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        buttonEditor.setData(table, row, column, (String) value);
        return buttonEditor;
    }

    @Override
    public Object getCellEditorValue() {
        return buttonEditor.getText();

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        buttonRenderer.setText((String) value);
        return buttonRenderer;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }
}