package com.cristiano.java.gm.ecs.comps.materials;

import java.util.List;

import com.cristiano.java.product.IGameElement;

public class MaterialData {
	private String textureMap;


	private List<IGameElement> layers;
	float[][][] arrayMap = null;

	public MaterialData(String textureMap) {
		this.textureMap = textureMap;
	}
	public void setLayers(List<IGameElement> layers) {
		this.layers = layers;
	}

	public void setArray(float[][][] array) {
		this.arrayMap = array;

	}

	public float[][][] getArray() {
		return arrayMap;
	}

	public List<IGameElement> getLayers() {
		return layers;
	}
}