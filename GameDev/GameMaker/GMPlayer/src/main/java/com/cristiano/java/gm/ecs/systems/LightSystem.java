package com.cristiano.java.gm.ecs.systems;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.effects.LightComponent;
import com.cristiano.java.gm.ecs.comps.visual.RenderComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.Vector3f;
import com.jme3.shadow.DirectionalLightShadowRenderer;

public class LightSystem extends JMEAbstractSystem {

	public LightSystem() {
		super(GameComps.COMP_LIGHT);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component, float tpf) {
		if (component.isFirstTick()) {
			LightComponent comp = (LightComponent) component;
			comp.firstTick = false;
			comp.getElement().getPropertyAsGE("colorObj");

			RenderComponent renderC = (RenderComponent) ent.getComponentWithIdentifier(GameComps.COMP_RENDER);
			addLight(renderC, comp);
			// factory.instantiateClass(elLight)createComponentFrom(compEl);
			comp.archive();
		}
	}

	private void addLight(RenderComponent renderC, LightComponent comp) {
		Light light = null;
		comp.tipo = comp.elLightControl.getProperty(Extras.PROPERTY_IDENTIFIER);
		IGameElement lightElement = comp.getElement();

		if (comp.tipo.equals(GameConsts.LIGHT_DIRECTIONAL)) {
			light = new DirectionalLight();
			Vector3f dir = new Vector3f(lightElement.getPropertyAsFloat(GameProperties.DIRECTION_X),
					lightElement.getPropertyAsFloat(GameProperties.DIRECTION_Y), lightElement.getPropertyAsFloat(GameProperties.DIRECTION_Z));
			((DirectionalLight) light).setDirection(dir);

			if (comp.castShadow) {
				addShadowRenderer(comp, light);
			}
		}
		if (comp.tipo.equals(GameConsts.LIGHT_AMBIENT)) {
			light = new AmbientLight();
		}

		if (light == null) {
			Log.error("Light não foi instanciada!!");
			return;
		}
		float intensity = comp.getElement().getPropertyAsFloat(GameProperties.INTENSITY);
		light.setColor(comp.color.mult(intensity));
		renderC.node.addLight(light);

	}

	private void addShadowRenderer(LightComponent comp, Light light) {
		final int SHADOWMAP_SIZE = 1024;
		DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(game.getAssetManager(), SHADOWMAP_SIZE, 3);
		dlsr.setLight((DirectionalLight) light);
		// game.getViewPort().addProcessor(dlsr);
		game.addShadowRender(dlsr);
		if (!dlsr.isInitialized()) {
			dlsr.initialize(game.getRenderManager(), game.getViewPort());
		}
		// dlsr.setFlushQueues(false);
		// comp.shadowRenderer = dlsr;
	}
}
