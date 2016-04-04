import java.util.Vector;

import RedeNeural.Config;
import RedeNeural.InputDado;
import RedeNeural.NetBP;

/*
 * Created on 24/11/2004
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
public class ItemLearningMatrix extends Objeto {
private double action=0;
private double performance=0;
private double actionPerf=0;
private Vector inputs=null;
private int added=0;
private int x,y=0;
private static int totItems=0;


public ItemLearningMatrix(){
}
public ItemLearningMatrix(ItemLearningMatrix item){
	this.action=item.action;
	this.performance=item.performance;
	this.inputs=new Vector(item.inputs);
}

/**
 * @return
 */
public double getAction() {
	return action;
}
public double getAction(NetBP net) {
	net.input.inputs.clear();
	net.input.inputs.add(getNetData(net));
	net.input.reset();
	net.config.Training=false;
	net.run();
	net.config.Training=true;
	String bin="";
	//net.print();
	for (int i=0;i<net.config.outputSize;i++){
		Double des=(Double)net.desired.elementAt(i);
	//	System.out.println("Output("+i+")"+Config.RoundTo(net.outputs.getNeuron(i).getOutput())+" Des:"+Config.RoundTo(des.doubleValue()));
		bin=bin+Math.round(net.outputs.getNeuron(i).getOutput());
	}
	//System.out.println("Action:"+bin);
	return Config.bin2int(bin);
}
/**
 * @return
 */
public Vector getInputs() {
	return inputs;
}
public InputDado getNetData(NetBP net) {
	
	Vector v=new Vector(inputs);
	Vector v2=Config.int2bin((int)action,net.config.outputSize);
	for (int i=0;i<v2.size();i++){
		v.add(v2.elementAt(i));
	}
	//v.add(new Integer(id));
	InputDado d=new InputDado();
	d.set(v);
	d.setPerf(getActionPerf());
	return d;
}

/**
 * @return
 */
public double getPerformance() {
	return performance;
}

/**
 * @param d
 */
public void setAction(double d) {
	action = d;
}

/**
 * @param vector
 */
public void setInputs(Vector vector) {
	inputs = new Vector(vector);
}

/**
 * @param d
 */
public void setPerformance(double d) {
	performance = d;
}

/**
 * @return
 */
public int getAdded() {
	return added;
}

/**
 * @param i
 */
public void setAdded(int i) {
	added = i;
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
 * @param d
 */
public void setX(int d) {
	x = d;
}

/**
 * @param d
 */
public void setY(int d) {
	y = d;
}

/**
 * @return
 */
public double getActionPerf() {
	return actionPerf;
}

/**
 * @param d
 */
public void setActionPerf(double d) {
	actionPerf = d;
}

}
