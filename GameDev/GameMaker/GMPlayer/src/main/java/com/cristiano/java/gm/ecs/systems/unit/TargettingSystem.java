package com.cristiano.java.gm.ecs.systems.unit;

import java.util.ArrayList;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.comps.unit.TargettingComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.utils.Log;

/*O que fazer quando a entidade morre? Talvez tocar algum som? Animacao?*/
public class TargettingSystem extends JMEAbstractSystem {

	public TargettingSystem() {
		super(GameComps.COMP_TARGETTING);

	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		TargettingComponent comp = (TargettingComponent) component;

		TargetComponent targetC = (TargetComponent) ent.getComponentWithIdentifier(GameComps.COMP_TARGET);
		if (isPlayer(ent)) {
			initPlayerTargetting(comp);
			playerBehaviour(ent,comp,targetC);
			return;
		}
		if (targetC == null) {
			noTargetBehaviour(ent, comp);
		}

	}

	private void playerBehaviour(IGameEntity ent, TargettingComponent comp, TargetComponent targetC) {
		if (comp.targetList.size() == 0) {
			return;
		}
		if (comp.isLocked()) {
			return;
		}
		comp.lock();
		sortTargetsList(ent, comp, comp.targetList);
		IGameEntity newTarget = comp.targetList.get(0);
		boolean changeTarget = checkIfShouldChangeTarget(targetC, newTarget);
		if (changeTarget){
			ECS.defineTarget(entMan,ent, newTarget);
		}
		comp.targetList.clear();
		comp.unlock();
	}

	private boolean checkIfShouldChangeTarget(TargetComponent targetC, IGameEntity newTarget) {
		boolean changeTarget=false;
		if (targetC==null){
			changeTarget=true;
		} else {
			if (targetC.target!=newTarget){
				changeTarget=true;
			}
		}
		return changeTarget;
	}

	private void noTargetBehaviour(IGameEntity ent, TargettingComponent comp) {
		if (comp.targetList.size() == 0) {
			return;
		}
		if (comp.isLocked()) {
			return;
		}
		comp.lock();
		// if theres only one possible target then no need to go further...
		if (comp.targetList.size() == 1) {
			IGameEntity target = comp.targetList.get(0);
			ECS.defineTarget(entMan,ent, target);
			comp.targetList.clear();
			comp.unlock();
			return;
		}

		sortTargetsList(ent, comp, comp.targetList);

		IGameEntity target = comp.targetList.get(0);
		ECS.defineTarget(entMan,ent, target);
		comp.targetList.clear();
		comp.unlock();

	}

	public void sortTargetsList(IGameEntity ent, TargettingComponent targettingC, ArrayList<IGameEntity> targetList) {
		if (targetList.size() <= 1) {
			return;
		}
		if (targettingC.selectionType.equals(LogicConsts.TARGETTING_RANDOM)) {
			return;
		}
		if (targettingC.selectionType.equals(LogicConsts.TARGETTING_NEAR)) {
			sortTargetsListNearer(ent, targetList);
		} else if (targettingC.selectionType.equals(LogicConsts.TARGETTING_FAR)) {
			sortTargetsListFar(ent, targetList);
		} else if (targettingC.selectionType.equals(LogicConsts.TARGETTING_STRONGER)) {
			sortTargetsListStrong(ent, targetList);
		} else if (targettingC.selectionType.equals(LogicConsts.TARGETTING_WEAK)) {
			sortTargetsListWeak(ent, targetList);
		} else {
			Log.error("Unknown targetting type:" + targettingC.selectionType);
		}
	}

	public void sortTargetsListWeak(IGameEntity ent, ArrayList<IGameEntity> targetList) {
		for (int i = 0; i < targetList.size() - 1; i++) {
			for (int j = i + 1; j < targetList.size(); j++) {
				IGameEntity entI = targetList.get(i);
				IGameEntity entJ = targetList.get(j);

				float hpI = ECS.getHealthPoints(entI);
				float hpJ =ECS.getHealthPoints(entJ);
				if (hpI > hpJ) {
					targetList.set(i, entJ);
					targetList.set(j, entI);
				}
			}
		}
	}

	public void sortTargetsListStrong(IGameEntity ent, ArrayList<IGameEntity> targetList) {
		for (int i = 0; i < targetList.size() - 1; i++) {
			for (int j = i + 1; j < targetList.size(); j++) {
				IGameEntity entI = targetList.get(i);
				IGameEntity entJ = targetList.get(j);

				float hpI = ECS.getHealthPoints(entI);
				float hpJ = ECS.getHealthPoints(entJ);
				if (hpI < hpJ) {
					targetList.set(i, entJ);
					targetList.set(j, entI);
				}
			}
		}
	}

	public void sortTargetsListNearer(IGameEntity ent, ArrayList<IGameEntity> targetList) {
		for (int i = 0; i < targetList.size() - 1; i++) {
			for (int j = i + 1; j < targetList.size(); j++) {
				IGameEntity entI = targetList.get(i);
				IGameEntity entJ = targetList.get(j);
				float distanceI = ECS.getTargetDistance(ent, entI);
				float distanceJ = ECS.getTargetDistance(ent, entJ);
				if (distanceI > distanceJ) {
					targetList.set(i, entJ);
					targetList.set(j, entI);
				}
			}
		}

	}

	public void sortTargetsListFar(IGameEntity ent, ArrayList<IGameEntity> targetList) {
		for (int i = 0; i < targetList.size() - 1; i++) {
			for (int j = i + 1; j < targetList.size(); j++) {
				IGameEntity entI = targetList.get(i);
				IGameEntity entJ = targetList.get(j);
				float distanceI = ECS.getTargetDistance(ent, entI);
				float distanceJ = ECS.getTargetDistance(ent, entJ);
				if (distanceI < distanceJ) {
					targetList.set(i, entJ);
					targetList.set(j, entI);
				}
			}
		}

	}
	private void initPlayerTargetting(TargettingComponent comp) {
		if (comp.isFirstTick()) {
			comp.selectionType = LogicConsts.TARGETTING_NEAR;
			comp.firstTick = false;
		}
	}
}
