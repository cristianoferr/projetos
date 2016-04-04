/*
 * Copyright (c) 2005-2007 jME Physics 2
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of 'jME Physics 2' nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package galactic.view;

import galactic.Utils.Consts;
import galactic.controller.GameLogic;
import galactic.model.Entity.Abstract.Item;
import galactic.view.models.ItemView;
import galactic.view.models.AbstractItemView;
import galactic.view.models.ShipView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyPressedEvent;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.vecmath2.Point3f;
import org.xith3d.loaders.models.Model;
import org.xith3d.loop.UpdatingThread.TimingMode;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.SkyBox;
import org.xith3d.scenegraph.primitives.Sphere;

import cristiano.math.Vector3;
import cristiano.performance.ClockWatch;
import cyclone.xith3d.XithViewer;

/**
 * @author
 */
public class View extends XithViewer implements IView{
    private GameLogic gameLogic;
   // protected ChaseCamera chaser;
   // CameraNode camNode;		
    private String title="Galactic";

    public static boolean drawBox=false;
    public boolean useScale=true;
    public final static int clippingDist=500000;
    //public static double scale=0.0001f;
    Vector3 center_position=new Vector3(0,0,0); //Posição da camera (não necessariamente, mas normalmente, a posição do jogador)
    AbstractItemView selection=null;
    private Transform3D viewT = new Transform3D();
    private org.xith3d.scenegraph.View viewGraph;
    boolean flagUpdateCamera=true;
    public TransformGroup skyBoxTransform;
    //Camera update
    AbstractItemView old_item=null;
    Vector3 old_cam_rel_pos=null;
    Vector3 cam_lookat=null;
    
    ShipView player=null;
    
    KeyMapping keyMap;
    boolean runGameLogic=true;
    
    private ItemsViewManagerAbstract itemsView;
    
    
    private HashMap<Object,AbstractItemView> nodeHandler=new HashMap<Object,AbstractItemView>();


    public View(String resPath  ) throws Throwable {
    	super(parseCommandLine( new String[]{}),resPath );
        this.viewGraph = env.getView();
        keyMap=new KeyMapping(false);
        createSkyBox();
        
    }
    
    public void setRunGameLogic(boolean b){runGameLogic=b;}
    public boolean isRunningGameLogic(){return runGameLogic;}
     
	public void createSkyBox() {
		// setupBackground();
		//    skyBox=createSkyBox( resLoc.getResource( "skyboxes/" ), "normal" );
		    //skyBox.getBranchGroup().addChild(child)
		 //   BranchGroup branchGroup=skyBox.getBranchGroup();
	//	env.addRenderPass( skyBox );
		String s="nebula_bluedistance_stars_blue_diff.jpg";
		//s="nebula__part_02_diff.jpg";
		//s="nebula__dust_diff.jpg";
		
		Sphere sphere = new Sphere( 10000.25f, 160, 160, "aa" );
        sphere.setName( "Sphere" );
		setSkyBox(new SkyBox( s,s,s,s,s,s	));
		skyBoxTransform = new TransformGroup( 0, 0f, 0f );
		skyBoxTransform.addChild(sphere);
		//getSkyBox().getBranchGroup().addChild(sphereTransform);
		//rootBranch.addChild(sphereTransform);
		env.addRenderPass( getSkyBox() );
	}
    
	
	public void print(String message,boolean debug){
		XithViewer.printLog(message, debug);
	}
    
    public ShipView getPlayer(){
    	if (player==null){
    		player=(ShipView)getItemsView().createArtificialEntity("Player Ship","Nave do Jogador", Consts.initPos,0f,1);
    		player.changeVelocity(Consts.initForce, 0, 0);
    		//player=new Ship(world.getDataManager(),world.getDataManager().getWareManager().getWare("Player Ship").getName(),"Player Ship");
    		//player.getCoord().set(initPos,0f,1);
    		//world.addEntity(player);
    		//gameLogic.connectPlayer(player);
    		//player.getBody().setVelocity(initForce, 0, 0);
    	//	player.getBody().addRotation(new Vector3(0,0,0.2));
    		selection=player;
    	}
    	
    	return player;
    }

    public void inicializa(){
    	setItemsView(new ItemsViewManager(gameLogic.getItemsController(),this,false));
//    	System.out.println("view.start()");
    	printLog("View Started",true);
    	
    	itemsView.start();
    	//gameLogic.reg
      //  world.getPropertyChangeSupport().addPropertyChangeListener( ItemsController.ITEMS_PROPERTY, new ItemsListener() );
    	// finally subscribe a listener for stuff added to world (and removed from the world)
     //   getPlayer();
     //   world.testMessage();
        
      //  getRootBranch().addChild( new PointLight( 1f, 0.8f, 0.2f, 0f, 0f, 0f, 0.0000001f ) );
        
        begin();
    }

    public void render(){
    	ClockWatch.startClock("View.render");
    	AbstractItemView viewItem=player;
    	keyMap.turn(getPlayer());
    //	updateCamera(getPlayer());
    	if (selection!=null){
    		//newSelection(nodeHandler.get(selection));
    		
	    	setLabel1("Pos:"+Consts.format(player.getCoord()));
	    	
	    	setLabel2("Dist:"+Consts.format(selection.getCoord().getSubVector(player.getCoord()).magnitude()-selection.getTamX()));
	    	setLabel3("Rotation:"+Consts.format(player.getRotation()));
	    	setLabel4("Velocity:"+Consts.format(player.getVelocity().magnitude()));
    	}
    	//gameLogic.renderme(player.getNode(),viewItem.getCoord());
    //	System.out.println("view.render()");
    //viewItem.draw(false);
    	
    	//Esse metodo é para atualizar a engine fisica... num aplicativo multi-thread isso seria desnecessario
    	if (runGameLogic) gameLogic.update();
    	ClockWatch.startClock("View.render.afterUpdate");
    	Iterator iterator = itemsView.getViewModels().keySet().iterator();
        while (iterator.hasNext()) {
        	
          Item key = (Item) iterator.next();
          AbstractItemView itemView=itemsView.getViewModels().get(key);
          
          
          if (itemView==viewItem)
    		  updateCamera(getPlayer());
          itemView.draw(itemView==viewItem);
         // viewModel.draw(false);
        	 
         // viewModel.draw(false);
          //(items.get(key)).draw();
        }
       // updateCamera(getPlayer());
      //  updateCamera(getPlayer());
        ClockWatch.stopClock("View.render.afterUpdate");
        ClockWatch.stopClock("View.render");
    }
    
    
    public void newSelection(Node node){
      	 System.out.println("Node:"+node.getTransformGroup().getPosition().x()+" "+node.getTransformGroup().getPosition().y()+" "+node.getTransformGroup().getPosition().z());
      		
      		/*for (int i=0;i<items.size();i++){
      			   if (node==items.elementAt(i).obj){
      				   //ammo.elementAt(i).body.setPosition(node.getTransformGroup().getPosition().x(), node.getTransformGroup().getPosition().y(), node.getTransformGroup().getPosition().z());
      				   //ammo.elementAt(i).moving=false;
      				selection=items.elementAt(i).body;
      			   }
      		   }	*/
      	 
      	 if (nodeHandler.get(node)!=null){
      		 System.out.println("!!!!");
      		selection=nodeHandler.get(node);
      		getPlayer().setTarget(selection);}
          }
    
    public void update( long gameTime, long frameTime, TimingMode timingMode )
    {
    	super.update(gameTime, frameTime, timingMode);
    	render();
    	
//    	clean();
    	
    }
    
    
    
   /*
    * This is for selecting items coming from VisualItem
    */

    public void backLinkVisualItem(Object objSelectable,AbstractItemView itemView,boolean addToScene){
    	nodeHandler.put(objSelectable,itemView);
    	if (!addToScene) return;
    	if (objSelectable instanceof TransformGroup){
    		getRootBranch().addChild((TransformGroup)objSelectable);
    		return;}
    	if (objSelectable instanceof Model){
    		getRootBranch().addChild((Model)objSelectable);
    	return;}
    	if (objSelectable instanceof Shape3D){
    		getRootBranch().addChild((Shape3D)objSelectable);
    	return;}

    }
    
    
    /*
    public void registerVisualItem(Node objSelectable,ItemViewAbstract itemView){
    	nodeHandler.put(objSelectable,itemView);
    }*/
    
    
    /**
     * We call this method for all items that get removed from the world.
     * <br> (it is package local to allow direct access from the listener)
     *
     * @param item removed item
     */
    
   public void removeVisualObject( Item item ) {
        // nothing to be done as the items node is deleted by the controller
	   //System.out.println("View.unregister: "+item+" "+nodeHandler.size());
	   getRootBranch().removeChild(getVisualItem(getItemsView().getItemViewFromItem(item)));
	   
	   ItemView itemView=(ItemView) itemsView.getItemViewFromItem(item);
	   
		Iterator iterator = nodeHandler.keySet().iterator();
		Vector v=new Vector();
		
	    while (iterator.hasNext()) {
	    	try {
				

		    	Object key = (Object) iterator.next();
		    	AbstractItemView parm=nodeHandler.get(key);
		    	if (parm==itemView) {
		    		v.add(key);
		    	}
				} catch (Exception e) {
					e.printStackTrace();
			}
		}
	    for (int i=0;i<v.size();i++)nodeHandler.remove(v.get(i));
	    
	  //  System.out.println("View.unregister fim: "+keyR+" "+nodeHandler.get(keyR));
    }

   
    
    public TransformGroup getVisualItem(AbstractItemView itemView){
    	return ((ItemView)itemView).getGroup();/*
		Iterator iterator = nodeHandler.keySet().iterator();
	    while (iterator.hasNext()) {
	    	TransformGroup key = (Node) iterator.next();
	    	ItemViewAbstract parm=nodeHandler.get(key);
	    	if (parm==itemView) return key;
			
		}
    	return null;*/
    }
    
   
    
    //Parte Gráfica do Client
    
	
	long lastTime;
	



	
    

	
	
	public void updateCamera(AbstractItemView item){
    	if (!flagUpdateCamera) return;
    	
    	/*position.addX( speed * direction );
        position.addZ( speed * direction );
        position.setY( terrain.getY( position.getX(), position.getZ() ) + HEIGHT );
        
        lookat.set( position );
        lookat.addX( distance * direction );
        lookat.addZ( distance * direction );
        lookat.setY( terrain.getY( lookat.getX(), lookat.getZ() ) + HEIGHT );
        */
    	if (item!=old_item){
    		old_item=item;
    		
    		old_cam_rel_pos=item.getCameraPosition();
    		if (old_cam_rel_pos==null)
    			old_cam_rel_pos=new Vector3(0,0,0);
    		
/*    		
    		Vector<ItemSlot> v=item.getSlotsFromWareGroup(item.getDataManager().getWareManager().getWareGroup(DetailManager.GRP_CAMERA));
    		
    		if (v.size()>0){
    			old_cam_rel_pos=v.get(0).getAbsPos();
    			cam_lookat=v.get(0).getSlot().getOrientation();
    		}*/
    		
    	}
    	center_position=item.getCoord();
    /*	center_position.x=item.getBody().getPosition().x;
    	center_position.y=item.getBody().getPosition().y;
    	center_position.z=item.getBody().getPosition().z;*/
    	
    	Vector3  lookat=item.getPointInWorldSpace(new Vector3(item.getTamX(),item.getTamY()/2,0));
    	//lookat.x((float)(lookat.x()-offset));
    	lookat.x=((lookat.x-center_position.x));
    	lookat.y=((lookat.y-center_position.y));
    	lookat.z=((lookat.z-center_position.z));
    /*lookat.x(0);
    	lookat.y(0);
    	lookat.z(0);*/
    //	lookat.x((float)item.getModelData().getTamX());
    	
    	Vector3 position=item.getPointInWorldSpace(old_cam_rel_pos);
    	position.x=(position.x-center_position.x);
    	position.y=(position.y-center_position.y);
    	position.z=(position.z-center_position.z);
    	//position.x=(float)(position.x-offset);
    	//System.out.println("pos:"+position.x);
   	//lookat.addY(-20);
    //	lookat.addX(-30);
    	Point3f pos=position.getPoint3f();
    	//pos.addY(20);
    	
    //	System.out.println(" pos:"+item.getBody().getPosition()+" lookat:"+lookat+" "+position);
        
        
     /*   final float vnear = 0.1f;
		final float vfar = Float.MAX_VALUE;
		final float k = 0.8f;     // view scale, 1 = +/- 45 degrees
		
		//viewT.frustum (-vnear*k,vnear*k,-vnear*k,vnear*k,vnear,vfar);
		
		final float vnear2 = 2f * vnear;
		final float t3 = vfar - vnear;
		float m22=-( vfar + vnear ) / t3;
		float m23=  -vfar * vnear2 / t3;*/
    	//view.setBackClipDistance(5000f);
    	//view.setFrontClipDistance(0.001f);
		viewT.lookAt( pos, lookat.getPoint3f(), item.getTransformDirection(new Vector3(0,1,0)).getVector3f());///lookat);//Vector3.UP.getVector3f() );

		//float[] f=new float[]{0,0,0,0};
		//viewT.getMatrix4f().getRow(3, f);
		//f[2]=m22;
		//f[3]=m23;
		//view.setBackClipDistance(1);
	//	view.setBackClipDistance(500000000f);
	//	view.setFrontClipDistance(1f);
		//viewT.getMatrix4f().setRow(3,f);
		//viewT.getMatrix4f().setRow(2,new float[]{0,0,m22,m23});
        //viewT.frustum(float left, float right, float bottom, float top, float near, float far)
        //aircraft.getRotation().getVector3f()
        viewGraph.setTransform( viewT );
       /* center_position.x=item.getBody().getPosition().x;
    	center_position.y=item.getBody().getPosition().y;
    	center_position.z=item.getBody().getPosition().z;*/
    }
	

	 public void onKeyReleased( KeyReleasedEvent e, Key key ){
		 keyMap.releaseKey(key.getKeyID());
	 }
	
    public void onKeyPressed( KeyPressedEvent e, Key key )
    {
    	super.onKeyPressed(e, key);
    	keyMap.pressKey2(key.getKeyID().toString());
    	  if (console.isPoppedUp()) return;
 	    switch ( key.getKeyID() ){
 	   case P:
 			flagUpdateCamera=!flagUpdateCamera;
 		break;
 	  case V:
			useScale=!useScale;
		break;
 	  case HOME:
 		  getPlayer().changeVelocity(0, 0, 0);
 		 getPlayer().changeRotation(0, 0,0);
 		  break;
 	 case END:
		 getPlayer().changeRotation(0, 0,0);
		  break;

    case O:
    	ClockWatch.report();
		  break;
      }
    }



	public Vector3 getRelativePosition() {
		return center_position;
	}



	public void setNodeHandler(HashMap<Object,AbstractItemView> nodeHandler) {
		this.nodeHandler = nodeHandler;
	}


	public void setItemsView(ItemsViewManagerAbstract itemsView) {
		this.itemsView = itemsView;
	}


	public ItemsViewManagerAbstract getItemsView() {
		return itemsView;
	}


	@Override
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic=gameLogic;
		
	}


	


	



	
	

	
}

/*
 * $Log$
 */

