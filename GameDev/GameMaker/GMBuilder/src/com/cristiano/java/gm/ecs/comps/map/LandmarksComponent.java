package com.cristiano.java.gm.ecs.comps.map;

import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.macro.enviro.EnviroEntity;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.CRMathUtils;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class LandmarksComponent extends GameComponent {


	public List<IGameElement> landmarks;
	public List<IGameElement> obstacles;
	public IGameElement landMarkInfo;
	public IGameElement obstacleInfo;
	
	//Random Generator 
	public Random random=null;
	
	public LandmarksComponent() {
		super(GameComps.COMP_LANDMARKS);
	}
	@Override
	public void free() {
		super.free();
		
	}
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		landmarks=ge.getObjectList(GameProperties.LANDMARKS);
		obstacles=ge.getObjectList(GameProperties.OBSTACLES);
		landMarkInfo=ge.getPropertyAsGE(GameProperties.LANDMARK_INFO);
		obstacleInfo=ge.getPropertyAsGE(GameProperties.OBSTACLE_INFO);
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
		LandmarksComponent ret = new LandmarksComponent();
		ret.loadFromElement(this.getElement());
		ret.setEntityManager(entMan);
		return ret;
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		return false;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		loadFromElement(getElement());
	}
	@Override
	public void resetComponent() {
		
	}
	public int getMaxObstacles() {
		return getMaxObjectsFrom(obstacleInfo);
	}
	
	private int getMaxObjectsFrom(IGameElement el) {
		return (int) CRJavaUtils.random(el.getPropertyAsInt("minQty"),el.getPropertyAsInt("maxQty"));
	}
	
	public IGameElement getRandomObstacle() {
		return getRandomObject(obstacles);
	}
	public IGameElement getRandomLandmark() {
		return getRandomObject(landmarks);
	}
	private IGameElement getRandomObject(List<IGameElement> objects) {
		return objects.get(random.nextInt(objects.size()));
	}
	
	public Vector3f getDoodadDimension(IGameElement obstacle,IGameElement elSize) {
		
		//sizeInfo leaf},minX=$minX,maxX=$maxX,minY=$minY,maxY=$maxY,minZ=$minZ,maxZ=$maxZ)
		float minX=elSize.getPropertyAsFloat(GameProperties.MIN_SIZE);
		float maxX=elSize.getPropertyAsFloat(GameProperties.MAX_SIZE);
		float minY=elSize.getPropertyAsFloat(GameProperties.MIN_SIZE);
		float maxY=elSize.getPropertyAsFloat(GameProperties.MAX_SIZE);
		float minZ=elSize.getPropertyAsFloat(GameProperties.MIN_SIZE);
		float maxZ=elSize.getPropertyAsFloat(GameProperties.MAX_SIZE);
		float x=minX+random.nextFloat()*(maxX-minX);
		float y=minY+random.nextFloat()*(maxY-minY);
		float z=minZ+random.nextFloat()*(maxZ-minZ);
		return new Vector3f(x,y,z);
	}
	
}
