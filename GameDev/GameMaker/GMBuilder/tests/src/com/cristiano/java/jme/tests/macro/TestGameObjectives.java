package com.cristiano.java.jme.tests.macro;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.macro.GameObjectiveComponent;
import com.cristiano.java.gm.ecs.comps.macro.VictoryCheckInitComponent;
import com.cristiano.java.gm.ecs.comps.macro.VictoryCheckerComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.LapResourceComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.UnitPositionComponent;
import com.cristiano.java.gm.ecs.systems.macro.VictoryCheckerSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;

public class TestGameObjectives extends MockAbstractTest {
	
	@BeforeClass
	public static void setUpTest() throws IOException {
		startHeadless();
	}

	@Test
	public void testVictoryChecker() {
		VictoryCheckerSystem victorS = initVictoryCheckerSystem();
		
		MapComponent mapC = startMapComponent();
		//loading objective
		GameObjectiveComponent objC = mockGameObjective(mapC);
		assertFalse("No victoryConditions loaded",objC.victoryConditions.isEmpty());
		
		//loading victoryCondition from the map...
		VictoryCheckerComponent victoryCheckerMap = objC.victoryConditions.get(0);
		
		//testing checkerInit...
		GameEntity unitEnt=new GameEntity();
		mockUnit(unitEnt);
		VictoryCheckInitComponent checkerInit=(VictoryCheckInitComponent) entMan.addComponent(GameComps.COMP_VICTORY_CHECKER_INIT, unitEnt);
		victorS.iterateEntity(unitEnt, checkerInit, 0);
		assertFalse(unitEnt.containsComponent(checkerInit));
		assertNotNull("victoryCheckerMap is null",victoryCheckerMap);
		assertTrue(unitEnt.containsComponent(victoryCheckerMap));
		
		//testing condition checker...  TestVictoryCondition	
		
		LapResourceComponent lapC=(LapResourceComponent) entMan.addComponent(GameComps.COMP_RESOURCE_LAP, unitEnt);
		UnitPositionComponent posC=(UnitPositionComponent) entMan.addComponent(GameComps.COMP_RESOURCE_UNIT_POSITION, unitEnt);
		
		assertNotNull("finishConditions null",victoryCheckerMap.finishConditions);
		assertFalse("finishConditions empty",victoryCheckerMap.finishConditions.isEmpty());
		assertFalse("victoryConditions empty",victoryCheckerMap.victoryConditions.isEmpty());
		
		//checking finish condition
		lapC.setCurrValue(1);
		lapC.setMaxValue(2);
		boolean hasFinished=victorS.checkFinishCondition(unitEnt,victoryCheckerMap);
		assertFalse("hasFinished should be false",hasFinished);
		lapC.setCurrValue(2);
		hasFinished=victorS.checkFinishCondition(unitEnt,victoryCheckerMap);
		assertTrue("hasFinished should be false",hasFinished);
		
		//checking victory and losing conditions
		posC.setCurrValue(1);
		boolean isWinner=victorS.checkVictoryCondition(unitEnt,victoryCheckerMap);
		assertTrue("isWinner should be true",isWinner);
		posC.setCurrValue(2);
		isWinner=victorS.checkVictoryCondition(unitEnt,victoryCheckerMap);
		assertFalse("isWinner should be false",isWinner);
		
		assertFalse("Map shouldnt be completed...",mapC.isCompleted);
		victorS.iterateEntity(unitEnt, victoryCheckerMap, 0);
		assertTrue("Map shouldt be completed...",mapC.isCompleted);
		assertFalse("player shouldnt be Victorious...",mapC.playerVictorious);
	}

	@Test
	public void testGameObjectiveCTF() {
		String filtro="captureTheFlag";
		String resource=GameComps.COMP_RESOURCE_FLAG;
		GameObjectiveComponent objective = startGameObjective(filtro);
	}
	
	private GameObjectiveComponent startGameObjective(String filtro) {
		IGameEntity entity=new GameEntity();
		entity.setEntityManager(entMan);
		GameObjectiveComponent objective=mockGameObjective(entity);//(GameObjectiveComponent) entMan.addComponent(GameComps.COMP_GAME_OBJECTIVE,entity);
		assertNotNull("Objetivo nulo",objective);
		assertTrue(objective.victoryConditions.size()>0);
		return objective;
	}
	
	
}
