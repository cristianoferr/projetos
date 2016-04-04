package com.cristiano.java.gm.ecs.systems;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.utils.Log;
import com.jme3.scene.Geometry;

public class SpatialSystem extends JMEAbstractSystem {

	public SpatialSystem() {
		super(GameComps.COMP_SPATIAL);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		
		if (component.isFirstTick()){
			SpatialComponent comp=(SpatialComponent) component;
			comp.firstTick=false;
			if (comp.addToRender){
				RenderComponent render = ECS.getRenderComponent(ent);
				if (render==null){
					Log.fatal("Entity has no Render Component!");
				}
				Geometry geometry = comp.spatial();
				if (comp.position!=null){
						geometry.setLocalTranslation(comp.position);
				}
				
				applyMaterial(comp,geometry);
				if (comp.addToRender){
					render.node.attachChild(geometry);
				} else {
					Log.debug("Not Adding!");
				}
				updateBounds(ent, render);
			}
			
		}
	}

	
	
	private void applyMaterial(SpatialComponent comp, Geometry geometry) {
		MaterialComponent mat=(MaterialComponent) comp.getComponentWithIdentifier(GameComps.COMP_MATERIAL);
		if (mat!=null){
			geometry.setMaterial(mat.mat());
		}
		
	}

}
