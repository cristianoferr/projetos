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
public class NodeFloor extends Node {

	/**
	 * 
	 */
	public NodeFloor() {
		super();
		setComma("+");
		setCodigo("floor");
	}
	public Node copyNode(){
		return copy2Node(new NodeCeil());
	}	

	public double getValue(){
		double x=0;
		setParamNodes();
		for (int i=0;i<getNodes().size();i++){
			x=x+getNode(i).getValue();
		}
		return Math.floor(x);
	}
}
