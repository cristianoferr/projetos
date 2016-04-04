package com.cristiano.java.gm.builder.map;

import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.product.IGameElement;

public class RandomMapSolver extends AbstractSolveMap implements ISolveMap {

	@Override
	public void startFrom(MapComponent mapC,MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre){
		super.startFrom(mapC,mapLocation, elSolver,genre);
		//BubbleComponent bubbleC = (BubbleComponent) entMan.addComponent(GameComps.COMP_BUBBLE, mapC);
		//bubbleC.loadFromElement(bubbleData.getRandomBubble());
		
		//bubbleC.dimensions=new Vector3f(mapC.getLength(),0,mapC.getLength());
		//bubbleC.position=new Vector3f(mapC.getLength()/2,0,mapC.getLength()/2);
	}

	@Override
	public boolean hasCompleted() {
		return true;
	}

}
