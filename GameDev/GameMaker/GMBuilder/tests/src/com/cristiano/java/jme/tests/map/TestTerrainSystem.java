package com.cristiano.java.jme.tests.map;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.systems.art.TextureGeneratorSystem;
import com.cristiano.java.gm.ecs.systems.map.TerrainGeneratorSystem;
import com.cristiano.java.gm.ecs.systems.map.TerrainLoaderSystem;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.utils.JMESnippets;

public class TestTerrainSystem extends MockAbstractTest {

	
	private  final String TERRAIN_MATERIAL_TAG="material terrain leaf";
	private static JMESnippets snippets;
	private TextureGeneratorSystem textureS;
	private IGameElement elComp;
	private TextureComponent textComp;
	private MaterialComponent matcomp;
	private TerrainGeneratorSystem terrainSystem;
	private MapComponent mapComponent;

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	@Test
	public void testTerrainLoaderSystem() {
		initializer();
		TerrainComponent terrainComponent = mockTerrainComponent(mapComponent);
		mapComponent.attachComponent(terrainComponent);
		mapComponent.stageControl.setCurrentStage(MapComponent.TERRAIN_GENERATING);
		TerrainLoaderSystem terrainS=initTerrainLoaderSystem();
		
		terrainS.iterateEntity(mapComponent, terrainComponent, 0);
	}
	
	
	@Test
	public void testTerrainSystem() {
		initializer();
		
		TerrainComponent terrainComponent = mockTerrainComponent(mapComponent);
		mapComponent.attachComponent(terrainComponent);
		
		mapComponent.stageControl.setCurrentStage(MapComponent.TERRAIN_GENERATING);
		validateTerrainGenerating(terrainComponent);
		mapComponent.stageControl.setCurrentStage(MapComponent.TERRAIN_FINISHING);
		validateTerrainFinishing(terrainComponent);
	
	}
	
	private void validateTerrainFinishing(TerrainComponent terrainComponent) {
		
	}
	private void validateTerrainGenerating(TerrainComponent terrainComponent) {
		
	}
	private void initializer() {
		textureS = initTextureSystem();
		terrainSystem = initTerrainSystem();
		mapComponent = startMapComponent();
	}

	

	private TerrainComponent startTerrain( MapComponent mapComponent, TextureComponent textComp) {
		TerrainComponent terrainComponent = mockTerrainComponent(mapComponent);
		entity.attachComponent(terrainComponent);
		mapComponent.stageControl.setCurrentStage(MapComponent.TERRAIN_GENERATING);
		
		mapComponent.attachComponent(terrainComponent);
		
		terrainSystem.iterateEntity(mapComponent, terrainComponent, 0);
		
		for (int i=0;i<terrainComponent.getHeightMap().length;i++){
			terrainComponent.getHeightMap()[i]=i%500-250;
		}
		
		return terrainComponent;
	}

}
