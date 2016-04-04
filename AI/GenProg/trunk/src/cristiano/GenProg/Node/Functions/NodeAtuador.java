/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node.Functions;

import cristiano.GenProg.GeneticProgramming;
import cristiano.GenProg.Node.Node;


/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NodeAtuador extends Node {
String functionName="";
GeneticProgramming gp;
	/**
	 * 
	 */
	public NodeAtuador(GeneticProgramming gp,String function) {
		super();
		functionName=function;
		this.gp=gp;
		setCodigo(function);
	
	}
	public double getValue(){
		
		
		double x=0;
		setParamNodes();
		for (int i=0;i<getNodes().size();i++){
			x=x+((Node)getNodes().elementAt(i)).getValue();
			//debug("i:"+i+" x:"+x);
		}
		return gp.getObj().setFunction(functionName,x);
		
	}
	public Node copyNode(){
		Node n=copy2Node(new NodeAtuador(gp,functionName));

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
