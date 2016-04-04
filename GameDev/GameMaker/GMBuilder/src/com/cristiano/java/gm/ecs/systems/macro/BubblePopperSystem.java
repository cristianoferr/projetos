package com.cristiano.java.gm.ecs.systems.macro;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.gm.builder.utils.EnviroUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.map.BubbleComponent;
import com.cristiano.java.gm.ecs.comps.map.BubbleDataComponent;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.systems.BuilderAbstractSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.java.gm.utils.GMUtils;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class BubblePopperSystem extends BuilderAbstractSystem {

	BubbleDataComponent data = null; // atalho para rápido acesso

	public BubblePopperSystem() {
		super(GameComps.COMP_BUBBLE);
	}

	@Override
	// ent=room
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		BubbleComponent comp = (BubbleComponent) component;

		startComponent();

		if (!comp.solved) {
			Log.debug("Solving bubble:" + comp.getId());
			solveBubble(comp, ent);
			comp.solved = true;
		}

		if (!comp.popped) {
			Log.debug("Generating sub-bubbles for bubble:" + comp.getId());
			popBubble(comp);
		}

		finishTheBubble(ent, comp);
	}

	private void finishTheBubble(IGameEntity ent, BubbleComponent comp) {
		if (comp.popped) {
			MapComponent map = getMap();
			// checking if there are any child...
			BubbleComponent parent = comp.parentBubble;
			if (parent != null) {
				if (!parent.popped) {
					return;
				}
			}
			if (map.stageControl.isOnStage(MapComponent.BUBBLE_SOLVING)) {
				entMan.removeComponentFromEntity(comp, ent);
			}
		}
	}

	private void popBubble(BubbleComponent comp) {
		Vector3f dimension = calcValidDimension(comp,
				EnviroUtils.getAreaEmUsoPerc(comp));
		Vector3f position = calculaPontoInicialValido(comp, dimension);

		addSubBubble(comp, dimension, position);

		comp.popped = (hasPopped(comp));

	}

	private Vector3f calculaPontoInicialValido(BubbleComponent parent,
			Vector3f dimension) {
		List<IGameComponent> bubbles = parent
				.getComponentsWithIdentifier(GameComps.COMP_BUBBLE);
		boolean valid = true;
		int loops = 0;
		Vector3f pt = null;
		do {
			valid = true;
			pt = new Vector3f(parent.dimensions.x / 2 - parent.dimensions.x
					* CRJavaUtils.random(), 0, parent.dimensions.z / 2
					- parent.dimensions.z * CRJavaUtils.random())
					.add(parent.position);

			centralizaPonto(parent, dimension, pt);

			// Must be separated... the first loop I check and change
			// position...
			loopAndSuggest(dimension, bubbles, pt);
			// and I check if it still is invalid after all "suggestions"...
			valid = loopAndValidate(dimension, bubbles, valid, pt);

			// but, if the point is outside the parent then something is wrong
			// (maybe too many suggestions)
			if (!parent.isPositionInside(pt, dimension)) {
				valid = false;
			}
			if (!valid) {
				pt = null;
			}
			loops++;
		} while ((!valid) && (loops < GameConsts.ERROR_TRIES));
		return pt;
	}

	private void centralizaPonto(BubbleComponent parent, Vector3f dimension,
			Vector3f pt) {
		float difx = (pt.x + dimension.x / 2f)
				- (parent.position.x + parent.dimensions.x / 2);
		float difz = (pt.z + dimension.z / 2f)
				- (parent.position.z + parent.dimensions.z / 2);
		if (difx > 0) {
			pt.x -= difx;
		}
		if (difz > 0) {
			pt.z -= difz;
		}
		difx = (pt.x - dimension.x / 2f)
				- (parent.position.x - parent.dimensions.x / 2);
		difz = (pt.z - dimension.z / 2f)
				- (parent.position.z - parent.dimensions.z / 2);
		if (difx < 0) {
			pt.x -= difx;
		}
		if (difz < 0) {
			pt.z -= difz;
		}
	}

	private boolean loopAndValidate(Vector3f dimension,
			List<IGameComponent> bubbles, boolean valid, Vector3f pt) {
		for (IGameComponent comp : bubbles) {
			BubbleComponent bubble = (BubbleComponent) comp;
			if (bubble.isPositionInside(pt, dimension)) {
				valid = false;
			}
		}
		return valid;
	}

	private void loopAndSuggest(Vector3f dimension,
			List<IGameComponent> bubbles, Vector3f pt) {
		for (IGameComponent comp : bubbles) {
			BubbleComponent bubble = (BubbleComponent) comp;
			if (bubble.isPositionInside(pt, dimension)) {
				bubble.suggestPosition(pt, dimension);
			}
		}
	}

	public Vector3f calcValidDimension(BubbleComponent comp, float areaEmUso) {

		Vector3f dimension;
		float areaLivre = 1 - areaEmUso / 100;
		float multi = areaLivre * comp.getMaxAreaPerc() / 100;
		dimension = new Vector3f(comp.getMinEixo()
				+ (comp.dimensions.x - comp.getMinEixo())
				* CRJavaUtils.random() * multi, comp.dimensions.y,
				comp.getMinEixo() + (comp.dimensions.z - comp.getMinEixo())
						* CRJavaUtils.random() * multi);

		return dimension;
	}

	private void addSubBubble(BubbleComponent comp, Vector3f dimension,
			Vector3f position) {
		BubbleComponent bubble = data.cloneBubbleFittingInto(dimension,
				comp.subBubbleTag);
		int x;
		if (bubble != null) {
			bubble.position = position;

			bubble.dimensions = dimension;
			bubble.spacing = comp.spacing;
			bubble.parentBubble = comp;
			entMan.addComponent(bubble, comp);
		}
	}

	private boolean hasPopped(BubbleComponent comp) {
		if (comp.isTerminal()) {
			return true;
		}
		float area = EnviroUtils.getAreaEmUsoPerc(comp);
		Log.debug("hasPopped: area:" + area + " minArea:"
				+ comp.getMinUsedArea() + " Bubble:" + comp.getId());
		if (area > comp.getMinUsedArea())
			return true;
		return false;
	}

	private void solveBubble(BubbleComponent comp, IGameEntity ent) {
		ElementManager em = (ElementManager) getElementManager();
		solveFloor(em, comp);
		solveWall(em, comp);
		solveMesh(em, comp);
		comp.popped = hasPopped(comp);
	}

	private void solveMesh(ElementManager em, BubbleComponent comp) {
		String mesh = comp.getEntityMeshTag();

		if (mesh != null) {
			float ratio = comp.getEntityDimensionRatio();
			Vector3f dimension = comp.dimensions.mult(ratio);
			IGameElement templ = BuilderUtils.mountEntity(em, mesh,
					comp.getCarrierTag(), dimension);
			ComponentRecipes.loadEntityFromElement(entMan, comp, templ,
					comp.position);
		}

	}

	private void solveWall(ElementManager em, BubbleComponent comp) {
		String meshTag = comp.getWallMeshTag();
		if (meshTag != null) {
			if (comp.dimensions.y > 0) {
				Log.debug("Loading wall Mesh with height: " + comp.dimensions.y);
				loadWallMeshes(em, comp, meshTag);
			} else {
				Log.warn("Bubble has height 0, no wall is being loaded.");
			}
		}

	}

	private void loadWallMeshes(ElementManager em, BubbleComponent comp,
			String meshTag) {
		Log.debug(comp.getElement().getIdentifier() + " is walled.");
		IGameElement floorElement = comp.getFloorElement();
		if (floorElement == null) {
			Log.fatal("FloorElement is null...");
			return;
		}
		ArrayList<Vector3f> points = BuilderUtils.extractPointsFrom(
				floorElement, Vector3f.ZERO);
		if (points.size() == 0) {
			Log.error("Error extracting points from floor element:empty");
			return;
		}
		float height = comp.dimensions.y * comp.getWallHeightMult();
		float width = comp.getWallWidth();

		Vector3f ptAnt = points.get(0);
		for (int i = 1; i < points.size(); i++) {
			game.addDebugBox(comp.position.add(ptAnt), "wall point");
			Vector3f pt = points.get(i);
			Vector3f posicao = pt.add(ptAnt).mult(0.5f).add(comp.position);

			float depth = pt.distance(ptAnt);
			float angle = CRMathUtils.calcDegreesXZ2(pt, ptAnt);
			posicao.y = comp.position.y + height / 2f;
			Vector3f dimension = new Vector3f(width, height, depth);
			BuilderUtils.addWallElement(entMan, em, comp, comp.getWallEnviro(),
					posicao, angle, dimension);
			ptAnt = pt;
		}

	}

	private void solveFloor(ElementManager em, BubbleComponent comp) {
		String mesh = comp.getFloorMeshTag();
		if (mesh != null) {
			Vector3f dimensions = new Vector3f(comp.dimensions);
			dimensions.y = comp.getFloorHeight();
			// TODO: define floor height
			IGameElement elFloor = em.createFinalElementFromTag(mesh,
					comp.getElement(),
					GMUtils.generateDimensionsProps(dimensions));
			comp.setFloorElement(elFloor);
			ComponentRecipes.flattenTerrain(entMan, comp.position, elFloor);
		}
	}

	private void startComponent() {
		if (data == null) {
			data = getBubbleData();
		}
	}

}
