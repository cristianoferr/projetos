package com.cristiano.java.gm.ecs.systems.unit.resources;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.unit.resources.LapResourceComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.RaceGoalComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.UnitPositionComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IRunGame;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.utils.Log;

/**
 * 
 * @author CMM4
 * 
 * 
 *         Test: TestNPCStuff
 */
public class UnitPositionSystem extends JMEAbstractSystem {

	private List<IGameEntity> entities = new ArrayList<IGameEntity>();
	IGameEntity[]entitiesArr=new IGameEntity[0];

	public UnitPositionSystem() {
		super(GameComps.COMP_RESOURCE_UNIT_POSITION);
	}

	@Override
	public void initSystem(EntityManager entMan, IRunGame game) {
		super.initSystem(entMan, game);
	}

	@Override
	protected void preTick(float tpf) {
		super.preTick(tpf);
		entitiesArr=entMan.getEntitiesWithComponent(compRequired,entities).toArray(entitiesArr);
		for (int i = 0; i < entities.size() - 1; i++) {
			for (int j = i + 1; j < entities.size(); j++) {
				IGameEntity entI = entities.get(i);
				IGameEntity entJ = entities.get(j);
				if (isEntityAhead(entJ, entI)) {
					entitiesArr[j]= entI;
					entitiesArr[i]=entJ;
				}
			}
		}

	}

	public IGameEntity[] testOrdering() {
		preTick(0);
		return entitiesArr;
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent comp, float tpf) {
		UnitPositionComponent posC = (UnitPositionComponent) comp;
		for (int i = 0; i < entitiesArr.length; i++) {
			IGameEntity entI = entitiesArr[i];
			if (entI == ent) {
				posC.setCurrValue(i + 1);
			}
		}
		posC.setMaxValue(entitiesArr.length);
	}

	public static boolean isEntityAhead(IGameEntity entity1, IGameEntity entity2) {
		LapResourceComponent lap1 = ECS.getLapResource(entity1);
		LapResourceComponent lap2 = ECS.getLapResource(entity2);
		int x;
		if (lap1==null){
			Log.error("Entities hava unitPosition and dont have lapResource!");
			entity1.removeComponent(GameComps.COMP_RESOURCE_UNIT_POSITION);
			return false;
		}
		if (lap2==null){
			Log.error("Entities hava unitPosition and dont have lapResource!");
			entity2.removeComponent(GameComps.COMP_RESOURCE_UNIT_POSITION);
			return false;
		}
		if (lap1.getCurrValue() > lap2.getCurrValue()) {
			return true;
		}
		if (lap1.getCurrValue() < lap2.getCurrValue()) {
			return false;
		}

		RaceGoalComponent goal1 = ECS.getRacegoalComponent(entity1);
		RaceGoalComponent goal2 = ECS.getRacegoalComponent(entity2);
		if (goal1.getCurrValue() > goal2.getCurrValue()) {
			return true;
		}
		if (goal1.getCurrValue() < goal2.getCurrValue()) {
			return false;
		}

		float d1 = RaceGoalSystem.calcDistanceWaypoint(goal1);
		float d2 = RaceGoalSystem.calcDistanceWaypoint(goal2);
		return d1 < d2;
	}

}
