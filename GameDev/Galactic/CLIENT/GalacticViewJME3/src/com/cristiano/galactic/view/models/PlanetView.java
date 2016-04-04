package com.cristiano.galactic.view.models;


import java.awt.Color;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.galactic.model.Entity.Abstract.Item;
import com.cristiano.galactic.model.enums.PlanetProperties;
import com.cristiano.galactic.view.View;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;



public class PlanetView extends SphereView {
	Vector3f[] vertex_vel,vertex_dir;
	AssetManager assetManager;
	Planet planet;

	public PlanetView( View view,double radius) {
		super(view,radius);
		this.assetManager=view.getAssetManager();
		
		
	}
	
	public void createVisualNodeFromGeom(Item item) {
		//super.createVisualNodeFromGeom(item);
		
		double atmosDensity=getPropertyAsDouble(PlanetProperties.PP_ATMOS_DENSITY);
		double height=getPropertyAsDouble(PlanetProperties.PP_PLANET_HEIGHTSCALE);
		double seed=getPropertyAsDouble(PlanetProperties.PP_PLANET_SEED);
		
		
		if (atmosDensity>0) {
			planet=createEarthLikePlanet((float)getRadius(),(float)(height),(int)seed);
			Color corAtmos=getPropertyAsColor(PlanetProperties.PP_ATMOS_COLOR);
			ColorRGBA corRGBA=new ColorRGBA((float)(corAtmos.getRed()/255f),(float)(corAtmos.getGreen()/255f),(float)(corAtmos.getBlue()/255f),(float)(corAtmos.getAlpha()/255f));
			planet.setAtmosphereFogColor(corRGBA);
			planet.setAtmosphereFogDensity((float) atmosDensity);
		} else{
			planet=createMoonLikePlanet((float)getRadius(),(float)(height),(int)seed);
		}
		
		
		objVisual=planet;
		rootNode.attachChild(objVisual);
		getView().backLinkVisualItem(rootNode,this,true);
		
		((View)getView()).addPlanet(planet);
	}
	
	public void update(){
		super.update();
		if (Float.isNaN(getCoordf().x)){
			return;
		}
		if ((getCoordf().x==getCoordf().y) && (getCoordf().x==getCoordf().z) && (getCoordf().x==0)){
			return;
		}
		
		//Não atualizo os objetos que estão muito longe...
		double d=getCoord().magnitude();
		if (d>PhysicsConsts.C*5){
			return;
		}
			planet.setCameraPosition(getCoordf());
        
	}
	
    private Planet createEarthLikePlanet(float radius, float heightScale, int seed) {
        boolean logarithmicDepthBuffer = true;
        
        // Prepare planet material
        Material planetMaterial = new Material(this.assetManager, "MatDefs/Planet/Terrain.j3md");
        
        // shore texture
        Texture dirt = this.assetManager.loadTexture("textures/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region1ColorMap", dirt);
        planetMaterial.setVector3("Region1", new Vector3f(0, heightScale * 0.2f, 0));
        // grass texture
        Texture grass = this.assetManager.loadTexture("textures/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region2ColorMap", grass);
        planetMaterial.setVector3("Region2", new Vector3f(heightScale * 0.16f, heightScale * 0.88f, 0));
        // rock texture
        Texture rock = this.assetManager.loadTexture("textures/rock.jpg");
        rock.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region3ColorMap", rock);
        planetMaterial.setVector3("Region3", new Vector3f(heightScale * 0.84f, heightScale * 1.36f, 0));
        // snow texture
        Texture snow = this.assetManager.loadTexture("textures/snow.jpg");
        snow.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region4ColorMap", snow);
        planetMaterial.setVector3("Region4", new Vector3f(heightScale * 0.94f, heightScale * 1.5f, 0));
        
        //planetMaterial = new Material(this.assetManager, "MatDefs/Planet/LogarithmicDepthBufferSimple.j3md");
        //planetMaterial.setColor("Color", ColorRGBA.Green);

        // Turn on Logarithmic Depth Buffer to avoid z-fighting
        planetMaterial.setBoolean("LogarithmicDepthBuffer", logarithmicDepthBuffer);
        
         // Create height data source
        FractalDataSource dataSource = new FractalDataSource(seed);
        dataSource.setHeightScale(heightScale);
        
        // create planet
        Planet planet = new Planet("Planet", radius, planetMaterial, dataSource);
        planet.setAtmosphereFogDensity(1);
        
        // create ocean
        Material oceanMaterial = assetManager.loadMaterial("Materials/Ocean.j3m");
        //oceanMaterial = new Material(this.assetManager, "MatDefs/Planet/LogarithmicDepthBufferSimple.j3md");
        //oceanMaterial.setColor("Color", ColorRGBA.Blue);
        oceanMaterial.setBoolean("LogarithmicDepthBuffer", logarithmicDepthBuffer);
        planet.createOcean(oceanMaterial);
        
        // create atmosphere
        Material atmosphereMaterial = new Material(this.assetManager, "MatDefs/Planet/Atmosphere.j3md");
        float atmosphereRadius = radius + (radius * .05f);
        atmosphereMaterial.setColor("Ambient", new ColorRGBA(0.5f,0.5f,1f,1f));
        atmosphereMaterial.setColor("Diffuse", new ColorRGBA(0.5f,0.5f,1f,1f));
        atmosphereMaterial.setColor("Specular", new ColorRGBA(0.7f,0.7f,1f,1f));
        atmosphereMaterial.setFloat("Shininess", 1.0f);
        atmosphereMaterial.setBoolean("LogarithmicDepthBuffer", logarithmicDepthBuffer);
        

        planet.createAtmosphere(atmosphereMaterial, atmosphereRadius);

        return planet;
    }
    
    private Planet createMoonLikePlanet(float radius, float heightScale, int seed) {
        // Prepare planet material
        Material planetMaterial = new Material(this.assetManager, "MatDefs/Planet/Terrain.j3md");
        
        // region1 texture
        Texture region1 = this.assetManager.loadTexture("textures/moon.jpg");
        region1.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region1ColorMap", region1);
        planetMaterial.setVector3("Region1", new Vector3f(0, heightScale * 0.2f, 0));
        // region2 texture
        Texture region2 = this.assetManager.loadTexture("textures/moon.jpg");
        region2.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region2ColorMap", region2);
        planetMaterial.setVector3("Region2", new Vector3f(heightScale * 0.16f, heightScale * 0.88f, 0));
        // region3 texture
        Texture region3 = this.assetManager.loadTexture("textures/rock.jpg");
        region3.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region3ColorMap", region3);
        planetMaterial.setVector3("Region3", new Vector3f(heightScale * 0.84f, heightScale * 1.36f, 0));
        // region4 texture
        Texture region4 = this.assetManager.loadTexture("textures/rock.jpg");
        region4.setWrap(WrapMode.Repeat);
        planetMaterial.setTexture("Region4ColorMap", region4);
        planetMaterial.setVector3("Region4", new Vector3f(heightScale * 0.94f, heightScale * 1.5f, 0));

        // Turn on Logarithmic Depth Buffer to avoid z-fighting
        planetMaterial.setBoolean("LogarithmicDepthBuffer", false);
        
         // Create height data source
        FractalDataSource dataSource = new FractalDataSource(seed);
        dataSource.setHeightScale(heightScale);
        
        // create planet
        Planet planet = new Planet("Moon", radius, planetMaterial, dataSource);
        
        return planet;
    }

	

}
