package cristiano.GenProg.rules;

import cristiano.GenProg.Node.Node;


/*
 * Contém a regra do fitness a ser verificada no Node 
 */
public class FitnessRule {
	private String rule;
	private String par;
	private int iPar;
	private double penalty;
	public FitnessRule(String rule, String par,double penalty){
		this.rule=rule;
		this.par=par;
		this.penalty=penalty;
	}
	public FitnessRule(String rule, int par,double penalty){
		this.rule=rule;
		this.iPar=par;
		this.penalty=penalty;
	}

	public void checkRule(Node node) {
		boolean ret=false;
		if (rule.equals(FitnessRules.CHECK_CONTAINS_CODE)){
			if (node.containsCode(par)) ret=true;
		}
		if (rule.equals(FitnessRules.CHECK_NOT_CONTAINS_CODE)){
			if (!node.containsCode(par)) ret=true;
		}
		if (rule.equals(FitnessRules.CHECK_SIZE)){
			if (node.size()>iPar) ret=true;
		}
		
		if (ret){
			node.setFitness(node.getFitness()+penalty);
		} 
	}
}
