package cristiano.GenProg.set;

import cristiano.GenProg.Node.Node;
import cristiano.GenProg.Node.Functions.NodeTerminal;
import cristiano.GenProg.Node.SimpleMath.NodeAbs;
import cristiano.GenProg.Node.SimpleMath.NodeAddiction;
import cristiano.GenProg.Node.SimpleMath.NodeAverage;
import cristiano.GenProg.Node.SimpleMath.NodeCeil;
import cristiano.GenProg.Node.SimpleMath.NodeDivision;
import cristiano.GenProg.Node.SimpleMath.NodeFloor;
import cristiano.GenProg.Node.SimpleMath.NodeMax;
import cristiano.GenProg.Node.SimpleMath.NodeMin;
import cristiano.GenProg.Node.SimpleMath.NodeMultiply;
import cristiano.GenProg.Node.SimpleMath.NodePow;
import cristiano.GenProg.Node.SimpleMath.NodeRound;
import cristiano.GenProg.Node.SimpleMath.NodeSqr;
import cristiano.GenProg.Node.SimpleMath.NodeSqrt;
import cristiano.GenProg.Node.SimpleMath.NodeSubtract;
import cristiano.GenProg.Node.Trig.NodeLog;
import cristiano.GenProg.Node.Trig.NodeLog10;

public class MathSet extends AbstractSet {

	public MathSet(){
		addFuncao("sum");//ok
		addFuncao("sub");//ok
		addFuncao("div");//ok
		addFuncao("mult");//ok
		addFuncao("avg");//ok
		addFuncao("max");//ok
		addFuncao("abs");//ok
		addFuncao("round");//ok
		addFuncao("min");//ok
		addFuncao("ceil");//ok
		addFuncao("floor");//ok
		addFuncao("pow");//ok	
		addFuncao("log");//ok
		addFuncao("log10");//ok
		addFuncao("sqr");//ok
		addFuncao("sqrt");//ok
	    
		addTerminal("pi");
    	addTerminal("e");
    	
    	
    	for (int i=-30;i<=30;i++){
    		addTerminal(Integer.toString(i));
    	}
    	for (int i=40;i<=1000;i+=10){
    		addTerminal(Integer.toString(i));
    	}
	}
	
	public Node translateNode(String s){
		
		if ((isNumber(s))|| (s.equals("rand"))|| (s.equals("pi"))|| (s.equals("e")))
		{
		return new NodeTerminal(s);
	}

		
		if (s.equals("ceil")){
			return new NodeCeil();
		}
		if (s.equals("floor")){
			return new NodeFloor();
		}
		if (s.equals("pow")){
			return new NodePow();
		}
		if (s.equals("sum")){
			return new NodeAddiction();
		}
		if (s.equals("log10")){
			return new NodeLog10();
		}
		if (s.equals("min")){
			return new NodeMin();
		}
		if (s.equals("log")){
			return new NodeLog();
		}
		if (s.equals("sub")){
			return new NodeSubtract();
		}
		if (s.equals("mult")){
			return new NodeMultiply();
		}
		if (s.equals("div")){
			return new NodeDivision();
		}
		if (s.equals("avg")){
			return new NodeAverage();
		}	
		if (s.equals("round")){
			return new NodeRound();
		}	
		if (s.equals("abs")){
			return new NodeAbs();
		}	
		if (s.equals("max")){
			return new NodeMax();
		}
		if (s.equals("sqr")){
			return new NodeSqr();
		}
		if (s.equals("sqrt")){
			return new NodeSqrt();
		}	
    	return super.translateNode(s);
    }
	
	
}
