/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node.Trig;

import cristiano.GenProg.Node.Node;



/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodeToDegrees extends Node {

	/**
	 * 
	 */
	public NodeToDegrees() {
		super();
		setComma("+");
		setCodigo("todegrees");
		setMinNodes(2);
	}
	public Node copyNode(){
		return copy2Node(new NodeToDegrees());
	}	

	public double getValue(){
		double x=0;
		setParamNodes();
		for (int i=0;i<getNodes().size();i++){
			x=x+((Node)getNodes().elementAt(i)).getValue();
			//debug("i:"+i+" x:"+x);

		}
		
		return Math.toDegrees(x);
	}
}
