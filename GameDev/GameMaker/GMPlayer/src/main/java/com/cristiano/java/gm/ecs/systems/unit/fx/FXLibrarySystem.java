package com.cristiano.java.gm.ecs.systems.unit.fx;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.AssetTypes;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.persists.AssetLoadRequestComponent;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.ecs.comps.unit.fx.FXLibraryComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.interfaces.IMakeEffects;
import com.cristiano.utils.Log;

public class FXLibrarySystem extends JMEAbstractSystem {

	public FXLibrarySystem() {
		super(GameComps.COMP_FX_LIB);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		FXLibraryComponent comp = (FXLibraryComponent) component;
		if (component.isFirstTick()) {
			comp.firstTick = false;
			ReuseManagerComponent reuseComp = (ReuseManagerComponent) ent
					.getComponentWithIdentifier(GameComps.COMP_REUSE_MANAGER);
			if (reuseComp == null) {
				Log.error("ReuseManager null!");
			}
			comp._reuseComp = reuseComp;

			loadAssets(comp, ent);
		}
		
		checkTimeToLive(comp,tpf);
	}
	
	

	private void checkTimeToLive(FXLibraryComponent component, float tpf) {
		for (int i=component.effectsInUse.size()-1;i>=0;i--){
			IMakeEffects eff=component.effectsInUse.get(i);
			if (!eff.isAlive(tpf)){
				component.unregisterFX(eff);
			}
		}
		
	}

	private void loadAssets(FXLibraryComponent comp, IGameEntity ent) {
		Log.debug("Loading FX Assets...");
		for (IGameElement fx : comp.effects) {
			String material = fx.getProperty(GameProperties.MATERIAL);
			String texture = fx.getProperty(GameProperties.TEXTURE);
			loadMaterial(material, ent);
			loadTexture(texture, ent);
		}
		Log.debug("FX Assets loaded...");
	}

	private void loadTexture(String texture, IGameEntity ent) {
		if (texture.equals("")) {
			return;
		}
		AssetLoadRequestComponent req = (AssetLoadRequestComponent) entMan
				.addComponent(GameComps.COMP_ASSET_LOAD_REQUEST, ent);
		req.type = AssetTypes.ASSET_TEXTURE;
		req.fileName = texture;
	}

	private void loadMaterial(String material, IGameEntity ent) {
		if (material.equals("")) {
			return;
		}

		AssetLoadRequestComponent req = (AssetLoadRequestComponent) entMan
				.addComponent(GameComps.COMP_ASSET_LOAD_REQUEST, ent);
		req.type = AssetTypes.ASSET_MATERIAL;
		req.fileName = material;

	}

}
