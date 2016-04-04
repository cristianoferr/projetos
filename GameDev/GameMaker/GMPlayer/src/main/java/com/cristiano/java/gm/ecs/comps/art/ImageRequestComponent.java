package com.cristiano.java.gm.ecs.comps.art;

import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.ui.AbstractUIComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.Log;

public class ImageRequestComponent extends GameComponent {

	private static final String SAVE_FOLDER = "saveFolder";
	private static final String REUSE_PREVIOUS_GEN = "reusePreviousGen";
	private static final String ASSET_ROOT = "assetRoot";
	private static final String DEFAULT_SIZE = "defaultSize";

	public final static String BG_MAP = "diffuse";
	public final static String FG_MAP = "shine";
	public static final String ADORNO_MAP = "normal";

	public boolean reusePreviousGen = true;
	public int defaultSize = 128;
	public String assetRoot;
	public String imageTag;

	//TODO: colocar no blueprint a cor do adorno
	public Color adornoColor = Color.white;
	public Color bgColor = Color.white;
	public Color fgColor = Color.white; // limit color, white encompass all
										// colors...
	public int finalSize;
	public boolean imageGenerated = false;
	public String imageSource;
	public String destinationFile;
	public AbstractUIComponent destinationEntity;
	public String destinationProp;
	
	public IGameElement assetMaterial;
	
	//false=any non-transparent pixel will be replaced by the given FG pixel, true=it will use the pixel from the image
	public boolean applySourceImgAsFG=false;

	public ImageRequestComponent() {
		super(GameComps.COMP_REQUEST_IMAGE);

	}

	@Override
	public void free() {
		super.free();
		bgColor = Color.white;
		fgColor = Color.white;
		defaultSize = 128;
		reusePreviousGen = true;
		imageGenerated = false;
		assetRoot=null;
		imageTag=null;
		finalSize=0;
		imageSource=null;
		destinationFile=null;
		destinationEntity=null;
		destinationProp=null;
		
	}
	
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		defaultSize = ge.getPropertyAsInt(DEFAULT_SIZE);
		assetRoot = ge.getProperty(ASSET_ROOT);
		reusePreviousGen = ge.getPropertyAsBoolean(REUSE_PREVIOUS_GEN);
	}

	@Override
	public IGameComponent clonaComponent() {
		ImageRequestComponent ret = new ImageRequestComponent();
		finishClone(ret);
		return ret;
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		//obj.put(GameProperties.IMAGE_GENERATED, destinationFile);
		//obj.put(GameProperties.IMAGE_SOURCE, imageSource);
		return false;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		//loadFromElement(getElement());
		//destinationFile=(String) obj.get(GameProperties.IMAGE_GENERATED);	
	//	imageSource=(String) obj.get(GameProperties.IMAGE_SOURCE);	
	}

	@Override
	public void resetComponent() {
	}

	public TextureComponent getTexture() {
		return (TextureComponent) this.getComponentWithIdentifier(GameComps.COMP_TEXTURE);
	}

	public MaterialComponent getMaterial() {
		MaterialComponent matCompBG = (MaterialComponent) this.getComponentWithIdentifier(GameComps.COMP_MATERIAL);
		if (matCompBG==null){
			Log.error("No material defined for ImageRequest");
		}
		return matCompBG;
	}

	public String getFilePath() {
		//return saveFolder + imageTag + ".png";
		return destinationFile;
	}
	

	public OutputStream getOutputStream() {
		OutputStream output = GMAssets.getOutputStream(getFilePath());
		return output;

	}



	public String getFullFilePath() {
		String filePath = getFilePath();
		return GMAssets.getFullFilePath(filePath);
	}

	public InputStream getInputStream() {
		//String fileName = getBasePath(IManageElements);
		return GMAssets.getInputStream(imageSource);
	}

	

}
