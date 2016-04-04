package com.cristiano.java.gm.builder.textures;

import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.product.IGameElement;

public interface IMakeTexture {

	void generateTexture(IGameElement elLayer, TextureComponent comp, float[][][] array, boolean applyBlending,  MaterialComponent matComp);
	
}
