package com.cristiano.java.jme.tests.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.comps.unit.BulletComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.comps.unit.actuators.WeaponComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.DPSComponent;
import com.cristiano.java.gm.ecs.comps.unit.sensors.TargetComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.systems.MeshLoaderSystem;
import com.cristiano.java.gm.ecs.systems.unit.BulletSystem;
import com.cristiano.java.gm.ecs.systems.unit.TargetSystem;
import com.cristiano.java.gm.ecs.systems.unit.UnitRoleSystem;
import com.cristiano.java.gm.ecs.systems.unit.actuators.WeaponSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.mocks.MockControlBody;
import com.cristiano.jme3.rigidBody.ActionController;
import com.cristiano.jme3.rigidBody.CharActions;
import com.jme3.math.Vector3f;

public class TestActuators extends MockAbstractTest {

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	private void validateBulletSystem(IGameEntity ent, BulletComponent bulletC) {
		BulletSystem bulletS=initBulletSystem();
		mockUnit(ent);
		
		bulletS.iterateEntity(ent, bulletC, 0);
		//TODO: finish this
	}
	
	
	
	@Test
	public void testTargetSystem() {
		IGameEntity ent=entMan.createEntity();
		
		mockPlayer(ent);
		//mockUnit(ent);
		
		TargetComponent targetC=mockTarget(ent);
		TargetSystem targetS = initTargetSystem();
		assertTrue(targetC.firstTick);
		targetS.iterateEntity(ent, targetC, 0);
		assertFalse(targetC.firstTick);
	}
	
	@Test
	public void testBulletSystem() {
		IGameEntity ent=entMan.createEntity();
		BulletComponent bulletC = generateBullet(ent);
		
		
		validateBulletSystem(ent,bulletC);
		
		ReuseManagerComponent reuseC = startReuseComponent();
		//IGameEntity reuseComp = reuseC.requestEntityFromElement(bulletC.elementSource);
		//assertNotNull(reuseComp);
		
	}

	private BulletComponent generateBullet(IGameEntity ent) {
		initGameEventSystem();
		
		WeaponSystem weaponS=initWeaponSystem();
		WeaponComponent weaponC = (WeaponComponent) entMan.addComponent(GameComps.COMP_ACTUATOR_WEAPON, entity);
		weaponC.loadFromElement(mockWeaponElement());
		weaponC.okToShoot=true;
		/*UnitRolesComponent rolesC = startUnitRolesComponent();
		assertNotNull(rolesC);
		UnitClassComponent unitRole = rolesC.classes.get(0);*/
		/*
		assertNotNull(unitRole);
		assertTrue(unitRole.blastRadius>0);
		assertTrue(unitRole.bulletSpeed>0);
		assertTrue(unitRole.rateOfFire>0);*/
		
		DPSComponent dpsC=(DPSComponent) entMan.addComponent(GameComps.COMP_RESOURCE_DPS, entity);
		dpsC.setCurrValue(100);
		
		
		PositionComponent posC=(PositionComponent) entMan.addComponent(GameComps.COMP_POSITION, entity);
		Vector3f pos=new Vector3f(0,50,0);
		posC.setPos(pos);
		
		mockUnit(ent);
		TargetComponent target = mockTarget(ent);
		
		//entity.attachComponent(unitRole);
		
		//fill the component with data...
		weaponS.defineReusableData(entity, weaponC);
		
		//iniciando a bullet...
		Vector3f relPos=new Vector3f(10,0,0);
		weaponC.weaponFrontPos.set(relPos);
		weaponC.cooldown=weaponC.getRateOfFire()+1;//I just set the cooldown, all the variations are tested in TestActuators
		BulletComponent bulletC=weaponS.shootBullet(entity,weaponC,target);
		assertNotNull(bulletC);
		assertNotNull(bulletC.elementID);
		assertTrue(bulletC.bulletDefines.explosionRadius>0);
		assertTrue(bulletC.bulletDefines.bulletDmg>0);
		//assertTrue(bulletC.bulletDefines.bulletDmg+"<>"+unitRole.rateOfFire*dpsC.getCurrValue(),bulletC.bulletDefines.bulletDmg==unitRole.rateOfFire*dpsC.getCurrValue());
		assertTrue(bulletC.bulletDefines.shotSpeed>0);
		
		//validateBulletEntity(bulletC,unitRole);
		
		return bulletC;
	}
	

	@Test
	public void testActuator() {
		IGameEntity ent=entMan.createEntity();
		MeshLoaderSystem meshLS = initMeshLoaderSystem();
		IGameElement elTurret = mockElementTurret();
		assertNotNull(elTurret);
		
		SpatialComponent spat=mockSpatial(ent);
		
		meshLS.verifyActuators(ent,elTurret,spat,Vector3f.ZERO);
		WeaponComponent weapon = (WeaponComponent) ent.getComponentWithIdentifier(GameComps.COMP_ACTUATOR_WEAPON);
		assertNotNull(weapon);
		assertTrue("No addToAction defined...",weapon.addToAction>0);
		
		//testing properties...
		float turnRate=elTurret.getPropertyAsFloat(GameProperties.TURN_RATE);
		assertTrue(turnRate>0);
		assertTrue("turnRate:"+weapon.turnRate,turnRate==weapon.turnRate);
		float mass=elTurret.getPropertyAsFloat(GameProperties.MASS);
		assertTrue(mass>0);
		assertTrue(mass==weapon.mass);
		float effect=elTurret.getPropertyAsFloat(GameProperties.BULLET_EFFECT_TYPE);
		assertTrue(effect>0);
		assertTrue(effect==weapon.bulletEffectType);
		
		//testing if action is enabled...
		PhysicsComponent physC = startMockPhysicsComponent(ent);
		ActionController actionController = physC.controlBody.getActionController();
		assertNotNull(actionController);
		assertFalse(actionController.isActionEnabled(CharActions.getActionNameFor(1)));
		WeaponSystem weaponS = initWeaponSystem();
		
		//preparing the entity...
		mockUnit(ent);
		TargetComponent target = mockTarget(ent);
		
		
		weaponS.iterateEntity(ent, weapon, 0);
		
		assertTrue(weapon.getRateOfFire()>0);
		assertTrue(actionController.isActionEnabled(CharActions.getActionNameFor(1)));
		
		//testing the action...
		
		//testing without shooting...
		BulletComponent bulletC = (BulletComponent) ent.getComponentWithIdentifier(GameComps.COMP_BULLET);
		assertNull(bulletC);
		
		//testing shooting but with a zero cooldown...
		actionController.sendAction(CharActions.getActionNameFor(1));
		actionController.update(1);
		bulletC = (BulletComponent) ent.getComponentWithIdentifier(GameComps.COMP_BULLET);
		assertNull("bullet was created (it shouldnt since the cooldown is 0, need to be bigger than rateOfFire...",bulletC);
		
		//testing with cooldown ok but without sending an update...
		weaponS.iterateEntity(ent, weapon, weapon.getRateOfFire()+1);
		weapon.okToShoot=true;
		weapon.cooldown=0;
		bulletC = weaponS.shootBullet(ent, weapon,target);
		assertNull("bullet was created with 0 cooldown...",bulletC);
		weapon.cooldown=weapon.getRateOfFire()+1;
		bulletC = weaponS.shootBullet(ent, weapon,target);
		assertNotNull("bullet wasnt created...",bulletC);
		validateBulletSystem(ent,bulletC);
		
	}



	
	private PhysicsComponent startMockPhysicsComponent(IGameEntity ent) {
		PhysicsComponent physC = (PhysicsComponent) entMan.addComponent(GameComps.COMP_PHYSICS, ent);
		assertNotNull(physC);
		physC.controlBody=new MockControlBody();
		return physC;
	}
	
	
	
	private void validateBulletEntity( BulletComponent bulletC, UnitClassComponent roleC) {
		UnitRoleSystem roleS=initUnitRoleSystem();
		ReuseManagerComponent reuseC = startReuseComponent();
		assertTrue(reuseC.requestEntityFromElement(roleC.bulletElement.id())==null);
		roleS.iterateEntity(entity, roleC, 0);
		entMan.cleanup();
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(GameComps.COMP_REUSE_MANAGER);
		assertTrue("More than one entity containing reuseManager:"+ents.size(),ents.size()>=1);
		assertTrue(reuseC.requestEntityFromElement(roleC.bulletElement)!=null);
		
	}
	


}
