package galactic.view.demos;


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


import java.util.ArrayList;
import java.util.Iterator;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.devices.components.Keys;
import org.jagatoo.input.devices.components.MouseButton;
import org.jagatoo.input.devices.components.MouseButtons;
import org.jagatoo.input.events.KeyPressedEvent;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.jagatoo.input.events.MouseButtonPressedEvent;
import org.openmali.types.twodee.Sized2iRO;
import org.openmali.vecmath2.Colorf;
import org.softmed.jops.ParticleManager;
import org.softmed.jops.ParticleSystem;
import org.softmed.jops.modifiers.PointMass;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.input.FirstPersonInputHandler;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.loop.opscheduler.Animator;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResLoc;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.SceneGraph;
import org.xith3d.scenegraph.StaticTransform;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.particles.jops.AbsoluteParticleSystemNode;
import org.xith3d.scenegraph.particles.jops.ParticleManagerUpdater;
import org.xith3d.scenegraph.particles.jops.ParticleSystemNode;
import org.xith3d.scenegraph.particles.jops.RelativeParticleSystemNode;
import org.xith3d.scenegraph.primitives.Sphere;
import org.xith3d.schedops.movement.RotatableGroup;
import org.xith3d.schedops.movement.TransformationDirectives;
import org.xith3d.schedops.movement.TranslatableGroup;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.ui.hud.HUD;
import org.xith3d.ui.hud.base.AbstractButton;
import org.xith3d.ui.hud.base.Border;
import org.xith3d.ui.hud.layout.BorderLayout;
import org.xith3d.ui.hud.layout.ListLayout;
import org.xith3d.ui.hud.listeners.ButtonListener;
import org.xith3d.ui.hud.listeners.HUDPickMissedListener;
import org.xith3d.ui.hud.listmodels.TextListModel;
import org.xith3d.ui.hud.utils.HUDPickResult.HUDPickReason;
import org.xith3d.ui.hud.widgets.Button;
import org.xith3d.ui.hud.widgets.ComboBox;
import org.xith3d.ui.hud.widgets.List;
import org.xith3d.ui.hud.widgets.Panel;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description
(
    fulltext = {
                   "A testcase for Xith3D's abstraction of the \"Java Open Particle System (jops)\""
               },
    authors = {
                  "Guilherme Gomes (aka guilhermegrg)"
              }
)
public class ParticleSystemTest extends Xith3DTest /*InputAdapterRenderLoop*/ implements HUDPickMissedListener
{
    private static final boolean absoluteParticleSystem = false;
    private static final boolean useExternalPointMass = false;
    private static final boolean setPointMassToAllParticleSystems = false;
    private boolean showGeneratorsAndPointMasses = true;
    
    private FirstPersonInputHandler fpih;
    
    // TODO go up one example
    // TODO go down one example
    // TODO build one from current example
    
    // TODO clear all
    
    private BranchGroup scene;
    
    private ParticleManager pmanager = new ParticleManager();
    
    private String currentHandleName = null;
    private String handle;
    private ParticleSystem pmps;
    
    private HUD hud;
    
    private final ArrayList< ParticleSystemNode > particleSystemNodes = new ArrayList< ParticleSystemNode >();
    
    private List list;
    
    @Override
    public void onKeyPressed( KeyPressedEvent e, Key key )
    {
        switch ( key.getKeyID() )
        {
            case Y:
                fpih.flipMouseYAxis();
                break;
        }
    }
    
    private String getPSHandle( String name )
    {
        try
        {
            if ( !name.equals( currentHandleName ) )
            {
                handle = pmanager.load( ResLoc.getResource( "ps/" + name + ".ops" ) );
                currentHandleName = name;
            }
            
            return ( handle );
        }
        catch ( Throwable t )
        {
            t.printStackTrace();
            
            return ( null );
        }
    }
    
    private void addNewParticleSystem( String name )
    {
        try
        {
            String handle = getPSHandle( name );
            
            if ( handle == null )
                return;
            
            ParticleSystem ps = pmanager.getCopyAttached( handle );
            ParticleSystemNode psn = null;
            
            if ( absoluteParticleSystem )
                psn = new AbsoluteParticleSystemNode();
            else
                psn = new RelativeParticleSystemNode();
            
            particleSystemNodes.add( psn );
            TextListModel model = (TextListModel)list.getModel();
            int index = model.addItem( currentHandleName );
            model.setItemUserObject( index, psn );
            model.markListDirty();
            scene.addChild( psn );
            
            if ( setPointMassToAllParticleSystems )
                ps.addInternalModifiersAsExternal( pmps );
            
            psn.setShowGeneratorsAndPointMasses( showGeneratorsAndPointMasses );
            
            psn.setParticleSystem( ps );
        }
        catch ( Throwable t )
        {
            t.printStackTrace();
        }
    }
    
    @Override
    public void onKeyReleased( KeyReleasedEvent e, Key key )
    {
        switch ( key.getKeyID() )
        {
            case Q:
                showGeneratorsAndPointMasses = !showGeneratorsAndPointMasses;
                ParticleSystemNode psnode = null;
                for ( Iterator< ParticleSystemNode > it = particleSystemNodes.iterator(); it.hasNext(); psnode = it.next() )
                {
                    if ( psnode.getRoot() == null )
                    {
                        it.remove();
                        continue;
                    }
                    
                    if ( psnode.getParticleSystem().isAlive() )
                        psnode.setShowGeneratorsAndPointMasses( showGeneratorsAndPointMasses );
                }
                break;
                
            case ESCAPE:
                this.end();
                break;
        }
    }
    
    private long lastSuspendTime = -1L;
    
    @Override
    public void onMouseButtonPressed( MouseButtonPressedEvent e, MouseButton button )
    {
        if ( !fpih.isSuspended() && ( button == MouseButtons.RIGHT_BUTTON ) )
        {
            fpih.setSuspended( true );
            lastSuspendTime = e.getWhen();
        }
    }
    
    public void onHUDPickMissed( MouseButton button, int x, int y, HUDPickReason pickReason, long when, long meta )
    {
        if ( button == MouseButtons.RIGHT_BUTTON )
        {
            if ( when != lastSuspendTime )
                fpih.setSuspended( false );
        }
    }
    
    private BranchGroup createScene( ParticleSystem ps, Animator animator )
    {
        scene = new BranchGroup();
        
        ParticleSystemNode psn;
        if ( absoluteParticleSystem )
            psn = new AbsoluteParticleSystemNode();
        else
            psn = new RelativeParticleSystemNode();
        
        TransformationDirectives rotDirecs = new TransformationDirectives( 0.0f, 0.0f, 0.0f );
        
        TransformationDirectives transDirecs = new TransformationDirectives( -0.0f, 0.0f, 0.0f );
        
        TranslatableGroup trans = new TranslatableGroup( transDirecs );
        animator.addAnimatableObject( trans );
        
        RotatableGroup rg = new RotatableGroup( rotDirecs );
        trans.addChild( rg );
        rg.addChild( psn );
//        animator.addAnimatableObject( rg );
        
        TransformGroup trans2 = new TransformGroup();
        trans2.addChild( trans );
        
        RotatableGroup rg2 = new RotatableGroup( rotDirecs );
        rg2.addChild( trans2 );
        animator.addAnimatableObject( rg2 );
        
        scene.addChild( rg2 );
        
        psn.setParticleSystem( ps );
        psn.setShowGeneratorsAndPointMasses( showGeneratorsAndPointMasses );
        TextListModel model = (TextListModel)list.getModel();
        int index = model.addItem( currentHandleName );
        model.setItemUserObject( index, rg2 );
        model.markListDirty();
        
        Sphere sphere4 = new Sphere( 0.1f, 5, 5, Colorf.BLUE );
        StaticTransform.translate( sphere4, -0.9f, 0f, 0f );
        scene.addChild( sphere4 );
        
        AbsoluteParticleSystemNode pmpsn = new AbsoluteParticleSystemNode();
        
        TransformGroup trans6 = new TransformGroup( new Transform3D( 10.0f, 0f, 0f ) );
        if (useExternalPointMass)
            trans6.addChild( pmpsn );
        
        RotatableGroup rg10 = new RotatableGroup( new TransformationDirectives( 0.0f, 0.1f, 0.0f ) );
        rg10.addChild( trans6 );
        animator.addAnimatableObject( rg10 );
        
        TransformGroup trans5 = new TransformGroup( 10f, 1.8f, 0f );
        trans5.addChild( rg10 );
        
        scene.addChild( trans5 );
        pmpsn.setParticleSystem( pmps );
        pmpsn.setShowGeneratorsAndPointMasses( true );
        
        return ( scene );
    }
    
    private final void initParticleSystem( String initialPSName, SceneGraph sg ) throws Exception
    {
        String handle = getPSHandle( initialPSName );
        ParticleSystem ps = pmanager.getCopyAttached( handle );
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
            ps.addInternalModifiersAsExternal( pmps );
            pmanager.getSystems().add( pmps );
        }
        
        sg.addPerspectiveBranch( createScene( ps, getAnimator() ) );
        getOperationScheduler().scheduleOperation( new ParticleManagerUpdater( pmanager ) );
    }
    
    private void createHUD( Sized2iRO canvasRes, SceneGraph sceneGraph )
    {
        Panel main = new Panel( true, 170f, 600f, new Colorf( 1f, 1f, 1f, 0.5f ) );
        main.setLayout( new BorderLayout( 3f, 3f, 3f, 3f, 3f, 3f ) );
        
        hud = new HUD( canvasRes, 800f, main );
        
        //Panel main = new Panel( 170f, 600f, new Colorf( 1f, 1f, 1f, 0.5f ) );
        //main.setLayout( new BorderLayout( 3f, 3f, 3f, 3f, 3f, 3f ) );
        
        Panel pnlAdd = new Panel( 164f, 64f );
        pnlAdd.setBorder( new Border.Description( 6, 6, 6, 6, "border-black.png" ) );
        pnlAdd.setLayout( new ListLayout( ListLayout.Orientation.VERTICAL, 3f, 3f, 3f, 3f, 3f ) );
        main.addWidget( pnlAdd, BorderLayout.Area.NORTH );
        
        final ComboBox combo = ComboBox.newTextCombo( 170f, 22f );
        combo.addItem( "9candles" );
        combo.addItem( "blowout" );
        combo.addItem( "candle" );
        combo.addItem( "catia" );
        combo.addItem( "coloredSquares" ); // doesn't work!
        combo.addItem( "flowerPower" );
        combo.addItem( "greenStuff" );
        combo.addItem( "lightningBall" );
        combo.addItem( "pentagram" );
        combo.addItem( "shockRifleBall" );
        combo.addItem( "snow" ); // looks strange. Wrong camera?
        combo.addItem( "spiral3" );
        combo.addItem( "strange" );
        combo.addItem( "tornado" );
        combo.addItem( "tutorialPointMass2" );
        combo.addItem( "tutorialPointMasses2" );
        
        combo.setSelectedIndex( combo.findItem( "tornado" ) );
        
        pnlAdd.addWidget( combo );
        
        Button btnAdd = new Button( 170f, 22f, "Add" );
        btnAdd.addButtonListener( new ButtonListener()
        {
            public void onButtonClicked( AbstractButton button, Object userObject )
            {
                addNewParticleSystem( combo.getSelectedItem().toString() );
            }
        } );
        
        pnlAdd.addWidget( btnAdd );
        
        Panel pnlRemove = new Panel( 10f, 10f );
        pnlRemove.setBorder( new Border.Description( 6, 6, 6, 6, "border-black.png" ) );
        pnlRemove.setLayout( new BorderLayout( 3f, 3f, 3f, 3f, 3f, 3f ) );
        main.addWidget( pnlRemove, BorderLayout.Area.CENTER );
        
        list = List.newTextList( 170f, 200f );
        list.setAddItemSetsSelectedItem( true );
        
        pnlRemove.addWidget( list, BorderLayout.Area.CENTER );
        
        Button btnRemove = new Button( 170f, 22f, "Remove" );
        btnRemove.addButtonListener( new ButtonListener()
        {
            public void onButtonClicked( AbstractButton button, Object userObject )
            {
                TextListModel model = (TextListModel)list.getModel();
                
                int selIdx = model.getSelectedIndex();
                
                if ( selIdx < 0 )
                    return;
                
                Node node = (Node)model.getItemUserObject( selIdx );
                //System.out.println( "removing: " + node );
                
                if ( node != null )
                {
                    scene.removeChild( node );
                    //particleSystemNodes.remove( psn );
                    model.removeItem( selIdx );
                    model.markListDirty();
                }
            }
        } );
        
        pnlRemove.addWidget( btnRemove, BorderLayout.Area.SOUTH );
        
        //hud.getContentPane().addWidget( main );
        
        sceneGraph.addHUD( hud );
        
        hud.addPickMissedListener( HUDPickReason.BUTTON_PRESSED_MASK/* | HUDPickReason.BUTTON_RELEASED_MASK*/, this );
    }
    
    public ParticleSystemTest( BasicApplicationArguments arguments ) throws Throwable
    {
        super( arguments.getMaxFPS() );
        
        //GeneratorShape3D.setWireFrameEnabled( true );
        
        Xith3DEnvironment env = new Xith3DEnvironment( 0f, 0f, 5f,
                                                       0f, 0f, 0f,
                                                       0f, 1f, 0f,
                                                       this
                                                     );
        
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL( "HUD" );
        
        resLoc = resLoc.getSubLocator( "jops/" );
        resLoc.useAsSingletonInstance();
        resLoc.createAndAddTSL( "textures" );
        
        final String initialPSName = "tornado";
        
        createHUD( arguments.getResolution(), env );
        
        initParticleSystem( initialPSName, env );
        
        Canvas3D canvas = createCanvas( arguments.getCanvasConstructionInfo(), getClass().getSimpleName() );
        env.addCanvas( canvas );
        
        canvas.addWindowClosingListener( new WindowClosingRenderLoopEnder( this ) );
        this.addFPSListener( new CanvasFPSListener( canvas ) );
        
        InputSystem.getInstance().registerNewKeyboardAndMouse( canvas.getPeer() );
        
        this.fpih = FirstPersonInputHandler.createDefault( env.getView(), canvas, arguments.getMouseYInverted() );
        fpih.getBindingsManager().createDefaultBindings();
        fpih.getBindingsManager().unbind( Keys.SPACE );
        fpih.setSuspended( true );
        InputSystem.getInstance().addInputHandler( fpih );
    }
    
    public static void main( String[] args ) throws Throwable
    {
        ParticleSystemTest test = new ParticleSystemTest( parseCommandLine( args ) );
        
        test.begin();
    }
}
