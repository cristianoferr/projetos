package com.cristiano.java.gm.builder.textures;

import java.awt.Color;
import java.nio.ByteBuffer;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.CRMathUtils;
import com.jme3.util.BufferUtils;

public abstract class AbstractProceduralTexture {
	

	protected void solveLayerBuffer(float[][][] buffer, IGameElement elLayer, int x, int y, TextureComponent comp,
			IGameElement elBlending) {
		int length = calcLength(elLayer, comp);

		x = limitValue(x, length);
		y = limitValue(y, length);

		applyLayer(buffer, elLayer, x, y, comp, elBlending);
	}

	protected void exportLayer(float[][][] buffer, int length) {
		CRDebugDraw draw = new CRDebugDraw(length*2);
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < length; y++) {
				float red = buffer[length - 1 - y][x][GameConsts.RED_INDEX];
				float green = buffer[length - 1 - y][x][GameConsts.GREEN_INDEX];
				float blue = buffer[length - 1 - y][x][GameConsts.BLUE_INDEX];
				red=CRMathUtils.limitRange(red,0,1);
				green=CRMathUtils.limitRange(green,0,1);
				blue=CRMathUtils.limitRange(blue,0,1);
				draw.drawPoint(x, y, new Color(red, green, blue));
				draw.drawPoint(x + length, y, new Color(0, green, 0));
				draw.drawPoint(x, y + length, new Color(0, 0, blue));
				draw.drawPoint(x + length, y + length, new Color(red, 0, 0));
			}

		}
		draw.finishDebugDraw("debugTexture");
	}
	
	protected IGameElement getBlendingObj(MaterialComponent matComp, boolean applyBlending) {
		IGameElement elBlending = null;
		if (applyBlending) {
			elBlending = matComp.blending;
		}
		return elBlending;
	}

	
	private int limitValue(int x, int length) {
		if (x >= length) {
			x = length - 1;
		}
		if (x < 0)
			x = 0;
		return x;
	}

	private int calcLength(IGameElement elLayer, TextureComponent comp) {
		int length = comp.length;
		elLayer.setVar("length", length);
		return length;
	}

	protected void applyLayer(float[][][] buffer, IGameElement elLayer, int x, int y, TextureComponent comp,
			IGameElement elBlending) {

		float alpha = calcFunction(elLayer, GameProperties.TEXTURE_FUNCTION_ALPHA, x, y, comp);
		float red = calcFunction(elLayer, GameProperties.TEXTURE_FUNCTION_RED, x, y, comp);
		float green = calcFunction(elLayer, GameProperties.TEXTURE_FUNCTION_GREEN, x, y, comp);
		float blue = calcFunction(elLayer, GameProperties.TEXTURE_FUNCTION_BLUE, x, y, comp);

		red *= alpha;
		green *= alpha;
		blue *= alpha;

		applyColor(comp, buffer, x, y, red, green, blue, elBlending);

	}

	protected void initVars(IGameElement elLayer, MaterialComponent matComp) {
		elLayer.setVar("h", 0);
		elLayer.setVar("w", 0);
		elLayer.setVar("d", 0);
		elLayer.setVar("0", 0);
		elLayer.setVar("x", 0);
		elLayer.setVar("y", 0);
		elLayer.setVar("seed", matComp.seed);

	}

	private float calcFunction(IGameElement elLayer, String functionName, int x, int y, TextureComponent comp) {
		String function = elLayer.getProperty(functionName);
		float v = Float.parseFloat(elLayer.resolveFunctionOf(function));
		return v;
	}

	protected void applyColor(TextureComponent comp, float[][][] buffer, int x, int y, float red, float green,
			float blue, IGameElement elBlending) {

		x = limitIndex(comp, x);
		y = limitIndex(comp, y);

		// Calc the Blending...
		red = calcBlending(elBlending, "R", GameConsts.RED_INDEX, red, buffer, x, y, comp.length);
		green = calcBlending(elBlending, "G", GameConsts.GREEN_INDEX, green, buffer, x, y, comp.length);
		blue = calcBlending(elBlending, "B", GameConsts.BLUE_INDEX, blue, buffer, x, y, comp.length);

	}

	public static float calcBlending(IGameElement elBlending, String colorAbbr, int color_index, float currColor,
			float[][][] buffer, int x, int y, int length) {
		float newColor = currColor;
		if (elBlending != null) {
			float prevVal = buffer[x][y][color_index];
			elBlending.setVar("prev" + colorAbbr, prevVal);
			elBlending.setVar("curr" + colorAbbr, currColor);
			String propertyHAsText = ((AbstractElement) elBlending).getPropertyHAsText("calc" + colorAbbr, true);
			newColor = Float.parseFloat(propertyHAsText);
		}
		newColor = CRMathUtils.limitColor(newColor);
		buffer[x][y][color_index] = newColor;
		return newColor;
	}

	

	private int limitIndex(TextureComponent comp, int x) {
		if (x >= comp.length) {
			x = comp.length - 1;
		}
		if (x < 0) {
			x = 0;
		}
		return x;
	}

	public static float[][][] initArray(int width, int height) {
		float[][][] array = new float[width][height][GameConsts.DATA_SIZE];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				for (int k = 0; k < GameConsts.DATA_SIZE; k++) {
					array[i][j][k] = 0;
				}
			}
		}
		return array;
	}

	public static ByteBuffer criaBuffer(float[][][] array, TextureComponent comp,boolean invertY) {
		if (invertY){
			array=invertYBuffer(array,comp.length);
		}
		ByteBuffer buffer = BufferUtils.createByteBuffer(comp.length * comp.length * GameConsts.DATA_SIZE);
		for (int i = 0; i < comp.length; i++) {
			for (int j = 0; j < comp.length; j++) {
				for (int k = 0; k < GameConsts.DATA_SIZE; k++) {
					float f = array[i][j][k];
					byte b = CRJavaUtils.convertFloatToByte(f);
					buffer.put(b);
				}
			}
		}
		return buffer;
	}

	private static float[][][] invertYBuffer(float[][][] array,int length) {
		float[][][] ret=new float[length][length][GameConsts.DATA_SIZE];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < GameConsts.DATA_SIZE; k++) {
					ret[i][j][k]=array[length-i-1][j][k];
				}
			}
		}
		return ret;
	}

}
