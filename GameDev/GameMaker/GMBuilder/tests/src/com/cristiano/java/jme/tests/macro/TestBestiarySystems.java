package com.cristiano.java.jme.tests.macro;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.macro.BestiaryLibraryComponent;
import com.cristiano.java.gm.ecs.comps.macro.GameOppositionComponent;
import com.cristiano.java.gm.ecs.comps.macro.QueryBestiaryComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.persists.CloneComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.systems.macro.QueryBestiarySystem;
import com.cristiano.java.gm.ecs.systems.macro.TeamSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;

public class TestBestiarySystems extends MockAbstractTest{


	@Test
	public void testCloneEntidade() {
		float valorOriginal=110;
		float valorMudado=88;
		IGameEntity molde=new GameEntity();
		HealthComponent healthCompOriginal = (HealthComponent) entMan.addComponent(GameComps.COMP_RESOURCE_HEALTH, molde);
		healthCompOriginal.setCurrValue(valorOriginal);
		
		IGameEntity clone=entMan.clonaEntidade(molde);
		HealthComponent healthCompClone= (HealthComponent) clone.getComponentWithIdentifier(GameComps.COMP_RESOURCE_HEALTH);
		
		assertNotNull(healthCompOriginal);
		assertNotNull("health do clone nulo",healthCompClone);
		healthCompClone.setCurrValue(valorMudado);
		
		assertTrue("healthClone.valor diferente do esperado",healthCompClone.getCurrValue()==valorMudado);
		assertTrue("healthOriginal.valor diferente do esperado",healthCompOriginal.getCurrValue()==valorOriginal);
		
		CloneComponent cloneComp = (CloneComponent) clone.getComponentWithIdentifier(GameComps.COMP_CLONE);
		assertTrue(cloneComp.idMolde==molde.getId());
		assertTrue(cloneComp.idMolde!=clone.getId());
		
	}
	
	@Test
	public void testBestiarySystem() {
		
		//setting up...
		//UnitRolesComponent compRoles = (UnitRolesComponent) entity.getComponentWithIdentifier(GameComps.COMP_UNIT_ROLES);
		
		QueryBestiarySystem bestS= initQueryBestiarySystem();
		TeamSystem teamS=initTeamSystem();
		
		assertNotNull(teamS);
		assertNotNull(bestS);
		MapComponent mapC = startMapComponent();
		TeamComponent teamC = startTeamComponent();
		GameOppositionComponent oppos = mockOpposition(mapC);
		assertNotNull(oppos.playerSide);
		assertNotNull(oppos.enemySide);
		IGameElement elRole = oppos.playerSide.getPropertyAsGE(GameProperties.UNIT_ROLE);
		assertNotNull(elRole);
		teamC.roleIdentifier=elRole.getIdentifier();
		assertNotNull(teamC.roleIdentifier);
		BestiaryLibraryComponent libComp = teamS.iniciaBestiaryLibrary();
		libComp.addStorage(elRole, 1);
		
		//ordering...
		int budget=1000;
		QueryBestiaryComponent queryC = teamS.orderNewEntity(mapC,budget,Vector3f.ZERO,teamC);
		assertNotNull(queryC);
		//executing request...
		bestS.iterateEntity(mapC, queryC, 0);
		
		assertTrue("no delivered units...",queryC.clonedUnit!=null);
		assertTrue("no delivered units on list...",queryC.readyToDeliver());
		assertNotNull("selectedRole is null",queryC.selectedClass);
		IGameEntity clone = queryC.retrieveEntity();
		assertNotNull(clone);
		
		assertNotNull(queryC.moldeUnit);
		assertTrue(queryC.moldeUnit.containsComponent(GameComps.COMP_MASTER));
		assertFalse(queryC.clonedUnit.containsComponent(GameComps.COMP_MASTER));
		assertFalse(queryC.moldeUnit.containsComponent(GameComps.COMP_CLONE));
		assertTrue(queryC.clonedUnit.containsComponent(GameComps.COMP_CLONE));
		
		//secondary tests...
		
	/*	UnitClassComponent randomRole = compRoles.getRandomClass();
		IGameEntity entidadeExists = bestS.existeEntityComRoleEBudget(randomRole,100,comp);
		assertNotNull("entidadeExists nula",entidadeExists);
		
		IGameEntity entidadeMolde = bestS.pegaEntidadeMolde(comp,100);
		assertNotNull("entidadeMolde nula",entidadeMolde);
		
		*/
		/*
		
		float budgetPedido = bestS.atendePedidoUnidade(comp, 100);
		assertTrue("atendePedido n�o gerou elemento com budget >0",budgetPedido>0);*/
	}

	@Test
	public void testUnitRoles() {
		//UnitRolesComponent compRoles = (UnitRolesComponent) entity.getComponentWithIdentifier(GameComps.COMP_UNIT_ROLES);
	//	assertNotNull("compRoles nulo",compRoles);
		//assertNotNull("compRoles.elUnitRoles nulo",compRoles.elUnitRoles);
		//assertTrue("compRoles.elUnitRoles vazio",compRoles.elUnitRoles.size()>0);
		//assertTrue("compRoles.roles vazio",compRoles.classes.size()>0);
		
		//UnitClassComponent randomRole = compRoles.getRandomClass();
		//assertNotNull("randomRole nulo",randomRole);
		
		/*for (UnitClassComponent role:compRoles.classes){
			assertTrue("role.unitResources vazio",role.unitResources.size()>0);
			assertTrue("role.budgetMultiplier",role.budgetMultiplier>0);
			assertTrue("role.spawnChance",role.spawnChance>0);
			assertTrue("role.rateOfFire",role.rateOfFire>0);
			assertTrue("role.max<min",role.maxCombatRange>=role.minCombatRange);
			assertTrue("role.max<ideal",role.maxCombatRange>=role.idealCombatRange);
			assertTrue("role.min>ideal",role.minCombatRange<=role.idealCombatRange);
			assertTrue("role.unitRootTag",!role.unitRootTag.equals(""));
			assertTrue("role.identifier",!role.identifier.equals(""));
		}*/
	}


}
