package com.databasefrontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by adityasahai on 01/04/17.
 */
public class ClientReportDialog extends JDialog {

    private JLabel clientId;
    private String siteId;
    private JLabel bunkLabel;
    private JLabel clientName;
    private JTextField clientIdText, clientNameText;
    private JButton addlogBtn, addToWaitlistBtn, showLogsBtn, showWaitListBtn;
    private JTextArea logsText;
    private boolean logsTextClickedOn;
    private JComboBox bunkType;

    String[] bunktypes = {"None", "Male Bunks", "Female Bunks", "Mixed Bunks", "Room"};
    String[] bunktypes_colname = {"", "malebunks", "femalebunks", "mixedbunks", "roomsavail"};

    public ClientReportDialog(JTable parent, Object clientid, Object clientFirstname, String siteid) {
        super();
        setModalityType(ModalityType.APPLICATION_MODAL);
        this.siteId = siteid;
        this.clientId = new JLabel("Clientid");
        this.clientName = new JLabel("Client Name");
        this.bunkLabel = new JLabel("Choose Shelter Type");
        this.clientIdText = new JTextField(10);
        this.clientNameText = new JTextField(10);
        this.logsText = new JTextArea(50, 50);
        this.logsTextClickedOn = false;
        this.addlogBtn = new JButton("Add Log");
        this.addToWaitlistBtn = new JButton("Add to WaitList");
        this.showLogsBtn = new JButton("Show Client Logs");
        this.showWaitListBtn = new JButton("Show Client WaitList");
        this.bunkType = new JComboBox(bunktypes);

        this.bunkType.setSelectedIndex(0);

        clientIdText.setText(clientid.toString());
        clientNameText.setText(clientFirstname.toString());
        clientNameText.setEditable(false);
        clientIdText.setEditable(false);
        clientIdText.setMinimumSize(new Dimension(100, 25));
        clientNameText.setMinimumSize(new Dimension(100, 25));

        logsText.setText("Enter log entry here... Click here.");
        logsText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!logsTextClickedOn) {
                    logsText.setText("");
                    logsTextClickedOn = true;
                }
            }
        });

        setAddLogBtnAL();
        setAddToWaitListAL();
        setShowLogsBtn();
        setShowWaitListBtn();

        layoutComponents();



    }

    private void setAddLogBtnAL() {
        addlogBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String log = logsText.getText();
                if (logsTextClickedOn && !log.equals("")) {
                    if (bunkType.getSelectedIndex() != 0) {
                        DBCalls dbCalls = new DBCalls(sqlCall_enum.getActiveStates, new String[]{siteId, siteId, siteId});
                        TableData tb = dbCalls.executeReadCall();
                        if (tb != null) {
                            Object[][] data = tb.getData();
                            if (data[0][0].toString().equals("inactive")) {
                                JOptionPane.showMessageDialog(getParent(),
                                        "Cannot Allot Bunk , Shelter Service is inactive",
                                        "", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }

                        DBCalls dbCalls4 = new DBCalls(sqlCall_enum.getbunkcount, new String[]{bunktypes_colname[bunkType.getSelectedIndex()], siteId});
                        TableData tb2 = dbCalls4.executeReadCall();
                        if (tb2 != null) {
                            Object[][] data = tb2.getData();
                            System.out.println(data.toString());
                            if (data[0][0].toString().equals("0")) {
                                JOptionPane.showMessageDialog(null,
                                        "Not enough bunks available",
                                        "", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                        DBCalls dbCalls2 = new DBCalls(sqlCall_enum.addLogToClient,
                                new String[] {clientIdText.getText(), log});
                        if (dbCalls2.executeWriteCall() == 0) {
                            if (bunkType.getSelectedIndex() != 0) {
                                DBCalls dbCalls3 = new DBCalls(sqlCall_enum.decrement_bunkcount, new String[] {
                                        bunktypes_colname[bunkType.getSelectedIndex()],
                                        bunktypes_colname[bunkType.getSelectedIndex()], siteId
                                });
                                if (dbCalls3.executeWriteCall() != 0) {
                                    System.out.println("fatal error");
                                    System.exit(1);
                                }
                            }
                        }
                        JOptionPane.showMessageDialog(getParent(),
                                "Added log!",
                                "Add Log", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                    JOptionPane.showMessageDialog(getParent(), "Enter Valid Log Entry!", "Add Log",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void setShowWaitListBtn() {
        showWaitListBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBCalls dbCalls = new DBCalls(sqlCall_enum.getall_waitlistedsites, new String[] {clientIdText.getText()});
                TableData tb = dbCalls.executeReadCall();
                if (tb != null) {
                    new ResultsDialog(getParent(), tb, "Wait List for Shelter");
                } else {
                    JOptionPane.showMessageDialog(getParent(), "Could Not find any logs",
                            "Client Logs", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void setShowLogsBtn() {
        showLogsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBCalls dbCalls = new DBCalls(sqlCall_enum.getClintLogs, new String[] {clientIdText.getText()});
                TableData tb = dbCalls.executeReadCall();
                if (tb != null) {
                    new ResultsDialog(getParent(), tb, "Client Logs");
                } else {
                    JOptionPane.showMessageDialog(getParent(), "Could Not find any logs",
                            "Client Logs", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void setAddToWaitListAL() {
        addToWaitlistBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBCalls dbCalls;
                dbCalls = new DBCalls(sqlCall_enum.getActiveStates, new String[]{siteId, siteId, siteId});
                TableData tb = dbCalls.executeReadCall();
                Object[][] data = tb.getData();
                if (data[0][0].toString().equals("inactive")) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Cannot add to Waitlist, Shelter Service is inactive",
                            "Add to WaitList", JOptionPane.ERROR_MESSAGE);
                } else {
                    dbCalls = new DBCalls(sqlCall_enum.insertWaitListEntry2,
                            new String[]{clientIdText.getText(), siteId, siteId});
                    if (dbCalls.executeWriteCall() == 0) {
                        JOptionPane.showMessageDialog(null,
                                "Added to Waitlist", "", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Failed to add to waitlist",
                                "", JOptionPane.ERROR_MESSAGE);

                    }
                }
            }
        });
    }


    private void layoutComponents() {
        setLayout(new GridBagLayout());
        setSize(new Dimension(400, 500));
        setResizable(false);
        setLocationRelativeTo(getParent());
        setTitle("Client Report");
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.fill = GridBagConstraints.NONE;
        gc.weighty = 0.1;
        gc.insets = new Insets(0, 0, 0, 10);
        add(clientId, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(clientIdText, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 10);
        add(clientName, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(clientNameText, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 10);
        add(bunkLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(bunkType, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.gridwidth = 1;
        gc.insets = new Insets(0, 0, 0, 10);
        add(addlogBtn, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(showLogsBtn, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0, 0, 0, 10);
        add(addToWaitlistBtn, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0, 10, 0, 0);
        add(showWaitListBtn, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.weighty = 0.8;
        add(new JScrollPane(logsText), gc);

        setVisible(true);
    }
}
