package com.cristiano.java.gm.utils;

import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.utils.Log;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

public abstract class MaterialHelper {

	public static boolean applyToUnshadedMaterial(MaterialComponent matComp, String mapType, ColorRGBA cor, Texture texture) {
		if (mapType.equals(GameConsts.TEXTURE_MAP_DIFFUSE)) {
			applyColor(matComp, cor, texture);
			return true;
		} else {
			return false;
		}
	}

	public static boolean applyToLightMaterial(MaterialComponent matComp, String mapType, ColorRGBA cor, Texture texture, TextureComponent comp) {
		matComp.mat().setBoolean("UseMaterialColors", true);
		if (mapType.equals(GameConsts.TEXTURE_MAP_DIFFUSE)) {
			MaterialHelper.applyDiffuse(matComp, cor, texture);
		} else if (mapType.equals(GameConsts.TEXTURE_MAP_GLOW)) {
			MaterialHelper.applyGlow(matComp, cor, texture);
		} else if (mapType.equals(GameConsts.TEXTURE_MAP_NORMAL)) {
			MaterialHelper.applyNormal(matComp, cor, texture, comp);
		} else if (mapType.equals(GameConsts.TEXTURE_MAP_SHINE)) {
			MaterialHelper.applyShininess(matComp, cor, texture);
		} else {
			Log.error("Unknown material:" + mapType);
			return false;
		}
		return true;
	}

	public static void applyTransparency(MaterialComponent matComp, SpatialComponent applyToSpatial) {
		if (matComp.isTransparent) {
			Material mat = matComp.mat();
			mat.getAdditionalRenderState().setBlendMode(BlendMode.Color);
			applyToSpatial.spatial().setQueueBucket(Bucket.Transparent);
		}
	}

	public static void applyShininess(MaterialComponent matComp, ColorRGBA color, Texture texture) {
		float intensity = matComp.shineIntensity;
		Material mat = matComp.mat();
		mat.setFloat("Shininess", intensity);
		mat.setTexture("SpecularMap", texture);
		mat.setColor("Specular", color);
	}

	public static void applyNormal(MaterialComponent matComp, ColorRGBA color, Texture texture, TextureComponent comp) {
		if (comp.applyToSpatial == null) {
			Log.warn("Apply to spatial is null...");
			return;
		}
		TangentBinormalGenerator.generate(comp.applyToSpatial.getMesh());
		matComp.mat().setTexture("NormalMap", texture);
	}

	public static void applyGlow(MaterialComponent matComp, ColorRGBA color, Texture texture) {
		Material mat = matComp.mat();
		mat.setColor("GlowColor", color);
		mat.setTexture("GlowMap", texture);
	}

	public static void applyColor(MaterialComponent matComp, ColorRGBA color, Texture texture) {
		Material mat = matComp.mat();
		mat.setTexture("ColorMap", texture);// texture principal
		mat.setColor("Color", color);// cor principal
	}

	public static void applyDiffuse(MaterialComponent matComp, ColorRGBA color, Texture texture) {
		Material mat = matComp.mat();
		mat.setTexture("DiffuseMap", texture);// texture principal
		mat.setColor("Diffuse", color);// cor principal
	}
}
