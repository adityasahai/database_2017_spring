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
 * Created by adityasahai on 28/03/17.
 */
public class ItemRequestDialog extends JDialog{
    private JTextField reqCount;
    private JTable itemInfoTable;
    private JButton requestButton, cancelButton;
    private UserClass user;

    public ItemRequestDialog(UserClass user, Object[] rowdata) {
        super();
        setModalityType(ModalityType.APPLICATION_MODAL);

        this.user = user;
        setTitle("Request " + rowdata[1]);
        this.reqCount = new JTextField("Enter count here...",10);
        Object[][] data = {{"Item Name", rowdata[1]}, {"Item Category", rowdata[2]}, {"Item SubCat", rowdata[3]},
                {"Storage Type", rowdata[4]}, {"Expiry", rowdata[6]}, {"Site ID", rowdata[7]}};
        this.itemInfoTable = new JTable(data, new String[] {"Name", "Value"});

        JTextField tf = new JTextField();
        tf.setEditable(false);

        this.itemInfoTable.setEnabled(false);

        requestButton = new JButton("Request");
        cancelButton = new JButton("Cancel");

        requestButton.setEnabled(false);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        requestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reqcount = 0;
                try {
                    reqcount = Integer.parseInt(reqCount.getText());
                    if ((reqcount <= Integer.parseInt(rowdata[5].toString()))) {
                        DBCalls dbCalls;
                        dbCalls = new DBCalls(sqlCall_enum.requestItem, new String[] {rowdata[0].toString(),
                                        reqCount.getText(), user.getUsername()});
                        if (dbCalls.executeWriteCall() == 0) {
                            JOptionPane.showMessageDialog(null, "Request Submitted!",
                                "Requsted!", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Reqeust added!");
                        }
                         dispose();
                    } else {
                    JOptionPane.showMessageDialog(null, "They only have " +
                            rowdata[5].toString() + " units", "Error: cannot request!", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex)  {
                    JOptionPane.showMessageDialog(null, "Please enter Integer!",
                            "", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.reqCount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                reqCount.setText("");
            }
        });

        this.reqCount.getDocument().addDocumentListener(new DocumentListener() {
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
                requestButton.setEnabled(true);
            }
        });

        layoutComponents();
    }

    private void layoutComponents() {
        setResizable(false);
        setSize(new Dimension(300, 200));
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridy = 0;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10,0,0,0);
        gc.weighty = 0.1;
        gc.gridwidth = 3;

        add(this.itemInfoTable, gc);

        gc.gridwidth = 1;
        gc.gridy++;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(0,0,0,0);
        gc.weighty = 0.1;
        add(reqCount, gc);


        gc.weighty = 0.1;
        gc.gridy++;
        add(requestButton, gc);
        gc.gridx++;
        add(cancelButton, gc );
    }

}
