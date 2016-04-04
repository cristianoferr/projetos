package com.cristiano.java.gm.ecs.comps.materials;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.comps.visual.SkyBoxComponent;
import com.cristiano.java.gm.ecs.systems.art.TextureLoaderSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.MaterialHelper;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.texture.Texture;

public class TextureComponent extends GameComponent {

	// to who should this texture be applied... shouldnt be set directly
	public TerrainComponent applyToTerrain;
	public SpatialComponent applyToSpatial;
	public SkyBoxComponent applyToSkyBox;
	public String exportTexture; // texture path

	// final product
	private final HashMap<String, Texture> textures = new HashMap<String, Texture>();

	// copied from the material...
	public int length;
	public MaterialComponent _matComp;
	public TextureComponent _comp = this;
	public IGameEntity _ent;
	public TextureLoaderSystem _system;
	public String futureGenerateMap = null;
	public String futureApplyToSpatial = null;
	private JSONObject json;

	public TextureComponent() {
		super(GameComps.COMP_TEXTURE);
	}

	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	@Override
	public void free() {
		super.free();
		json = null;
		futureApplyToSpatial = null;
		textures.clear();
		_matComp = null;
		_comp = this;
		_system = null;
		futureGenerateMap = null;
		exportTexture = null;
		applyToTerrain = null;
		applyToSpatial = null;
		applyToSkyBox = null;
	}

	@Override
	public IGameComponent clonaComponent() {
		TextureComponent ret = (TextureComponent) entMan
				.spawnComponent(GameComps.COMP_TEXTURE);
		ret.textures.putAll(textures);
		ret.addInfo("Cloning texture... Size:"+textures.size());
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	public void applyRoadLayer(float[][][] array) {
		if (applyToTerrain == null) {
			Log.error("No Terrain defined");
			return;
		}
		applyToTerrain.applyRoadLayer(array);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.LENGTH, length);
		exportTextures(obj);

		return true;

	}

	private void exportTextures(JSONObject obj) {
		String keys = "";

		Iterator<String> it = textures.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String id = getId() + "_" + key;
			Texture texture = textures.get(key);
			ArrayList<ByteBuffer> data = (ArrayList<ByteBuffer>) texture
					.getImage().getData();
			data.get(0).rewind();
			while (data.get(0).remaining() > 0) {
				data.get(0).get();
			}
			// texture.getImage().setData((ArrayList<ByteBuffer>)null);
			entMan.getFactory().exportSavable(id, GameConsts.ASSET_TEXTURE,
					texture);
			keys += key + ";";
			// texture.getImage().setData(data);
			Log.debug("Exporting texture with id:" + id);

			GMAssets.writeByteBuffer(id + "_data", GameConsts.ASSET_TEXTURE,
					data);
			// data.get(0).rewind();
			// obj.put(id, data);
		}
		if (textures.isEmpty() || keys.equals("")) {
			Log.fatalIfRunning("No Textures being exported: " + this);
		}
		obj.put(GameProperties.KEYS, keys);
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		length = CRJsonUtils.getInteger(obj, GameProperties.LENGTH);
		this.json = obj;

	}

	private void importTextures() {
		if (json == null) {
			return;
		}
		String keys = (String) json.get(GameProperties.KEYS);
		String[] split = keys.split(";");
		Texture texture = null;
		for (String key : split) {
			String id = getId() + "_" + key;
			while (texture == null) {
				texture = (Texture) entMan.getFactory().importSavable(id,
						GameConsts.ASSET_TEXTURE);
				if (texture == null) {
					Log.warn("Texture with id '" + id
							+ "' is null, trying again...");
					// return;
				}
				CRJavaUtils.sleep(10);
			}

			ArrayList<ByteBuffer> data = GMAssets.readByteBuffer(id + "_data",
					GameConsts.ASSET_TEXTURE);
			texture.getImage().setData(data);
			textures.put(key, texture);
			json = null;
		}
		if (textures.isEmpty()) {
			Log.fatal("No Textures being imported");
		}
	}

	public void setTexture(String mapType, Texture texture) {
		importTextures();
		textures.put(mapType, texture);
	}

	public boolean isEmpty() {
		importTextures();
		return textures.isEmpty();
	}

	public Texture getTexture(String mapType) {
		importTextures();
		return textures.get(mapType);
	}

	// Multithreading

	public Callable<Object> applyToSpatialMethod = new Callable<Object>() {
		public Object call() throws Exception {
			MaterialHelper.applyTransparency(_matComp, applyToSpatial);
			linkaTexCoord(applyToSpatial);
			return null;
		}
	};

	protected void linkaTexCoord(SpatialComponent spatComp) {
		if (spatComp.getMesh() == null) {
			Log.error("No Mesh found for spatialComponent");
			return;
		}
		spatComp.getMesh().updateBound();
	}

	public Callable<Object> generateMapsMethod = new Callable<Object>() {
		public Object call() throws Exception {
			_system.generateMaps(_ent, _comp, _matComp);
			return null;
		}
	};

}
