package com.cristiano.java.gm.builder.map;

import java.util.List;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class RaceMapTest extends AbstractSolveMap implements ISolveMap {

	int currRoom = 0;
	private Vector3f midPoint;

	@Override
	public void startFrom(MapComponent mapC, MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre) {
		super.startFrom(mapC, mapLocation, elSolver,genre);
		Log.debug("Race Map Solver...");

		midPoint = new Vector3f(mapC.length / 2, 0, mapC.length / 2);
		mapC.setProperty(GameProperties.LAP_GOAL, 1);
		

	}

	@Override
	public boolean hasCompleted() {

		int offset=300;
		int mult=3;
		RoomComponent room0 = createNewRoom();
		room0.setPosition(new Vector3f(100*mult+offset, 0, 100*mult+offset));

		RoomComponent room1 = createNewRoom();
		room1.setPosition(new Vector3f(200*mult+offset, 0, 100*mult+offset));

		RoomComponent room2 = createNewRoom();
		room2.setPosition(new Vector3f(200*mult+offset, 0, 200*mult+offset));
		
		RoomComponent room3 = createNewRoom();
		room3.setPosition(new Vector3f(100*mult+offset, 0, 200*mult+offset));

		linkRooms(room0, room1);
		linkRooms(room2, room1);
		linkRooms(room2, room3);
		linkRooms(room3, room0);

		wallMap(mapC, elSolver, room0);
		
		roomInicial = room0;
		room0.startingRoom = true;
		finishSolving();

		finishDebugDraw("raceMap");
		return true;
	}
	// finishSolving();
	// finishDebugDraw("raceMap");

	private void linkRooms(RoomComponent room1, RoomComponent room2) {
		RoadComponent road = ComponentRecipes.linkRooms(entMan, room1, room2);
		entMan.cleanup();
		
		List<IGameEntity> rooms = entMan.getEntitiesWithComponent(road);
		if (rooms.size() != 2) {
			Log.fatal("There should have 2 rooms with the given road!");
		}
	}
}
