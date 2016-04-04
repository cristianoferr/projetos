import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

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
public class Component extends Objeto {
private int angle=0;
private int x,y,z=0;
double realX,realY=0;
public boolean power=true;
public int totInputs=0;
public int totOutputs=0;
public int id=0;
public String text="";
public Vector inputs=new Vector();
public Vector outputs=new Vector();
public Color cor=Color.white;
public RobotBasico owner;
int powerConsumption=10;
public boolean usingEnergy=true;


public final static int sizeComponent=5;
	/**
	 * 
	 */
	public Component(RobotBasico o,int px,int py) {
		super();
		x=px;
		y=py;
		this.owner=o;

	}
	public double batteryRecharge(double d){
		return d;
	}
	
	public Vector getInputs(Vector vet){
		for (int i=0;i<inputs.size();i++){
			vet.add(inputs.elementAt(i));
		}
		return vet;
	}
	public Vector getOutputs(Vector vet){
		for (int i=0;i<outputs.size();i++){
			vet.add(outputs.elementAt(i));
		}
		return vet;
	}
	public int setInput(Vector vet,int p){
		for (int i=0;i<totInputs;i++){
			//p=compsX[i].setOutput(vet,p);
			inputs.setElementAt(vet.elementAt(p),i);
			p++;
		}
		return p;
	}

	public void desenha(Graphics g){
		g.setColor(cor);
		//g.fillOval((int)getRealX(),(int)getRealY(),1,1);
		//g.drawString(Integer.toString(id)+" "+text,(int)getRealX(),(int)getRealY());
	}
	public void turn(Graphics g){
		
	}
	public double getRealAngle() {
		return angle+owner.getAngle();
	}
/**
 * @return
 */
public int getAngle() {
	return angle;
}

/**
 * @return
 */
public int getX() {
	return x;
}

/**
 * @return
 */
public int getY() {
	return y;
}

/**
 * @param i
 */
public void setAngle(int i) {
	angle = i;
}

/**
 * @return
 */
public Color getCor() {
	return cor;
}

/**
 * @return
 */
public double getRealX() {
	return realX;
}

/**
 * @return
 */
public double getRealY() {
	return realY;
}

/**
 * @param i
 */
public void setRealX(double i) {
	realX = i;
}

/**
 * @param i
 */
public void setRealY(double i) {
	realY = i;
}

/**
 * @return
 */
public int getZ() {
	return z;
}

/**
 * @return
 */
public int getPowerConsumption() {
	return powerConsumption;
}
public int batteryComsumption(int e) {
	return e;
}

/**
 * @return
 */
public boolean isUsingEnergy() {
	return usingEnergy;
}


/**
 * @return
 */
public int getTotInputs() {
	return totInputs;
}

/**
 * @return
 */
public int getTotOutputs() {
	return totOutputs;
}

/**
 * @param i
 */
public void setTotInputs(int i) {
	totInputs = i;
}

/**
 * @param i
 */
public void setTotOutputs(int i) {
	totOutputs = i;
}

/**
 * @return
 */

/**
 * @param d
 */
public void addGlobalPerf(double d) {
	owner.addGlobalPerf(d);
}

}
