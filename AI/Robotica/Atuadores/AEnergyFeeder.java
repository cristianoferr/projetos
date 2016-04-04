package Atuadores;
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
public class AEnergyFeeder extends Component {
boolean power=false;
int maxDist=25; 
int energyPerTurn=1000;
	/**
	 * @param x
	 * @param y
	 */
	public AEnergyFeeder(RobotBasico o,int x, int y) {
		super(o,x, y);
		id=5;
		cor=Color.PINK;
		usingEnergy=false;
		totOutputs=1;
		outputs.clear();
		for (int i=0;i<totOutputs;i++){
  			outputs.add(new Double(0));
		}
		totInputs=1;
		inputs.clear();
		for (int i=0;i<totInputs;i++){
			inputs.add(new Double(0));
		}
		text="";
		// TODO Auto-generated constructor stub
	}
	public void turn(Graphics g){
		outputs.setElementAt(new Double(0),0);
		
		Double p=(Double)inputs.elementAt(0);
		power=(p.doubleValue()>0);
		for (int i=0;i<owner.world.totObjeto;i++){
			ObjetoPlano obj=owner.world.getObjeto(i);
			Component comp=null;
			
			if ((obj!=owner) && (obj.isEmiteEnergia())){
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
					if (power) {
						owner.AddtoEnergy(energyPerTurn);
						addGlobalPerf(5);
					} else {
						addGlobalPerf(-5);			
					}
				} else {
					if (power) {
						addGlobalPerf(-5);
					} else {
						addGlobalPerf(5);			
					}
				}
				
				}
			}
			
		}
	}

	public void desenha(Graphics g){
		super.desenha(g);
		
		g.setColor(Color.BLACK);
		if (power){
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
