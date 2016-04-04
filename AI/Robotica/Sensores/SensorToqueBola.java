package Sensores;

import ObjetoPlano;
import RobotBasico;

import java.awt.Graphics;

/*
 * Created on 19/11/2004
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
public class SensorToqueBola extends SensorToque {
int potencia=10;
	/**
	 * @param o
	 * @param x
	 * @param y
	 */
	public SensorToqueBola(RobotBasico o, int x, int y) {
		super(o, x, y);
		maxDist=5; 
		usingEnergy=false;
	}
	public void turn(Graphics g){
		super.turn(g);
		double ang=ObjetoPlano.calcAngle(getRealX(),getRealY(),owner.getX(),owner.getY());
		double px=Math.cos((ang-0) * ObjetoPlano.deg2rad )*potencia;
		double py=Math.sin((ang-0)*ObjetoPlano.deg2rad)*potencia;
		
	//	g.setColor(Color.GREEN);
	//	g.drawLine((int)getRealX(),(int)getRealY(),
	//	(int)(owner.getPosx()+px),(int)(owner.getPosy()+py));
		if (power==1){
			//out("a: rx:"+getRealX()+" ry:"+getRealX()+" px:"+px+" py:"+py);
			
			
	//		g.setColor(Color.RED);
			
//			g.drawLine((int)getRealX(),(int)getRealY(),
//			(int)(owner.getPosx()+px),(int)(owner.getPosy()+py));
			//out("b: rx:"+getRealX()+" ry:"+getRealX()+" px:"+px+" py:"+py);
//			g.setColor(Color.BLACK);
//			g.setColor(Color.blue);
			if ((owner.getAddtoX()==0)&& (owner.getAddtoY()==0)){
			owner.setAddtoX(px);
			owner.setAddtoY(py);
		
			//out(" run: "+World.run+" ang:"+(int)ang+" px:"+(int)px+" py:"+(int)py+" x:"+owner.getPosx()+" y:"+owner.getPosy());
			}
		}
	}

}
