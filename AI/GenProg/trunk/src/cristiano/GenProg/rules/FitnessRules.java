package cristiano.GenProg.rules;

import java.util.Vector;

import cristiano.GenProg.Node.Node;

/*
 * Essa classe verifica o node contra uma série de regras...
 */
public class FitnessRules {
	public final static String CHECK_CONTAINS_CODE="CONTAINS_CODE";
	public final static String CHECK_NOT_CONTAINS_CODE="NOTCONTAINS_CODE";
	public final static String CHECK_SIZE="CHECK_SIZE";
	
	private Vector<FitnessRule> fitnessRules;
	public FitnessRules(){
		fitnessRules=new Vector<FitnessRule>();
	}
	
	public void addFitnessRule(FitnessRule f){
		fitnessRules.add(f);
	}
	
	public void addFitnessRule(String rule,String par,double penalty){
		fitnessRules.add(new FitnessRule(rule,par,penalty));
	}
	
	public void addFitnessRule(String rule,int par,double penalty){
		fitnessRules.add(new FitnessRule(rule,par,penalty));
	}
	
	public void checkRules(Node node){
		for (int i=0;i<fitnessRules.size();i++){
			fitnessRules.get(i).checkRule(node);
		}
	}
}
