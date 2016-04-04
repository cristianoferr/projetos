package com.cristiano.java.gm.ecs.comps.macro.enviro;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.product.IGameElement;
import com.jme3.math.Vector3f;

public class EnviroFitting {
	Vector3f max = new Vector3f(-1, -1, -1);
	Vector3f min = new Vector3f(-1, -1, -1);

	public EnviroFitting(float maxX, float minX, float maxY, float minY,
			float maxZ, float minZ) {
		max.x = maxX;
		max.y = maxY;
		max.z = maxY;
		min.x = minX;
		min.y = minY;
		min.z = minZ;
	}

	public EnviroFitting() {
	}

	public void loadFromElement(IGameElement ge) {
		max.x = ge.getPropertyAsFloat(GameProperties.MAX_X);
		max.y = ge.getPropertyAsFloat(GameProperties.MAX_Y);
		max.z = ge.getPropertyAsFloat(GameProperties.MAX_Z);
		min.x = ge.getPropertyAsFloat(GameProperties.MIN_X);
		min.y = ge.getPropertyAsFloat(GameProperties.MIN_Y);
		min.z = ge.getPropertyAsFloat(GameProperties.MIN_Z);
	}

	public boolean fitsInto(Vector3f dimension) {
		if (min.x > -1) {
			if (dimension.x < min.x)
				return false;
		}
		if (max.x > -1) {
			if (dimension.x > max.x)
				return false;
		}
		if (min.y > -1) {
			if (dimension.y < min.y)
				return false;
		}
		if (max.y > -1) {
			if (dimension.y > max.y)
				return false;
		}
		if (min.z > -1) {
			if (dimension.z < min.z)
				return false;
		}
		if (max.z > -1) {
			if (dimension.z > max.z)
				return false;
		}
		return true;
	}
}