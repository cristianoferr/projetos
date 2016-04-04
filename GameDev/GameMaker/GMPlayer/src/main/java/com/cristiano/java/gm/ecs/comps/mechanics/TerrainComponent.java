package com.cristiano.java.gm.ecs.comps.mechanics;

import java.awt.Color;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainQuad;

public class TerrainComponent extends GameComponent {

	public float defaultHeight = 0;
	public String terrainType;
	public int lengthOnPower;// 2ˆ10=1024
	public int length;// 10
	public float scale;// scale é calculada automaticamente relacionando
						// tamanho do mapa
	private float[] heightMap = null;
	public int[][] roadMap = null;
	public int maxHeight;
	public float minHeight;
	public int softenArea;
	public String futureName = null;
	public String futureConfigure=null;

	public TerrainComponent() {
		super(GameComps.COMP_TERRAIN);

	}
	
	@Override
	public void free(){
		super.free();
		futureName = null;
		terrainType=null;
		futureConfigure=null;
		heightMap = null;
		roadMap = null;
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		terrainType = ge.getProperty(GameProperties.TERRAIN_TYPE);
		length = ge.getPropertyAsInt(GameProperties.LENGTH);
		lengthOnPower = (int) Math.pow(2, length);
		defaultHeight = ge.getPropertyAsInt(GameProperties.DEFAULT_HEIGHT);
		maxHeight = ge.getPropertyAsInt(GameProperties.MAX_HEIGHT);
		minHeight = ge.getPropertyAsInt(GameProperties.MIN_HEIGHT);
		softenArea = ge.getPropertyAsInt(GameProperties.TERRAIN_SOFTEN_AREA);
	}

	@Override
	public IGameComponent clonaComponent() {
		TerrainComponent ret = new TerrainComponent();
		ret.terrainType = terrainType;
		ret.length = length;
		ret.lengthOnPower = lengthOnPower;
		ret.scale = scale;
		ret.setHeightMap(heightMap);
		ret.roadMap = roadMap;
		return ret;
	}

	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		GMAssets.exportHeightMap(GameConsts.ASSET_TERRAIN, getElement().id(), heightMap, true);
		obj.put(GameProperties.SCALE, scale);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());

		futureName="TerrainLoading"+getId();
		FutureManager.requestFuture(futureName, importHeightmap);

		scale = CRJsonUtils.getFloat(obj, GameProperties.SCALE);
	}

	public int getIndiceForTerrainCoord(int y, int x) {
		int val = x * lengthOnPower + y;
		if (val < 0)
			val = 0;
		if (val >= heightMap.length)
			val = heightMap.length - 1;
		return val;
	}

	public float getValueAt(int x, int y) {
		return heightMap[getIndiceForTerrainCoord(x, y)];
	}

	public float getScaledValueAt(int x, int y) {
		return heightMap[getIndiceForTerrainCoord((int) (x / scale), (int) (y / scale))];
	}

	public void setValueAt(float x, float z, float v) {
		heightMap[getIndiceForTerrainCoord((int) (x), (int) (z))] = v;
	}

	public void setScaledValueAt(float x, float z, float v) {
		heightMap[getIndiceForTerrainCoord((int) (x / scale), (int) (z / scale))] = v;
	}

	public void flattenArea(Vector3f[] tri, CRDebugDraw draw) {
		// no need to flatten a flat map...
		if (terrainType.equals(GameConsts.TERRAIN_FLAT)) {
			return;
		}

		Vector3f min = new Vector3f(tri[0]);
		Vector3f max = new Vector3f(tri[0]);
		for (int i = 1; i < tri.length; i++) {
			min.x = Math.min(tri[i].x, min.x);
			max.x = Math.max(tri[i].x, max.x);
			min.y = Math.min(tri[i].y, min.y);
			max.y = Math.max(tri[i].y, max.y);
			min.z = Math.min(tri[i].z, min.z);
			max.z = Math.max(tri[i].z, max.z);
		}
		float difX = (max.x - min.x);
		float difZ = (max.z - min.z);
		if (difX == 0) {
			return;
		}
		if (difZ == 0) {
			return;
		}
		Vector3f vet = new Vector3f(min);

		float inc = 0.1f;
		Log.debug("Flattening Area");

		while (vet.x <= max.x) {
			vet.z = min.z;
			while (vet.z <= max.z) {
				boolean test = CRMathUtils.polygonContainsPointXZ(vet, tri);
				if (test) {
					setScaledValueAt(vet.x, vet.z, 0);
					setRoadAtScaledPosition(vet.x, vet.z);
					if (draw != null) {
						draw.drawPoint(new Vector2f(vet.x / scale, vet.z / scale), Color.pink);
					}
				}
				vet.z += inc;
			}
			vet.x += inc;
		}
	}

	private void setRoadAtScaledPosition(float x, float z) {
		x = x / scale;
		z = z / scale;
		if ((x < 0) || (z < 0)) {
			return;
		}
		if ((x >= lengthOnPower) || (z >= lengthOnPower)) {
			return;
		}
		roadMap[(int) (x)][(int) (z)] = 1;
	}

	public float getValueAt(Vector3f position) {
		return getValueAt((int) (position.x / scale), (int) (position.z / scale));
	}

	public void softenArea(Vector3f topLeftO, Vector3f bottomRightO) {
		Vector3f topLeft = topLeftO.mult(1f / scale);
		Vector3f bottomRight = bottomRightO.mult(1f / scale);
		float difX = (topLeft.x - bottomRight.x);
		float difY = (topLeft.y + bottomRight.y) / 2 * scale;
		float difZ = (topLeft.z - bottomRight.z);
		float tx = (float) Math.abs(difX);
		float tz = (float) Math.abs(difZ);
		float cx = 0;
		float cz = 0;
		difX /= (int) tx;
		difZ /= (int) tz;
		float val = 0;
		while (Math.abs(cx) < Math.abs(tx)) {
			cz = 0;
			int indice = getIndiceForTerrainCoord((int) (topLeft.x + cx), (int) (topLeft.z + cz));
			if ((indice >= 0) && (indice < heightMap.length)) {
				val = heightMap[indice];
			}
			while (Math.abs(cz) < Math.abs(tz)) {
				indice = getIndiceForTerrainCoord((int) (bottomRight.x + cx), (int) (bottomRight.z + cz));
				if ((indice >= 0) && (indice < heightMap.length)) {
					heightMap[indice] = val - ((val + difY) * Math.abs(cz / tz));
				}
				cz += difZ / 10;
			}
			cx += difX / 10;

		}
	}

	@Override
	public void resetComponent() {
	}

	public void applyRoadLayer(float[][][] array) {
		int textLength = array[0].length;
		float rel = lengthOnPower / textLength;
		for (int x = 0; x < textLength; x++) {
			for (int y = 0; y < textLength; y++) {
				array[x][y][GameConsts.ROAD_INDEX] = roadMap[(int) (x * rel)][(int) (y * rel)];
				if (array[x][y][GameConsts.ROAD_INDEX] > 0) {
					array[x][y][GameConsts.GREEN_INDEX] = 0;
					array[x][y][GameConsts.RED_INDEX] = 0;
				}
			}
		}
		// Se lengthOnPower=1024
		// se textLength=128, rel=lengthOnPower/textLength = 8
		//

	}

	public float[] getHeightMap() {
		if (heightMap == null && CRJavaUtils.isRelease()) {
			// heightMap = GMAssets.importHeightMap(GameConsts.ASSET_TERRAIN,
			// getElement().id(), true);
			try {
				checkHeightMapFuture();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return heightMap;
	}

	private void checkHeightMapFuture() throws InterruptedException, ExecutionException {
		Log.info("Checking heightMap Future... ");
		if (futureName != null) {
			while (heightMap == null) {
				if (FutureManager.isDone(futureName)){

					heightMap = (float[]) FutureManager.retrieveFuture(futureName);
					Log.info("HeightMap Future is ready...");
					futureName=null;
				} else {
					Log.info("heightMap Future not ready... sleeping");
					CRJavaUtils.sleep(10);
				}
			}
		}
	}

	public void setHeightMap(float[] heightMap) {
		this.heightMap = heightMap;
	}

	public float[] createEmptyHeightMap() {
		float[] ret = new float[lengthOnPower * lengthOnPower];
		roadMap = new int[lengthOnPower][lengthOnPower];
		for (int x = 0; x < lengthOnPower; x++) {
			for (int y = 0; y < lengthOnPower; y++) {
				roadMap[x][y] = 0;
			}
		}
		return ret;
	}

	// MUlti-thread
	public Callable<Object> createTerrain = new Callable<Object>() {
		public Object call() throws Exception {
			int patchSize = 65;// A good value for terrain tiles is 64x64 -- so
								// we
			// supply 64+1=65.
			int totalSize = lengthOnPower + 1;
			TerrainQuad terrain = new TerrainQuad(getElement().getName(), patchSize, totalSize, getHeightMap());

			return terrain;
		}
	};
	public Callable<Object> importHeightmap = new Callable<Object>() {
		public Object call() throws Exception {
			Object heightMap = GMAssets.importHeightMap(GameConsts.ASSET_TERRAIN, getElement().id(), true);
			return heightMap;
		}
	};
	
}
