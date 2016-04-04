package cristiano.behaviortree.decorator;

import cristiano.behaviortree.execution.Pointer;

public class FilterQuery extends DecoratorAbstract {
String query;
	public FilterQuery(String name,String query) {
		super(name);
		this.query=query;
	}
	
	
	public void turn(Pointer pointer){
		if (checkQuery(pointer,query)==0){
			setStatus(Status.CLEAN_ERROR);
			pointer.moveUp();
			return;
		}
		super.turn(pointer);
	}
	
	
	
}
