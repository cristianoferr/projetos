package com.cristiano.java.gm.interfaces.map;

import java.util.List;

import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;


public interface ISolveMap {
	void initVars(EntityManager entMan);
	void startFrom(MapComponent mapC,MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre);
	boolean hasCompleted();//true=concluido
	void finishGeneration();
	
	RoomComponent createNewRoom();
	List<IGameComponent> getRoads();
}
