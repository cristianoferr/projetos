package Sensores;
import Component;
import ObjetoPlano;
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
public class SensorObjeto extends Component {
double aux=0;
	/**
	 * @param x
	 * @param y
	 */
	public SensorObjeto(RobotBasico o,int x, int y) {
		super(o,x, y);
		id=8;
		
		cor=Color.ORANGE;
		totOutputs=4;
		outputs.clear();
		for (int i=0;i<totOutputs;i++){
		outputs.add(new Double(0));
		}
		
	}
	public void turn(Graphics g){
		super.turn(g);
		double ang=0;
		double maxdist=99999;
		for (int i=0;i<totOutputs;i++){
		  outputs.setElementAt(new Double(0),i);
		}

		for (int i=0;i<owner.world.totObjeto;i++){
			ObjetoPlano obj=owner.world.getObjeto(i);
			Component comp=null;
				
			if (obj!=owner){
				comp=obj.getClosestCompoment(this);
				
				if (comp!=null) if (ObjetoPlano.distPontos(comp.getRealX(),comp.getRealX(),getRealX(),getRealY())<maxdist){
					ang=ObjetoPlano.calcAngle(comp.owner.getX(),comp.owner.getY(),owner.getX(),owner.getY());
					
					maxdist=ObjetoPlano.distPontos(comp.getRealX(),comp.getRealX(),getRealX(),getRealY());
				}
			}
		}
		
		ang=ObjetoPlano.realAngle(ang-getRealAngle());
      if ((ang<=45) || (ang>=315))
      	outputs.setElementAt(new Double(1),0);
      if ((ang>=45) && (ang<=135))
      	outputs.setElementAt(new Double(1),1);
      if ((ang>=135) && (ang<=225))
      	outputs.setElementAt(new Double(1),2);
      if ((ang>=225) && (ang<=315))
      	outputs.setElementAt(new Double(1),3);
      
      if (aux!=ang){
     //  out("ang:"+ang+" owner ang:"+owner.getAngle()+" realang:"+getRealAngle());
      aux=ang;
	  }
      
      text="";
		for (int i=0;i<totOutputs;i++){
		Double d=(Double)outputs.elementAt(i);
		  if (d.doubleValue()==1){
		  		  text=text+"1";
		  } else
		  text=text+"0";
		  
		}
	  
	}
}
