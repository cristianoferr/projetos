package com.cristiano.java.jme.tests.persistence;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.interfaces.IGameFactory;
import com.cristiano.java.jme.tests.mocks.MockFactory;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.jme3.visualizadores.CRSimpleGame;
import com.cristiano.utils.CRJavaUtils;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;

public class HelloVisualizaSavable extends CRSimpleGame{

	public static IGameFactory factory;
	
	public static void main(String[] args) {
		HelloVisualizaSavable app = new HelloVisualizaSavable();
		app.start();
	}
	
	public void simpleInitApp() {
		super.simpleInitApp();
		CRJavaUtils.IS_TEST=true;
		ScreenshotAppState screenShotState = new ScreenshotAppState();
		this.stateManager.attach(screenShotState);
		
		factory=new MockFactory(null, null, assetManager);
		Node node = (Node)factory.importSavable(418, GameConsts.ASSET_MESH);
		
		
		String textId = "600";
		Material mat=snippets.createMaterialLight();

		loadTexture(textId, "_diffuse", "DiffuseMap", mat);
		//loadTexture(textId, "_normal", "NormalMap", mat);
		loadTexture(textId, "_shine", "SpecularMap", mat);
		mat.setFloat("Shininess", 100);
		mat.setColor("Specular", ColorRGBA.White);
		
		node.setMaterial(mat);
		
		snippets.addSunLight(rootNode);
		snippets.createSkyBox();
		rootNode.attachChild(node);
		node.setLocalTranslation(Vector3f.ZERO);
		getFlyByCamera().setMoveSpeed(100);
	}

	private void loadTexture(String textId, String textType, String textMap, Material mat) {
		Texture texture = (Texture) factory.importSavable(textId+textType, GameConsts.ASSET_TEXTURE);
		ArrayList<ByteBuffer> data=GMAssets.readByteBuffer(textId+textType+"_data",GameConsts.ASSET_TEXTURE);
		texture.getImage().setData(data);
		
		mat.setTexture(textMap, texture);// texture principal
		//mat.setColor("Diffuse", ColorRGBA.White);
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
	}
}
