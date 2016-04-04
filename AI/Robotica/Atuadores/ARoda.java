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
public class ARoda extends Component {
	int grauGiro=10;
	final double potencia=0.6;
	
	/**
	 * @param x
	 * @param y
	 */
	public ARoda(RobotBasico o,int x, int y) {
		super(o,x, y);
		id=1;
		power=false;
		cor=Color.BLACK;
		totInputs=1;
		outputs.clear();
		for (int i=0;i<totInputs;i++){
		inputs.add(new Double(0));
		}
		text="";
		// TODO Auto-generated constructor stub
	}

	public void turn(Graphics g){
		super.turn(g);
		Double power=(Double)inputs.elementAt(0);
		double pot=(potencia*power.doubleValue());
		this.power=(pot>0);
		
			double ang=grauGiro*pot+getRealAngle();
			owner.setAddtoAngle((grauGiro*pot));
			double ax=owner.getX()+(Math.cos(ang * ObjetoPlano.deg2rad )*pot);
			double ay=owner.getY()+(Math.sin(ang * ObjetoPlano.deg2rad)*pot);
			owner.setAddtoX((ax-owner.getX()));
			owner.setAddtoY((ay-owner.getY()));
			//out("x:"+owner.getPosx()+" px:"+ax+" giro:"+grauGiro+" pot:"+pot);
			//owner.setPosx(ax);
			//owner.setPosy(ay);
			
	}
	public void desenha(Graphics g){
		super.desenha(g);
		double ang=getRealAngle();
		double pot=sizeComponent*1.5;

		
		double ax=getRealX()+(Math.cos(ang * ObjetoPlano.deg2rad )*pot);
		double ay=getRealY()+(Math.sin(ang * ObjetoPlano.deg2rad)*pot);
		
		ang+=180;
		double bx=getRealX()+(Math.cos(ang * ObjetoPlano.deg2rad )*pot);
		double by=getRealY()+(Math.sin(ang * ObjetoPlano.deg2rad)*pot);
		
		pot=pot/2;
		ang-=90;
		double cx=getRealX()+(Math.cos(ang * ObjetoPlano.deg2rad )*pot);
		double cy=getRealY()+(Math.sin(ang * ObjetoPlano.deg2rad)*pot);

		ang+=180;
		double dx=getRealX()+(Math.cos(ang * ObjetoPlano.deg2rad )*pot);
		double dy=getRealY()+(Math.sin(ang * ObjetoPlano.deg2rad)*pot);
		
		
		int vetx[]=new int[4];
		vetx[0]=(int)ax;
		vetx[1]=(int)cx;
		vetx[2]=(int)bx;
		vetx[3]=(int)dx;
		
		int vety[]=new int[4];
		vety[0]=(int)ay;
		vety[1]=(int)cy;
		vety[2]=(int)by;
		vety[3]=(int)dy;	
		g.setColor(Color.BLACK);
		if (power){
			g.fillPolygon(vetx,vety,4);
		} else {
			g.drawPolygon(vetx,vety,4);
		}
		//out("ax:"+(int)ax+" bx: "+(int)bx+" cx:"+(int)cx+" dx:"+(int)dx );
		//out("ay:"+(int)ay+" by: "+(int)by+" cy:"+(int)cy+" dy:"+(int)dy );
		

	}
}
