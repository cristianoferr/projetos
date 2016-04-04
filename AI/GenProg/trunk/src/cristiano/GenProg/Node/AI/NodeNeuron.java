/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node.AI;


import java.util.Vector;

import cristiano.GenProg.Node.Node;



/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodeNeuron extends Node {
Vector weights=new Vector();
double p=Math.random()-Math.random();
	/**
	 * 
	 */
	public NodeNeuron() {
		super();
		// TODO Auto-generated constructor stub
		setComma(",");
		setCodigo("neuron");
	}
	
	public Node copyNode(){
		initWeights();
		
		NodeNeuron n=(NodeNeuron)copy2Node(new NodeNeuron());
		n.setP(p);
		
		for (int i=0;i<getWeights().size();i++){
			n.getWeights().add(getWeights().elementAt(i));
		}
		
		return n;
	}
	public void mutateNode(){
		super.mutateNode();
		setParamNodes();
		double dif=getP()*(Math.random()-Math.random())*getGP().GA_MUTATIONRATE;
	//	out("oldP:"+getP()+" dif: "+dif+" newP:"+getP()+dif);
		setP(getP()+dif);
	//	out("newP:"+getP());
		for (int i=0;i<weights.size();i++){
			double w=((Double)weights.elementAt(i)).doubleValue();
			dif=(Math.random()-Math.random())*getGP().GA_MUTATIONRATE;
			w=w+dif;
			weights.setElementAt(new Double(w),i);
		}
		
	}	
	public double getValue(){
		initWeights();
		
		double x=0;
		for (int i=0;i<getNodes().size();i++){
			double w=((Double)weights.elementAt(i)).doubleValue();
			x=x+((Node)getNodes().elementAt(i)).getValue()*w; 
			//debug("i:"+i+" x:"+x);
		}
		return x+p;
	}

	private void initWeights() {
		if (weights.size()==0){
			weights=new Vector();
			for (int i=0;i<getNodes().size();i++){
				weights.add(new Double(Math.random()-Math.random()));
			}
		}
	}

	public double getP() {
		return p;
	}

	public void setP(double r) {
		this.p = r;
	}

	public Vector getWeights() {
		return weights;
	}

	public void setWeights(Vector weights) {
		this.weights = weights;
	}
}
