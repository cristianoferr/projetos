package com.cristiano.java.gm.ecs.systems.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.map.RoadComponent;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.TerrainComponent;
import com.cristiano.java.gm.ecs.systems.BuilderAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/*
 * UnitTest: TestRoadSolver
 * */
public class RoadSolverSystem extends BuilderAbstractSystem {

	public RoadSolverSystem() {
		super(GameComps.COMP_ROAD);
	}

	// used internally, no need to saving...
	Vector3f[] previousArea = null;

	// TODO: remove this in the final version...
	public static CRDebugDraw draw;
	public static CRDebugDraw drawPts;

	@Override
	// ent=RoomComponent
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {

		if (component.isFirstTick()) {
			MapComponent map = getMap();
			if (!map.isOnStage(MapComponent.ROAD_SOLVING)) {
				return;
			}

			RoadComponent roadComp = (RoadComponent) component;
			solveRoad(roadComp, map);
			roadComp.firstTick = false;
		}
	}

	private void solveRoad(RoadComponent roadComp, MapComponent map) {
		List<IGameEntity> rooms = entMan.getEntitiesWithComponent(roadComp);

		RoomComponent roomI = (RoomComponent) rooms.get(0);
		RoomComponent roomF = (RoomComponent) rooms.get(1);

		Vector3f posIni = roomI.getPosition();
		Vector3f posFim = roomF.getPosition();
		int pontos = map.roadPoints;
		String lineTag = map.roadFunctionTag;
		if ((lineTag == null) || ("".equals(lineTag))) {
			Log.fatal("lineTag (solveRoad) is null!!");
		}
		if (!roomI.hasEdges()){
			Log.fatal("Room "+roomI+" dont have edges...");
			return;
		}
		if (!roomF.hasEdges()){
			Log.fatal("Room "+roomI+" dont have edges...");
			return;
		}

		TerrainComponent terrain = ECS.getTerrainFrom(map);
		initDebugDraw(terrain);
		ElementManager elementManager = (ElementManager) getElementManager();

		IGameElement line = BuilderUtils.generateElementLine(lineTag, posIni,
				posFim, pontos, elementManager);

		// parei aqui... precisa de alguma forma de pegar o edgePt do room e
		// aplicar aqui...
		List<Vector3f> edges0 = findPoints(true, roomI, roomF);
		List<Vector3f> edges1 = findPoints(false, roomI, roomF);
		float distance = roomI.getPosition().distance(roomF.getPosition());
		List<Vector3f> segments = BuilderUtils.extractPointsFromList(line,
				"ponto", Vector3f.ZERO);
		List<Vector3f> upperLine = transposeLine(edges0, distance, segments);
		List<Vector3f> underLine = transposeLine(edges1, distance, segments);

		if (line == null) {
			Log.fatal("Line (solveRoad) is null...");
		}

		debugDrawPoints(posIni, posFim, terrain, line);
		carregaSegmentos(map, roadComp, terrain, rooms, upperLine, underLine,
				segments);

	}

	// This Method gets a line (points) and transpose it to the "edges",
	// changing its scale if necessary
	public List<Vector3f> transposeLine(List<Vector3f> edges, float distance,
			List<Vector3f> points) {
		Vector3f[] ret = new Vector3f[points.size()];
		if (edges.size() != 2) {
			Log.error("Edges has a wrong number of points:" + edges.size());
			return null;
		}
		float edgeDistance = edges.get(0).distance(edges.get(1));
		float ratio = edgeDistance / distance;
		Vector3f edgeI = edges.get(0);
		Vector3f edgeF = edges.get(1);
		ret[0] = edgeI;
		ret[points.size() - 1] = edgeF;
		int i = 1;
		int f = points.size() - 2;
		// adicionar a diferença do ponto até a distancia for maior que
		// anterior...
		Vector3f ptI = new Vector3f(edgeI);
		Vector3f ptF = new Vector3f(edgeF);

		int c = 0;
		while (i <= f) {
			Vector3f difI = points.get(i).subtract(points.get(i - 1))
					.mult(ratio);
			Vector3f difF = points.get(f).subtract(points.get(f + 1))
					.mult(ratio);
			//I want to update just one position per loop...
			if (c % 2 == 0) {
				ptI = ret[i - 1].add(difI);
				ret[i] = ptI;
				i++;
			} else {
				ptF = ret[f + 1].add(difF);
				ret[f] = ptF;
				f--;
			}

			c++;
		}

		List<Vector3f> listRet = new ArrayList<Vector3f>();
		for (i = 0; i < ret.length; i++) {
			Vector3f v = ret[i];
			// if the position is null, I generate an average position (this shouldn´t occur anymore)
			if (v == null) {
				v = ret[i - 1].add(ret[i + 1]).divide(2);
				Log.warn("transposeLine generated a null position...");
			}
			listRet.add(v);
		}
		return listRet;
	}

	public List<Vector3f> findPoints(boolean closest, RoomComponent roomI,
			RoomComponent roomF) {
		
		List<Vector3f> ret = new ArrayList<Vector3f>();
		
		float angleRooms=CRMathUtils.calcDegreesXZ(roomI.getPosition(), roomF.getPosition());
		float angleI0toF0=CRMathUtils.calcDegreesXZ(roomI.edgePt0, roomF.edgePt0);
		float angleI0toF1=CRMathUtils.calcDegreesXZ(roomI.edgePt0, roomF.edgePt1);
		//float angleI1toF0=CRMathUtils.calcDegreesXZ(roomI.edgePt1, roomF.edgePt0);
		//float angleI1toF1=CRMathUtils.calcDegreesXZ(roomI.edgePt1, roomF.edgePt1);
		
		float dif1=Math.abs(angleI0toF0-angleRooms);
		float dif2=Math.abs(angleI0toF1-angleRooms);
		
	
		if (dif1<dif2){
			if (closest) {
				ret.add(roomI.edgePt0);
				ret.add(roomF.edgePt0);
			} else {
				ret.add(roomI.edgePt1);
				ret.add(roomF.edgePt1);
			}
		} else {
			if (closest) {
				ret.add(roomI.edgePt0);
				ret.add(roomF.edgePt1);
			} else {
				ret.add(roomI.edgePt1);
				ret.add(roomF.edgePt0);
			}
		}

		return ret;
	}

	private void debugDrawPoints(Vector3f posIni, Vector3f posFim,
			TerrainComponent terrain, IGameElement line) {
		drawPts.drawCircle(new Vector2f(posIni.x / terrain.scale, posIni.z
				/ terrain.scale), 5, Color.blue);
		drawPts.drawCircle(new Vector2f(posFim.x / terrain.scale, posFim.z
				/ terrain.scale), 5, Color.green);
		drawPts.drawString(line.getIdentifier(), posIni.x / terrain.scale,
				posIni.z / terrain.scale);
		drawPts.drawLineXZ(posIni.mult(1 / terrain.scale).add(50, 0, 0),
				posFim.mult(1 / terrain.scale));
	}

	void carregaSegmentos(MapComponent map, RoadComponent roadComp,
			TerrainComponent terrain, List<IGameEntity> rooms,
			List<Vector3f> upperLine, List<Vector3f> underLine,
			List<Vector3f> segments) {

		/*
		 * List<IGameElement> lines = line.getObjectList("line"); for
		 * (IGameElement elLine : lines) { carregaSegmentos(map, roadComp,
		 * elLine, terrain, posIni, posFim, rooms); }
		 */
		if (upperLine.isEmpty()) {
			Log.error("upperLine has no points");
			return;
		}
		if (underLine.isEmpty()) {
			Log.error("underLine has no points");
			return;
		}
		if (segments.isEmpty()) {
			Log.fatal("segments has no points");
			return;
		}

		for (int i = 0; i < segments.size(); i++) {
			adicionaWaypoint(map, roadComp, segments, i);
			adicionaWall(map, upperLine, i);
			adicionaWall(map, underLine, i);
			flattenSegment(map, terrain, upperLine, underLine, i);
			// adicionaSegmento(posAnt, posAtual, map, terrain, true);
		}
		previousArea = null;

	}

	private void flattenSegment(MapComponent map, TerrainComponent terrain,
			List<Vector3f> upperLine, List<Vector3f> underLine, int i) {
		if (i == 0) {
			return;
		}
		Vector3f topLeft = upperLine.get(i - 1);
		Vector3f bottomLeft = underLine.get(i - 1);
		Vector3f topRight = upperLine.get(i);
		Vector3f bottomRight = underLine.get(i);

		Vector3f[] tri1 = new Vector3f[] { topLeft, bottomLeft, topRight };
		Vector3f[] tri2 = new Vector3f[] { bottomRight, bottomLeft, topRight };
		terrain.flattenArea(tri1, draw);
		terrain.flattenArea(tri2, draw);
	}

	private void adicionaWall(MapComponent map, List<Vector3f> line, int i) {
		if (i == 0) {
			return;
		}
		Vector3f pt0 = line.get(i - 1);
		Vector3f pt1 = line.get(i);
		BuilderUtils.generateWall(entMan, (ElementManager) getElementManager(),
				map, pt0, pt1);

	}

	private void adicionaWaypoint(MapComponent map, RoadComponent roadComp,
			List<Vector3f> segments, int i) {
		// Vector3f posAnt = segments.get(i - 1);
		Vector3f posAtual = segments.get(i);
		ComponentRecipes.addWaypoint(game.getSnippets(), entMan, map, posAtual,
				roadComp);
	}

	private void initDebugDraw(TerrainComponent terrain) {
		if (draw == null) {
			draw = new CRDebugDraw(terrain.lengthOnPower);
			drawPts = new CRDebugDraw(terrain.lengthOnPower);
		}
	}


}
