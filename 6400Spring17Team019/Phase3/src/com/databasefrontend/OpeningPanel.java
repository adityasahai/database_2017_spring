package com.databasefrontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 17/03/17.
 */
public class OpeningPanel extends JPanel{
    private boolean success;
    private JButton loginBtn;
    private JLabel welcomeLabel;
    private JButton viewShelter;
    private JButton viewMeals;
    private JPanel buttonsPanel;
    private MasterFrame.panelNumber pn;

    public OpeningPanel(MasterFrame frame) {
        this.pn = MasterFrame.panelNumber.OpeningPanel;

        // Meals Button
        viewMeals = new JButton("View Number of Meals Available Report");
        viewMeals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewMealsReport vmr = new ViewMealsReport(frame);
                if (!vmr.isTableNull()) {
                    vmr.setLocationRelativeTo(frame);
                    vmr.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(OpeningPanel.this,
                            "Error! Data not found!", "View Meals Report", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Shelter Button
        viewShelter = new JButton("View Number of Bunks and Rooms Report");
        viewShelter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewBunksReport vbr = new ViewBunksReport(frame);
                if (!vbr.isTableNull()) {
                    vbr.setLocationRelativeTo(frame);
                    vbr.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(OpeningPanel.this,
                            "Error! Data not found!", "View Bunks Report", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Login Button
        loginBtn = new JButton("LOGIN");
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginUser loginDiag = new LoginUser(frame);
                loginDiag.setLocationRelativeTo(frame);
                loginDiag.setVisible(true);
                success = loginDiag.isSuccess();
                if (success) {
                    // Open New Panel here
                    setVisible(false);
                    frame.shuffleView(pn, loginDiag.getUsername());
                }
            }
        });

        // Buttons Panel
        buttonsPanel = new JPanel();


        // Welcome Text
        welcomeLabel = new JLabel("Welcome to ASACS System!");
        Font currentFont = welcomeLabel.getFont();
        int fontSizeToUse = 25;
        welcomeLabel.setFont(new Font(currentFont.getName(), Font.BOLD, fontSizeToUse));

        setMinimumSize(new Dimension(200, 100));
        setSize(new Dimension(600, 200));

        layoutComponents();
    }

    public void layoutComponents() {
        setLayout(new GridBagLayout());

        buttonsPanel.setLayout(new GridLayout(3, 1));

        buttonsPanel.add(loginBtn);
        buttonsPanel.add(viewMeals);
        buttonsPanel.add(viewShelter);

        GridBagConstraints gc = new GridBagConstraints();

        // First Row
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(0, 10, 20, 0);

        gc.weightx = 1;
        gc.weighty = 0.2;

        gc.gridwidth = 3;

        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.PAGE_START;
        add(welcomeLabel, gc);

        // Second Row
        gc.gridy+= 1;

        gc.weightx = 0.5;
        gc.weighty = 1;
        gc.gridheight = 1;

        gc.gridx = 2;
        gc.insets = new Insets(30, 0, 0, 0);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.gridwidth = 1;

        add(buttonsPanel, gc);
    }
}
