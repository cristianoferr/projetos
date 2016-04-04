package com.cristiano.java.jme.tests.unit;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.systems.unit.TargettingSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.jme3.math.Vector3f;

public class TestTargettingSystem extends MockAbstractTest {

	private static  Vector3f POS_ENT = new Vector3f(50,50,50);
	private static  Vector3f POS_TARGET = new Vector3f(100,100,100);
	IGameEntity entity = new GameEntity();

	
	
	@Test
	public void testTargetSorting() {
		TargettingSystem targetS = initTargettingSystem();
		ArrayList<IGameEntity> targetList=initEntitiesList();
		IGameEntity ent = addEntity("entity", new Vector3f(0,0,0), 10);
		
		targetS.sortTargetsListFar(ent, targetList);
		IGameEntity name=targetList.get(0);
		assertTrue("Far retornou: "+name,name==targetList.get(2));
		
		targetS.sortTargetsListNearer(ent, targetList);
		name=targetList.get(0);
		assertTrue("Near retornou: "+name,name==targetList.get(3));
		
		targetS.sortTargetsListStrong(ent, targetList);
		name=targetList.get(0);
		assertTrue("Strong retornou: "+name,name==targetList.get(3));
		
		targetS.sortTargetsListWeak(ent, targetList);
		name=targetList.get(0);
		assertTrue("Weak retornou: "+name,name==targetList.get(1));
	}

	private ArrayList<IGameEntity> initEntitiesList() {
		ArrayList<IGameEntity> list=new ArrayList<IGameEntity>();
		
		list.add(addEntity("ent0", new Vector3f(0,20,0), 20));
		list.add(addEntity("ent1", new Vector3f(0,10,0), 10));
		list.add(addEntity("ent2", new Vector3f(0,40,0), 30));
		list.add(addEntity("ent3", new Vector3f(0,2,0), 55));
		list.add(addEntity("ent4", new Vector3f(0,3,0), 45));
		list.add(addEntity("ent5", new Vector3f(0,4,0), 50));
		return list;
	}

	private IGameEntity addEntity(String name, Vector3f pos, float hp) {
		IGameEntity ent=new GameEntity();
		PositionComponent posComp=(PositionComponent) entMan.addComponent(GameComps.COMP_POSITION, ent);
		posComp.setPos(pos);
		HealthComponent health=(HealthComponent) entMan.addComponent(GameComps.COMP_RESOURCE_HEALTH, ent);
		health.setCurrValue(hp);
		return ent;
	}

}
