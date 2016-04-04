package com.cristiano.java.jme.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.macro.GameEventComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.PointsResourceComponent;
import com.cristiano.java.gm.ecs.systems.macro.GameEventSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;


public class TestGameEvents extends MockAbstractTest {

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}	

	@Test
	public void testGameEvent() {
		GameEventSystem ges=initGameEventSystem();
		
		//sender:sent the damage (got points)
		//entity=receiver, receives the damage
		
		IGameEntity sender=new GameEntity();
		entMan.addEntity(sender);
		
		GameEventComponent gec=(GameEventComponent) entMan.addComponent(GameComps.COMP_GAME_EVENT, entity);
		gec.sender=sender;
		gec.eventName=LogicConsts.EVENT_DMG_RECEIVED;
		gec.showScrollingText=true;
		ges.iterateEntity(entity, gec, 0);
		
		//testing receiver
		List<IGameComponent> components = entity.getComponentsWithIdentifier(GameComps.COMP_GAME_EVENT);
		assertTrue(components.size()==1);
		GameEventComponent event0=(GameEventComponent) components.get(0);
		//GameEventComponent event1=(GameEventComponent) components.get(1);
		assertTrue("Event not correct:"+event0.eventName,event0.eventName.equals(LogicConsts.EVENT_ENTITY_HIT));
		assertTrue(event0.sender==sender);
		
		//testing sender...
		 components = sender.getComponentsWithIdentifier(GameComps.COMP_GAME_EVENT);
		assertTrue(components.size()==1);
		event0=(GameEventComponent) components.get(0);
		//GameEventComponent event1=(GameEventComponent) components.get(1);
		assertTrue("event not correct:"+event0.eventName,event0.eventName.equals(LogicConsts.EVENT_ENEMY_HIT));
		assertTrue(event0.sender==entity);
		
		
		GameGenreComponent genreC = (GameGenreComponent) entity.getComponentWithIdentifier(GameComps.COMP_GAME_GENRE);
		assertNotNull("genreC undefined",genreC);
		assertNotNull("genreC.element undefined",genreC.getElement());
		genreC.loadFromElement(em.pickFinal(TestStrings.TEST_GENRE_TAG));
		assertTrue(genreC.eventsSize()>0);
		
		validateRewards(event0,genreC,ges);
	}

	private void validateRewards(GameEventComponent event0, GameGenreComponent genreC, GameEventSystem ges) {
		GameEventComponent eventData = genreC.getEventWithName(event0.eventName);
		assertNotNull("eventData for "+event0.eventName+" not found",eventData);
		eventData.hitPointReward=-100;
		eventData.multiByValue=false;
		event0.value=10;//will not change the result...
		
		//testing without multiByValue
		HealthComponent health=(HealthComponent) entMan.addIfNotExistsComponent(GameComps.COMP_RESOURCE_HEALTH, entity);
		int initialValue = 500;
		health.setCurrValue(initialValue);
		ges.iterateEntity(entity, event0, 0);
		assertTrue("health hasnt changed",health.getCurrValue()==initialValue+eventData.hitPointReward);
		
		//testing with multiByValue
		eventData.pointReward=100;
		eventData.multiByValue=true;
		event0.value=10;//will change the result...
		PointsResourceComponent points=(PointsResourceComponent) entMan.addIfNotExistsComponent(GameComps.COMP_RESOURCE_POINTS, entity);
		points.setCurrValue(initialValue);
		ges.iterateEntity(entity, event0, 0);
		assertTrue("points hasnt changed:"+points.getCurrValue(),points.getCurrValue()==initialValue+eventData.pointReward*event0.value);
	}
	
	
	
	
}
