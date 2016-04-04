package cyclone.xith3d;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.swing.JFrame;

import org.jagatoo.commandline.ArgumentsRegistry;
import org.jagatoo.commandline.CommandlineParser;
import org.jagatoo.commandline.CommandlineParsingException;
import org.xith3d.loop.InputAdapterRenderLoop;
import org.xith3d.loop.RenderLoop;
import org.xith3d.loop.RenderLoopListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.render.Canvas3DFactory;
import org.xith3d.render.config.CanvasConstructionInfo;
import org.xith3d.render.config.DisplayMode;
import org.xith3d.render.config.DisplayMode.FullscreenMode;
import org.xith3d.render.config.DisplayModeSelector;
import org.xith3d.render.config.OpenGLLayer;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.commandline.XithArgumentsHandler;
import org.xith3d.utility.commandline.XithArgumentsRegistry;

/**
 * This interface must be implemented by any Test which shall be run by the
 * Xith3DTestLauncher class.
 * 
 * The Tests are started through their constructor. So make sure, that the
 * constructor is the starting point of your test class.
 * 
 * To make your test able to get the resolution by the launcher, a contructor
 * of the following type must be present:
 * 
 * Contructor(CanvasConstructionInfo)
 * 
 * @see org.xith3d.test.Xith3DTestLauncher
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public abstract class Xith3DBasicClient extends InputAdapterRenderLoop
{
    public static interface FinishListener
    {
        /**
         * This event is fired by the test-class when the test was finished.
         */
        public void onTestFinished();
    }
    
    @Retention( RetentionPolicy.RUNTIME )
    public static @interface Description
    {
        String[] fulltext();
        String[] authors();
    }
    
    private static final OpenGLLayer FALLBACK_DEFAULT_OGLLAYER = OpenGLLayer.LWJGL;
    public static OpenGLLayer DEFAULT_OGL_LAYER = OpenGLLayer.getDefault();
    static
    {
        try
        {
            String defaultOGLLayer = System.getProperty( "org.xith3d.test.OpenGLLayer.default", FALLBACK_DEFAULT_OGLLAYER.name() );
            
            DEFAULT_OGL_LAYER = OpenGLLayer.valueOf( defaultOGLLayer );
            
            if ( DEFAULT_OGL_LAYER != null )
                DEFAULT_OGL_LAYER = FALLBACK_DEFAULT_OGLLAYER;
        }
        catch ( Throwable t )
        {
            DEFAULT_OGL_LAYER = FALLBACK_DEFAULT_OGLLAYER;
        }
    }
    
    public static final DisplayMode DEFAULT_DISPLAY_MODE = DisplayModeSelector.getImplementation( DEFAULT_OGL_LAYER ).getBestMode( 1280, 1024, 32 );
    public static final FullscreenMode DEFAULT_FULLSCREEN = DisplayMode.WINDOWED;
    public static final boolean DEFAULT_VSYNC = DisplayMode.VSYNC_ENABLED;
    
    protected static final CanvasConstructionInfo createCCI( OpenGLLayer oglLayer, DisplayMode displayMode, FullscreenMode fullscreen, boolean vsyncEnabled, String title )
    {
        if ( ( oglLayer != null ) && !oglLayer.equals( DEFAULT_OGL_LAYER ) )
            displayMode = DisplayModeSelector.getImplementation( oglLayer ).getBestMode( displayMode.getWidth(), displayMode.getHeight(), displayMode.getBPP(), displayMode.getFrequency() );
        
        if ( oglLayer == null )
            oglLayer = OpenGLLayer.getDefault();
        
        return ( new CanvasConstructionInfo( oglLayer, displayMode, fullscreen, vsyncEnabled, title ) );
    }
    
    protected static final CanvasConstructionInfo createCCI( DisplayMode displayMode, FullscreenMode fullscreen, boolean vsyncEnabled, String title )
    {
        return ( createCCI( null, displayMode, fullscreen, vsyncEnabled, title ) );
    }
    
    protected static final CanvasConstructionInfo createCCI( DisplayMode displayMode, boolean vsyncEnabled, String title )
    {
        return ( createCCI( displayMode, DEFAULT_FULLSCREEN, vsyncEnabled, title ) );
    }
    
    protected static final CanvasConstructionInfo createCCI( boolean vsyncEnabled, String title )
    {
        return ( createCCI( DEFAULT_DISPLAY_MODE, vsyncEnabled, title ) );
    }
    
    protected static final CanvasConstructionInfo createCCI( OpenGLLayer oglLayer, boolean vsyncEnabled, String title )
    {
        return ( createCCI( oglLayer, DEFAULT_DISPLAY_MODE, DEFAULT_FULLSCREEN, vsyncEnabled, title ) );
    }
    
    protected static final CanvasConstructionInfo createCCI( OpenGLLayer oglLayer, String title )
    {
        return ( createCCI( oglLayer, DEFAULT_DISPLAY_MODE, DEFAULT_FULLSCREEN, DEFAULT_VSYNC, title ) );
    }
    
    protected static final CanvasConstructionInfo createCCI( String title )
    {
        return ( createCCI( DEFAULT_VSYNC, title ) );
    }
    
    private Canvas3D createCanvasWithOwner( CanvasConstructionInfo cci, String title )
    {
        System.err.println( "Warning: Standalone-incapable OpenGLLayer selected. Wrapping in dummy frame. Things may not work as expected." );
        
        // We don't support fullscreen here!
        cci.setFullscreenMode( FullscreenMode.WINDOWED );
        
        org.xith3d.render.config.DisplayMode displayMode = cci.getDisplayMode();
        
        Point upperLeft = null;
        
        //final Frame frame = ( cci.getOpenGLLayer() == OpenGLLayer.JOGL_SWING ) ? new JFrame( title ) : new Frame( title );
        final Frame frame = new JFrame( title );
        frame.setLayout( null );
        frame.setSize( displayMode.getWidth(), displayMode.getHeight() );
        frame.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent e )
            {
                Xith3DBasicClient.this.end();
            }
        } );
        
        Canvas3D canvas = Canvas3DFactory.create( cci, ( frame instanceof JFrame ) ? ( (JFrame)frame ).getContentPane() : frame );
        frame.setVisible( true );
        
        Thread.yield();
        
        Insets insets = frame.getInsets();
        //glCanvas.setLocation( insets.left, insets.top );
        Dimension frameSize = new Dimension( displayMode.getWidth() + insets.left + insets.right, displayMode.getHeight() + insets.top + insets.bottom );
        frame.setSize( frameSize );
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        upperLeft = new Point( ( screenSize.width - frameSize.width ) / 2, ( screenSize.height - frameSize.height ) / 2 );
        frame.setLocation( upperLeft );
        
        //frame.setResizable( false/* !fullscreen */);
        
        Thread.yield();
        
        //java.awt.Point loc = glCanvas.getLocation();
        //glCanvas.setLocation( loc.x + 1, loc.y + 1 );
        
        frame.setResizable( false );
        
        java.awt.Dimension size = frame.getSize();
        frame.setSize( size.width - 2, size.height - 2 );
        
        Thread.yield();
        
        if ( ( frame.getLocation().x == 0 ) && ( frame.getLocation().y == 0 ) )
            frame.setLocation( upperLeft );
        
        this.addRenderLoopListener( new RenderLoopListener()
        {
            public void onRenderLoopStarted( RenderLoop rl )
            {
            }
            
            public void onRenderLoopPaused( RenderLoop rl, long gameTime, TimingMode timingMode, int pauseMode )
            {
            }
            
            public void onRenderLoopResumed( RenderLoop rl, long gameTime, TimingMode timingMode, int pauseMode )
            {
            }
            
            public void onRenderLoopStopped( RenderLoop rl, long gameTime, TimingMode timingMode, float averageFPS )
            {
                frame.setVisible( false );
                frame.dispose();
            }
        } );
        
        return ( canvas );
    }
    
    protected Canvas3D createCanvas( CanvasConstructionInfo cci, String title )
    {
        // This is just for comparing Swing rendering with others. Don't do this at home!
        if ( !cci.getOpenGLLayer().isStandaloneCapable() )
            return ( createCanvasWithOwner( cci, title ) );
        
        return ( Canvas3DFactory.create( cci, getClass().getSimpleName() ) );
    }
    
    public static final BasicApplicationArguments parseCommandLine( String[] args ) throws CommandlineParsingException
    {
        XithArgumentsHandler handler = new XithArgumentsHandler();
        
        
        ArgumentsRegistry argReg = XithArgumentsRegistry.createStandardArgumentsRegistry();
        CommandlineParser parser = new CommandlineParser( argReg, handler );
        parser.parseCommandline( args );
        
        if ( handler.helpRequested() )
        {
            argReg.dump();
            System.exit( 0 );
        }
        
        return ( handler.getArguments() );
    }
        
    private FinishListener finishListener = null;
    
    @Override
    protected void exit()
    {
        if ( finishListener != null )
            finishListener.onTestFinished();
        else
            super.exit();
    }
    
    public void begin( FinishListener finishListener )
    {
        this.finishListener = finishListener;
        
        begin( RunMode.RUN_IN_SAME_THREAD );
    }
    
    public void begin( FinishListener finishListener, RunMode runMode )
    {
        this.finishListener = finishListener;
        
        begin( runMode );
    }
    
    protected Xith3DBasicClient( Float maxFPS )
    {
        super( maxFPS == null ? Float.MAX_VALUE : maxFPS );
    }
}
