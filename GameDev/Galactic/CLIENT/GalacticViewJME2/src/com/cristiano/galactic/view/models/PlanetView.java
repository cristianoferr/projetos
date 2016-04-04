package com.cristiano.galactic.view.models;

import jme2.effects.procedural.ProceduralPlanet;
import jme2.effects.textures.PerlinNoise;

import com.cristiano.galactic.view.View;
import com.cristiano.math.Vector3;
import com.jme.image.Texture;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;


public class PlanetView extends SphereView {
private ProceduralPlanet pp;
private TextureState ts1;
private PerlinNoise paintedText;

	public PlanetView( View view,double radius) {
		super( view,radius);
		
		
		paintedText = new PerlinNoise(128,128,10);
		Texture texture =TextureManager.loadTexture(getClass().getResource("hudtutorial3.png"), Texture.MinificationFilter.Trilinear,
	            Texture.MagnificationFilter.Bilinear, 1.0f, false);
		
	    texture.setImage(paintedText);
	    
	    ts1 = view.getDisplay().getRenderer().createTextureState();
	    ts1.setTexture(texture);
	    ts1.setEnabled(true);
	    		
		pp=new ProceduralPlanet((float) radius, ts1);
		pp.setMultiHeight((int) (radius/50));
		view.registerProceduralLOD(this);
	}



	public void applyError(Vector3 center_position) {
		if (pp.isUnlocked()){
			pp.lock();
			pp.applyError(getCoord().getMultiVector(-1));
			pp.unlock();
		}
		
		
    	
	}
	
	public void updatePosition(boolean direct){
		super.updatePosition(direct);
		if ((pp.meshChanged)&&(pp.isUnlocked())){
			pp.lock();
    		objVisual.detachAllChildren();
        	//pp.createMesh(objVisual);
    		objVisual.attachChild(pp.generateMesh(ts1));
    		//objVisual.attachChild(pp.generateLines(ts1));
    		
        	//node2.setLocalTranslation(750,0,0);
        	//node2.setLocalTranslation(-750,0,0);
        	//objVisual.setRenderState(ts1);
        	objVisual.updateRenderState();
        	rootNode.updateRenderState();
        	pp.meshChanged=false;
        	pp.unlock();
        }
	}
	
}
