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
 * Essa classe verifica o primeiro argumento e se for >0 então executa o 2o argumento 
 * senão executa o 3o argumento, se existir.
 */
public class NodeIf extends Node {

	/**
	 * 
	 */
	public NodeIf() {
		super();
		setComma(",");
		setMinNodes(2);
		setMaxNodes(3);
		setCodigo("if");
	}
	
	public Node copyNode(){
		return copy2Node(new NodeAnd());
	}
	public double getIF(){
		return ((Node)getNodes().elementAt(0)).getValue();
	}
	
	public double getThen(){
		return ((Node)getNodes().elementAt(1)).getValue();
	}
	
	public double getElse(){
		if (sizeNode()>2){
			return ((Node)getNodes().elementAt(2)).getValue(); 
		}else {
			return 0;
		}
	}
	
	public double getValue(){
		setParamNodes();

		double r=0;
		if (getIF()>0){
			r=getThen();
		} else {
			r=getElse();
		}
		
		return r;
	}
}
