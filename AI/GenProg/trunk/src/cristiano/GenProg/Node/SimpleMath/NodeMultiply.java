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
public class NodeMultiply extends Node {

	/**
	 * 
	 */
	public NodeMultiply() {
		super();
		setComma("*");
		setMinNodes(2);
		setCodigo("mult");
	}
	public Node copyNode(){
		return copy2Node(new NodeMultiply());
	}			
	public double getValue(){
		setParamNodes();
		double x=1;
		for (int i=0;i<getNodes().size();i++){
			x=x*getNodes().elementAt(i).getValue();
		}
		
		return x;
	}
}
