package com.cristiano.java.gm.builder.map;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;


public class ArenaMapSolver extends GenericMapSolver implements ISolveMap {


	private float percW;
	private float percD;

	@Override
	public void startFrom(MapComponent mapC,MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre){
		super.startFrom(mapC,mapLocation, elSolver,genre);
		Log.info("Arena Map Solver...");
		float minPerc=elSolver.getPropertyAsFloat(GameProperties.MIN_MAP_FILL_PERC)/100;
		float maxPerc=elSolver.getPropertyAsFloat(GameProperties.MAX_MAP_FILL_PERC)/100;
		percW = CRJavaUtils.random(minPerc,maxPerc);
		percD = CRJavaUtils.random(minPerc,maxPerc);
		room.startingRoom=true;
		int x = mapC.getLength()/2;
		int z = mapC.getLength()/2;
		room.setPosition(x, getTerrainHeightAt(x,z), z);
		initDimensions();
		
		wallMap(mapC, elSolver, room);
	}
	
	protected void initDimensions() {
		if (mapC.roomHeight<=0){
			Log.fatal("Map has invalid room height:"+mapC.roomHeight);
		}
		room.setDimension(mapC.getLength()*percW, mapC.roomHeight,mapC.getLength()*percD);
	}

	@Override
	public boolean hasCompleted() {
		return super.hasCompleted();
	}
	

}
