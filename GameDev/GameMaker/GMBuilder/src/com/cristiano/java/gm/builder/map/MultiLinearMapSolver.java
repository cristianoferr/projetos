package com.cristiano.java.gm.builder.map;

import java.util.List;

import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

public class MultiLinearMapSolver extends AbstractSolveMap implements ISolveMap {

	@Override
	public void startFrom(MapComponent mapC,MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre){
		super.startFrom(mapC,mapLocation, elSolver,genre);
		Log.debug("MultiLinear Map Solver...");
		createInitialRooms(true);
		
	}
	
	@Override
	public boolean hasCompleted() {
		int maxRooms=mapC.maxRooms;
		int qtdRooms=0;
		addRandomRoom();
		qtdRooms=countRooms();
		Log.debug("Solving "+qtdRooms+"/"+maxRooms+" rooms.");
		return (qtdRooms>=maxRooms);
	}


	

	

	private void addRandomRoom() {
		List<IGameComponent> roads = getRoads();
		RoadComponent roadToReplace=(RoadComponent) roads.get((int) (CRJavaUtils.random()*roads.size()));
		
		int qtdRooms=(int) (1+(mapC.maxRoadsRoom-1)*CRJavaUtils.random());
		List<IGameEntity> connectedRooms=getConnectedRoomsToRoad(roadToReplace);
		if (connectedRooms.size()!=2){
			Log.error("Numero de rooms conectada à road difere de 2: "+connectedRooms.size());
		}
		
		RoomComponent room0=(RoomComponent) connectedRooms.get(0);
		RoomComponent room1=(RoomComponent) connectedRooms.get(1);
		RoomComponent roomParent=room0;
		RoomComponent roomChild=room1;
		if (room0.parentRoom==room1.roomId){
			roomParent=room1;
			roomChild=room0;
		}
		if (roomChild==roomParent){
			Log.error("room0 e room1 são iguais");
		}
		
		entMan.removeComponentFromEntity(roadToReplace, room0);
		entMan.removeComponentFromEntity(roadToReplace, room1);
		entMan.removeEntity(roadToReplace);
		for (int i=0;i<qtdRooms;i++){
			RoomComponent room = createNewRoom();
			ComponentRecipes.linkRooms(entMan,room0,room);
			ComponentRecipes.linkRooms(entMan,room1,room);
			room.parentRoom=roomParent.roomId;
			roomChild.parentRoom=room.roomId;
		}
		
	}


	
}
