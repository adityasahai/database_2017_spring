package com.databasefrontend;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by adityasahai on 15/04/17.
 */
public class AdminModule extends JDialog{
    private Container parent;
    private JTextArea queryArea;
    private TableData resultsData;
    private JButton clearBtn, executeBtn;
    private boolean neverClickedOn;

    public AdminModule() {
        super();
        this.parent = getParent();
        setModalityType(ModalityType.MODELESS);
        setTitle("Admin Module");
        queryArea = new JTextArea("Enter your SQL query here...", 10, 20);
        executeBtn = new JButton("Execute");
        clearBtn = new JButton("Clear");

        queryArea.setBackground(Color.black);
        queryArea.setForeground(Color.white);
        queryArea.setFont(new Font("courier", Font.PLAIN, 13));
        queryArea.setCaretColor(Color.white);
        queryArea.setLineWrap(true);

        neverClickedOn = true;

        queryArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (neverClickedOn) {
                    queryArea.setText("");
                    neverClickedOn = false;
                }
            }
        });

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryArea.setText("");
            }
        });

        executeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DBInterface db = new DBInterface();
                    Connection connection = db.connect();
                    Statement st = connection.createStatement();
                    boolean hasResults = false;
                    for (String s : queryArea.getText().split(";")) {
                        hasResults = st.execute(s + ";");
                    }
                    if (hasResults) {
                        ResultSet rs = st.getResultSet();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int j = 0;
                        String[] colNames = new String[rsmd.getColumnCount()];
                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            colNames[j++] = rsmd.getColumnName(i);
                        }
                        java.util.List<String[]> list = new ArrayList<>();
                        AtomicInteger index = new AtomicInteger();
                        while (rs.next()) {
                            String[] row = new String[rsmd.getColumnCount()];
                            index.set(0);
                            for (int colindex = 1; colindex <= rsmd.getColumnCount(); colindex++) {
                                row[index.getAndIncrement()] = rs.getString(colindex);
                            }
                            list.add(row);
                        }
                        resultsData = new TableData();
                        Object[][] data = new Object[list.size()][];
                        Iterator<String[]> iterator = list.iterator();
                        int k = 0;
                        while (iterator.hasNext()) {
                            data[k++] = iterator.next();
                        }
                        resultsData.setNumRows(list.size());
                        resultsData.setData(data);
                        resultsData.setColumnNames(colNames);
                        showResults();
                        rs.close();
                    } else {
                        JOptionPane.showMessageDialog(executeBtn, "Query Successful",
                                "Query", JOptionPane.INFORMATION_MESSAGE);
                    }
                    st.close();
                    connection.close();
                } catch (java.sql.SQLTimeoutException j) {
                    System.out.println(queryArea.getText());
                    JOptionPane.showMessageDialog(executeBtn, "DataBase Timed Out! " + j.getMessage(),
                            "Query", JOptionPane.ERROR_MESSAGE);

                } catch (java.sql.SQLException j) {
                    JOptionPane.showMessageDialog(executeBtn, "DataBase Error! " + j.getMessage(),
                            "Query", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        layoutComponents();
    }

    private void layoutComponents() {
        setLocationRelativeTo(parent);
        setSize(new Dimension(400, 220));
        setResizable(true);
        setLayout(new BorderLayout());

        add(new JScrollPane(queryArea), BorderLayout.NORTH);
        add(executeBtn, BorderLayout.CENTER);
        add(clearBtn, BorderLayout.PAGE_END);
        setVisible(true);

    }

    private void showResults() {
        new ResultsDialog(this, this.resultsData, "Admin Module");
    }
}
