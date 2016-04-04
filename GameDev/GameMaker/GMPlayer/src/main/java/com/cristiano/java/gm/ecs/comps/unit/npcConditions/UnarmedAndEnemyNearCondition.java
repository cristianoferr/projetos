package com.cristiano.java.gm.ecs.comps.unit.npcConditions;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class UnarmedAndEnemyNearCondition extends AbstractNPCConditionComponent {

	public UnarmedAndEnemyNearCondition(){
		super(GameComps.COMP_NPC_CONDITION_UNARMED_ENEMY_NEAR);
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}
	@Override
	public IGameComponent clonaComponent() {
		return new UnarmedAndEnemyNearCondition();
	}
	@Override
	public void resetComponent() {
	}
}
