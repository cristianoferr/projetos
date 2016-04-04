package com.cristiano.galactic.view.demos;
import java.nio.FloatBuffer;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TexCoords;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;
public class HUDTutorial2 extends SimpleGame{
	 private static final float MAXIMUM = 100f;
     protected Quaternion rotQuat = new Quaternion();
     protected Vector3f axis = new Vector3f(1, 1, 0);
     protected Cylinder cylinder;
     protected float angle = 0;
 
     private Node hudNode;
     protected Quad gauge;
 
     private int textureWidth;
     private int textureHeight;
     
     
     
     public static void main(String[] args) {
         HUDTutorial2 app = new HUDTutorial2();
         app.setConfigShowMode(SimpleGame.ConfigShowMode.AlwaysShow);
         app.start();
     }
     

     private float getUForPixel(int xPixel) {
         return (float) xPixel / textureWidth;
     }
 
     private float getVForPixel(int yPixel) {
         return 1f - (float) yPixel / textureHeight;
     }
     
     
     private void setGauge(int value) {
         value %= (int)MAXIMUM;
         FloatBuffer texCoords = BufferUtils.createVector2Buffer(4);
         float relCoord = 0.5f - ((float)value / MAXIMUM) * 0.5f;
         texCoords.put(relCoord).put(getVForPixel(56));
         texCoords.put(relCoord).put(getVForPixel(63));
         texCoords.put(relCoord + 0.5f).put(getVForPixel(63));
         texCoords.put(relCoord + 0.5f).put(getVForPixel(56));     
         gauge.setTextureCoords(new TexCoords(texCoords));
    }
     
     protected void simpleInitGame() {
         display.setTitle("HUD Tutorial 3");
 
         /* create a rotating cylinder so we have something in the background */
         cylinder = new Cylinder("Cylinder", 6, 18, 5, 10);
         cylinder.setModelBound(new BoundingBox());
         cylinder.updateModelBound();
 
         MaterialState ms = display.getRenderer().createMaterialState();
         ms.setAmbient(new ColorRGBA(1f, 0f, 0f, 1f));
         ms.setDiffuse(new ColorRGBA(1f, 0f, 0f, 1f));
 
         ms.setEnabled(true);
         cylinder.setRenderState(ms);
         cylinder.updateRenderState();
 
         rootNode.attachChild(cylinder);
 
         hudNode = new Node("hudNode");
         
         Quad hudQuad = new Quad("hud", 34f, 10f);
         gauge = new Quad("gauge", 32f, 8f);
         
         hudNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
         
         hudNode.setLocalTranslation(new Vector3f(display.getWidth()/2,display.getHeight()/2,0));        
  
         TextureState ts = display.getRenderer().createTextureState();
         ts.setTexture(TextureManager.loadTexture(getClass().getResource("hudtutorial3.png"), Texture.MinificationFilter.Trilinear,
                 Texture.MagnificationFilter.Bilinear, 1.0f, true));
         textureWidth = ts.getTexture().getImage().getWidth();
         textureHeight = ts.getTexture().getImage().getHeight();
         ts.setEnabled(true);
  
         FloatBuffer texCoords = BufferUtils.createVector2Buffer(4);
         texCoords.put(getUForPixel(0)).put(getVForPixel(0));
         texCoords.put(getUForPixel(0)).put(getVForPixel(10));
         texCoords.put(getUForPixel(34)).put(getVForPixel(10));
         texCoords.put(getUForPixel(34)).put(getVForPixel(0));
         //hudQuad.setTextureBuffer(0, texCoords);
         hudQuad.setTextureCoords(new TexCoords(texCoords));
  
         BlendState as = display.getRenderer().createBlendState();
         as.setBlendEnabled(true);
  
         as.setSourceFunctionAlpha(BlendState.SourceFunction.SourceAlpha);
         // set the destination function
         as.setDestinationFunctionAlpha(BlendState.DestinationFunction.OneMinusSourceAlpha);

         //as.setSrcFunction(BlendState.SourceFunction.SourceAlpha);
         //as.setDstFunction(BlendState.DB_ONE_MINUS_SRC_ALPHA);
         as.setTestEnabled(false);
         as.setEnabled(true);             

         
         hudNode.setLightCombineMode(Spatial.LightCombineMode.Off);        
         hudNode.updateRenderState();
  
         hudNode.attachChild(hudQuad);
         hudNode.attachChild(gauge);
  
         hudNode.setRenderState(ts);
         hudNode.setRenderState(as);
         hudNode.updateRenderState();
        // setGauge(0);
         rootNode.attachChild(hudNode);
     }
     
     protected void simpleUpdate() {
         /* recalculate rotation for the cylinder */
         if (timer.getTimePerFrame() < 1) {
             angle = angle + timer.getTimePerFrame();
         }
         rotQuat.fromAngleAxis(angle, axis);
         cylinder.setLocalRotation(rotQuat);
         setGauge((int)cam.getLocation().length());
    }
}
