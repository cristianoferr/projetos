package com.cristiano.java.gm.builder.utils;

import java.util.List;

import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.BubbleComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public abstract class EnviroUtils {

	public static Vector3f getAbsolutePosition(BubbleComponent comp, BubbleComponent ph) {
		float dx = comp.position.x - comp.dimensions.x / 2 + ph.position.x;
		float dz = comp.position.z - comp.dimensions.z / 2 + ph.position.z;
		Vector3f pos = new Vector3f(dx, 0, dz);
		pos.y = 0;
		return pos;

	}

	public static boolean isPontoValido(Vector3f pt, List<BubbleComponent> BubbleComponents) {
		for (BubbleComponent ph : BubbleComponents) {
			if (ph.isPointInside(pt)) {
				return false;
			}
		}
		return true;
	}

	public static float getAreaEmUso(BubbleComponent parentBubble) {
		List<IGameComponent> bubbles = parentBubble.getComponentsWithIdentifier(GameComps.COMP_BUBBLE);
		float area = 0;
		for (IGameComponent ph : bubbles) {
			BubbleComponent bubble = (BubbleComponent) ph;
			area += bubble.getArea();
		}
		return area;
	}

	public static float getAreaEmUsoPerc(BubbleComponent parentBubble) {
		float areaTotal = parentBubble.dimensions.x * parentBubble.dimensions.z;
		if (areaTotal == 0) {
			Log.error("Bubble Area is 0...");
		}
		float area = getAreaEmUso(parentBubble) / areaTotal * 100;
		return area;
	}

}
