package com.cristiano.java.gm.ecs.comps.map;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.macro.enviro.EnviroEntity;
import com.cristiano.java.gm.ecs.comps.macro.enviro.EnviroFitting;
import com.cristiano.java.gm.ecs.comps.macro.enviro.EnviroProperties;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class BubbleComponent extends GameComponent {

	public Vector3f position = null;
	public Vector3f dimensions = null;
	public boolean popped = false;

	public float floorHeight = 0;

	private EnviroFitting fitting;
	private EnviroProperties props;
	private EnviroEntity floorEntity;
	private EnviroEntity meshEntity;
	private EnviroEntity separatorEntity;
	public String subBubbleTag;// children tag filter... its cumulative (or)
	public boolean solved = false;
	public float spacing;
	public BubbleComponent parentBubble;

	public BubbleComponent() {
		super(GameComps.COMP_BUBBLE);
	}
	@Override
	public void free() {
		super.free();
		position=null;
		dimensions=null;
		fitting=null;
		props=null;
		floorEntity=null;
		meshEntity=null;
		separatorEntity=null;
		parentBubble=null;
		
	}
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);

		fitting = new EnviroFitting();
		fitting.loadFromElement(ge.getPropertyAsGE(GameProperties.FITTING));
		props = new EnviroProperties();
		props.loadFromElement(ge.getPropertyAsGE(GameProperties.PROPERTIES));
		
		meshEntity=createEnviroEntity(ge,GameProperties.ENTITY_OBJ);
		floorEntity=createEnviroEntity(ge,GameProperties.FLOOR);
		separatorEntity=createEnviroEntity(ge,GameProperties.SEPARATOR_OBJ);
		
		subBubbleTag = StringHelper.removeChaves(ge.getProperty(GameProperties.BUBBLE_SUB));
	}

	private EnviroEntity createEnviroEntity(IGameElement ge,String prop) {
		IGameElement meshGE = ge.getPropertyAsGE(prop);
		if (meshGE!=null){
			EnviroEntity ee= new EnviroEntity();
			ee.loadFromElement(meshGE);
			return ee;
		} else {
			Log.error(prop+" is null");
			return null;
		}
	}

	@Override
	public IGameComponent clonaComponent() {
		BubbleComponent ret = new BubbleComponent();
		ret.loadFromElement(this.getElement());
		ret.setEntityManager(entMan);
		return ret;
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		if (getElement()==null){
			Log.fatal("Bubble Element is null!!");
		}
		loadFromElement(getElement());
	}

	public boolean fitsInto(Vector3f dimension) {
		return fitting.fitsInto(dimension);

	}

	public float getMinUsedArea() {
		return props.minUsedArea;
	}

	public void setFittings(int maxX, int minX, int maxY, int minY, int maxZ, int minZ) {
		fitting = new EnviroFitting(maxX, minX, maxY, minY, maxZ, minZ);

	}

	public String getFloorMeshTag() {
		if (floorEntity == null) {
			return null;
		}
		return floorEntity.getMeshTag();
	}

	public IGameElement getFloorElement() {
		return floorEntity.getMeshElement();
	}

	public void setFloorElement(IGameElement elFloor) {
		floorEntity.setMeshElement(elFloor);
	}

	public String getWallMeshTag() {
		if (separatorEntity == null) {
			return null;
		}
		return separatorEntity.getMeshTag();
	}

	public String getEntityMeshTag() {
		if (meshEntity == null) {
			return null;
		}
		return meshEntity.getMeshTag();
	}

	public IGameElement getEntityElement() {
		return meshEntity.getMeshElement();
	}

	public float getWallHeightMult() {
		return props.wallHeightMult;
	}

	public float getWallWidth() {
		return props.wallWidth;
	}

	public float getEntityDimensionRatio() {
		return props.getDimensionRatio();
	}

	public String getCarrierTag() {
		return meshEntity.getCarrierTag();
	}

	public boolean isTerminal() {
		return props.isTerminal();
	}

	public float getArea() {
		return (dimensions.x + spacing) * (dimensions.z + spacing);
	}

	public float getMaxAreaPerc() {
		return props.getMaxAreaPerc();
	}

	public float getMinEixo() {
		return props.getMinEixo();
	}

	public boolean isPointInside(Vector3f point) {

		if ((point.x >= position.x - dimensions.x / 2) && (point.x <= position.x + dimensions.x / 2)) {
			if ((point.z >= position.z - dimensions.z / 2) && (point.z <= position.z + dimensions.z / 2)) {
				return true;
			}
		}

		return false;
	}

	private boolean checaEixo(Vector3f center, Vector3f posFinal) {
		int n = 150;
		Vector3f pt = new Vector3f(center);
		Vector3f dif = posFinal.subtract(pt).mult(1f / n);
		for (int i = 0; i < n + 2; i++) {
			if (isPointInside(pt)) {
				return true;
			}
			pt.addLocal(dif);
		}
		return false;
	}

	public boolean isPositionInside(Vector3f pt, Vector3f dimension) {
		int quad = getIntersectionQuadrant(pt, dimension);
		return quad >= 0;
	}

	public int getIntersectionQuadrant(Vector3f pt, Vector3f dimension) {
		// the returned quadrant is from the parameter point of view...
		if (checaEixo(pt, new Vector3f(pt.x - dimension.x / 2, 0, pt.z - dimension.z / 2))) {
			return 0;
		}
		if (checaEixo(pt, new Vector3f(pt.x + dimension.x / 2, 0, pt.z - dimension.z / 2))) {
			return 1;
		}
		if (checaEixo(pt, new Vector3f(pt.x - dimension.x / 2, 0, pt.z + dimension.z / 2))) {
			return 2;
		}
		if (checaEixo(pt, new Vector3f(pt.x + dimension.x / 2, 0, pt.z + dimension.z / 2))) {
			return 3;
		}
		return -1;
	}

	public void suggestPosition(Vector3f pt, Vector3f dim) {
		int quad = getIntersectionQuadrant(pt, dim);
		if (quad < 0) {
			return;
		}
		float xdif = 0;
		float zdif = 0;

		if (quad == 1) {
			xdif = -checkSuggest(xdif, (pt.x + dim.x / 2) - (position.x - dimensions.x / 2));
			zdif = checkSuggest(zdif, (position.z + dimensions.z / 2) - (pt.z - dim.z / 2));
		}

		if (quad == 0) {
			xdif = checkSuggest(xdif, (position.x + dimensions.x / 2) - (pt.x - dim.x / 2));
			zdif = checkSuggest(zdif, (position.z + dimensions.z / 2) - (pt.z - dim.z / 2));
		}
		if (quad == 2) {
			xdif = checkSuggest(xdif, (position.x + dimensions.x / 2) - (pt.x - dim.x / 2));
			zdif = -checkSuggest(zdif, (pt.z + dim.z / 2) - (position.z - dimensions.z / 2));
		}
		if (quad == 3) {
			xdif = -checkSuggest(xdif, (pt.x + dim.x / 2) - (position.x - dimensions.x / 2));
			zdif = -checkSuggest(zdif, (pt.z + dim.z / 2) - (position.z - dimensions.z / 2));
		}

		if (xdif != 0) {
			if (xdif > 0)
				xdif += spacing;
			if (xdif < 0)
				xdif -= spacing;
		}
		if (zdif != 0) {
			if (zdif > 0)
				zdif += spacing;
			if (zdif < 0)
				zdif -= spacing;
		}

		// I check if by adding just one axis I remove the point from inside the
		// other...
		if (Math.abs(xdif) < Math.abs(zdif)) {
			checkXFirst(pt, dim, xdif, zdif);
		} else {
			checkZFirst(pt, dim, xdif, zdif);
		}

	}

	private void checkZFirst(Vector3f pt, Vector3f dim, float xdif, float zdif) {
		if (!isPositionInside(pt.add(0, 0, zdif), dim)) {
			pt.addLocal(0, 0, zdif);
		} else if (!isPositionInside(pt.add(xdif, 0, 0), dim)) {
			pt.addLocal(xdif, 0, 0);
		} else {
			pt.addLocal(xdif, 0, zdif);
		}
	}

	private void checkXFirst(Vector3f pt, Vector3f dim, float xdif, float zdif) {
		if (!isPositionInside(pt.add(xdif, 0, 0), dim)) {
			pt.addLocal(xdif, 0, 0);
		} else if (!isPositionInside(pt.add(0, 0, zdif), dim)) {
			pt.addLocal(0, 0, zdif);
		} else {
			pt.addLocal(xdif, 0, zdif);
		}
	}

	private float checkSuggest(float dif, float g) {
		if (g > 0) {
			return g;
		}
		return dif;
	}

	public float getFloorHeight() {
		return floorEntity.getHeight();
	}

	@Override
	public void resetComponent() {
	}

	public EnviroEntity getWallEnviro() {
		return floorEntity;
	}

	
}
