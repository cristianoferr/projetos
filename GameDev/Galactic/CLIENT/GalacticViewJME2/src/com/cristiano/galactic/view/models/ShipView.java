package com.cristiano.galactic.view.models;


import java.util.ArrayList;

import org.eclipse.swt.widgets.Item;

import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.view.View;
import com.cristiano.galactic.view.models.AbstractItemView;
import com.cristiano.galactic.view.models.IShipView;
import com.cristiano.math.Vector3;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Line;
import com.jme.scene.shape.Box;



public class ShipView extends ItemView implements IShipView {
	private AbstractItemView target=null;
	private Line lineVel=null;
	private Line lineDir=null;
	

	
	private Vector3f[] vertex_vel,vertex_dir;

	public ShipView( View view) {
		super(view);
	}
	
	public AbstractItemView getTarget() {
		
		return target;
	}

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
		//renderHull();
	   // debugInternalPoints();
	}

	/*
	public void debugInternalPoints() {
		GeomOBJ geom=(GeomOBJ) debugItem.getGeom();
	    ArrayList<InternalPoint> points =geom.getPoints();
	    //objVisual=new Node();
	    Vector3 ipSize=geom.halfSize.getMultiVector(1.0/geom.getnPontos()*2);
		for (int i=0;i<points.size();i++){
			InternalPoint ip=points.get(i);
			Vector3 pos=ip.getPosicao();
			
			//Sphere sph=createSphere(null,10);
			Box sph=createBox("",ipSize);
			sph.setRandomColors();
			sph.setLocalTranslation((float)(pos.getX()-ipSize.x/2),(float)(pos.getY()-ipSize.y/2),(float)(pos.getZ()-ipSize.z/2));
			debugNode.attachChild(sph);
			
			//view.backLinkVisualItem(rootNode,this,true);
		}
		//objVisual.setModelBound(new BoundingSphere());
		//objVisual.updateModelBound();
		//rootNode.attachChild(objVisual);
		//objVisual.setLocalTranslation(0,0,0);
	}*/



	private void createLines() {
		vertex_vel=new Vector3f[2];
		vertex_vel[0]=new Vector3f(0,0,0);
		vertex_vel[1]=new Vector3f((float)getVelocity().x,(float)getVelocity().y,(float)getVelocity().z);
		ColorRGBA[] colors=new ColorRGBA[2];
		colors[0]=ColorRGBA.green;
		colors[1]=ColorRGBA.green;
		
		vertex_dir=new Vector3f[2];
		vertex_dir[0]=new Vector3f(0,0,0);
		vertex_dir[1]=new Vector3f(-10,10,40);
		ColorRGBA[] colors2=new ColorRGBA[2];
		colors2[0]=ColorRGBA.red;
		colors2[1]=ColorRGBA.red;
		
		
		lineVel=new Line("LineVel",vertex_vel,null,colors,null);
		lineVel.setLineWidth(5);
		lineVel.setCastsShadows(false);
		lineVel.setDefaultColor(ColorRGBA.green);
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
		
		rootNode.attachChild(lineVel);
		
	//	group_noRot.setLocalTranslation(relCoord.getXf(),relCoord.getYf(),relCoord.getZf());
		lineDir=new Line("lineDir",vertex_dir,null,colors2,null);
		lineDir.setLineWidth(5);
		//group_noRot.attachChild(lineDir);
	}

	public void updatePosition(boolean direct){
		super.updatePosition(direct);
		lineVel.removeFromParent();
		lineDir.removeFromParent();
		createLines();
		
		}


		

}
