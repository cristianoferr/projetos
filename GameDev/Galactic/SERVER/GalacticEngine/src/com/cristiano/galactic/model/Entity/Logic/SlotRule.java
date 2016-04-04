package com.cristiano.galactic.model.Entity.Logic;

import java.util.ArrayList;
import java.util.Vector;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Vector3;


public class SlotRule {
Vector<String> rules=new Vector<String>();

public SlotRule(){
}

public void addRule(String s){
	
	s=s.replace("<>", "#");
	s=s.replace("!=", "#");
	rules.add(s);
}

public void clear(){
	rules.clear();
}

public void applyRules(SlotGrouping sg,Vector<Slot> actuators){
	String operators="><#=";
	double v=0;
	for (int i=0;i<actuators.size();i++){
		Slot slot=actuators.get(i); 
		boolean ruleApplies=true;
		
		for (int j=0;j<rules.size();j++){
			if (!ruleApplies) break;
			String rule=rules.get(j).toLowerCase();
			//String[] opers=rule.split(operators,-1);
			boolean isString=false;
			ArrayList<String> opers=StringUtils.tokenize(operators,rule,true);
			if (opers.get(0).equals("orient")) {
				isString=true;
				opers.set(2,opers.get(2).toUpperCase());
				Vector3 vec=PhysicsConsts.orientations.get(opers.get(2));
				if (opers.get(1).equals("="))
				    if (!vec.equals(slot.getOrientation())) 
				    	ruleApplies=false;
				if (opers.get(1).equals("#"))
				    if (vec.equals(slot.getOrientation())) 
				    	ruleApplies=false;
				
				
			}
			if (opers.get(0).equals("group")) {
				isString=true;
				if (opers.get(1).equals("=")){
					if (!slot.getType().toLowerCase().equals(opers.get(2))) ruleApplies=false;
				}
				if (opers.get(1).equals("#")){
					if (slot.getType().equals(opers.get(2))) ruleApplies=false;
				}
			} 
			if (!isString){
				double o=Double.parseDouble(opers.get(2));
				if (opers.get(0).equals("x")) v=slot.getPosition().x;
				if (opers.get(0).equals("y")) v=slot.getPosition().y;
				if (opers.get(0).equals("z")) v=slot.getPosition().z;
				if (opers.get(1).equals("=")){
					if (!(v==o))ruleApplies=false;
				}
				if (opers.get(1).equals(">")){
					if (!(v>o))ruleApplies=false;
				}
				if (opers.get(1).equals("<")){
					if (!(v<o))ruleApplies=false;
				}
				if (opers.get(1).equals("#")){
					if (!(v!=o))ruleApplies=false;
				}
			}
		}
		if (ruleApplies){
			sg.addSlot(slot);
		}
	}	
}
}
