/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node;




/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodeVariavel extends Node {
	DadoNode var=null;
int pos=0;
	/**
	 * 
	 */
	public NodeVariavel( DadoNode var,int pos) {
		super();
		this.var=var;
		setCodigo("var"+pos);
		this.pos=pos;
		
	}
	public Node copyNode(){
		Node n=copy2Node(new NodeVariavel(var,pos));
		return n;
	}	
	public double getValue(){
		return var.getDouble();
	}

	public void setValue(double d){
		var.setDouble(d);
	}

}
