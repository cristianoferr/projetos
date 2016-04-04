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
public class SensorDirecional extends Component {

	/**
	 * @param x
	 * @param y
	 */
	int maxDist=300;
	int angleDif=30;
	boolean power=false;
	public SensorDirecional(RobotBasico o,int x, int y) {
		super(o,x, y);
		id=4;
		cor=Color.GREEN;
		totOutputs=4;
		outputs.clear();
		for (int i=0;i<totOutputs;i++){
		outputs.add(new Double(0));
		text="";
		}

	}
	public void desenha(Graphics g){
		super.desenha(g);
		//text=Double.toString(getRealAngle());
		double px;
		double py;
		/*g.setColor(Color.RED);
		int px=getRealX()+(int)(Math.cos(getRealAngle() * ObjetoPlano.deg2rad )*maxDist);
		int py=getRealY()+(int)(Math.sin(getRealAngle()*ObjetoPlano.deg2rad)*maxDist);
		g.drawLine(getRealX(),getRealY(),px,py);*/

		g.setColor(cor);
		px=getRealX()+(Math.cos((getRealAngle()-angleDif) * ObjetoPlano.deg2rad )*maxDist);
		py=getRealY()+(Math.sin((getRealAngle()-angleDif)*ObjetoPlano.deg2rad)*maxDist);
		//g.drawLine((int)getRealX(),(int)getRealY(),(int)px,(int)py);
		px=getRealX()+(Math.cos((getRealAngle()+angleDif) * ObjetoPlano.deg2rad )*maxDist);
		py=getRealY()+(Math.sin((getRealAngle()+angleDif)*ObjetoPlano.deg2rad)*maxDist);
		//g.drawLine((int)getRealX(),(int)getRealY(),(int)px,(int)py);


		double ang=getRealAngle();
		double pot=sizeComponent*1.5;

		
		double ax=getRealX();
		double ay=getRealY();
		
		ang+=angleDif;
		double bx=getRealX()+(Math.cos(ang * ObjetoPlano.deg2rad )*pot);
		double by=getRealY()+(Math.sin(ang * ObjetoPlano.deg2rad)*pot);
		
		//pot=pot/2;
		ang-=angleDif*2;
		double cx=getRealX()+(Math.cos(ang * ObjetoPlano.deg2rad )*pot);
		double cy=getRealY()+(Math.sin(ang * ObjetoPlano.deg2rad)*pot);

		
		
		int vetx[]=new int[3];
		vetx[0]=(int)ax;
		vetx[1]=(int)cx;
		vetx[2]=(int)bx;
		
		int vety[]=new int[3];
		vety[0]=(int)ay;
		vety[1]=(int)cy;
		vety[2]=(int)by;
		g.setColor(Color.BLACK);
		if (power){
			g.fillPolygon(vetx,vety,3);
		} else {
			g.drawPolygon(vetx,vety,3);
		}
			


	}
	public void turn(Graphics g){
		super.turn(g);
		power=false;
		for (int i=0;i<totOutputs;i++)
  		  outputs.setElementAt(new Double(0),i);

		
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
						double angObj=ObjetoPlano.calcAngle(getRealX(),getRealY(),comp.getRealX(),comp.getRealY());
						double difAng=ObjetoPlano.difAngles(getRealAngle(),angObj);
						//text=difAng;
  						//out("AngComp:"+getRealAngle()+" Angulo Objeto:"+angObj+" DifAng:"+difAng);
  						if (difAng<angleDif){
							outputs.setElementAt(new Double(1),0);
							double difDist=maxDist-dist;
							for (int j=0;j<totOutputs;j++){
								if (difDist>=maxDist/totOutputs*j) {
								  outputs.setElementAt(new Double(1),j);
								  power=true;
								}
							}
							//outputs=new Vector(int2bin((int)difDist,totOutputs));
							g.setColor(cor);
							g.drawLine((int)comp.getRealX(),(int)comp.getRealY(),(int)getRealX(),(int)getRealY());
  						}
					}
				
					}
				}
			
			}
		//out("o:"+outputs);
		}
}
