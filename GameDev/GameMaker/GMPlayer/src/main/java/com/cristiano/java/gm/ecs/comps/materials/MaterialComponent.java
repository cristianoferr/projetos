package com.cristiano.java.gm.ecs.comps.materials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.json.simple.JSONObject;

import com.cristiano.benchmark.Bench;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.export.Savable;
import com.jme3.material.Material;

public class MaterialComponent extends GameComponent {
	// used for model editing
	public static boolean debugApplyMaterial = true;

	public static final int MAT_LIGHT = 1;
	public static final int MAT_TERRAIN = 2;
	public static final int MAT_UNSHADED = 3;

	private List<String> layersToRemove = null;

	private final HashMap<String, MaterialData> maps = new HashMap<String, MaterialData>();

	public int materialType = 0;
	public boolean castShadows = false;
	private Material mat = null;
	private float density;
	public int length;
	public boolean isTransparent;
	public int seed;
	public String color;
	public int shineIntensity;
	public IGameElement blending;
	public IGameElement merging;// used for 2d art

	private String futureMaterial = null;

	private boolean needsImporting = false;

	public MaterialComponent() {
		super(GameComps.COMP_MATERIAL);
	}

	@Override
	public void free() {
		super.free();
		materialType = 0;
		castShadows = false;
		needsImporting = false;
		layersToRemove = null;
		mat = null;
		futureMaterial = null;
		density = 0;
		length = 0;
		isTransparent = false;
		seed = 0;
		color = null;
		shineIntensity = 0;
		blending = null;
		merging = null;
	}

	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		materialType = ge.getPropertyAsInt(GameProperties.MATERIAL_TYPE);
		density = ge.getPropertyAsFloat(GameProperties.DENSITY);
		color = ge.getProperty(GameProperties.COLOR);
		seed = ge.getPropertyAsInt(GameProperties.SEED);
		shineIntensity = ge.getPropertyAsInt(GameProperties.SHINE_INTENSITY);
		// TODO:castShadows desabilitado...
		// castShadows=ge.getPropertyAsBoolean(GameProperties.CAST_SHADOWS);
		length = ge.getPropertyAsInt(GameProperties.LENGTH);
		isTransparent = ge.getPropertyAsBoolean(GameProperties.IS_TRANSPARENT);
		blending = ge.getPropertyAsGE(GameProperties.TEXTURE_BLENDING);
		merging = ge.getPropertyAsGE(GameProperties.TEXTURE_MERGING);

		addMap(GameConsts.TEXTURE_MAP_DIFFUSE, ge);// bg
		addMap(GameConsts.TEXTURE_MAP_GLOW, ge);
		addMap(GameConsts.TEXTURE_MAP_NORMAL, ge);// adorno
		addMap(GameConsts.TEXTURE_MAP_SHINE, ge);// fg

		if (maps.isEmpty()) {
			Log.fatal("Material Maps is empty!");
		}

		if (!debugApplyMaterial) {
			maps.clear();
		}
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		if (mat == null) {
			Log.fatal("Material is null when exporting!");
			return false;
		}
		if (getElement() == null) {
			Log.fatal("Material without element when exporting.");
		}
		entMan.getFactory().exportSavable(getId(), GameConsts.ASSET_MATERIAL,
				(Savable) mat);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		Bench.start("MaterialComponent - loadFromElement",
				BenchConsts.CAT_COMP_INIT);
		loadFromElement(getElement());
		Bench.end("MaterialComponent - loadFromElement");

		needsImporting = true;

		//futureMaterial = "material" + getId();
		//FutureManager.requestFuture(futureMaterial, loadMaterial);
	}

	// This method doesn´t go well with multi-threading...
	private Material importMaterial() {
		if (!needsImporting) {
			return mat;
		}
		Material mat = null;
		while (mat == null) {
			Bench.start(BenchConsts.EV_MATERIAL_LOADER,
					BenchConsts.CAT_ATOMIC_INIT);
			int id = getId();
			Log.info("Importing material: " + id);
			mat = (Material) entMan.getFactory().importSavable(id,
					GameConsts.ASSET_MATERIAL);
			if (mat == null) {
				Log.error("Material is null when importing:" + id+", trying again.");
			}
			CRJavaUtils.sleep(50);
		}
		Bench.end(BenchConsts.EV_MATERIAL_LOADER);
		return mat;
	}

	public Callable<Object> loadMaterial = new Callable<Object>() {
		public Object call() throws Exception {
			Material mat = importMaterial();
			return mat;
		}
	};

	private void addMap(String textureMap, IGameElement ge) {
		List<IGameElement> layers = ge.getPropertyAsGEList(textureMap);
		if (layers.isEmpty()) {
			return;
		}
		MaterialData mapData = getMap(textureMap);
		mapData.setLayers(layers);
	}

	private MaterialData getMap(String textureMap) {
		MaterialData mapData = maps.get(textureMap);
		if (mapData == null) {
			mapData = new MaterialData(textureMap);
			maps.put(textureMap, mapData);
		}
		return mapData;
	}

	@Override
	public IGameComponent clonaComponent() {
		MaterialComponent ret = (MaterialComponent) entMan
				.spawnComponent(ident);
		ret.mat = mat();
		ret.setElement(getElement());

		ret.materialType = materialType;
		ret.color = color;
		ret.maps.putAll(maps);
		ret.density = density;
		ret.seed = seed;
		ret.castShadows = castShadows;
		ret.shineIntensity = shineIntensity;

		ret.isTransparent = isTransparent;
		ret.length = length;
		return ret;
	}

	@Override
	public void resetComponent() {
	}

	public void initMaterialFromSnippets(JMESnippets snippets) {

		if (snippets == null) {
			Log.error("Snippets=null!");
			return;
		}
		if (materialType == MAT_LIGHT) {
			mat = snippets.createMaterialLight();
			return;
		}
		if (materialType == MAT_TERRAIN) {
			mat = snippets.createMaterialTerrain();
			return;
		}
		if (materialType == MAT_UNSHADED) {
			mat = snippets.createMaterialUnshaded();
			return;
		}
		Log.error("Unkwnon materialType:" + materialType);

	}

	public void setArray(String mapType, float[][][] array) {
		getMap(mapType).setArray(array);
	}

	public float[][][] getArray(String mapType) {
		return getMap(mapType).getArray();
	}

	public void iterateMaterials(IIterateMaterial iIterateMaterial) {
		if (maps.size() == 0) {
			Log.error("No proceduralTextures defined in material!" + getId());
			return;
		}

		Iterator<Entry<String, MaterialData>> it = maps.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, MaterialData> pairs = (Map.Entry<String, MaterialData>) it
					.next();
			String mapType = pairs.getKey();
			MaterialData matData = maps.get(mapType);
			iIterateMaterial.iterate(mapType, matData);
		}
		removeUnusedLayers();
	}

	public int mapSize() {
		return maps.size();
	}

	public Material mat() {
		if (mat != null) {
			return mat;
		}

		if (futureMaterial != null) {
			mat = (Material) FutureManager.retrieveFuture(futureMaterial);
		} else {
			mat = importMaterial();
		}

		return mat;
	}

	public void mat(Material mat) {
		this.mat = mat;
	}

	public void markLayerForRemoval(String textureMap) {
		if (layersToRemove == null) {
			layersToRemove = new ArrayList<String>();
		}
		layersToRemove.add(textureMap);
	}

	public void removeUnusedLayers() {
		if (layersToRemove == null) {
			return;
		}
		int size = layersToRemove.size();
		for (int i = size - 1; i >= 0; i--) {
			String layer = layersToRemove.get(i);
			maps.remove(layer);
		}
		layersToRemove.clear();
	}
}
