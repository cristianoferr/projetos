import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;


public class TreeTest {
    private JFrame myFrame;
    private JTree myTree;
    private JButton addButton, removeButton;

    private int numberOfNodes;
    private DefaultMutableTreeNode rootNode;

    private ArrayList<String> graphicIDS;
    private ArrayList<String> graphicInfo;

    public static void main (String [ ] args){
        new TreeTest();
    }

    public TreeTest() {
        graphicIDS = new ArrayList<String>();
        numberOfNodes = 0;
        graphicInfo = new ArrayList<String>();
        graphicInfo.add("Info A");
        graphicInfo.add("Info B");
        graphicInfo.add("Info C");
        graphicInfo.add("Info D");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        myFrame = new JFrame("JTree test");
        myFrame.setResizable(false);
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(5,5,5,5);

        rootNode = new DefaultMutableTreeNode("Root node");
        myTree = new JTree(rootNode);
        myTree.setPreferredSize(new Dimension(200, 500));
        panel.add(myTree, c);

        c.gridwidth = 1;
        c.gridy++;
        removeButton = new JButton("Remove");
        removeButton.setEnabled(false);
        removeButton.addActionListener(new ActionListener (){
            @Override
            public void actionPerformed(ActionEvent e) {    
                System.out.println("Removed curve "+(graphicIDS.size()));
                graphicIDS.remove(graphicIDS.size()-1);
                numberOfNodes--;
                updateMeasurementsTree();
            }
        });
        panel.add(removeButton, c);

        c.gridx++;
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener (){
            @Override
            public void actionPerformed(ActionEvent e) {
                graphicIDS.add("Curve "+(numberOfNodes+1));
                System.out.println("Added curve "+(numberOfNodes+1));
                numberOfNodes++;
                updateMeasurementsTree();
            }
        });
        panel.add(addButton, c);

        myFrame.getContentPane().add(panel);
        myFrame.pack();
        myFrame.setVisible(true);
    }

    public void updateMeasurementsTree(){
        rootNode.removeAllChildren();

        for(int i=0; i<numberOfNodes;i++){  
            String idString = graphicIDS.get(i);
            DefaultMutableTreeNode idNode = new DefaultMutableTreeNode("Graphic "+idString);
            rootNode.add(idNode);
            int randomValue = (int) Math.floor(Math.random()*graphicInfo.size());
            String infoString = graphicInfo.get(randomValue);
            DefaultMutableTreeNode infoNode = new DefaultMutableTreeNode("Info "+infoString);
            idNode.add(infoNode);
        }
        if(numberOfNodes==0) removeButton.setEnabled(false);
        else{
            removeButton.setEnabled(true);
            expandAll();
        }
    }

    public void expandAll() {
        int row = 0;
        while (row < myTree.getRowCount()) {
          myTree.expandRow(row);
          row++;
        }
    }
}