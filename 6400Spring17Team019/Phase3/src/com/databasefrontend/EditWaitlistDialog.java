package com.databasefrontend;



import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ChandrikaMohith on 03/04/17.
 */
public class EditWaitlistDialog extends JDialog {

    private JTable parent;
    private JLabel cwlNumber;
    private JTextField nwlNumber;
    private JLabel nwlNumberL;
    private String siteID;
    JButton updateBtn;
    JButton cancelBtn;


    public EditWaitlistDialog(JTable parent, String cwlNumber, String siteID) {
        super();
        setModalityType(ModalityType.APPLICATION_MODAL);
        this.cwlNumber = new JLabel("Current Waitlist Number - " + cwlNumber);
        this.nwlNumberL = new JLabel("Enter New Waitlist Number - ");
        this.nwlNumber = new JTextField(3);
        this.parent = parent;
        this.siteID = siteID;
        this.updateBtn = new JButton("Update");
        this.cancelBtn = new JButton("Cancel");

        updateBtn.setEnabled(false);

        this.nwlNumber.getDocument().addDocumentListener(new DocumentListener() {
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
                DBCalls dbCalls = new DBCalls(sqlCall_enum.updateWaitList, new String[] {cwlNumber, nwlNumber.getText(), siteID});
                if (dbCalls.executeWriteCall() == 0) {
                    JOptionPane.showMessageDialog(null, "Table Updated", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Table Update Failed", "Update Failed", JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });

        layoutComponents();

    }
    private void layoutComponents(){
        // Layout Componenets
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.weighty = 0.1;
        gc.gridy++;
        add(this.cwlNumber, gc);
        gc.gridy++;
        gc.weighty = 0.5;
        add(this.nwlNumberL, gc);
        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx++;
        gc.weightx = 0.5;
        add(this.nwlNumber, gc);

        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 10);
        gc.gridy++;
        gc.gridx = 0;
        gc.weighty = 0.1;
        add(updateBtn, gc);
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        gc.gridx++;
        add(cancelBtn, gc);


        setTitle("Edit Waitlist");

        setSize(new Dimension(300, 150));
        setMinimumSize(new Dimension(200, 100));
        setLocationRelativeTo(this.parent);

        setVisible(true);
    }


}
