package com.cristiano.java.gm.ecs.systems.map;

import java.util.concurrent.ExecutionException;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.PhysicsSpaceComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.ecs.systems.JMEAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.gm.utils.FutureManager;
import com.cristiano.utils.Log;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;

/*
 * Unit Test:TestTerrainSystem
 * */
public class TerrainLoaderSystem extends JMEAbstractSystem {

	public TerrainLoaderSystem() {
		super(GameComps.COMP_TERRAIN);
	}

	@Override
	// ent=mapComponent
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {

		TerrainComponent comp = (TerrainComponent) component;
		MapComponent map = (MapComponent) ent;
		if (component.isFirstTick()) {

			if (map.isOnStage(MapComponent.TERRAIN_GENERATING)) {
				comp.getHeightMap();
				map.nextStage();
			}

			// finishing: pega o heightmap e adiciona o node visual
			if (map.isOnStage(MapComponent.TERRAIN_FINISHING)) {
				if (existsComponents(GameComps.COMP_FLATTEN_TERRAIN)) {
					return;
				}
				map.nextStage();
				createTerrain(map, comp);
				comp.firstTick = false;
			}
		} else {
			try {
				checkFuture(map, comp);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		}

	}

	private void checkFuture(MapComponent map, TerrainComponent comp)
			throws InterruptedException, ExecutionException {
		if (comp.futureConfigure != null) {
			if (FutureManager.isDone(comp.futureConfigure)) {
				Log.info("Terrain: Future is done.");
				TerrainQuad terrain = (TerrainQuad) FutureManager.retrieveFuture(comp.futureConfigure);
				configureTerrain(map, comp, terrain);

				comp.archive();
				comp.futureConfigure = null;
			} 
		}

	}

	protected void configureTerrain(MapComponent map, TerrainComponent comp,
			TerrainQuad terrain) {
		CamComponent camC = getCamComponent();
		RenderComponent renderC = ECS.getRenderComponent(map);
		PhysicsSpaceComponent physSpC = getPhysicsSpace();

		TerrainLodControl control = new TerrainLodControl(terrain, camC.cam);

		// terrain.setMaterial(snippets.createMaterialLight());

		terrain.addControl(control);
		terrain.setShadowMode(ShadowMode.Receive);
		RigidBodyControl physControl = new RigidBodyControl(0);
		terrain.setLocalTranslation(map.getLength() / 2, 0, map.getLength() / 2);
		terrain.setLocalScale(comp.scale, 1, comp.scale);

		terrain.setLocked(false);
		physControl.setSpatial(terrain);
		terrain.addControl(physControl);

		MaterialComponent materialComponent = getMaterialComponent(comp, map);
		terrain.setMaterial(materialComponent.mat());

		renderC.node.attachChild(terrain);
		physSpC.addSpatial(terrain);
	}

	protected void createTerrain(MapComponent map, TerrainComponent comp) {
		comp.futureConfigure="createTerrain-"+comp.getId();
		FutureManager.requestFuture(comp.futureConfigure, comp.createTerrain);

	}

	protected MaterialComponent getMaterialComponent(TerrainComponent comp,
			IGameEntity ent) {
		MaterialComponent materialComponent = (MaterialComponent) ent
				.getComponentWithIdentifier(GameComps.COMP_MATERIAL);
		return materialComponent;
	}

	protected void createTextureComponent(TerrainComponent comp, IGameEntity ent) {
		TextureComponent text = (TextureComponent) ent
				.getComponentWithIdentifier(GameComps.COMP_TEXTURE);
		if (text == null) {
			Log.debug("Creating texture component for terrain... "
					+ comp.getId());
			text = (TextureComponent) entMan.addComponent(
					GameComps.COMP_TEXTURE, ent);
		}

	}
}
