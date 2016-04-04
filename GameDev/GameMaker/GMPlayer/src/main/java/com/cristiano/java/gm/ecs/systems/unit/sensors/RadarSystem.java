package com.cristiano.java.gm.ecs.systems.unit.sensors;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.unit.TargettingComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.RadarComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.GMUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class RadarSystem extends JMEAbstractSystem {
	Vector3f reuseVector = new Vector3f();

	final List<IGameEntity> _targets = new ArrayList<IGameEntity>();

	public RadarSystem() {
		super(GameComps.COMP_RADAR);
	}

	@Override
	protected void preTick(float tpf) {
		super.preTick(tpf);
		entMan.getEntitiesWithComponent(GameComps.COMP_TARGETABLE, _targets);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		if (!isGameRunning()) {
			return;
		}
		RadarComponent radarC = (RadarComponent) component;
		IGameComponent targetC = ent.getComponentWithIdentifier(GameComps.COMP_TARGET);
		if (targetC == null) {
			noTargetBehaviour(ent, radarC);
		} else {
			targetBehaviour(ent, radarC, (TargetComponent) targetC, tpf);
		}
	}

	public void targetBehaviour(IGameEntity ent, RadarComponent radarC, TargetComponent targetC, float tpf) {
		int targetVisible = isTargetVisible(radarC, ent, targetC.target);
		if (targetVisible > 0) {
			updateNearRange(radarC, ent, targetC);

			// Log.debug("I can see you...");
			ECS.updateTargetPosition(ent, targetC);
			updateTargetVisibleComponent(ent);
			targetC.timeNotVisible = 0;
		} else if (targetVisible == -2) {
			// -2 is when a targetcomponent points to a non existent entity...
			ent.removeComponent(targetC);
		} else {
			targetNotVisibleBehavior(ent, targetC, tpf);
			removeTargetVisibleComponent(ent);
		}

	}

	private void updateTargetVisibleComponent(IGameEntity ent) {
		entMan.addIfNotExistsComponent(GameComps.COMP_TARGET_VISIBLE, ent);

	}

	private void removeTargetVisibleComponent(IGameEntity ent) {
		ent.removeComponent(GameComps.COMP_TARGET_VISIBLE);

	}

	private void updateNearRange(RadarComponent radarC, IGameEntity ent, TargetComponent targetC) {
		float distance = ECS.getTargetDistance(ent, targetC.target);
		if (distance < radarC.nearRange) {
			entMan.addIfNotExistsComponent(GameComps.COMP_NPC_STATUS_ENEMY_NEAR, ent);
		} else {
			ent.removeComponent(GameComps.COMP_NPC_STATUS_ENEMY_NEAR);
		}
	}

	private void targetNotVisibleBehavior(IGameEntity ent, TargetComponent targetC, float tpf) {
		targetC.timeNotVisible += tpf;
		TargettingComponent targetting = (TargettingComponent) ent.getComponentWithIdentifier(GameComps.COMP_TARGETTING);
		// Log.debug("Where are you? forgeting in:"+(targetting.timeoutTarget-targetC.timeNotVisible));
		if ((targetC.timeNotVisible >= targetting.timeoutTarget) && (targetting.timeoutTarget >= 0)) {

			undefineTarget(ent, targetC);
		}
	}

	private void undefineTarget(IGameEntity ent, TargetComponent targetC) {
		Log.debug("Forgetting target...");
		ent.removeComponent(targetC);
		ent.removeComponent(GameComps.COMP_NPC_STATUS_ENEMY_NEAR);
		removeTargetVisibleComponent(ent);
	}

	public void noTargetBehaviour(IGameEntity ent, RadarComponent radarC) {
		removeTargetVisibleComponent(ent);
		TargettingComponent targettingC = (TargettingComponent) ent.getComponentWithIdentifier(GameComps.COMP_TARGETTING);
		if (targettingC.isLocked()) {
			return;
		}
		targettingC.lock();
		targettingC.targetList.clear();

		for (IGameEntity target : _targets) {
			checkIfEntityIsEnemy(radarC, ent, target, targettingC.targetList);
		}

		targettingC.unlock();
	}

	public void checkIfEntityIsEnemy(RadarComponent radarC, IGameEntity ent, IGameEntity target, ArrayList<IGameEntity> targetList) {
		if (target.getId() != ent.getId()) {
			if (getRelationBetween(ent, target) == LogicConsts.RELATION_ENEMY) {
				if (isTargetVisible(radarC, ent, target) > 0) {
					targetList.add(target);
				}
			}
		}
	}

	public int isTargetVisible(RadarComponent radarC, IGameEntity ent, IGameEntity target) {
		if (!target.isActive()){
			return -2;
		}
		float distance = ECS.getTargetDistance(ent, target);
		if (distance == -2) {
			return -2;
		}

		if (!isTargetInsideRange(distance, radarC)) {
			// Log.debug("target outside range...");
			return -1;
		}
		PositionComponent posE = (PositionComponent) ent.getComponentWithIdentifier(GameComps.COMP_POSITION);
		PositionComponent posT = (PositionComponent) target.getComponentWithIdentifier(GameComps.COMP_POSITION);
		if (posT == null) {
			Log.error("Target has no position component...");
			return -2;
		}
		if (posE == null) {
			Log.error("Entity has no position component...");
			return -2;
		}
		reuseVector.set(posT.getPos());

		String targetName = GMUtils.getNodeName(target, "");
		String entName = GMUtils.getNodeName(ent, "");
		Geometry checkRayCollision = game.getSnippets().checkRayCollision(posE.getPos(), reuseVector.subtractLocal(posE.getPos()), entName);
		if (checkRayCollision != null) {
			if (checkRayCollision.getName().startsWith(targetName)) {
				return 1;
			}
		}

		return -1;
	}

	public boolean isTargetInsideRange(float distance, RadarComponent radarC) {

		return distance < radarC.detectRange;
	}

}
