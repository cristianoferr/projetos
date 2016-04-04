package com.cristiano.galactic.view.demos;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

public class HUDTutorial4 extends HUDTutorial2{
    private PaintedGauge gaugeImage;
    private TextureState ts1;
    private int distance = 0;
    
    
    
    public static void main(String[] args) {
        HUDTutorial4 app = new HUDTutorial4();
        app.setConfigShowMode(SimpleGame.ConfigShowMode.AlwaysShow);
        app.start();
    }
    

    public void simpleInitGame(){
    	super.simpleInitGame();
    	/* now create the gauge */
        gaugeImage = new PaintedGauge();
        gaugeImage.setValue(80);

       /* Texture texture = new Texture(); 
        texture.setApply(Texture.AM_MODULATE);
        texture.setBlendColor(new ColorRGBA(1, 1, 1, 1));
        texture.setCorrection(Texture.CM_PERSPECTIVE);
        texture.setFilter(Texture.MM_LINEAR);*/

        
        Texture texture =TextureManager.loadTexture(getClass().getResource("hudtutorial2.png"), Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear, 1.0f, true);
      /*  Texture texture = TextureManager.loadTexture(monkeyLoc,
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear);*/
        
        texture.setImage(gaugeImage);
        //texture.setMipmapState(Texture.FM_LINEAR);

        ts1 = display.getRenderer().createTextureState();

        ts1.setTexture(texture);
        ts1.setEnabled(true);
        gauge.setRenderState(ts1);
        gauge.updateRenderState();
    }
    
    protected void simpleUpdate() {
        /* recalculate rotation for the cylinder */
        if (timer.getTimePerFrame() < 1) {
            angle = angle + timer.getTimePerFrame();
        }
        rotQuat.fromAngleAxis(angle, axis);
        cylinder.setLocalRotation(rotQuat);

        int newDistance = (int)cam.getLocation().length();
        if (newDistance != distance) {
            distance = newDistance;
            gaugeImage.setValue(distance);
            /* force re-apply of the texture, otherwise the changed 
               image won't be uploaded */
            ts1.deleteAll();
        }
    }
}
