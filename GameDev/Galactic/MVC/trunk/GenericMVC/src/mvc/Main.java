/*
 * Main.java
 *
 * Created on January 10, 2007, 1:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mvc;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import mvc.controller.DefaultController;
import mvc.model.DocumentModel;
import mvc.model.TextElementModel;
import mvc.view.DisplayViewPanel;
import mvc.view.PropertiesViewPanel;

/**
 *
 * @author Robert Eckstein
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
        
        TextElementModel textElementModel = new TextElementModel();
        DocumentModel documentModel = new DocumentModel();

        DefaultController controller = new DefaultController();
        
        DisplayViewPanel displayViewPanel = new DisplayViewPanel(controller);       
        PropertiesViewPanel propertiesViewPanel = new PropertiesViewPanel(controller);
        PropertiesViewPanel propertiesViewPanel2 = new PropertiesViewPanel(controller);
        
        controller.addView(displayViewPanel);
        controller.addView(propertiesViewPanel);
        controller.addView(propertiesViewPanel2);
        controller.addModel(textElementModel);
        controller.addModel(documentModel);
        
        textElementModel.initDefault();
        documentModel.initDefault();        
        
        
        JFrame displayFrame = new JFrame("Display (View 1)");
        displayFrame.getContentPane().add(displayViewPanel, BorderLayout.CENTER);
        displayFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        displayFrame.pack();
        
        JDialog propertiesDialog = new JDialog(displayFrame, "Properties (View 2)");
        propertiesDialog.setModal(false);
        propertiesDialog.getContentPane().add(propertiesViewPanel, BorderLayout.CENTER);
        propertiesDialog.pack();
        
        JDialog propertiesDialog2 = new JDialog(displayFrame, "Properties (View 2)");
        propertiesDialog2.setModal(false);
        propertiesDialog2.getContentPane().add(propertiesViewPanel2, BorderLayout.CENTER);
        propertiesDialog2.pack();
        
        displayFrame.setVisible(true);
        propertiesDialog.setVisible(true);
        propertiesDialog2.setVisible(true);
        

        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main main = new Main();
    }
    
}
