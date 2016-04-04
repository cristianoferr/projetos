package com.cristiano.java.jme.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameActions;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.mechanics.DamageOverTimeComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.DamageReceivedComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIActionComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIScreenComponent;
import com.cristiano.java.gm.ecs.comps.unit.ChildComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.HealthComponent;
import com.cristiano.java.gm.ecs.comps.visual.ThemeComponent;
import com.cristiano.java.gm.ecs.systems.DamageCalculatorSystem;
import com.cristiano.java.gm.ecs.systems.DamageOverTimeSystem;
import com.cristiano.java.gm.ecs.systems.LoadEntitySystem;
import com.cristiano.java.gm.ecs.systems.ThemeSystem;
import com.cristiano.java.gm.ecs.systems.ui.UIActionListenerSystem;
import com.cristiano.java.gm.ecs.systems.ui.UIScreenSystem;
import com.cristiano.java.gm.ecs.systems.unit.DeathSystem;
import com.cristiano.java.gm.ecs.systems.unit.resources.HealthSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.IGameSystem;
import com.cristiano.java.gm.interfaces.state.IGameState;
import com.cristiano.java.gm.states.BPBuilderState;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;

import de.lessvoid.nifty.Nifty;

public class TestGamePlayer extends MockAbstractTest {

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}

	@Test
	public void testaAction() {
		mockMapWorld();
		mockPlayer();
		entMan.cleanup();

		UIActionComponent actionLaunch = (UIActionComponent) entMan.addComponent(GameComps.COMP_UI_ACTION, game.getGameEntity());
		actionLaunch.originComponent = game.getGameEntity();
		actionLaunch.action = GameActions.ACTION_LAUNCH_BALL;
		UIActionListenerSystem actionS = initUIActionListenerSystem();
		IGameEntity gameEntity = game.getGameEntity();
		actionS.iterateEntity(gameEntity, actionS.sendAction(gameEntity, GameActions.ACTION_TOGGLE_PHYS_DEBUG), 1);

		actionS.iterateEntity(gameEntity, actionS.sendAction(gameEntity, GameActions.ACTION_TOGGLE_MOUSE), 1);
		actionS.iterateEntity(gameEntity, actionS.sendAction(gameEntity, GameActions.ACTION_TOGGLE_GRAVITY), 1);
		actionS.iterateEntity(gameEntity, actionS.sendAction(gameEntity, GameActions.ACTION_DEBUG_GAME), 1);
		actionS.iterateEntity(gameEntity, actionS.sendAction(gameEntity, GameActions.ACTION_DIAGNOSTICO), 1);
		actionS.iterateEntity(gameEntity, actionS.sendAction(gameEntity, GameActions.ACTION_GAME_CONFIG), 1);
		actionS.iterateEntity(gameEntity, actionS.sendAction(gameEntity, GameActions.ACTION_RESET_PLAYER), 1);
		actionS.iterateEntity(gameEntity, actionS.sendAction(gameEntity, GameActions.ACTION_TOGGLE_CAMERA), 1);
		actionS.iterateEntity(gameEntity, actionS.sendAction(gameEntity, GameActions.ACTION_START_GAME), 1);

		// simulando clique no bot�o

		NiftyComponent niftyC = (NiftyComponent) entity.getComponentWithIdentifier(GameComps.COMP_NIFTY);
		Nifty nifty = niftyC.nifty;
		UIScreenSystem screenS = initUIScreenSystem();//(UIScreenSystem) nifty.getCurrentScreen().getScreenController();
		assertNotNull("screenSystem nulo", screenS);
		screenS.action(Integer.toString(game.getGameEntity().getId()), GameActions.ACTION_TOGGLE_MOUSE);

		actionS.iterateEntity(game.getGameEntity(), actionLaunch, 1);
		assertNotNull("Entitidade n�o foi gerada apos lan�ar bola...", actionLaunch.entityGenerated);

	}

	@Test
	public void testNiftyLoaded() throws IOException {
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(GameComps.COMP_NIFTY);
		assertTrue("Nifty n�o foi carregado", ents.size() > 0);
	}

	@Test
	public void testUIFactory() throws IOException {
		IGameElement geMestre = em.createFinalElementFromTag(TestStrings.TAG_BUTTON_TEST);
		assertNotNull(geMestre);
		String resultado = geMestre.getProperty("label");
		String esperado = "Teste";
		assertTrue("Label diferente: " + esperado + "<>" + resultado, resultado.equals(esperado));
		resultado = geMestre.getIdentifier();
		esperado = GameComps.COMP_UI_CONTROL;
		assertTrue("ident diferente: " + esperado + "<>" + resultado, resultado.equals(esperado));
	}

	@Test
	public void testaLoadScreen() {
		IGameElement world = game.getIntegrationState().getWorldElement();
		assertNotNull("world nulo", world);

		IGameElement theme = em.pickFinal(TestStrings.TAG_THEME);
		assertNotNull("theme nulo", theme);
		IGameEntity gameEntity = game.getGameEntity();
		ThemeComponent themeC = (ThemeComponent) gameEntity.getComponentWithIdentifier(GameComps.COMP_THEME);
		assertNotNull(themeC);
		themeC.loadFromThemeElement(theme);
		themeC.firstTick = true;

		// valida carga da screen...
		gameEntity.removeComponent(GameComps.COMP_UI_SCREEN);
		ThemeSystem themeS = initThemeSystem();
		assertTrue("Screens already generated", gameEntity.getComponentsWithIdentifier(GameComps.COMP_UI_SCREEN).size() == 0);
		themeS.iterateEntity(gameEntity, themeC, 0);
		assertTrue("No Screen generated", gameEntity.getComponentsWithIdentifier(GameComps.COMP_UI_SCREEN).size() > 0);
		entMan.cleanup();
		List<IGameEntity> screenEnts = entMan.getEntitiesWithComponent(GameComps.COMP_UI_SCREEN);
		assertTrue("screenEnts vazia:" + screenEnts.size(), screenEnts.size() > 0);
		IGameEntity entity = screenEnts.get(0);
		UIScreenComponent screenC = (UIScreenComponent) entity.getComponentWithIdentifier(GameComps.COMP_UI_SCREEN);
		UIScreenSystem screenS = initUIScreenSystem();
		screenS.iterateEntity(entity, screenC, 0);

		assertNotNull("screenC nulo", screenC);
		assertNotNull("screenC.elScreenUI nulo", screenC.elUI);
		assertNotNull("screenC.name nulo", screenC.name);
		assertNotNull("screenC.layers nulo", screenC.children);
		//assertTrue("screenC.layers vazia:" + screenC.children.size(), screenC.children.size() > 0);

	}

	@Test
	public void testaLoadMesh() {
		IGameEntity entity = factory.createEntityFromTag(TestStrings.TEST_ENTITY_TAG);
		assertNotNull("entity nulo", entity);

	}

	@Test
	public void testaLoadFromTag() {
		IGameEntity entity = factory.createEntityFromTag(TestStrings.TEST_ENTITY_TAG);
		assertNotNull("entity nulo", entity);

		String tag = TestStrings.TEST_ENTITY_TAG;
		ChildComponent comp = (ChildComponent) entMan.addComponent(GameComps.COMP_CHILD, entity);
		comp.tag = tag;
		comp.madeBy = entity;
		LoadEntitySystem loadS = initLoadEntitySystem();
		loadS.iterateEntity(entity, comp, 1);
		assertNotNull("comp.entity n�o foi carregada!", comp.getAllComponents().size()>0);


		entMan.addEntity(entity);
		assertTrue("entMan n�o contem entity.", entMan.containsEntity(entity));
		IGameComponent deathC = entMan.addComponent(GameComps.COMP_DEATH, entity);
		DeathSystem deathS = initDeathSystem();
		deathS.iterateEntity(entity, deathC, 0);
		entMan.cleanup();
		assertTrue("entMan contem entity apos remove-la.", !entMan.containsEntity(entity));

	}

	@Test
	public void testaEntityFromTag() {
		IGameEntity entity = factory.createEntityFromTag("testentity");
		assertNotNull("entity nulo", entity);

		verificaDot(entity);

		ChildComponent parentComp = ECS.getChild(entity);
		// ParentEntitySystem parentS = initParentEntitySystem();
		// parentS.iterateEntity(entity, parentComp, 0);

		assertNotNull("parentComp nulo", parentComp);
		// assertNotNull("parentComp.elparent nulo", parentComp.elParent);
		// assertNotNull("parentComp.parent nulo", parentComp.parent);

	}

	private void verificaDot(IGameEntity entity) {
		DamageOverTimeComponent dot1 = (DamageOverTimeComponent) entMan.addComponent(GameComps.COMP_DAMAGE_OVER_TIME, entity);
		DamageOverTimeComponent dot2 = (DamageOverTimeComponent) entMan.addComponent(GameComps.COMP_DAMAGE_OVER_TIME, entity);
		HealthComponent health = (HealthComponent) entMan.addComponent(GameComps.COMP_RESOURCE_HEALTH, entity);

		assertNotNull("comp1 nulo", dot1);
		assertTrue("Identifier errado: " + dot1.getIdentifier(), dot1.getIdentifier().equals(GameComps.COMP_DAMAGE_OVER_TIME));
		health.setCurrValue(100);
		health.setMaxValue(100);
		health.alive = true;

		dot1.damageLeft = 10;
		dot2.damageLeft = 5;
		dot1.damageSecond = 1;
		dot2.damageSecond = 2;
		float esperado = 100;

		esperado = checkDamageApplied(entity, dot1, health, esperado, 1);
		esperado = checkDamageApplied(entity, dot2, health, esperado, 2);

		HealthSystem healthS = initHealthSystem();
		healthS.iterateEntity(entity, health, 1);

	}

	private float checkDamageApplied(IGameEntity entity, DamageOverTimeComponent dot1, HealthComponent health, float esperado, float dano) {
		esperado -= dano;
		DamageOverTimeSystem dotS = initDamageOverTimeSystem();
		dotS.iterateEntity(entity, dot1, 1);
		DamageReceivedComponent damageRec = (DamageReceivedComponent) entity.getComponentWithIdentifier(GameComps.COMP_DAMAGE_RECEIVED);
		assertNotNull(damageRec);
		DamageCalculatorSystem dmgCalcS = initDamageCalculatorSystem();
		dmgCalcS.iterateEntity(entity, damageRec, 1);
		assertTrue("Health mudou de forma errada: " + health.getCurrValue(), health.getCurrValue() == esperado);
		return esperado;
	}

	@Test
	public void testaWorld() {
		IGameElement world = game.getIntegrationState().getWorldElement();
		assertNotNull("world nulo", world);

		IGameEntity entity = game.getGameEntity();
		assertNotNull("entity nulo", entity);
		//assertEquals(world, entity.getElement());

	}

	@Test
	public void testaCriacaoGameStates() {

		BPBuilderState integr = (BPBuilderState) game.getIntegrationState();
		List<IGameElement> elStates = world.getObjectList(GameConsts.IDENT_STATES);
		assertNotNull("world nulo", world);
		ArrayList<IGameState> states = integr.getStates();
		assertEquals("Qtd States world(" + elStates.size() + ")<>integr(" + states.size() + ")", states.size(), elStates.size());
	}

	@Test
	public void testaCriacaoGameSystems() {

		BPBuilderState integr = (BPBuilderState) game.getIntegrationState();
		List<IGameElement> elSystems = world.getObjectList(GameConsts.IDENT_SYSTEM);
		assertNotNull("world nulo", world);

		entMan = integr.getEntityManager();

		List<IGameSystem> systems = entMan.getSystems();
		assertTrue(systems.size() > 0);
		assertTrue(elSystems.size() > 0);
		// assertEquals("Qtd Systems world(" + elSystems.size() + ")<>entMan("
		// + systems.size() + ")", systems.size(), elSystems.size());
		// integr.update(1);

	}

	@Test
	public void testaCargaElemento() throws IOException {

		IGameEntity geCircle = factory.createEntityFromTag(TestStrings.TEST_ENTITY_TAG);
		assertNotNull("TEST_ENTITY_TAG nulo", geCircle);
		assertNotNull("TEST_ENTITY_TAG.ge nulo", geCircle.getElement());
	}

	@Test
	public void testaAddEntity() {
		// game.sendEvent(event, eventInfo)
	}

}
