package cyclone.xith3d;

/**
 * Copyright (c) 2003-2010, Xith3D Project Group all rights reserved.
 * 
 * Portions based on the Java3D interface, Copyright by Sun Microsystems.
 * Many thanks to the developers of Java3D and Sun Microsystems for their
 * innovation and design.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of the 'Xith3D Project Group' nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
 * RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE
 */


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jagatoo.commands.Command;
import org.jagatoo.commands.CommandException;
import org.jagatoo.commands.CommandLine;
import org.jagatoo.commands.CommandProcessor;
import org.jagatoo.input.InputSystem;
import org.jagatoo.input.InputSystemException;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.devices.components.Keys;
import org.jagatoo.input.events.KeyPressedEvent;
import org.jagatoo.loaders.textures.locators.TextureStreamLocator;
import org.jagatoo.logging.Log;
import org.jagatoo.logging.LogChannel;
import org.jagatoo.logging.LogLevel;
import org.openmali.FastMath;
import org.openmali.types.twodee.Sized2iRO;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Vector3f;
import org.softmed.jops.ParticleManager;
import org.softmed.jops.ParticleSystem;
import org.softmed.jops.modifiers.PointMass;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.input.FirstPersonInputHandler;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.render.RenderPass;
import org.xith3d.render.config.DisplayMode.FullscreenMode;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.AmbientLight;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.Group;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.SceneGraph;
import org.xith3d.scenegraph.StaticTransform;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.View.CameraMode;
import org.xith3d.scenegraph.particles.jops.ParticleManagerUpdater;
import org.xith3d.scenegraph.primitives.Line;
import org.xith3d.scenegraph.primitives.Rectangle;
import org.xith3d.scenegraph.primitives.SkyBox;
import org.xith3d.scenegraph.primitives.Rectangle.ZeroPointLocation;
import org.xith3d.selection.HUDContextMenuProvider;
import org.xith3d.selection.Selectable;
import org.xith3d.selection.SelectionListener;
import org.xith3d.selection.SelectionManager;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.ui.hud.HUD;
import org.xith3d.ui.hud.base.AbstractButton;
import org.xith3d.ui.hud.borders.EmptyBorder;
import org.xith3d.ui.hud.layout.GridLayout;
import org.xith3d.ui.hud.listeners.ButtonListener;
import org.xith3d.ui.hud.widgets.Label;
import org.xith3d.ui.hud.widgets.Panel;
import org.xith3d.ui.hud.widgets.assemblies.ConsoleListener;
import org.xith3d.ui.hud.widgets.assemblies.HUDConsole;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.commands.ExitCommand;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description
(
    fulltext = { 
                   "Simple Xith3D Selection Framework (work in progress) Test"
               },
    authors = {
                  "Mathias Henze (aka cylab)"
              }
)
public class XithViewer extends Xith3DBasicClient implements ConsoleListener, ButtonListener//InputAdapterRenderLoop
{
    private Canvas3D canvas;
    protected BranchGroup rootBranch;
    
    //Particle System
    private ParticleManager pmanager = new ParticleManager();
    private ParticleSystem pmps;
    private static final boolean absoluteParticleSystem = false;
    private static final boolean useExternalPointMass = false;
    private static final boolean setPointMassToAllParticleSystems = false;
    private boolean showGeneratorsAndPointMasses = true;


   
	private HUD hud;
    private SelectionManager selectionManager = new CycloneSelectionManager();
    public Xith3DEnvironment env;
    public Label label1,label2,label3,label4;
    
    private SkyBox skyBox=null;
    
    private ResourceLocator resLoc;
    private FirstPersonInputHandler moveHandler;
    public TransformGroup topTransform;
    
    static XithViewer instance=null;
    public HUDConsole console;
    private final LogChannel logChannel = new LogChannel( "HUDConsoleTest" );
    
	private final CommandProcessor commandProcessor = new CommandProcessor( logChannel );
    private final CommandLine tmpCommandLine = new CommandLine();
    
    

    

    public void onCommandEntered( HUDConsole console, String commandLine )
    {
        console.println( "received command: " + commandLine );
        
        tmpCommandLine.parse( commandLine );
        
        Command command = commandProcessor.getRegisteredCommand( tmpCommandLine.getKey() );
        if ( command != null )
        {
            try
            {
                command.execute( null, tmpCommandLine );
            }
            catch ( CommandException e )
            {
                e.printStackTrace();
            }
        }
    }
    
	@Override
    public void onKeyPressed( KeyPressedEvent e, Key key )
    {
        switch ( key.getKeyID() )
        {
        case CIRCUMFLEX:
        case F5:
            //if ( !console.isPoppedUp() )
            //    console.println( "Console opened" );
            console.popUp( !console.isPoppedUp() );
            break;
            
            case SPACE:
                moveHandler.setMouseMovementSuspended( !moveHandler.isMouseMovementSuspended() );
                break;
            case ESCAPE:
                this.end();
                break;
        }
    }
    
    private BranchGroup createSceneGraph()
    {
        BranchGroup scene = new BranchGroup();
        //scene.addChild( new AmbientLight( Colorf.WHITE ) );
        
        topTransform = new TransformGroup( 0f, 0f, 0f );
        
//        Group g = new Group();
        
//        Rectangle floor = new Rectangle( 300f, 300f, ZeroPointLocation.CENTER_CENTER, "flat.jpg" );
//        floor.setName( "Floor" );
//        StaticTransform.rotateX( floor, -FastMath.PI_HALF );
//        
        //plane = new Plane( 0f, 1f, 0f, 0f );
        
  //      g.addChild( floor );
        
        //topTransform.addChild(g);
        
        

        //TransformGroup sphereTransform = new TransformGroup( 1f, -1f, 1f );
       /* TransformGroup cubeTransform = new TransformGroup( -1f, -1f, 1f );
      
        TransformGroup modelTransform = new TransformGroup( 0f, -1f, -1f );
        */
      /*  Cube cube = new Cube( 0.5f, "jplogo.jpg" );
        cube.setName( "Cube" );
        final BoundingBoxSelectable<Node> cubeSelectable = new BoundingBoxSelectable<Node>( cube );
        cubeSelectable.register( new MenuAction( "Action1" )
        {
            @Override
            public void onActionPerformed()
            {
                //JOptionPane.showMessageDialog( null, "Clicked " + this.getName() );
                MsgBox.show( "Clicked " + this.getName(), hud );
            }
        } );
        cubeSelectable.register( new MenuAction( "Action2" )
        {
            @Override
            public void onActionPerformed()
            {
                //JOptionPane.showMessageDialog( null, "Clicked " + this.getName() );
                MsgBox.show( "Clicked " + this.getName(), hud );
            }
        } );
        cubeSelectable.register( MenuAction.SEPARATOR );
        cubeSelectable.register( new MenuAction( "Action3" )
        {
            @Override
            public void onActionPerformed()
            {
                //JOptionPane.showMessageDialog( null, "Clicked " + this.getName() );
                MsgBox.show( "Clicked " + this.getName(), hud );
            }
        } );
        cube.setUserData( Selectable.class,cubeSelectable );
        cubeTransform.addChild( cube );
        topTransform.addChild( cubeTransform );
        
       */
        /*Sphere sphere = new Sphere( 0.25f, 16, 16, "deathstar.jpg" );
        sphere.setName( "Sphere" );
        sphere.setUserData( Selectable.class, new BoundingBoxSelectable<Node>( sphere ) );
        sphereTransform.addChild( sphere );
        topTransform.addChild( sphereTransform );*/
        
      /*  try
        {
            Model model = ModelLoader.getInstance().loadModel( resLoc.getResource( "models/tris.md2" ), "marvin.pcx", 0.04f * 0.5f );
            TransformGroup tg = new TransformGroup();
            tg.getTransform().setRotation( new AxisAngle3f( Vector3f.POSITIVE_Y_AXIS, -FastMath.PI_HALF ) );
            tg.addChild( model );
            model.setCurrentAnimation( "salute" );
            model.setUserData( Selectable.class, new BoundingBoxSelectable<Node>( model ) );
            modelTransform.addChild( tg );
            topTransform.addChild( modelTransform );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }*/
        
        scene.addChild( topTransform );
        
        return ( scene );
    }
    
    private Group createCrossHair()
    {
        Group crossHair = new Group();
        float o1 = 0.05f;
        float o2 = 0.034f;
        float i1 = 0.02f;
        float i2 = 0.01f;
        Colorf oc = Colorf.GREEN;
        Colorf ic = Colorf.LIGHT_GRAY;
        Colorf cc = Colorf.WHITE;
        Line[] lines = new Line[]
        {
             new Line( new Point3f( -o1,  o1, 0 ), new Point3f( -o2,  o1, 0 ), oc ),
             new Line( new Point3f(  o1,  o1, 0 ), new Point3f(  o2,  o1, 0 ), oc ),
             new Line( new Point3f( -o1,  o1, 0 ), new Point3f( -o1,  o2, 0 ), oc ),
             new Line( new Point3f(  o1,  o1, 0 ), new Point3f(  o1,  o2, 0 ), oc ),
             new Line( new Point3f( -o1, -o1, 0 ), new Point3f( -o2, -o1, 0 ), oc ),
             new Line( new Point3f(  o1, -o1, 0 ), new Point3f(  o2, -o1, 0 ), oc ),
             new Line( new Point3f( -o1, -o1, 0 ), new Point3f( -o1, -o2, 0 ), oc ),
             new Line( new Point3f(  o1, -o1, 0 ), new Point3f(  o1, -o2, 0 ), oc ),
             
             new Line( new Point3f( -i1,  i1, 0 ), new Point3f( -i2,  i1, 0 ), ic ),
             new Line( new Point3f(  i1,  i1, 0 ), new Point3f(  i2,  i1, 0 ), ic ),
             new Line( new Point3f( -i1,  i1, 0 ), new Point3f( -i1,  i2, 0 ), ic ),
             new Line( new Point3f(  i1,  i1, 0 ), new Point3f(  i1,  i2, 0 ), ic ),
             new Line( new Point3f( -i1, -i1, 0 ), new Point3f( -i2, -i1, 0 ), ic ),
             new Line( new Point3f(  i1, -i1, 0 ), new Point3f(  i2, -i1, 0 ), ic ),
             new Line( new Point3f( -i1, -i1, 0 ), new Point3f( -i1, -i2, 0 ), ic ),
             new Line( new Point3f(  i1, -i1, 0 ), new Point3f(  i1, -i2, 0 ), ic ),
             
             new Line( new Point3f( -i2, 0, 0 ), new Point3f( i2, 0, 0 ), cc ),
             new Line( new Point3f( 0, -i2, 0 ), new Point3f( 0, i2, 0 ), cc )
        };
        
        for ( int i = 0; i < lines.length; i++ )
        {
            crossHair.addChild( lines[ i ] );
        }
        
        return ( crossHair );
    }
    
    private HUD createHUD( Sized2iRO canvasRes, SceneGraph sceneGraph )
    {
        
        HUD hud = new HUD( canvasRes, 800f);
        sceneGraph.addHUD( hud );

    	Panel panel = createPanel();
    	
    	
        
        this.console = new HUDConsole( hud.getResX(), 200f, logChannel.getID(), false );
        console.addConsoleListener( this );
        hud.getContentPane().addWidget( console );
        hud.getContentPane().addWidget( panel);
        Log.getLogManager().registerLog( console );
        
        commandProcessor.registerCommand( new ExitCommand( this ) );
        
        console.registerCommands( commandProcessor.getRegisteredCommands() );
        
        return ( hud );
    }

	private Panel createPanel() {
		Panel panel = new Panel( true, 600f, 50f, new Colorf( 0.3f, 0.3f, 0.3f, 0.5f ) );
        panel.setLocation( 10f, 10f );
       //HUD hud = new HUD( canvasRes, 800f, panel );

        
        panel.setBorder( new EmptyBorder( 1 ) );
        
        GridLayout layout = new GridLayout( 2, 2 );
        layout.setColWeights( new float[] { 0.3f, 0.2f } );
        label1=new Label( 120f, 16f, "", Colorf.WHITE );
        label2=new Label( 120f, 16f, "", Colorf.WHITE );
        label3=new Label( 120f, 16f, "", Colorf.WHITE );
        label4=new Label( 120f, 16f, "", Colorf.WHITE );
        //label1.setAutoSizeEnabled(true);
        
        panel.setLayout( layout );
        panel.addWidget( label1 );
        panel.addWidget( label2 );
        panel.addWidget( label3 );
        panel.addWidget( label4 );
        //panel.addWidget( /*viewDirectionLbl =*/ new Label( 100f, 16f, "0.00", Colorf.WHITE ) );
     /*   panel.addWidget( new Label( 120f, 16f, "MovementSpeed (MW):", Colorf.WHITE ) );
        panel.addWidget( movementSpeedLbl = new Label( 100f, 16f, "", Colorf.WHITE ) ); movementSpeedLbl.setText( movementSpeed );
        panel.addWidget( new Label( 120f, 16f, "MovementType (f):", Colorf.WHITE ) );
        panel.addWidget( movementTypeLbl = new Label( 100, 16, isFlying ? "flying" : "walking", Colorf.WHITE ) );
        panel.addWidget( new Label( 120f, 16f, "TerrainHeight:", Colorf.WHITE ) );
        panel.addWidget( currentHeightLbl = new Label( 100f, 16f, "0.00", Colorf.WHITE ) );
        panel.addWidget( new Label( 120f, 16f, "FPS [min;curM;max] (r):", Colorf.WHITE ) );
        panel.addWidget( fpsLbl = new Label( 100f, 16f, "0.00", Colorf.WHITE ) );
        */
		return panel;
	}
    
	private void setupBackground() {

        Rectangle quad = new Rectangle(200, 200, TextureLoader.getFallbackTexture());

        TransformGroup transformGroup = new TransformGroup();
        transformGroup.addChild(quad);
        transformGroup.setTransform(new Transform3D(0f, 0f, -5f));

        BranchGroup bg = new BranchGroup();
        bg.addChild(transformGroup);

        env.addParallelBranch(bg);
    }

    
    private Group createGround()
    {
        Group g = new Group();
        
        Rectangle floor = new Rectangle( 300f, 300f, ZeroPointLocation.CENTER_CENTER, "stone.jpg" );
        floor.setName( "Floor" );
        StaticTransform.rotateX( floor, -FastMath.PI_HALF );
        //Plane plane;
       // plane = new Plane( 0f, -1f, 0f, 0f );
        
        g.addChild( floor );
        
        return ( g );
    }
    
    public XithViewer( BasicApplicationArguments arguments,String resPath ) throws Throwable
    {
        super( arguments.getMaxFPS() );
        
        arguments.setResolution(800, 600);
        //arguments.setResolution(1180, 1024);
        //arguments.setResolution(1920, 1080);
       // arguments.setFullscreenMode(FullscreenMode.FULLSCREEN);        
        instance=this;
        env = new Xith3DEnvironment( -15f, 25f, -15f,
                                                       0f, 20f, 0f,
                                                       0f, 1f, 0f,
                                                       this
                                                     );
      try {  
       setResLoc(ResourceLocator.create( resPath +  "/assets" ));
      }
      catch (Exception e){
    	  setResLoc(TestUtils.createResourceLocator());
      }
       // setResLoc(TestUtils.createResourceLocator());
        getResLoc().createAndAddTSL( "textures" );
        
      
        
        

        
        this.rootBranch = createSceneGraph();
        /*this.renderPass = */env.addPerspectiveBranch( rootBranch );
        
        createBackGround();
        
        this.hud = createHUD( arguments.getResolution(), env );
        
        createGround();
        
        RenderPass pass = RenderPass.createParallel( CameraMode.VIEW_FIXED );
        pass.getBranchGroup().addChild( createCrossHair() );
        env.addRenderPass( pass );
        
        this.setCanvas(createCanvas( arguments.getCanvasConstructionInfo(), getClass().getSimpleName() ));
        env.addCanvas( getCanvas() );
        
/*        Frustum frustum =new Frustum();
        frustum.set*/
        createListeners();
        
        initParticleSystem();
        
        moveHandler = new FirstPersonInputHandler( env.getView(), env.getCanvas(), 0.5f, 0.5f, arguments.getMouseYInverted(), 0.5f );
        moveHandler.setMouseMovementSuspended( true );
       //moveHandler.getBindingsManager().createDefaultBindings();
        moveHandler.getBindingsManager().unbind( Keys.SPACE );
        InputSystem.getInstance().addInputHandler( moveHandler );
        
        
    }

    public String getPSHandle( String name )
    {
		String handle=null;
        try
        {
                handle = getPmanager().load( getResLoc().getResource( "jops/ps/" + name + ".ops" ) );
            
            return ( handle );
        }
        catch ( Throwable t )
        {
            t.printStackTrace();
            
            return ( null );
        }
    }
	
    
    private final void initParticleSystem(  ) throws Exception
    {
        //pmanager.getSystems().add( ps );
        
        pmps = new ParticleSystem();
        pmps.setLimited( false );
        PointMass pm = new PointMass();
        pm.getStrength().addValue( -9f, 0f );
        pm.setThreshold( false );
        pm.getThresholdValue().addValue( 1f, 0f );
        pm.setDistanceVariation( PointMass.LINEAR );
        pm.setDistanceProportion( PointMass.INVERSE );
        
        if ( useExternalPointMass )
        {
            pmps.getModifiers().add( pm );
//            ps.addInternalModifiersAsExternal( pmps );
            pmanager.getSystems().add( pmps );
        }
        //rootBranch.addChild( psn );
        
      //  sg.addPerspectiveBranch( createScene( ps, getAnimator() ) );
        getOperationScheduler().scheduleOperation( new ParticleManagerUpdater( pmanager ) );
    }
    
	private void createBackGround() throws MalformedURLException {
		// setupBackground();
		//    skyBox=createSkyBox( resLoc.getResource( "skyboxes/" ), "normal" );
		    //skyBox.getBranchGroup().addChild(child)
		 //   BranchGroup branchGroup=skyBox.getBranchGroup();
	//	env.addRenderPass( skyBox );
		
/*		    float radius=280000,pos=920000;
		    
		    Sphere sphere = new Sphere( radius, 16, 16, new Colorf((float)Math.random()*1,(float)Math.random()*1,(float)Math.random()*1) );
		    sphere.setName( "Sphere2" );
		    sphere.setUserData( Selectable.class, new BoundingBoxSelectable<Node>( sphere ) );
		    TransformGroup sphereTransform = new TransformGroup( pos, 0f, 0f );
		    sphereTransform.addChild( sphere );
		    
		    float radius2=radius/10;
		    float pos2=pos/10;
		    
		    Sphere sphere2 = new Sphere( radius2, 160, 160, new Colorf((float)Math.random()*1,(float)Math.random()*1,(float)Math.random()*1) );
		    sphere2.setName( "Sphere3" );
		    sphere2.setUserData( Selectable.class, new BoundingBoxSelectable<Node>( sphere2 ) );
		    TransformGroup sphereTransform2 = new TransformGroup( pos2, 0, 0f );
		    sphereTransform2.addChild( sphere2 );
		    //branchGroup.addChild( sphereTransform );
		//    branchGroup.setPickableRecursive(true);
		 //   branchGroup.
		    
		    float radius3=radius2/10;
		    float pos3=pos2/10;
		    
		    Sphere sphere3 = new Sphere( radius3, 160, 160, new Colorf((float)Math.random()*1,(float)Math.random()*1,(float)Math.random()*1) );
		    sphere3.setName( "Sphere4" );
		    sphere3.setUserData( Selectable.class, new BoundingBoxSelectable<Node>( sphere3 ) );
		    TransformGroup sphereTransform3 = new TransformGroup( pos3, 0, 0f );
		    sphereTransform3.addChild( sphere3 );
		    //branchGroup.addChild( sphereTransform );
		    
		    //branchGroup.removeChild(sphereTransform2);
	        
	        
	        rootBranch.addChild(sphereTransform);
	        rootBranch.addChild( sphereTransform2 );
	        rootBranch.addChild(sphereTransform3);*/
	        
	        
	        env.getView().setFrontClipDistance(1);
	        env.getView().setBackClipDistance(200000000);
	}

	private void createListeners() throws InputSystemException {
		getCanvas().addWindowClosingListener( new WindowClosingRenderLoopEnder( this ) );
        this.addFPSListener( new CanvasFPSListener( getCanvas() ) );
        
        selectionManager.bind( rootBranch, getCanvas() );
        //selectionManager.setContextMenuProvider( new AWTContextMenuProvider( (java.awt.Component)canvas.getPeer().getComponent() ) );
        selectionManager.setContextMenuProvider( new HUDContextMenuProvider( hud ) );
        selectionManager.addSelectionListener( new SelectionListener()
        {
            public void selectionChanged( List<Selectable> selection, List<Selectable> selectedContext )
            {
                for ( int i = 0; i < selection.size(); i++ )
                {
                    Selectable selectable = selection.get( i );
                    Node node = selectable.getNode();
                    System.out.println( "Selected " + node.getName() );
                    XithViewer.getInstance().newSelection(node);
                    
                }
            }
            
            public void selectionMoved( List<Selectable> selection, List<Selectable> selectedContext, Vector3f delta )
            {
            	
            	
                for ( int i = 0; i < selection.size(); i++ )
                {
                    Selectable selectable = selection.get( i );
                    Node node = selectable.getNode();
/*                    Point3f pos=node.getTransformGroup().getPosition();
                    node.getTransformGroup().getPosition().set(pos.getX()-delta.getX(), pos.getY()-delta.getY(), pos.getZ()-delta.getZ());*/
                    System.out.println( node.getName() + " moved" );
                    //XithViewer.getInstance().selectionMoved(node);
                }
            }
        } );
        
        InputSystem.getInstance().registerNewKeyboardAndMouse( getCanvas().getPeer() );
        InputSystem.getInstance().getMouse().addMouseListener( selectionManager );
        hud.addPickMissedListener( SelectionManager.HUD_PICK_MISSED_MASK, selectionManager );
	}
    
 public void newSelection(Node node){
	 
    }
    public void selectionMoved(Node node){
    	
    }
    
    
    public static void main( String[] args ) throws Throwable
    {
        XithViewer test = new XithViewer( parseCommandLine( args ),"." );
        
        test.begin();
    }
    
    
    public final void init()
    {
        begin( RunMode.RUN_IN_SEPARATE_THREAD, TimingMode.MICROSECONDS );

        while (true){
        	try {
        		run();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    /**
     * Creates a SkyBox.
     */
    public static SkyBox createSkyBox( URL baseURL, String name )
    {
        final long t0 = TestUtils.dumpAction( "Creating SkyBox from \"" + baseURL + name + "\"..." );
        
        TextureStreamLocator tsl = TextureLoader.getInstance().addTextureStreamLocator( baseURL );
        
        final String ext = check( baseURL, name ) ? "png" : "jpg";
        
        SkyBox sb = new SkyBox( name + "/front." + ext,
                                name + "/right." + ext,
                                name + "/back." + ext,
                                name + "/left." + ext,
                                name + "/top." + ext,
                                name + "/bottom." + ext );
        
        TextureLoader.getInstance().removeTextureStreamLocator( tsl );
        
        TestUtils.dumpDoneIn( t0 );
        
        return ( sb );
    }
    
    private static boolean check( URL baseURL, String name )
    {
        URL url = null;
        try
        {
            url = new URL( baseURL, name + "/front.png" );
        }
        catch ( IOException e )
        {
            return ( false );
        }
        
        try
        {
            url.openStream();
        }
        catch ( IOException e )
        {
            return ( false );
        }
        
        return ( true );
    }    
    /**
	 * @return the instance
	 */
	public static XithViewer getInstance() {
		return instance;
	}

	@Override
	public void onButtonClicked(AbstractButton button, Object userObject) {
	/*	  if ( userObject == REGULAR )
	        {
	            Log.println( logChannel, LogLevel.REGULAR, "This is a random log message (" + System.currentTimeMillis() + ")" );
	        }
	        else if ( userObject == EXCEPTION )
	        {
	            Log.println( logChannel, LogLevel.EXCEPTION, "This is a random log message (" + System.currentTimeMillis() + ")" );
	        }
	        else if ( userObject == ERROR )
	        {
	            Log.println( logChannel, LogLevel.ERROR, "This is a random log message (" + System.currentTimeMillis() + ")" );
	        }*/
		
	}

	public static void printLog(String s,boolean debug){
		System.out.println(s);
		if (instance!=null){
			
			if (debug)
				Log.println( XithViewer.getLogChannel(), LogLevel.EXCEPTION, s );
			else
				Log.println( XithViewer.getLogChannel(), LogLevel.REGULAR, s );
		}
	}
	
	public String getLabel1() {
		return label1.getText();
	}

	public void setLabel1(String s) {
		label1.setText(s);
	}

	public String getLabel2() {
		return label2.getText();
	}

	public String getLabel3() {
		return label2.getText();
	}
	public void setLabel2(String s) {
		label2.setText(s);
	}
	public void setLabel3(String s) {
		label3.setText(s);
	}
	
	public String getLabel4() {
		return label4.getText();
	}
	public void setLabel4(String s) {
		label4.setText(s);
	}
	 public BranchGroup getRootBranch() {
			return rootBranch;
		}	
	 
	 public static LogChannel getLogChannel() {
			return instance.logChannel;
		}

	public void setResLoc(ResourceLocator resLoc) {
		this.resLoc = resLoc;
	}

	public ResourceLocator getResLoc() {
		return resLoc;
	}

	public SkyBox getSkyBox() {
		return skyBox;
	}

	public void setSkyBox(SkyBox skyBox) {
		this.skyBox = skyBox;
	}
	
	public ParticleManager getPmanager() {
		return pmanager;
	}

	public void setCanvas(Canvas3D canvas) {
		this.canvas = canvas;
	}

	public Canvas3D getCanvas() {
		return canvas;
	}
}
