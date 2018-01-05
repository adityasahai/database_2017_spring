package com.databasefrontend;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by adityasahai on 20/03/17.
 */
public class Logo extends JPanel {
    private BufferedImage image;

    public Logo(File file) {
        try {
            image = ImageIO.read(file);
        } catch (IOException ex) {
            System.out.println("IO Exception.. Exiting");
            System.out.println(ex.toString());
            System.exit(1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        setLocation(dim.width/2 - this.getSize().width/2, dim.height/2 - this.getSize().height/2);
        g.drawImage(image, 190, 0, 250, 200,this);
    }
}
