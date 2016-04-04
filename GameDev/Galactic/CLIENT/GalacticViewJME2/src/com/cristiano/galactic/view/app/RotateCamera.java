package com.cristiano.galactic.view.app;

/*
 * RotateCamera.java
 */
 

import java.util.Vector;

import com.cristiano.galactic.Utils.Consts;
import com.cristiano.galactic.view.hud.HUDController;
import com.jme.input.AbsoluteMouse;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.CameraNode;
import com.jme.scene.Spatial;
 
/**
 * InputHandler for orbit rotation around selected object
 *
 * @author slaava
 * @version 1.0
 */
public class RotateCamera extends InputHandler {
 
	KeyMappingJME keyMap;
    /**
     * Custom mouse input handler
     */
    private RotateMouseInput mouseInput;
 
    /**
     * Selected spatial to rotation around
     */
    private Spatial target;
 
    /**
     * Node with camera
     */
    private CameraNode camNode;
 
    /**
     * Rotation
     */
    private Quaternion quatRotation;
 
    /**
     * Camera translation
     */
    private Vector3f camTranslation;
 
    /**
     * Distance between target and camera
     */
    private float distance;
 
    /**
     * Zoom modificator
     */
    private float zoomTime = 0.005f;
 
    /**
     * Scene width
     */
    private int width;
 
    /**
     * Scene height
     */
    private int height;
 
    /**
     * Action for keyboard input to rotate up
     */
    private KeyInputAction up;
    
    private KeyInputAction reset;
    
    private boolean lockedCamera=true;
    private KeyInputAction cam_lock;
    
    
    /**
     * Action for keyboard input to rotate down
     */
    private KeyInputAction down;
 
    /**
     * Action for keyboard input to rotate left
     */
    private KeyInputAction left;
 
    /**
     * Action for keyboard input to rotate right
     */
    private KeyInputAction right;
 
    /**
     * Action for keyboard input to zoom in
     */
    private KeyInputAction zoomIn;
 
    /**
     * Action for keyboard input to zoom out
     */
    private KeyInputAction zoomOut;
	
 
    private HUDController hudController;
    /**
     * Constructor with scene dimension and camera node
     *
     * @param width scene width
     * @param height scene height
     * @param camNode camera node
     */
    public RotateCamera(HUDController hudController,int width, int height, CameraNode camNode,KeyMappingJME keyMap) {
        super();
        this.hudController=hudController;
        this.width = width;
        this.height = height;
        
        this.keyMap=keyMap;
       // clearActions();
        createMouseInput();
        createKeyInput();
 
        
        this.camNode = camNode;
        
        initSystemKeyBinds();
    }

	private void initSystemKeyBinds() {
		KeyBindingManager.getKeyBindingManager().remove("step");
		KeyBindingManager.getKeyBindingManager().remove("toggle_wire");
		KeyBindingManager.getKeyBindingManager().remove("screen_shot");
		KeyBindingManager.getKeyBindingManager().remove("toggle_normals");
		KeyBindingManager.getKeyBindingManager().remove("parallel_projection");
		KeyBindingManager.getKeyBindingManager().remove("mem_report");
		KeyBindingManager.getKeyBindingManager().remove("camera_out");
		KeyBindingManager.getKeyBindingManager().remove("toggle_pause");
		KeyBindingManager.getKeyBindingManager().remove("toggle_bounds");
		KeyBindingManager.getKeyBindingManager().remove("toggle_depth");
		//KeyBindingManager.getKeyBindingManager().remove("toggle_depth");
		//KeyBindingManager.getKeyBindingManager().remove("parallel_projection");
		
		
		
		/*
		KeyBindingManager.getKeyBindingManager().set( "toggle_pause",
                KeyInput.KEY_P );
        // Assign key ADD to action "step". 
        KeyBindingManager.getKeyBindingManager().set( "step",
                KeyInput.KEY_ADD );
        // Assign key T to action "toggle_wire". 
        KeyBindingManager.getKeyBindingManager().set( "toggle_wire",
                KeyInput.KEY_T );
        // Assign key L to action "toggle_lights". 
        KeyBindingManager.getKeyBindingManager().set( "toggle_lights",
                KeyInput.KEY_L );
        // Assign key B to action "toggle_bounds". 
        KeyBindingManager.getKeyBindingManager().set( "toggle_bounds",
                KeyInput.KEY_B );
        // Assign key N to action "toggle_normals". 
        KeyBindingManager.getKeyBindingManager().set( "toggle_normals",
                KeyInput.KEY_N );
        // Assign key C to action "camera_out". 
        KeyBindingManager.getKeyBindingManager().set( "camera_out",
                KeyInput.KEY_C );
        KeyBindingManager.getKeyBindingManager().set( "screen_shot",
                KeyInput.KEY_F1 );
        KeyBindingManager.getKeyBindingManager().set( "exit",
                KeyInput.KEY_ESCAPE );
        KeyBindingManager.getKeyBindingManager().set( "parallel_projection",
                KeyInput.KEY_F2 );
        KeyBindingManager.getKeyBindingManager().set( "toggle_depth",
                KeyInput.KEY_F3 );
        KeyBindingManager.getKeyBindingManager().set("mem_report",
                KeyInput.KEY_R);*/
	}
 
    /**
     * Asign new target. Camera will rotate around it.
     *
     * @param target target object
     * @param camNode 
     */
    public void reLoad(Spatial target, CameraNode camNode) {
        this.target = target;
        quatRotation = new Quaternion();
        this.camNode=camNode;
 
        Camera camera = camNode.getCamera();
        distance = camera.getLocation().distance(target.getWorldTranslation());
 
        camTranslation = new Vector3f(0, 50, -150);
        camNode.setLocalTranslation(camera.getLocation());
        camNode.lookAt(target.getWorldTranslation(), camera.getUp());
        quatRotation = camNode.getLocalRotation();
    }
 
    /**
     * Create mouse input
     */
    private void createMouseInput() {
        AbsoluteMouse absoluteMouse = new AbsoluteMouse("Absolute mouse", width, height);
 
        mouseInput = new RotateMouseInput(absoluteMouse, this);
        addAction(mouseInput);
    }
 
    /**
     * Create key input
     */
    private void createKeyInput() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();
 
     //   keyboard.set("reset", KeyInput.KEY_T);
    /*    keyboard.set("up", KeyInput.KEY_W);
        keyboard.set("down", KeyInput.KEY_S);
        keyboard.set("right", KeyInput.KEY_A);
        keyboard.set("left", KeyInput.KEY_D);*/
        keyboard.set("zoomIn", KeyInput.KEY_UP);
        keyboard.set("zoomOut", KeyInput.KEY_DOWN);
        
        keyboard.set("toggle_camera_lock", KeyInput.KEY_HOME);
        
        Vector<String> keys=keyMap.getKeys();
        KeyMapInputAction action;
       // clearActions();
        
        for (int i=0;i<keys.size();i++){
        	action= new KeyMapInputAction(keys.get(i)) {
                public void performAction(InputActionEvent evt) {
                	super.performAction(evt);
                    pressKey(evt,key);
                }
            };
            //System.out.println("addAction:"+keys.get(i)+" "+action);
            keyboard.set(keys.get(i), KeyMappingJME.getKeyInput(keys.get(i)));
            addAction(action, keys.get(i), true);
        }
 

        reset = new KeyInputAction() {
            public void performAction(InputActionEvent evt) {
                reLoad(target,camNode);
            }
        };
        
        cam_lock = new KeyInputAction() {
            public void performAction(InputActionEvent evt) {
                camLockAlternate();
            }

			
        };
        
        up = new KeyInputAction() {
            public void performAction(InputActionEvent evt) {
                rotate((width / 2), (height / 2), (width / 2), (height / 2) + 1);
            }
        };
 
        down = new KeyInputAction() {
            public void performAction(InputActionEvent evt) {
                rotate((width / 2), (height / 2), (width / 2), (height / 2) - 1);
            }
        };
 
        left = new KeyInputAction() {
            public void performAction(InputActionEvent evt) {
                rotate((width / 2), (height / 2), (width / 2) - 1, (height / 2));
            }
        };
 
        right = new KeyInputAction() {
            public void performAction(InputActionEvent evt) {
                rotate((width / 2), (height / 2), (width / 2) + 1, (height / 2));
            }
        };
 
        zoomIn = new KeyInputAction() {
            public void performAction(InputActionEvent evt) {
                zoom(zoomTime);
            }
        };
 
        zoomOut = new KeyInputAction() {
            public void performAction(InputActionEvent evt) {
                zoom(-zoomTime);
            }
        };
 
        addAction(down, "down", true);
        addAction(up, "up", true);
        addAction(reset, "reset", true);
        addAction(left, "left", true);
        addAction(right, "right", true);
        addAction(zoomIn, "zoomIn", true);
        addAction(zoomOut, "zoomOut", true);
        addAction(cam_lock, "toggle_camera_lock", false);
        
    }

    
    private void camLockAlternate() {
		lockedCamera=!lockedCamera;
		
	}
    
    public void pressKey(InputActionEvent evt, String key){
    	//evt.
    	//System.out.println(KeyInput.get().isKeyDown(KeyMappingJME.getKeyInput(key)));
    //	System.out.println(evt.getTriggerIndex()+" "+evt.getTriggerPressed()+" "+evt.getTriggerAllowsRepeats()+" "+evt.getTriggerPosition()+" "+evt.getTriggerDevice()+" "+evt.getTriggerData()+" "+evt.getTriggerDelta()+" "+evt.getTriggerCharacter()+" "+evt.getTriggerName()+" ");
    	if (hudController.isConsoleVisible())return;
    	keyMap.getKeyMap().pressKey(key);
    }

    /**
     * Move camera to new position depending the mouse location
     *
     * @param oldX old X position
     * @param oldY old Y position
     * @param newX new X position
     * @param newY new Y position
     */
    
    public void rotate(int oldX, int oldY, int newX, int newY) {
    	//if (isLockedCamera())return;
    	
        Vector3f oLastVector3f = this.computeTrackPosition(oldX, oldY);
        //quatRotation = new Quaternion();
        Vector3f oNewVector3f = this.computeTrackPosition(newX, newY);
        Vector3f oAxisVector = oLastVector3f.cross(oNewVector3f);
        oAxisVector=oAxisVector.mult(new Vector3f(0,1,0));
 
        Vector2f oVector2f = new Vector2f(newX - oldX, newY - oldY);
        float tAngle = oVector2f.length();
        Quaternion quatLocal = new Quaternion();
        quatLocal.fromAngleAxis(-tAngle * FastMath.DEG_TO_RAD, oAxisVector);
        quatRotation.multLocal(quatLocal);
        camNode.setLocalTranslation(quatRotation.mult(camTranslation).add(target.getWorldTranslation()));
       // camNode.setLocalTranslation(quatLocal.mult(target.getWorldTranslation()));
        camNode.setLocalRotation(quatRotation);
    }
 
    /**
     * TrackBall mapping
     *
     * @param x X position
     * @param y Y position
     * @return TrackBall position
     */
    private Vector3f computeTrackPosition(int x, int y) {
        float newX = ((float) x) - (float) width / 2;
        float newY = ((float) y) - (float) height / 2;
        float z2 = width * height * 1000 - x * x - y * y;
        float z = z2 > 0 ? FastMath.sqrt(z2) : 0;
        return new Vector3f(newX, newY, z);
    }
 
    /**
     * Zoom in/out camera to target
     *
     * @param value amount zooming
     */
    protected void zoom(float value) {
    	float d= Math.abs(camTranslation.x)+Math.abs(camTranslation.y)+Math.abs(camTranslation.z);
    	//System.out.println(value+" "+camTranslation+" "+d);
    	if ((value<0)&&(d>Consts.MAXDISTANCE_CAMERA)) return;
        camTranslation.multLocal(1-value);
        Vector3f difference = camNode.getLocalTranslation().subtract(target.getWorldTranslation());
        difference.multLocal(1-value);
        Vector3f newCoords = target.getWorldTranslation().add(difference);
        camNode.setLocalTranslation(newCoords);
    }
 

    /**
     * Update dimension
     *
     * @param width new width
     * @param height new height
     */
    public void updateSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

	public boolean isLockedCamera() {
		return lockedCamera;
	}

	public void setLockedCamera(boolean lockedCamera) {
		this.lockedCamera = lockedCamera;
	}
 
}