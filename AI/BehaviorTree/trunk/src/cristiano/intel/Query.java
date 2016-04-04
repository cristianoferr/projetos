package cristiano.intel;

import cristiano.behaviortree.execution.Executor;

public class Query {

private String name;
private String fact;

public Query(String name,String fact){
	this.name=name;
	this.setFact(fact);
}

	
public double isOn(Executor exec){
	if (exec.getFm().getFact(getFact())==null) return 0;
	return exec.getFm().getFact(getFact()).getResult();
}

/**
 * @return the name
 */
public String getName() {
	return name;
}


public void setFact(String fact) {
	this.fact = fact;
}


public String getFact() {
	return fact;
}

}
