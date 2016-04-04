package com.cristiano.cyclone.forceGenerator.actuators;

import java.util.ArrayList;
import java.util.Vector;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.StringUtils;
import com.cristiano.cyclone.utils.Vector3;


public class ActuatorRule {
private Vector<String> rules=new Vector<String>();

public ActuatorRule(){
}

public void addRule(String s){
	String s1=s;
	s1=s.replace("<>", "#");
	s1=s.replace("!=", "#");
	rules.add(s1);
}

public void addRuleGroup(String g){
	addRule("group="+g);
}


public void addRuleOrient(String g){
	addRule("orient="+g);
}
public void clear(){
	rules.clear();
}

public void applyRules(ActuatorGroup sg,Vector<Actuator> actuators){
	String operators="><#=";
	
	for (int i=0;i<actuators.size();i++){
		Actuator slot=actuators.get(i); 
		boolean ruleApplies=true;
		
		for (int j=0;j<rules.size();j++){
			if (!ruleApplies) {
				break;
			}
			String rule=rules.get(j).toLowerCase();
			//String[] opers=rule.split(operators,-1);
			boolean isString=false;
			ArrayList<String> opers=StringUtils.tokenize(operators,rule,true);
			if (opers.get(0).equals("orient")) {
				isString=true;
				ruleApplies = checkOrient(slot, ruleApplies, opers);
				
				
			}
			if (opers.get(0).equals("group")) {
				isString=true;
				if (opers.get(1).equals("=")){
					if (!slot.getType().toLowerCase().equals(opers.get(2))) {
						ruleApplies=false;
					}
				}else
				if (opers.get(1).equals("#")){
					if (slot.getType().equals(opers.get(2))) {ruleApplies=false;}
				}
			} 
			if (!isString){
				
				ruleApplies = checkCoord(slot, ruleApplies, opers);
			}
		}
		if (ruleApplies){
			sg.addSlot(slot);
			sg.setType(slot.getType());
		}
	}	
}

private boolean checkOrient(Actuator slot, boolean ruleApplies,
		ArrayList<String> opers) {
	opers.set(2,opers.get(2).toUpperCase());
	Vector3 vec=PhysicsConsts.orientations.get(opers.get(2));
	if (opers.get(1).equals("=")){
	    if (!vec.equals(slot.getOrientation())) {
	    	ruleApplies=false;
	    }
	}
	if (opers.get(1).equals("#")){
	    if (vec.equals(slot.getOrientation())) {
	    	ruleApplies=false;
	    }
	}
	return ruleApplies;
}

private boolean checkCoord(Actuator slot, boolean ruleApplies,
		ArrayList<String> opers) {
	double v=0;
	double o=Double.parseDouble(opers.get(2));
	if (opers.get(0).equals("x")) {v=slot.getPosition().x;}else
	if (opers.get(0).equals("y")) {v=slot.getPosition().y;}else
	if (opers.get(0).equals("z")) {v=slot.getPosition().z;}
	
	if (opers.get(1).equals("=")){
		if (!(v==o)){ruleApplies=false;}
	} else
	if (opers.get(1).equals(">")){
		if (!(v>o)){ruleApplies=false;}
	}else
	if (opers.get(1).equals("<")){
		if (!(v<o)){ruleApplies=false;}
	}else
	if (opers.get(1).equals("#")){
		if (!(v!=o)){ruleApplies=false;}
	}
	return ruleApplies;
}
}
