package cristiano.GenProg.set;

import cristiano.GenProg.Node.Node;
import cristiano.GenProg.Node.Logic.NodeAnd;
import cristiano.GenProg.Node.Logic.NodeEqual;
import cristiano.GenProg.Node.Logic.NodeIf;
import cristiano.GenProg.Node.Logic.NodeNand;
import cristiano.GenProg.Node.Logic.NodeNor;
import cristiano.GenProg.Node.Logic.NodeOr;
import cristiano.GenProg.Node.Logic.NodeXrl;

public class LogicSet extends AbstractSet {

	public LogicSet(){
		addFuncao("equal");//ok
		addFuncao("xrl");//ok
		addFuncao("and");//ok
		addFuncao("nor");//ok
		addFuncao("or");//ok
		addFuncao("and");//ok
		addFuncao("nand");//ok
		addFuncao("if");//ok
	}
	
	public Node translateNode(String s){
		if (s.equals("xrl")){
			return new NodeXrl();
		}
		if (s.equals("equal")){
			return new NodeEqual();
		}	
		if (s.equals("if")){
			return new NodeIf();
		}	
		if (s.equals("and")){
			return new NodeAnd();
		}	
		if (s.equals("nand")){
			return new NodeNand();
		}			
		if (s.equals("nor")){
			return new NodeNor();
		}			
		if (s.equals("or")){
			return new NodeOr();
		}			
    	return super.translateNode(s);
    }
	
	
}
