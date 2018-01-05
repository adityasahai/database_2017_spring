package com.databasefrontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by adityasahai on 16/04/17.
 */
public class AdminLogin extends JDialog{

    private JTextField username;
    private JPasswordField password;
    private JButton loginBtn, cancelBtn;

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }

    private boolean isLoginSuccess;

    public AdminLogin() {
        super();
        setModalityType(ModalityType.APPLICATION_MODAL);

        username = new JTextField(20);
        password = new JPasswordField(20);
        loginBtn = new JButton("Login");
        cancelBtn = new JButton("Cancel");
        isLoginSuccess = false;

        username.setForeground(Color.white);
        password.setForeground(Color.white);
        username.setBackground(Color.black);
        password.setBackground(Color.black);
        username.setCaretColor(Color.white);
        password.setCaretColor(Color.white);

        username.setText("Admin UserName here... Click here");

        username.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                username.setText("");
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (username.getText().equals("Admin") && getPassword().equals("magic123")) {
                    setLoginSuccess(true);
                } else {
                    setLoginSuccess(false);
                    JOptionPane.showMessageDialog(getParent(), "Admin Login not Successful! " +
                            "Perhaps you found this by mistake!", "Admin Login", JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });

        layoutComponents();
    }

    private String getPassword() {
        return new String(password.getPassword());
    }

    private void layoutComponents() {
        setSize(new Dimension(300, 120));
        setResizable(false);
        setLocationRelativeTo(getParent());
        setTitle("Admin Login");

        setLayout(new FlowLayout());
        add(username);
        add(password);
        add(loginBtn);
        add(cancelBtn);
    }

}
