package galactic.view.effects;

import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.scenegraph.primitives.Line;
import org.xith3d.schedops.movement.Bullet;

public class LaserImpulse extends Bullet
{
    private static final float LI_THICKNESS = .3f;
    
    public LaserImpulse( Vector3f velocity, long maxLifeTime, Colorf color )
    {
        super( velocity, maxLifeTime );
        
        Vector3f lineEnd = new Vector3f( velocity );
        lineEnd.normalize();
        lineEnd.scale( 10.75f );
        
        Line line = new Line( lineEnd, LI_THICKNESS, true, color );
        
        this.addChild( line );
    }
}
