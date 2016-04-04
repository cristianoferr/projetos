package cristiano.behaviortree.execution;

import java.util.Vector;

import cristiano.behaviortree.Node;
import cristiano.intel.FactManager;
import cristiano.intel.Query;
import cristiano.intel.QueryManager;



public class Executor {
	Vector<Pointer> pointers;
	FactManager fm;
	QueryManager qm;

	
	public final static String TYPE_BOOLEAN="BOOLEAN";
	
	
	
	public Executor(){
		pointers=new Vector<Pointer>();
		
	}
	
	public int getPointersSize(){
		return pointers.size();
	}
	public void addPointer(Node node){
		if (node==null)return;
		Pointer pointer=new Pointer(this,node);
		pointers.add(pointer);
	}
	
	
	public void setQuery(String info,Query query){
		qm.addQuery(query);
		
	}
	public Query createQuery(String name,String fact){
		return qm.createQuery(name, fact);
	}
	
	public Query getQuery(String info){
		return qm.getQuery(info);
	}
	
	public double isQueryOn(String info){
		if (getQuery(info)!=null) return getQuery(info).isOn(this);
		return 0;
	}
	
	public void turn(){
		for (int i=pointers.size()-1;i>=0;i--){
			
			Pointer pointer=pointers.get(i);
		//	System.out.println("Executor.turn  i="+i+" "+pointer.getCurrent().getName());
			pointer.turn();
			
			if (!pointer.isRunning()) pointers.remove(pointer);
		}
	}
	
	
	public boolean isRunning(){
		return (pointers.size()>0);
	}

	public FactManager getFm() {
		return fm;
	}

	public void setFm(FactManager fm) {
		this.fm = fm;
	}

	public QueryManager getQm() {
		return qm;
	}

	public void setQm(QueryManager qm) {
		this.qm = qm;
	}

	public double executeAction(String action,double intensity) {
		return 0;
	}




}
