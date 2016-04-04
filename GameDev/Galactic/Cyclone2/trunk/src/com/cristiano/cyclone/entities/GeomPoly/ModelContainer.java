package com.cristiano.cyclone.entities.GeomPoly;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.cristiano.cyclone.Cyclone;


public class ModelContainer {
/*	private static HashMap<String, Texture> textures;
	private static HashMap<String, ObjModel> models;*/
	private Map<String, GeomOBJ> objDatas;
	//static TextureLoad loader;
	
	public ModelContainer(){
	//	loader = new TextureLoad();
	//	models = new HashMap<String, ObjModel>();
	//	textures = new HashMap<String, Texture>();
		objDatas = new HashMap<String, GeomOBJ>();
}
	
	/*public static void loadTexture(String name,String fileName) throws IOException{
		Texture texture=loader.getTexture(fileName);
		addTexture(name,texture);
	}
	public static void loadModel(String name,String fileName,float scale) throws IOException{
		addModel(name,ObjLoader.loadObj(fileName,scale));
	}
	
	public static  void addTexture(String name,Texture texture) {
		if (texture!=null){
			textures.put(name, texture);
		} 
	}	*/
	
	public GeomOBJ loadGLModel(String name,String fileName, float scale,double mass){
		if (getObjData(name)!=null) return getObjData(name);
		try {
		//GLModel object = new GLModel(Consts.workdir+fileName,scale, cwidth,cthikness, clength);
			if (objDatas.get(name)!=null) {return objDatas.get(name);}
			
			//InputStream in = ModelContainer.class.getClassLoader().getResourceAsStream(fileName);
			File f1 = new File (fileName);
			try {
			      // System.out.println ("Current directory : " + f1.getCanonicalPath());
			       fileName="file:/"+f1.getCanonicalPath().replace("\\", "/");
			       } 
			     catch(Exception e) {
			    	 Cyclone.log(e.getMessage());
			       
			       }
			GeomOBJ objData=new GeomOBJ(fileName,scale,mass);
		/*System.out.println("Loading ObjData: "+fileName);
			if (in == null) {
				throw new IOException("Unable to find: "+fileName);
			}
			
		objData=new GeomOBJ(in);
		*/
       // object.regenerateNormals();
        objDatas.put(name, objData);
       // glModels.put(name, object);
        return objData;
		}
		catch (Exception e){
			Cyclone.log(e.getMessage());
		}
		return null;
	}
	
	public GeomOBJ getObjData(String id){
		if (id==null) return null;
		return objDatas.get(id);
	}

	
	
	
	
}
