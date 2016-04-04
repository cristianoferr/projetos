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
public class NodeDivision extends Node {

	/**
	 * 
	 */
	public NodeDivision() {
		super();
		setComma(",");
		setMinNodes(2);
		setCodigo("div");
	}
	public Node copyNode(){
		Node n=copy2Node(new NodeDivision());
		return n;
	}		
	public double getValue(){
		setParamNodes();
		double x=((Node)getNodes().elementAt(0)).getValue();
		for (int i=1;i<getNodes().size();i++){
			if (getNode(i).getValue()!=0){
			  x=x/getNode(i).getValue();
			}
		}
		return x;
	}
}
