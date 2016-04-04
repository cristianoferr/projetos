package com.cristiano.galactic.view.demos;

import java.awt.Color;
import java.awt.Graphics2D;

public class PaintedTexture extends PaintableImage {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public PaintedTexture(int width,int height) {
        super(width, height, true);
        refreshImage();
    }

   
    public void paint(Graphics2D g) {
        g.setBackground(new Color(0f, 0f, 0f, 0f));
        g.clearRect(0, 0, getWidth(), getHeight());

    	System.out.println("paint");
    	for (int i=0;i<getWidth();i++){
    		for (int j=0;j<getHeight();j++){
    			g.setColor(new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), (float)Math.random()));
    			g.fillRect(i, j, i+1, j+1);
    		}
    	}
       
    }
}