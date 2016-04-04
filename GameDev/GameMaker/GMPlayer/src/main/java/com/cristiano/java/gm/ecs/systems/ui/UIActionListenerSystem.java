package com.cristiano.java.gm.ecs.systems.ui;

import java.util.List;

import com.cristiano.benchmark.Bench;
import com.cristiano.java.gm.consts.GameActions;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.macro.GameEventComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.ApplyForceComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.PhysicsSpaceComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.comps.persists.GameConstsComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyElementComponent;
import com.cristiano.java.gm.ecs.comps.ui.UIActionComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.unit.PlayerComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamComponent;
import com.cristiano.java.gm.ecs.comps.unit.UnitClassComponent;
import com.cristiano.java.gm.ecs.comps.unit.resources.ResourceComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.OrientationComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.comps.visual.ThemeComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.units.controls.GMBombControl;
import com.cristiano.java.gm.units.controls.GMBulletDefines;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.IConsole;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IRigidBody;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.input.FlyByCamera;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;

import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.dropdown.DropDownControl;
import de.lessvoid.nifty.controls.textfield.TextFieldControl;

public class UIActionListenerSystem extends JMEAbstractSystem {

	boolean flagPhysDebug = false;
	private boolean flagGravity = false;
	private boolean flagCamera = true;

	public UIActionListenerSystem() {
		super(GameComps.COMP_UI_ACTION);
	}

	public static IConsole console = null;

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		UIActionComponent comp = (UIActionComponent) component;
		checkActions(ent, comp);
		entMan.removeComponentFromEntity(comp, ent);
	}

	private void checkActions(IGameEntity ent, UIActionComponent comp) {
		Log.debug("UIActionListenerSystem:" + comp.action);

		mapRelatedActions(ent, comp);
		if (comp.action.equals(GameActions.ACTION_DIAGNOSTICO)) {
			executaDiagnostico();
		}

		if (comp.action.equals(GameActions.ACTION_CONSOLE)) {
			if (console != null) {
				console.console(comp.auxInfo, entMan, getElementManager());
			}
		}
		if (comp.action.equals(GameActions.ACTION_LIST_LEVELS)) {
			// TODO: list levels
			listLevels();
		}
		if (comp.action.equals(GameActions.ACTION_BENCHMARK)) {
			System.out.println(Bench.report());
		}
		if (comp.action.equals(GameActions.ACTION_PAUSE_GAME)) {
			pauseGame();
		}
		if (comp.action.equals(GameActions.ACTION_DEBUG_GAME)) {
			debugGame();
		}
		if (comp.action.equals(GameActions.ACTION_START_GAME)) {
			goToRunningScreen();
		}
		if (comp.action.equals(GameActions.ACTION_GAME_CONFIG)) {
			configGame();
		}
		if (comp.action.equals(GameActions.ACTION_DEBUG_SPLASH)) {
			gotoScreen(GameConsts.SCREEN_SPLASH);
		}

		if (comp.action.equals(GameActions.ACTION_GOTO_VICTORY_SCREEN)) {
			gotoScreen("victory");
		}

		if (comp.action.equals(GameActions.ACTION_GOTO_FAIL_SCREEN)) {
			gotoScreen("fail");
		}

		if (comp.action.equals(GameActions.ACTION_RETURN_TITLE)) {
			returnTitle();
		}

		// debug
		if (comp.action.equals(GameActions.ACTION_TOGGLE_MOUSE)) {
			toggleMouse();
		}
		if (comp.action.equals(GameActions.ACTION_TOGGLE_PHYS_DEBUG)) {
			entMan.reportEntities();
			togglePhysicsDebug();
		}
		if (comp.action.equals(GameActions.ACTION_LAUNCH_BALL)) {
			launchBall(ent, comp);
		}

		if (comp.action.equals(GameActions.ACTION_SAVE_STATE)) {
			saveState();
		}
		if (comp.action.equals(GameActions.ACTION_TOGGLE_GRAVITY)) {
			toggleGravity();
			// getElementManager().getElementWithID(2320);
		}
		if (comp.action.equals(GameActions.ACTION_TOGGLE_CAMERA)) {
			toggleCamera();
		}
		if (comp.action.equals(GameActions.ACTION_LOAD_ENTITY)) {
			Log.warn("Not Implemented (nor will)");
		}
		if (comp.action.equals(GameActions.ACTION_RESET_PLAYER)) {
			resetPlayer(ent, comp);
		}

	}

	private void saveState() {
		game.getIntegrationState().writeState();

	}

	private void mapRelatedActions(IGameEntity ent, UIActionComponent comp) {
		if (comp.action.equals(GameActions.ACTION_RELOAD_GAME)) {
			reloadGame(ent);
		}

		if (comp.action.equals(GameActions.ACTION_NEXT_MAP)) {
			nextMap(ent);
		}
	}

	private void nextMap(IGameEntity ent) {
		MapWorldComponent mapW = getMapWorld();
		mapW.setStage(MapWorldComponent.NEXT_MAP);
		goToRunningScreen();

	}

	private MapWorldComponent getMapWorld() {
		MapWorldComponent mapW = (MapWorldComponent) game.getGameEntity()
				.getComponentWithIdentifier(GameComps.COMP_MAP_WORLD);
		return mapW;
	}

	private void reloadGame(IGameEntity ent) {
		MapWorldComponent mapW = getMapWorld();
		mapW.setStage(MapWorldComponent.RESET_MAP);
		goToRunningScreen();

	}

	private void executaDiagnostico() {
		Log.info("------------");
		Log.info("Running diagnostics...");
		IGameEntity gameEntity = getGameEntity();
		List<IGameComponent> gameComps = gameEntity.getAllComponents();
		Log.info("Components on gameEntity:" + gameComps.size());
		MapWorldComponent mapWorld = getMapWorldComponent();
		MapComponent mapC = getMap();
		GameConstsComponent gameConsts = getGameConstsComponent();
		MapLocationComponent mapLocal = getMapLocationComponent();

		diagnosticaComponent("mapLocal", mapLocal);
		Log.info("  mapLocal.mapSolverFilter:" + mapLocal.mapSolverFilter);
		Log.info("  mapLocal.roomBubbleFilter:" + mapLocal.roomBubbleFilter);
		diagnosticaComponent("mapWorld", mapWorld);
		executaDiagnosticoMapa(mapC);
		diagnosticaComponent("gameConsts", gameConsts);

	//	diagnosticaRoles();

		Log.debug("UNITS");
		List<IGameEntity> units = entMan
				.getEntitiesWithComponent(GameComps.COMP_TARGETABLE);
		for (IGameEntity unit : units) {
			// diagnosticaComponent(" unit",unit);
		}
	}
/*
	private void diagnosticaRoles() {
		UnitRolesComponent unitRolesData = getUnitRolesData();
		diagnosticaComponent("unitRolesData", unitRolesData);
		for (UnitClassComponent role : unitRolesData.classes) {
			Log.debug(" Role:" + role.identifier);
			for (ResourceComponent res : role.unitResources) {
				diagnosticaResource(res);
			}
			Log.debug("-----");
		}
	}
*/
	private void diagnosticaResource(ResourceComponent res) {
		diagnosticaComponent("   Resource:", res);
		// Log.debug("    Resource: "+res.getIdentifier()+" CurrValue:"+res.getCurrValue()+" startingValue:"+res.startingValue+" weight:"+res.weight+" budgetPrice:"+res.budgetPrice);
	}

	private void diagnosticaComponent(String tipo, IGameComponent comp) {
		if (comp == null) {
			Log.error(tipo + " is null");
			return;
		}
		Log.info(tipo + " info:" + comp.getInfo());
	}

	private void executaDiagnosticoMapa(MapComponent mapC) {
		if (mapC == null) {
			return;
		}
		Log.info("MAP DIAGNOSTIC");
		diagnosticaComponent("  Map", mapC);
		Log.info("  Map Stage: " + mapC.getStage());
		Log.info("  PopulateMap: " + mapC.populateMap);
		Log.info("  roadFunctionTag: " + mapC.roadFunctionTag);
		Log.info("  length: " + mapC.length);
		Log.info("  roadPoints: " + mapC.roadPoints);
		Log.info("  roadWidth: " + mapC.roadWidth);

		List<IGameComponent> teamComps = mapC
				.getComponentsWithIdentifier(GameComps.COMP_TEAM);
		diagnosticoGameGenre();

		Log.info("TEAM");
		Log.info("  Teams on map:" + teamComps.size());
		for (IGameComponent comp : teamComps) {
			TeamComponent team = (TeamComponent) comp;
			executaDianosticoTeam(team);
		}

		TerrainComponent terrain = (TerrainComponent) mapC
				.getComponentWithIdentifier(GameComps.COMP_TERRAIN);
		Log.info("TERRAIN");
		diagnosticaComponent("  terrain", terrain);
		executaDianosticoTerrain(terrain);

		Log.info("ROOM");
		Log.info("  map.roomHeight: " + mapC.roomHeight);
		Log.info("  map.maxRooms: " + mapC.maxRooms);
		Log.info("  map.maxRoomSize: " + mapC.maxRoomSize);
		List<IGameComponent> roomComps = mapC
				.getComponentsWithIdentifier(GameComps.COMP_ROOM);
		Log.info("  Rooms on map:" + roomComps.size());
		for (IGameComponent comp : roomComps) {
			RoomComponent team = (RoomComponent) comp;
			executaDianosticoRoom(team);
		}

		Log.info("END MAP DIAGNOSTIC");

	}

	private void diagnosticoGameGenre() {
		Log.info("GAMEGENRE");
		GameGenreComponent gameGenre = getGameGenre();
		diagnosticaComponent("  gameGenre", gameGenre);
		Log.info("  npcBudget:" + gameGenre.npcBudget);
		Log.info("  playerBudget:" + gameGenre.playerBudget);
		Log.info("  events:" + gameGenre.eventsSize());
		for (GameEventComponent event : gameGenre.getEvents()) {
			Log.info("    EventInfo:" + event.getInfo());
		}
	}

	private void executaDianosticoTerrain(TerrainComponent terrain) {
		diagnosticaComponent("  Terrain", terrain);
		if (terrain == null) {
			return;
		}
		Log.info("  Terrain Length:" + terrain.lengthOnPower);
		Log.info("  Terrain Type:" + terrain.terrainType);
		Log.info("  Terrain maxHeight:" + terrain.maxHeight);
		Log.info("  Terrain minHeight:" + terrain.minHeight);
		// List<IGameComponent> allComps =
		// terrain.getComponentsWithTag(Extras.TAG_ALL);

	}

	private void executaDianosticoRoom(RoomComponent room) {
		diagnosticaComponent("  room", room);
		List<IGameComponent> roadComps = room
				.getComponentsWithIdentifier(GameComps.COMP_ROAD);
		Log.info("  Room has " + roadComps.size() + " roads");

	}

	private void executaDianosticoTeam(TeamComponent team) {
		Log.info("  Team on Stage:" + team.getStage());
		List<IGameComponent> comps = team.getAllComponents();
		Log.info("  Components on Team:" + comps.size());
	}

	private void listLevels() {
		// TODO Auto-generated method stub

	}

	private void pauseGame() {
		ThemeComponent theme = getTheme();
		IGameElement elScreen = theme.elPauseScreen;
		changeScreen(elScreen);

	}

	private void debugGame() {
		ThemeComponent theme = getTheme();
		IGameElement elScreen = theme.elDebugScreen;
		changeScreen(elScreen);

	}

	private void returnTitle() {
		ThemeComponent theme = getTheme();
		IGameElement elScreen = theme.elStartScreen;
		changeScreen(elScreen);

	}

	private void configGame() {
		ThemeComponent theme = getTheme();
		IGameElement elScreen = theme.elConfigScreen;
		changeScreen(elScreen);

	}

	private void gotoScreen(String screenName) {
		IGameEntity gameEnt = getGameEntity();
		NiftyComponent niftyC = (NiftyComponent) gameEnt
				.getComponentWithIdentifier(GameComps.COMP_NIFTY);
		niftyC.changeScreenTo(screenName);
	}

	private void goToRunningScreen() {
		ThemeComponent theme = getTheme();
		if (theme == null) {
			Log.error("Theme not found.");
			return;
		}
		IGameElement elScreen = theme.elRunningScreen;
		changeScreen(elScreen);
	}

	private void resetPlayer(IGameEntity ent, UIActionComponent comp) {
		IGameEntity playerEnt = getPlayerEntity();
		PlayerComponent playerC = getPlayerComponent();
		PositionComponent posC = ECS.getPositionComponent(playerEnt);
		if (posC == null) {
			return;
		}
		RoomComponent room = ECS.getRoomWithId(getMap(), 1);
		if (room == null) {
			Log.error("No room 1 found!");
			posC.setPos(Vector3f.ZERO);
		} else {
			posC.setPos(room.getPosition().add(0, 10, 0));
		}

		OrientationComponent orientationComponent = ECS
				.getOrientationComponent(playerEnt);
		orientationComponent.setOrientation(new Quaternion());
		// PhysicsComponent physC=getPhysicsComponent(playerEnt);
		// physC.controlBody.setSpatial(posC.node);

		List<IGameEntity> ents = entMan
				.getEntitiesWithComponent(GameComps.COMP_AI);
		for (IGameEntity aiEnt : ents) {
			posC = ECS.getPositionComponent(aiEnt);
			posC.setPos(room.getPosition().add(CRJavaUtils.random() * 10,
					CRJavaUtils.random() * 10, CRJavaUtils.random() * 10));
			Log.debug("AI Pos:" + posC.getPos());
		}
		// game.getAudioRenderer().setEnvironment(new
		// Environment(Environment.Dungeon));

	}

	private void toggleCamera() {
		CamComponent camC = getCamComponent();
		flagCamera = !flagCamera;
		camC.setEnabled(flagCamera);
		IGameEntity playerEnt = getPlayerEntity();
		if (playerEnt == null) {
			return;
		}
		PhysicsComponent physC = ECS.getPhysicsComponent(playerEnt);
		if (physC == null) {
			return;
		}
		physC.setEnabled(flagCamera);
		FlyByCamera flyByCamera = game.getFlyByCamera();
		if (flyByCamera != null) {
			flyByCamera.setEnabled(!flagCamera);
		}

	}

	private void toggleGravity() {
		PhysicsSpaceComponent physSpaceComp = (PhysicsSpaceComponent) game
				.getGameEntity().getComponentWithIdentifier(
						GameComps.COMP_PHYSICS_SPACE);
		flagGravity = !flagGravity;
		if (flagGravity) {
			physSpaceComp.setGravity(new Vector3f(0, -10, 0));
		} else {
			physSpaceComp.setGravity(Vector3f.ZERO);

		}
	}

	private void togglePhysicsDebug() {
		PhysicsSpaceComponent physSpaceComp = (PhysicsSpaceComponent) game
				.getGameEntity().getComponentWithIdentifier(
						GameComps.COMP_PHYSICS_SPACE);
		flagPhysDebug = !flagPhysDebug;
		if (physSpaceComp.physics == null) {
			Log.error("No physics defined.");
			return;
		}
		if (physSpaceComp.physics.getPhysicsSpace() == null) {
			Log.error("No physicsSpaces defined.");
			return;
		}
		if (flagPhysDebug) {
			physSpaceComp.physics.getPhysicsSpace().enableDebug(
					game.getAssetManager());
		} else {
			physSpaceComp.physics.getPhysicsSpace().disableDebug();
		}

		List<IGameEntity> ents = entMan
				.getEntitiesWithComponent(GameComps.COMP_PHYSICS);
		for (IGameEntity ent : ents) {
			PhysicsComponent physComp = (PhysicsComponent) ent
					.getComponentWithIdentifier(GameComps.COMP_PHYSICS);
			if (physComp.physNode != null) {
				physComp.physNode.setLinearVelocity(Vector3f.ZERO);
				physComp.physNode.setAngularVelocity(Vector3f.ZERO);
			} else {
				Log.error("PhysicsComponent has no Physnode set...");
			}
		}

	}

	private String getTextFrom(IGameElement element) {
		String textTag = element.getProperty("textEdit");
		String ddTag = element.getProperty("dropDownSource");
		// String name=textEnt.getElement().getProperty(Extras.PROPERTY_NAME);

		if (!textTag.equals("")) {
			// niftyElement.control;
			IGameEntity textEnt = entMan.getEntityWithTag(textTag);
			NiftyElementComponent niftyElement = (NiftyElementComponent) textEnt
					.getComponentWithIdentifier(GameComps.COMP_UI_NIFTY_ELEMENT);
			TextField textField = niftyElement.control
					.getControl(TextFieldControl.class);
			return textField.getText();
		}
		if (!ddTag.equals("")) {
			// niftyElement.control;
			IGameEntity textEnt = entMan.getEntityWithTag(ddTag);
			NiftyElementComponent niftyElement = (NiftyElementComponent) textEnt
					.getComponentWithIdentifier(GameComps.COMP_UI_NIFTY_ELEMENT);
			DropDown textField = niftyElement.control
					.getControl(DropDownControl.class);
			return (String) textField.getSelection();
		}
		Log.error("Erro: texto n�o encontrando no elemento:"
				+ element.getIdentifier());
		return null;
	}

	private void launchBall(IGameEntity ent, UIActionComponent comp) {
		CamComponent compCam = getCamComponent();

		IGameEntity entBullet = entMan.createEntity();
		comp.entityGenerated = entBullet;
		RenderComponent compRender = (RenderComponent) entMan.addComponent(
				GameComps.COMP_RENDER, entBullet);
		PhysicsComponent compPhysics = (PhysicsComponent) entMan.addComponent(
				GameComps.COMP_PHYSICS, entBullet);
		ApplyForceComponent compApplyForce = (ApplyForceComponent) entMan
				.addComponent(GameComps.COMP_APPLY_FORCE, entBullet);
		compApplyForce.applyForce = true;

		Node node = new Node();
		compRender.node = node;

		if (compCam.cam != null) {
			compApplyForce.force.set(compCam.cam.getDirection()
					.mult(135 * 1000));
			node.setLocalTranslation(compCam.cam.getLocation());
		} else {
			compApplyForce.force.set(0, 10, 0);
			node.setLocalTranslation(new Vector3f());
		}
		// compRenderParent.node.attachChild(compRender.node);
		try {

			Geometry bulletg = criaRenderizacaoBullet();

			SphereCollisionShape bulletCollisionShape = new SphereCollisionShape(
					0.4f);
			// GMRigidBody bulletNode = new GMRigidBody(bulletCollisionShape,
			// 1500f);
			GMBulletDefines bulletDefines = new GMBulletDefines();
			bulletDefines.mass = 1500;
			bulletDefines.gravity = Vector3f.ZERO;
			IRigidBody bulletNode = new GMBombControl(game.getAssetManager(),
					bulletCollisionShape, bulletDefines);
			compPhysics.physNode = bulletNode;
			node.attachChild(bulletg);

			// node.addControl(bulletNode);

			// getPhysicsSpace().add(bulletNode);
			// compPhys.physics.getPhysicsSpace().add(bulletNode);

			// entMan.getAllEntitiesWithComponent(ent);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	private Geometry criaRenderizacaoBullet() {
		Sphere bullet;
		bullet = new Sphere(32, 32, 0.4f, true, false);
		bullet.setTextureMode(TextureMode.Projected);
		Material mat2 = new Material(game.getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		TextureKey key2 = new TextureKey(
				"Textures/terrain/rock/gray/Pavement.jpg");
		key2.setGenerateMips(true);
		Texture tex2 = game.getAssetManager().loadTexture(key2);
		mat2.setTexture("ColorMap", tex2);

		Geometry bulletg = new Geometry("bullet", bullet);
		bulletg.setMaterial(mat2);
		bulletg.setShadowMode(ShadowMode.CastAndReceive);

		return bulletg;
	}

	private void toggleMouse() {
		FlyByCamera flyByCamera = game.getFlyByCamera();
		if (flyByCamera == null) {
			Log.warn("FlyByCamera is null...");
			return;
		}
		boolean b = !flyByCamera.isEnabled();
		flyByCamera.setEnabled(b);
		flyByCamera.setDragToRotate(!b);

	}

}
