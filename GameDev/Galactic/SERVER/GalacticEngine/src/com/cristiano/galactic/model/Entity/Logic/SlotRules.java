package com.cristiano.galactic.model.Entity.Logic;

import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.wares.ModelData;


/*
 * Regras genericas para definição de grupos de Slot baseado na posição 
 * e direção de cada Slot.
 *  
 */

public class SlotRules {
	
	
	

	
	
public SlotRules(ModelData md){
	

	createGrpEngine(md);
	createGrpLaserFront(md);
	createGrpWeaponFront(md);
}

private void createGrpLaserFront(ModelData md) {
	SlotGrouping sg=new SlotGrouping();
	
	SlotRule sr=new SlotRule();
	
	//Canto inf esq
	sr.addRule("x>0");
	//sr.addRule("y=0");
	sr.addRule("orient=north");
	sr.addRule("group=laser");
	sr.applyRules(sg,md.getSlots());
	
	md.addSlotGrouping(Consts.CTL_GRP_LASER, sg);
}

private void createGrpWeaponFront(ModelData md) {
	SlotGrouping sg=new SlotGrouping();
	
	SlotRule sr=new SlotRule();
	
	//Canto inf esq
	sr.addRule("x>0");
	//sr.addRule("y=0");
	sr.addRule("orient=north");
	sr.addRule("group=weapon");
	sr.applyRules(sg,md.getSlots());
	
	md.addSlotGrouping(Consts.CTL_GRP_WEAPON, sg);
}


private void createGrpEngine(ModelData md) {
	SlotGrouping sg=new SlotGrouping();
	
	SlotRule sr=new SlotRule();
	
	//Tudo à frente, mirando pra baixo
	sr.addRule("x<0");
	sr.addRule("orient=south");
	sr.addRule("group=engine");
	sr.applyRules(sg,md.getSlots());
	sr.clear();
	
	md.addSlotGrouping(Consts.CTL_GRP_ENGINE, sg);
}

}
