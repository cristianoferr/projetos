package galactic.view.demos;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.types.twodee.Sized2iRO;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.effects.EffectFactory;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.GLSLContext;
import org.xith3d.scenegraph.Geometry;
import org.xith3d.scenegraph.PointLight;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Sphere;
import org.xith3d.schedops.movement.RotatableGroup;
import org.xith3d.schedops.movement.TransformationDirectives;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.scenegraph.SkyBoxTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.ui.hud.HUD;
import org.xith3d.ui.hud.listeners.SliderListener;
import org.xith3d.ui.hud.widgets.Label;
import org.xith3d.ui.hud.widgets.Slider;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;


public class BloomTest extends Xith3DTest implements SliderListener//InputAdapterRenderLoop
{
    private Label label1, label2;
    
    @Override
    public void onKeyReleased( KeyReleasedEvent e, Key key )
    {
        switch ( key.getKeyID() )
        {
            case SPACE:
                break;
                
            case ESCAPE:
                this.end();
                break;
            case T:
            	EffectFactory.getInstance().getBloomFactory().setSceneWeight( (float)Math.random() );
            	
            	break;
            case R:
            	EffectFactory.getInstance().getBloomFactory().setBloomWeight( (float)Math.random() );
            	break;
        }
    }
    
    private BranchGroup createSceneGraph( Xith3DEnvironment env ) throws Exception
    {
        BranchGroup objRoot = new BranchGroup();
        
        RotatableGroup testRotateYGroup = new RotatableGroup( new TransformationDirectives( 0f, 0.01f, 0f ) );
        objRoot.addChild( testRotateYGroup );
        
        PointLight light = new PointLight( true, Colorf.GRAY30, new Point3f( -1f, 0.5f, 0.45f ), 0.5f );
        objRoot.addChild( light );
        
        // Make extra transfrom we may want to play with (scale etc.)
        TransformGroup sceneRootTransform = new TransformGroup();
        Transform3D t = new Transform3D();
        t.setIdentity();
        sceneRootTransform.setTransform( t );
        testRotateYGroup.addChild( sceneRootTransform );
        
        Texture texture = TextureLoader.getInstance().getTexture( "deathstar.jpg" );
        
        {
            Sphere sphere = new Sphere( 1.8f, 64, 64, Geometry.COORDINATES | Geometry.NORMALS | Geometry.TEXTURE_COORDINATES, false, 2 );
            Appearance a = new Appearance();
            a.setTexture( texture );
            //a.setColoringAttributes( new ColoringAttributes( Colorf.RED, ColoringAttributes.SHADE_GOURAUD ) );
            sphere.setAppearance( a );
            
            sceneRootTransform.addChild( sphere );
        }
        
        env.addPerspectiveBranch( objRoot );
        this.getAnimator().addAnimatableObject( testRotateYGroup );
        
        return ( objRoot );
    }
    
    public void onSliderValueChanged( Slider slider, int newValue )
    {
        if ( slider.getUserObject().equals( "SCENE_WEIGHT" ) )
        {
            float sceneWeight = newValue / 100f;
            EffectFactory.getInstance().getBloomFactory().setSceneWeight( sceneWeight );
            label1.setText( String.valueOf( newValue ) );
        }
        else if ( slider.getUserObject().equals( "BLOOM_WEIGHT" ) )
        {
            float blurWeight = newValue / 100f;
            EffectFactory.getInstance().getBloomFactory().setBloomWeight( blurWeight );
            label2.setText( String.valueOf( newValue ) );
        }
    }
    
    private void createHUD( Xith3DEnvironment env, Sized2iRO resolution )
    {
        // create the HUD
        HUD hud = new HUD( resolution, 1024f );
        
        Slider slider = new Slider( 265f );
        slider.setMinMaxAndValue( 0, 200, 200 );
        slider.setSmoothSliding( true );
        slider.addSliderListener( this );
        slider.setUserObject( "SCENE_WEIGHT" );
        
        hud.getContentPane().addWidget( slider, 32f, 580f );
        label1 = new Label( 50, 20f, "100", Colorf.WHITE );
        hud.getContentPane().addWidget( label1, 305f, 580f );
        
        slider = new Slider( 265f );
        slider.setMinMaxAndValue( 0, 200, 200 );
        slider.setSmoothSliding( true );
        slider.addSliderListener( this );
        slider.setUserObject( "BLOOM_WEIGHT" );
        hud.getContentPane().addWidget( slider, 32f, 620f );
        label2 = new Label( 50, 20f, "100", Colorf.WHITE );
        hud.getContentPane().addWidget( label2, 305f, 620f );
        
        
        // add the HUD to the SceneGraph
        env.addHUD( hud );
    }
    
    public BloomTest( BasicApplicationArguments arguments ) throws Throwable
    {
        super( arguments.getMaxFPS() );
        
        GLSLContext.setDebuggingEnabled( true );
        
        Xith3DEnvironment env = new Xith3DEnvironment( 0f, 2f, 3f,
                                                       0f, 0f, 0f,
                                                       0f, 1f, 0f,
                                                       this
                                                     );
        
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        
        resLoc.createAndAddTSL( "textures" );
        
        BranchGroup group = createSceneGraph( env );
        
      //  env.addRenderPass( SkyBoxTest.createSkyBox( resLoc.getResource( "skyboxes/" ), "normal" ) );
        
        Canvas3D canvas = createCanvas( arguments.getCanvasConstructionInfo(), getClass().getSimpleName() );
        env.addCanvas( canvas );
        
        canvas.addWindowClosingListener( new WindowClosingRenderLoopEnder( this ) );
        this.addFPSListener( new CanvasFPSListener( canvas ) );

        InputSystem.getInstance().registerNewKeyboardAndMouse( canvas.getPeer() );
        
        EffectFactory.getInstance().getBloomFactory().prepareForBloom( env, canvas, group );
        
        createHUD( env, arguments.getResolution() );
        
    }
    
    public static void main( String[] args ) throws Throwable
    {
        BloomTest test = new BloomTest( parseCommandLine( args ) );
        
        test.begin();
    }
}
