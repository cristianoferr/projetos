package cristiano.GenProg.set;

import cristiano.GenProg.Node.Node;
import cristiano.GenProg.Node.SimpleMath.NodeSqrt;
import cristiano.GenProg.Node.Trig.NodeATan;
import cristiano.GenProg.Node.Trig.NodeCos;
import cristiano.GenProg.Node.Trig.NodeSin;
import cristiano.GenProg.Node.Trig.NodeTan;
import cristiano.GenProg.Node.Trig.NodeToDegrees;
import cristiano.GenProg.Node.Trig.NodeToRadians;

public class TrigonometrySet extends AbstractSet {

	public TrigonometrySet(){
		addFuncao("sin");//ok
		addFuncao("cos");//ok
		addFuncao("tan");//ok
		addFuncao("atan");//ok

		addFuncao("todegrees");//ok
		addFuncao("toradians");//ok
	}
	
	public Node translateNode(String s){
		if (s.equals("sin")){
			return new NodeSin();
		}	
		if (s.equals("cos")){
			return new NodeCos();
		}
		if (s.equals("todegrees")){
			return new NodeToDegrees();
		}
		if (s.equals("toradians")){
			return new NodeToRadians();
		}
		if (s.equals("tan")){
			return new NodeTan();
		}	
		if (s.equals("atan")){
			return new NodeATan();
		}	
		
		
    	return super.translateNode(s);
    }
}
