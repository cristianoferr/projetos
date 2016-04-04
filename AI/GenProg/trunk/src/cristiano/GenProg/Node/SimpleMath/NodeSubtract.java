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
public class NodeSubtract extends Node {

	/**
	 * 
	 */
	public NodeSubtract() {
		super();
		setComma("-");
		setMinNodes(2);
		setCodigo("sub");
	}
	public Node copyNode(){
		return copy2Node(new NodeSubtract());
	}				
	public double getValue(){
		setParamNodes();
		double x=((Node)getNodes().elementAt(0)).getValue();
		for (int i=1;i<getNodes().size();i++){
			x-=getNode(i).getValue();
		}
		return x;
	}
}
