package com.databasefrontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ggoutham on 23/03/17.
 */
public class AddClientPanel extends JPanel {
    String[] idtypes = new String[] {"driving_license", "ssn", "passport",
            "birth_certificate", "refugee_travel_document", "visa"};

    private UserClass user;
    private JLabel firstName, lastName, phoneNumber, idNumber, idType;
    private JTextField firstNameTextField, lastNameTextField, phoneNumberTextField, idNumberTextField;
    private JButton addBtn, cancelBtn;

    private JComboBox idtypeslist;


    public AddClientPanel(UserClass user){
        this.user = user;
        this.firstNameTextField = new JTextField(25);
        this.lastNameTextField = new JTextField(25);
        this.phoneNumberTextField = new JTextField(25);
        this.idNumberTextField = new JTextField(25);
        this.firstName = new JLabel("Enter First Name here");
        this.lastName = new JLabel("Enter Last Name here");
        this.phoneNumber = new JLabel("Enter Phone Number here");
        this.idNumber = new JLabel("Enter ID Number here");
        this.idType = new JLabel("Select ID Type");

        this.idtypeslist = new JComboBox();
        for (String s : idtypes) {
            idtypeslist.addItem(toTitleCase(s));
        }

        this.addBtn = new JButton("Add");
        this.cancelBtn = new JButton("Cancel");

        this.cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Set all text fields to null
                clear();

            }
        });

        this.addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (phoneNumberTextField.getText().equals("")) {
                    phoneNumberTextField.setText("0");
                }
                if (idNumberTextField.getText().equals("") || firstNameTextField.getText().equals("") ||
                    lastNameTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Client Addition Failed, Please " +
                                    "enter valid input!","Add",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DBCalls dbCalls = new DBCalls(sqlCall_enum.addClient, new String[]{idNumberTextField.getText(),
                        idtypes[idtypeslist.getSelectedIndex()], firstNameTextField.getText(),
                        lastNameTextField.getText(), phoneNumberTextField.getText(),
                        idNumberTextField.getText(), idtypes[idtypeslist.getSelectedIndex()]});
                if (dbCalls.executeWriteCall() == 0) {
                    JOptionPane.showMessageDialog(null, "Client Added!", "Add",
                            JOptionPane.INFORMATION_MESSAGE);
                    clear();
                } else {
                    try {
                        Integer.parseInt(phoneNumberTextField.getText()) ;
                        JOptionPane.showMessageDialog(null, "Client Addition Failed, Please " +
                                        "recheck inputs!","Add",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (Exception f) {
                        JOptionPane.showMessageDialog(null, "Please enter valid phone " +
                                        "number","Add",
                                JOptionPane.ERROR_MESSAGE);

                    }


                }
            }
        });

        layoutComponents();
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

    private void layoutComponents() {

        setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();


        //////////// First row ///////////////////////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 5);
        add(this.firstName, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.firstNameTextField, gc);

        ////////////Second row ///////////////////////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridy = 1;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.lastName, gc);

        gc.gridy = 1;
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.lastNameTextField, gc);

        ////////////Third row ///////////////////////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridy = 2;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.phoneNumber, gc);

        gc.gridy = 2;
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.phoneNumberTextField, gc);

        ////////////Fourth row ///////////////////////////////////

        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridy = 3;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.idNumber, gc);

        gc.gridy = 3;
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.idNumberTextField, gc);

        ////////////Fifth row ///////////////////////////////////
        gc.weightx = 1;
        gc.weighty = 0.1;

        gc.gridy = 4;
        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        add(this.idType, gc);

        gc.gridy = 4;
        gc.gridx = 1;
        gc.insets = new Insets(0, 0, 0, 0);
        gc.anchor = GridBagConstraints.LINE_START;
        add(this.idtypeslist, gc);


        ////////////Sixth row ///////////////////////////////////
        gc.weighty = 2.0;

        gc.gridy = 5;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets(10, 0, 0, 10);
        add(this.addBtn, gc);

        gc.gridx++;
        gc.insets = new Insets(10, 10, 0, 0);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(this.cancelBtn, gc);

    }

    private void clear() {
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        phoneNumberTextField.setText("");
        idNumberTextField.setText("");
        idtypeslist.setSelectedIndex(0);
    }

}


