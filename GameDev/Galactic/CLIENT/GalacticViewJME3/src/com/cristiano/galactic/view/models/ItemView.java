package com.cristiano.galactic.view.models;


import javax.swing.Box;

import com.cristiano.cyclone.utils.Quaternion;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.ViewConsts;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.view.IView;
import com.cristiano.galactic.view.View;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

/**
 * Classe base para objetos visuais na camada view
 * @author Cristiano
 *
 */
public class ItemView extends AbstractItemView {
	
	Spatial objVisual=null;
	Node rootNode=null;
	Vector3f coordf=new Vector3f();//objeto para acesso rápido a coordenada

	
	public ItemView(IView view){
		super(view);
		rootNode=new Node();
	}
	
	public void setCoord(double x, double y, double z) {
		coordf.x=(float) x;
		coordf.y=(float) y;
		coordf.z=(float) z;
		super.setCoord(x, y, z);
	}
	public void setCoord(Vector3 coord) {
		coordf.x=coord.getXf();
		coordf.y=coord.getYf();
		coordf.z=coord.getZf();
		super.setCoord(coord);
	}


	
	private Geometry createSphere(String texture,double radius){
		int x=10;
		if (radius>Math.pow(10,5)) x=30;
		
		Sphere sph=new Sphere( x,x,(float)radius );
		Geometry geom = new Geometry("Geometria Esfera"+getName(), sph);
		if (getTexture()!=null){
				String s=Consts.rootPath+Consts.recursosPath+"/textures/"+texture;
				s=s.replace("\\", "/");
				//s=s.replace("file:/", "");
				
			    Material mat_tl = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			    mat_tl.setTexture("ColorMap", getAssetManager().loadTexture(s));
			    //mat_tl.setColor("Color", new ColorRGBA(1f,0f,1f, 1f)); // purple
			    geom.setMaterial(mat_tl);
			    rootNode.attachChild(geom);
			} else {
				Material mat = new Material(getAssetManager(),
		        "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
		        mat.setColor("Color", ColorRGBA.randomColor());   // set color of material to blue
		        geom.setMaterial(mat);                   // set the cube's material
			}
		
	
		//sph.settex
		return geom;
	}
	
	private Geometry createBox(String texture,Vector3 halfSize){
		int x=1;
		Box box = new Box(Vector3f.ZERO,(float)halfSize.x, (float)halfSize.y, (float)halfSize.z);
		
		//box.setLocalTranslation(0, 0, 0);
		Geometry geom = new Geometry("Box", box);
		Material mat = new Material(getAssetManager(),
        "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.randomColor());   // set color of material to blue
        geom.setMaterial(mat);                   // set the cube's material
        
		return geom;
	}
	
	
	
	public AssetManager getAssetManager(){
		return ((View)getView()).getAssetManager();
	}

	@Override
	public void loadModel(ModelData md) {
		Galactic.printLog("Loading "+md.getPath()+" "+md.getScale() , true);
		objVisual = getAssetManager().loadModel(md.getPath());
		objVisual.setLocalScale(md.getScale());
		rootNode.attachChild(objVisual);
		
		
		// show normals as material
        //Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md");
		// Material planetMaterial = new Material(getAssetManager(), "MatDefs/Planet/Terrain.j3md");
        //objVisual.setMaterial(planetMaterial);
        
        
		objVisual.setLocalTranslation(0,0,0);
		//rootNode.setLocalTranslation(10000,0,0);
		getView().backLinkVisualItem(rootNode,this,true);
		
		if ((View.drawBox))
				//&& (!getName().equals("Nave do Jogador")))
		{
			int x=1;
			Box box = new Box(new Vector3f(-(float)md.getTamX()/x,-(float)md.getTamY()/x,-(float)md.getTamZ()/x), (float)md.getTamX()/x,(float)md.getTamY()/x,(float)md.getTamZ()/x);
			//box.setLocalTranslation(0, 0, 0);
			Geometry geom = new Geometry("Box", box);
	        Material mat2 = new Material(getAssetManager(),
	          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
	        mat2.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
	        geom.setMaterial(mat2);                   // set the cube's material

			rootNode.attachChild(geom); 
		}
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
			 		//double prev_max_drawing_distance=ViewConsts.getPreviousMaxDistanceObject();
			 		
			 		//scale=scale+ViewConsts.getMaxDistanceObject()/(prev_max_drawing_distance-ViewConsts.clippingDist);
			 			
			 		//System.out.println(getName()+" prev_max_drawing_distance:"+Formatacao.formatDistance(prev_max_drawing_distance)+" scale:"+scale+" distancia:"+Formatacao.formatDistance(distancia) );
			 	}
		 	}
		 	
		 if (getName().equals("Lua")){
			// scale=1;
		 }
		 	
		 //System.out.println(getName()+":"+coordf+" scale:"+scale);
		 rootNode.setLocalScale((float)scale);
		 rootNode.setLocalTranslation(coord.getXf(scale), coord.getYf(scale),coord.getZf(scale));
		 
		//if (getName().equals("Lua")){
	 		//System.out.println(getName()+" prev_max_drawing_distance:"+Formatacao.formatDistance(prev_max_drawing_distance)+" scale:"+scale+" distancia:"+Formatacao.formatDistance(distancia) );
	 		//System.out.println(	"LocalTrans:"+rootNode.getLocalTranslation()+" scale:"+scale);
	 	//}

		 
		 //Atualiza orientação
		 com.cristiano.math.Quaternion orientation=getOrientation();
		 Quaternion o=rootNode.getLocalRotation();
		 o.set((float)orientation.i,(float)orientation.j,(float)orientation.k,(float)orientation.r);
		 rootNode.setLocalRotation(o);

		 //System.out.println(getName()+" "+coord);
		 /*
		 Vector3 relCoord=getCoord();
		 com.cristiano.math.Quaternion orientation=getOrientation();
		 rootNode.setLocalTranslation(relCoord.getXf(), relCoord.getYf(),relCoord.getZf());
		 
		 Quaternion o=objVisual.getLocalRotation();
		 o.set((float)orientation.i,(float)orientation.j,(float)orientation.k,(float)orientation.r);
		 objVisual.setLocalRotation(o);
//		 rootNode.updateRenderState();*/
	    }


	 public void setOrientation(com.cristiano.math.Quaternion orientation) {
		 super.setOrientation(orientation);
		 if (rootNode==null) return;
		 Quaternion o=rootNode.getLocalRotation();
		 o.set((float)orientation.i,(float)orientation.j,(float)orientation.k,(float)orientation.r);
	 }
	 
/**
 * @return the objVisual
 */
public Spatial getObjVisual() {
	return objVisual;
}

public Node getRootNode() {
	return rootNode;
}

@Override
public void attachBox(Vector3 halfSize) {
	objVisual=createBox(getTexture(),halfSize);
	rootNode.attachChild(objVisual);
	objVisual.setLocalTranslation(0,0,0);
	getView().backLinkVisualItem(rootNode,this,true);
	
}

@Override
public void attachSphere(double radius) {
	objVisual=createSphere(getTexture(),radius);
	rootNode.attachChild(objVisual);
	objVisual.setLocalTranslation(0,0,0);
	getView().backLinkVisualItem(rootNode,this,true);
}

public Vector3f getCoordf() {
	return coordf;
}



}
