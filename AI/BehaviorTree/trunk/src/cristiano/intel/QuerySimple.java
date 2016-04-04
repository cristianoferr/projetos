package cristiano.intel;

import cristiano.behaviortree.execution.Executor;

/**
 * A simple class to query facts.
 * 
 * */
public class QuerySimple extends Query {
	QueryType queryType;
	double value;
	public QuerySimple(String name, String fact,QueryType queryType,double value) {
		super(name, fact);
		this.queryType=queryType;
		this.value=value;
	}

	public double isOn(Executor exec){
		if (exec.getFm().getFact(getFact())==null) return 0;
		double res=exec.getFm().getFact(getFact()).getResult();
//		System.out.println(getName()+" QuerySimple.isON: ret("+queryType+","+res+","+value+")="+((QueryType.checkQuery(queryType, res, value))?1:0));
		return ((QueryType.checkQuery(queryType, res, value))?1:0);
	}
	


}
