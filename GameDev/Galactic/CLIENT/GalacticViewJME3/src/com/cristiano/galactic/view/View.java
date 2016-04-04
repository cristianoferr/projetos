package com.cristiano.galactic.view;


import java.awt.Canvas;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import com.cristiano.cyclone.utils.Formatacao;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.GalacticViewStarterJME3;
import com.cristiano.galactic.ViewConsts;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.controller.GameLogic;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.faction.PlayerFaction;
import com.cristiano.galactic.view.hud.ShipHud;
import com.cristiano.galactic.view.models.AbstractItemView;
import com.cristiano.galactic.view.models.ItemView;
import com.cristiano.galactic.view.models.ShipView;
import com.cristiano.gamelib.interfaces.IObjeto;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.ChaseCamera;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;


public class View extends SimpleApplication implements IView {
	public static final boolean drawBox = false;
	private static Logger logger = Logger.getLogger(Galactic.class);

	private GameLogic gameLogic;
	private ItemsViewManagerAbstract itemsView;
    private HashMap<Node,AbstractItemView> nodeHandler=new HashMap<Node,AbstractItemView>();
    Vector3 relative_position=new Vector3(0,0,0);
    //float maxFrustum=100000f;
    
    PlayerFaction playerFaction=null;
    int playerID=0; //Variavel que contém o ID do jogador atualmente logado
    
    private ShipHud shipHud;
    
    //KeyMapping keyMap;
    //KeyMappingJME keyMapJME;
    /** A sky box for our scene. */
	//Skybox sb;
    
    //Necessary for syncronized updates 
    Vector<Node> queueToRemove=new Vector<Node>(); 
    Vector<Node> queueToAdd=new Vector<Node>();
    
    ChaseCamera chaseCam=null;
    
    //private RotateCamera rotateCam;
    
    boolean updated=false;
    
    boolean runGameLogic=true;
    
    private PlanetAppStateWrapper planetAppState;

    
    
    
    
    //Relativo a camera
    CameraNode camNode;
    AbstractItemView old_item=null;
    Vector3 old_cam_rel_pos=null;
    boolean flagUpdateCamera=true;
    protected Canvas            canvas;
    
    
    ShipView player=null;
    AbstractItemView selection=null;
	private IObjeto selecao;

	@Override
	public void simpleInitApp() {
		print("View Started",true);
		java.util.logging.Logger.getLogger("com.jme3").setLevel(java.util.logging.Level.WARNING);
	//	assetManager.registerLocator(recursosPath,UrlLocator.class.getName());
	//	assetManager.registerLocator(Consts.rootPath+Consts.recursosPath, ClasspathLocator.class);
	//	assetManager.registerLocator(Consts.absolutePath+Consts.recursosPath, ClasspathLocator.class);
		assetManager.registerLocator(Consts.rootPath+Consts.recursosPath, FileLocator.class.getName());
		setItemsView(new ItemsViewManager(gameLogic.getItemsController(),this,true));

		
		shipHud=new ShipHud(this);
		
        //camNode.setLocalTranslation(new Vector3f(0, 1250, -20));
        //camNode.update;
        //rootNode.attachChild(camNode);
        //cam.setFrustumFar(Float.MAX_VALUE);
        //cam.setFrustumFar(100000f);
        ///cam.setFrustumNear(1);
       // cam.setFrustumPerspective(45, 4f/3f, 1, 40000);*/
        //cam.setFrustumPerspective(45.0f,(float) display.getWidth() / (float) display.getHeight(), 1, 1000);
        cam.setFrustum(1.0f, ViewConsts.getMaxFrustum(), -0.55f, 0.55f, 0.4125f, -0.4125f);
        //getRenderer().setCamera(cam);
        
        /** Must add a light to make the lit object visible! */
      /*  DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1,0,2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);*/
        
        
        createSkyBox();
       /* Box b;
        b = new Box("Mybox", new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));
        b.setRandomColors();
        b.setLocalTranslation(0, 0, 0);*/
        
		
        
     // new RoteteCamera
//        rotateCam = new RotateCamera(getWidth(), display.getHeight(), camNode,keyMapJME);
        // set target
        //rotateCam.reLoad(b);
 
       // this.input = rotateCam;
        
        
        //lightState.setGlobalAmbient(new ColorRGBA(5f,5f,5f,0));

       /* MaterialState materialState = display.getRenderer().createMaterialState();
        materialState.setAmbient(Jme2Utils.AMBIENT_GRAY_COLOR);
        materialState.setDiffuse(Jme2Utils.DIFFUSE_COLOR);
        materialState.setSpecular(Jme2Utils.NO_COLOR);
        materialState.setShininess(Jme2Utils.NO_SHININESS);
        materialState.setEmissive(Jme2Utils.NO_COLOR);
        materialState.setEnabled(true);*/
        
     //   rootNode.setRenderState(materialState);
        rootNode.updateGeometricState();

        // Add planet app state
        planetAppState = new PlanetAppStateWrapper();
        stateManager.attach(planetAppState);
        

        //Registra os itens
		itemsView.start();
		
        initCamera();

	}

	public void addPlanet(Planet planet){
		planetAppState.addPlanet(planet);
	}

	public void updateCamera(){
		Node objNode=((ItemView)getPlayer()).getRootNode();
		//Spatial objVisual=((ItemView)getPlayer()).getObjVisual();
		
		//System.out.println("cam:"+camNode.getLocalTranslation()+" objNode:"+objNode.getLocalTranslation());
		camNode.setLocalTranslation(new Vector3f(-20, 0, 0));
	    //setting the camNode to look at the teaNode
	    camNode.lookAt(objNode.getLocalTranslation(), Vector3f.UNIT_Y);
	    camNode.setLocalRotation(objNode.getLocalRotation().fromAngleAxis( FastMath.PI/2 , new Vector3f(0,1,0) ));
	}
	
	private void initCamera() {
	/*	flyCam.setEnabled(false);
        Spatial objVisual=((ItemView)getPlayer()).getObjVisual();
        
        chaseCam = new ChaseCamera(cam, objVisual, inputManager);
        chaseCam.setSmoothMotion(true);
        
        */
       //camNode.setControlDir(ControlDirection.SpatialToCamera);
        
     // Disable the default flyby cam
		//Attach the camNode to the target:
        //if (1==1) return;
		Node objNode=((ItemView)getPlayer()).getRootNode();
//		Spatial objVisual=((ItemView)getPlayer()).getObjVisual();
		
		//camNode = new CameraNode("Camera Node", cam);
		camNode = new CameraNode("Camera Node", cam);

		//camNode.removeFromParent(); 
		
		objNode.attachChild(camNode);
        //Move camNode, e.g. behind and above the target:
        //camNode.setLocalTranslation(new Vector3f(0, 5, -5));
        //Rotate the camNode to look at the target:
        //camNode.lookAt(objVisual.getLocalTranslation(), new Vector3f(0,1,0) );
       // camNode.setLocalTranslation(objNode.getLocalTranslation());
        //camNode.setLocalRotation(objVisual.getLocalRotation());//.fromAngleAxis( FastMath.PI/2 , new Vector3f(0,1,0) ));
	    camNode.setLocalTranslation(new Vector3f(-10, 0, 0));
	    //setting the camNode to look at the teaNode
	    //camNode.lookAt(objNode.getLocalTranslation(), Vector3f.UNIT_Y);

        
		
       // System.out.println("LocalRot:"+camNode.getLocalRotation());
        flyCam.setEnabled(false);
        //create the camera Node
        //camNode = new CameraNode("Camera Node", cam);
        //This mode means that camera copies the movements of the target:
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        
	}
	
	
	
	@Override
	public AbstractItemView getPlayer() {
    	if (player==null){
    		PlayerFaction pf=getPlayerFaction();
    		player=(ShipView)getItemsView().getEntityByID(pf.getPlayerShipID());
    		
    		
    		

    	}
    	
    	return player;

	}

	@Override
	public void print(String message, boolean debug) {
		System.out.println("MESSAGE:"+message);

	}

	 
	
	
	
	public void render() {
		
		//initCamera();
		updateCamera();
		//System.out.println("render");
		
		
    	AbstractItemView viewItem=player;
    	shipHud.update(this);
    	if (runGameLogic) gameLogic.update();
    	
    	
    	ViewConsts.setPreviousMaxDistanceObject(ViewConsts.getMaxDistanceObject());
    	ViewConsts.setMaxDistanceObject(-1);

    	
    	Iterator iterator = itemsView.getViewModels().keySet().iterator();
        while (iterator.hasNext()) {
          Item key = (Item) iterator.next();
          AbstractItemView itemView=itemsView.getViewModels().get(key);
          itemView.updatePosition(itemView==viewItem);

        }

	}


	private void syncObjects() {
		for (int i=0;i<queueToRemove.size();i++){
			queueToRemove.get(i).removeFromParent();
		}
		queueToRemove.clear();
		
		
		for (int i=0;i<queueToAdd.size();i++){
			rootNode.attachChild(queueToAdd.get(i));
		}
		queueToAdd.clear();
		
		itemsView.sync();
	}

	@Override
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic=gameLogic;

	}

	
	@Override
	public void removeVisualObject(Item item) {
		System.out.println("View.unregister: "+item+" Trash Can Size:"+queueToRemove.size());
		shipHud.unregisterObjetoJogo(getItemsView().getItemViewFromItem(item));

		//getVisualItem(getItemsView().getViewModel(item)).removeFromParent();
		//rootNode.detachChild(getVisualItem(getItemsView().getViewModel(item)));
		queueToRemove.add(getRootNodeFromItem(getItemsView().getItemViewFromItem(item)));
	}
	
    public Node getRootNodeFromItem(AbstractItemView itemView){
    	return ((ItemView)itemView).getRootNode();/*
		Iterator iterator = nodeHandler.keySet().iterator();
	    while (iterator.hasNext()) {
	    	TransformGroup key = (Node) iterator.next();
	    	ItemViewAbstract parm=nodeHandler.get(key);
	    	if (parm==itemView) return key;
			
		}
    	return null;*/
    }
    


	

	


	

	public void simpleUpdate(float tpf) {
		render();
		super.simpleUpdate(tpf);
		syncObjects();
/*		if ( KeyInput.get().isKeyDown( KeyInput.KEY_O ) ) {
    		ClockWatch.report();
    	}
		if ( KeyInput.get().isKeyDown( KeyInput.KEY_HOME ) ) {
			getPlayer().changeVelocity(0, 0, 0);
	 		getPlayer().changeRotation(0, 0,0);
    	}
		if ( KeyInput.get().isKeyDown( KeyInput.KEY_END ) ) {
	 		getPlayer().changeRotation(0, 0,0);
    	}*/
	}

	@Override
	public ItemsViewManagerAbstract getItemsView() {
		
		return itemsView;
	}

	@Override
	public void setItemsView(ItemsViewManagerAbstract itemsView) {
		this.itemsView=itemsView;
		
	}

	@Override
	public void backLinkVisualItem(Object objSelectable,AbstractItemView itemView,boolean addToScene){
		registerVisualItem((Node)objSelectable,itemView,addToScene);
    }
    
	public Node getRootNode(){
		return rootNode;
	}
    
    public void registerVisualItem(Node objSelectable,AbstractItemView itemView,boolean addToScene){
    	nodeHandler.put(objSelectable,itemView);
    	if (addToScene)
    		queueToAdd.add(objSelectable) ;
    	shipHud.registerObjetoJogo(itemView);    		
    }

    @Override
	public Vector3 getPlayerAbsolutePosition() {
    	if (getPlayer()==null) return Vector3.ZERO;
		return getPlayer().getAbsolutePosition();
	}


	public void setRunGameLogic(boolean b){runGameLogic=b;}
    public boolean isRunningGameLogic(){
    	return runGameLogic;
    }

	@Override
	public void createSkyBox() {
	
        // Add sky
        Node sceneNode = new Node("Scene");
        sceneNode.attachChild(Utility.createSkyBox(this.getAssetManager(), "textures/blue-glow-1024.dds"));
        rootNode.attachChild(sceneNode);
/*
		
		Mesh sphere = new Sphere(10, 10, ViewConsts.getMaxFrustum()*0.7f);
        sphere.setStatic();
        Geometry sky = new Geometry("SkyBox", sphere);
        sky.setQueueBucket(Bucket.Sky);
        sky.setCullHint(Spatial.CullHint.Never);
        sky.setShadowMode(ShadowMode.Off);
        sky.setModelBound(new BoundingSphere(Float.POSITIVE_INFINITY, Vector3f.ZERO));
        
        Image cube = assetManager.loadTexture("Textures/blue-glow-1024.dds").getImage();
        TextureCubeMap cubemap = new TextureCubeMap(cube);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Sky.j3md");
        mat.setBoolean("SphereMap", false);
        mat.setTexture("Texture", cubemap);
        mat.setVector3("NormalScale", Vector3f.UNIT_XYZ);
        sky.setMaterial(mat);
        
        Node sceneNode = new Node("Scene");
        sceneNode.attachChild(sky);
        rootNode.attachChild(sceneNode);*/
	}


	@Override
	public Vector3 getScreenCoordinates(Vector3 pos3d) {
		Vector3f dir=cam.getScreenCoordinates(new Vector3f(pos3d.getXf(),pos3d.getYf(),pos3d.getZf()));
		return new Vector3(dir.x,dir.y,dir.z);
		
	}


	@Override
	public IObjeto getSelection() {
		
		return selecao;
	}


	@Override
	public void notifyPropertyChanged(IObjeto objeto, Enum property) {
		notifyPropertyChanged(objeto,property.toString());
		
	}
	
	@Override
	public void notifyPropertyChanged(IObjeto objeto, String property) {
		// TODO Implementar essa funcao
		
	}



	@Override
	public boolean sendConsoleCommand(String command,String[] pars) {
		if (command.equals("whoami")){
			print("You are: '"+getPlayer().getName()+"'", false);
			print("You are at: "+Formatacao.format(getPlayer().getCoord()), false);
			print("Your speed is: "+Formatacao.format(getPlayer().getVelocity())+" ("+Formatacao.format(getPlayer().getVelocity().magnitude())+")", false);
			print("Your rotation is: "+Formatacao.format(getPlayer().getRotation()), false);
			print("",false);
			return true;
		}
		if (command.equals("moveto")){
			String p=pars[0];
			try {
				p=p + " " + pars[1];
			} catch (Exception e) {
			}
			//if (pars.()>1) p=p+" "+pars[1];
			return itemsView.sendConsoleCommand(command,new String[]{Integer.toString(getPlayerFaction().getCurrentPlayerShip().getId()),p});	
		}
		if (command.equals("move")){
			String p=getSelection().getName();
			
			//if (pars.()>1) p=p+" "+pars[1];
			return itemsView.sendConsoleCommand("moveto",new String[]{Integer.toString(getPlayerFaction().getCurrentPlayerShip().getId()),p});	
		}
		return itemsView.sendConsoleCommand(command,pars);
	}


	@Override
	public void setSelection(IObjeto objeto) {
		this.selecao=objeto;
		shipHud.updateSelection(objeto);
		itemsView.setSelection(objeto);
		
	}

	public static void log(String msg){
		logger.info(msg);
	}
	
	public static void logDebug(String msg){
		logger.debug("(view.logDebug):"+msg);
	}



	@Override
	public PlayerFaction getPlayerFaction() {
	if (playerFaction==null){
		playerFaction=itemsView.getPlayerFactionID(playerID);
	}
		return playerFaction;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public void destroy(){
		super.destroy();
		GalacticViewStarterJME3.running=false;
	}

}
