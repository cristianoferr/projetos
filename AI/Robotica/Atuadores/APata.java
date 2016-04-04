package Atuadores;
import Component;
import RobotBasico;

import java.awt.Color;

/*
 * Created on 29/12/2004
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
public class APata extends Component {
int sizePata=15;
int angVertical=0;
int angHorizontal=0;
int minAng=-30;
int maxAng=30;
	/**
	 * @param o
	 * @param px
	 * @param py
	 */
	public APata(RobotBasico o, int px, int py) {
		super(o, px, py);
		id=9;
		cor=Color.MAGENTA;
		totInputs=4;
		totOutputs=4;
		outputs.clear();
		inputs.clear();
		for (int i=0;i<totInputs;i++){
		inputs.add(new Double(0));
		}
		for (int i=0;i<totOutputs;i++){
		outputs.add(new Double(0));
		}
		text="";
		// TODO Auto-generated constructor stub
	}

}
