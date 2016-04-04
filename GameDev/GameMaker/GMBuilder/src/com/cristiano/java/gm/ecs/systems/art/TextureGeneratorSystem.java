package com.cristiano.java.gm.ecs.systems.art;

import java.awt.Color;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.builder.textures.AbstractProceduralTexture;
import com.cristiano.java.gm.builder.textures.IMakeTexture;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;

public class TextureGeneratorSystem extends TextureLoaderSystem {

	protected Texture applyToMapType(IGameEntity ent, MaterialComponent matComp, String mapType, ColorRGBA cor, List<IGameElement> list, TextureComponent comp) {
		Texture texture = null;
		// flip the texture...
		// matComp.mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

		if ((matComp.materialType == MaterialComponent.MAT_LIGHT) || (matComp.materialType == MaterialComponent.MAT_UNSHADED)) {
			float[][][] array = solveLayers(list, comp, matComp, mapType);
			texture = generateTexture(comp, matComp, true, mapType, array);
			texture.setWrap(WrapMode.BorderClamp);
			applyToMaterial(matComp, mapType, cor, texture, comp, matComp.materialType);
		} else if (matComp.materialType == MaterialComponent.MAT_TERRAIN) {
			float[][][] array = solveLayers(list, comp, matComp, "terrain");
			comp.applyRoadLayer(array);
			array = correctFinalArray(array);
			texture = generateTexture(comp, matComp, true, mapType, array);
			applyToTerrainMaterial(ent, matComp, mapType, cor, texture);

		} else {
			Log.error("Unknown Material:" + matComp.materialType);
		}
		return texture;
	}
	
	@Override
	protected void futureRequestMap(IGameEntity ent, TextureComponent comp, MaterialComponent matComp) {
		Bench.start("TextureLoaderSystem - generateMaps", BenchConsts.CAT_SYSTEM_UPDATE);
		comp._matComp = matComp;
		comp._ent = ent;
		comp._system = this;
		//comp.futureGenerateMap = "generateMap" + comp.getId();
		//FutureManager.requestFuture(comp.futureGenerateMap, comp.generateMapsMethod);
		try {
			comp.generateMapsMethod.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void checkFutureGenerateMap(TextureComponent comp) {
		if (comp.applyToSpatial != null) {
			applyToSpatialFutureInit(comp, comp._matComp);
		} else {
			comp.archive();
		}
	}

	@Override
	protected void applyToSpatialFutureInit(TextureComponent comp, MaterialComponent matComp) {
		comp._matComp = matComp;
		try {
			comp.applyToSpatialMethod.call();
			comp.archive();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private float[][][] correctFinalArray(float[][][] array) {
		int textLength = array[0].length;
		float[][][] ret = new float[textLength][textLength][GameConsts.DATA_SIZE];
		for (int i = 0; i < textLength; i++) {
			for (int j = 0; j < textLength; j++) {
				fixMultTexture(array, i, j);
				for (int k = 0; k < GameConsts.DATA_SIZE; k++) {
					ret[i][j][k] = array[i][j][k];
				}
			}
		}

		for (int i = 0; i < textLength; i++) {
			for (int j = 0; j < textLength; j++) {
				// this will make the road "thinner", by making it one tile
				// smaller when it has a neighbour...
				thinnerRoad(ret, array, i, j, 1);
			}
		}
		softenRoad(textLength, ret);

		// copying back the values...
		for (int i = 0; i < textLength; i++) {
			for (int j = 0; j < textLength; j++) {
				for (int k = 0; k < GameConsts.DATA_SIZE; k++) {
					array[i][j][k] = ret[j][i][k];
				}
			}
		}

		return array;
	}

	private void softenRoad(int textLength, float[][][] ret) {
		for (int i = 0; i < textLength; i++) {
			for (int j = 0; j < textLength; j++) {
				for (int k = 0; k < GameConsts.DATA_SIZE; k++) {
					// array[i][j][k] = ret[j][i][k];
					int qtdColor = countNeighbour(ret, i, j, GameConsts.ROAD_INDEX);

					if (qtdColor == 3) {
						ret[i][j][GameConsts.ROAD_INDEX] = 1;
						ret[i][j][GameConsts.ROAD_INDEX] = 0;
						ret[i][j][GameConsts.GREEN_INDEX] = 0;
					}
				}
			}
		}

	}

	private void thinnerRoad(float[][][] ret, float[][][] array, int i, int j, int k) {
		if (ret[i][j][GameConsts.ROAD_INDEX] == 1) {
			int qtdGreen = countNeighbour(array, i, j, GameConsts.GREEN_INDEX);
			int qtdRed = countNeighbour(array, i, j, GameConsts.RED_INDEX);
			if (qtdGreen > qtdRed) {
				ret[i][j][GameConsts.ROAD_INDEX] = 0;
				ret[i][j][GameConsts.GREEN_INDEX] = 1;
			} else if ((qtdRed >= qtdGreen) && (qtdRed > 0)) {
				ret[i][j][GameConsts.ROAD_INDEX] = 0;
				ret[i][j][GameConsts.RED_INDEX] = 1;
			}
		}
	}

	private int countNeighbour(float[][][] array, int x, int y, int index) {
		int tot = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if ((i >= 0) && (i < array[0].length)) {
					if ((j >= 0) && (j < array[0].length)) {
						tot += array[i][j][index];
					}
				}
			}
		}
		return tot;
	}

	private void fixMultTexture(float[][][] array, int i, int j) {
		// first, road less...
		if (array[i][j][GameConsts.ROAD_INDEX] == 0) {
			if ((array[i][j][GameConsts.GREEN_INDEX] == 1) && (array[i][j][GameConsts.RED_INDEX] == 1)) {
				array[i][j][GameConsts.GREEN_INDEX] = 0;
			}
			if ((array[i][j][GameConsts.GREEN_INDEX] == 0) && (array[i][j][GameConsts.RED_INDEX] == 0)) {
				array[i][j][GameConsts.GREEN_INDEX] = 1;
			}
		} else {
			array[i][j][GameConsts.GREEN_INDEX] = 0;
			array[i][j][GameConsts.RED_INDEX] = 0;
		}
	}

	private float[][][] solveLayers(List<IGameElement> list, TextureComponent comp, MaterialComponent matComp, String mapType) {
		float[][][] array = AbstractProceduralTexture.initArray(matComp.length, matComp.length);
		textureLayerize(comp, list, array, matComp);
		matComp.setArray(mapType, array);
		return array;
	}

	protected Texture generateTexture(TextureComponent comp, MaterialComponent matComp, boolean invertY, String mapType, float[][][] array) {
		Texture2D texture = new Texture2D();
		ByteBuffer buffer = AbstractProceduralTexture.criaBuffer(array, comp, invertY);
		Image img = new Image(Format.RGB8, matComp.length, matComp.length, buffer);
		texture.setImage(img);

		if (comp.exportTexture != null) {
			exportTexture(comp.exportTexture, mapType, array, matComp.length, 1024);
		}

		/*
		 * ByteBuffer buf = img.getData().get(0); buf.rewind();
		 * while(buf.remaining()>0){ buf.get(); }
		 */

		return texture;
	}

	public static void exportTexture(OutputStream outputStream, String suffix, float[][][] array, int length, int imageSize) {
		CRDebugDraw draw = generateDrawing(array, length, imageSize);
		draw.saveTo(outputStream);

	}

	public static void exportTexture(String exportTexture, String suffix, float[][][] array, int length, int imageSize) {

		CRDebugDraw draw = generateDrawing(array, length, imageSize);
		if (suffix == null) {
			draw.saveTo(exportTexture);
		} else {
			draw.saveTo(exportTexture + "_" + suffix + ".png");
		}
	}

	private static CRDebugDraw generateDrawing(float[][][] array, int length, int imageSize) {
		CRDebugDraw draw = new CRDebugDraw(length, imageSize);
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				float r = CRMathUtils.limitColor(array[i][j][0]);
				float g = CRMathUtils.limitColor(array[i][j][1]);
				float b = CRMathUtils.limitColor(array[i][j][2]);
				float a = 1;
				if (array[i][j].length == 4) {
					a = array[i][j][3];
				}
				float f = 0.5f;
				/*
				 * r = r / 2f + f; g = g / 2f + f; b = b / 2f + f;
				 */
				draw.drawPoint(i, j, new Color(r, g, b, a));
			}
		}
		return draw;
	}

	private void textureLayerize(TextureComponent comp, List<IGameElement> list, float[][][] buffer, MaterialComponent matComp) {
		int i = 0;
		initReuseComponent();
		for (IGameElement elLayer : list) {
			String textureType = elLayer.getProperty(GameProperties.TEXTURE_TYPE);
			IMakeTexture textureMaker = (IMakeTexture) reuseC.instantiateUniqueClass(textureType);
			textureMaker.generateTexture(elLayer, comp, buffer, i > 0, matComp);
			i++;
		}
	}

}
