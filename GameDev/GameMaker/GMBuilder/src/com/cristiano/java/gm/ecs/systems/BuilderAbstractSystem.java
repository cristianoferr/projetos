package com.cristiano.java.gm.ecs.systems;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.BubbleDataComponent;
import com.cristiano.utils.Log;

public abstract class BuilderAbstractSystem extends JMEAbstractSystem{

	public BuilderAbstractSystem(String compRequired) {
		super(compRequired);
	}
	

	
	protected BubbleDataComponent getBubbleData() {
		BubbleDataComponent data = (BubbleDataComponent) game.getGameEntity()
				.getComponentWithIdentifier(GameComps.COMP_BUBBLE_DATA);
		if (data == null) {
			return null;
		}

		if (data.enviros.size() == 0) {
			Log.error("No enviro loaded!");
		}

		return data;
	}
}
