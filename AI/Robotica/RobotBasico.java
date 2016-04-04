import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import Atuadores.AEnergyFeeder;
import Atuadores.AGarra;
import Atuadores.ARodaDir;
import Atuadores.ARodaEsq;
import Sensores.SensorBateria;
import Sensores.SensorDirecional;
import Sensores.SensorObjeto;
import Sensores.SensorSom;
import Sensores.SensorToque;


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
public class RobotBasico  extends ObjetoPlano{
int maxComponentX=6;
int maxComponentY=6;
int maxComponentZ=1;
Component comps[][][];
Component compsX[];
int timer=0;
int status=0;//0=idle 1=busy
boolean hasEnergy=true;
boolean energyAcquired=false;
double globalPerf=0;
Vector display=new Vector(); 
 
int totComp=0;
	/**
	 * 
	 */
	public RobotBasico(World w,int x,int y,int maxCX,int maxCY){
		super(x,y);
		name="Robot";
		setCor(Color.MAGENTA);
		setX(x);
		world=w;
		maxComponentX=maxCX;
		maxComponentY=maxCY;
		setY(y);
		setWidth((maxComponentX)*Component.sizeComponent);
		setHeight((maxComponentY)*Component.sizeComponent);
		comps=new Component[maxComponentX][maxComponentY][maxComponentZ];
		compsX=new Component[maxComponentX*maxComponentY];
		for (int i=0;i<maxComponentX;i++){
				for (int j=0;j<maxComponentX;j++){
				  comps[i][j][0]=new Component(this,i,j);
				}
		}
		
	}
	
	public void addComponent(Component comp){
		if (comps[comp.getX()][comp.getY()]!=null){
			//System.out.println("Componente existente em:"+comp.getX()+","+comp.getY());
		}
		comps[comp.getX()][comp.getY()][comp.getZ()]=comp;
		compsX[totComp]=comp;
		totComp++;
	}
	public Vector getInputs(){
		Vector vet=new Vector();
		for (int i=0;i<totComp;i++){
			compsX[i].getInputs(vet);
		}
		return vet;
	}

	public Vector getOutputs(){
		Vector vet=new Vector();
		for (int i=0;i<totComp;i++){
			vet=compsX[i].getOutputs(vet);
		}
		return vet;
	}	

	public void setInputs(Vector vet){
		int p=0;
   		for (int i=0;i<totComp;i++){
			p=compsX[i].setInput(vet,p);
			
		}
	}	
	public void colocaComponents(){
		ARodaEsq roda1=new ARodaEsq(this,0,0);		
		addComponent(roda1);
		ARodaDir roda2=new ARodaDir(this,0,5);
		//roda2.setAngle(45);   		
		addComponent(roda2);
		AGarra garra=new AGarra(this,5,3);		
		addComponent(garra);
		//SensorBussola bussola=new SensorBussola(this,1,3);		
		//addComponent(bussola);
		SensorObjeto objeto=new SensorObjeto(this,2,3);		
		addComponent(objeto);
		SensorDirecional sensordir=new SensorDirecional(this,5,2);		
		addComponent(sensordir);
		SensorDirecional sensordir2=new SensorDirecional(this,5,0);
		sensordir2.setAngle(0);		
		addComponent(sensordir2);
		SensorDirecional sensordir3=new SensorDirecional(this,5,5);
		sensordir3.setAngle(0);		
		addComponent(sensordir3);
		SensorDirecional sensordir4=new SensorDirecional(this,4,0);
		sensordir4.setAngle(-90);		
		addComponent(sensordir4);
		SensorDirecional sensordir5=new SensorDirecional(this,4,4);
		sensordir5.setAngle(90);		
		addComponent(sensordir5);
		SensorDirecional sensordir6=new SensorDirecional(this,0,3);
		sensordir6.setAngle(180);		
		addComponent(sensordir6);
		

		SensorToque toque1=new SensorToque(this,3,0);		
		addComponent(toque1);
		SensorToque toque2=new SensorToque(this,3,5);		
		addComponent(toque2);
		SensorToque toque3=new SensorToque(this,5,1);		
		addComponent(toque3);
		SensorToque toque4=new SensorToque(this,5,4);		
		addComponent(toque4);
		SensorToque toque5=new SensorToque(this,0,2);		
		addComponent(toque5);
		SensorBateria bat=new SensorBateria(this,4,1);		
		addComponent(bat);
		AEnergyFeeder pow=new AEnergyFeeder(this,4,2);		
		addComponent(pow);
		SensorSom som1=new SensorSom(this,2,0);		
		addComponent(som1);
		SensorSom som2=new SensorSom(this,2,5);		
		addComponent(som2);

		
	}
	public Component getClosestCompoment(Component obj){
		Component comp=null;
		int maxdist=99999;
	//	out("1a "+totComp);
		for (int i=0;i<totComp;i++){
			//if (compsX[i]!=null){
				
			  int dist=(int)distPontos(compsX[i].getRealX(),compsX[i].getRealY(),
			  		obj.getRealX(),obj.getRealY());
			  //out("2 "+dist);
			  if (dist<maxdist){
			  //	out("3");
			  	maxdist=dist;
			  	comp=compsX[i];
			  }
			//}
	}

		
		return comp;
	}
	public void turn(Graphics g){
		super.turn(g);
		display.clear();
		

		int energiaGasta=50;
			for (int i=0;i<totComp;i++){
			  energiaGasta+=compsX[i].getPowerConsumption();
	 
			}
		double d=getAddtoEnergy();
		energyAcquired=(d>0);	
		for (int i=0;i<totComp;i++){
			energiaGasta=compsX[i].batteryComsumption(energiaGasta);
			d=compsX[i].batteryRecharge(d);
		}
		hasEnergy=!(energiaGasta>0);
		//if (id==0) out("hasEnergy: "+hasEnergy+" energiaGasta: "+energiaGasta+" name:"+name);
		
		
		
		for (int i=0;i<totComp;i++){
			if ((hasEnergy) || (!compsX[i].isUsingEnergy()))
			  compsX[i].turn(g);
		}
		
		Vector v=getInputs();
		if (v.size()>0){
			//v.setElementAt(new Double(1),0);
		//	v.setElementAt(new Double(1),1);
		    //setInputs(v);
		}

				
				
		int pmeiox=Math.round(maxComponentX/2);
		int pmeioy=Math.round(maxComponentY/2);
		
		for (int i=0;i<maxComponentX;i++){
			for (int j=0;j<maxComponentY;j++) {
			  double ang=getAngle()+calcAngle(pmeiox,pmeioy,i,j);
			  double dist=(distPontos(pmeiox,pmeioy,i,j))*(Component.sizeComponent);
			 //   out("pmeiox:"+pmeiox+" pmeioy:"+pmeioy+" i:"+i+" j:"+j+" dist:"+distPontos(pmeiox,pmeioy,i,j));
			  double px=getX()+(Math.cos(ang * deg2rad )*dist);
			  double py=getY()+(Math.sin(ang*deg2rad)*dist);
			  if (comps[i][j]!=null){
				  comps[i][j][0].setRealX(px);
				  comps[i][j][0].setRealY(py);
				}
			}	
		}		
	}
	public void desenha(Graphics g){

		
		double x=1;
		double fx=0;
		double fy=0;
		double ex=0;
		double ey=0;
		double gx=0;
		double gy=0;
		double hx=0;
		double hy=0;

		for (int i=0;i<maxComponentX;i++){
			for (int j=0;j<maxComponentY;j++) {
			  double px=comps[i][j][0].getRealX();
			  double py=comps[i][j][0].getRealY();
			  comps[i][j][0].desenha(g);
			  if ((i==0) && (j==0)){
			  	hx=px;
			  	hy=py;
			  }
			  if ((i==maxComponentX-1) && (j==0)){
			  	ex=px;
			  	ey=py;
			  }			  
			  if ((i==maxComponentX-1) && (j==maxComponentY-1)){
			  	fx=px;
			  	fy=py;
			  }					  
			  if ((i==0) && (j==maxComponentY-1)){
			  	gx=px;
			  	gy=py;
			  }					  
			}
		}
		
	
		draw(g, fx, fy, ex, ey, gx, gy, hx, hy);
			
		Vector v=getOutputs();
		String text="";
		for (int i=0;i<v.size();i++){
			Double d=(Double)v.elementAt(i);
			  if (d.doubleValue()>0.5){
			  		  text=text+"1";
			  } else
			  text=text+"0";
		}
		
		g.drawString(text,(int)getX(),(int)getY()+100);
		
		v=getInputs();
		text="";
		for (int i=0;i<v.size();i++){
			Double d=(Double)v.elementAt(i);
			  if (d.doubleValue()>0.5){
					  text=text+"1";
			  } else
			  text=text+"0";
		}
		g.drawString(text,(int)getX(),(int)getY()+130);
		String s;
		for (int i=0;i<display.size();i++){
			s=(String)display.elementAt(i);
			g.drawString(s,(int)getX()-50,(int)getY()+30+(i*13));
		}
		
	}

	private void draw(
		Graphics g,
		double fx,
		double fy,
		double ex,
		double ey,
		double gx,
		double gy,
		double hx,
		double hy) {
		g.setColor(getCor());
		g.drawLine((int)gx,(int)gy,(int)fx,(int)fy);
		g.drawLine((int)fx,(int)fy,(int)ex,(int)ey);
		g.drawLine((int)hx,(int)hy,(int)gx,(int)gy);
		g.drawLine((int)ex,(int)ey,(int)hx,(int)hy);
	}

	
/**
 * @return
 */
public int getMaxComponentX() {
	return maxComponentX;
}

/**
 * @return
 */
public int getMaxComponentY() {
	return maxComponentY;
}


/**
 * @return
 */
public int getTotComp() {
	return totComp;
}




/**
 * @return
 */
public boolean isHasEnergy() {
	return hasEnergy;
}

public void setGlobalPerf(double d) {
	globalPerf=d;
}
public void addGlobalPerf(double d) {
	globalPerf+=d;
}

/**
 * @return
 */
public double getGlobalPerf() {
	return globalPerf;
}

}
