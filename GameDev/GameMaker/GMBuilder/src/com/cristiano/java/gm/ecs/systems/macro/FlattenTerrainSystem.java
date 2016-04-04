package com.cristiano.java.gm.ecs.systems.macro;

import java.util.ArrayList;

import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.FlattenTerrainComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.systems.BuilderAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.jme3.hull.in2D.ConvexHull2D;
import com.cristiano.jme3.utils.JMEUtils;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class FlattenTerrainSystem extends BuilderAbstractSystem {

	public FlattenTerrainSystem() {
		super(GameComps.COMP_FLATTEN_TERRAIN);
	}

	@Override
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		FlattenTerrainComponent comp = (FlattenTerrainComponent) component;

		TerrainComponent terrain = (TerrainComponent) ent
				.getComponentWithIdentifier(GameComps.COMP_TERRAIN);
		if (comp.elMeshFunction != null) {
			flattenMesh(terrain, comp);
		}

		ent.removeComponent(component);
		Log.debug("Flattening finished...");
	}

	private void flattenMesh(TerrainComponent terrain,
			FlattenTerrainComponent comp) {
		ArrayList<Vector3f> points = BuilderUtils.extractPointsFrom(
				comp.elMeshFunction, comp.position);
		Vector3f min = new Vector3f(points.get(0));
		Vector3f max = new Vector3f(points.get(0));

		// Calculo a quantidade de pontos para determinar se houve mudança na
		// shape, se houve é por que o ponto está fora
		int qtdPointsMesh = ConvexHull2D.convexHull(points).size();

		JMEUtils.calcMinMax(points, min, max);
		Log.debug("Flattening Terrain at " + comp.position + " min:" + min
				+ " max:" + max);
		Vector3f pt = new Vector3f(min);
		while (pt.x <= max.x) {
			pt.z = min.z;
			while (pt.z <= max.z) {
				if (JMEUtils.isPointInsideMesh(pt, points, qtdPointsMesh)) {
					terrain.setScaledValueAt(pt.x, pt.z, comp.position.y+CRJavaUtils.random());
				}

				pt.z++;
			}
			pt.x++;
		}
	}

	

	

}
