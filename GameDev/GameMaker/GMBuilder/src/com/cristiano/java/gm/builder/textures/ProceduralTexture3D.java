package com.cristiano.java.gm.builder.textures;

import com.cristiano.java.gm.ecs.comps.effects.SpatialComponent;
import com.cristiano.java.gm.ecs.comps.materials.MaterialComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.hull.Face2D;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class ProceduralTexture3D extends AbstractProceduralTexture implements IMakeTexture {

	@Override
	public void generateTexture(IGameElement elLayer, TextureComponent comp, float[][][] buffer, boolean applyBlending, MaterialComponent matComp) {
		initVars(elLayer, matComp);

		if (comp.applyToTerrain != null) {
			Log.debug("Generating Texture for Terrain..." + comp.getId());
			// draw=new CRDebugDraw(comp.length*2);
			generateTerrainTexture(elLayer, comp, buffer, applyBlending, matComp);
			// draw.finishDebugDraw("textureTerrain");
			Log.debug("Texture for Terrain generated...");
		} else {
			generateSpatialTexture(elLayer, comp, buffer, applyBlending, matComp);
		}
	}

	private void generateTerrainTexture(IGameElement elLayer, TextureComponent comp, float[][][] buffer, boolean applyBlending, MaterialComponent matComp) {

		TerrainComponent terrain = comp.applyToTerrain;
		float[] heightMap = terrain.getHeightMap();
		float minH = heightMap[0];
		float maxH = heightMap[0];

		for (int i = 0; i < heightMap.length; i++) {

			if (heightMap[i] < minH) {
				minH = heightMap[i];
			}
			if (heightMap[i] > maxH) {
				maxH = heightMap[i];
			}
		}

		// avoiding division by 0
		if (minH == maxH) {
			minH--;
			maxH++;
		}
		IGameElement elBlending = getBlendingObj(matComp, applyBlending);
		float scale = ((float) terrain.lengthOnPower) / ((float) comp.length);
		float x = 0;

		while (x < terrain.lengthOnPower) {
			float width = x / terrain.lengthOnPower * 2 - 1;
			float z = 0;
			while (z < terrain.lengthOnPower) {
				float depth = z / terrain.lengthOnPower * 2 - 1;
				float height = terrain.getValueAt((int) x, (int) z);
				height = ((minH - height) / (minH - maxH)) * 2 - 1;
				// Log.debug ("Height:"+height);
				elLayer.setVar("h", height);
				elLayer.setVar("w", width);
				elLayer.setVar("d", depth);
				float textX = ((float) x) / scale;
				float textY = ((float) z) / scale;
				solveLayerBuffer(buffer, elLayer, (int) textX, (int) textY, comp, elBlending);

				z += scale;
			}
			Log.debug("Terrain Texture Perc:" + CRMathUtils.round(x / terrain.lengthOnPower * 100, 2) + "%");
			x += scale;
		}

		Log.trace("end of generateTerrainTexture");
	}

	private void generateSpatialTexture(IGameElement elLayer, TextureComponent comp, float[][][] buffer, boolean applyBlending, MaterialComponent matComp) {
		// Faces2D faces = comp.applyToSpatial.faces2D;

		// blending: used to put 2 or more layers together by
		// adding/multiplying/dividing/etc
		IGameElement elBlending = getBlendingObj(matComp, applyBlending);
		Vector3f minPt = new Vector3f(), maxPt = new Vector3f();
		calcMaxMin(comp, minPt, maxPt);
		for (int i = 0; i < comp.length; i++) {
			for (int j = 0; j < comp.length; j++) {
				int x = i;
				int y = j;

				apply2dVars(elLayer, comp, i, j);
				apply3dVars(elLayer, comp, minPt, maxPt, x, y);

				solveLayerBuffer(buffer, elLayer, x, y, comp, elBlending);
			}
			if (comp.length > 1) {
				Log.debug("Spatial Texture Perc:" + CRMathUtils.round((((float) i / comp.length)) * 100, 2) + "%");
			}
		}
		// exportLayer(buffer,comp.length);
	}

	private void apply2dVars(IGameElement elLayer, TextureComponent comp, int i, int j) {
		// setting 2d params...
		elLayer.setVar("x", i / comp.length * 2 - 1);
		elLayer.setVar("y", j / comp.length * 2 - 1);
		elLayer.setVar("i", i);
		elLayer.setVar("length", comp.length);
		elLayer.setVar("j", j);
	}

	private void apply3dVars(IGameElement elLayer, TextureComponent comp, Vector3f minPt, Vector3f maxPt, int x, int y) {
		if (comp.applyToSpatial == null) {
			return;
		}
		Vector3f pt3d = calcTexturePoint3d(x, y, comp.applyToSpatial, comp.length);
		if (pt3d != null) {
			float width = (pt3d.x - minPt.x) / (maxPt.x - minPt.x) * 2 - 1;
			float height = (pt3d.y - minPt.y) / (maxPt.y - minPt.y) * 2 - 1;
			float depth = (pt3d.z - minPt.z) / (maxPt.z - minPt.z) * 2 - 1;
			elLayer.setVar("h", height);
			elLayer.setVar("w", width);
			elLayer.setVar("d", depth);
		}
	}

	private void calcMaxMin(TextureComponent comp, Vector3f minPt, Vector3f maxPt) {
		float atualizou = -1;
		for (int i = 0; i < comp.length; i++) {
			for (int j = 0; j < comp.length; j++) {
				Vector3f pt3d = calcTexturePoint3d(i, j, comp.applyToSpatial, comp.length);
				if (pt3d != null) {
					if (atualizou == -1) {
						atualizou = 1;
						minPt.set(pt3d);
						maxPt.set(pt3d);
					}
					if (pt3d.x < minPt.x) {
						minPt.x = pt3d.x;
					}
					if (pt3d.y < minPt.y) {
						minPt.y = pt3d.y;
					}
					if (pt3d.z < minPt.z) {
						minPt.z = pt3d.z;
					}
					if (pt3d.x > maxPt.x) {
						maxPt.x = pt3d.x;
					}
					if (pt3d.y > maxPt.y) {
						maxPt.y = pt3d.y;
					}
					if (pt3d.z > maxPt.z) {
						maxPt.z = pt3d.z;
					}
				}
			}
		}
	}

	private Vector3f calcTexturePoint3d(int x, int y, SpatialComponent applyToSpatial, float length) {
		if (applyToSpatial == null) {
			return null;
		}
		Face2D face = applyToSpatial.faces2D.getFaceWithPoint2D(x / length, y / length);
		if (face == null) {
			return null;
		}
		return applyToSpatial.faces2D.calcPoint3dFrom2d(face, x / length, y / length);
	}

}
