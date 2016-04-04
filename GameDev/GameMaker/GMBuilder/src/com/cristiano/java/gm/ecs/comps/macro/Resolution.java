package com.cristiano.java.gm.ecs.comps.macro;

public class Resolution{
	String device=null; //"Phone","tablet7","tablet10"
	int width=0;
	int height=0;
	
	public Resolution(String device, int width, int height) {
		this.device=device;
		this.width=width;
		this.height=height;
	}

	public String getDevice() {
		return device;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	
}