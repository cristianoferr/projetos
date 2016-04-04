

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
public class AGarra extends Component {

double potencia=30;

ObjetoPlano grabbed=null;
	/**
	 * @param x
	 * @param y
	 */
	public AGarra(RobotBasico o,int x, int y) {
		super(o,x, y);
		id=7;
		power=false;
		cor=Color.BLUE;
		cor=Color.BLACK;
		totInputs=1;
		totOutputs=2;
		inputs.clear();
		for (int i=0;i<totInputs;i++){
		  inputs.add(new Double(0));
		}
		outputs.clear();
		for (int i=0;i<totOutputs;i++){
		  outputs.add(new Double(0));
		}
	}
	
	public void turn(Graphics g){
		super.turn(g);
		
		double ax=getRealX()+(Math.cos(getRealAngle() * ObjetoPlano.deg2rad )*sizeComponent*4);
		double ay=getRealY()+(Math.sin(getRealAngle() * ObjetoPlano.deg2rad )*sizeComponent*4);
		if (grabbed!=null) {
			grabbed.setX(ax);
			grabbed.setY(ay);		
		}
		
		Double p=(Double)inputs.elementAt(0);
		if (p.doubleValue()>0){ 
			power=!power;
		//	out("Garra "+power); 
			grabbed=null;
			if (power) {
				for (int i=0;i<owner.world.totObjeto;i++){
					ObjetoPlano obj=owner.world.getObjeto(i);
					Component comp=null;
					
					if ((obj!=owner) && (obj.isPickedPossible())){
						comp=obj.getClosestCompoment(this);
						if (comp!=null) {
							double dist=ObjetoPlano.distPontos(comp.getRealX(),comp.getRealY(),getRealX(),getRealY());
							//text=Double.toString(dist);
							//out("Owner:"+comp.owner+" Comp:"+comp+" dist:"+dist);
							if (dist<potencia) {
								grabbed=obj;
							}
						}
					}
				}
			} else {
				grabbed=null;
			}
			
		}
		inputs.setElementAt(new Double(0),0);

		if (power){
			outputs.setElementAt(new Double(1),0);
		}
		else {
			outputs.setElementAt(new Double(0),0);
		}
		if (grabbed!=null){
			outputs.setElementAt(new Double(0),1); }
		else {
			outputs.setElementAt(new Double(0),0);
		}
/*			double ang=grauGiro*pot+getRealAngle();
			owner.setAddtoAngle((grauGiro*pot));
			double ax=owner.getX()+(Math.cos(ang * ObjetoPlano.deg2rad )*pot);
			double ay=owner.getY()+(Math.sin(ang * ObjetoPlano.deg2rad)*pot);
			owner.setAddtoX((ax-owner.getX()));
			owner.setAddtoY((ay-owner.getY()));
			//out("x:"+owner.getPosx()+" px:"+ax+" giro:"+grauGiro+" pot:"+pot);
			//owner.setPosx(ax);
			//owner.setPosy(ay);*/
			
	}
	
	public void desenha(Graphics g){
	super.desenha(g);
	double ang=getRealAngle();
	double ang2=getRealAngle();
	double pot=potencia;
	if (power){
			g.setColor(Color.BLACK);
	} else {
		g.setColor(Color.red);
	}
		
	double ax=getRealX()+(Math.cos(ang * ObjetoPlano.deg2rad )*sizeComponent*4);
	double ay=getRealY()+(Math.sin(ang * ObjetoPlano.deg2rad)*sizeComponent*4);
		
	if (!power){
		ang+=45;
		ang2-=45;	
	} else{
	}
	
	double bx=ax+(Math.cos(ang * ObjetoPlano.deg2rad )*pot);
	double by=ay+(Math.sin(ang * ObjetoPlano.deg2rad)*pot);
		
	double cx=ax+(Math.cos(ang2 * ObjetoPlano.deg2rad )*pot);
	double cy=ay+(Math.sin(ang2 * ObjetoPlano.deg2rad)*pot);

	if (power){
			g.setColor(Color.BLACK);
	} else {
		g.setColor(Color.red);
	}
			
	g.drawLine((int)getRealX(),(int)getRealY(),(int)ax,(int)ay);
	g.drawLine((int)ax,(int)ay,(int)bx,(int)by);
	g.drawLine((int)ax,(int)ay,(int)cx,(int)cy);
	//out("ax:"+(int)ax+" bx: "+(int)bx+" cx:"+(int)cx+" dx:"+(int)dx );
	//out("ay:"+(int)ay+" by: "+(int)by+" cy:"+(int)cy+" dy:"+(int)dy );
		

}

}
