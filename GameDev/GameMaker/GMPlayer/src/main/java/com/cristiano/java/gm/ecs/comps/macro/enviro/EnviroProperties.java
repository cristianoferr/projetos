package com.cristiano.java.gm.ecs.comps.macro.enviro;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;

public class EnviroProperties {
	private boolean isTerminal = false;
	private float minUsedSpace;
	private float maxUsedSpace;
	private float minArea;
	private float startingSolidChance;
	private float minEixo;
	private float maxAreaPerc;
	public float wallHeightMult; // the bubble height will be multiplied by this
									// number
	public float wallWidth;
	private float minDimensionRatio;
	private float maxDimensionRatio;
	public float minUsedArea;

	public void loadFromElement(IGameElement ge) {
		setTerminal(ge.getPropertyAsBoolean(GameProperties.IS_TERMINAL));
		this.minUsedSpace = ge
				.getPropertyAsFloat(GameProperties.ENVIRO_MIN_USED);
		this.maxUsedSpace = ge
				.getPropertyAsFloat(GameProperties.ENVIRO_MAX_USED);
		this.minArea = ge.getPropertyAsFloat(GameProperties.ENVIRO_MIN_AREA);
		this.startingSolidChance = ge
				.getPropertyAsFloat(GameProperties.ENVIRO_START_SOLID_CHANCE) / 100;
		this.minEixo = ge.getPropertyAsFloat(GameProperties.ENVIRO_MIN_EIXO);
		this.maxAreaPerc = ge
				.getPropertyAsFloat(GameProperties.ENVIRO_MAX_AREA_PERC);
		this.wallHeightMult = ge
				.getPropertyAsFloat(GameProperties.WALL_HEIGHT_MULT);
		this.wallWidth = ge.getPropertyAsFloat(GameProperties.WALL_WIDTH);

		this.minDimensionRatio = ge
				.getPropertyAsFloat(GameProperties.MIN_DIMENSION_RATIO);
		this.maxDimensionRatio = ge
				.getPropertyAsFloat(GameProperties.MAX_DIMENSION_RATIO);

		minUsedArea = ge.getPropertyAsFloat(GameProperties.MIN_USED_AREA);
	}

	public float getDimensionRatio() {
		return CRJavaUtils.random(minDimensionRatio, maxDimensionRatio);
	}

	public float getMaxAreaPerc() {
		return maxAreaPerc;
	}

	public float getMinEixo() {
		return minEixo;
	}

	public boolean isTerminal() {
		return isTerminal;
	}

	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}
}
