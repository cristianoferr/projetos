/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node.SimpleMath;

import comum.Objeto;
import cristiano.GenProg.Node.Node;



/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodeRound extends Node {

	/**
	 * 
	 */
	public NodeRound() {
		super();
		// TODO Auto-generated constructor stub
		setCodigo("round");
	}
	
	public Node copyNode(){
		return copy2Node(new NodeRound());
	}
	
	public double getValue(){
		setParamNodes();
		double x=0;
		for (int i=0;i<getNodes().size();i++){
			x+=getNode(i).getValue();
			//debug("i:"+i+" x:"+x);
		}
		return Math.round(x);
	}
}
