/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node.Logic;

import cristiano.GenProg.Node.Node;



/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodeEqual extends Node {

	/**
	 * 
	 */
	public NodeEqual() {
		super();
		setMinNodes(2);
		setComma(",");
		setCodigo("equal");
	}
	
	public Node copyNode(){
		return copy2Node(new NodeEqual());
	}
	
	public double getValue(){
		setParamNodes();
		double x=((Node)getNodes().elementAt(0)).getValue();
		
		for (int i=1;i<getNodes().size();i++){
			if (((Node)getNodes().elementAt(i)).getValue()!=x) x=0;

		}
		return x;
	}
}
