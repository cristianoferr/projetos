package Sensores;
import Component;
import RobotBasico;

import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on 18/11/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author CMM4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SensorBussola extends Component {

	/** 
	 * @param x
	 * @param y
	 */
	public SensorBussola(RobotBasico o,int x, int y) {
		super(o,x, y);
		id=3;
		
		cor=Color.ORANGE;
		totOutputs=4;
		outputs.clear();
		for (int i=0;i<totOutputs;i++){
		outputs.add(new Double(0));
		}
		
	}
	public void turn(Graphics g){
		super.turn(g);
		for (int i=0;i<totOutputs;i++){
		  outputs.setElementAt(new Double(0),i);
		}
		
		double angle=owner.getAngle();
      if ((angle<=45) || (angle>=315))
      	outputs.setElementAt(new Double(1),0);
      if ((angle>=45) && (angle<=135))
      	outputs.setElementAt(new Double(1),1);
      if ((angle>=135) && (angle<=225))
      	outputs.setElementAt(new Double(1),2);
      if ((angle>=225) && (angle<=315))
      	outputs.setElementAt(new Double(1),3);
      
      text="";
		for (int i=0;i<totOutputs;i++){
		Double d=(Double)outputs.elementAt(i);
		  if (d.doubleValue()==1){
		  		//  text=text+"1";
		  } else;
		//  text=text+"0";
		  
		}
	  
	}
}
