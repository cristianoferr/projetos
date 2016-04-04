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
public class NodeMax extends Node {

	/**
	 * 
	 */
	public NodeMax() {
		super();
		setComma(",");
		setMinNodes(2);
		setCodigo("max");
	}
	public Node copyNode(){
		return copy2Node(new NodeMax());
	}	
	public double getValue(){
		double x=0;
		double m=0;
		setParamNodes();
		for (int i=0;i<getNodes().size();i++){
			x=((Node)getNodes().elementAt(i)).getValue();
			if ((i==0) ||(x>m)) m=x;
			//debug("i:"+i+" x:"+x);
		}

		return m;
	}
}
