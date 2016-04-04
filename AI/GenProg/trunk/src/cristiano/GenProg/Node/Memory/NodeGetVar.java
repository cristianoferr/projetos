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
public class NodeGetVar extends Node {

	/**
	 * 
	 */
	public NodeGetVar() {
		super();
		setCodigo("getVar");
		
	}
	
	public Node copyNode(){
		return copy2Node(new NodeGetVar());
	}
	
		
	public double getValue(){
		int x=0;
		setParamNodes();
		for (int i=0;i<getNodes().size();i++){
			x=x+(int)((Node)getNodes().elementAt(i)).getValue();
			//debug("i:"+i+" x:"+x);
		}
		NodeVariavel var=getGP().getVariavel(x);
		if (var==null) {
			return 0;
		} else{
			return var.getValue();	
		}
		
	}
}
