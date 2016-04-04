package cristiano.intel;

import java.util.HashMap;


public class FactManager {
	
HashMap<String,Fact> facts;

public FactManager(){
	facts=new HashMap<String,Fact>();
}

public void addFact(Fact fact){
	if (fact==null)return;
	facts.put(fact.getName(),fact);
}

public Fact createFact(String name,double value){
	Fact fact=null;
//	if (name.equals(fact_SelfPositionX))fact=new FactSelfPositionX(fact_SelfPositionX,0,item); 
//	if (name.equals(fact_SelfPositionY))fact=new FactSelfPositionY(fact_SelfPositionY,0,item);
//	if (name.equals(fact_SelfPositionZ))fact=new FactSelfPositionZ(fact_SelfPositionZ,0,item);
	if (fact==null) fact=new Fact(name,value);
	addFact(fact);
	return fact;
}

public Fact getFact(String str){
	return facts.get(str);
}

}
