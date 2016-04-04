package cristiano.behaviortree;

import cristiano.behaviortree.execution.Pointer;



public class SelectorNode extends Node{

	public SelectorNode(String name) {
		super(name);
		
	}

	//Na 1a vez que um filho acertar, ele retorna
	public boolean okToContinue(Pointer pointer){
		Node n=getFilhos().get(getPos()-1);
		if (n.getStatus()==Status.FINISHED_OK){
			setStatus(Status.FINISHED_OK);
			pointer.moveUp();
		}
		return (getStatus()!=Status.FINISHED_OK);
		
	}

	//If the selector came this far then no node worked.
	public void endLoop(Pointer pointer){
		setStatus(Status.CLEAN_ERROR);
		pointer.moveUp();
	}	
	
}
