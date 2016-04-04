package com.cristiano.java.gm.ecs.systems.visual;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.visual.AbstractFilterComponent;
import com.cristiano.java.gm.ecs.comps.visual.BloomFilterComponent;
import com.cristiano.java.gm.ecs.comps.visual.DepthOfFieldComponent;
import com.cristiano.java.gm.ecs.comps.visual.FogFilterComponent;
import com.cristiano.java.gm.ecs.comps.visual.LightScatteringComponent;
import com.cristiano.java.gm.ecs.systems.BuilderAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.FogFilter;

public class ApplyFiltersSystem extends BuilderAbstractSystem {

	public ApplyFiltersSystem() {
		super(GameComps.TAG_COMPS_VISUAL_ENHANCE);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		if (CRJavaUtils.isAndroid()){
			Log.info("Removing a non-android component");
			ent.removeComponent(component);
			return;
		}
		if (component.isFirstTick()) {
			AbstractFilterComponent comp=(AbstractFilterComponent) component;
			addFilter(ent, comp);
			comp.firstTick=false;
			comp.archive();
		}
	}

	/*
	 * private void updateFilter(IGameComponent component) { if
	 * (component.getIdentifier().equals(GameComps.COMP_DEPTH_OF_FIELD)) {
	 * updateDoF((DepthOfFieldComponent) component); } }
	 * 
	 * private void updateDoF(DepthOfFieldComponent component) {
	 * DepthOfFieldFilter dofFilter = (DepthOfFieldFilter) component.filter;
	 * dofFilter.setBlurScale(0.9f); dofFilter.setFocusDistance(0);
	 * dofFilter.setFocusRange(10); }
	 */

	private void addFilter(IGameEntity ent, IGameComponent component) {
		if (component.getIdentifier().equals(GameComps.COMP_DEPTH_OF_FIELD)) {
			addDepthOfField(ent, (DepthOfFieldComponent) component);
		} else if (component.getIdentifier()
				.equals(GameComps.COMP_BLOOM_FILTER)) {
			addBloom(ent, (BloomFilterComponent) component);
		} else if (component.getIdentifier().equals(
				GameComps.COMP_LIGHT_SCATTERING)) {
			addLightScattering(ent, (LightScatteringComponent) component);
		}  else if (component.getIdentifier().equals(
				GameComps.COMP_FOG_FILTER)) {
			addFog(ent, (FogFilterComponent) component);
		}else {
			Log.error("Unknown filter:" + component.getIdentifier());
		}
	}

	private void addFog(IGameEntity ent, FogFilterComponent component) {
		FilterPostProcessor fpp = game.getFieldPostProcessor();
		component.firstTick = false;
		FogFilter fog = new FogFilter();
        fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
        fog.setFogDistance(component.distance);
        fog.setFogDensity(component.density);
        fpp.addFilter(fog);
        component.filter = fog;
        game.getViewPort().addProcessor(fpp);
	}

	private void addLightScattering(IGameEntity ent,
			LightScatteringComponent component) {
		component.firstTick = false;
		//TODO: finish this... need the light position
		/*
		FilterPostProcessor fpp = game.getFieldPostProcessor();
		LightScatteringFilter filter = new LightScatteringFilter();
		filter.setLightPosition(lightPos);
		filter.setLightDensity(component.lightDensity);

		fpp.addFilter(filter);
		component.filter = filter;
		game.getViewPort().addProcessor(fpp);*/

	}

	private void addBloom(IGameEntity ent, BloomFilterComponent component) {
		component.firstTick = false;
		FilterPostProcessor fpp = game.getFieldPostProcessor();
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
		bloom.setBloomIntensity(component.intensity);
		bloom.setExposurePower(component.exposurePower);
		bloom.setBlurScale(component.blurScale);
		fpp.addFilter(bloom);
		component.filter = bloom;
		if (game.getViewPort()==null){
			Log.errorIfRunning("Game View Port is null!");
			return;
		}
		game.getViewPort().addProcessor(fpp);
	}

	private void addDepthOfField(IGameEntity ent,
			DepthOfFieldComponent component) {
		component.firstTick = false;
		FilterPostProcessor fpp = game.getFieldPostProcessor();

		DepthOfFieldFilter dofFilter = new DepthOfFieldFilter();
		component.filter = dofFilter;
		dofFilter.setFocusDistance(component.focusDistance);
		dofFilter.setFocusRange(component.focusRange);
		dofFilter.setBlurScale(component.blurScale);
		fpp.addFilter(dofFilter);

	}

}
