package com.cristiano.galactic.view.app;

/*
 * RotateMouseInput.java
 */
 
import com.jme.input.AbsoluteMouse;
import com.jme.input.MouseInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;
 
/**
 * Custom mouse handler for RotateCamera handler
 *
 * @author slaava
 * @version 1.0
 */
public class RotateMouseInput extends MouseInputAction {
 
    /**
     * RotateCamera with target object
     */
    private RotateCamera parent;
 
    /**
     * Left mouse button
     */
    public static int MOUSE_BUTTON_LEFT = 0;
 
    /**
     * Button pressed
     */
    private boolean pressed = false;
 
    /**
     * Old X position
     */
    private int oldX;
 
    /**
     * Old Y position
     */
    private int oldY;
 
    /**
     * New X position
     */
    private int newX;
 
    /**
     * New Y position
     */
    private int newY;
 
    /**
     * Zoom modificator
     */
    private float zoomSpeed = 0.001f;
 
    /**
     * Constructor with mouse and RotateCamera
     *
     * @param mouse mouse input
     * @param parent RotateCamera with target
     */
    public RotateMouseInput(AbsoluteMouse mouse, RotateCamera parent) {
        this.mouse = mouse;
        this.parent = parent;
    }
 
    public void performAction(InputActionEvent evt) {
        // left button
        if (MouseInput.get().isButtonDown(MOUSE_BUTTON_LEFT)) {
            if (!pressed) {
                pressed = true;
                oldX = MouseInput.get().getXAbsolute();
                oldY = MouseInput.get().getYAbsolute();
 
            }
            newX = MouseInput.get().getXAbsolute();
            newY = MouseInput.get().getYAbsolute();
 
            // camera rotation
            parent.rotate(oldX, oldY, newX, newY);
 
            oldX = newX;
            oldY = newY;
        } else {
            pressed = false;
        }
 
        // zoom
        int wdelta = MouseInput.get().getWheelDelta();
        if (wdelta != 0) {
            parent.zoom(wdelta * zoomSpeed);
        }
    }
 
    /**
     * Zoom modificator getter
     *
     * @return zoom modificator
     */
    public float getZoomSpeed() {
        return zoomSpeed;
    }
 
    /**
     * Zoom modificator setter
     *
     * @param zoomSpeed new zoom modificator
     */
    public void setZoomSpeed(float zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
    }
 
}