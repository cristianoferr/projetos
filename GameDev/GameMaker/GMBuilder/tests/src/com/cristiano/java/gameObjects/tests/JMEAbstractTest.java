package com.cristiano.java.gameObjects.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.ElementManagerComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitResourcesComponent;
import com.cristiano.java.gm.states.BPBuilderState;
import com.cristiano.java.jme.tests.mocks.MockGame;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
public abstract class JMEAbstractTest extends AbstractTest {

	
	protected static JMESnippets snippets;
	
	protected static void startHeadless() {
		Log.info("startHeadless...");
		while (game==null){
			CRJavaUtils.sleep(200);
		}
		Log.info("gameStarted");
		game.startHeadless();
		while (game.getSnippets()==null){
			CRJavaUtils.sleep(200);
		}
		Log.info("snippets loaded");
		snippets=game.getSnippets();
		
	}
	
	@AfterClass
	public static void endClass(){
		Log.debug("Ending test class...");
		if (game!=null){
			game.stop();
		}
		CRJavaUtils.gc();
	}

	@BeforeClass
	public static void initializeTest() throws IOException {
		
		CRJavaUtils.IS_TEST=true;
		integr=new BPBuilderState();
		game = new MockGame(entity);
		((MockGame)game).setIntegrationState(integr);
		integr.initializeState(null, game);
		integr.initObjects();
		entity=game.getGameEntity();
		
		
		factory=integr.getFactory();
		em=(ElementManager) integr.getElementManager();
		world=integr.getWorldElement();
		entMan=integr.getEntityManager();
		ElementManagerComponent emC=(ElementManagerComponent) entMan.addComponent(GameComps.COMP_ELEMENT_MANAGER, entity);
		emC.em=em;
		macroDefs=integr.getMacroDefs();
		
		assertNotNull("ElementManager is null",em);
		assertNotNull("EntityManager is null",entMan);
		assertNotNull("world is null",world);
		assertNotNull("entity is null",entity);
		assertNotNull("game is null",game);
		assertNotNull("macroDefs is null",macroDefs);
		
	}

	
	// components
	

	protected UnitResourcesComponent startUnitResourcesComponent() {
		UnitResourcesComponent genreC = (UnitResourcesComponent) entMan.addIfNotExistsComponent(
				GameComps.COMP_UNIT_RESOURCES, entity);
		if (genreC.getElement() == null) {
			genreC.loadFromElement(em.pickFinal("UnitResourcesComponent leaf"));
		}
		assertNotNull("COMP_UNIT_RESOURCES null", genreC);
		assertNotNull("COMP_UNIT_RESOURCES.unitResources null", genreC.unitResources);
		assertTrue("unitResources empty", genreC.unitResources.size() > 0);
		return genreC;
	}

	
}
