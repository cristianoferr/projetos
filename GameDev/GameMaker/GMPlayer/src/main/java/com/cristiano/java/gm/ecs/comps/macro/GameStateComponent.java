package com.cristiano.java.gm.ecs.comps.macro;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.StagedComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;


public class GameStateComponent extends StagedComponent {
			
	private static final String WAITING = "WAITING";

	public GameStateComponent(){
		super(GameComps.COMP_GAME_STATE);
		stageControl.add(WAITING);
	}
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		
	}
	
	@Override
	public IGameComponent clonaComponent() {
		GameStateComponent ret = new GameStateComponent();
		ret.stageControl.setCurrentStage(stageControl);
		//ret.master=master;
		return ret;
	}
	
	@Override
	public void resetComponent() {
	}
	

}
