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
public class SensorBateria extends Component {
public double maxPower=1500000;
public double powerLeft=maxPower;
	/** 
	 * @param x
	 * @param y
	 */
	public SensorBateria(RobotBasico o,int x, int y) {
		super(o,x, y);
		
		cor=Color.pink;
		totOutputs=4;
		outputs.clear();
		usingEnergy=false;
		for (int i=0;i<totOutputs;i++){
		outputs.add(new Double(0));
		}
		
	}
	public void turn(Graphics g){
		super.turn(g);
		for (int i=0;i<totOutputs;i++){
		  outputs.setElementAt(new Double(1),i);
		}
		double perc=(double)(powerLeft/maxPower*100);
		//out("perc:"+perc+" maxPower:"+maxPower+" powerleft:"+powerLeft+" "+powerLeft/maxPower);
      if (perc>20)
      	outputs.setElementAt(new Double(0),0);
		if (perc>40)
      	outputs.setElementAt(new Double(0),1);
		if (perc>60)
      	outputs.setElementAt(new Double(0),2);
		if (perc>80)
      	outputs.setElementAt(new Double(0),3);
      
      text=""+power;
		
	}
	public int batteryComsumption(int e) {
		if (powerLeft>=e){
			powerLeft-=e;
			e=0;
		} else {
			e-=(int)powerLeft;
			powerLeft=0;
		}
		return e;
	}
	public double batteryRecharge(double d){
		//if (d>0) out("Recarregando:"+d+" power:"+powerLeft);
		powerLeft+=d;
		d=0;
		if (powerLeft>=maxPower){
			d=powerLeft-maxPower;
			powerLeft=maxPower;
		}
		return d;
	}
	
	
}
