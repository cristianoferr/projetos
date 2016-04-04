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
public class NodeMin extends Node {

	/**
	 * 
	 */
	public NodeMin() {
		super();
		// TODO Auto-generated constructor stub
		setComma(",");
		setMinNodes(2);
		setCodigo("min");
	}
	public Node copyNode(){
		Node n=copy2Node(new NodeMin());
		return n;
	}	
	public double getValue(){
		double x=0;
		double m=0;
		setParamNodes();
		for (int i=0;i<getNodes().size();i++){
			x=((Node)getNodes().elementAt(i)).getValue();
			if ((i==0) || (x<m)) m=x;
			//debug("i:"+i+" x:"+x+" m:"+m);
		}
		return m;
	}
}
