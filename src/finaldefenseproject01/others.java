/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finaldefenseproject01;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *0
 * @author SENGOUA NATHAN
 */
public class others {
    
    
    
    public void fitImageToLable(JLabel label){
        Icon icon = label.getIcon();
        if(icon != null && icon instanceof ImageIcon){
            Image image = ((ImageIcon)icon).getImage();
            Image scaledImage = image.getScaledInstance(label.getWidth(),label.getHeight(),Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImage));
        }
        
    }
    public void fitshowimage(JLabel label, String path){
        ImageIcon icon = new ImageIcon(path); // Replace "path_to_your_image.jpg" with your image path
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        label.setIcon(scaledIcon);
    }
    
}
