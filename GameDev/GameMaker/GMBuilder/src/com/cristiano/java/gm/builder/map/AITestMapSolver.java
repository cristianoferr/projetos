package com.cristiano.java.gm.builder.map;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;

public class AITestMapSolver extends AbstractSolveMap implements ISolveMap {

	@Override
	public void startFrom(MapComponent mapC,MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre){
		super.startFrom(mapC,mapLocation, elSolver,genre);
		Log.info("AITest Map Solver...");
		mapC.minRoomSize=1;
		mapC.maxRoomSize=1;
		RoomComponent room = createNewRoom(); //this map has only one room...
		room.startingRoom=true;
		int x = mapC.getLength()/2;
		int z = mapC.getLength()/2;
		room.setPosition(x, getTerrainHeightAt(x,z), z);
		
		
		BuilderUtils.addWall(entMan,(ElementManager) elSolver.getElementManager(), room,mapC.getLength()/4,60,20,room.getPosition().x,0,room.getPosition().z);
		BuilderUtils.addWall(entMan,(ElementManager) elSolver.getElementManager(), room,20,60,mapC.getLength()/4,room.getPosition().x/2,0,room.getPosition().z/2);
		
		wallMap(mapC, elSolver, room);
		
	}



	

	

	@Override
	public boolean hasCompleted() {
		return true;
	}


}
