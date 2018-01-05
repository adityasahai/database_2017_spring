package com.databasefrontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adityasahai on 11/03/17.
 */
public class LoginUser extends JDialog{

    private JButton okBtn;
    private JButton cancelBtn;
    private JLabel passwordLb;
    private JLabel usernameLb;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JPanel topPanel;
    private JPanel midPanel;
    private JPanel bottomPanel;
    private JPanel mainPanel;
    private boolean success;

    public LoginUser(JFrame frame) {
        // Declarations
        super(frame, "Login", true);
        success = false;

        // Panels
        mainPanel = new JPanel();
        topPanel = new JPanel();
        midPanel = new JPanel();
        bottomPanel = new JPanel();

        // Fields and buttons
        okBtn = new JButton("Login");
        cancelBtn = new JButton("Cancel");
        passwordLb = new JLabel("Password:");
        usernameLb = new JLabel("Username:");
        usernameTextField = new JTextField(15);
        passwordField = new JPasswordField(15);

        usernameTextField.setText("emp1");
        passwordField.setText("gatech123");

        // Add elements to main panel
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(topPanel);
        mainPanel.add(midPanel);
        mainPanel.add(bottomPanel);

        // Add elements to top Panel
        topPanel.setLayout(new FlowLayout());
        topPanel.add(usernameLb);
        topPanel.add(usernameTextField);

        // Add elements to mid Panel
        midPanel.setLayout(new FlowLayout());
        midPanel.add(passwordLb);
        midPanel.add(passwordField);

        // Add elements to bottom Panel
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(okBtn);
        bottomPanel.add(cancelBtn);

        // Add action Listeners to buttons
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (authenticate(getUsername(), getPassword())) {
                    JOptionPane.showMessageDialog(LoginUser.this,
                            "Hi " + getUsername() + "! You have successfully Logged in.", "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    success = true;
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginUser.this,
                            "Invalid Username or Password", "Login", JOptionPane.ERROR_MESSAGE);
                    usernameTextField.setText("");
                    passwordField.setText("");
                }
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setSize(new Dimension(300, 175));
        setMinimumSize(new Dimension(300, 175));
        add(mainPanel);
    }

    private boolean authenticate(String username, String password) {
        DBCalls dbcalls = new DBCalls(sqlCall_enum.getUser, new String[] {username, password});
        TableData tab = dbcalls.executeReadCall();
        if (tab != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public boolean isSuccess() {
        return success;
    }

}
