package com.cristiano.java.gm.ecs.systems.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.BubbleComponent;
import com.cristiano.java.gm.ecs.comps.map.BubbleDataComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.systems.BuilderAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

/*
 * RoomSolver pega as rooms geradas pelo mapLoader e as posiciona no mapa... as rooms nao sao geradas aqui.
 */

public class RoomSolverSystem extends BuilderAbstractSystem {
	public static CRDebugDraw draw;

	public RoomSolverSystem() {
		super(GameComps.COMP_ROOM);
	}

	@Override
	// ent=Mapcomponent
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		RoomComponent roomComp = (RoomComponent) component;
		MapComponent map = (MapComponent) ent;

		initDebugDraw(1000);

		if (component.isFirstTick()) {
			if (map.isOnStage(MapComponent.ROOM_SOLVING)) {
				solveRoom(map, roomComp);
				calcEdges(map, roomComp);
				//ComponentRecipes.debugPoint(snippets, roomComp.getPosition(), ColorRGBA.Green, 80);
				draw.setColor(Color.blue);
				draw.fillRect(roomComp.getPosition().x, roomComp.getPosition().z, 15, 15);
			}

		}

	}

	public void initDebugDraw(int size) {
		if (draw == null) {
			draw = new CRDebugDraw(size);
		}
	}

	public void calcEdges(MapComponent ent, RoomComponent roomComp) {
		List<RoomComponent> connectedRooms = getConnectedRooms(ent, roomComp);
		if (connectedRooms != null) {
			calcEdge(ent, roomComp, connectedRooms);
		} else {
			Log.warn("Room has no roads:"+roomComp);
		}
	}

	public List<RoomComponent> getConnectedRooms(MapComponent ent, RoomComponent roomComp) {
		List<IGameComponent> roads = ECS.getRoads(roomComp);
		if (roads.size() != 2) {
			return null;
		}
		List<RoomComponent> connectedRooms = new ArrayList<RoomComponent>();
		for (int i = 0; i < roads.size(); i++) {
			RoomComponent connectedRoom = getConnectedRoom(ent, roomComp, roads.get(i));
			if (connectedRoom!=null){
				connectedRooms.add(connectedRoom);
			}
		}
		return connectedRooms;
	}

	private void calcEdge(MapComponent ent, RoomComponent roomBase, List<RoomComponent> connectedRooms) {
		Vector3f posAtual = roomBase.getPosition();

		connectedRooms=getConnectedRooms(ent, roomBase);
		if (connectedRooms.isEmpty()){
			return;
		}
		List<Vector3f> ptsAnt = JMEUtils.getPerpendicularPointsXZ(posAtual, connectedRooms.get(0).getPosition(), ent.roadWidth);
		List<Vector3f> ptsAtual = JMEUtils.getPerpendicularPointsXZ(posAtual, connectedRooms.get(1).getPosition(), ent.roadWidth);

		Vector3f ptA = ptsAnt.get(0);// A
		Vector3f ptC = ptsAnt.get(1);// C
		Vector3f ptD = ptsAtual.get(0);// D
		Vector3f ptB = ptsAtual.get(1);// B

		Vector3f midptAB = ptA.add(ptB).mult(0.5f);
		Vector3f midptCD = ptC.add(ptD).mult(0.5f);

		roomBase.edgePt0 = JMEUtils.rotatePointXZ(posAtual, midptAB, 0, ent.roadWidth);
		roomBase.edgePt1 = JMEUtils.rotatePointXZ(posAtual, midptCD, 0, ent.roadWidth);

		draw.setColor(Color.red);
		draw.fillRect(roomBase.edgePt0.x, roomBase.edgePt0.z, 15, 15);
		draw.fillRect(roomBase.edgePt1.x, roomBase.edgePt1.z, 15, 15);

		Log.debug("Closing edge for room:" + roomBase);

		TerrainComponent terrain = ECS.getTerrainFrom(ent);
		Vector3f[] tri1 = new Vector3f[] { ptA, ptB, ptC };
		Vector3f[] tri2 = new Vector3f[] { ptC, ptD, ptB };

		if (terrain != null) {
			terrain.flattenArea(tri1, null);
			terrain.flattenArea(tri2, null);
		}

	}

	public RoomComponent getConnectedRoom(IGameEntity ent, RoomComponent roomFrom, IGameComponent road) {
		List<IGameEntity> rooms1 = entMan.getEntitiesWithComponent(GameComps.COMP_ROOM);
		List<IGameEntity> rooms = entMan.getEntitiesWithComponent(road);
		if (rooms.size() != 2) {
			Log.error("There should have 2 rooms with the given road!");
			return null;
		}
		RoomComponent roomTo = (RoomComponent) rooms.get(0);
		if (roomTo == roomFrom) {
			roomTo = (RoomComponent) rooms.get(1);
		}

		return roomTo;
	}

	private void solveRoom(MapComponent ent, RoomComponent roomComp) {
		Log.debug("Solving room " + roomComp.getId() + "...");
		BubbleDataComponent data = getBubbleData();
		BubbleComponent bubble = data.cloneBubbleFittingInto(roomComp.getDimensions(), roomComp.bubbleFilter);
		if (bubble != null) {
			bubble.position = roomComp.getPosition();
			bubble.dimensions = roomComp.getDimensions();
			bubble.spacing = ent.enviroSpacing;
			entMan.addComponent(bubble, roomComp);
		}

		roomComp.firstTick = false;
	}

}
