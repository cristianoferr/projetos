package cyclone.xith3d;

import org.openmali.FastMath;
import org.openmali.spatial.bounds.Bounds;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.scenegraph.Group;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.StaticTransform;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Cone;
import org.xith3d.scenegraph.primitives.Line;
import org.xith3d.selection.BoundingBoxSelectable;
import org.xith3d.selection.SelectionManager;

public class CycloneBoundingBoxSelectable<NodeType extends Node>  extends BoundingBoxSelectable
{
    private TransformGroup group;
    
    public void setSelected( SelectionManager selectionManager, boolean selected  )
    {
     //   super.setSelected( selectionManager, selected );
        Node node = getNode();
        if ( selected && ( group == null ) )
        {
            Tuple3f center = Tuple3f.fromPool();
            
            Bounds bounds = node.getBounds();
            bounds.getCenter( center );
            float radius = bounds.getMaxCenterDistance();
                
            // TODO die Gruppe cachen und nicht immer wieder neu erstellen
            group = new TransformGroup();//new TransformGroup( node.getWorldTransform() );
            node.getTransformGroup().addChild(group);
            group.addChild( createBox( radius ) );
            //group.addChild( createCoordinateSystem( radius ) );
//            selectionManager.getSelectionLayer().addChild( group );
            //node.getRoot().addChild( group );
            
            Tuple3f.toPool( center );
        }
        else if ( group != null )
        {
        	node.getTransformGroup().removeChild( group );
            //node.getRoot().removeChild( group );
//            selectionManager.getSelectionLayer().removeChild( group );
            group = null;
        }
    }
    
    private Group createCoordinateSystem( float r )
    {
        final Group coords = new Group();
        final Colorf col = Colorf.GREEN;
        final float lw = 2;
        
        Line[] lines = new Line[] {
            new Line( new Tuple3f( r, 0f, 0f ), lw, col ),
            new Line( new Tuple3f( 0f, r, 0f ), lw, col ),
            new Line( new Tuple3f( 0f, 0f, r ), lw, col ),
        };
        
        for ( int i = 0; i < lines.length; i++ )
        {
            coords.addChild( lines[ i ] );
        }
        
        Cone cone = new Cone( r / 10f, r / 3f, 10, col );
        StaticTransform.rotateZ( cone, -FastMath.PI_HALF );
        StaticTransform.translate( cone, r, 0f, 0f );
        coords.addChild( cone );

        cone = new Cone( r / 10f, r / 3f, 10, col );
        StaticTransform.translate( cone, 0f, r, 0f );
        coords.addChild( cone );
        
        cone = new Cone( r / 10f, r / 3f, 10, col );
        StaticTransform.rotateX( cone, FastMath.PI_HALF );
        StaticTransform.translate( cone, 0f, 0f, r );
        coords.addChild( cone );
        
        return ( coords );
    }
    
    private Group createBox(float r)
    {
    	
        Group box = new Group();
        float ou = r;
        float in = 0.8f * r;
        float lw = 4;
        Colorf col = Colorf.WHITE;
        
        Line[] lines = new Line[] {
            new Line( new Tuple3f( -ou,  ou,  ou ), new Tuple3f( -in,  ou,  ou ), lw, col ),
            new Line( new Tuple3f(  ou,  ou,  ou ), new Tuple3f(  in,  ou,  ou ), lw, col ),
            new Line( new Tuple3f( -ou,  ou,  ou ), new Tuple3f( -ou,  in,  ou ), lw, col ),
            new Line( new Tuple3f(  ou,  ou,  ou ), new Tuple3f(  ou,  in,  ou ), lw, col ),
            new Line( new Tuple3f( -ou, -ou,  ou ), new Tuple3f( -in, -ou,  ou ), lw, col ),
            new Line( new Tuple3f(  ou, -ou,  ou ), new Tuple3f(  in, -ou,  ou ), lw, col ),
            new Line( new Tuple3f( -ou, -ou,  ou ), new Tuple3f( -ou, -in,  ou ), lw, col ),
            new Line( new Tuple3f(  ou, -ou,  ou ), new Tuple3f(  ou, -in,  ou ), lw, col ),
            
            new Line( new Tuple3f( -ou,  ou, -ou ), new Tuple3f( -in,  ou, -ou ), lw, col ),
            new Line( new Tuple3f(  ou,  ou, -ou ), new Tuple3f(  in,  ou, -ou ), lw, col ),
            new Line( new Tuple3f( -ou,  ou, -ou ), new Tuple3f( -ou,  in, -ou ), lw, col ),
            new Line( new Tuple3f(  ou,  ou, -ou ), new Tuple3f(  ou,  in, -ou ), lw, col ),
            new Line( new Tuple3f( -ou, -ou, -ou ), new Tuple3f( -in, -ou, -ou ), lw, col ),
            new Line( new Tuple3f(  ou, -ou, -ou ), new Tuple3f(  in, -ou, -ou ), lw, col ),
            new Line( new Tuple3f( -ou, -ou, -ou ), new Tuple3f( -ou, -in, -ou ), lw, col ),
            new Line( new Tuple3f(  ou, -ou, -ou ), new Tuple3f(  ou, -in, -ou ), lw, col ),
            
            new Line( new Tuple3f(  ou, -ou,  ou ), new Tuple3f(  ou, -in,  ou ), lw, col ),
            new Line( new Tuple3f(  ou,  ou,  ou ), new Tuple3f(  ou,  in,  ou ), lw, col ),
            new Line( new Tuple3f(  ou, -ou,  ou ), new Tuple3f(  ou, -ou,  in ), lw, col ),
            new Line( new Tuple3f(  ou,  ou,  ou ), new Tuple3f(  ou,  ou,  in ), lw, col ),
            new Line( new Tuple3f(  ou, -ou, -ou ), new Tuple3f(  ou, -in, -ou ), lw, col ),
            new Line( new Tuple3f(  ou,  ou, -ou ), new Tuple3f(  ou,  in, -ou ), lw, col ),
            new Line( new Tuple3f(  ou, -ou, -ou ), new Tuple3f(  ou, -ou, -in ), lw, col ),
            new Line( new Tuple3f(  ou,  ou, -ou ), new Tuple3f(  ou,  ou, -in ), lw, col ),
            
            new Line( new Tuple3f( -ou, -ou,  ou ), new Tuple3f( -ou, -in,  ou ), lw, col ),
            new Line( new Tuple3f( -ou,  ou,  ou ), new Tuple3f( -ou,  in,  ou ), lw, col ),
            new Line( new Tuple3f( -ou, -ou,  ou ), new Tuple3f( -ou, -ou,  in ), lw, col ),
            new Line( new Tuple3f( -ou,  ou,  ou ), new Tuple3f( -ou,  ou,  in ), lw, col ),
            new Line( new Tuple3f( -ou, -ou, -ou ), new Tuple3f( -ou, -in, -ou ), lw, col ),
            new Line( new Tuple3f( -ou,  ou, -ou ), new Tuple3f( -ou,  in, -ou ), lw, col ),
            new Line( new Tuple3f( -ou, -ou, -ou ), new Tuple3f( -ou, -ou, -in ), lw, col ),
            new Line( new Tuple3f( -ou,  ou, -ou ), new Tuple3f( -ou,  ou, -in ), lw, col ),
            
            new Line( new Tuple3f( -ou,  ou,  ou ), new Tuple3f( -in,  ou,  ou ), lw, col ),
            new Line( new Tuple3f(  ou,  ou,  ou ), new Tuple3f(  in,  ou,  ou ), lw, col ),
            new Line( new Tuple3f( -ou,  ou,  ou ), new Tuple3f( -ou,  ou,  in ), lw, col ),
            new Line( new Tuple3f(  ou,  ou,  ou ), new Tuple3f(  ou,  ou,  in ), lw, col ),
            new Line( new Tuple3f( -ou,  ou, -ou ), new Tuple3f( -in,  ou, -ou ), lw, col ),
            new Line( new Tuple3f(  ou,  ou, -ou ), new Tuple3f(  in,  ou, -ou ), lw, col ),
            new Line( new Tuple3f( -ou,  ou, -ou ), new Tuple3f( -ou,  ou, -in ), lw, col ),
            new Line( new Tuple3f(  ou,  ou, -ou ), new Tuple3f(  ou,  ou, -in ), lw, col ),
            
            new Line( new Tuple3f( -ou, -ou,  ou ), new Tuple3f( -in, -ou,  ou ), lw, col ),
            new Line( new Tuple3f(  ou, -ou,  ou ), new Tuple3f(  in, -ou,  ou ), lw, col ),
            new Line( new Tuple3f( -ou, -ou,  ou ), new Tuple3f( -ou, -ou,  in ), lw, col ),
            new Line( new Tuple3f(  ou, -ou,  ou ), new Tuple3f(  ou, -ou,  in ), lw, col ),
            new Line( new Tuple3f( -ou, -ou, -ou ), new Tuple3f( -in, -ou, -ou ), lw, col ),
            new Line( new Tuple3f(  ou, -ou, -ou ), new Tuple3f(  in, -ou, -ou ), lw, col ),
            new Line( new Tuple3f( -ou, -ou, -ou ), new Tuple3f( -ou, -ou, -in ), lw, col ),
            new Line( new Tuple3f(  ou, -ou, -ou ), new Tuple3f(  ou, -ou, -in ), lw, col ),
        };
        
        for ( int i = 0; i < lines.length; i++ )
        {
            box.addChild( lines[ i ] );
        }
        
        return ( box );
    }
    

    public CycloneBoundingBoxSelectable( NodeType node )
    {
        super( node );
    }
}
