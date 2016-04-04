package com.cristiano.java.gm.ecs.comps.macro.enviro;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;

public class EnviroEntity {
	private String meshTag;
	private IGameElement meshElement;
	private String carrierTag;
	private float height=0;
	private float wallHeight=0;
	private float wallWidth=0;
	private IGameElement ge;

	public void loadFromElement(IGameElement ge) {
		this.ge=ge;
		meshTag = StringHelper.removeChaves(ge.getProperty(GameProperties.MESH_TAG));
		if (this.meshTag.equals("")) {
			this.meshTag = null;
		}
		if ("null".equals(meshTag)) {
			this.meshTag = null;
		}
		carrierTag = ge.getProperty(GameProperties.CARRIER_TAG);
		wallHeight = ge.getPropertyAsFloat(GameProperties.WALL_HEIGHT);
		wallWidth = ge.getPropertyAsFloat(GameProperties.WALL_WIDTH);
	}

	public String getMeshTag() {
		return meshTag;
	}

	public IGameElement getMeshElement() {
		return meshElement;
	}

	public void setMeshElement(IGameElement mesh) {
		this.meshElement = mesh;
	}

	public String getCarrierTag() {

		return carrierTag;
	}

	public float getHeight() {
		return height;
	}

	public float getWallHeight() {
		return wallHeight;
	}

	public float getWallWidth() {
		return wallHeight;
	}
}