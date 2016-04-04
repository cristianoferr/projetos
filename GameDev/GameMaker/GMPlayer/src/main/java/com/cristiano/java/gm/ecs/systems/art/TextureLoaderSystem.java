package com.cristiano.java.gm.ecs.systems.art;

import java.util.List;

import com.cristiano.benchmark.Bench;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.materials.IIterateMaterial;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialData;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.java.gm.utils.GMUtils;
import com.cristiano.java.gm.utils.MaterialHelper;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class TextureLoaderSystem extends JMEAbstractSystem {

	public TextureLoaderSystem() {
		super(GameComps.COMP_TEXTURE);
	}

	@Override
	// ent geralmente vai ser quem está sendo texturizado...
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		TextureComponent comp = (TextureComponent) component;
		// comp.firstTick = false;
		if (component.isFirstTick()) {
			Bench.start("TextureLoaderSystem - inicio", BenchConsts.CAT_SYSTEM_UPDATE);
			SpatialComponent spatComp = (SpatialComponent) ent.getComponentWithIdentifier(GameComps.COMP_SPATIAL);
			TerrainComponent terrainComp = (TerrainComponent) ent.getComponentWithIdentifier(GameComps.COMP_TERRAIN);

			MaterialComponent matComp = (MaterialComponent) ent.getComponentWithIdentifier(GameComps.COMP_MATERIAL);
			// a material is necessary for setting the texture...
			if (matComp == null) {
				Log.warn("No material defined... returning...");
				return;
			}

			prepareComponent(ent, comp, spatComp, terrainComp, matComp);

			futureRequestMap(ent, comp, matComp);
			comp.firstTick = false;
			// generateMaps(ent, comp, matComp);
			Bench.end("TextureLoaderSystem - generateMaps");
		}

		checkFutureGenerateMap(comp);

		checkApplyToSpatialFuture(comp);
	}

	protected void futureRequestMap(IGameEntity ent, TextureComponent comp, MaterialComponent matComp) {
		Bench.start("TextureLoaderSystem - generateMaps", BenchConsts.CAT_SYSTEM_UPDATE);
		comp._matComp = matComp;
		comp._ent = ent;
		comp._system = this;
		comp.futureGenerateMap = "generateMap" + comp.getId();
		FutureManager.requestFuture(comp.futureGenerateMap, comp.generateMapsMethod);
	}

	protected void checkFutureGenerateMap(TextureComponent comp) {
		if (FutureManager.isDone(comp.futureGenerateMap)) {
			FutureManager.retrieveFuture(comp.futureGenerateMap);
			comp.futureGenerateMap = null;

			Bench.start("TextureLoaderSystem - applyToSpatial", BenchConsts.CAT_SYSTEM_UPDATE);
			if (comp.applyToSpatial != null) {
				applyToSpatialFutureInit(comp, comp._matComp);
			} else {
				comp.archive();
			}
			Bench.end("TextureLoaderSystem - applyToSpatial");
		}
	}

	private void checkApplyToSpatialFuture(TextureComponent comp) {
		if (FutureManager.isDone(comp.futureApplyToSpatial)) {
			FutureManager.retrieveFuture(comp.futureApplyToSpatial);
			comp.futureApplyToSpatial = null;
			comp.archive();
		}
	}

	protected void applyToSpatialFutureInit(TextureComponent comp, MaterialComponent matComp) {
		comp._matComp = matComp;
		comp.futureApplyToSpatial="applyToSpatial"+comp.getId();
		FutureManager.requestFuture(comp.futureApplyToSpatial, comp.applyToSpatialMethod);
	}

	private void prepareComponent(IGameEntity ent, TextureComponent comp, SpatialComponent spatComp, TerrainComponent terrainComp, MaterialComponent matComp) {
		comp.length = matComp.length;
		comp.applyToTerrain = terrainComp;
		comp.applyToSpatial = spatComp;
		if (ent instanceof SpatialComponent) {
			comp.applyToSpatial = (SpatialComponent) ent;

		}
		comp.firstTick = false;
	}

	public void generateMaps(final IGameEntity ent, final TextureComponent comp, final MaterialComponent matComp) {
		matComp.iterateMaterials(new IIterateMaterial() {
			@Override
			public void iterate(String mapType, MaterialData matData) {
				generateMap(mapType, matData.getLayers(), ent, matComp, comp);
			}
		});

		comp.addInfo("Maps generated... " + comp.size());
		if (comp.isEmpty()) {
			Log.fatal("No Texture is being used!!");
		}

	}

	protected void generateMap(String mapType, List<IGameElement> list, IGameEntity ent, MaterialComponent matComp, TextureComponent comp) {
		if (list.isEmpty()) {
			return;
		}
		ColorRGBA cor = GMUtils.createColor(matComp.color);
		comp.setTexture(mapType, applyToMapType(ent, matComp, mapType, cor, list, comp));

	}

	protected Texture applyToMapType(IGameEntity ent, MaterialComponent matComp, String mapType, ColorRGBA cor, List<IGameElement> list, TextureComponent comp) {
		Texture texture = null;
		// flip the texture...
		// matComp.mat().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		texture = generateTexture(comp, matComp, true, mapType, null);

		if ((matComp.materialType == MaterialComponent.MAT_LIGHT) || (matComp.materialType == MaterialComponent.MAT_UNSHADED)) {
			texture.setWrap(WrapMode.BorderClamp);
			applyToMaterial(matComp, mapType, cor, texture, comp, matComp.materialType);
		} else if (matComp.materialType == MaterialComponent.MAT_TERRAIN) {
			applyToTerrainMaterial(ent, matComp, mapType, cor, texture);
		} else {
			Log.fatal("Unknown Material:" + matComp.materialType);
		}
		return texture;
	}

	protected Texture generateTexture(TextureComponent comp, MaterialComponent matComp, boolean invertY, String mapType, float[][][] array) {
		Texture texture = comp.getTexture(mapType);
		if (texture == null) {
			Log.fatal("Undefined texture");
		}
		return texture;
	}

	protected void applyToTerrainMaterial(IGameEntity ent, MaterialComponent matComp, String mapType, ColorRGBA cor, Texture alphaTexture) {
		alphaTexture.setWrap(WrapMode.Repeat);

		MapComponent map = null;
		if (ent instanceof MapComponent) {
			map = (MapComponent) ent;
		} else {
			Log.error("Entity doesnt extends MapComponent");
		}
		AssetManager assetManager = game.getAssetManager();

		// TODO: tornar os valores de scale dinamicos
		// roadTexture=$this.roadTexture,lowHeightTexture=$this.lowHeightTexture.value,highHeightTexture=$this.highHeightTexture
		String lowHeightTexture = map.getLowHeightTexture();
		String highHeightTexture = map.getHighHeightTexture();
		String roadTexture = map.getRoadTexture();

		matComp.mat().setTexture("Alpha", alphaTexture);

		Texture tex1 = assetManager.loadTexture(lowHeightTexture);
		tex1.setWrap(WrapMode.Repeat);
		matComp.mat().setTexture("Tex1", tex1);
		matComp.mat().setFloat("Tex1Scale", 64f);

		// 1.3) Add DIRT texture into the green layer (Tex2)
		Texture tex2 = assetManager.loadTexture(highHeightTexture);
		tex2.setWrap(WrapMode.Repeat);
		matComp.mat().setTexture("Tex2", tex2);
		matComp.mat().setFloat("Tex2Scale", 64f);

		// 1.4) Add ROAD texture into the blue layer (Tex3)
		Texture road = assetManager.loadTexture(roadTexture);
		road.setWrap(WrapMode.Repeat);
		matComp.mat().setTexture("Tex3", road);
		matComp.mat().setFloat("Tex3Scale", 32f);

	}

	protected void applyToMaterial(MaterialComponent matComp, String mapType, ColorRGBA cor, Texture texture, TextureComponent comp, int materialType) {
		if (matComp.mat() == null) {
			Log.warn("Material is null, cant apply textureMap");
			return;
		}
		Log.debug("Applying material:" + mapType);
		boolean layerWasUsed = false;
		if (materialType == MaterialComponent.MAT_UNSHADED) {
			layerWasUsed = MaterialHelper.applyToUnshadedMaterial(matComp, mapType, cor, texture);
		} else if (materialType == MaterialComponent.MAT_LIGHT) {
			layerWasUsed = MaterialHelper.applyToLightMaterial(matComp, mapType, cor, texture, comp);
		} else {
			Log.fatal("Unknown Material type: " + materialType);
		}
		if (!layerWasUsed) {
			matComp.markLayerForRemoval(mapType);
		}

	}

}
