/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node.Logic;


import comum.Objeto;
import cristiano.GenProg.Node.Node;
import cristiano.GenProg.Node.SimpleMath.NodeAddiction;



/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodeOr extends Node {

	/**
	 * 
	 */
	public NodeOr() {
		super();
		// TODO Auto-generated constructor stub
		setComma(",");
		setMinNodes(2);
		setCodigo("or");
	}
	
	public Node copyNode(){
		Node n=copy2Node(new NodeOr());
		return n;
	}
	
	public double getValue(){
		double x=0;
		setParamNodes();
		for (int i=0;i<getNodes().size();i++){
			if (((Node)getNodes().elementAt(i)).getValue()>0) x=1;
			//debug("i:"+i+" x:"+x);
		}
		return x;
	}
}
