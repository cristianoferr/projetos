package cristiano.behaviortree;

import cristiano.behaviortree.Node.Status;
import cristiano.behaviortree.execution.Pointer;

public class ParalelNode extends Node {

	public ParalelNode(String name) {
		super(name);
	}

	
	public void reset(){
		super.reset();

		
	}
	
	public void turn(Pointer pointer){
		if (getStatus()==Status.NOT_STARTED) {
			setStatus(Status.RUNNING);
			for (int i=0;i<getFilhos().size();i++){
				pointer.getExec().addPointer(getFilhos().get(i));
			}
		}
		
		boolean finished=true;
		
		for (int i=0;i<getFilhos().size();i++){
			Status stat=getFilhos().get(i).getStatus();
			if ((stat!=Status.CLEAN_ERROR) && (stat!=Status.FINISHED_OK))finished=false;
			if (stat==Status.CLEAN_ERROR) setStatus(Status.CLEAN_ERROR);
		}
		if (finished){
			setStatus(Status.FINISHED_OK);
			pointer.moveUp();
		}
	
		//super.turn(pointer);
	}
}
