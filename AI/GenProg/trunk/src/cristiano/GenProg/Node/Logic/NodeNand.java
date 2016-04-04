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
public class NodeNand extends Node {

	/**
	 * 
	 */
	public NodeNand() {
		super();
		// TODO Auto-generated constructor stub
		setComma(",");
		setCodigo("nand");
		setMinNodes(2);
	}
	
	public Node copyNode(){
		return copy2Node(new NodeNand());
	}
	
	public double getValue(){
		setParamNodes();
		double x=((Node)getNodes().elementAt(0)).getValue();
		double r=1;
		
		for (int i=0;i<getNodes().size();i++){
			if (((Node)getNodes().elementAt(i)).getValue()!=x) r=0;
			//debug("i:"+i+" x:"+x);
		}
		if (r==0) {r=1;} else {r=0;}
		return r;
	}
}
