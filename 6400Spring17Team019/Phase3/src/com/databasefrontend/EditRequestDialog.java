package com.databasefrontend;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by adityasahai on 15/04/17.
 */
public class EditRequestDialog extends JDialog{
    String reqId;
    JTable infoTable;
    JTextField updateText;
    JButton okBtn, cancelBtn;
    JTable parent;

    public int getUpdatedValue() {
        return updatedValue;
    }

    public void setUpdatedValue(int updatedValue) {
        this.updatedValue = updatedValue;
    }

    int updatedValue;

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    boolean updated;

    public EditRequestDialog(JTable parent, String reqId, int row) {
        super();
        this.reqId = reqId;
        this.parent = parent;
        setModalityType(ModalityType.APPLICATION_MODAL);
        Object [][] data = {{"Item Name", parent.getValueAt(row, 2).toString()},
                {"Units Available", parent.getValueAt(row, 6).toString()},
                {"Units Requested", parent.getValueAt(row, 8),toString()}};
        infoTable = new JTable(data, new String[]{"column1", "column2"});
        updateText = new JTextField();
        okBtn = new JButton("OK");
        cancelBtn = new JButton("Cancel");
        setUpdated(false);
        setUpdatedValue(0);

        okBtn.setEnabled(false);
        infoTable.setEnabled(false);
        updateText.setText("Enter new Count here");

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpdated(false);
                dispose();
            }
        });

        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBCalls dbCalls = new DBCalls(sqlCall_enum.updateRequestCount, new String[] {updateText.getText(), reqId});
                if (dbCalls.executeWriteCall() == 0) {
                    JOptionPane.showMessageDialog(parent, "Request Quantity Updated", "Update Requet",
                            JOptionPane.INFORMATION_MESSAGE);
                    setUpdated(true);
                    setUpdatedValue(Integer.parseInt(updateText.getText()));
                } else {
                    JOptionPane.showMessageDialog(parent, "Request Quantity Update Failed", "Update Request",
                            JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });

        this.updateText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateText.setText("");
            }
        });

        this.updateText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableButton();
            }

            private void enableButton() {
                okBtn.setEnabled(true);
            }
        });

        layoutComponents();
    }

    private void layoutComponents() {
        setTitle("Edit Outstanding Request");
        setSize(new Dimension(300, 200));
        setResizable(false);
        setLocationRelativeTo(this.parent);

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridy = 0;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10,0,0,0);
        gc.weighty = 0.1;
        gc.gridwidth = 3;

        add(infoTable, gc);

        gc.gridwidth = 1;
        gc.gridy++;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(0,0,0,0);
        gc.weighty = 0.1;
        add(updateText, gc);


        gc.weighty = 0.1;
        gc.gridy++;
        add(okBtn, gc);
        gc.gridx++;
        add(cancelBtn, gc );

        setVisible(true);

    }
}
