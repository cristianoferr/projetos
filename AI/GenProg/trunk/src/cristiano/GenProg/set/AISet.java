package cristiano.GenProg.set;

import cristiano.GenProg.Node.Node;
import cristiano.GenProg.Node.AI.NodeNeuron;

public class AISet extends AbstractSet {

	public AISet(){
		addFuncao("neuron");

	}
	
	public Node translateNode(String s){
		if (s.equals("neuron")){
			return new NodeNeuron();
		}
    	return super.translateNode(s);
    }
	
}
