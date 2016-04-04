package com.cristiano.galactic.view.demos;



//TODO add imports here

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.cristiano.comum.Formatacao;
import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.controller.GameLogic;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.faction.PlayerFaction;
import com.cristiano.galactic.model.wares.ModelData;
import com.cristiano.galactic.view.IView;
import com.cristiano.galactic.view.ItemsViewManagerAbstract;
import com.cristiano.galactic.view.hud.HUDController;
import com.cristiano.galactic.view.models.AbstractItemView;
import com.cristiano.gamelib.interfaces.IJogo;
import com.cristiano.gamelib.interfaces.IObjeto;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;
import com.cristiano.math.Vector3;
import com.jme.app.SimpleGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.MouseInput;
import com.jme.input.controls.GameControlManager;
import com.jme.input.controls.binding.MouseAxisBinding;
import com.jme.input.controls.binding.MouseOffsetBinding;
import com.jme.input.controls.controller.Axis;
import com.jme.input.controls.controller.RotationController;
import com.jme.math.Vector3f;
import com.jme.scene.CameraNode;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.jme.input.JmeInputSystem;
import de.lessvoid.nifty.jme.render.JmeRenderDevice;
import de.lessvoid.nifty.jme.sound.JmeSoundDevice;
import de.lessvoid.nifty.tools.TimeProvider;

class DummyItem extends AbstractItemView{
	Box b;
	Vector3 dest; //Usado para manter os objetos de movendo
	int maxPos=0;
	
	
	
	public DummyItem(IView view,int size,int pos) {
		super(view);
		this.maxPos=pos;
		dest=new Vector3(Math.random()*pos,Math.random()*pos,Math.random()*pos);
		b=new Box("box",Vector3f.ZERO,new Vector3f((float)(Math.random()*size),(float)(Math.random()*size),(float)(Math.random()*size)));
		b.setRandomColors();
		setCoord(new Vector3(Math.random()*pos,Math.random()*pos,Math.random()*pos));
		
		
	}
	
	public void update(){
		double x=dest.x-getCoord().x;
		double y=dest.y-getCoord().y;
		double z=dest.z-getCoord().z;
		if (Math.abs(x)<1)x=0;
		if (Math.abs(y)<1)y=0;
		if (Math.abs(z)<1)z=0;
		getView().notifyPropertyChanged(this, PropriedadesObjeto.PROP_POSITION);
		
		if (x==0)dest.x=Math.random()*maxPos;
		if (y==0)dest.y=Math.random()*maxPos;
		if (z==0)dest.z=Math.random()*maxPos;
		if (x>0)getCoord().x+=0.1;
		if (x<0)getCoord().x-=0.1;
		if (y>0)getCoord().y+=0.1;
		if (y<0)getCoord().y-=0.1;
		if (z>0)getCoord().z+=0.1;
		if (z<0)getCoord().z-=0.1;
		
		
		b.setLocalTranslation(getCoord().getXf(),getCoord().getYf(),getCoord().getZf());
	}
	
	@Override
	public String getPropertyAsText(String property) {
		if (property.equals(PropriedadesObjeto.PROP_POSITION)){
			return Formatacao.formatDistance(getCoord().getSubVector(getView().getPlayerAbsolutePosition()).magnitude());
		}
		
		return super.getPropertyAsText(property);
	}
	
	@Override
	public void attachBox(Vector3 halfSize) {
		
	}
	@Override
	public void attachSphere(double radius) {
		
	}
	
	@Override
	public void updatePosition(boolean direct) {
		
	}
	@Override
	public void loadModel(ModelData md) {
		
	}
	public Spatial getBox() {
		return b;
	}

	@Override
	public void activateControl(Enum group) {
		// TODO Auto-generated method stub
		
	}

	
	

	

		
}

public class TestHUD extends SimpleGame implements IView {

	
	private Vector<DummyItem> dummies;
	
	private Nifty nifty;
	private HUDController hudController;
	private IObjeto selecao,player;


  public static void main(final String[] args) {
      TestHUD app = new TestHUD();
      app.setConfigShowMode(ConfigShowMode.AlwaysShow);
      app.start();
      
  }

  public TestHUD() {
	  dummies=new Vector<DummyItem>();
  }
  
  private void generateRandomDummy(int qtd,int size){
	  for (int i=0;i<qtd;i++){
		  adicionaObjetoDummy(size, 200,"Dummy"+i);
	  }
  }

private DummyItem adicionaObjetoDummy(int size, int maxPos,String name) {
	DummyItem d=new DummyItem(this,size,maxPos);
	  d.setName(name);
	  
	  rootNode.attachChild(d.getBox());
	  hudController.registerObjetoJogo(d);
	  dummies.add(d);
	  return d;
}

  
  @Override
  protected void simpleInitGame() {
      // Initialize input system
		//camNode=new CameraNode("Camera Node", cam);
       // rootNode.attachChild(camNode);
        
        
        initCamera();

      nifty = new Nifty(new JmeRenderDevice(), new JmeSoundDevice(), new JmeInputSystem(), new TimeProvider());
      Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE); 
      Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE); 
      
      MouseInput.get().setCursorVisible(true);
     // nifty.fromXml("nifty/tutorial1.xml", "start");
      //nifty.fromXml("nifty/console-samescreen.xml", "start");
      nifty.fromXml(Consts.niftyHUD, "start");
      
      hudController=(HUDController) nifty.findScreenController(HUDController.class.getName());
      hudController.setJogo(this);
      hudController.addOverviewField("Name", PropriedadesObjeto.PROP_NAME.toString());
      hudController.addOverviewField("Distance", PropriedadesObjeto.PROP_POSITION.toString());
      hudController.addOverviewField("Speed", PropriedadesObjeto.PROP_SPEED.toString());
      //nifty.setDebugOptionPanelColors(true);
      //nifty.getNiftyMouse().enableMouseCursor("default");
    //  display.getRenderer().setBackgroundColor(ColorRGBA.white.clone());
      initSystemKeyBinds();
	   // Box b=new Box("box",Vector3f.ZERO,new Vector3f(100,100,100));
	   // b.setRandomColors();
	   // rootNode.attachChild(b);
      player=adicionaObjetoDummy(1,200,"Jogador");
      generateRandomDummy(30,10);
    
      hudController.initialize();
  
  }

private void initCamera() {
	// create a GameControlManager and Controls to rotate the camera node
	GameControlManager m = new GameControlManager();
	m.addControl("left").addBinding(new MouseOffsetBinding(MouseAxisBinding.AXIS_X, true));
	m.addControl("right").addBinding(new MouseOffsetBinding(MouseAxisBinding.AXIS_X, false));
	
	m.addControl("up").addBinding(new MouseOffsetBinding(MouseAxisBinding.AXIS_Y, true));
	m.addControl("down").addBinding(new MouseOffsetBinding(MouseAxisBinding.AXIS_Y, false));
	
	// create a camera node
	CameraNode camNode = new CameraNode("camNode", display.getRenderer().getCamera());
	rootNode.attachChild(camNode);
	
	// Moving the Mouse to the left or right should rotate the camera node around the Y Axis.
	RotationController yawControl = new RotationController(camNode, 
	                                        m.getControl("left"), m.getControl("right"), 1.0f, Axis.Y);
	
	// Moving the Mouse up or down should rotate the camera node around the X Axis.
	RotationController pitchControl = new RotationController(camNode, 
	                                        m.getControl("up"), m.getControl("down"), 1.0f, Axis.X);
	
	// add the controllers to the camera node
	camNode.addController(yawControl);
	camNode.addController(pitchControl);
}
  
  private void initSystemKeyBinds() {
		KeyBindingManager.getKeyBindingManager().remove("step");
		KeyBindingManager.getKeyBindingManager().remove("toggle_wire");
		KeyBindingManager.getKeyBindingManager().remove("screen_shot");
		KeyBindingManager.getKeyBindingManager().remove("toggle_normals");
		KeyBindingManager.getKeyBindingManager().remove("parallel_projection");
		KeyBindingManager.getKeyBindingManager().remove("mem_report");
		KeyBindingManager.getKeyBindingManager().remove("camera_out");
		//KeyBindingManager.getKeyBindingManager().remove("toggle_depth");
		//KeyBindingManager.getKeyBindingManager().remove("parallel_projection");
		
		
	
	}

  // TODO add methods here
  @Override
  protected void simpleRender() {
      super.simpleRender();
      for (int i=0;i<dummies.size();i++){
    	  dummies.get(i).update();
      }
      hudController.update();
     // System.out.println("Loc:"+cam.getLocation()+" Dir:"+cam.getDirection()+" Left:"+cam.getLeft()+" up:"+cam.getUp()+" "+cam.get );
      
      nifty.render(false);
      nifty.update();
      
      
  }

@Override
public void backLinkVisualItem(Object objSelectable, AbstractItemView itemView,
		boolean addToScene) {
	
}

@Override
public void createSkyBox() {
	
}

@Override
public ItemsViewManagerAbstract getItemsView() {
	return null;
}

@Override
public IObjeto getPlayer() {
	return player;
}

@Override
public Vector3 getPlayerAbsolutePosition() {
	return new Vector3(cam.getLocation().x,cam.getLocation().y,cam.getLocation().z);
}



@Override
public boolean isRunningGameLogic() {
	return false;
}

@Override
public void print(String message, boolean debug) {
System.out.println("TestHUD:"+message);
	
}

@Override
public void removeVisualObject(Item item) {
	
}


@Override
public void setGameLogic(GameLogic gameLogic) {
	
}

@Override
public void setItemsView(ItemsViewManagerAbstract itemsView) {
	
}

@Override
public void setRunGameLogic(boolean flagRun) {
	
}



@Override
public Vector3 getScreenCoordinates(Vector3 pos3d) {
	Vector3f dir=cam.getScreenCoordinates(new Vector3f(pos3d.getXf(),pos3d.getYf(),pos3d.getZf()));
	return new Vector3(dir.x,dir.y,dir.z);
}

@Override
public boolean sendConsoleCommand(String command,String[] pars) {
	return false;
}

@Override
public void notifyPropertyChanged(IObjeto objeto, Enum property) {
	hudController.propertyChanged(objeto, property.toString());
	
	
}

@Override
public IObjeto getSelection() {
	return selecao;
}

@Override
public void setSelection(IObjeto objeto) {
	this.selecao=objeto;
	hudController.updateSelection(objeto);
	
}

@Override
public void notifyPropertyChanged(IObjeto objeto, String property) {
	hudController.propertyChanged(objeto, property);
	
}

@Override
public PlayerFaction getPlayerFaction() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void updateCamera() {
	// TODO Auto-generated method stub
	
}
}