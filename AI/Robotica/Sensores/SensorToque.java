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
public class SensorToque extends Component {
int power=0;
int maxDist=25; 
	/**
	 * @param x
	 * @param y
	 */
	public SensorToque(RobotBasico o,int x, int y) {
		super(o,x, y);
		id=5;
		cor=Color.PINK;
		totOutputs=1;
		outputs.clear();
		for (int i=0;i<totOutputs;i++){
		outputs.add(new Double(0));
		}
		text="";
		// TODO Auto-generated constructor stub
	}
	public void turn(Graphics g){
		outputs.setElementAt(new Double(0),0);
		power=0;
		for (int i=0;i<owner.world.totObjeto;i++){
			ObjetoPlano obj=owner.world.getObjeto(i);
			Component comp=null;
			
			if ((obj!=owner) && (obj.isPickedPossible())){
				comp=obj.getClosestCompoment(this);
				if (comp!=null) {
					
				double dist=ObjetoPlano.distPontos(comp.getRealX(),comp.getRealY(),getRealX(),getRealY());
				//text=Double.toString(dist);
				//out("Owner:"+comp.owner+" Comp:"+comp+" dist:"+dist);
				
				if (dist<maxDist) {
					outputs.setElementAt(new Double(1),0);
					//out("Proximo!");
					g.setColor(cor);
					g.drawLine((int)comp.getRealX(),(int)comp.getRealY(),(int)getRealX(),(int)getRealY());
					power=1;
				}
				
				}
			}
			
		}
	}

	public void desenha(Graphics g){
		super.desenha(g);
		
		g.setColor(Color.BLACK);
		if (power==1){
			g.fillOval((int)getRealX(),(int)getRealY(),sizeComponent,sizeComponent);
		} else {
			g.setColor(Color.RED);
			g.drawOval((int)getRealX(),(int)getRealY(),sizeComponent,sizeComponent);
		}
			
		g.fillOval((int)getRealX(),(int)getRealY(),sizeComponent,sizeComponent);
		//out("ax:"+(int)ax+" bx: "+(int)bx+" cx:"+(int)cx+" dx:"+(int)dx );
		//out("ay:"+(int)ay+" by: "+(int)by+" cy:"+(int)cy+" dy:"+(int)dy );
		

	}

}
