package com.databasefrontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 15/04/17.
 */
public class SiteServicesPanel extends JPanel {
    private UserClass user;
    private JTable servicesTable;
    private int activeServices;

    public enum serviceType {servicefoodbank, servicesoupkitchen, serviceshelter, servicefoodpantry}

    public SiteServicesPanel(UserClass user) {
        this.user = user;
        this.activeServices = -1;
        reload();
    }
    public void reload() {
        removeAll();
        setLayout(new BorderLayout());

        populateTable();

        add(new JScrollPane(this.servicesTable), BorderLayout.CENTER);
    }

    private void populateTable() {
        // Table
        DBCalls dbcalls = new DBCalls(sqlCall_enum.getAllServices, new String[]{this.user.getSiteId(),
                this.user.getSiteId(), this.user.getSiteId(), this.user.getSiteId()});
        TableData services = dbcalls.executeReadCall();

        if (services != null) {
            DefaultTableModel model = new DefaultTableModel(services.getData(), services.getColumnNames());
            servicesTable = new JTable(model);
            TableUtils.addButtonCol(servicesTable, "Add/Remove", "Add/Remove Service");
            TableUtils.addButtonCol(servicesTable, "Edit", "Edit Service");
            TableUtils.addButtonCol(servicesTable, "Info", "Service Info");

            ActionListener addRemoveBtnAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    JTable table = source.getTable();
                    int row = source.getRow();
                    // Add or remove action
                    boolean add = table.getValueAt(row, 1).toString().equals("inactive");
                    String newStatus = add ? "active" : "inactive";
                    String buttonText = add ? "Add" : "Remove";
                    boolean removeFoodBank = !add && row == 0; // Is Food Bank and is being removed?
                    // Calculate number of currently active services
                    if (activeServices == -1) { // Calculate activeServices if hasn't been calculated yet
                        activeServices = 0;
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (table.getValueAt(i, 1).toString().equals("active")) {
                                activeServices++;
                            }
                        }
                    }
                    if (activeServices == 1 && !add) {
                        JOptionPane.showMessageDialog(table,
                                " Cannot delete Last Service!", "Remove Service",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        int dialogResult = JOptionPane.showConfirmDialog(servicesTable, "Are you sure " +
                                "you want to " + buttonText + " " + DBInterface.serviceNames[row] +
                                " Service to this Site?", buttonText, JOptionPane.YES_NO_OPTION);

                        if (dialogResult == JOptionPane.YES_OPTION) {
                            serviceType service = serviceType.values()[row];
                            DBCalls dbCalls;
                            if (add) {
                                dbCalls = new DBCalls(sqlCall_enum.addService, new String[]{service.name(), user.getSiteId()});
                            } else {
                                dbCalls = new DBCalls(sqlCall_enum.deleteService, new String[]{service.name(), user.getSiteId()});
                            }
                            if (dbCalls.executeWriteCall() == 0) {
                                table.setValueAt(newStatus, row, 1);
                                activeServices = add ? activeServices + 1 : activeServices - 1;
                                if (removeFoodBank) {
                                    dbCalls = new DBCalls(sqlCall_enum.deleteFoodbankUpdateItems,
                                            new String[]{user.getSiteId()});
                                    dbCalls.executeWriteCall();
                                    dbCalls = new DBCalls(sqlCall_enum.deleteFoodbankUpdateRequests,
                                            new String[]{user.getSiteId()});
                                    dbCalls.executeWriteCall();

                                }
                                JOptionPane.showMessageDialog(servicesTable, buttonText + " Completed!", buttonText,
                                        JOptionPane.INFORMATION_MESSAGE);

                            } else {
                                JOptionPane.showMessageDialog(servicesTable, buttonText + " Failed!", buttonText,
                                        JOptionPane.ERROR_MESSAGE);
                            }

                        }
                    }
                }
            };

            ActionListener infoButtonAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    int row = source.getRow();
                    JTable table = source.getTable();
                    System.out.println("row: " + row);
                    DBCalls dbCalls = null;
                    switch (row) {
                        case 0:
                            dbCalls = new DBCalls(sqlCall_enum.getInfoServiceFoodbank,
                                    new String[]{user.getSiteId()});
                            break;
                        case 1:
                            dbCalls = new DBCalls(sqlCall_enum.getInfoServiceFoodpantry,
                                    new String[]{user.getSiteId()});
                            break;
                        case 2:
                            dbCalls = new DBCalls(sqlCall_enum.getInfoServiceShelter,
                                    new String[]{user.getSiteId()});
                            break;
                        case 3:
                            dbCalls = new DBCalls(sqlCall_enum.getInfoServiceSoupkitchen,
                                    new String[]{user.getSiteId()});
                            break;
                        default:
                            break;
                    }
                    TableData ab = dbCalls.executeReadCall();
                    ServiceInfoDialog sid = new ServiceInfoDialog(table, ab);
                }
            };

            ActionListener editBtnAL = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TableButton source = (TableButton) e.getSource();
                    int row = source.getRow();
                    JTable table = source.getTable();
                    boolean noAction = false;
                    DBCalls dbCalls = null;
                    switch (row) {
                        case 0:
                            noAction = true;
                            break;
                        case 1:
                            dbCalls = new DBCalls(sqlCall_enum.getInfoServiceSoupkitchen,
                                    new String[] {user.getSiteId()});
                            break;
                        case 2:
                            dbCalls = new DBCalls(sqlCall_enum.getInfoServiceShelter,
                                    new String[] {user.getSiteId()});
                            break;
                        case 3:
                            dbCalls = new DBCalls(sqlCall_enum.getInfoServiceFoodpantry,
                                    new String[] {user.getSiteId()});
                            break;
                        default:
                            break;
                    }
                    if (!noAction) {
                        TableData ab = dbCalls.executeReadCall();
                        JTable editTable = new JTable(ab.getData(), ab.getColumnNames());
                        new ServiceEditDialog(editTable, editTable, row, user.getSiteId());
                    } else {
                        JOptionPane.showMessageDialog(table, "Noting to Edit in Food Bank!", "Edit",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            };

            JTextField tf = new JTextField();
            tf.setEditable(false);

            this.servicesTable.getColumn("Service Name").setCellEditor(new DefaultCellEditor(tf));
            this.servicesTable.getColumn("Status").setCellEditor(new DefaultCellEditor(tf));
            this.servicesTable.getColumn("Add/Remove Service").setCellRenderer(new ButtonEditorAndRenderer(addRemoveBtnAL));
            this.servicesTable.getColumn("Add/Remove Service").setCellEditor(new ButtonEditorAndRenderer(addRemoveBtnAL));
            this.servicesTable.getColumn("Service Info").setCellRenderer(new ButtonEditorAndRenderer(infoButtonAL));
            this.servicesTable.getColumn("Service Info").setCellEditor(new ButtonEditorAndRenderer(infoButtonAL));
            this.servicesTable.getColumn("Edit Service").setCellRenderer(new ButtonEditorAndRenderer(editBtnAL));
            this.servicesTable.getColumn("Edit Service").setCellEditor(new ButtonEditorAndRenderer(editBtnAL));

        } else {
            this.servicesTable = new JTable(4, 5);
        }
    }
}