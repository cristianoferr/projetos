package com.cristiano.java.jme.tests.texture;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.GameEntity;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.materials.IIterateMaterial;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialData;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.systems.art.TextureGeneratorSystem;
import com.cristiano.java.gm.ecs.systems.map.TerrainGeneratorSystem;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.hull.FaceSolver;
import com.cristiano.jme3.hull.Faces2D;
import com.cristiano.jme3.hull.convex.QuickHull3D;
import com.cristiano.jme3.hull.tests.TestFace;
import com.cristiano.jme3.interfaces.IFace;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

public class TestTextureSystem extends MockAbstractTest {

	private  final int DEFAULT_TEXTURE_SIZE = 16;
	private  final String TEXTURE_TAG="test";
	static IGameEntity entity = new GameEntity();
	
	private TextureGeneratorSystem textureS;
	private IGameElement elComp;
	private TextureComponent textComp;
	private MaterialComponent matcomp;

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	@Test
	public void testModelTextureSystem() {
		entity.removeAllComponents();
		initializer();
		MapComponent mapComponent = startMapComponent();
		
		SpatialComponent spatial = generateSpatial();
		assertNotNull(spatial);
		textComp.applyToSpatial=spatial;
		assertNull(textComp.applyToTerrain);
		assertNotNull(spatial.faces2D);
		
		//validating system...
		textureS.iterateEntity(mapComponent, textComp, 1);
		assertNotNull(textComp);
	}
	private SpatialComponent generateSpatial() {
		ArrayList<IFace> faces=TestFace.initFacesTubo();
		QuickHull3D hull=new QuickHull3D();
		Vector3f[] points = TestFace.extractPointsFrom(faces);
		assertTrue(points.length>3);
		hull.build(points);
		hull.setDebug(true);
		hull.generateMesh();
		//hull.exportInfo();
		
		ArrayList<IFace> allFaces = hull.getAllFaces();
		Faces2D faces2d = hull.getFaces2D();
		assertTrue(allFaces.size()>3);
		
		FaceSolver fs=new FaceSolver();
		fs.addVertexes(allFaces);
		fs.addTextCoords(faces2d);
		fs.generate();
		Mesh m = hull.generateMesh();
		Geometry geo = new Geometry("GeoDinamic", m);
		
		
		
		//mat.setTexture("DiffuseMap", getTexture(false));// texture principal
		//matcomp.mat.setColor("Ambient", ColorRGBA.White );
		//matcomp.mat.setColor("Diffuse", ColorRGBA.White);// cor principal
		geo.setMaterial(matcomp.mat());
		
		SpatialComponent spatial=(SpatialComponent) entMan.addComponent(GameComps.COMP_SPATIAL, entity);
		spatial.spatial(geo);
		spatial.faces2D=faces2d;

		entMan.addIfNotExistsComponent(GameComps.COMP_RENDER, entity);
		
		return spatial;
	}

	@Test
	public void testTerrainTextureSystem() {
		entity.removeAllComponents();
		
		initializer();
		
		MapComponent mapComponent = startMapComponent();
		
		TerrainComponent terrain = startTerrain(mapComponent,textComp);
		assertNotNull(terrain);
		//textComp.attachComponent(terrain);

		//validating system...
		textureS.iterateEntity(mapComponent, textComp, 1);
		assertNotNull(textComp);
		
		//TODO: falta aplicar a textura no terrain
	}
	private void initializer() {
		textureS = initTextureSystem();
		elComp = em.createElement();
		elComp.setProperty("identifier",GameComps.COMP_TEXTURE);
		assertNotNull(elComp);
		elComp.setPropertyTag(GameProperties.TEXTURE_TAG, TEXTURE_TAG);
		elComp=em.createFinalElement(elComp);
		assertNotNull(elComp);
		
		IGameElement elMaterial = em.pickFinal(TestStrings.MATERIAL_TEST);
		if (elMaterial==null){
			elMaterial=mockMaterialLayers();
		}
		assertNotNull(elMaterial);
		
		matcomp = (MaterialComponent) entMan.addComponent(GameComps.COMP_MATERIAL, entity);
		matcomp.loadFromElement(elMaterial);
		matcomp.materialType=MaterialComponent.MAT_TERRAIN;
		matcomp.mat(snippets.createMaterialLight());
		
		textComp = createTextureComponent(elComp,matcomp);
		assertNotNull(textComp);
	}

	private TextureComponent createTextureComponent(IGameElement elComp, MaterialComponent matcomp) {
		TextureComponent textComp = (TextureComponent) entMan.addComponent(elComp, entity);
		assertNotNull(textComp);
		assertTrue("empty procedures...",matcomp.mapSize()>0);
		textComp.length=DEFAULT_TEXTURE_SIZE;
		
		
		matcomp.iterateMaterials(new IIterateMaterial() {
			@Override
			public void iterate(String mapType, MaterialData matData) {
				List<IGameElement> layers = matData.getLayers();
		    	if (mapType.equals("diffuse")){
					assertTrue("Size should be >=1 but is "+layers.size(),layers.size()>=1);
				} else{
					//assertTrue("textureTag contem algo:"+mapType,layers.size()==0);
				}
			}
		});
		
		
		
		return textComp;
	}

	private TerrainComponent startTerrain( MapComponent mapComponent, TextureComponent textComp) {
		TerrainComponent terrainComponent = mockTerrainComponent(mapComponent);
		entity.attachComponent(terrainComponent);
		mapComponent.stageControl.setCurrentStage(MapComponent.TERRAIN_GENERATING);
		TerrainGeneratorSystem terrainSystem = initTerrainSystem();
		mapComponent.attachComponent(terrainComponent);
		
		terrainSystem.iterateEntity(mapComponent, terrainComponent, 0);
		
		for (int i=0;i<terrainComponent.getHeightMap().length;i++){
			terrainComponent.getHeightMap()[i]=i%500-250;
		}
		
		return terrainComponent;
	}

	
}
