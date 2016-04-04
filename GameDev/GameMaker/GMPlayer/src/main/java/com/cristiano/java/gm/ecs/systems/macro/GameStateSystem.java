package com.cristiano.java.gm.ecs.systems.macro;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.macro.GameStateComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;


public class GameStateSystem extends AbstractMacroSystem {

	public GameStateSystem() {
		super(GameComps.COMP_GAME_STATE);
		
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,float tpf) {
		GameStateComponent comp=(GameStateComponent) component;
			}

	
	
}
