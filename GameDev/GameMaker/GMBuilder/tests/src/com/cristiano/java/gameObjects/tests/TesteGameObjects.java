package com.cristiano.java.gameObjects.tests;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.Extras;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.systems.unit.resources.HealthSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;


public class TesteGameObjects extends MockAbstractTest {

	
	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	@Test public void testEntMan() {
		IGameEntity entity=entMan.createEntity();
		assertTrue("entMan nao contem a entity.",entMan.containsEntity(entity));
		assertFalse("entMan contem a entity nova.",entMan.containsEntity(new GameEntity()));
		
		assertFalse("entMan nao deveria ter incluido entidade ja presente",entMan.addEntity(entity));
		assertFalse("entMan nao deveria ter incluido entidade nula",entMan.addEntity(null));
		
		int id=entity.getId();
		assertTrue("entMan deveria ter incluido entidade nova",entMan.containsEntity(entMan.createEntity()));
		assertTrue("entMan deveria ter incluido entidade nova",entMan.addEntity(new GameEntity()));
		
		IGameEntity entRet=entMan.getEntityWithId(id);
		assertTrue("entidade retornada diferente da esperada:"+entRet.getId(),entity==entRet);
		assertTrue("id da entidade retornada diferente da esperada:"+entRet.getId(),id==entRet.getId());
		
		entRet=entMan.getEntityWithId(46546846);
		assertNull("entidade com id invalida retornada:"+entRet,entRet);
		
		assertTrue("Qtd entities =0",entMan.size()>0);
		
		entMan.removeEntity(entity);
		assertFalse("entMan nao contem a entity.",entMan.containsEntity(entity));
		
		assertTrue("entMan deveria ter incluido entidade removida anteriormente",entMan.addEntity(entity));
	}
	
	
	@Test public void testComponentes() {
		IGameEntity entity=entMan.createEntity();
		factory.addClasse(GameComps.COMP_RESOURCE_HEALTH,"unit/resources/"+GameComps.COMP_RESOURCE_HEALTH);
		HealthComponent comp=(HealthComponent) entMan.addComponent(GameComps.COMP_RESOURCE_HEALTH, entity);
		assertNotNull("healthComp nulo",comp);
		
		IGameComponent compIdent = entity.getComponentWithIdentifier(GameComps.COMP_RESOURCE_HEALTH);
		assertTrue("comp<>compIdent",compIdent==comp);
		compIdent = entity.getComponentWithIdentifier("assdasaas");
		assertTrue("compIdent<>nulo",compIdent==null);
		assertTrue("nao contem comp quando deveria...",entity.containsComponent(GameComps.COMP_RESOURCE_HEALTH));
		assertFalse("contem comp quando neo deveria...",entity.containsComponent("aaaa"));
		assertTrue("nao contem comp quando deveria...",entity.containsComponent(comp));
		
		entMan.cleanup();
		List<IGameEntity> entsIdent = entMan.getEntitiesWithComponent(GameComps.COMP_RESOURCE_HEALTH);
		List<IGameEntity> entsComp = entMan.getEntitiesWithComponent(comp);
		assertTrue("Tamanho da entsIdent zerado",entsIdent.size()>0);
		assertTrue("Tamanho da entsComp zerado",entsComp.size()>0);
		assertTrue("Tamanho da entsComp<>entsIdent",entsComp.size()==entsIdent.size());
		
		IGameEntity entHealth=entMan.getEntityWithComponent(GameComps.COMP_RESOURCE_HEALTH);
		assertNotNull(entHealth);
		assertTrue(entHealth.containsComponent(GameComps.COMP_RESOURCE_HEALTH));
		
		List<IGameComponent> components = entity.getAllComponents();
		assertTrue("Entidade com componentes zerados",components.size()>0);
		entMan.removeComponentsFromEntity(Extras.TAG_ALL, entity);
		entMan.update(0);
		components = entity.getAllComponents();
		assertTrue("Entidade ficou com componentes apos remocao:"+components.size(),components.size()==0);
		
		entMan.addComponent(comp, entity);
		components = entity.getComponentsWithIdentifier(GameComps.COMP_RESOURCE_HEALTH);
		assertTrue("getComponents zerado:"+components.size(),components.size()>0);
		components = entity.getComponents("aasasas");
		assertTrue("getComponents nao retornou zerado:"+components.size(),components.size()==0);

		entMan.removeComponentFromEntity(comp, entity);
		entMan.update(0);
		components = entity.getAllComponents();
		assertTrue("Entidade ficou com componentes apos remocao:"+components.size(),components.size()==0);
		
	}
	/*
	@Test public void testEntityElementIntegration() {
		GenericElement ge=new GenericElement(em);
		
		IGameEntity entityFromElement = entMan.getEntityFromElement(ge);
		assertTrue("entidade do elemento diferente",entityFromElement==entity);
		
		IGameEntity createEntityFrom = fact.createEntityFromTag("aaaa");
		assertNull("entidade deve ser nula",createEntityFrom);
		
	}*/
	
	@Test public void testSystems() {
		HealthSystem hs=new HealthSystem();
		hs.initSystem(entMan, null);
		entMan.addSystem(null);
		entMan.addSystem(hs);
		entMan.addSystem(hs);
		entMan.update(1);
		assertTrue("entMan nao contem system como entity",entMan.containsEntity(hs));
	}
	
	
}
