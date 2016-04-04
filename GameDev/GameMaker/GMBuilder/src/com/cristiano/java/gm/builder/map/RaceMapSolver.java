package com.cristiano.java.gm.builder.map;

import java.util.List;
import java.util.Random;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class RaceMapSolver extends AbstractSolveMap implements ISolveMap {

	int currRoom = 0;
	private IGameElement line;
	private RoomComponent roomAnterior, room;
	float angleIncrease = 0;
	float angle = 0;
	int iterations = 0;
	private Vector3f midPoint;
	private Random rnd;
	private float lapDistance=0;
	private float currDistance=0;

	@Override
	public void startFrom(MapComponent mapC, MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre) {
		super.startFrom(mapC, mapLocation, elSolver,genre);
		Log.debug("Race Map Solver...");
		
		lapDistance = mapC.lapDistance;
		if (lapDistance<=0){
			Log.fatal("Invalid lapDistance:"+lapDistance);
		}

		rnd = new Random(mapC.seed);
		angleIncrease = 360 / mapC.maxRooms;
		angle = 0;

		midPoint = new Vector3f(mapC.length / 2, 0, mapC.length / 2);
		mapC.setProperty(GameProperties.LAP_GOAL,1);
	}

	@Override
	public boolean hasCompleted() {
		if (angle < 360) {
			room = createNewRoom();
			ComponentRecipes.linkRooms(entMan,roomAnterior, room);
			Vector3f pos = new Vector3f();
			float radius = mapC.length * 0.2f + rnd.nextFloat()*mapC.length * 0.8f;
			radius /= 2;
			CRMathUtils.rotateVector2D(Vector3f.ZERO, radius, angle, pos);//+midPoint
			
			room.setPosition(pos);
			

			updateDistance();
			if (roomAnterior == null) {
				roomInicial = room;
			}
			angle += angleIncrease / 2 +(angleIncrease / 4)+rnd.nextFloat()*(angleIncrease / 4);
			roomAnterior = room;
			iterations++;
			return false;
		} else {
			checkLapSize();
			ComponentRecipes.linkRooms(entMan,roomAnterior, roomInicial);
			List<IGameComponent> rooms = getRooms();
			RoomComponent room = (RoomComponent) rooms.get((int) (rooms.size() * rnd.nextFloat()));
			room.startingRoom = true;
			finishSolving();

			finishDebugDraw("raceMap");
		}
		return true;
	}
	// finishSolving();
	// finishDebugDraw("raceMap");

	private void checkLapSize() {
		float ratio=lapDistance/currDistance;
		List<IGameComponent> rooms = getRooms();
		for (IGameComponent comp:rooms){
			
			RoomComponent room=(RoomComponent) comp;
			float y=room.getPosition().y;
			room.getPosition().multLocal(ratio);
			room.getPosition().addLocal(midPoint);
			room.getPosition().y=y;
		}
	}

	private void updateDistance() {
		if (roomAnterior!=null){
			currDistance+=roomAnterior.getPosition().distance(room.getPosition());
		}
	}
}
