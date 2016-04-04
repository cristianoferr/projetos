package cristiano.intel;

import java.util.Vector;

import cristiano.behaviortree.execution.Executor;

/**
 * A simple class to query facts.
 * 
 * */

class QueryData{
	String fact;
	QueryType queryType;
	double value;
	public QueryData(String fact,QueryType qt,double value){
		this.fact=fact;
		this.queryType=qt;
		this.value=value;
	}
}

public class QueryComplex extends Query {
	Vector<QueryData> queries=new Vector<QueryData>();
	public QueryComplex(String name) {
		super(name, null);
		
	}
	public void addQuery(String fact,QueryType qt,double value){
		queries.add(new QueryData(fact,qt,value));
	}
	

	public double isOn(Executor exec){
		double res=1;
		for (int i=0;i<queries.size();i++){
			QueryData qd=queries.get(i);
			double r=exec.getFm().getFact(qd.fact).getResult();
			r=((QueryType.checkQuery(qd.queryType, r, qd.value))?1:0);
			if (r==0) res=0;
		}
		return res;
	}
	


}
