package com.databasefrontend;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by adityasahai on 01/04/17.
 */
public class EditClientDialog extends JDialog {

    private JTable parent;
    private JLabel firstname;
    private JLabel lastname;
    private JLabel idnumber;
    private JLabel idtype;
    private JLabel phonenumber;
    private JTextField firstnametf;
    private JTextField lastnametf;
    private JTextField idnumbertf;
    private JTextField phonenumbertf;
    private JComboBox idtypetf;
    private boolean idValueChanged;

    public boolean isClientUpdated() {
        return clientUpdated;
    }

    public void setClientUpdated(boolean clientUpdated) {
        this.clientUpdated = clientUpdated;
    }

    private boolean clientUpdated;
    private JButton saveBtn, cancelBtn;

    String[] idtypes = new String[] {"driving_license", "ssn", "passport",
            "birth_certificate", "refugee_travel_document", "visa"};

    public EditClientDialog(JTable table, int row) {
        super();
        setModalityType(ModalityType.APPLICATION_MODAL);
        this.parent = table;
        this.firstname = new JLabel("First Name");
        this.lastname = new JLabel("Last Name");
        this.idtype = new JLabel("ID Type");
        this.idnumber = new JLabel("ID Number");
        this.phonenumber = new JLabel("Phone Number");
        this.firstnametf = new JTextField(10);
        this.lastnametf = new JTextField(10);
        this.idnumbertf = new JTextField(10);
        this.phonenumbertf = new JTextField(10);
        this.idtypetf = new JComboBox();
        this.idValueChanged = false;
        this.clientUpdated = false;


        // Set Values
        for (String s : idtypes) {
            idtypetf.addItem(toTitleCase(s));
        }
        this.firstnametf.setText(table.getValueAt(row, 3).toString());
        this.lastnametf.setText(table.getValueAt(row, 4).toString());
        this.idnumbertf.setText(table.getValueAt(row, 1).toString());
        if (table.getValueAt(row, 5) == null) {
            this.phonenumbertf.setText("0");
        } else {
            this.phonenumbertf.setText(table.getValueAt(row, 5).toString());
        }

        // Add document listeners
        DocListener firstNameDocListener = new DocListener();
        DocListener lastNameDocListener = new DocListener();
        DocListener idnumberDocListener = new DocListener();
        DocListener phonenumberDocListener = new DocListener();
        this.firstnametf.getDocument().addDocumentListener(firstNameDocListener);
        this.lastnametf.getDocument().addDocumentListener(lastNameDocListener);
        this.idnumbertf.getDocument().addDocumentListener(idnumberDocListener);
        this.phonenumbertf.getDocument().addDocumentListener(phonenumberDocListener);
        this.idtypetf.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println("Id Type Value Changed");
                setIdValueChanged(true);
            }
        });

        for (int i = 0; i < idtypes.length; i++) {
            if (table.getValueAt(row, 2).toString().equals(idtypes[i])) {
                idtypetf.setSelectedIndex(i);
            }
        }

        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> log = new ArrayList<String>( );
                boolean valueChanged = false;

                if (firstnametf.getText().equals("") || lastnametf.getText().equals("") ||
                        idnumbertf.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Firstname, lastname " +
                                    " and idnumber cannot be empty!","Add",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (firstNameDocListener.isValueChanged()) {
                    log.add("First Name changed from " + table.getValueAt(row, 3).toString() +
                            " to " + firstnametf.getText());
                    valueChanged = true;
                }
                if (lastNameDocListener.isValueChanged()) {
                    log.add("Last Name changed from " + table.getValueAt(row, 4).toString() +
                            " to " + lastnametf.getText());
                    valueChanged = true;
                }
                if (idnumberDocListener.isValueChanged()) {
                    log.add("ID Number changed from " + table.getValueAt(row, 1).toString() +
                            " to " + idnumbertf.getText());
                    valueChanged = true;
                }
                if (phonenumberDocListener.isValueChanged()) {
                    try {
                        Integer.parseInt(phonenumbertf.getText());
                        log.add("Phone Number changed from " + table.getValueAt(row, 5).toString() +
                                " to " + phonenumbertf.getText());
                        valueChanged = true;
                    } catch (Exception f) {
                        JOptionPane.showMessageDialog(parent, "Please enter valid phone number", "Update",
                                JOptionPane.ERROR_MESSAGE);
                        return;

                    }
                }
                if (isIdValueChanged()) {
                    log.add("ID Type changed from " + toTitleCase(table.getValueAt(row, 2).toString()) +
                            " to " + toTitleCase(idtypes[idtypetf.getSelectedIndex()]));
                    valueChanged = true;
                }

                if (valueChanged) {
                    DBCalls dbcalls = new DBCalls(sqlCall_enum.editClient, new String[]{
                            idnumbertf.getText(), idtypes[idtypetf.getSelectedIndex()], firstnametf.getText(),
                            lastnametf.getText(), phonenumbertf.getText(),
                            table.getValueAt(row, 0).toString(), table.getValueAt(row, 0).toString(),
                            String.join(", ",log.toArray(new String[0]))});
                    if (dbcalls.executeWriteCall() == 0) {
                        JOptionPane.showMessageDialog(parent, "Update Successful!", "Update",
                                JOptionPane.INFORMATION_MESSAGE);
                        setClientUpdated(true);

                    } else {
                        JOptionPane.showMessageDialog(parent, "Update Failed!", "Update",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(getParent(), "No Values have been Changed!",
                            "Edit Client", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        layoutComponents();
    }

    public boolean isIdValueChanged() {
        return idValueChanged;
    }

    public void setIdValueChanged(boolean idValueChanged) {
        this.idValueChanged = idValueChanged;
    }

    private void layoutComponents() {
        // Layout Componenets
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0.1;
        gc.weightx = 0.5;
        gc.insets = new Insets(0, 0, 0, 10);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.firstname, gc);
        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(this.firstnametf, gc);
        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 10);
        add(this.lastname, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(this.lastnametf, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 10);
        add(this.idtype, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(this.idtypetf, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 10);
        add(this.idnumber, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(this.idnumbertf, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 10);
        add(this.phonenumber, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(this.phonenumbertf, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 10);
        add(saveBtn, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(cancelBtn, gc);


        setTitle("Edit Client");
        setSize(new Dimension(400, 250));
        setResizable(false);
        setLocationRelativeTo(this.parent);
        setVisible(true);
    }

    private static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                c = ' ';
            }
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}

class DocListener implements DocumentListener {

    private boolean valueChanged;

    public DocListener() {
        this.valueChanged = false;
    }

    public boolean isValueChanged() {
        return valueChanged;
    }

    public void setValueChanged(boolean valueChanged) {
        this.valueChanged = valueChanged;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updated();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updated();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updated();
    }

    public void updated() {
        setValueChanged(true);
    }
}
