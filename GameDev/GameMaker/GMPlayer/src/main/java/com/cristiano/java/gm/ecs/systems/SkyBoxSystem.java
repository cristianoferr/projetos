package com.cristiano.java.gm.ecs.systems;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.comps.visual.SkyBoxComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.jme3.assets.GMAssets;
import com.jme3.scene.Spatial;

public class SkyBoxSystem extends JMEAbstractSystem {

	public SkyBoxSystem() {
		super(GameComps.COMP_SKYBOX);
	}

	@Override
	// ent=mapcomponent
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		SkyBoxComponent comp = (SkyBoxComponent) component;
		if (component.isFirstTick()) {
			comp.firstTick = false;

			RenderComponent compRender = (RenderComponent) game.getGameEntity()
					.getComponentWithIdentifier(GameComps.COMP_RENDER);
			if (compRender != null) {
				loadSkyBox(comp, compRender);
			}
		}
		if (comp.futureName != null) {
			loadFutureSkybox(ent, comp);
		}else {
			comp.archive();
		}
	}

	private void loadFutureSkybox(IGameEntity ent, SkyBoxComponent comp){
		if (FutureManager.isDone(comp.futureName)) {
			ECS.getRenderComponent(ent).node
					.attachChild((Spatial) FutureManager
							.retrieveFuture(comp.futureName));
			comp.futureName = null;
			comp.archive();
		}
	}

	private void loadSkyBox(SkyBoxComponent comp, RenderComponent compRender) {
		String texture = comp.elSkyBox.getProperty(GameProperties.TEXTURE);
		if (!texture.equals("")) {
			texture = texture.replace(" ", "");
			snippets.createSkyBox(texture);
		} else {
			comp._down = getFilePath(comp, GameProperties.IMAGE_DOWN);
			comp._east = getFilePath(comp, GameProperties.IMAGE_EAST);
			comp._north = getFilePath(comp, GameProperties.IMAGE_NORTH);
			comp._south = getFilePath(comp, GameProperties.IMAGE_SOUTH);
			comp._up = getFilePath(comp, GameProperties.IMAGE_UP);
			comp._west = getFilePath(comp, GameProperties.IMAGE_WEST);
			comp._snippets = snippets;
			String event = "SkyBoxSystem - createSkyBox";
			// Bench.start(event,BenchConsts.CAT_SYSTEM_UPDATE);
			comp.futureName = "SkyBox-" + comp.getId();
			FutureManager.requestFuture(comp.futureName, comp.createSkybox);
			// Bench.end(event);

			/*
			 * snippets.createSkyBox(name+"_ft.jpg", //west name+"_bk.jpg",
			 * //east name+"_lf.jpg",//north name+"_rt.jpg", //sourth
			 * name+"_up.jpg", name+"_dn.jpg");
			 */
		}

	}

	private String getFilePath(SkyBoxComponent comp, String prop) {
		String file = comp.elSkyBox.getProperty(prop).replace(" ", "");
		file = GMAssets.requestAsset(file);
		return file;
	}

}
