package io.github.autobleem.abcoveredit.ui.components;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author artur.jakubowicz
 */
public class JImagePanel extends JPanel {

    private BufferedImage image;

    public JImagePanel() {
            image = null;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }
    
    

    public void loadImage(String path)
    {
         try {                
          image = ImageIO.read(new File(path));
           repaint();
       } catch (IOException ex) {
            image = null;
             repaint();
            
       }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image!=null)
        {
           g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
        } else
        {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 226, 226);
        }
    }

}
