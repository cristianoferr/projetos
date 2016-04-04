package com.cristiano.java.gm.utils;

import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.map.FlattenTerrainComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.map.WaypointComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TimerComponent;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.ecs.comps.visual.OrientationComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public abstract class ComponentRecipes {

	public static void addWaypoint(JMESnippets snippets, EntityManager entMan, IGameEntity mapEnt, Vector3f posAtual, RoadComponent roadComp) {
		//debugPoint(snippets, posAtual,ColorRGBA.Brown,10);
		WaypointComponent way = (WaypointComponent) entMan.addComponent(GameComps.COMP_WAYPOINT, mapEnt);
		roadComp.attachComponent(way);
		way.position.set(posAtual);
	}
	
	public static void debugPoint(JMESnippets snippets, Vector3f posAtual,ColorRGBA color,int size) {
		Log.debug("debugPoint:"+posAtual+" Color:"+color);
		debugPoint(snippets, posAtual,color,size,5);
	}

	public static void debugPoint(JMESnippets snippets, Vector3f posAtual,ColorRGBA color,int size,int width) {
		if (snippets==null){
			return;
		}
		if (CRJavaUtils.IS_DEBUG) {
			snippets.attach(snippets.generateBox(color, new Vector3f(width, size, width), posAtual));
		}
	}

	public static RoadComponent linkRooms(EntityManager entMan, RoomComponent roomInicial, RoomComponent roomFinal) {
		if ((roomInicial == null) || (roomFinal == null)) {
			return null;
		}
		RoadComponent caminho = (RoadComponent) entMan.addComponent(GameComps.COMP_ROAD, roomInicial);
		entMan.addComponent(caminho, roomFinal);
		Log.debug("Linking room " + roomInicial.getId() + " to room " + roomFinal.getId() + " road:" + caminho.getId());
		return caminho;
	}


	public static void addDeathTimer(EntityManager entMan, IGameEntity entity, int time) {
		TimerComponent timer = (TimerComponent) entMan.addComponent(GameComps.COMP_TIMER, entity);
		timer.limitTime = time;
		entMan.addComponent(GameComps.COMP_DEATH, timer);
	}

	// Carrega a gameEntity a partir da IGameElement informada...
	public static ChildComponent loadEntityFromElement(EntityManager entMan, IGameEntity ent, IGameElement elEntity, Vector3f pos) {
		ChildComponent compLoader = createCompLoader(entMan, ent, pos);
		compLoader.elementSource = elEntity;
		return compLoader;
	}

	public static ChildComponent loadEntityFromTag(EntityManager entMan, IGameEntity ent, String tag, Vector3f pos) {
		ChildComponent compLoader = createCompLoader(entMan, ent, pos);
		compLoader.tag = tag;

		return compLoader;
	}

	// add or set the entity position
	public static void defineEntityPosition(IGameEntity player, Vector3f pos, EntityManager entMan) {
		PositionComponent posComp = (PositionComponent) player.getComponentWithIdentifier(GameComps.COMP_POSITION);
		if (posComp == null) {
			Log.error("Entity " + player + " has no position defined!");
			return;
		}
		posComp.setPos(pos);
	}

	public static void defineEntityOrientation(ChildComponent loader, float radians, Vector3f unitY, EntityManager entMan) {
		OrientationComponent posComp = (OrientationComponent) loader.getComponentWithIdentifier(GameComps.COMP_ORIENTATION);
		if (posComp == null) {
			Log.error("Entity " + loader + " has no orientation defined!");
			return;
		}
		Quaternion roll = new Quaternion();
		roll.fromAngleAxis(radians, unitY);
		posComp.setOrientation(roll);

	}

	public static ChildComponent createCompLoader(EntityManager entMan, IGameEntity ent, Vector3f pos) {
		ChildComponent compLoader = (ChildComponent) entMan.addComponent(GameComps.COMP_CHILD, ent);
		compLoader.madeBy = ent;
		compLoader.addRender = true;

		PositionComponent compPosition = (PositionComponent) entMan.addComponent(GameComps.COMP_POSITION, compLoader);
		compPosition.setPos(pos);

		entMan.addComponent(GameComps.COMP_ORIENTATION, compLoader);

		return compLoader;
	}

	public static void flattenTerrain(EntityManager entMan, Vector3f position, IGameElement elFloor) {
		List<IGameEntity> maps = entMan.getEntitiesWithComponent(GameComps.COMP_TERRAIN);
		if (maps.size() == 1) {
			FlattenTerrainComponent flatten = (FlattenTerrainComponent) entMan.addComponent(GameComps.COMP_FLATTEN_TERRAIN, maps.get(0));
			flatten.setPosition(position);
			flatten.setMeshFunction(elFloor);
		} else {
			Log.error("Number of maps invalid:" + maps.size());
		}

	}

}
