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
public class NodeAnd extends Node {

	/**
	 * 
	 */
	public NodeAnd() {
		super();
		setComma(",");
		setMinNodes(2);
		setCodigo("and");
	}
	
	public Node copyNode(){
		return copy2Node(new NodeAnd());
	}
	
	public double getValue(){
		setParamNodes();
		double x=((Node)getNodes().elementAt(0)).getValue();
		double r=1;
		
		for (int i=0;i<getNodes().size();i++){
			if (((Node)getNodes().elementAt(i)).getValue()!=x) r=0;
			//debug("i:"+i+" x:"+x);
		}
		return r;
	}
}
