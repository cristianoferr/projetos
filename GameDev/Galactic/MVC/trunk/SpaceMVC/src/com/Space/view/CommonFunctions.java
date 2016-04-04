package com.Space.view;

import javax.swing.ImageIcon;

import com.jme.image.Texture;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;

public class CommonFunctions {
	 public static Spatial applyTerrain(Sphere page) {

	        
//	        DirectionalLight dl = new DirectionalLight();
//	        dl.setDiffuse( new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f ) );
//	        dl.setDirection( new Vector3f( 1, -0.5f, 1 ) );
//	        dl.setEnabled( true );
//	        lightState.attach( dl );
	//
//	        DirectionalLight dr = new DirectionalLight();
//	        dr.setEnabled( true );
//	        dr.setDiffuse( new ColorRGBA( 1.0f, 1.0f, 1.0f, 1.0f ) );
//	        dr.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
//	        dr.setDirection( new Vector3f( 0.5f, -0.5f, 0 ) );
	//
//	        lightState.attach( dr );
	//
//	        display.getRenderer().setBackgroundColor( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1 ) );

	        FaultFractalHeightMap heightMap = new FaultFractalHeightMap( 257, 32, 0, 255,
	                0.75f, 3 );
	        heightMap.setHeightScale( 0.001f );

	        CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
	        cs.setCullMode( CullState.CS_BACK );
	        cs.setEnabled( true );
	        page.setRenderState( cs );


	        ProceduralTextureGenerator pt = new ProceduralTextureGenerator( heightMap );
	        pt.addTexture( new ImageIcon( View.class.getClassLoader().getResource(
	                "jmetest/data/texture/grassb.png" ) ), -128, 0, 128 );
	        pt.addTexture( new ImageIcon( View.class.getClassLoader().getResource(
	                "jmetest/data/texture/dirt.jpg" ) ), 0, 128, 255 );
	        pt.addTexture( new ImageIcon( View.class.getClassLoader().getResource(
	                "jmetest/data/texture/highest.jpg" ) ), 128, 255, 384 );

	        pt.createTexture( 512 );

	        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	        ts.setEnabled( true );
	        Texture t1 = TextureManager.loadTexture(
	                pt.getImageIcon().getImage(),
	                Texture.MM_LINEAR_LINEAR,
	                Texture.FM_LINEAR,
	                true );
	        ts.setTexture( t1, 0 );

	        Texture t2 = TextureManager.loadTexture( View.class.getClassLoader().
	                getResource(
	                "jmetest/data/texture/Detail.jpg" ),
	                Texture.MM_LINEAR_LINEAR,
	                Texture.FM_LINEAR );
	        ts.setTexture( t2, 1 );
	        t2.setWrap( Texture.WM_WRAP_S_WRAP_T );

	        t1.setApply( Texture.AM_COMBINE );
	        t1.setCombineFuncRGB( Texture.ACF_MODULATE );
	        t1.setCombineSrc0RGB( Texture.ACS_TEXTURE );
	        t1.setCombineOp0RGB( Texture.ACO_SRC_COLOR );
	        t1.setCombineSrc1RGB( Texture.ACS_PRIMARY_COLOR );
	        t1.setCombineOp1RGB( Texture.ACO_SRC_COLOR );
	        t1.setCombineScaleRGB( 1.0f );

	        t2.setApply( Texture.AM_COMBINE );
	        t2.setCombineFuncRGB( Texture.ACF_ADD_SIGNED );
	        t2.setCombineSrc0RGB( Texture.ACS_TEXTURE );
	        t2.setCombineOp0RGB( Texture.ACO_SRC_COLOR );
	        t2.setCombineSrc1RGB( Texture.ACS_PREVIOUS );
	        t2.setCombineOp1RGB( Texture.ACO_SRC_COLOR );
	        t2.setCombineScaleRGB( 1.0f );
	        page.setRenderState( ts );

//	        FogState fs = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
//	        fs.setDensity( 0.5f );
//	        fs.setEnabled( true );
//	        fs.setColor( new ColorRGBA( 0.5f, 0.5f, 0.5f, 0.5f ) );
//	        fs.setEnd( 1000 );
//	        fs.setStart( 500 );
//	        fs.setDensityFunction( FogState.DF_LINEAR );
//	        fs.setApplyFunction( FogState.AF_PER_VERTEX );
//	        page.setRenderState( fs );

	        return page;
	    }
}
