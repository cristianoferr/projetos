/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node.Memory;

import cristiano.GenProg.Node.Node;
import cristiano.GenProg.Node.NodeVariavel;



/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodeSetVar extends Node {

	/**
	 * 
	 */
	public NodeSetVar() {
		super();
		// Codigo("setVar");
		
	}
	
	public Node copyNode(){
		return copy2Node(new NodeSetVar());
	}
	
		
	public double getValue(){
		double x=0;
		setParamNodes();
		int p=(int)((Node)getNodes().elementAt(0)).getValue();
		for (int i=1;i<getNodes().size();i++){
			x=x+((Node)getNodes().elementAt(i)).getValue();
			//debug("i:"+i+" x:"+x);
		}
		
		NodeVariavel var=getGP().getVariavel(p);
		if (var==null) return 0;
		var.setValue(x);
		return x;
		
	}
}
