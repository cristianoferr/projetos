package com.cristiano.java.jme.tests.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.unit.AIComponent;
import com.cristiano.java.gm.ecs.comps.unit.npcBehaviours.BehaviourFollowWaypointComponent;
import com.cristiano.java.gm.ecs.comps.unit.npcConditions.ArmedAndEnemyNearCondition;
import com.cristiano.java.gm.ecs.comps.unit.npcConditions.HealthPackNearSafeCondition;
import com.cristiano.java.gm.ecs.comps.unit.npcStates.AbstractNPCStateComponent;
import com.cristiano.java.gm.ecs.comps.unit.npcStates.NPCStateRaceDriver;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.LapResourceComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.RaceGoalComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.systems.macro.QueryBestiarySystem;
import com.cristiano.java.gm.ecs.systems.unit.AISystem;
import com.cristiano.java.gm.ecs.systems.unit.npcStates.NPCStateManagerSystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.HealthSystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.RaceGoalSystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.UnitPositionSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameSystem;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class TestNPCStuff extends MockAbstractTest{
	private static final Vector3f SECOND_POS = new Vector3f(-100,0,100);
	private static final Vector3f INITIAL_POS = new Vector3f(100,0,100);

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}

	
	@Test public void testNPCRaceDriver() {
	
		entMan.cleanup();
		
		IGameEntity aiEntity = configureRacer();
		AIComponent stateCheck=ECS.getAIComp(aiEntity);
		
		MapComponent mapC = mockRaceMap();
		
		AISystem aiSystem=initAISystem();
		
		aiSystem.iterateEntity(aiEntity, stateCheck, 0);
		RaceGoalComponent goal = (RaceGoalComponent) entMan.addComponent(GameComps.COMP_RESOURCE_RACE_GOAL, aiEntity);

		NPCStateManagerSystem stateSystem = initNPCStateManagerSystem();
		
		NPCStateRaceDriver stateRaceDriver=(NPCStateRaceDriver) entMan.addComponent(GameComps.COMP_NPC_RACE_DRIVER, aiEntity);
		stateRaceDriver.loadFromElement(mockNPCStateRaceDriver());
		
		stateRaceDriver.states=stateCheck;
		stateRaceDriver.stateTick(aiEntity,stateSystem);
		
		//ai should be on this behaviour...
		BehaviourFollowWaypointComponent followBehav=(BehaviourFollowWaypointComponent) aiEntity.getComponentWithIdentifier(GameComps.COMP_BEHAVIOR_FOLLOW_WAYPOINT);
		
		assertNotNull("FollowBehaviour is null",followBehav);
		
	}
	
	 
	
	

	@Test public void testRaceLapPosition() {
		MapComponent mapC = mockRaceMap();
		
		IGameEntity entity1 = configureRacer();
		IGameEntity entity3 = configureRacer();
		IGameEntity entity2 = configureRacer();

		RaceGoalComponent goal1 = ECS.getRacegoalComponent(entity1);
		assertNotNull(goal1);
		RaceGoalComponent goal2 = ECS.getRacegoalComponent(entity2);
		assertNotNull(goal2);
		assertTrue(goal1!=goal2);
		RaceGoalComponent goal3 = ECS.getRacegoalComponent(entity3);
		
		RaceGoalSystem goalS = initRaceGoalSystem();
		UnitPositionSystem unitPosS = initUnitPositionSystem();
		
		goalS.iterateEntity(entity1, goal1, 0);
		assertNull(goal1.currDestination);
		goalS.iterateEntity(entity1, goal1, 0);
		assertNotNull(goal1.currDestination);
		goalS.iterateEntity(entity2, goal2, 0);
		goalS.iterateEntity(entity1, goal2, 0);
		goalS.iterateEntity(entity3, goal3, 0);
		goalS.iterateEntity(entity3, goal3, 0);
		
		
		
		float dist=goalS.calcDistanceWaypoint(goal1);
		assertTrue(dist>0);
		
		PositionComponent pos1=ECS.getPositionComponent(entity1);
		pos1.setPos(INITIAL_POS.add(SECOND_POS).mult(0.5f));
		
		//entity1 is closer to 1st waypoint
		assertFalse(unitPosS.isEntityAhead(entity2,entity1));
		assertTrue(unitPosS.isEntityAhead(entity1,entity2));
		
		//next waypoint
		goal2.incValue();
		assertTrue(unitPosS.isEntityAhead(entity2,entity1));
		assertFalse(unitPosS.isEntityAhead(entity1,entity2));
		
		//next lap
		LapResourceComponent lap1 = ECS.getLapResource(entity1);
		lap1.incValue();
		assertFalse(unitPosS.isEntityAhead(entity2,entity1));
		assertTrue(unitPosS.isEntityAhead(entity1,entity2));
		
		assertTrue(entity1.containsComponent(GameComps.COMP_RESOURCE_UNIT_POSITION));
		assertTrue(entMan.containsEntity(entity1));
		entMan.cleanup();
		IGameEntity[] testOrdering = unitPosS.testOrdering();
		assertTrue("Size differ:"+testOrdering.length,testOrdering.length==3);
		assertTrue(testOrdering[0]==entity1);
		assertTrue(testOrdering[1]==entity2);
		assertTrue(testOrdering[2]==entity3);
	}


	private IGameEntity configureRacer() {
		IGameEntity entity1 = entMan.createEntity();
		AIComponent stateCheck1 = mockAIUnit(entity1);
		assertNotNull(stateCheck1);
		
		PositionComponent posC = ECS.getPositionComponent(entity1);
		posC.setPos(INITIAL_POS);
		entMan.addIfNotExistsComponent(GameComps.COMP_RESOURCE_LAP, entity1);
		entMan.addIfNotExistsComponent(GameComps.COMP_RESOURCE_UNIT_POSITION, entity1);
		
		RaceGoalComponent goal = (RaceGoalComponent) entMan.addComponent(GameComps.COMP_RESOURCE_RACE_GOAL, entity1);
		goal.arrowTag=TestStrings.TAG_RACE_GOAL_ARROW;
		goal.arrowX=0.5f;
		goal.arrowScale=0.5f;
		goal.idealDistance=20;
		goal.arrowY=0.5f;
		goal.arrowDistance=20;
		
		return entity1;
	}

	@Test public void testRaceGoalSystem() {
		entMan.cleanup();
		
		MapComponent mapC = mockRaceMap();
		IGameEntity aiEntity = configureRacer();
		
		entMan.addComponent(GameComps.COMP_PLAYER, aiEntity);
		
		RaceGoalComponent goal = ECS.getRacegoalComponent(aiEntity);
		assertNotNull(goal);
		
		RaceGoalSystem goalS = initRaceGoalSystem();
		assertTrue("Waypoints already generated.",goal.isEmpty());
		
		//first iteration... 
		//before:no waypoints defined 
		//after:waypoints defined, goal._entifyPos defined
		goalS.iterateEntity(aiEntity, goal, 0);
		
		checkArrowGeneration(aiEntity,goalS,goal);
		
		
		int wayptsQtd=goal.waypoints.size();
		assertNotNull("EntityPos not defined...",goal._entityPos);
		assertTrue("waypts should be >0",wayptsQtd>0);
		
		//2nd iteration
		//after:goal.currDestination!=null
		goalS.iterateEntity(aiEntity, goal, 0);
		assertNotNull("currDestination not defined...",goal.currDestination);
		
		//3rd iteration
		//a waypoint should be removed...
		goal._entityPos.setPos(goal.currDestination);
		goalS.iterateEntity(aiEntity, goal, 0);
		int wayptsQtdAfter=goal.waypoints.size();
		assertTrue("Waypts didnt vary after reaching goal:"+wayptsQtdAfter,wayptsQtdAfter==wayptsQtd-1);
		
		RoomComponent nextRoom = ECS.getRoomWithId(mapC,1);
		assertNotNull("nextRoom is null",nextRoom);
		assertFalse("No waypoints generated.",goal.isEmpty());
		
		//ECS.getr
		List<IGameComponent> rooms = ECS.getRooms(mapC);
		
		
		LapResourceComponent lapC = ECS.getLapResource(aiEntity);
		assertTrue("lap is not 0:"+lapC.currValue,lapC.currValue==0);
		
		float val=goal.getCurrValue();
		goalS.calculateWaypoints(aiEntity,  goal);
		assertTrue(goal.getCurrValue()==2);
		assertTrue(lapC.currValue==0);
		goalS.calculateWaypoints(aiEntity,  goal);
		assertTrue(lapC.currValue==0);
		assertTrue(goal.getCurrValue()==3);
		
		goalS.calculateWaypoints(aiEntity,  goal);
		assertTrue(lapC.currValue==1);
		assertTrue("Current value should be 1 but is "+goal.getCurrValue(),goal.getCurrValue()==0);
		
	}
	
	private void checkArrowGeneration(IGameEntity aiEntity, RaceGoalSystem goalS, RaceGoalComponent goal) {
		//verificar a logica do querybestiarysystem, garantir que a entity seja carregada com a tag correta
		assertNotNull("arrowQuery is null!",goal.arrowQuery);
		QueryBestiarySystem queryS = initQueryBestiarySystem();
		queryS.iterateEntity(aiEntity, goal.arrowQuery, 0);
		
		
		goalS.iterateEntity(aiEntity, goal, 0);
		assertNotNull("arrow didnt load",goal.arrowEntity);
		assertTrue("arrow didnt load LOAD_ENTITY",goal.arrowEntity.containsComponent(GameComps.COMP_CHILD));
		assertTrue("arrow didnt load position",goal.arrowEntity.containsComponent(GameComps.COMP_POSITION));
		
		//assertTrue("Arrow entity dont have GUI Component...",goal.arrowEntity.containsComponent(GameComps.COMP_GUI));
		
		goalS.iterateEntity(aiEntity, goal, 0);
		//assertNotNull("arrow Orientation null",goal.arrowOrientation);
		//TODO: falta carregar a mesh e adicionar na tela 
	}


	private MapComponent mockRaceMap() {
		MapComponent mapC = startMapComponent();
		
		mapC.removeComponent(GameComps.COMP_ROOM);
		
		Vector3f[] points=new Vector3f[]{INITIAL_POS,SECOND_POS,new Vector3f(-100,0,-100),new Vector3f(100,0,-100)};
		
		RoomComponent[] rooms=new RoomComponent[points.length];
		
		RoomComponent prevRoom=null;
		for (int i=0;i<points.length;i++){
			RoomComponent room = mockRoom(mapC);
			rooms[i]=room;
			room.setPosition(points[i]);
			room.roomId=i;
			if (prevRoom!=null){
				RoadComponent road = ComponentRecipes.linkRooms(entMan,room,prevRoom);
				addTestWayPoints(mapC,road,room,prevRoom);
				
				RoadComponent roadTest=ECS.findRoadInCommon(room, prevRoom);
				assertTrue("roads differ",roadTest==road);
			}
			prevRoom=room;
		}
		
		//linking first and last rooms (closed circle)
		RoadComponent road = ComponentRecipes.linkRooms(entMan,rooms[0],rooms[points.length-1]);
		addTestWayPoints(mapC,road,rooms[0],rooms[points.length-1]);
		
		return mapC;
	}


	

	private void addTestWayPoints(MapComponent mapC, RoadComponent road, RoomComponent room0, RoomComponent room1) {
		ComponentRecipes.addWaypoint(game.getSnippets(),entMan,mapC,room0.getPosition(),road);
		ComponentRecipes.addWaypoint(game.getSnippets(),entMan,mapC,room0.getPosition().add(room1.getPosition()).mult(0.5f),road);
		
	}


	@Test public void testNPCStates() {
		IGameEntity ent=entMan.createEntity();
		entMan.cleanup();
		AIComponent dataComp = mockAIUnit(ent);
		//entMan.addComponent(GameComps.COMP_AI, ent);
		
		//NPCStateCheckerComponent dataComp = initStateDataComponent();
		IGameSystem checkerSystem = initAISystem();
		checkerSystem.iterateEntity(ent, dataComp, 0);
		
		verificaSistema(ent,dataComp,"NPCStateManagerSystem",GameComps.COMP_NPC_STATE_ATTACK);
		/*for (GenericElement elState:dataComp.elStates){
			String identifier = elState.getIdentifier();
		}*/
	}
	
	
	
	private void verificaSistema(IGameEntity ent,AIComponent dataComp, String systemIdent, String compIdent) {
		ent.attachComponent(dataComp);

		Log.debug("Testing "+systemIdent);
		assertTrue("NPCStates is empty!",dataComp.npcStates.size()>0);
		NPCStateManagerSystem system=initNPCStateManagerSystem();
		system.initSystem(entMan, null);
		AbstractNPCStateComponent component = (AbstractNPCStateComponent) dataComp.getTemplateStateWithIdentifier(compIdent);
		entMan.addComponent(component, ent);
		assertNotNull("System not found:"+systemIdent,system);
		assertNotNull("Component Not Found:"+compIdent,component);
		
		verificaConditions(dataComp,system, component);
		
		
	}

	private void verificaConditions(AIComponent conditionDataComp, NPCStateManagerSystem system,
			AbstractNPCStateComponent component) {
		system.iterateEntity(entity, component, 1);
		if (component.entryConditions==null){
			assertTrue("The state conditions are false when it should be ok(empty)",system.stateConditionsMet(entity,component.entryConditions));
		} else {
			assertFalse("The state conditions are ok when it should be false(filled)",system.stateConditionsMet(entity,component.entryConditions));
			String conditionIdent=component.entryConditions[0];
			IGameComponent conditionComp = conditionDataComp.getTemplateConditionWithIdentifier(conditionIdent);
			assertNotNull(conditionIdent +" was not found in condition IGameElements.",conditionComp);
			entity.attachComponent(conditionComp);
			system.iterateEntity(entity, component, 1);
			assertTrue("The state conditions are false when it should be ok(filled)",system.stateConditionsMet(entity,component.entryConditions));
		}
	}

	@Test public void testHealthSystemActivatingLowHealthStatus() {
		IGameEntity entity=entMan.createEntity();
		entMan.cleanup();
		
		HealthSystem healthS=new HealthSystem();
		healthS.initSystem(entMan, null);
		HealthComponent healthC=new HealthComponent();
		healthC.setCurrValue(10);
		healthC.setMaxValue(100);
		healthC.lowHealthThreshold=0.5f;
		
		assertFalse(entity.containsComponent(GameComps.COMP_NPC_STATUS_LOW_HEALTH));
		healthS.iterateEntity(entity, healthC, 1);
		entMan.cleanup();
		assertTrue("entity doesnt have lowHealthStatus...",entity.containsComponent(GameComps.COMP_NPC_STATUS_LOW_HEALTH));
		healthC.setCurrValue(100);
		entMan.cleanup();
		healthS.iterateEntity(entity, healthC, 1);
		assertFalse("entity has lowHealthStatus when it shouldnt...",entity.containsComponent(GameComps.COMP_NPC_STATUS_LOW_HEALTH));
		
	}
	
	@Test public void testConditionComponents() {
		IGameEntity entity=entMan.createEntity();
		entMan.cleanup();
		
		ArmedAndEnemyNearCondition c1 = new ArmedAndEnemyNearCondition();
		c1.loadFromElement(mockGenericComponentElement(new String[]{GameComps.TAG_NPC_CONDITION}));
		entMan.addComponent(c1, entity);
		
		HealthPackNearSafeCondition c2 = new HealthPackNearSafeCondition();
		c2.loadFromElement(mockGenericComponentElement(new String[]{GameComps.TAG_NPC_CONDITION}));
		entMan.addComponent(c2, entity);
		
		IGameComponent c3 = entMan.addComponent(GameComps.COMP_NPC_CONDITION_LOW_HP_ENEMY_NEAR, entity);
		c3.loadFromElement(mockGenericComponentElement(new String[]{GameComps.TAG_NPC_CONDITION}));
		
		List<IGameComponent> comps = entity.getComponents(GameComps.TAG_NPC_CONDITION);
		assertTrue("Tamanho da array com condicoes difere de 3:"+comps.size(),comps.size()==3);
		
		entity.removeAllComponents();entMan.cleanup();
		
		AISystem condCheckerS=initAISystem();
		condCheckerS.initSystem(entMan, null);
		
		
		assertFalse("UnarmedAndEnemyNearCondition was activated before anything...",entity.containsComponent(GameComps.COMP_NPC_CONDITION_UNARMED_ENEMY_NEAR));
		entMan.addComponent(GameComps.COMP_AI, entity);
		
		AIComponent condCheckerC = mockAIUnit(entity);
		
		entMan.addComponent(GameComps.COMP_NPC_STATUS_LOW_HEALTH, entity);
		entMan.addComponent(GameComps.COMP_NPC_STATUS_ENEMY_NEAR, entity);
		condCheckerS.iterateEntity(entity,condCheckerC, 1f);
		entMan.cleanup();
		
		assertTrue("UnarmedAndEnemyNearCondition wasnt activated...",entity.containsComponent(GameComps.COMP_NPC_CONDITION_UNARMED_ENEMY_NEAR));
		
		entMan.removeComponentsFromEntity(GameComps.COMP_NPC_STATUS_ENEMY_NEAR, entity);
		entMan.cleanup();
		condCheckerS.iterateEntity(entity,condCheckerC, 1f);
		assertFalse("UnarmedAndEnemyNearCondition was activated after removing status...",entity.containsComponent(GameComps.COMP_NPC_CONDITION_UNARMED_ENEMY_NEAR));

		
	}


	


	
	
}
