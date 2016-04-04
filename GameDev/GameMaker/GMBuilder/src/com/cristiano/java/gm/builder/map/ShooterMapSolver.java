package com.cristiano.java.gm.builder.map;

import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class ShooterMapSolver extends GenericMapSolver implements ISolveMap {

	@Override
	public void startFrom(MapComponent mapC, MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre) {
		Log.debug("Shooter Map Solver...");
		super.startFrom(mapC, mapLocation, elSolver,genre);
		
	}

	@Override
	public boolean hasCompleted() {
		return super.hasCompleted();
		
	}
}
