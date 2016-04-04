/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node.Functions;


import comum.Objeto;
import cristiano.GenProg.GeneticProgramming;
import cristiano.GenProg.Node.Node;
import cristiano.GenProg.Node.SimpleMath.NodeSubtract;


/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodeTerminal extends Node {
private String functionName="";
	/**
	 * 
	 */
	public NodeTerminal(String function) {
		super();
		functionName=function;
		setCodigo(function);
	}
	
		
	public double getValue(){
	//	out("gp:"+gp);
	//	out("Function:"+functionName);
	//	out("OBJ:"+gp.getObj());
		if (isNumber(functionName)) return Double.parseDouble(functionName);
		if (functionName.equals("rand")) return Math.random(); 
		if (functionName.equals("pi")) return Math.PI;
		if (functionName.equals("e")) return Math.E;

		return getGP().getObj().getFunction(functionName);
	}
	public Node copyNode(){
		Node n=copy2Node(new NodeTerminal(functionName));
		return n;
	}	
/**
 * @return
 */
public String getFunctionName() {
	return functionName;
}

/**
 * @param string
 */
public void setFunctionName(String string) {
	functionName = string;
}

}
