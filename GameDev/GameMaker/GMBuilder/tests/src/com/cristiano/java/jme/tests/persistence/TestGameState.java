package com.cristiano.java.jme.tests.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.states.BPBuilderState;
import com.cristiano.java.gm.states.BPPlayerState;
import com.cristiano.java.gm.utils.JSONGameLoader;
import com.cristiano.java.gm.visualizadores.IFinalGame;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.Log;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class TestGameState extends MockAbstractTest {

	@BeforeClass
	public static void setUpTest() throws IOException {
		startHeadless();
	}

	@Test
	public void testWriteProperties() {
		String propName = GMAssets.getPropertiesPath();
		integr.writeGameProperties();
		assertTrue(GMAssets.assetExists(propName));

	}

	@Test
	public void testFactorySavable() {
		// exporting
		RenderComponent renderC = (RenderComponent) entMan
				.spawnComponent(GameComps.COMP_RENDER);
		renderC.node = new Node();
		renderC.node.attachChild(snippets.generateBox(ColorRGBA.Yellow,
				new Vector3f(10, 10, 10), new Vector3f(1, 2, 3)));
		String expectedPath = GMAssets.getPathForSavableEntity(renderC.getId(),
				GameConsts.ASSET_MESH);
		assertNotNull(expectedPath);
		GMAssets.deleteAsset(expectedPath);
		assertFalse(GMAssets.assetExists(expectedPath));
		String path = factory.exportSavable(renderC.getId(),
				GameConsts.ASSET_MESH, renderC.node);
		assertTrue(path.equals(expectedPath));
		assertTrue(GMAssets.assetExists(path));

		// importing
		Node nodeCopy = (Node) factory.importSavable(renderC.getId(),
				GameConsts.ASSET_MESH);
		assertNotNull(nodeCopy);
		assertFalse(nodeCopy == renderC.node);
		assertTrue(nodeCopy.getChildren().size() == renderC.node.getChildren()
				.size());
		assertTrue(nodeCopy.getLocalTranslation().equals(
				renderC.node.getLocalTranslation()));
		assertTrue(nodeCopy.getLocalRotation().equals(
				renderC.node.getLocalRotation()));
		assertTrue(nodeCopy.getChild(0).getLocalRotation()
				.equals(renderC.node.getChild(0).getLocalRotation()));
		assertTrue(nodeCopy.getChild(0).getLocalTranslation()
				.equals(renderC.node.getChild(0).getLocalTranslation()));

		// clean
		GMAssets.deleteAsset(expectedPath);
	}

	public static void validateWriteReadJSON(BPBuilderState integr) {
		String filePath = GMAssets.getJSONPath(GameProperties.ELEMENT_MANAGER);
		assertNotNull(filePath);
		GMAssets.deleteAsset(filePath);
		assertFalse(GMAssets.assetExists(filePath));
		assertTrue(integr.writeState());
		assertTrue(GMAssets.assetExists(filePath));

		BPPlayerState playerState = new BPPlayerState();
		assertNotNull(JSONGameLoader.getJSON(GameProperties.ELEMENT_MANAGER));
	}

	/*
	 * @Test public void testExportGame() {
	 * 
	 * }
	 * 
	 * @Test public void testImportGame() {
	 * 
	 * }
	 */
	/*
	 * @Test public void testImportExportGame() {
	 * TestGameState.validateExportImport(integr,game); }
	 */
	public static void validateExportImport(BPBuilderState fromState,
			IFinalGame game) {
		assertNotNull("game is null", game);
		assertNotNull("fromState is null", fromState);

		EntityManager entManFrom = fromState.getEntityManager();
		entManFrom.cleanup();
		IGameEntity entWorldFrom = entManFrom
				.getEntityWithComponent(GameComps.COMP_WORLD);
		fromState.writeState();

		assertNotNull(entWorldFrom);
		IGameComponent compTheme = entWorldFrom
				.getComponentWithIdentifier(GameComps.COMP_THEME);
		assertNotNull(compTheme);

		BPPlayerState toState = new BPPlayerState();
		toState.initialize(game.getStateManager(), game.getApplication());

		validateEntityManager(fromState, toState);
	}

	private static void validateEntityManager(BPBuilderState fromState,
			BPPlayerState toState) {
		// origin
		EntityManager entManFrom = fromState.getEntityManager();
		JSONObject jsonEnts = JSONGameLoader.getJSON(GameProperties.ENTITY_MANAGER);
		assertNotNull(jsonEnts);

		JSONObject ents = (JSONObject) jsonEnts.get(GameProperties.ENTITY);
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < ents.size(); i++) {
			JSONObject ent = (JSONObject) ents.get("ent" + i);
			assertNotNull("Entity ent" + i + " is null...");
			String strID = ent.get(GameProperties.ID).toString();
			// Log.debug("i:"+i+"strID:"+strID);
			int id = Integer.parseInt(strID);
			assertTrue("invalid id:" + id, id >= 0);
			isIdOK(ids, id);
			ids.add(id);
		}

		// destination
		EntityManager entManDest = toState.getEntityManager();
		// entManDest.
		// assertTrue(entManFrom.size()+"<>"+entManDest.size(),entManFrom.size()==entManDest.size());
		// entManDest.importFromJSON(jsonEnts);
		for (IGameEntity ent : entManDest.getEntities()) {
			assertNotNull(ent);
			IGameEntity entFrom = entManFrom.getEntityWithId(ent.getId());
			if (entFrom == null) {
				Log.warn("ent não encontrado na origem:" + ent);
			} else {
				if (ent instanceof GameComponent) {
					if ((entFrom instanceof GameComponent)) {
						validaComponent((GameComponent) ent,
								(GameComponent) entFrom);
					} else {
						Log.error("Object types differ: '" + ent + "' and '"
								+ entFrom + "'");
					}
				}
			}

		}
	}

	private static void validaComponent(GameComponent ent, GameComponent entFrom) {
		if (!ent.getIdentifier().equals(entFrom.getIdentifier())) {
			Log.warn("identifiers differ:" + ent + " <> " + entFrom);
		}
		// Log.warn("identifiers differ:"+ent+" <> "+entFrom);
		// assertTrue(ent.getIdentifier()+"<>"+entFrom.getIdentifier(),ent.getIdentifier().equals(entFrom.getIdentifier()));
		// assertTrue(ent.firstTick==entFrom.firstTick);
		// Log.debug("ent:"+ent);
		if (entFrom.getElement() != null) {
			// assertTrue("Erro na classe:" + entFrom.getClasse(),
			// entFrom.getClasse().equals(ent.getClasse()));
		}
	}

	private static void isIdOK(List<Integer> ids, int id) {
		for (Integer listId : ids) {
			assertFalse("Repeated ID:" + id, listId.intValue() == id);

		}

	}
}
