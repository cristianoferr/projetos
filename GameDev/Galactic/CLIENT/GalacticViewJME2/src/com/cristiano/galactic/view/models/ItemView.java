package com.cristiano.galactic.view.models;

import java.awt.Color;

import jme2.utils.Jme2Utils;

import com.cristiano.comum.Formatacao;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.ViewConsts;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.view.IView;
import com.cristiano.galactic.view.View;
import com.cristiano.galactic.view.models.AbstractItemView;
import com.cristiano.math.Vector3;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;


public class ItemView extends AbstractItemView {
	
	Node objVisual=null;
	Node rootNode=null;
	Node debugNode=null;
	Node infoNode=null;

	
	public ItemView(IView view){
		super(view);
		rootNode=new Node();
	}
	
	protected Sphere createSphere(String texture,double radius){
		int x=10;
		if (radius>Math.pow(10,5)) {
			x=30;
		}
		this.radius=radius;
		//texture="";
		Sphere sph=new Sphere( getName()+".sph",x,x,(float)radius );
		if (corAtmos!=null){
			sph.setSolidColor(new ColorRGBA(corAtmos.getRed(),corAtmos.getGreen(),corAtmos.getBlue(),corAtmos.getAlpha()));
		}
//		sph.setSolidColor(ColorRGBA.randomColor());
		if (texture!=null)
			if (!texture.equals("")){
				String s=View.recursosPath+"textures/"+texture;
				s=s.replace("\\", "/");
				s=s.replace("file:/", "");
				Texture text = TextureManager.loadTexture(
						s, Texture.MinificationFilter.Trilinear,
						Texture.MagnificationFilter.Bilinear);
				//PerlinNoise pn=new PerlinNoise(128,128);
				//text.setImage(pn);
				TextureState ts = ((View)getView()).getDisplay().getRenderer().createTextureState();
				ts.setEnabled(true);
				ts.setTexture(text);
				sph.setRenderState(ts);
			}
		sph.setModelBound(new BoundingSphere());
		sph.updateModelBound();
	
		//sph.settex
		return sph;
	}
	
	protected Box createBox(String texture,Vector3 halfSize){
		Box sph=new Box(getName()+".box", new Vector3f(0, 0, 0), new Vector3f((float)halfSize.x, (float)halfSize.y, (float)halfSize.z));
		sph.setModelBound(new BoundingBox());
		sph.updateModelBound();
		return sph;
	}
	
	public void clearDebugNode(){
		if (debugNode==null){
			return;
		}
		debugNode.detachAllChildren();	
		debugNode.removeFromParent();
		debugNode=new Node();
		objVisual.attachChild(debugNode);
	}
	

	
	@Override
	public void attachSphere(double radius){
		objVisual=new Node();
		objVisual.attachChild(createSphere(getTexture(),radius));
		rootNode.attachChild(objVisual);
		debugNode=new Node();
		objVisual.attachChild(debugNode);
		objVisual.setLocalTranslation(0,0,0);
		getView().backLinkVisualItem(rootNode,this,true);
	}
	
	@Override
	public void attachBox(Vector3 halfSize){
		objVisual=new Node();
		objVisual.attachChild(createBox(getTexture(),halfSize));
		rootNode.attachChild(objVisual);
		debugNode=new Node();
		objVisual.attachChild(debugNode);
		objVisual.setLocalTranslation(0,0,0);
		getView().backLinkVisualItem(rootNode,this,true);
	}

	private void initInfoNode() {
		infoNode=new Node();
		rootNode.attachChild(infoNode);
		Text t=new Text("label",getName());
		t.setLocalTranslation(0,0,0);
		infoNode.setLocalTranslation(0,0,0);
		t.setTextColor(ColorRGBA.green); 
		infoNode.attachChild(t);
	}
	

	@Override
	public void loadModel(ModelData md) {
		Galactic.printLog("Loading "+md.getPath()+" "+md.getScale() , true);
		objVisual=Jme2Utils.loadOBJ(View.recursosPath+md.getPath(),(float)md.getScale());
		rootNode.attachChild(objVisual);
		debugNode=new Node();
		//debugNode.setLocalScale((float)md.getScale());
		objVisual.attachChild(debugNode);
		
		
		//objVisual.setLocalTranslation(0,0,0);
		rootNode.setLocalTranslation(10000,0,0);
		getView().backLinkVisualItem(rootNode,this,true);
		
		if ((ViewConsts.drawBox))
				//&& (!getName().equals("Nave do Jogador")))
		{
			createBox(md);
		}
	//	initInfoNode();
	}

	private void createBox(ModelData md) {
		int x=1;
		Box box = new Box("Mybox", new Vector3f(-(float)md.getTamX()/x,-(float)md.getTamY()/x,-(float)md.getTamZ()/x), new Vector3f((float)md.getTamX()/x,(float)md.getTamY()/x,(float)md.getTamZ()/x));
		box.setRandomColors();
		//box.setLocalTranslation(0, 0, 0);
		objVisual.attachChild(box);
	}

	@Override
	public void updatePosition(boolean direct) {
		
		//if ((direct) || (relCoord.getX()!=coord.getX()) || (relCoord.getY()!=coord.getY()) ||(relCoord.getZ()!=coord.getZ()))
		update();
		doMove();
		
	}

	
	 public void doMove() 
	    {        
		 //System.out.println("doMove:"+name+" "+orientation);
	    /*	Transform3D transform3D = new Transform3D();
	  		group.getTransform(transform3D);
	  		Transform3D toMove = new Transform3D();
	  		toMove.setTranslation(theMove);
	  		transform3D.mul(toMove);
	  		transform3D.setRotation(node.getOrientation().getQuaternion4f());
	  		group.setTransform(transform3D);*/
	  		
	    	//Transform3D transform = new Transform3D();
	//System.out.println("name:"+getName()+" relCoord:"+relCoord);
		 double scale=1;
		 
		 double relSize=getRelativeScreenSize();
		 double minScale=ViewConsts.getMaxDistanceObject();
		 Vector3 coord=getCoord();
		 if ((relSize*Consts.UNIT2PIXEL>1) && ((minScale<coord.magnitude() ) || (minScale==-1 ))){
			 ViewConsts.setMaxDistanceObject(coord.magnitude());
		 }
		 	if (ViewConsts.useScale){
			 	double distancia=coord.magnitude();
			 	if (distancia>ViewConsts.clippingDist){
			 		scale=ViewConsts.clippingDist/distancia;
			 		double prev_max_drawing_distance=ViewConsts.getPreviousMaxDistanceObject();
			 		
			 		//scale=scale+ViewConsts.getMaxDistanceObject()/(prev_max_drawing_distance-ViewConsts.clippingDist);
			 		scale=Math.abs(scale);
			 		//if (getName().equals("asiall")){ 
			 		//System.out.println(getName()+" "+relSize+" prev_max_drawing_distance:"+Formatacao.formatDistance(prev_max_drawing_distance)+" scale:"+scale);
//			 			scale=1;
			 		//}
			 		
			 	}
		 	}

	 		rootNode.setLocalScale((float)scale);
	 		rootNode.setLocalTranslation(coord.getXf(scale), coord.getYf(scale),coord.getZf(scale));
		 
		 
		 //System.out.println(getName()+" "+coord); 
		// infoNode.setLocalScale(1000);
		 rootNode.updateRenderState();
		 
		 
	    }

	 public void setOrientation(com.cristiano.math.Quaternion orientation) {
		 super.setOrientation(orientation);
		 if (objVisual==null) return;
		 Quaternion o=objVisual.getLocalRotation();
		 o.w=(float)orientation.r;
		 o.x=(float)orientation.i;
		 o.y=(float)orientation.j;
		 o.z=(float)orientation.k;
	 }


/**
 * @return the objVisual
 */
public Node getObjVisual() {
	return objVisual;
}

public Node getRootNode() {
	return rootNode;
}

public Node getDebugNode() {
	return debugNode;
}

public void setDebugNode(Node debugNode) {
	this.debugNode = debugNode;
}











}
