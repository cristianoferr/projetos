package Atuadores;
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
public class ARodaEsq extends ARoda {
	
	/**
	 * @param o
	 * @param x
	 * @param y
	 */
	public ARodaEsq(RobotBasico o, int x, int y) {
		super(o, x, y);
		//grauGiro=2;
		// TODO Auto-generated constructor stub
	}
	public void turn(Graphics g){
		//disabled=owner.world.button2;
			super.turn(g);
	}

}
