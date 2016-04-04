package com.cristiano.java.jme.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.DamageOverTimeComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TimerComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.BatteryComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.systems.DamageCalculatorSystem;
import com.cristiano.java.gm.ecs.systems.DamageOverTimeSystem;
import com.cristiano.java.gm.ecs.systems.TimerSystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.BatterySystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.HealthSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.GameState;

public class TestEntitySystem extends MockAbstractTest {

	DamageCalculatorSystem dmgCalcS;
	BatterySystem battS;

	
	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	@Test
	public void testSystemStage() {
		BatterySystem system = initBatterySystem();
		system.systemStageRequirements=GameState.STG_INIT+GameState.STG_PAUSED;
		GameState.currentStage=GameState.STG_INIT;
		assertTrue(system.checkGameStage());
		GameState.currentStage=GameState.STG_PAUSED;
		assertTrue(system.checkGameStage());
		GameState.currentStage=GameState.STG_RUNNING;
		assertFalse(system.checkGameStage());
		GameState.currentStage=GameState.STG_START;
		assertFalse(system.checkGameStage());
		
		system.systemStageRequirements=GameState.STG_RUNNING;
		GameState.currentStage=GameState.STG_INIT;
		assertFalse(system.checkGameStage());
		GameState.currentStage=GameState.STG_PAUSED;
		assertFalse(system.checkGameStage());
		GameState.currentStage=GameState.STG_RUNNING;
		assertTrue(system.checkGameStage());
		GameState.currentStage=GameState.STG_START;
		assertFalse(system.checkGameStage());
		
		system.systemStageRequirements=GameState.STG_START+GameState.STG_RUNNING;
		GameState.currentStage=GameState.STG_INIT;
		assertFalse(system.checkGameStage());
		GameState.currentStage=GameState.STG_PAUSED;
		assertFalse(system.checkGameStage());
		GameState.currentStage=GameState.STG_RUNNING;
		assertTrue(system.checkGameStage());
		GameState.currentStage=GameState.STG_START;
		assertTrue(system.checkGameStage());
	}
	
	@Test
	public void testEntityComponent() {
		IGameEntity ent = entMan.createEntity();
		
		String compIdent = GameComps.COMP_RESOURCE_HEALTH;
		IGameComponent comp = entMan.addComponent(compIdent,ent);
		
		entMan.cleanup();
		//ent contem comp...
		List<IGameEntity> entsDirect = entMan.getEntitiesWithComponentDirect(compIdent,new ArrayList<IGameEntity>());
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(compIdent,new ArrayList<IGameEntity>());
		
		assertTrue(entsDirect.size()==ents.size());
		assertTrue(entsDirect.contains(ent));
		assertTrue(ents.contains(ent));
		assertTrue(entsDirect.contains(ent));
		
		//removendo comp (lista deve retornar vazia
		entMan.removeComponent(comp);
		entMan.cleanup();
		
		ents = entMan.getEntitiesWithComponent(compIdent,new ArrayList<IGameEntity>());
		entsDirect = entMan.getEntitiesWithComponentDirect(compIdent,new ArrayList<IGameEntity>());
		
		assertTrue(entsDirect.size()==ents.size());
		assertTrue(entsDirect.size()==0);
		
		//readicionando o component
		comp = entMan.addComponent(compIdent,ent);
		entMan.cleanup();
		
		ents = entMan.getEntitiesWithComponent(compIdent,new ArrayList<IGameEntity>());
		entsDirect = entMan.getEntitiesWithComponentDirect(compIdent,new ArrayList<IGameEntity>());
		
		assertTrue(entsDirect.size()==ents.size());
		assertTrue(entsDirect.size()==1);
		
		//removendo a entity
		entMan.removeEntity(ent);
		ents = entMan.getEntitiesWithComponent(compIdent,new ArrayList<IGameEntity>());
		entsDirect = entMan.getEntitiesWithComponentDirect(compIdent,new ArrayList<IGameEntity>());
		
		assertTrue(entsDirect.size()==ents.size());
		assertTrue(entsDirect.size()==0);
		
	}

	@Test
	public void testComponentReUse() {
		//entMan.addEntity(entity);
		entMan.addComponent(GameComps.COMP_REUSE_MANAGER, entity);
		
		HealthSystem anySystem = initHealthSystem();

		IGameComponent reusableComponent = anySystem.getReusableComponent(GameComps.COMP_RESOURCE_HEALTH,false);
		assertNull("There was some component already...",reusableComponent);
		
		IGameComponent anyComponent = entMan.addComponent(GameComps.COMP_RESOURCE_HEALTH, entity);
		IGameComponent anyOtherComponent = entMan.addComponent(GameComps.COMP_RESOURCE_DPS, entity);
		assertNotNull("anyComponent null",anyComponent);
		assertNotNull("anyOtherComponent null",anyOtherComponent);
		entMan.cleanup();
		anySystem.removeReusableComponent(anyComponent);
		anySystem.removeReusableComponent(anyOtherComponent);
		assertTrue(entMan.containsEntity(entity));
		assertFalse(entity.containsComponent(anyComponent));
		assertFalse(entity.containsComponent(anyOtherComponent));
		
		reusableComponent = anySystem.getReusableComponent(GameComps.COMP_RESOURCE_HEALTH,false);
		assertTrue("Reusable returned wrong component...",reusableComponent==anyComponent);
		reusableComponent =anySystem.getReusableComponent(GameComps.COMP_RESOURCE_HEALTH,false);
		assertNull("There was some component already after removing...",reusableComponent);
		
		reusableComponent =anySystem.getReusableComponent(GameComps.COMP_RESOURCE_HEALTH,true);
		assertNotNull("The component wasnt generated...",reusableComponent);
		assertTrue(reusableComponent.getIdentifier().equals(GameComps.COMP_RESOURCE_HEALTH));
		
		entity.attachComponent(anyComponent);
		assertTrue(entity.containsComponent(anyComponent));
		anySystem.removeReusableComponent(anyComponent);
		assertFalse(entity.containsComponent(anyComponent));
	}

	@Test
	public void testTimerSystem() {
		IGameEntity entity=entMan.createEntity();
		
		TimerComponent timerComp = (TimerComponent) entMan.addComponent(GameComps.COMP_TIMER, entity);
		IGameComponent anyComponent = entMan.addComponent(GameComps.COMP_RESOURCE_HEALTH, timerComp);
		timerComp.limitTime=60;
		assertFalse(entity.containsComponent(anyComponent));
		TimerSystem timerS = initTimerSystem();
		timerS.iterateEntity(entity, timerComp, 1);
		assertFalse(entity.containsComponent(anyComponent));
		timerS.iterateEntity(entity, timerComp, 10);
		assertFalse(entity.containsComponent(anyComponent));
		assertTrue(entity.containsComponent(timerComp));
		timerS.iterateEntity(entity, timerComp, 70);
		assertTrue(entity.containsComponent(anyComponent));
		assertFalse(entity.containsComponent(timerComp));
	}

	@Test
	public void testEntitySystem() {
		String compIdent = "teste";
		GameComponent comp1 = new GameComponent(compIdent) {

			@Override
			public IGameComponent clonaComponent() {
				return null;
			}
			@Override
			public void resetComponent() {
			}
			@Override
			protected boolean exportComponentToJSON(JSONObject obj) {
				return true;
			}
			@Override
			protected void importComponentFromJSON(JSONObject obj) {
				// TODO Auto-generated method stub
				
			}
			
		};
		String compClass = comp1.getIdentifier();
		comp1.setEntityManager(entMan);

		entMan.addEntity(entity);
		entMan.addComponent(comp1, entity);
		entMan.cleanup();
		
		//Testa getComponentsWithIdentifier
		List<IGameComponent> listComps = entMan.getComponentsWithIdentifier(compIdent);
		assertNotNull("listComps retornou nula", listComps);
		assertTrue(listComps.size()==1);
		assertTrue("lista nao contem comp1", listComps.contains(comp1));


		List<?> listRet = entMan.getEntitiesWithComponent(compClass);
		assertNotNull("lista retornou nula", listRet);
		assertTrue("lista retornou vazia", listRet.size() == 1);
		assertTrue("lista nao contem ent1", listRet.contains(entity));

		assertTrue(entity.containsComponent(comp1));
		int size = entMan.size();
		assertTrue("Qtd de entities =0:" + size, size > 0);
		
		IGameComponent compRet = entity.getComponentWithIdentifier(comp1.getIdentifier());
		assertTrue("compRet<>comp1: " + compRet, compRet == comp1);
		
		entMan.removeEntity(entity);
		entMan.cleanup();
		int novoSize = entMan.size();
		assertTrue("Qtd de entities não diminuiu apos remover entidade:" + novoSize, novoSize < size);

	}
/*
	@Test
	public void testMeshLoaderSystem() {
		GameEntity entity = new GameEntity();
		entMan.addEntity(entity);
		UIActionComponent actionComp = (UIActionComponent) entMan.addComponent(GameComps.COMP_UI_ACTION, entity);
		actionComp.auxInfo = "testBox";
		actionComp.action = GameActions.ACTION_LOAD_ENTITY;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<IGameEntity> childrenOf = entMan.getChildrenOf(entity);
		assertTrue("entidade n�o foi gerada", childrenOf.size() > 0);

	}*/

	@Test
	public void testHealthSystem() {
		IGameEntity ent=entMan.createEntity();
		mockUnit(ent);
		HealthComponent healthC = startHealthComponent(ent);
		
		assertNotNull("healthC nulo", healthC);
		verificaGetEntities(healthC,ent);

		HealthSystem healthS = initHealthSystem();
		healthS.iterateEntity(ent, healthC,1);
		assertTrue("healthC nao esta vivo!", healthC.alive);
		healthC.setCurrValue(0);
		healthS.iterateEntity(ent, healthC,1);
		assertFalse("healthC is alive when shouldnt!", healthC.alive);
	}

	

	@Test
	public void testBatterySystem() {
		BatteryComponent batt = new BatteryComponent();
		batt.setMaxValue(100);
		batt.setCurrValue(50);
		batt.rechargeSec = 10;
		batt.setEntityManager(entMan);
		entMan.addComponent(batt, entity);

		battS=initBatterySystem();
		verificaGetEntities(batt,entity);

		battS.iterateEntity(entity,batt,1);
		assertTrue("Bateria nao recarregou o quanto deveria:" + batt.getCurrValue(), batt.getCurrValue() == 60);
		battS.iterateEntity(entity,batt,0.5f);
		assertTrue("Bateria nao recarregou o quanto deveria:" + batt.getCurrValue(), batt.getCurrValue() == 65);
	}

	@Test
	public void testDamageOverTime() {
		IGameEntity ent=entMan.createEntity();
		mockUnit(ent);
		HealthComponent healthC = (HealthComponent) ent.getComponentWithIdentifier(GameComps.COMP_RESOURCE_HEALTH);
		healthC.currValue=100;
		healthC.setMaxValue(100);
		entMan.addComponent(healthC, ent);

		DamageOverTimeComponent dotC = (DamageOverTimeComponent) entMan.spawnComponent(GameComps.COMP_DAMAGE_OVER_TIME);
		dotC.damageLeft = 10;
		dotC.damageSecond = 2;
		entMan.addComponent(dotC, ent);

		verificaGetEntities(dotC,ent);
		// entMan.update(1);
		DamageOverTimeSystem dotS = initDamageOverTimeSystem();
		DamageCalculatorSystem dmgCalcS = initDamageCalculatorSystem();
		iterateDot(dotC, dotS, dmgCalcS, 1,ent);

		assertTrue("dotC nao diminuiu o dano restante!" + dotC.damageLeft, dotC.damageLeft < 10);
		float hp = healthC.getCurrValue();
		iterateDot(dotC, dotS, dmgCalcS, 0.5f,ent);
		assertTrue("healthComponent nao diminuiu o hp:" + healthC.getCurrValue(), healthC.getCurrValue() < hp);
		iterateDot(dotC, dotS, dmgCalcS, 100,ent);

		List<?> listRet = entMan.getEntitiesWithComponent(dotC.getIdentifier());
		assertTrue("dotC nao zerou:" + dotC.damageLeft, dotC.damageLeft == 0);
		assertTrue("dotC exaurido mas nao foi removido da lista:" + listRet.size(), listRet.size() == 0);
		assertTrue("healthComponent nao diminuiu o hp apOs todo o DOT:" + healthC.getCurrValue(),
				healthC.getCurrValue() == (healthC.getMaxValue() - 10));
	}

	private void iterateDot(DamageOverTimeComponent dotC, DamageOverTimeSystem dotS, DamageCalculatorSystem dmgCalcS,
			float tpf, IGameEntity ent) {
		dotS.iterateEntity(ent, dotC, tpf);
		IGameComponent dmgRec = ent.getComponentWithIdentifier(GameComps.COMP_DAMAGE_RECEIVED);
		assertNotNull("No dmgReceived generated", dmgRec);
		dmgCalcS.iterateEntity(ent, dmgRec, tpf);
	}

	private void verificaGetEntities(IGameComponent comp, IGameEntity ent) {
		entMan.cleanup();
		List<?> listRet = entMan.getEntitiesWithComponent(comp.getIdentifier());
		List<?> listRet2 = entMan.getEntitiesWithComponent(comp);
		assertNotNull("lista retornou nula (" + comp.getIdentifier() + ")", listRet);
		assertTrue("lista2 (" + comp.getIdentifier() + ") retornou vazia:" + listRet2.size(), listRet2.size() >= 1);
		assertTrue("lista (" + comp.getIdentifier() + ") retornou vazia:" + listRet.size(), listRet.size() >= 1);
		assertTrue("lista (" + comp.getIdentifier() + ") nao contem ent1", listRet.contains(ent));
		assertTrue("lista2 (" + comp.getIdentifier() + ") nao contem ent1", listRet2.contains(ent));

		// assertTrue("listas n�o possuem os mesmos componentes..",listRet.get(0)==listRet2.get(0));
	}

}
