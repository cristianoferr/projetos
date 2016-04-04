package cristiano.behaviortree;

import cristiano.behaviortree.execution.Pointer;


public class AssertionNode extends Leaf {
String query;
	public AssertionNode(String name, String query) {
		super(name);
		this.query=query;
		 
	}

	public double checkAssertion(Pointer pointer){
		return pointer.getExec().isQueryOn(query);
	}
}
