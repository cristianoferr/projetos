package com.cristiano.java.gm.builder.map;

import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.product.IGameElement;

public class FixedTestMapSolver extends AbstractSolveMap implements ISolveMap {

	@Override
	public void startFrom(MapComponent mapC,MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre){
		super.startFrom(mapC,mapLocation, elSolver,genre);
		RoomComponent roomTopLeft = createNewRoom();
		roomTopLeft.setPosition(300, 0,100);
		roomTopLeft.setDimension(500,50, 100);
		
		RoomComponent roomTopDown = createNewRoom();
		roomTopDown.setPosition(512,0, 800);
		roomTopLeft.setDimension(100, 50,100);
	}

	@Override
	public boolean hasCompleted() {
		return true;
	}

}
