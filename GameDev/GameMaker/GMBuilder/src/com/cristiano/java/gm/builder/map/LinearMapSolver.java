package com.cristiano.java.gm.builder.map;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class LinearMapSolver extends AbstractSolveMap implements ISolveMap {

	int currRoom = 0;
	private IGameElement line;
	private RoomComponent roomAnterior;

	@Override
	public void startFrom(MapComponent mapC, MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre) {
		super.startFrom(mapC, mapLocation, elSolver,genre);
		Log.debug("Linear Map Solver...");
		createInitialRooms(false);
		defineInitialRoomsPositions();

		Vector3f posIni = roomInicial.getPosition();
		Vector3f posFim = roomFinal.getPosition();
		int pontos = (mapC.maxRooms - 1);
		String lineTag = elSolver.getProperty(GameProperties.LINE_FUNCTION_TAG);
		line = BuilderUtils.generateElementLine(lineTag, posIni, posFim, pontos, (ElementManager) elSolver.getElementManager());

		Log.debug("line: " + line.getIdentifier() );

		currRoom = 1;
		roomAnterior = roomInicial;

	}

	@Override
	public boolean hasCompleted() {
		if (countRooms() < mapC.maxRooms) {
			if (roomAnterior==null){
				Log.error("roomAnterior is null!");
			}
			RoomComponent room = createNewRoom();
			int posX = (int) line.getParamAsFloat(Extras.LIST_POSITION, "ponto#" + currRoom + ".x");
			int posY = (int) line.getParamAsFloat(Extras.LIST_POSITION, "ponto#" + currRoom + ".y");
			int posZ = (int) line.getParamAsFloat(Extras.LIST_POSITION, "ponto#" + currRoom + ".z");
			room.setPosition(posX, posY, posZ);
			ComponentRecipes.linkRooms(entMan,roomAnterior, room);
			roomAnterior = room;
			currRoom++;
		} else {
			ComponentRecipes.linkRooms(entMan,roomAnterior, roomFinal);
			finishSolving();
			finishDebugDraw("linearMap");
			return true;
		}
		return false;
	}

	
}
