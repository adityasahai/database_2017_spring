package com.databasefrontend;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 20/03/17.
 */
public class FunctionsPanel extends JTabbedPane {
    private UserClass user;
    private InventoryPanel inventoryPanel;
    private UserRequestPanel userRequestPanel;
    private OutstandingRequestPanel outstandingRequestPanel;
    private WaitlistPanel waitlistPanel;
    private SiteServicesPanel servicesPanel;

    public FunctionsPanel(UserClass user, ActionListener logoutAL) {
        this.user = user;
        this.inventoryPanel = new InventoryPanel(this.user);
        this.userRequestPanel = new UserRequestPanel(this.user);
        this.outstandingRequestPanel = new OutstandingRequestPanel(this.user);
        this.waitlistPanel = new WaitlistPanel(this.user);
        this.servicesPanel = new SiteServicesPanel(this.user);

        setSize(new Dimension(800, 100));
        this.addTab("View Inventory", this.inventoryPanel);
        this.addTab("View Waitlist", this.waitlistPanel);
        this.addTab("View My Requests", this.userRequestPanel);
        this.addTab("View Outstanding Requests", this.outstandingRequestPanel);
        this.addTab("Add New Item", new AddItemPanel(this.user));
        this.addTab("Add New Client", new AddClientPanel(this.user));
        this.addTab("Item Search", new ItemSearchPanel(this.user));
        this.addTab("Client Search", new ClientSearchPanel(this.user));
        this.addTab("Site Services for Site ID " + this.user.getSiteId(), this.servicesPanel);
        this.addTab("User Info", new UserInfoPanel(this.user, logoutAL));

        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (getSelectedIndex() == 0) {
                    inventoryPanel.reload();
                } else if (getSelectedIndex() == 1) {
                    waitlistPanel.reload();
                } else if (getSelectedIndex() == 2) {
                    userRequestPanel.reload();
                } else if (getSelectedIndex() == 3) {
                    outstandingRequestPanel.reload();
                } else if (getSelectedIndex() == 8) {
                    servicesPanel.reload();
                }
            }
        });

        setBackgroundAt(0, Color.pink);
        setBackgroundAt(1, Color.pink);
        setBackgroundAt(2, Color.pink);
        setBackgroundAt(3, Color.pink);
        setBackgroundAt(4, Color.CYAN);
        setBackgroundAt(5, Color.CYAN);
        setBackgroundAt(6, Color.green);
        setBackgroundAt(7, Color.green);
        setBackgroundAt(8, Color.GRAY);
        setBackgroundAt(9, Color.ORANGE);

        setVisible(true);
    }
}
