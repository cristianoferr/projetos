package com.cristiano.java.jme.tests.full;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.gameObjects.tests.JMEAbstractTest;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.lifeCycle.MeshLoaderComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.unit.PhysicsComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.MeshLoaderSystem;
import com.cristiano.java.gm.ecs.systems.RenderSystem;
import com.cristiano.java.gm.ecs.systems.art.TextureGeneratorSystem;
import com.cristiano.java.gm.ecs.systems.unit.PhysicsSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.jme.tests.persistence.TestGameState;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.rigidBody.GMPhysicalVehicle;
import com.jme3.texture.Texture;

public class TestWithAllElements extends JMEAbstractTest{
	
	@BeforeClass
	public static void initializeTesting() throws IOException {
		startHeadless();
	}
	
	@Test public void testMacroDefinitions() throws IOException {
		assertNotNull("MacroDefinitions is null...",macroDefs);
		String gameGenreTag = macroDefs.resolveFunctionOf("pickSingle({ggfact gameGenre leaf})");
		assertFalse(gameGenreTag.equals(""));
		IGameElement gameGenre = macroDefs.getPropertyAsGE(GameProperties.GAME_GENRE);
		assertNotNull("gameGenre is null...",gameGenre);
		
		assertNotNull("world is null...",world);
		
		
	}
	
	
	@Test public void testExportImportFull() {
		TestGameState.validateExportImport(integr,game);
	}

	
	

	//Testing a full load of a unit, from its tag up to the creation of the mesh...
		@Test public void testLoadWheeledTank() {
		
			String tag = TestStrings.TAG_MESH_TANK_WHEELED;
			
			validateUnit(tag);
			
		}
		
		@Test public void testLoadWheeledTankSimple() {
			
			String tag = TestStrings.TAG_MESH_TANK_WHEELED;
			IGameEntity entity = BuilderUtils.createExampleUnit(entMan,em,tag);
			assertNotNull(entity);
			PhysicsComponent physC = validateLoadMesh(entity);
			assertNotNull(physC);
		}

		private void validateUnit(String tag) {
			IGameEntity entity = BuilderUtils.createExampleUnit(entMan,em,tag);
			assertNotNull(entity);
			
			PhysicsComponent physC = validateLoadMesh(entity);
			
			SpatialComponent spatC = (SpatialComponent)entity.getComponentWithIdentifier(GameComps.COMP_SPATIAL);
			assertNotNull("No spatial found on entity...",spatC);
			MaterialComponent matC = (MaterialComponent)spatC.getComponentWithIdentifier(GameComps.COMP_MATERIAL);
			assertNotNull(matC);
			TextureComponent textC = (TextureComponent)spatC.getComponentWithIdentifier(GameComps.COMP_TEXTURE);
			assertNotNull(textC);
			RenderComponent renderC=(RenderComponent) entity.getComponentWithIdentifier(GameComps.COMP_RENDER);
			
			assertTrue("No map was loaded for material.",matC.mapSize()>0);
			
			TextureGeneratorSystem textS = initTextureSystem();
			textS.iterateEntity(spatC, textC, 0);
			assertFalse("No texture generated",textC.isEmpty());
			Texture texture = textC.getTexture(GameConsts.TEXTURE_MAP_DIFFUSE);
			assertNotNull(texture);
			
			assertTrue(physC.isControllable);
			
			PhysicsSystem physicS = initPhysicsSystem();
			//criaControleFisico
			physicS.iterateEntity(entity, physC, 1);
			assertNotNull("Physics Node is null",physC.physNode);
			assertTrue(physC.isFirstTick());
			assertNotNull(physC.physNode);
			assertTrue(physC.physNode instanceof GMPhysicalVehicle);
			//anexaControleFisico
			physicS.iterateEntity(entity, physC, 1);
			assertFalse(physC.isFirstTick());
			assertTrue(renderC.node.getControl(0)==physC.physNode);
			//registerActions
			physicS.iterateEntity(entity, physC, 1);
			assertTrue(physC.actionsRegistered);
			
		}

		private PhysicsComponent validateLoadMesh(IGameEntity entity) {
			startFXLibraryComponent();
			
			RenderComponent renderC=(RenderComponent) entity.getComponentWithIdentifier(GameComps.COMP_RENDER);
			assertNotNull(renderC);
			PhysicsComponent physC=(PhysicsComponent) entity.getComponentWithIdentifier(GameComps.COMP_PHYSICS);
			assertNotNull(physC);
			
			MeshLoaderComponent meshLoaderC=(MeshLoaderComponent) entity.getComponentWithIdentifier(GameComps.COMP_MESH_LOADER);
			assertNull(meshLoaderC);
			
			RenderSystem renderS = initRenderSystem();
			renderS.iterateEntity(entity, renderC, 0);
			
			PositionComponent posC=(PositionComponent) entity.getComponentWithIdentifier(GameComps.COMP_POSITION);
			assertNull(posC);
			
			meshLoaderC=(MeshLoaderComponent) entity.getComponentWithIdentifier(GameComps.COMP_MESH_LOADER);
			assertNotNull(meshLoaderC);
			MeshLoaderSystem meshLoaderS = initMeshLoaderSystem();
			meshLoaderS.isDebugMode=true;
			meshLoaderS.iterateEntity(entity, meshLoaderC, 0);
			posC=(PositionComponent) entity.getComponentWithIdentifier(GameComps.COMP_POSITION);
			assertNotNull(posC);
			return physC;
		}
}