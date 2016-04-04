package com.cristiano.java.gm.builder.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.systems.map.RoadSolverSystem;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.Log;

public abstract class ExportUtils {

	public static BufferedImage loadImage(String imageSource) {
		BufferedImage img = null;
		try {
			// resource = comp.getReadingResource(getElementManager());
			InputStream inputStream = GMAssets.getInputStream(imageSource);
			img = ImageIO.read(inputStream);
		} catch (Exception e) {
			Log.error("Error reading Image" );
			e.printStackTrace();
		}
		return img;
	}
	
	public static void exportTerrain(TerrainComponent comp) {
		float min = comp.getValueAt(0, 0);
		float max = comp.getValueAt(0, 0);
		for (int x = 0; x < comp.lengthOnPower; x++) {
			for (int y = 0; y < comp.lengthOnPower; y++) {
				float z = comp.getValueAt(x, y);
				if (z < min) {
					min = z;
				}
				if (z > max) {
					max = z;
				}
			}
		}
		CRDebugDraw draw = new CRDebugDraw(comp.lengthOnPower);

		for (int x = 0; x < comp.lengthOnPower; x++) {
			for (int y = 0; y < comp.lengthOnPower; y++) {
				float z = comp.getValueAt(x, y);
				z = (z - min) / (max - min);
				draw.drawPoint(x, y, new Color(z, z, z, 1));
			}
		}
		draw.finishDebugDraw("exportTerrain");
		if (RoadSolverSystem.draw != null) {
			RoadSolverSystem.draw.finishDebugDraw("roadSegment");
			RoadSolverSystem.drawPts.finishDebugDraw("roadSegmentPts");
		}
		Log.debug("Terrain Exported");
	}

	public static void resizeImage(String fullFilePath, String toFile,
			int toSize) {
		try {
			Thumbnails.of(fullFilePath).size(toSize, toSize).toFile(toFile);
		} catch (IOException e) {
			e.printStackTrace();
			Log.fatal("Error resizing image:"+fullFilePath);
		}
		
	}
}
