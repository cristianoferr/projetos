package com.cristiano.cyclone.forceGenerator.actuators;

import com.cristiano.cyclone.forceGenerator.ActuatorController;


/*
 * Regras genericas para definição de grupos de Slot baseado na posição 
 * e direção de cada Slot.
 *  
 */

public class ActuatorRules {
	private ActuatorController md;
	
	

	
	
public ActuatorRules(ActuatorController md){
	this.md=md;
	
}



public void createGroups() {
	createGrpTurnLeft(md);
	createGrpTurnRight(md);
	createGrpMoveLeft(md);
	createGrpMoveRight(md);
	createGrpRollLeft(md);
	createGrpRollRight(md);
	createGrpTurnDown(md);
	createGrpTurnUP(md);
	createGrpMoveUP(md);
	createGrpMoveDown(md);
}



private void createGrpTurnLeft(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();
	
	ActuatorRule sr=new ActuatorRule();
	
	//Canto inf esq
	sr.addRule("x<0");
	//sr.addRule("y=0");
	sr.addRule("z<0");
	sr.addRuleOrient("west");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	//Canto sup dir
	sr.addRule("x>0");
	//sr.addRule("y=0");
	sr.addRule("z>0");
	sr.addRuleOrient("east");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_TURN_RIGHT, sg);
}

private void createGrpTurnRight(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();
	
	ActuatorRule sr=new ActuatorRule();
	
	//Canto inf dir
	sr.addRule("x<0");
	//sr.addRule("y=0");
	sr.addRule("z>0");
	sr.addRuleOrient("east");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	//Canto sup esq
	sr.addRule("x>0");
	//sr.addRule("y=0");
	sr.addRule("z<0");
	sr.addRuleOrient("west");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_TURN_LEFT, sg);
}

private void createGrpMoveUP(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();

	ActuatorRule sr=new ActuatorRule();
	
	//Tudo à esquerda
	sr.addRule("y<0");
	sr.addRuleOrient("down");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_THRUST_UP, sg);
}

private void createGrpMoveDown(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();
	
	ActuatorRule sr=new ActuatorRule();
	
	//Tudo à esquerda
	sr.addRule("y>0");
	sr.addRuleOrient("up");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_THRUST_DOWN, sg);
}


private void createGrpMoveLeft(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();
	
	ActuatorRule sr=new ActuatorRule();
	
	//Tudo à esquerda
	sr.addRule("z<0");
	sr.addRuleOrient("west");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_THRUST_LEFT, sg);
}

private void createGrpMoveRight(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();
	
	ActuatorRule sr=new ActuatorRule();
	
	//Tudo à direita
	sr.addRule("z>0");
	sr.addRuleOrient("east");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_THRUST_RIGHT, sg);
}

private void createGrpRollLeft(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();
	
	ActuatorRule sr=new ActuatorRule();
	
	//Tudo à esquerda, mirando pra baixo
	sr.addRule("z<0");
	sr.addRuleOrient("down");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	
	//Tudo à direita, mirando pra cima
	sr.addRule("z>0");
	sr.addRuleOrient("up");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_ROTATE_RIGHT, sg);
}

private void createGrpRollRight(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();
	
	ActuatorRule sr=new ActuatorRule();
	
	//Tudo à esquerda, mirando pra cima
	sr.addRule("z<0");
	sr.addRuleOrient("up");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	
	//Tudo à direita, mirando pra baixo
	sr.addRule("z>0");
	sr.addRuleOrient("down");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_ROTATE_LEFT, sg);
}

private void createGrpTurnDown(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();
	
	ActuatorRule sr=new ActuatorRule();
	
	//Tudo à frente, mirando pra cima
	sr.addRule("x>0");
	sr.addRuleOrient("up");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	
	//Tudo atras, mirando pra baixo
	sr.addRule("x<0");
	sr.addRuleOrient("down");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_TURN_UP, sg);
}

private void createGrpTurnUP(ActuatorController md) {
	ActuatorGroup sg=new ActuatorGroup();
	
	ActuatorRule sr=new ActuatorRule();
	
	//Tudo à frente, mirando pra baixo
	sr.addRule("x>0");
	sr.addRuleOrient("down");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	
	//Tudo atras, mirando pra cima
	sr.addRule("x<0");
	sr.addRuleOrient("up");
	sr.addRuleGroup(ActuatorController.ACT_THRUSTER);
	sr.applyRules(sg,md.getActuators());
	sr.clear();
	
	md.addGroup(ActuatorController.STEER_TURN_DOWN, sg);
}

}
