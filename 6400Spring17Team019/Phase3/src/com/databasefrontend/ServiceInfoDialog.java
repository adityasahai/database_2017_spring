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
public class ServiceInfoDialog extends JDialog {
    JTable serviceInfo;

    public ServiceInfoDialog(JTable parent, TableData data) {
        super();
        setModalityType(ModalityType.APPLICATION_MODAL);

        serviceInfo = new JTable(data.getData(), data.getColumnNames());
        serviceInfo.setEnabled(false);

        setTitle("Information About Service");

        setSize(new Dimension(700, 100));
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        add(new JScrollPane(serviceInfo), BorderLayout.CENTER);
        setVisible(true);
    }



}
