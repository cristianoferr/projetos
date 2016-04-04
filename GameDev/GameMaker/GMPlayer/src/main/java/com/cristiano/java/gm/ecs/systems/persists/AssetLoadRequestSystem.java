package com.cristiano.java.gm.ecs.systems.persists;

import com.cristiano.benchmark.Bench;
import com.cristiano.java.gm.consts.AssetTypes;
import com.cristiano.java.gm.consts.BenchConsts;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.persists.AssetLoadRequestComponent;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.Log;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

public class AssetLoadRequestSystem extends JMEAbstractSystem {

	public AssetLoadRequestSystem() {
		super(GameComps.COMP_ASSET_LOAD_REQUEST);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		AssetLoadRequestComponent comp = (AssetLoadRequestComponent) component;
		
		ReuseManagerComponent reuseComp = (ReuseManagerComponent) ent
				.getComponentWithIdentifier(GameComps.COMP_REUSE_MANAGER);
		
		if (comp.type==AssetTypes.ASSET_MATERIAL){
			Bench.start("AssetLoadRequestSystem - loadMaterial",BenchConsts.CAT_SYSTEM_UPDATE);
			loadMaterial(comp,reuseComp);
			Bench.end("AssetLoadRequestSystem - loadMaterial");
		} else if (comp.type==AssetTypes.ASSET_TEXTURE){
			Bench.start("AssetLoadRequestSystem - loadTexture",BenchConsts.CAT_SYSTEM_UPDATE);
			loadTexture(comp,reuseComp);
			Bench.end("AssetLoadRequestSystem - loadTexture");
		} else {
			Log.error("Unknown Asset type :"+comp.type);
		}

		ent.removeComponent(comp);
		
	}

	private void loadTexture(AssetLoadRequestComponent comp, ReuseManagerComponent reuseComp) {
		Object mat = reuseComp.getObjectWithKey(comp.fileName);
		if (mat == null) {
			String filePath=GMAssets.requestAsset(comp.fileName);
			Log.debug("Loading Texture: "+filePath);
			Texture loadTexture = game.getAssetManager().loadTexture(filePath);
			reuseComp.setObjectForKey(comp.fileName, loadTexture);
		}
		
	}

	private void loadMaterial(AssetLoadRequestComponent comp, ReuseManagerComponent reuseComp) {
		Object mat = reuseComp.getObjectWithKey(comp.fileName);
		if (mat == null) {
			String filePath=GMAssets.requestAsset(comp.fileName);
			Log.debug("Loading Material: "+filePath);
			mat = new Material(game.getAssetManager(), filePath);
			reuseComp.setObjectForKey(comp.fileName, mat);
		}
		
	}

}
