package com.cristiano.galactic;

public abstract class ViewConsts {
	
	public static final boolean drawBox = false;
    public static boolean useScale=true;
    public final static int clippingDist=   50000;
    public final static int maxDrawingDist=2500000;
    private static float maxFrustum=(float)maxDrawingDist*2f;

    //Dados atualizados durante o jogo
    private static double previousMaxDistanceObject=1,maxDistanceObject=1;

    
	public static void setMaxFrustum(float maxFrustum) {
		ViewConsts.maxFrustum = maxFrustum;
	}

	
	public static float getMaxFrustum() {
		return maxFrustum;
	}


	public static void setPreviousMaxDistanceObject(double previous) {
		previousMaxDistanceObject = previous;
	}


	public static double getPreviousMaxDistanceObject() {
		return previousMaxDistanceObject;
	}


	public static void setMaxDistanceObject(double max) {
		maxDistanceObject = max;
	}


	public static double getMaxDistanceObject() {
		return maxDistanceObject;
	}



}