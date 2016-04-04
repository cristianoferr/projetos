/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node.SimpleMath;

import cristiano.GenProg.Node.Node;



/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodePow extends Node {

	/**
	 * 
	 */
	public NodePow() {
		super();
		setComma("+");
		setCodigo("floor");
		setMinNodes(2);
	}
	public Node copyNode(){
		return copy2Node(new NodeCeil());
	}	

	public double getValue(){
		double x=0;
		setParamNodes();
		double n=getNodes().elementAt(0).getValue();
		for (int i=1;i<getNodes().size()-1;i++){
			x=x+getNodes().elementAt(i).getValue();
			//debug("i:"+i+" x:"+x);
		}
		
		return Math.pow(n,x);
	}
}
