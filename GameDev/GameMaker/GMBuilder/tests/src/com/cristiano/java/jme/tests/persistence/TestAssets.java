package com.cristiano.java.jme.tests.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.builder.textures.AbstractProceduralTexture;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.jme3.assets.GMAssets;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;

public class TestAssets extends MockAbstractTest{


	@Test
	public void testWriteReadByteBuffer() {
		TextureComponent comp = (TextureComponent) entMan.spawnComponent(GameComps.COMP_TEXTURE);
		comp.length=5;
		float[][][] array =new float[comp.length][comp.length][GameConsts.DATA_SIZE];
		for (int i=0;i<comp.length;i++){
			for (int j=0;j<comp.length;j++){
				for (int k=0;k<GameConsts.DATA_SIZE;k++){
					array[i][j][k]=i+j+k;
				}
			}
			
		}
		ByteBuffer buffer = AbstractProceduralTexture.criaBuffer(array, comp, false);
		Image img = new Image(Format.RGB8, comp.length, comp.length, buffer);
		List<ByteBuffer> data = img.getData();
		byte[]arr=new byte[0];
		String 	genFile = GMAssets.writeByteBuffer("test", GameConsts.ASSET_TEXTURE, data);
		assertNotNull(genFile);
		assertTrue(GMAssets.assetExists(genFile));
		
		ArrayList<ByteBuffer> fromBuffer = GMAssets.readByteBuffer("test", GameConsts.ASSET_TEXTURE);
		assertNotNull(fromBuffer);
		assertFalse(fromBuffer.isEmpty());
		buffer.rewind();
		byte bout=0; 
		ByteBuffer bufferIn = fromBuffer.get(0);
		bufferIn.rewind();
		while (buffer.remaining()>0){
			bout=buffer.get();
			byte bin=bufferIn.get();
			assertTrue(bin==bout);
		}
		entMan.removeEntity(comp);
		entMan.cleanup();
	}
	
	@Test
	public void testRequestAsset() {
		String file="Interface/Images/source/test/test1.png";
		assertTrue(GMAssets.assetExists(file));
		String newFile=GMAssets.requestAsset(file);
		assertNotNull(newFile);
		assertTrue(newFile.startsWith(GMAssets.GEN_FOLDER));
		assertTrue(GMAssets.assetExists(newFile));
		GMAssets.deleteAsset(newFile);
		assertFalse(GMAssets.assetExists(newFile));
	}
	
	@Test
	public void testExportImportHeightMap() {
		int size = 5000;
		float[] map = new float[size];
		for (int i = 0; i < size; i++) {
			map[i] =  (i%400 + (i / 3f));
		}
		float[] retMap = new float[size];

		String idRaw = "tstRaw";
		String assetFolder = "terrain";
		String idZip = "tstZip";
		
		cleanup(idRaw, assetFolder, idZip);
		
		assertFalse(GMAssets.assetExists(assetFolder+"/"+idRaw+GMAssets.ASSET_EXTENSION));
		assertFalse(GMAssets.assetExists(assetFolder+"/"+idRaw+GMAssets.ASSET_EXTENSION+".zip"));
		
		boolean exportHeightMap = GMAssets.exportHeightMap(assetFolder, idRaw, map,false);
		assertTrue(exportHeightMap);
		assertTrue(GMAssets.assetExists(GMAssets.GEN_FOLDER+assetFolder+"/"+idRaw+GMAssets.ASSET_EXTENSION));
		assertFalse(GMAssets.assetExists(GMAssets.GEN_FOLDER+assetFolder+"/"+idRaw+GMAssets.ASSET_EXTENSION+".zip"));
		retMap = GMAssets.importHeightMap(assetFolder, idRaw,false);
		validateHeightMap(size, map, retMap);
		
		
		assertTrue(GMAssets.exportHeightMap(assetFolder, idZip, map,true));
		assertFalse(".dat file exists on zip export",GMAssets.assetExists(assetFolder+"/"+idZip+GMAssets.ASSET_EXTENSION));
		assertTrue(GMAssets.assetExists("gen/"+assetFolder+"/"+idZip+GMAssets.ASSET_EXTENSION+".zip"));
		retMap = GMAssets.importHeightMap(assetFolder, idZip,true);
		validateHeightMap(size, map, retMap);
		
		cleanup(idRaw, "gen/"+assetFolder, idZip);
	}

	private void cleanup(String idRaw, String assetFolder, String idZip) {
		//removing assets (from previous tests...
		GMAssets.deleteAsset(assetFolder+"/"+idRaw+GMAssets.ASSET_EXTENSION);
		GMAssets.deleteAsset(assetFolder+"/"+idRaw+GMAssets.ASSET_EXTENSION+".zip");
		GMAssets.deleteAsset(assetFolder+"/"+idZip+GMAssets.ASSET_EXTENSION);
		GMAssets.deleteAsset(assetFolder+"/"+idZip+GMAssets.ASSET_EXTENSION+".zip");
	}

	private void validateHeightMap(int size, float[] map, float[] retMap) {
		assertNotNull("retMap is null", retMap);
		for (int i = 0; i < size; i++) {
			assertTrue("Valores diferem: " + map[i] + "<>" + retMap[i], map[i] == retMap[i]);
		}
	}
}
