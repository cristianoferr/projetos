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
public class NodeNor extends Node {

	/**
	 * 
	 */
	public NodeNor() {
		super();
		setComma(",");
		setCodigo("nor");
		setMinNodes(2);
	}
	
	public Node copyNode(){
		return copy2Node(new NodeNor());
	}
	
	public double getValue(){
		double x=1;
		setParamNodes();
		for (int i=0;i<getNodes().size();i++){
			if (((Node)getNodes().elementAt(i)).getValue()>0) x=0;
			//debug("i:"+i+" x:"+x);
		}
		return x;
	}
}
