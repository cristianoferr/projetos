package com.cristiano.java.gm.utils;

import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.art.ImageRequestComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.comps.persists.InternationalComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.unit.AIComponent;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.ecs.comps.unit.DimensionComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.DPSComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.LapResourceComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.RaceGoalComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.SpeedComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.BillboardComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.OrientationComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.utils.Log;

/*
 * Focus on getting stuff... */
public abstract class ECS {
	
	public static void addRotationComponent(IGameEntity ent, RenderComponent compRender,EntityManager entMan) {
		OrientationComponent posComp;
		posComp = (OrientationComponent) ent.getComponentWithIdentifier(GameComps.COMP_ORIENTATION);
		if (posComp == null) {
			posComp = (OrientationComponent) entMan.addComponent(GameComps.COMP_ORIENTATION, ent);
		} else {
			if (posComp.getOrientation() != null) {
				compRender.node.setLocalRotation(posComp.getOrientation());
			}
		}
		posComp.setNode(compRender.node);
	}
	
	//Used by the ImageRequestSystem
	public static ImageRequestComponent checkIfImageIsRequested(List<IGameEntity> ents, String imageTag) {
		for (IGameEntity ent : ents) {
			ImageRequestComponent comp = (ImageRequestComponent) ent.getComponentWithIdentifier(GameComps.COMP_REQUEST_IMAGE);
			if (comp.imageTag.equals(imageTag)) {
				return comp;
			}
		}
		return null;
	}

	
	public static RoadComponent findRoadInCommon(RoomComponent room0, RoomComponent room1) {
		if (room0==null ||room1==null){
			Log.error("Null rooms found...");
			return null;
		}
		List<IGameComponent> roads0 = room0.getComponentsWithIdentifier(GameComps.COMP_ROAD);
		List<IGameComponent> roads1 = room1.getComponentsWithIdentifier(GameComps.COMP_ROAD);
		
		for (IGameComponent road0:roads0){
			for (IGameComponent road1:roads1){
				if (road0==road1){
					return (RoadComponent) road0;
				}
			}
		}
		return null;
	}

	public static List<IGameComponent> getRooms(IGameEntity mapC) {
		List<IGameComponent> components = mapC.getComponentsWithIdentifier(GameComps.COMP_ROOM);
		return components;
	}
	
	public static RoomComponent getRoomWithId(IGameEntity map, int idRoom) {
		RoomComponent roomDest = null;
		List<IGameComponent> rooms = getRooms(map);
		for (IGameComponent comp : rooms) {
			RoomComponent room = (RoomComponent) comp;
			if (room.roomId == idRoom) {
				roomDest = room;
			}
		}
		return roomDest;
	}

	public static RaceGoalComponent getRacegoalComponent(IGameEntity ent) {
		return (RaceGoalComponent) ent.getComponentWithIdentifier(GameComps.COMP_RESOURCE_RACE_GOAL);
	}

	public static PlayerComponent getPlayerComponent(IGameEntity ent) {
		if (ent==null){
			Log.error("No Player found!");
			return null;
		}
		return (PlayerComponent) ent.getComponentWithIdentifier(GameComps.COMP_PLAYER);
	}

	public static MapComponent getMap(IGameEntity ent) {
		MapComponent data = (MapComponent) ent.getComponentWithIdentifier(GameComps.COMP_MAP);
		return data;
	}

	public static MapWorldComponent getMapWorldComponent(IGameEntity ent) {
		return (MapWorldComponent) ent.getComponentWithIdentifier(GameComps.COMP_MAP_WORLD);
	}

	public static OrientationComponent getOrientationComponent(IGameEntity playerEnt) {
		OrientationComponent posC = (OrientationComponent) playerEnt.getComponentWithIdentifier(GameComps.COMP_ORIENTATION);
		return posC;
	}

	public static PositionComponent getPositionComponent(IGameEntity playerEnt) {
		if (playerEnt==null){
			return null;
		}
		PositionComponent posC = (PositionComponent) playerEnt.getComponentWithIdentifier(GameComps.COMP_POSITION);
		return posC;
	}
	public static CamComponent getCamComponent(IGameEntity playerEnt) {
		CamComponent posC = (CamComponent) playerEnt.getComponentWithIdentifier(GameComps.COMP_CAM);
		return posC;
	}

	public static float getHealthPoints(IGameEntity ent) {
		HealthComponent healthC = getHealthComponent(ent);
		if (healthC == null) {
			Log.error("Entity '" + ent + "'" + " has no health component.");
			return 0;
		}
		return healthC.getCurrValue();
	}

	public static float getTargetDistance(IGameEntity ent, IGameEntity target) {
		PositionComponent posE = (PositionComponent) ent.getComponentWithIdentifier(GameComps.COMP_POSITION);
		PositionComponent posT = (PositionComponent) target.getComponentWithIdentifier(GameComps.COMP_POSITION);

		// posE.setPos(posT.getPos().add(0, 20, 0));
		if (posT == null) {
			Log.error("Target has no position component:" + target);

			return -2;
		}
		if ((posE == null)) {
			Log.error("Entity has no position component!!");
			return -1;
		}
		float distance = posE.getPos().distance(posT.getPos());
		return distance;
	}

	public static void updateTargetPosition(IGameEntity ent, TargetComponent targetC) {
		PositionComponent entPosition = (PositionComponent) ent.getComponentWithIdentifier(GameComps.COMP_POSITION);
		PositionComponent posT = (PositionComponent) targetC.target.getComponentWithIdentifier(GameComps.COMP_POSITION);
		targetC.lastPosition.set(posT.getPos());

		if (targetC.dimensions == null) {
			DimensionComponent dimC = getDimensionComponent(targetC.target);
			if (dimC==null){
				Log.error("Entity has no dimension component defined...");
			} else {
				targetC.dimensions = dimC.dimension;
			}
		}

		SpeedComponent speedT = getSpeedComponent(targetC.target);
		if (speedT != null) {
			SpeedComponent entSpeed = getSpeedComponent(ent);
			if (speedT.velocity != null) {
				targetC.lastVelocity.set(speedT.velocity);

				targetC.lastRelVelocity.set(targetC.lastVelocity).subtractLocal(entSpeed.velocity);
			}
		}

		targetC.lastRelPos.set(targetC.lastPosition);
		targetC.lastRelPos.y -= targetC.dimensions.y * 2;
		targetC.lastRelPos.subtractLocal(entPosition.getPos());
	}

	public static DimensionComponent getDimensionComponent(IGameEntity ent) {
		return (DimensionComponent) ent.getComponentWithIdentifier(GameComps.COMP_DIMENSION);
	}

	public static TargetComponent getTargetComponent(IGameEntity ent) {
		return (TargetComponent) ent.getComponentWithIdentifier(GameComps.COMP_TARGET);
	}

	public static DPSComponent getDPSComponent(IGameEntity ent) {
		return (DPSComponent) ent.getComponentWithIdentifier(GameComps.COMP_RESOURCE_DPS);
	}

	public static SpeedComponent getSpeedComponent(IGameEntity ent) {
		SpeedComponent speed = (SpeedComponent) ent.getComponentWithIdentifier(GameComps.COMP_SPEED);
		if (speed == null) {
			Log.error("Entity '" + ent + "' has no speedComponent");
		}
		return speed;
	}

	public static UnitClassComponent getUnitRoleComponent(IGameEntity ent) {
		return (UnitClassComponent) ent.getComponentWithIdentifier(GameComps.COMP_UNIT_CLASS);
	}

	public static TerrainComponent getTerrainFrom(IGameEntity ent) {
		return (TerrainComponent) ent.getComponentWithIdentifier(GameComps.COMP_TERRAIN);
	}

	public static void defineTarget(EntityManager entMan, IGameEntity ent, IGameEntity target) {
		Log.debug(ent + " has defined target to:" + target);
		TargetComponent targetC = (TargetComponent) entMan.addIfNotExistsComponent(GameComps.COMP_TARGET, ent);
		targetC.target = target;
		targetC.directPosition = (PositionComponent) target.getComponentWithIdentifier(GameComps.COMP_POSITION);
		updateTargetPosition(ent, targetC);
	}

	public static List<IGameComponent> getWaypointsFrom(IGameComponent ent, List<IGameComponent> waypoints) {
		return ent.getComponentsWithIdentifier(GameComps.COMP_WAYPOINT,waypoints);
	}

	public static LapResourceComponent getLapResource(IGameEntity ent) {
		return (LapResourceComponent) ent.getComponentWithIdentifier(GameComps.COMP_RESOURCE_LAP);
	}

	public static BillboardComponent getBillboardComponent(IGameEntity ent) {
		return (BillboardComponent) ent.getComponentWithIdentifier(GameComps.COMP_BILLBOARD);
	}

	public static HealthComponent getHealthComponent(IGameEntity ent) {
		return (HealthComponent) ent.getComponentWithIdentifier(GameComps.COMP_RESOURCE_HEALTH);
	}

	public static IGameComponent getComponent(IGameEntity ent,String ident) {
		if (ent==null){
			return null;
		}
		return ent.getComponentWithIdentifier(ident);
	}

	public static RenderComponent getRenderComponent(IGameEntity ent) {
		return (RenderComponent) getComponent(ent,GameComps.COMP_RENDER);
	}
	
	public static PhysicsComponent getPhysicsComponent(IGameEntity playerEnt) {
		PhysicsComponent playerC = (PhysicsComponent) playerEnt.getComponentWithIdentifier(GameComps.COMP_PHYSICS);
		return playerC;
	}

	public static List<IGameComponent> getRoads(IGameEntity mapC) {
		List<IGameComponent> components = mapC.getComponentsWithIdentifier(GameComps.COMP_ROAD);
		return components;
	}


	public static NiftyComponent getNifty(IGameEntity gameEntity) {
		return (NiftyComponent) gameEntity.getComponentWithIdentifier(GameComps.COMP_NIFTY);
	}

	public static InternationalComponent getInternationalComponent(
			IGameEntity entity) {
		return (InternationalComponent) entity.getComponentWithIdentifier(GameComps.COMP_INTERNATIONAL);
	}

	public static AIComponent getAIComp(IGameEntity ent) {
		return (AIComponent) ent.getComponentWithIdentifier(GameComps.COMP_AI);
		
	}

	public static ChildComponent getChild(IGameEntity entity) {
		return (ChildComponent) getComponent(entity, GameComps.COMP_CHILD);
	}



	
}
