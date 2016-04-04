package cristiano.behaviortree.decorator;

import cristiano.behaviortree.Node;
import cristiano.behaviortree.Node.Status;
import cristiano.behaviortree.execution.Pointer;

public class LoopNode extends DecoratorAbstract {
int counter=0;
int loops=-1;
	public LoopNode(String name,int loops) {
		super(name);
		this.loops=loops;
	}
	public LoopNode(String name) {
		super(name);
	}

	public void turn(Pointer pointer){
		if (getStatus()!=Status.RUNNING) {
			counter=0;
			System.out.println(getName()+" INIT COUNTER=0");
		}
		super.turn(pointer);
	}
			
	public boolean okToContinue(Pointer pointer){
		Node n=getFilhos().get(getPos()-1);
		if (n.getStatus()==Status.CRITICAL_ERROR){
			setStatus(Status.CRITICAL_ERROR);
			pointer.moveUp();
		}
		return (n.getStatus()!=Status.CRITICAL_ERROR);
	}	
		
	public void endLoop(Pointer pointer){
		setPos(0);
		counter++;
	//	System.out.println("COUNTER="+counter);
		if (loops==-1)return;
		if (counter>=loops) {
			System.out.println("endloop");
			setStatus(Status.FINISHED_OK);
			pointer.moveUp();
			
		}
	}
	
	public void childrenIterator(Pointer pointer){
		super.childrenIterator(pointer);
		boolean run_ok=false;
		for (int i=0;i<getFilhos().size();i++){
			if (getFilhos().get(i).getStatus()==Status.FINISHED_OK)run_ok=true;
			if (getFilhos().get(i).getStatus()==Status.NOT_STARTED)run_ok=true;
			if (getFilhos().get(i).getStatus()==Status.CLEAN_ERROR)run_ok=true;
		}
		if (!run_ok){
			setStatus(Status.CLEAN_ERROR);
			System.out.println(getName()+"=> Clean Error on Loop!!!!");
			pointer.moveUp();
		}
	
	}
}


