/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package finaldefenseproject01;
//import com.formdev.flatlaf.FlatIntelliJLaf;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

/**
 *
 * @author SENGOUA NATHAN
 */
public class FinalDefenseProject01 {

    /**
     * @param args the command line arguments
     */
   /* public static void main(String[] args) {
        // TODO code application logic here
        try{
            //set the FlatLaf look and feel
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            LOGIN L= new LOGIN();
                L.setVisible(true);
        }catch(Exception ex){
            System.err.println("Failed to initialize FlatLaf: " + ex.getMessage());
        }
    
    }*/
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            //set the FlatLaf look and feel
            UIManager.setLookAndFeel(new FlatLightLaf());
            LOGIN L= new LOGIN();
                L.setVisible(true);
        }catch(Exception ex){
            System.err.println("Failed to initialize FlatLaf: " + ex.getMessage());
        }
    
    }
    
}
