package com.cristiano.java.gm.ecs.systems.macro;

import java.util.List;

import com.cristiano.benchmark.Bench;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.macro.GameObjectiveComponent;
import com.cristiano.java.gm.ecs.comps.macro.VictoryCheckInitComponent;
import com.cristiano.java.gm.ecs.comps.macro.VictoryCheckerComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.systems.MultiComponentAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.utils.Log;

/**
 * 
 * @author CMM4
 * 
 * 
 *         Test: TestGameObjectives
 */
public class VictoryCheckerSystem extends MultiComponentAbstractSystem {

	public VictoryCheckerSystem() {
		super(GameComps.COMP_VICTORY_CHECKER,GameComps.COMP_VICTORY_CHECKER_INIT);
	}

	@Override
	protected void preTick(float tpf) {
		super.preTick(tpf);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		if (component.getIdentifier().equals(GameComps.COMP_VICTORY_CHECKER)) {
			VictoryCheckerComponent checker = (VictoryCheckerComponent) component;
			Bench.start("VictoryCheckerSystem - checkerForVictory",BenchConsts.CAT_SYSTEM_UPDATE);
			checkerForVictory(ent, checker);
			Bench.end("VictoryCheckerSystem - checkerForVictory");
			return;
		}

		Bench.start("VictoryCheckerSystem - victoryCheckerInit",BenchConsts.CAT_SYSTEM_UPDATE);
		victoryCheckerInit(ent, component);
		Bench.end("VictoryCheckerSystem - victoryCheckerInit");
	}

	private void checkerForVictory(IGameEntity ent, VictoryCheckerComponent checker) {
		Bench.start("VictoryCheckerSystem - checkerForVictory-1",BenchConsts.CAT_SYSTEM_UPDATE);
		boolean checkFinishCondition = checkFinishCondition(ent, checker);
		Bench.end("VictoryCheckerSystem - checkerForVictory-1");
		if (!checkFinishCondition) {
			return;
		}

		Bench.start("VictoryCheckerSystem - checkerForVictory-2",BenchConsts.CAT_SYSTEM_UPDATE);
		MapComponent map = getMap();
		Bench.end("VictoryCheckerSystem - checkerForVictory-2");
		map.isCompleted = true;
		
		Bench.start("VictoryCheckerSystem - checkerForVictory-3",BenchConsts.CAT_SYSTEM_UPDATE);
		map.playerVictorious = checkVictoryCondition(ent, checker);
		Bench.end("VictoryCheckerSystem - checkerForVictory-3");
		
		Log.info("Map is completed... Is player victorious?" + map.playerVictorious);
	}

	// this will take the victorychecker from the objectiveComponent in the map
	// and apply to the entity
	private void victoryCheckerInit(IGameEntity ent, IGameComponent component) {
		if (component.getIdentifier().equals(GameComps.COMP_VICTORY_CHECKER_INIT)) {
			VictoryCheckInitComponent comp = (VictoryCheckInitComponent) component;
			loadVictoryCheckerFromMap(getMap(), ent);
			ent.removeComponent(comp);
		}
	}

	private void loadVictoryCheckerFromMap(MapComponent map, IGameEntity ent) {
		if (!isPlayer(ent)){
			return;
		}
		List<IGameComponent> objectives = map.getComponentsWithIdentifier(GameComps.COMP_GAME_OBJECTIVE);
		for (IGameComponent comp : objectives) {
			GameObjectiveComponent obj = (GameObjectiveComponent) comp;
			for (VictoryCheckerComponent vc : obj.victoryConditions) {
				ent.attachComponent(vc);
			}
		}

	}

	public boolean checkFinishCondition(IGameEntity unitEnt, VictoryCheckerComponent checker) {
		return checker.checkFinishConditions(unitEnt);
	}

	public boolean checkVictoryCondition(IGameEntity unitEnt, VictoryCheckerComponent checker) {
		return checker.checkVictoryConditions(unitEnt);
	}
}

// em gameopposition adicionar victoryChecker em scope... ok
// - adicionar os victorycheckers do GameObjectiveComponent do mapa no scopo
// especificado. ok
// - usar um componente dummy para iniciar a carga dos componentes em
// AbstractMacroSystem. ok