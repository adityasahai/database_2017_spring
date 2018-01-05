package com.databasefrontend;

import javax.swing.*;

/**
 * Created by adityasahai on 17/03/17.
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                }
                catch (Exception ex1) {
                    System.out.println(ex1.toString());
                    try {
                        UIManager.setLookAndFeel("javax.swing.plaf.basic");
                    }
                    catch (Exception ex2) {
                        System.out.println(ex2.toString());
                    }
                }
                new MasterFrame();
            }
        });
    }
}
