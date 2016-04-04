package com.cristiano.galactic.view.models;


import javax.sound.sampled.Line;

import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.view.View;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;



public class ShipView extends ItemView implements IShipView {
	AbstractItemView target=null;
	Line lineVel=null;
	Line lineDir=null;
	Geometry geomVel=null;
	Geometry geomDir=null;
	
	Vector3f[] vertex_vel,vertex_dir;

	public ShipView( View view) {
		super(view);
	}
	
	@Override
	public AbstractItemView getTarget() {
		
		return target;
	}

	@Override
	public void setTarget(AbstractItemView target) {
		this.target=target;
		

	}

	
	public void loadModel(ModelData md){
		super.loadModel(md);
		
		createLines();
		//((View)view).getRootNode().attachChild(group_noRot);
		
		//view.backLinkVisualItem(lineVel,this,true);
/*		lineVel=new Line("LineVel",new Vector3f(0,0,0),2,Colorf.GREEN);
		lineDir=new Line(new Tuple3f(0,0,0),2,Colorf.RED);
		//lineFront=new Line(new Tuple3f((float)getTamX()*2,0,0),2,Colorf.BLUE);
		*/
	    
		PointLight light=new PointLight();
		light.setPosition(Vector3f.ZERO);
		//light.setRadius((float)getRadius());
		light.setColor(ColorRGBA.White);
		rootNode.addLight(light);
		
		
	}

	private void createLines() {
		initVertexes();
		
		

		lineVel=new Line(vertex_vel[0],vertex_vel[1]);
		lineVel.setLineWidth(5);

		
		geomVel = new Geometry("Geometria LinhaVel:"+getName(), lineVel);
        Material mat = new Material(getAssetManager(),
        "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.Green);   // set color of material to blue
        geomVel.setMaterial(mat);                   // set the cube's material

		
		//lineVel.setDefaultColor(ColorRGBA.Green);
		//lineVel.setLocalTranslation(vertex_vel[1].x, vertex_vel[1].y, vertex_vel[1].z);
		
		
		/*// Create a point light
		PointLight l = new PointLight();
		// Give it a location
		l.setLocation(vertex_vel[1]);
		// Make it a red light
		l.setDiffuse(ColorRGBA.yellow.clone());
		// Enable it
		l.setEnabled(true);
		
		LightState ls = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		ls.attach(l);
		ls.setTwoSidedLighting(true);
		lineVel.setLightState(ls);
		lineVel.setRenderState(ls);*/
		
		rootNode.attachChild(geomVel);
		
	//	group_noRot.setLocalTranslation(relCoord.getXf(),relCoord.getYf(),relCoord.getZf());
		lineDir=new Line(vertex_dir[0],vertex_dir[1]);
		lineDir.setLineWidth(5);
		Geometry geomDir = new Geometry("Geometria LinhaVel:"+getName(), lineDir);
		
		//group_noRot.attachChild(lineDir);
	}

	private void initVertexes() {
		Vector3 velocity=getVelocity();
		vertex_vel=new Vector3f[2];
		vertex_vel[0]=new Vector3f(0,0,0);
		vertex_vel[1]=new Vector3f((float)velocity.x,(float)velocity.y,(float)velocity.z);

		vertex_dir=new Vector3f[2];
		vertex_dir[0]=new Vector3f(0,0,0);
		vertex_dir[1]=new Vector3f(-10,10,40);
	}

	public void updatePosition(boolean direct){
		super.updatePosition(direct);
		/*geomVel.removeFromParent();
		geomDir.removeFromParent();
		createLines();*/
		initVertexes();
		lineVel.getEnd().set(vertex_vel[0]);
		
		}
	
		

}
