package cyclone.demos.xith3d;

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


import java.util.List;

import org.jagatoo.commands.Command;
import org.jagatoo.commands.CommandException;
import org.jagatoo.commands.CommandLine;
import org.jagatoo.commands.CommandProcessor;
import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.devices.components.Keys;
import org.jagatoo.input.events.KeyPressedEvent;
import org.jagatoo.logging.Log;
import org.jagatoo.logging.LogChannel;
import org.jagatoo.logging.LogLevel;
import org.openmali.FastMath;
import org.openmali.spatial.bodies.Plane;
import org.openmali.types.twodee.Sized2iRO;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.input.FirstPersonInputHandler;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.render.RenderPass;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.AmbientLight;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.Group;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.SceneGraph;
import org.xith3d.scenegraph.StaticTransform;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.View.CameraMode;
import org.xith3d.scenegraph.primitives.Line;
import org.xith3d.scenegraph.primitives.Rectangle;
import org.xith3d.scenegraph.primitives.Sphere;
import org.xith3d.scenegraph.primitives.Rectangle.ZeroPointLocation;
import org.xith3d.selection.BoundingBoxSelectable;
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
public class XithViewer extends Xith3DTest implements ConsoleListener, ButtonListener//InputAdapterRenderLoop
{
    private Canvas3D canvas;
    protected BranchGroup rootBranch;
    private HUD hud;
    private SelectionManager selectionManager = new SelectionManager();
    public Xith3DEnvironment env;
    public Label label1,label2;
    
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
        scene.addChild( new AmbientLight( Colorf.WHITE ) );
        
        topTransform = new TransformGroup( 0f, 0.5f, 0f );

        TransformGroup sphereTransform = new TransformGroup( 1f, -1f, 1f );
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
        
       */ Sphere sphere = new Sphere( 0.25f, 16, 16, "deathstar.jpg" );
        sphere.setName( "Sphere" );
        sphere.setUserData( Selectable.class, new BoundingBoxSelectable<Node>( sphere ) );
        sphereTransform.addChild( sphere );
        topTransform.addChild( sphereTransform );
        
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
        
        Panel panel = new Panel( true, 600f, 50f, new Colorf( 0.3f, 0.3f, 0.3f, 0.5f ) );
        panel.setLocation( 10f, 10f );
       //HUD hud = new HUD( canvasRes, 800f, panel );
        HUD hud = new HUD( canvasRes, 800f);
        
        sceneGraph.addHUD( hud );

        
        panel.setBorder( new EmptyBorder( 1 ) );
        
        GridLayout layout = new GridLayout( 2, 1 );
        layout.setColWeights( new float[] { 0.6f, 0.4f } );
        label1=new Label( 120f, 16f, "ViewPosition:", Colorf.WHITE );
        label2=new Label( 120f, 16f, "ViewDirection:", Colorf.WHITE );
        
        
        panel.setLayout( layout );
        panel.addWidget( label1 );
        panel.addWidget( label2 );
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
        
        this.console = new HUDConsole( hud.getResX(), 200f, logChannel.getID(), false );
        console.addConsoleListener( this );
        hud.getContentPane().addWidget( console );
        hud.getContentPane().addWidget( panel);
        Log.getLogManager().registerLog( console );
        
        commandProcessor.registerCommand( new ExitCommand( this ) );
        
        console.registerCommands( commandProcessor.getRegisteredCommands() );
        
        return ( hud );
    }
    
    
    public Group createGround()
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
    
    public XithViewer( BasicApplicationArguments arguments ) throws Throwable
    {
        super( arguments.getMaxFPS() );
        
        instance=this;
        env = new Xith3DEnvironment( 0f, 0f, 3f,
                                                       0f, 0f, 0f,
                                                       0f, 1f, 0f,
                                                       this
                                                     );
        
        resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL( "textures" );
        
        this.rootBranch = createSceneGraph();
        /*this.renderPass = */env.addPerspectiveBranch( rootBranch );
        
        this.hud = createHUD( arguments.getResolution(), env );
        
        RenderPass pass = RenderPass.createParallel( CameraMode.VIEW_FIXED );
        pass.getBranchGroup().addChild( createCrossHair() );
        env.addRenderPass( pass );
        
        this.canvas = createCanvas( arguments.getCanvasConstructionInfo(), getClass().getSimpleName() );
        env.addCanvas( canvas );
        
        canvas.addWindowClosingListener( new WindowClosingRenderLoopEnder( this ) );
        this.addFPSListener( new CanvasFPSListener( canvas ) );
        
        selectionManager.bind( rootBranch, canvas );
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
                }
            }
            
            public void selectionMoved( List<Selectable> selection, List<Selectable> selectedContext, Vector3f delta )
            {
                for ( int i = 0; i < selection.size(); i++ )
                {
                    Selectable selectable = selection.get( i );
                    Node node = selectable.getNode();
                    System.out.println( node.getName() + " moved" );
                    XithViewer.getInstance().selectionMoved(node);
                }
            }
        } );
        
        InputSystem.getInstance().registerNewKeyboardAndMouse( canvas.getPeer() );
        InputSystem.getInstance().getMouse().addMouseListener( selectionManager );
        hud.addPickMissedListener( SelectionManager.HUD_PICK_MISSED_MASK, selectionManager );
        
        moveHandler = new FirstPersonInputHandler( env.getView(), env.getCanvas(), 0.5f, 0.5f, arguments.getMouseYInverted(), 0.5f );
        moveHandler.setMouseMovementSuspended( true );
        moveHandler.getBindingsManager().createDefaultBindings();
        moveHandler.getBindingsManager().unbind( Keys.SPACE );
        InputSystem.getInstance().addInputHandler( moveHandler );
    }
    public void selectionMoved(Node node){
    	
    }
    
    
    public static void main( String[] args ) throws Throwable
    {
        XithViewer test = new XithViewer( parseCommandLine( args ) );
        
        test.begin();
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
}
