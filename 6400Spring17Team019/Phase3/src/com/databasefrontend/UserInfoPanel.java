package com.databasefrontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Created by adityasahai on 16/04/17.
 */
public class UserInfoPanel extends JPanel {
    private JLabel tempLabel;
    private BufferedImage image;
    private JLabel userNameLabel, nameLabel, siteIDLabel, emailLabel;
    private JTextField userName, name, siteId, email;
    private JButton logoutBtn;

    public UserInfoPanel(UserClass user, ActionListener logoutAL) {

        URL path = UserInfoPanel.class.getResource("faceImage.png");
//        File f = new File(path.getFile());
        ImageIcon newImage = new ImageIcon(path);
//        try {
//            image = ImageIO.read(f);
//        } catch (IOException e) {
//            System.out.println("Face Image not Found");
//            System.exit(1);
//        }

        // Initialize variables
        tempLabel = new JLabel("", newImage, JLabel.CENTER);
        userNameLabel = new JLabel("Username");
        nameLabel = new JLabel("Name");
        siteIDLabel = new JLabel("Site ID");
        emailLabel = new JLabel("Email");
        userName = new JTextField(10);
        name = new JTextField(10);
        siteId = new JTextField(10);
        email = new JTextField(10);
        logoutBtn = new JButton("Logout");

        userName.setText(user.getUsername());
        name.setText(user.getFirstname() + " " + user.getLastname());
        siteId.setText(user.getSiteId());
        email.setText(user.getEmail());

        userName.setEditable(false);
        name.setEditable(false);
        siteId.setEditable(false);
        email.setEditable(false);

        logoutBtn.addActionListener(logoutAL);

        layoutComponents();
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0.1;
        gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.fill = GridBagConstraints.HORIZONTAL;
        add(tempLabel, gc);

        gc.gridy = 1;
        gc.weightx = 0.2;
        gc.gridx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0,0,0,10);
        add(userNameLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0,10,0,0);
        add(userName, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0,0,0,10);
        add(nameLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0,10,0,0);
        add(name, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0,0,0,10);
        add(emailLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0,10,0,0);
        add(email, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0,0,0,10);
        add(siteIDLabel, gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets(0,10,0,0);
        add(siteId, gc);

        gc.gridy++;
        gc.gridx = 0;
        gc.weighty = 0.8;
//        gc.weightx = 1.0;
        gc.anchor = GridBagConstraints.LINE_END;
        add(logoutBtn, gc);
    }
}
