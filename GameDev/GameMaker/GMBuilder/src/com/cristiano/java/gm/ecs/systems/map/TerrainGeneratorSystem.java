package com.cristiano.java.gm.ecs.systems.map;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.builder.utils.ExportUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.proc.noise.IMakeNoise;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.terrain.geomipmap.TerrainQuad;

/*
 * Unit Test:TestTerrainSystem
 * */
public class TerrainGeneratorSystem extends TerrainLoaderSystem {

	@Override
	// ent=mapComponent
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		if (component.isFirstTick()) {
			MapComponent map = (MapComponent) ent;
			TerrainComponent comp = (TerrainComponent) component;

			// generating: gera o heightmap de acordo com as regras e armazena
			// no componente...
			if (map.isOnStage(MapComponent.TERRAIN_GENERATING)) {
				comp.scale = (float) map.getLength() / comp.lengthOnPower;
				if (comp.scale == 0) {
					Log.error("TerrainSystem: Terrain.scale=0!!");
				}
				comp.setHeightMap(generateHeightMap(map, comp));
				map.nextStage();
			}

			// finishing: pega o heightmap e adiciona o node visual
			if (map.isOnStage(MapComponent.TERRAIN_FINISHING)) {
				if (existsComponents(GameComps.COMP_FLATTEN_TERRAIN)) {
					return;
				}
				map.nextStage();
				createTerrain(map, comp, ent);
				comp.firstTick = false;
			}

		}
	}

	protected MaterialComponent getMaterialComponent(TerrainComponent comp,
			IGameEntity ent) {
		MaterialComponent materialComponent = BuilderUtils.createMaterial(null,
				comp.getElement(),entMan,game);
		ent.attachComponent(materialComponent);
		return materialComponent;
	}

	protected void createTerrain(MapComponent map, TerrainComponent comp,
			IGameEntity ent) {
		int patchSize = 65;// A good value for terrain tiles is 64x64 -- so we
		// supply 64+1=65.
		int totalSize = comp.lengthOnPower + 1;
    	TerrainQuad terrain = new TerrainQuad(getElement().getName(),
				patchSize, totalSize, comp.getHeightMap());
    	
		configureTerrain(map, comp, terrain);
		createTextureComponent(comp, map);

		if (CRJavaUtils.IS_DEBUG) {
			ExportUtils.exportTerrain(comp);
		}
	}

	private float[] generateHeightMap(MapComponent map, TerrainComponent comp) {
		Log.info("Generating Terrain with type:" + comp.terrainType);
		if (comp.terrainType.equals(GameConsts.TERRAIN_NOISE)) {
			return generateNoiseMap(map, comp);
		}
		if (comp.terrainType.equals(GameConsts.TERRAIN_FLAT)) {
			return generateFlatMap(comp, comp.defaultHeight);
		}
		if (comp.terrainType.equals(GameConsts.TERRAIN_PLACEHOLDER)) {
			return generatePlaceHolderMap(map, comp);
		}
		Log.error("Unkwnown TerrainType:" + comp.terrainType);
		return null;
	}

	private float[] generatePlaceHolderMap(MapComponent map,
			TerrainComponent comp) {
		float[] ret = comp.createEmptyHeightMap();
		int p = 0;
		for (int x = 0; x < comp.lengthOnPower; x++) {
			for (int y = 0; y < comp.lengthOnPower; y++) {
				if ((x < comp.lengthOnPower / 2)
						&& (y < comp.lengthOnPower / 2)) {
					ret[p] = comp.minHeight + 50;
				} else {
					ret[p] = comp.maxHeight - 50;
				}
				p++;
			}
		}

		return ret;
	}

	private float[] generateFlatMap(TerrainComponent comp, float h) {
		float[] ret = comp.createEmptyHeightMap();
		int p = 0;
		for (int x = 0; x < comp.lengthOnPower; x++) {
			for (int y = 0; y < comp.lengthOnPower; y++) {
				ret[p] = h;
				p++;
			}
		}
		return ret;
	}

	private float[] generateNoiseMap(MapComponent map, TerrainComponent comp) {
		String classe = comp.getElement().getProperty(
				GameProperties.NOISE_CLASS);
		IMakeNoise noise = (IMakeNoise) CRJavaUtils.instanciaClasse(classe);
		noise.init(map.seed, comp.minHeight, comp.maxHeight);
		float[] ret = comp.createEmptyHeightMap();
		int p = 0;
		for (int x = 0; x < comp.lengthOnPower; x++) {
			for (int y = 0; y < comp.lengthOnPower; y++) {
				ret[p] = (float) noise.getNoise(x, y);
				p++;
			}
		}

		return ret;
	}

}
