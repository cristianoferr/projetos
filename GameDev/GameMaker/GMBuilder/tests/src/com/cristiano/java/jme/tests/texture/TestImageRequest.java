package com.cristiano.java.jme.tests.texture;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.art.ImageRequestComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.ui.AbstractUIComponent;
import com.cristiano.java.gm.ecs.systems.art.ImageRequestSystem;
import com.cristiano.java.gm.ecs.systems.art.TextureGeneratorSystem;
import com.cristiano.java.gm.nifty.NiftyPropertyApplier;
import com.cristiano.java.product.IGameElement;

public class TestImageRequest extends MockAbstractTest {

	private static final int IMG_SIZE = 128;

	@BeforeClass
	public static void setUpTest() throws IOException {
		startHeadless();
	}


	@Test
	public void testImageRequest() {
		AbstractUIComponent entity = (AbstractUIComponent) entMan.spawnComponent(GameComps.COMP_UI_CONTROL);
		GenericElement geImage = new GenericElement(em);// GeneratedImages
		geImage.setProperty(GameProperties.IMAGE, geImage.resolveFunctionOf("getImage(" + TestStrings.IMAGE_TEST_TAG + ")"));
		geImage.setProperty(GameProperties.IMAGE_TAG, TestStrings.IMAGE_TEST_TAG);
		geImage.setProperty(GameProperties.FILE_NAME, geImage.resolveFunctionOf("$this.image.sourceValue"));
		geImage.setProperty(GameProperties.FOLDER_DEST, geImage.resolveFunctionOf("$this.image.folderDest"));

		ImageRequestSystem imageReqS = initImageRequestSystem();
		ImageRequestComponent imageReqC = NiftyPropertyApplier.checkImageArt(geImage, entMan, entity);
		assertNotNull(imageReqC);
		imageReqC.reusePreviousGen = false;
		imageReqC.imageTag = "test";
		assertTrue("Wrong default size:" + imageReqC.defaultSize, imageReqC.defaultSize > 0);
		imageReqC.defaultSize = IMG_SIZE;
		imageReqC.assetMaterial=createLayersArt();
		// imageReqC.bgColor=Color.magenta;
		imageReqS.iterateEntity(entity, imageReqC, 0);
		
		

		MaterialComponent materialC = imageReqC.getMaterial();

		materialC.initMaterialFromSnippets(snippets);
		assertNotNull(materialC.mat());

		assertNotNull("Image Material is null", materialC);
		int mapSize = materialC.mapSize();
		assertTrue("There should be 3 layers but there are:" + mapSize, mapSize == 3);

		TextureComponent bgTexture = imageReqC.getTexture();
		assertNotNull(bgTexture);
		bgTexture.length = IMG_SIZE;

		TextureGeneratorSystem textureS = initTextureSystem();
		textureS.iterateEntity(imageReqC, bgTexture, 0);

		boolean isDone = imageReqS.checkIfItsDone(imageReqC);
		assertTrue("Should have completed...", isDone);

		assertTrue(imageReqS.checkIfItsDone(imageReqC));
		imageReqS.iterateEntity(entity, imageReqC, 0);
	}

	private IGameElement createLayersArt() {
		
		//bg
		IGameElement geDiffuse=em.createElement();
		String diffFunct = "0.8";
		geDiffuse.setProperty("blueFunction", diffFunct);
		geDiffuse.setProperty("redFunction", diffFunct);
		geDiffuse.setProperty("greenFunction", diffFunct);
		geDiffuse.setProperty("alphaFunction", "1");
		geDiffuse.setProperty("createdOn", "TestImageRequest.createLayersArt");
		geDiffuse.setProperty(GameProperties.TEXTURE_TYPE,TestStrings.TEXTURE_TYPE_INIT);
		
		//adorno
		IGameElement geNormal=em.createElement();
		String adornoFunction = "if($j<$length/4,1,0)";
		geNormal.setProperty("blueFunction", adornoFunction);
		geNormal.setProperty("redFunction", adornoFunction);
		geNormal.setProperty("greenFunction", adornoFunction);
		geNormal.setProperty("alphaFunction", "1");
		geNormal.setProperty("createdOn", "TestImageRequest.createLayersArt");
		geNormal.setProperty(GameProperties.TEXTURE_TYPE,TestStrings.TEXTURE_TYPE_INIT);
		
		//fg
		IGameElement geShine=em.createElement();
		geShine.setProperty("blueFunction", "0");
		geShine.setProperty("redFunction", "1");
		geShine.setProperty("greenFunction", "1");
		geShine.setProperty("alphaFunction", "1");
		geShine.setProperty("createdOn", "TestImageRequest.createLayersArt");
		geShine.setProperty(GameProperties.TEXTURE_TYPE,TestStrings.TEXTURE_TYPE_INIT);
		
		IGameElement geAsset=em.createElement();
		geAsset.setProperty(GameConsts.TEXTURE_MAP_DIFFUSE, geDiffuse);
		geAsset.setProperty(GameConsts.TEXTURE_MAP_NORMAL, geNormal);
		geAsset.setProperty(GameConsts.TEXTURE_MAP_SHINE, geShine);
		geAsset.setProperty(GameProperties.RELATIVE_SIZE, 1);
		geAsset.setProperty(GameProperties.MATERIAL_TYPE, MaterialComponent.MAT_LIGHT);
		geAsset.setProperty(GameProperties.COLOR,"#cccccc");
		
		geAsset.setProperty("createdOn", "TestImageRequest.createLayersArt");
		return geAsset;
		/*
		addMap(GameConsts.TEXTURE_MAP_DIFFUSE,ge);//bg
		addMap(GameConsts.TEXTURE_MAP_GLOW,ge);
		addMap(GameConsts.TEXTURE_MAP_NORMAL,ge);//adorno
		addMap(GameConsts.TEXTURE_MAP_SHINE,ge);//fg*/
	}

	@Test
	public void testImageRequestFunctions() {
		ImageRequestSystem imageReqS = initImageRequestSystem();
		int size = 10;
		int[][] transp = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				transp[i][j] = 0;
			}
		}

		assertFalse(imageReqS.isNeighbourPoint(0, 0, transp, size));
		assertFalse(imageReqS.isNeighbourPoint(size, 0, transp, size));
		assertFalse(imageReqS.isNeighbourPoint(size - 1, 0, transp, size));
		transp[0][0] = 1;
		assertTrue(imageReqS.isNeighbourPoint(0, 0, transp, size));
		assertTrue(imageReqS.isNeighbourPoint(1, 0, transp, size));
		assertTrue(imageReqS.isNeighbourPoint(1, 1, transp, size));
		assertFalse(imageReqS.isNeighbourPoint(2, 0, transp, size));
	}

}