package galactic.view.models;

import galactic.view.View;

import org.openmali.vecmath2.Colorf;
import org.softmed.jops.ParticleSystem;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.particles.jops.ParticleSystemNode;
import org.xith3d.scenegraph.particles.jops.RelativeParticleSystemNode;
import org.xith3d.scenegraph.primitives.Sphere;
import org.xith3d.selection.BoundingBoxSelectable;
import org.xith3d.selection.Selectable;

public class SphereModel extends ItemView implements ISphereModel {
boolean addflare=false;
double radius=0;
	public SphereModel( View view,double radius) {
		super( view);
		this.radius=radius;
		
		
	}
	
	
	

	
	@Override
	public double getRadius() {
		
		return radius;
	}
	
public double getTamX() {
		
		return radius;
	}

	@Override
	public void setRadius(double r) {
		radius=r;
	}
	
	
public void addFlare(){
		
		String handle = ((View)getView()).getPSHandle( "shockRifleBall" );
        
        if ( handle == null )
            return;
        
        ParticleSystem ps = ((View)getView()).getPmanager().getCopyAttached( handle );
        ParticleSystemNode psn = null;
        
        psn = new RelativeParticleSystemNode();
        Transform3D g3d = new Transform3D();
        TransformGroup g=new TransformGroup(); 
        g.addChild(psn);
        
        g3d.setScale((float)radius);
        g.setTransform(g3d);
        
        group.addChild( g);
        
        
        psn.setShowGeneratorsAndPointMasses( true );
        
        psn.setParticleSystem( ps );
   
	}
	
	
	/* (non-Javadoc)
	 * @see galactic.view.models.ISphereModel#draw(boolean)
	 */
	public void draw(boolean direct){
		super.draw(direct);
		if (!addflare){
			//addFlare();
			addflare=!addflare;
		/*	try {
				EffectFactory.getInstance().getBloomFactory().prepareForBloom( view.env, view.getCanvas(), view.getRootBranch() );
			} catch (IOException e) {
			
				e.printStackTrace();
			}*/
		}
		
			
		//	EffectFactory.getInstance().getBloomFactory().setSceneWeight( (float)Math.random() );
        //	EffectFactory.getInstance().getBloomFactory().setBloomWeight( (float)Math.random() );
		
	}
}
