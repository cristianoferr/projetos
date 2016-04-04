package com.cristiano.java.gameObjects.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.gm.builder.textures.ProceduralTexture3D;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.ElementManagerComponent;
import com.cristiano.java.gm.ecs.comps.macro.BestiaryLibraryComponent;
import com.cristiano.java.gm.ecs.comps.map.BubbleDataComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.persists.GameConstsComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitResourcesComponent;
import com.cristiano.java.gm.ecs.comps.unit.fx.FXLibraryComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.ecs.systems.unit.fx.FXLibrarySystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.states.BPBuilderState;
import com.cristiano.java.jme.tests.mocks.MockFactory;
import com.cristiano.java.jme.tests.mocks.MockGame;
import com.cristiano.java.jme.tests.mocks.Mockery;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.IReadWrite;
import com.cristiano.utils.Log;

public abstract class MockAbstractTest extends AbstractTest {

	protected static final float TERRAIN_HEIGHT = 500;
	

	protected static void startHeadless() {
		game.startHeadless();
		while (game.getSnippets() == null) {
			CRJavaUtils.sleep(200);
		}
		factory.setAssetManager(game.getAssetManager());
		snippets = game.getSnippets();
		snippets.initNifty();
		NiftyComponent niftyC=(NiftyComponent) entity.getComponentWithIdentifier(GameComps.COMP_NIFTY);
		niftyC.nifty=snippets.getNifty();
		assertNotNull(niftyC.nifty);
	

	}

	@AfterClass
	public static void endClass() {
		Log.debug("Ending test class...");
		if (game != null) {
			game.stop();
		}
		CRJavaUtils.gc();
	}
	
	

	@BeforeClass
	public static void initializeTest() throws IOException {

		CRJavaUtils.IS_TEST = true;
		integr = new BPBuilderState();
		game = new MockGame(entity);
		((MockGame) game).setIntegrationState(integr);
		integr.initializeState(null, game);
		em = new ElementManager();
		em.loadTestBlueprintsFromFile();
		IGameElement worldElement = em.pickFinal(TestStrings.TEST_WORLD_TAG);

		integr.initElementManager(em, worldElement);
		integr.setEntityManager(new EntityManager());
		// integr.initObjects();
		game.setGameEntity(new GameEntity());
		entity = game.getGameEntity();
		CRJavaUtils.context=(IReadWrite) game;

		em = (ElementManager) integr.getElementManager();
		
		world = integr.getWorldElement();

		entMan = integr.getEntityManager();
		
		factory = new MockFactory(em, entMan,game.getAssetManager());
		integr.setFactory(factory);
		ElementManagerComponent emC = (ElementManagerComponent) entMan.addComponent(GameComps.COMP_ELEMENT_MANAGER, entity);
		emC.em = em;
		macroDefs = integr.getMacroDefs();
		Mockery.mockGame();
		mockWorld(entity);
		assertNotNull("ElementManager is null", em);
		assertNotNull("EntityManager is null", entMan);
		assertNotNull("world is null", world);
		assertNotNull("entity is null", entity);
		assertNotNull("game is null", game);
		assertNotNull("factory is null", factory);

	}

	

	// mocks

	protected static void mockWorld(IGameEntity entity) {
		// UnitResourcesComponent

		IGameComponent worldComp = entMan.addComponent(GameComps.COMP_WORLD, entity);
		IGameComponent reuseComp = entMan.addComponent(GameComps.COMP_REUSE_MANAGER, entity);
		FXLibraryComponent fxlibC = startFXLibraryComponent();
		FXLibrarySystem fxlib = initFXLibrarySystem();
		fxlib.iterateEntity(entity, fxlibC, 0);

		UnitResourcesComponent unitResC = (UnitResourcesComponent) entMan.addComponent(GameComps.COMP_UNIT_RESOURCES, entity);
		mockUnitResources(unitResC);
		ResourceComponent resource = mockUnitResource();
		unitResC.unitResources.add(resource);

		BestiaryLibraryComponent libC = (BestiaryLibraryComponent) entMan.addComponent(GameComps.COMP_BESTIARY_LIB, entity);
		IGameElement geLib = em.createElement();
		geLib.setProperty(GameProperties.CHANCE_NEW_ENTITY, 50);
		libC.loadFromElement(geLib);
		//UnitRolesComponent rolesC = mockUnitRoles(entity);
		// ge.setParam(Extras.LIST_OBJECT,"unitResources#0",);

		BubbleDataComponent bubble = startBubbleDataComponent();

		GameConstsComponent gameConsts = startGameConstsComponent();
		gameConsts.assetMaterial = mockMaterialLayers();

	}

	protected UnitResourcesComponent startUnitResourcesComponent() {
		UnitResourcesComponent genreC = (UnitResourcesComponent) entMan.addIfNotExistsComponent(GameComps.COMP_UNIT_RESOURCES, entity);
		if (genreC.getElement() == null) {
			IGameElement el = em.pickFinal("UnitResourcesComponent leaf");
			if (el == null) {
				mockUnitResources(genreC);
				/*
				 * Log.debug("UnitResourcesComponent element is null, mocking it..."
				 * ); el = new GenericElement(em); genreC.loadFromElement(el);
				 * genreC.unitResources.add(mockUnitResource());
				 */
			} else {
				genreC.loadFromElement(el);
			}
		}
		assertNotNull("COMP_UNIT_RESOURCES null", genreC);
		assertNotNull("COMP_UNIT_RESOURCES.unitResources null", genreC.unitResources);
		assertTrue("unitResources empty", genreC.unitResources.size() > 0);
		return genreC;
	}

	private static void mockUnitResources(UnitResourcesComponent unitResC) {
		GenericElement ge = new GenericElement(em);

		GenericElement geRes = new GenericElement(em);
		geRes.setName("PointsResourceComponent");
		geRes.setProperty("identifier=PointsResourceComponent");
		geRes.setProperty("notificationTypeTag={shout}");
		geRes.setProperty("scopeTag={team}");
		ge.setParam(Extras.LIST_OBJECT, "unitResources#0", geRes);

		unitResC.loadFromElement(ge);
	}

	protected static GenericElement mockMaterialLayers() {
		GenericElement ge = new GenericElement(em);
		ge.setProperty("density", 1);
		ge.setProperty("color", "#FFFFFF");
		ge.setProperty(GameProperties.MATERIAL_TYPE, MaterialComponent.MAT_LIGHT);
		ge.setProperty("relativeSize", 1.5f);
		ge.setProperty(GameProperties.LENGTH, 10);

		GenericElement elBlending = new GenericElement(em);
		elBlending.setProperty("calcB", "0");
		ge.setProperty("blending", elBlending);

		GenericElement elShine = new GenericElement(em);
		elShine.setName("shine");
		elShine.setProperty("textureType", ProceduralTexture3D.class.getCanonicalName());
		elShine.setProperty("alphaFunction", 1);
		elShine.setProperty("blueFunction", 1);
		elShine.setProperty("redFunction", 1);
		elShine.setProperty("greenFunction", 1);
		GenericElement elDiffuse = new GenericElement(em);
		elDiffuse.setName("diffuse");
		elDiffuse.setProperty("alphaFunction", 1);
		elDiffuse.setProperty("blueFunction", 1);
		elDiffuse.setProperty("redFunction", 1);
		elDiffuse.setProperty("greenFunction", 1);
		elDiffuse.setProperty("textureType", ProceduralTexture3D.class.getCanonicalName());
		GenericElement elNormal = new GenericElement(em);
		elNormal.setProperty("textureType", ProceduralTexture3D.class.getCanonicalName());
		elNormal.setName("normal");
		elNormal.setProperty("alphaFunction", 1);
		elNormal.setProperty("blueFunction", 1);
		elNormal.setProperty("redFunction", 1);
		elNormal.setProperty("greenFunction", 1);

		ge.setProperty("shine", elShine);
		ge.setProperty("diffuse", elDiffuse);
		ge.setProperty("normal", elNormal);

		return ge;
	}


	
}
