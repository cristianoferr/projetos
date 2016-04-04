package com.cristiano.java.gm.ecs.systems.art;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.builder.textures.AbstractProceduralTexture;
import com.cristiano.java.gm.builder.utils.ExportUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.art.ImageRequestComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.persists.GameConstsComponent;
import com.cristiano.java.gm.ecs.comps.ui.AbstractUIComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;

/*
 * Unit Test: TestImageRequest
 * */
public class ImageRequestSystem extends JMEAbstractSystem {

	public ImageRequestSystem() {
		super(GameComps.COMP_REQUEST_IMAGE);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		ImageRequestComponent comp = (ImageRequestComponent) component;
		if (comp.imageGenerated) {
			if (deliverImage(comp)) {
				ent.removeComponent(comp);
			}
			return;
		}
		if (comp.firstTick) {
			initReuseComponent();
			if (hasAsset(comp)) {
				// ent.removeComponent(comp);
				return;
			}

			Log.info("Image Request arrived:" + comp.imageTag);
			GameConstsComponent gameConstsComponent = getGameConstsComponent();
			if (comp.assetMaterial == null) {
				comp.assetMaterial = gameConstsComponent.assetMaterial;
			}

			generateTextureRequest(comp.assetMaterial, comp, gameConstsComponent);
			comp.firstTick = false;
			return;
		}

		if (!checkIfItsDone(comp)) {
			return;
		}

		mergeTextures(comp);

		// ent.removeComponent(comp);
		comp.archive();

	}

	private boolean deliverImage(ImageRequestComponent comp) {
		List<IGameEntity> ents = entMan.getEntitiesWithComponent(comp);
		boolean b = true;
		for (IGameEntity ent : ents) {
			//
			if (ent instanceof AbstractUIComponent) {
				if (!deliverToNifty((AbstractUIComponent) ent, comp)) {
					b = false;
				}
			}
		}
		return b;
	}

	private boolean deliverToNifty(AbstractUIComponent ent, ImageRequestComponent comp) {

		if (ent.niftyElement == null) {
			Log.warn("Nifty Element is null...");
			return false;
		} else {
			updateImage(ent.niftyElement, comp);
			ent.removeComponent(comp);
			return true;
		}
	}

	private void updateImage(Element element, ImageRequestComponent comp) {
		String filePath = comp.getFilePath();
		// filePath="Interface/Images/gen/config.png";
		try {

			NiftyComponent niftyC = getNiftyComponent();
			NiftyImage img2 = null;
			img2 = niftyC.nifty.createImage(filePath, false);
			// img2 =
			// niftyC.nifty.createImage(GMAssets.getFullFilePath(filePath),
			// false);

			element.getRenderer(ImageRenderer.class).setImage(img2);
		} catch (Exception e) {
			Log.error("Error updating image:" + filePath + " error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void mergeTextures(ImageRequestComponent comp) {

		BufferedImage img = ExportUtils.loadImage(comp.imageSource);
		loadImage(img, comp);
	}

	private void loadImage(BufferedImage img, ImageRequestComponent comp) {
		MaterialComponent material = comp.getMaterial();
		float[][][] arrayFG = material.getArray(ImageRequestComponent.FG_MAP);
		float[][][] arrayBG = material.getArray(ImageRequestComponent.BG_MAP);
		float[][][] arrayAdorno = material.getArray(ImageRequestComponent.ADORNO_MAP);

		normalizaLimites(arrayFG, comp.finalSize);
		normalizaLimites(arrayBG, comp.finalSize);

		// Color[][] arrayBase=new Color[comp.finalSize][comp.finalSize];
		float[][][] arrayFinal = new float[comp.finalSize][comp.finalSize][GameConsts.DATA_SIZE + 1]; // +alpha

		int offset = (comp.finalSize - comp.defaultSize) / 2;
		IGameElement elBlending = material.merging;

		int[][] arrayTransparency = initTransparencyBuffer(offset, img, comp, arrayFG);

		applyLimitColors(comp, arrayFG, arrayBG);

		/*
		 * TextureGeneratorSystem.exportTexture(
		 * GMAssets.getFullFilePath(comp.getFilePath()), "bg", arrayBG,
		 * comp.finalSize, comp.finalSize);
		 * TextureGeneratorSystem.exportTexture(
		 * GMAssets.getFullFilePath(comp.getFilePath()), "fg", arrayFG,
		 * comp.finalSize, comp.finalSize);
		 */

		applyAdorno(arrayFinal, arrayAdorno, arrayFG, arrayBG, arrayTransparency, comp);

		/*
		 * TextureGeneratorSystem.exportTexture(
		 * GMAssets.getFullFilePath(comp.getFilePath()), "final_pos_adorno",
		 * arrayFinal, comp.finalSize, comp.finalSize);
		 * TextureGeneratorSystem.exportTexture(
		 * GMAssets.getFullFilePath(comp.getFilePath()), "bg_pos_adorno",
		 * arrayBG, comp.finalSize, comp.finalSize);
		 */

		applyLayers(comp, arrayFinal, arrayFG, arrayBG, elBlending, arrayTransparency);
		/*
		 * TextureGeneratorSystem.exportTexture(
		 * GMAssets.getFullFilePath(comp.getFilePath()),
		 * "final_pos_applylayers", arrayFinal, comp.finalSize, comp.finalSize);
		 */
		// TextureSystem.exportTexture(getExportPath(),"p4", arrayFinal,
		// comp.finalSize, 1024);
		// TextureSystem.exportTexture(comp.getFilePath(),"final-1024",
		// arrayFinal, comp.finalSize, 1024);
		TextureGeneratorSystem.exportTexture(comp.getOutputStream(), null, arrayFinal, comp.finalSize, comp.defaultSize);
		deliverAsset(comp);
	}

	private void normalizaLimites(float[][][] arrayFG, int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int c = 0; c < GameConsts.DATA_SIZE; c++) {
					arrayFG[i][j][c] = arrayFG[i][j][c] / 2f + 0.5f;
				}
			}
		}

	}

	private void applyAdorno(float[][][] arrayFinal, float[][][] arrayAdorno, float[][][] arrayFG, float[][][] arrayBG, int[][] arrayBase,
			ImageRequestComponent comp) {
		float[] adornoColor = comp.adornoColor.getColorComponents(null);
		for (int i = 0; i < comp.finalSize; i++) {
			for (int j = 0; j < comp.finalSize; j++) {
				// opaque
				arrayFinal[i][j][GameConsts.DATA_SIZE] = 1;

				for (int c = 0; c < GameConsts.DATA_SIZE; c++) {

					arrayFinal[i][j][c] = arrayBG[i][j][c];
					if (arrayAdorno[i][j][c] == 0) {
						arrayFinal[i][j][c] = adornoColor[c];
					}

					/*
					 * if (arrayAdorno[i][j][c] == 0) { arrayFinal[i][j][c] =
					 * arrayBG[i][j][c]; }
					 */

					if (arrayAdorno[i][j][c] < 0) {
						arrayBase[i][j] = 0;
					}
				}
			}
		}

	}

	private void applyLimitColors(ImageRequestComponent comp, float[][][] arrayFG, float[][][] arrayBG) {
		float[] rgbBG = comp.bgColor.getRGBColorComponents(null);
		float[] rgbFG = comp.fgColor.getRGBColorComponents(null);
		for (int i = 0; i < comp.finalSize; i++) {
			for (int j = 0; j < comp.finalSize; j++) {
				for (int c = 0; c < GameConsts.DATA_SIZE; c++) {
					arrayBG[i][j][c] = arrayBG[i][j][c] * rgbBG[c];
				}
				for (int c = 0; c < GameConsts.DATA_SIZE; c++) {
					arrayFG[i][j][c] = arrayFG[i][j][c] * rgbFG[c];
				}
			}
		}
	}

	private void applyLayers(ImageRequestComponent comp, float[][][] arrayFinal, float[][][] arrayFG, float[][][] arrayBG, IGameElement elBlending,
			int[][] arrayTransparency) {
		for (int i = 0; i < comp.finalSize; i++) {
			for (int j = 0; j < comp.finalSize; j++) {
				// 0 = transparent
				if (isNeighbourPoint(i, j, arrayTransparency, comp.finalSize)) {
					applyBlending(arrayFinal, arrayFG, arrayBG, i, j, elBlending, comp.finalSize);
				} else if (arrayTransparency[i][j] == 1) {
					applyPixel(arrayFinal, arrayFG, i, j);
					/*
					 * applyPixel(arrayFinal, arrayBG, i, j); }else {
					 */
				}
			}
		}
	}

	private void applyPixel(float[][][] arrayFinal, float[][][] arrayFG, int i, int j) {
		for (int c = 0; c < GameConsts.DATA_SIZE; c++) {
			arrayFinal[i][j][c] = arrayFG[i][j][c];
		}
	}

	private int[][] initTransparencyBuffer(int offset, BufferedImage img, ImageRequestComponent comp, float[][][] arrayFG) {
		int[][] arrayTransparency = new int[comp.finalSize][comp.finalSize];
		for (int i = 0; i < comp.finalSize; i++) {
			for (int j = 0; j < comp.finalSize; j++) {
				arrayTransparency[i][j] = 0;
			}
		}

		for (int i = 0; i < comp.defaultSize; i++) {
			for (int j = 0; j < comp.defaultSize; j++) {
				int rgb = img.getRGB(i, j);
				Color color = new Color(rgb, true);
				arrayTransparency[offset + i][offset + j] = (color.getAlpha() < 100) ? 0 : 1;
				float compArray[] = color.getComponents(null);
				if (comp.applySourceImgAsFG) {
					arrayFG[offset + i][offset + j][0] = compArray[0];
					arrayFG[offset + i][offset + j][1] = compArray[1];
					arrayFG[offset + i][offset + j][2] = compArray[2];
				}
			}
		}
		/*
		 * CRDebugDraw draw = new CRDebugDraw(comp.defaultSize,
		 * comp.defaultSize); for (int i = 0; i < comp.defaultSize; i++) { for
		 * (int j = 0; j < comp.defaultSize; j++) { int rgb = img.getRGB(i, j);
		 * Color color = new Color(rgb, true); float compArray[] =
		 * color.getComponents(null); Color colorTo=new
		 * Color(compArray[0],compArray[1],compArray[2],compArray[3]);
		 * draw.drawPoint(i, j, colorTo); } }
		 * draw.saveTo(GMAssets.getFullFilePath(comp.getFilePath() +
		 * "_raw.png")); TextureGeneratorSystem.exportTexture(
		 * GMAssets.getFullFilePath(comp.getFilePath()), "fg1", arrayFG,
		 * comp.finalSize, comp.finalSize);
		 */

		return arrayTransparency;
	}

	private void applyBlending(float[][][] buffer, float[][][] arrayFG, float[][][] arrayBG, int x, int y, IGameElement elBlending, int defaultSize) {
		// Calc the Blending...
		float red = arrayBG[x][y][GameConsts.RED_INDEX];
		float green = arrayBG[x][y][GameConsts.GREEN_INDEX];
		float blue = arrayBG[x][y][GameConsts.BLUE_INDEX];
		red = AbstractProceduralTexture.calcBlending(elBlending, "R", GameConsts.RED_INDEX, red, arrayFG, x, y, defaultSize);
		green = AbstractProceduralTexture.calcBlending(elBlending, "G", GameConsts.GREEN_INDEX, green, arrayFG, x, y, defaultSize);
		blue = AbstractProceduralTexture.calcBlending(elBlending, "B", GameConsts.BLUE_INDEX, blue, arrayFG, x, y, defaultSize);
		buffer[x][y][GameConsts.RED_INDEX] = red;
		buffer[x][y][GameConsts.GREEN_INDEX] = green;
		buffer[x][y][GameConsts.BLUE_INDEX] = blue;
	}

	// if a given point has transparent and non-transparent then it returns true
	public boolean isNeighbourPoint(int i, int j, int[][] arrayTransparency, int defaultSize) {
		int size = 1;
		int transPoints = 0;
		int nonTransPoints = 0;
		for (int x = i - size; x <= i + size; x++) {
			if ((x >= 0) && (x < defaultSize)) {
				for (int y = j - size; y <= j + size; y++) {
					if ((y >= 0) && (y < defaultSize)) {
						if (arrayTransparency[x][y] == 0) {
							transPoints++;
						} else {
							nonTransPoints++;
						}
					}
				}
			}
		}
		return ((transPoints > 0) && (nonTransPoints > 0));
	}

	private boolean hasAsset(ImageRequestComponent comp) {
		String asset = reuseC.getAsset(comp.imageTag);
		if (asset != null) {
			deliverAsset(comp);
			return true;
		}
		if (!comp.reusePreviousGen) {
			return false;
		}
		asset = comp.getFullFilePath();
		if (CRJavaUtils.existsFile(asset)) {
			deliverAsset(comp);
			return true;
		}

		return false;
	}

	private void deliverAsset(ImageRequestComponent comp) {
		Log.info("Delivering Art:" + comp.getFilePath());
		comp.imageGenerated = true;
		if (comp.destinationEntity == null) {
			return;
		}
		comp.destinationEntity.imageArt = comp.getFilePath();
		updateImageProperty(comp.destinationEntity, comp.destinationProp, comp.getFilePath());

	}

	public static void updateImageProperty(AbstractUIComponent uiComp, String prop, String imagePath) {
		if (uiComp.getElement() == null) {
			return;
		}
		uiComp.getElement().setProperty(prop, imagePath);
		List<IGameElement> properties = uiComp.getElement().getObjectList(GameProperties.PROPERTIES);
		for (IGameElement p : properties) {
			if (p.getProperty(GameProperties.KEY).equals(prop)) {
				p.setProperty(Extras.PROPERTY_VALUE, imagePath);
			}
		}
	}

	public boolean checkIfItsDone(ImageRequestComponent comp) {
		// TextureComponent textFG = getFGTexture(comp);
		// TextureComponent textBG = getBGTexture(comp);
		MaterialComponent matCompFG = comp.getMaterial();
		if (matCompFG.getArray(ImageRequestComponent.BG_MAP) == null) {
			return false;
		}
		if (matCompFG.getArray(ImageRequestComponent.FG_MAP) == null) {
			return false;
		}
		if (matCompFG.getArray(ImageRequestComponent.ADORNO_MAP) == null) {
			return false;
		}
		Log.debug("Textures have been generated...");
		return true;
	}

	private void generateTextureRequest(IGameElement assetMaterial, ImageRequestComponent comp, GameConstsComponent gameConstsComponent) {
		MaterialComponent matCompFG = (MaterialComponent) entMan.addComponent(GameComps.COMP_MATERIAL, comp);
		matCompFG.loadFromElement(assetMaterial);
		int finalSize = (int) (comp.defaultSize * assetMaterial.getPropertyAsFloat(GameProperties.RELATIVE_SIZE));
		matCompFG.length = finalSize;
		comp.finalSize = finalSize;

		TextureComponent textCompFG = (TextureComponent) entMan.addComponent(GameComps.COMP_TEXTURE, comp);
		// textCompFG.exportTexture = getExportPath();
	}

}
