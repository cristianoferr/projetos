package com.cristiano.galactic.view;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


import org.apache.log4j.Logger;

import com.cristiano.comum.Formatacao;
import com.cristiano.dados.GenericDTO;
import com.cristiano.galactic.Galactic;
import com.cristiano.galactic.ViewConsts;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.controller.GameLogic;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.faction.PlayerFaction;
import com.cristiano.galactic.view.IView;
import com.cristiano.galactic.view.ItemsViewManagerAbstract;
import com.cristiano.galactic.view.hud.ShipHud;
import com.cristiano.galactic.view.models.AbstractItemView;
import com.cristiano.galactic.view.models.ItemView;
import com.cristiano.galactic.view.models.PlanetView;
import com.cristiano.galactic.view.models.ShipView;
import com.cristiano.gamelib.interfaces.IObjeto;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;
import com.cristiano.math.Vector3;
import com.cristiano.performance.ClockWatch;
import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.CameraNode;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;


public class View extends SimpleGame implements IView {
	private static Logger logger = Logger.getLogger(Galactic.class);

	public static String recursosPath;
	private GameLogic gameLogic;
	private ItemsViewManagerAbstract itemsView;
    private HashMap<Node,AbstractItemView> nodeHandler=new HashMap<Node,AbstractItemView>();
    private Vector3 relative_position=new Vector3(0,0,0); //Indica a posição relativa do jogador em relação ao universo (usado para simplificar a precisão para a placa de vídeo)
    
    /** A sky box for our scene. */
    private Skybox sb;
	
    private ShipHud shipHud;
	
    PlayerFaction playerFaction=null;
    int playerID=0; //Variavel que contém o ID do jogador atualmente logado
    
    private Vector<PlanetView> updateProceduralLOD=new Vector<PlanetView>();
	
	
    private Vector<Line> debug_lines=new Vector<Line>();
	
    //Necessary for syncronized updates 
    private Vector<Node> queueToRemove=new Vector<Node>(); 
    private Vector<Node> queueToAdd=new Vector<Node>();
    
    
    private boolean runGameLogic=true;
    
    //Relativo a camera
    private CameraNode camNode;
    private AbstractItemView old_item=null;
    private Vector3 posRelativaCamera=null;
    private boolean flagUpdateCamera=true;
    
    
    private ShipView player=null;

	private IObjeto selecao;
	
	public View(String recursosPath) {
		View.recursosPath=recursosPath+"/";
	}

	public void registerProceduralLOD(PlanetView pp){
		updateProceduralLOD.add(pp);
	}
	public void updateLOD(){
		for (int i=0;i<updateProceduralLOD.size();i++){
			updateProceduralLOD.get(i).applyError(relative_position);
		}
	}
	
	public DisplaySystem getDisplay(){
		return display;
	}
	
	@Override
	protected void simpleInitGame() {
		print("View Started",true);
		setItemsView(new ItemsViewManager(gameLogic.getItemsController(),this,true));
        shipHud=new ShipHud(this);

        
        
		setCamNode(new CameraNode("Camera Node", cam));
        //camNode.setLocalTranslation(new Vector3f(0, 1250, -20));
        getCamNode().updateWorldData(0);
        rootNode.attachChild(getCamNode());
        //cam.setFrustumFar(Float.MAX_VALUE);
        //cam.setFrustumFar(100000f);
        ///cam.setFrustumNear(1);
       // cam.setFrustumPerspective(45, 4f/3f, 1, 40000);*/
        //cam.setFrustumPerspective(45.0f,(float) display.getWidth() / (float) display.getHeight(), 1, 1000);
        cam.setFrustum(1.0f, ViewConsts.getMaxFrustum(), -0.55f, 0.55f, 0.4125f, -0.4125f);
        display.getRenderer().setCamera(cam);
        MouseInput.get().setCursorVisible(true);
        createSkyBox();
       /* Box b;
        b = new Box("Mybox", new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));
        b.setRandomColors();
        b.setLocalTranslation(0, 0, 0);*/
        
        
     // new RoteteCamera
        
        // set target
        //rotateCam.reLoad(b);
 
        
        //lightState.setGlobalAmbient(ColorRGBA.red);
        lightState.get(0).setEnabled(false);//Desabilitando a luz ambiente
        
        

        /*MaterialState materialState = display.getRenderer().createMaterialState();
        materialState.setAmbient(Jme2Utils.AMBIENT_GRAY_COLOR);
        materialState.setDiffuse(Jme2Utils.DIFFUSE_COLOR);
        materialState.setSpecular(Jme2Utils.NO_COLOR);
        materialState.setShininess(Jme2Utils.NO_SHININESS);
        materialState.setEmissive(Jme2Utils.NO_COLOR);
        materialState.setEnabled(true);
        rootNode.setRenderState(materialState);*/
        
        rootNode.updateRenderState();
        
		itemsView.start();
        updateCamera();

	}

	
	
	@Override
	public AbstractItemView getPlayer() {
    	if (player==null){
    		//TODO: Esse método não é muito MVC...
    		player=(ShipView)getItemsView().getEntityByID(getPlayerFaction().getCurrentPlayerShip().getId());
    	}
    	
    	return player;

	}

	@Override
	public void print(String message, boolean debug) {
		log("MESSAGE:"+message);
		if (shipHud!=null){
			shipHud.printConsole(message);
		}

	}

	public static void log(String msg){
		logger.info(msg);
	}
	
	public static void logDebug(String msg){
		logger.debug("(logDebug):"+msg);
	}
	 
	protected void simpleUpdate(){
		input.update(tpf);
	}
	
	
	
	private void render() {
		//tmp_createspheresBVH();
		
		ClockWatch.startClock("View.render");
		
		/*if (Math.random()<0.5){
		ClockWatch.startClock("View.render.LOD");
		updateLOD();
		ClockWatch.stopClock("View.render.LOD");}*/
		
		ClockWatch.startClock("View.render.sync");
		syncObjects();
		ClockWatch.stopClock("View.render.sync");
		
    	AbstractItemView viewItem=player;
    	 
    	if (runGameLogic) {gameLogic.update();}
    	
    	ClockWatch.startClock("View.render.iterator");
    	Iterator iterator = itemsView.getViewModels().keySet().iterator();
    	
    	ViewConsts.setPreviousMaxDistanceObject(ViewConsts.getMaxDistanceObject());
    	ViewConsts.setMaxDistanceObject(-1);

    	
        while (iterator.hasNext()) {
          Item key = (Item) iterator.next();
          AbstractItemView itemView=itemsView.getViewModels().get(key);
          itemView.updatePosition(itemView.equals(viewItem));

        }
        //shipHud.reLoad(((ItemView)getPlayer()).getObjVisual());
        updateCamera();
        
        
        ClockWatch.stopClock("View.render.iterator");
        ClockWatch.stopClock("View.render");
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
		
		shipHud.unregisterObjetoJogo(getItemsView().getItemViewFromItem(item));
		AbstractItemView itemView=getItemsView().getItemViewFromItem(item);
		if (itemView==null) {
			return;
		}
		Node node=getRootNodeFromItem(itemView);
		queueToRemove.add(node);
		
	}
	
    public Node getRootNodeFromItem(AbstractItemView itemView){
    	return ((ItemView)itemView).getRootNode();
	}
    

	@Override
	public void updateCamera() {
		if (!flagUpdateCamera) {return;}
		AbstractItemView item=getPlayer();
    	Node objVisual=((ItemView)item).getObjVisual();
    	
    	
		//Verifico se houve mudança entre os objetos
		if (!item.equals(old_item)){
			Vector3 position;
	    	Vector3f offset;

    		old_item=item;
    		//Pego a posição da camera do objeto:
    		posRelativaCamera=item.getCameraPosition();
    		position=item.getPointInWorldSpace(posRelativaCamera);
    		if (posRelativaCamera==null){
    			posRelativaCamera=new Vector3(0,0,0);
    			}
    		offset = new Vector3f( (float)(position.getXf()), position.getYf(), position.getZf() );
    		getCamNode().getLocalTranslation().set(offset);
    		getCamNode().removeFromParent();
    		objVisual.attachChild(getCamNode());
    		getCamNode().getLocalRotation().set( objVisual.getLocalRotation().fromAngleAxis( FastMath.PI/2 , new Vector3f(0,1,0) ) );
    		
    		shipHud.reLoad(objVisual,camNode);

    	}
		
		
		
		if (shipHud.isLockedCamera()){
			shipHud.setLockedCamera(false);
			Quaternion rot=objVisual.getLocalRotation();
			Quaternion newRot=rot.fromAngleAxis( FastMath.PI/2 , Vector3f.UNIT_Y);
			//System.out.println("Rot:"+rot+" newRot:"+newRot+" cam:"+getCamNode().getLocalRotation());
			getCamNode().getLocalRotation().set( objVisual.getLocalRotation().fromAngleAxis( FastMath.PI/2 , new Vector3f(0,1,0) ) );
		//	getCamNode().getLocalRotation().w=newRot.w;
		//	getCamNode().getLocalRotation().x=newRot.x;
		//	getCamNode().getLocalRotation().y=newRot.y;
		//	getCamNode().getLocalRotation().z=newRot.z;
		}
	//
		//rotateCamera(1, tpf, cam.getLeft());
		

	}
	

	

	
	protected void simpleRender() {
		//Runtime.getRuntime().gc();

		render();
		MouseInput.get().setCursorVisible(true);
		shipHud.update(this);
		if (shipHud.getHudController().isConsoleVisible()){
			return;
		}
		
		
		if ( KeyInput.get().isKeyDown( KeyInput.KEY_O ) ) {
    		ClockWatch.report();
    	}
		if ( KeyInput.get().isKeyDown( KeyInput.KEY_HOME ) ) {
		//	getPlayer().changeVelocity(0, 0, 0);
	 	//	getPlayer().changeRotation(0, 0,0);
    	}
		if ( KeyInput.get().isKeyDown( KeyInput.KEY_END ) ) {
	 	//	getPlayer().changeRotation(0, 0,0);
	 	//	Cyclone.bvnode.report("-");
    	}
		
		if ( KeyInput.get().isKeyDown( KeyInput.KEY_V) ) {
			ViewConsts.useScale=!ViewConsts.useScale;
			System.out.println("useScale:"+ViewConsts.useScale);
		}
		
		debug_render();
	}
	
	private void debug_render(){
		for (int i=0;i<debug_lines.size();i++){
			Line l=debug_lines.get(i);
			l.removeFromParent();
		}
		debug_lines.clear();
		
		for (int i=0;i<Galactic.debug_lines.size();i++){
			GenericDTO dto=Galactic.debug_lines.get(i);
			Vector3f[] pontos = new Vector3f[2];
			int pcor=dto.getInt(2);
			ColorRGBA cor=ColorRGBA.green;;
			if (pcor==0){cor =ColorRGBA.green;}
			if (pcor==1){cor =ColorRGBA.red;}
			if (pcor==2){cor =ColorRGBA.blue;}
			if (pcor==3){cor =ColorRGBA.orange;}
			Vector3 p0=dto.getVector(0).getSubVector(getPlayerAbsolutePosition());
			Vector3 p1=dto.getVector(1).getSubVector(getPlayerAbsolutePosition());
			pontos[0]=new Vector3f(p0.getXf(),p0.getYf(),p0.getZf());
			pontos[1]=new Vector3f(p1.getXf(),p1.getYf(),p1.getZf());
			
			ColorRGBA[] colors=new ColorRGBA[2];
			colors[0]=cor;
			colors[1]=cor;
			
			Line l=new Line("LineVel",pontos,null,colors,null);
			l.setLineWidth(5);
			l.setCastsShadows(false);
			l.setDefaultColor(cor);
			rootNode.attachChild(l);
			
			debug_lines.add(l);
		}
		
		Galactic.debug_lines.clear();
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
    	if (addToScene){
    		queueToAdd.add(objSelectable) ;
    	}
    	shipHud.registerObjetoJogo(itemView);
    		
    }

	@Override
	public Vector3 getPlayerAbsolutePosition() {
		if (getPlayer()==null) return Vector3.RIGHT;
		return getPlayer().getAbsolutePosition();
	}

	public void setRunGameLogic(boolean b){runGameLogic=b;}
    public boolean isRunningGameLogic(){
    	return runGameLogic;
    }

	@Override
	public void createSkyBox() {
		// 
		sb = new Skybox("skybox", ViewConsts.getMaxFrustum()*0.8f, ViewConsts.getMaxFrustum()*0.8f, ViewConsts.getMaxFrustum()*0.8f);
		
		//model = new URL(View.recursosPath+objModel);
		String s=View.recursosPath+"textures/nebula_bluedistance_stars_blue_diff.jpg";
		s=s.replace("\\", "/");
		s=s.replace("file:/", "");
		sb.setTexture(Skybox.Face.North, TextureManager.loadTexture(
				s, Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.West, TextureManager.loadTexture(s,
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.South, TextureManager.loadTexture(
				s, Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.East, TextureManager.loadTexture(s,
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.Up, TextureManager.loadTexture(s,
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.Down, TextureManager.loadTexture(
				s, Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.preloadTextures();
 
		CullState cullState = display.getRenderer().createCullState();
		cullState.setCullFace(CullState.Face.None);
		cullState.setEnabled(true);
		sb.setRenderState(cullState);
 
		sb.updateRenderState();
	
		// Attach the skybox to our root node, and force the rootnode to show
		// so that the skybox will always show
		rootNode.attachChild(sb);		
	}

	public boolean sendConsoleCommand(String cmd, String[] pars) {
		if (cmd.equals("whoami")){
			print("You are: '"+getPlayer().getName()+"'", false);
			print("You are at: "+Formatacao.format(getPlayer().getCoord()), false);
			print("Your speed is: "+Formatacao.format(getPlayer().getVelocity())+" ("+Formatacao.format(getPlayer().getVelocity().magnitude())+")", false);
			print("Your rotation is: "+Formatacao.format(getPlayer().getRotation()), false);
			print("",false);
			return true;
		}
		
		if (cmd.equals("moveto")){
			String p=pars[0];
			try {
				p=p + " " + pars[1];
			} catch (Exception e) {
			}
			//if (pars.()>1) p=p+" "+pars[1];
			return itemsView.sendConsoleCommand(cmd,new String[]{Integer.toString(getPlayerFaction().getCurrentPlayerShip().getId()),p});	
		}
		if (cmd.equals("move")){
			String p=getSelection().getName();
			
			//if (pars.()>1) p=p+" "+pars[1];
			return itemsView.sendConsoleCommand("moveto",new String[]{Integer.toString(getPlayerFaction().getCurrentPlayerShip().getId()),p});	
		}
		return itemsView.sendConsoleCommand(cmd,pars);
		
	}

	public void setInput(InputHandler rotateCam) {
		this.input=rotateCam;
		
	}

	public void setCamNode(CameraNode camNode) {
		this.camNode = camNode;
	}

	public CameraNode getCamNode() {
		return camNode;
	}

	@Override
	public Vector3 getScreenCoordinates(Vector3 pos3d) {
		Vector3f dir=cam.getScreenCoordinates(new Vector3f(pos3d.getXf(),pos3d.getYf(),pos3d.getZf()));
		return new Vector3(dir.x,dir.y,dir.z);
	}

	@Override
	public void notifyPropertyChanged(IObjeto objeto, Enum property) {
		notifyPropertyChanged(objeto,property.toString());
		
	}


	@Override
	public IObjeto getSelection() {
		return selecao;
	}

	@Override
	public void setSelection(IObjeto objeto) {
		this.selecao=objeto;
		shipHud.updateSelection(objeto);
		itemsView.setSelection(objeto);
		
	}

	@Override
	public void notifyPropertyChanged(IObjeto objeto, String property) {
		 if (shipHud==null){
		return;
	}
		 shipHud.notifyPropertyChanged(objeto,property);
		
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

	public LightState getLightState() {
		return lightState;
		
	}



}
