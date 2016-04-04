import java.awt.Color;
import java.awt.Graphics;

import Sensores.SensorToqueBola;

/*
 * Created on 18/11/2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author usuario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Bola extends RobotBasico {
	
	
	public Bola(World w,int x,int y){
		super(w,x,y,6,6);
		setCor(Color.RED);
		colocaComponents();
		name="Bola";
	}

	public void desenha(Graphics g){
		//super.desenha(g);
	
		g.setColor(getCor());
		g.fillOval((int)(getX()-getWidth()/1.6),(int)(getY()-getHeight()/1.6),getWidth(),getHeight());
	//	out("getX():"+getX());
		
		if ((getX()<20) || (getY()<20) || (getX()>world.getMaxX()-20) || (getY()>world.getMaxY()-20)) {
			setX(world.getMaxX()/2); 
			setY(world.getMaxY()/2);
		}
		
	}
	
		
	
	public void colocaComponents(){
		out("bola.colocaComponents()");
		for (int i=0;i<maxComponentX;i++){
			for (int j=0;j<maxComponentY;j++){
				SensorToqueBola comp=new SensorToqueBola(this,i,j);		
				addComponent(comp);
			}			
		}


		
	}
	
	/**
	 * @return Returns the size.
	 */
}
