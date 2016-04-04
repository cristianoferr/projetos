package com.cristiano.java.gm.ecs.comps.visual;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.Log;
import com.jme3.input.ChaseCamera;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;

/*
 * Objetos com esse componente estao sendo visualizados (a camera esta nele)*/
public class CamComponent extends GameComponent {

	public Camera cam=null;
	public float distX;
	public float distY;
	public float distZ;
	public String camType;
	public float minDist;
	public float maxDist;
	public ChaseCamera chaseCam;
	public CameraNode camNode;
	private IGameElement elCamera;
	public float angleRotateX;
	public float angleRotateY;
	public float angleRotateZ;

	public CamComponent() {
		super(GameComps.COMP_CAM);
		firstTick=false; //this will be activated when added...
	}
	
	@Override
	public void free() {
		super.free();
		cam=null;
		chaseCam=null;
		camNode=null;
		camType=null;
		angleRotateX=0;
		angleRotateY=0;
		angleRotateZ=0;
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		elCamera=ge.getPropertyAsGE(GameProperties.CAM_TYPE);
		if (elCamera==null){
			Log.fatal("No Camera Element defined!");
		}
		this.camType=elCamera.getProperty(GameProperties.VALUE);
		this.distX=elCamera.getPropertyAsFloat(GameProperties.DIST_X);
		this.distY=elCamera.getPropertyAsFloat(GameProperties.DIST_Y);
		this.distZ=elCamera.getPropertyAsFloat(GameProperties.DIST_Z);
		this.minDist=elCamera.getPropertyAsFloat(GameProperties.MIN_DIST);
		this.maxDist=elCamera.getPropertyAsFloat(GameProperties.MAX_DIST);
		this.angleRotateX=elCamera.getPropertyAsFloat(GameProperties.ANGLE_ROTATE_X);
		this.angleRotateY=elCamera.getPropertyAsFloat(GameProperties.ANGLE_ROTATE_Y);
		this.angleRotateZ=elCamera.getPropertyAsFloat(GameProperties.ANGLE_ROTATE_Z);
	}
	
	
	@Override
	public IGameComponent clonaComponent() {
		CamComponent ret = new CamComponent();
		ret.cam=cam;
		ret.distY=distY;
		ret.distZ=distZ;
		ret.camType=camType;
		ret.minDist=minDist;
		ret.maxDist=maxDist;
		ret.angleRotateX=angleRotateX;
		return ret;
	}

	public void setEnabled(boolean flagCamera) {
		if (chaseCam!=null){
			chaseCam.setEnabled(flagCamera);
		}
		if (camNode!=null){
			camNode.setEnabled(flagCamera);
		}
	}

	@Override
	public void resetComponent() {
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.DIST_X,this.distX);
		obj.put(GameProperties.DIST_Y,this.distY);
		obj.put(GameProperties.DIST_Z,this.distZ);
		obj.put(GameProperties.CAM_TYPE,this.camType);
		obj.put(GameProperties.MIN_DIST,this.minDist);
		obj.put(GameProperties.MAX_DIST,this.maxDist);
		obj.put(GameProperties.ANGLE_ROTATE_X,this.angleRotateX);
		obj.put(GameProperties.ANGLE_ROTATE_Y,this.angleRotateY);
		obj.put(GameProperties.ANGLE_ROTATE_Z,this.angleRotateZ);
		return true;
	}
	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		this.distX=CRJsonUtils.getFloat(obj,GameProperties.DIST_X);
		this.distY=CRJsonUtils.getFloat(obj,GameProperties.DIST_Y);
		this.distZ=CRJsonUtils.getFloat(obj,GameProperties.DIST_Z);
		this.camType=(String) obj.get(GameProperties.CAM_TYPE);
		this.minDist=CRJsonUtils.getFloat(obj,GameProperties.MIN_DIST);
		this.maxDist=CRJsonUtils.getFloat(obj,GameProperties.MAX_DIST);
		this.angleRotateX=CRJsonUtils.getFloat(obj,GameProperties.ANGLE_ROTATE_X);
		this.angleRotateY=CRJsonUtils.getFloat(obj,GameProperties.ANGLE_ROTATE_Y);
		this.angleRotateZ=CRJsonUtils.getFloat(obj,GameProperties.ANGLE_ROTATE_Z);
		
	}

}
