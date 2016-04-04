package cristiano.behaviortree;

import java.util.ArrayList;

import cristiano.behaviortree.execution.Pointer;


public class Node {
	
	/*There are three well-defined termination codes that are useful for AI engines.

	Completed successfully
	The behavior accomplished its purpose and terminated without any problems. The actor’s state was modified as intended.
	Failed cleanly
	The behavior did not accomplish its purpose, but still terminated cleanly. All problems were anticipated by the programmer, and none of the starting assumptions were broken. This typically means that there were no side effects on the actor’s state.
	Unexpected error
	The behavior could not accomplish its purpose, and while trying to do so, it broke the starting assumptions. There were side effects on the actor’s state, and this should be taken into account by higher-level logic.
	
	http://aigamedev.com/hierarchical-logic/termination-status/
	*/
	public	enum Status    {
	    NOT_STARTED ,
	    RUNNING,
	    FINISHED_OK,
	    CLEAN_ERROR,
	    CRITICAL_ERROR}

private ArrayList<Node> filhos=new ArrayList<Node>();
ArrayList<AssertionNode> assertions=new ArrayList<AssertionNode>();

private int pos=0;
private String name;
private Status status=Status.NOT_STARTED;

public Node(String name){
	this.name=name;
	
	reset();
}

public void addFilho(Node filho){
	if (filho instanceof AssertionNode){
		addAssertion((AssertionNode)filho);
		return;
	}
	getFilhos().add(filho);
}

public void reset(){
	setPos(0);
	setStatus(Status.NOT_STARTED);
	for (int i=0;i<filhos.size();i++){
		filhos.get(i).reset();
	}
}

public void addAssertion(AssertionNode node){
	assertions.add(node);
}

public boolean checkAssertions(Pointer pointer){
	boolean ret=true;
	
	for (AssertionNode assertion: assertions){
		if (assertion.checkAssertion(pointer)==0) ret=false;
		//System.out.println(name+" Assertion="+assertion.checkAssertion(pointer));
		setStatus(Status.CLEAN_ERROR);
	}
	
	return ret;
}


public void turn(Pointer pointer){
	//System.out.println("turn.node:"+name+" pos="+pos+"  STATUS:"+getStatus());
	if (getStatus()==Status.NOT_STARTED) setStatus(Status.RUNNING);
	
	if (!checkAssertions(pointer)){
		pointer.moveUp();
		return;
	}
	
	if (getPos()-1>=0){
		if (!okToContinue(pointer)) {
			//System.out.println("Not Ok to continue "+name+" status="+getStatus());
			return;
		}
	}
	if (getPos()==getFilhos().size()){
		executeAction(pointer);
		endLoop(pointer);
		
		return;
	}
	
	childrenIterator(pointer);
}

public double checkQuery(Pointer pointer,String query){
	return pointer.getExec().isQueryOn(query);
}

public void childrenIterator(Pointer pointer){
	Node n=getFilhos().get(getPos());
	pointer.addToStack(n);
	setPos(getPos() + 1);
}


//What to do with previous child's result?
//Se der erro no filho, retorna com erro
public boolean okToContinue(Pointer pointer){
	Node n=getFilhos().get(getPos()-1);
	if (n.getStatus()==Status.CLEAN_ERROR){
		setStatus(Status.CLEAN_ERROR);
		pointer.moveUp();
	}
	return (n.getStatus()!=Status.CLEAN_ERROR);
}

public void endLoop(Pointer pointer){
	setStatus(Status.FINISHED_OK);
	pointer.moveUp();
}

public void executeAction(Pointer pointer){
}

public void setPos(int pos) {
	this.pos = pos;
}

public int getPos() {
	return pos;
}

public void setFilhos(ArrayList<Node> filhos) {
	this.filhos = filhos;
}

public ArrayList<Node> getFilhos() {
	return filhos;
}

public String getName() {
	
	return name;
}

public void setStatus(Status status) {
	this.status = status;
}

public Status getStatus() {
	return status;
}

}
