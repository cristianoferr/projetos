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
public class NodeXrl extends Node {

	/**
	 * 
	 */
	public NodeXrl() {
		super();
		setComma(",");
		setMinNodes(2);
		setCodigo("xrl");
	}
	public Node copyNode(){
		return copy2Node(new NodeXrl());
	}	
	public double getValue(){
		setParamNodes();
		double x=((Node)getNodes().elementAt(0)).getValue();
		
		for (int i=1;i<getNodes().size();i++){
			x=(int)x ^ (int)((Node)getNodes().elementAt(i)).getValue();
			//debug("i:"+i+" x:"+x);
		}
			return x;
	}
}
