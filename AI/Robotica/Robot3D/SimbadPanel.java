/*
 * Created on 04/03/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package Robot3D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
class SimbadPanel extends JPanel {
Vector input=new Vector();
Vector output=new Vector();	
	SimbadPanel(SimbadRobot robot){
		setName("Robot");
		setPreferredSize(new Dimension(400,100));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLocation(new Point(100,200));
		JButton button=new JButton("ok");
		add(new JLabel(" "));
		add(new JButton("teste"));
		
		JTextArea jtextArea = new JTextArea(30, 10);
		jtextArea.setEditable(false);
		jtextArea.setAutoscrolls(true);
		jtextArea.setFont(new Font("Courier", Font.PLAIN, 10));
		add(jtextArea);
		jtextArea.setText(robot.getName());

		
	}
            protected void paintComponent(Graphics g) {
                int width = 300;
                int height =300;
				
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0,0,input.size(),4);
				g.fillRect(0,4,output.size(),8);
                g.setColor(Color.BLACK);
                for (int i = 0; i < input.size(); i += 1) {
                	Integer v=(Integer)input.elementAt(i);
                  if (v.intValue()==1){
                  	 g.setColor(Color.BLACK);
                  } else {
					g.setColor(Color.WHITE);
                  }
                  
                    //System.out.println(1);
                   g.fillRect(i*4, 0, 4, 4);
                }
				for (int i = 0; i < output.size(); i += 1) {
					Integer v=(Integer)output.elementAt(i);
				  if (v.intValue()==1)
				   g.fillRect(i*4, 4, 4, 4);
				}
                
                
            }
/**
 * @return
 */
public Vector getInput() {
	return input;
}

/**
 * @return
 */
public Vector getOutput() {
	return output;
}

/**
 * @param vector
 */
public void setInput(Vector vector) {
	input = vector;
}

/**
 * @param vector
 */
public void setOutput(Vector vector) {
	output = vector;
}

        }
