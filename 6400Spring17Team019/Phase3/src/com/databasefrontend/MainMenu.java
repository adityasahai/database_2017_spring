package com.databasefrontend;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 19/03/17.
 */
public class MainMenu extends JPanel {
    private MasterFrame.panelNumber pn;
    private UserClass userData;
    private ActionListener logoutAL;

    // Panels and components
    private FunctionsPanel siteFunctionsTabbedPane;

    public MainMenu(String username, MasterFrame frame) {
        this.pn = MasterFrame.panelNumber.MainMenu;
        // Get User Data
        DBCalls dbCalls = new DBCalls(sqlCall_enum.getUserData, new String[]{username});
        TableData tb = dbCalls.executeReadCall();
        JTable table = new JTable(tb.getData(), tb.getColumnNames());
        String email = null, firstname = null, lastname = null, siteid = null;
        for (int i=0; i < table.getColumnCount(); i++)  {
            switch (table.getColumnName(i).toString()) {
                case "email":
                    email = table.getValueAt(0, i).toString();
                    break;
                case "firstname":
                    firstname = table.getValueAt(0, i).toString();
                    break;
                case "lastname":
                    lastname = table.getValueAt(0, i).toString();
                    break;
                case "siteid":
                    siteid = table.getValueAt(0,i).toString();
                    break;
                default:
                    break;
            }
        }
        this.userData = new UserClass(username, email, "", firstname, lastname, siteid);

        this.logoutAL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to Logout?",
                        "Logout", JOptionPane.OK_CANCEL_OPTION);
                if (dialogResult == JOptionPane.OK_OPTION) {
                    setVisible(false);
                    frame.shuffleView(pn, username);
                }
            }
        };

        // Populate and add Site Functions Panel
        populateSiteFunctions();

        layoutComponents();

        setVisible(true);
    }

    public void populateSiteFunctions() {
        // Functions
        this.siteFunctionsTabbedPane = new FunctionsPanel(this.userData, this.logoutAL);
    }

    public void layoutComponents() {
        setSize(new Dimension(1500, 800));
        setLayout(new BorderLayout());
        add(new JScrollPane(siteFunctionsTabbedPane), BorderLayout.CENTER);
    }
}
