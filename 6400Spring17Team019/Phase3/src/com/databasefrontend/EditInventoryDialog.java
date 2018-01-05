package com.databasefrontend;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 01/04/17.
 */
public class EditInventoryDialog extends JDialog {

    private JTable parent;
    private JLabel itemName;
    private JLabel itemIDLabel;
    private int row;
    private JTextField itemCountTextField;

    public EditInventoryDialog(JTable parent, String itemID, String itemName, String itemCount, int row) {
        super();
        setModalityType(ModalityType.APPLICATION_MODAL);
        this.itemName = new JLabel("Item Name - " + itemName);
        this.itemCountTextField = new JTextField(itemCount, 3);
        this.itemIDLabel = new JLabel("Item ID - " + itemID);
        this.parent = parent;
        this.row = row;

        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");

        updateBtn.setEnabled(false);

        this.itemCountTextField.getDocument().addDocumentListener(new DocumentListener() {
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
                updateBtn.setEnabled(true);
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean cannot_update = false;
                String newValue = itemCountTextField.getText();
                if (Integer.parseInt(newValue) > Integer.parseInt(itemCount)) {
                    cannot_update = true;
                }
                DBCalls dbcalls = new DBCalls(sqlCall_enum.editItemCount, new String[]{newValue, itemID});
                if (!cannot_update && dbcalls.executeWriteCall() == 0) {
                    JOptionPane.showMessageDialog(parent, "Update Successful!", "Update",
                            JOptionPane.INFORMATION_MESSAGE);
                    parent.setValueAt(newValue, row, 5 /* Item count column number */);

                }
                else {
                    JOptionPane.showMessageDialog(parent, cannot_update?"Items can only be added through " +
                                    " add items panel":"Update Failed!", "Update",
                            JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });

        // Layout Componenets
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.weighty = 0.1;
        add(this.itemIDLabel, gc);
        gc.gridy++;
        add(this.itemName, gc);
        gc.gridy++;
        gc.weighty = 0.5;
        add(this.itemCountTextField, gc);
        gc.gridy++;
        gc.gridy++;
        gc.gridx = 0;
        gc.weighty = 0.1;
        add(updateBtn, gc);
        gc.gridx++;
        add(cancelBtn, gc);


        setTitle("Edit Inventory - " + itemName);

        setSize(new Dimension(500, 300));
        setMinimumSize(new Dimension(200, 100));
        setLocationRelativeTo(this.parent);

        setVisible(true);
    }

}
