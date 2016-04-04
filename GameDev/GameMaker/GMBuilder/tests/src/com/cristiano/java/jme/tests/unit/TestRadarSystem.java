package com.cristiano.java.jme.tests.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.LogicConsts;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.unit.TargettingComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.RadarComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.systems.unit.TargettingSystem;
import com.cristiano.java.gm.ecs.systems.unit.sensors.RadarSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.GMUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class TestRadarSystem extends MockAbstractTest {

	private static  Vector3f POS_ENT = new Vector3f(50,50,50);
	private static  Vector3f POS_TARGET = new Vector3f(100,100,100);
	IGameEntity entity = new GameEntity();

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}

	@Test
	public void testRayCast() {
		String nameO = "boxO";
		String nameD = "boxD";
		Geometry boxO = setupBox(POS_ENT,nameO);
		Geometry boxD = setupBox(POS_TARGET,nameD);
		Geometry collided = game.getSnippets().checkRayCollision(POS_ENT, POS_TARGET.subtract(POS_ENT), nameO);
		assertNotNull("Não colidiu com nada: ",collided); 
		assertNotNull("Não colidiu com a box correta: "+collided.getName(),collided.getName().startsWith(nameD));
		
		Vector3f midPoint = POS_ENT.add(POS_TARGET).mult(0.5f);
		String nameObstaculo = "obstaculo";
		
		Geometry boxObstaculo = setupBox(midPoint,nameObstaculo);
		collided = game.getSnippets().checkRayCollision(POS_ENT, POS_TARGET.subtract(POS_ENT), nameO);
		assertNotNull("Não Colidiu quando devia...",collided);
		assertTrue("Não colidiu com o obstaculo:"+collided.getName(),collided.getName().startsWith(nameObstaculo));
		
		boxObstaculo.removeFromParent();
		boxD.setLocalTranslation(Vector3f.ZERO);
		collided = game.getSnippets().checkRayCollision(POS_ENT, POS_TARGET.subtract(POS_ENT), nameO);
		assertNull("Colidiu com algo quando não devia",collided);
		boxO.removeFromParent();
		boxD.removeFromParent();
	}

	private Geometry setupBox( Vector3f pos,String name) {
		Geometry boxO = game.getSnippets().generateBox(ColorRGBA.Red, new Vector3f(10,10,10),pos);
		boxO.setName(name);
		game.getRootNode().attachChild(boxO);
		return boxO;
	}

	@Test
	public void testRadar() {
		entity.removeAllComponents();
		RadarSystem radarS = initRadarSystem();
		
		IGameEntity ent=entMan.createEntity();;
		IGameEntity target=entMan.createEntity();;
		RadarComponent radarC = mockRadar(ent);
		PositionComponent posThis=(PositionComponent) entMan.addComponent(GameComps.COMP_POSITION, ent);
		mockTargetting(ent); 
		PositionComponent posTarget=(PositionComponent) entMan.addComponent(GameComps.COMP_POSITION, target);
		entMan.addComponent(GameComps.COMP_TARGETABLE, ent);
		entMan.addComponent(GameComps.COMP_TARGETABLE, target);
		entMan.cleanup();
		assertNotNull(posThis);
		assertNotNull(posTarget);
		assertNotNull(ent);
		assertNotNull(target);
		assertNotNull(radarC);
		radarC.detectRange=50;
		
		int isEnemy=radarS.getRelationBetween(ent, target);
		assertTrue("should be enemies... "+isEnemy,isEnemy==LogicConsts.RELATION_ENEMY);
		posThis.setPos(new Vector3f(0,0,0));
		posTarget.setPos(new Vector3f(0,radarC.detectRange*2,0));
		float distance = ECS.getTargetDistance(ent, target);
		assertFalse(radarS.isTargetInsideRange(distance,radarC));
		posTarget.setPos(new Vector3f(0,radarC.detectRange/2,0));
		distance = ECS.getTargetDistance(ent, target);
		assertTrue(radarS.isTargetInsideRange(distance,radarC));
		assertTrue("distancia calculada errada:"+distance,distance==radarC.detectRange/2);
		
		//precisa de mesh...
		
		TargettingComponent targettingC=(TargettingComponent) ent.getComponentWithIdentifier(GameComps.COMP_TARGETTING);
		
		Geometry boxO = setupBox(posThis.getPos(),GMUtils.getNodeName(ent, ""));
		Geometry boxD = setupBox(posTarget.getPos(),GMUtils.getNodeName(target, ""));
		int targetVisible = radarS.isTargetVisible(radarC, ent, target);
		assertTrue("não está visivel quando deveria estar...",targetVisible>0);
		
		assertTrue(targettingC.targetList.size()==0);
		radarS.noTargetBehaviour(ent,radarC);
		assertTrue(targettingC.targetList.size()>0);
		
		validaTargetingSystem(ent,targettingC);
		
		TargetComponent targetC = (TargetComponent) ent.getComponentWithIdentifier(GameComps.COMP_TARGET);
		assertNotNull("No target found...",targetC);
		
		assertTrue(targetC.lastPosition+"<>"+posTarget,targetC.lastPosition.equals(posTarget.getPos()));
		assertTrue(targetC.target==target);
		
		//adicionando um obstaculo...
		String nameObstaculo = "obstaculo";
		Vector3f midPoint = posTarget.getPos().add(posThis.getPos()).mult(0.5f);
		Geometry boxObstaculo = setupBox(midPoint,nameObstaculo);
		targetVisible = radarS.isTargetVisible(radarC, ent, target);
		assertTrue("Está visivel quando não deveria estar...",targetVisible<0);
		
		targetC.timeNotVisible=0;
		radarS.targetBehaviour(ent,radarC,targetC,1);
		assertTrue("timeNotvisible não mudou... ",targetC.timeNotVisible>0);
		radarS.targetBehaviour(ent,radarC,targetC,100);
		boolean hasTarget=ent.containsComponent(GameComps.COMP_TARGET);
		assertFalse("não devia ter target após timeout...",hasTarget);
		
		boxO.removeFromParent();
		boxD.removeFromParent();
		boxObstaculo.removeFromParent();
	}

	
	

	private void validaTargetingSystem(IGameEntity ent,TargettingComponent targettingC) {
		TargettingSystem targettingS = initTargettingSystem();
		targettingS.iterateEntity(ent, targettingC, 0);
		
	}

}
