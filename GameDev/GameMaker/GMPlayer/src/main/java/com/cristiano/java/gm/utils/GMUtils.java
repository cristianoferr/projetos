package com.cristiano.java.gm.utils;

import java.util.List;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.TeamComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public abstract class GMUtils {

	
	//Used to define a default name for nodes...
	public static String getNodeName(IGameEntity ent, String suffix) {
		return "NODE"+ent.getId()+"-"+suffix;
	}
	

	public static String generateDimensionsProps(Vector3f dimensions) {
		return "width=" + dimensions.x + ",height=" + dimensions.y + ",depth="
				+ dimensions.z;
	}


	public static ColorRGBA createColor(String color) {
		String red = color.substring(1, 3);
		String green = color.substring(3, 5);
		String blue = color.substring(5, 7);
		ColorRGBA cor = new ColorRGBA(CRJavaUtils.hex2dec(red) / 255f,
				CRJavaUtils.hex2dec(green) / 255f,
				CRJavaUtils.hex2dec(blue) / 255f, 1);

		return cor;
	}

	public static ColorRGBA createColor(IGameElement elMap) {
		String color = elMap.getProperty(GameProperties.COLOR);
		return createColor(color);
	}

	public static Vector3f addRotation(String objName, IGameElement elMesh,
			Vector3f rotation) {
		Vector3f ret = new Vector3f(rotation);
		ret.x += elMesh.getParamAsFloat(Extras.LIST_ROTATION, objName + ".x");
		ret.y += elMesh.getParamAsFloat(Extras.LIST_ROTATION, objName + ".y");
		ret.z += elMesh.getParamAsFloat(Extras.LIST_ROTATION, objName + ".z");
		return ret;
	}

	
	
	
}
