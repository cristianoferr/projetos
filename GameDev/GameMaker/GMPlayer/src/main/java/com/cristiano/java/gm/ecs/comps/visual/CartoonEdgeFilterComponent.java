package com.cristiano.java.gm.ecs.comps.visual;

import org.json.simple.JSONObject;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;

public class CartoonEdgeFilterComponent extends AbstractFilterComponent {

	private static final String EDGE_WIDTH = "edgeWidth";
	private static final String EDGE_INTENSITY = "edgeIntensity";
	private static final String NORMAL_THRESHOLD = "normalThreshold";
	public float edgeWidth;
	public float edgeIntensity;
	public float normalThreshold;

	public CartoonEdgeFilterComponent() {
		super(GameComps.COMP_CARTOON_EDGE_FILTER);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		edgeWidth = ge.getPropertyAsFloat(EDGE_WIDTH);
		edgeIntensity = ge.getPropertyAsFloat(EDGE_INTENSITY);
		normalThreshold = ge.getPropertyAsFloat(NORMAL_THRESHOLD);
	}

	@Override
	public IGameComponent clonaComponent() {
		CartoonEdgeFilterComponent ret = new CartoonEdgeFilterComponent();
		ret.normalThreshold = normalThreshold;
		ret.edgeIntensity = edgeIntensity;
		ret.edgeWidth = edgeWidth;
		
		return ret;
	}
	
/*
	public void applyFilterChildren(Spatial spatial) {
		super.applyFilterChildren(spatial);
		if (spatial instanceof Node) {
			Node n = (Node) spatial;
			for (Spatial child : n.getChildren())
				applyFilterChildren(child);
		} else if (spatial instanceof Geometry) {
			Geometry g = (Geometry) spatial;
			Material m = g.getMaterial();
			if (m.getMaterialDef().getName().equals("Phong Lighting")) {
				Texture t = assetManager
						.loadTexture("Textures/ColorRamp/toon.png");
				// t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
				// t.setMagFilter(Texture.MagFilter.Nearest);
				m.setTexture("ColorRamp", t);
				m.setBoolean("UseMaterialColors", true);
				m.setColor("Specular", ColorRGBA.Black);
				m.setColor("Diffuse", ColorRGBA.White);
				m.setBoolean("VertexLighting", true);
			}
		}
	}*/
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(EDGE_WIDTH,this.edgeWidth);
		obj.put(EDGE_INTENSITY,this.edgeIntensity);
		obj.put(NORMAL_THRESHOLD,this.edgeIntensity);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		edgeWidth = (float)obj.get(EDGE_WIDTH);
		edgeIntensity = (float)obj.get(EDGE_INTENSITY);
		normalThreshold = (float)obj.get(NORMAL_THRESHOLD);
		
	}
	
	@Override
	public void resetComponent() {
	}
}
