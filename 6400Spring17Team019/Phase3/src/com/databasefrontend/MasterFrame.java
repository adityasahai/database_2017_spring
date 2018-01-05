package com.databasefrontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

/**
 * Created by adityasahai on 17/03/17.
 */
public class MasterFrame extends JFrame{
    private OpeningPanel welcome;
    private Logo image;

    public enum panelNumber {OpeningPanel, MainMenu;}

    public MasterFrame() {
        super("ASCSC - Global Reports Form");
        URL path = MasterFrame.class.getResource("ASACSlogo.png");
        File f = new File(path.getFile());
        System.out.println(f.toString());
        this.image = new Logo(f);

        this.image.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdminLogin adminLogin = new AdminLogin();
                adminLogin.setVisible(true);
                if (adminLogin.isLoginSuccess()) {
                    new AdminModule();
                }
            }
        });

        launchNewMasterFrame();
    }

    public void shuffleView(panelNumber pn, String username) {
        // Power Main Menu Here
        if (pn.equals(panelNumber.OpeningPanel)) {
            this.setTitle("ASACS - Main Menu");
            this.image.setVisible(false);
            setResizable(true);
            MainMenu mm = new MainMenu(username, this);
            this.add(mm);
            this.setSize(mm.getSize());
            refreshLocation();
            mm.setVisible(true);
        }
        else if (pn.equals(panelNumber.MainMenu)) {
            this.setTitle("ASACS - Global Reports Form");
            launchNewMasterFrame();
        }
    }

    private void launchNewMasterFrame() {
        setLayout(new BorderLayout());
        welcome = new OpeningPanel(this);

        this.image.setVisible(true);
        add(this.image, BorderLayout.CENTER);
        add(welcome, BorderLayout.PAGE_END);

        setSize(new Dimension(600, 400));
        setResizable(false);
        refreshLocation();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void refreshLocation() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2 - this.getSize().width/2, dim.height/2 - this.getSize().height/2);
    }

}
