package galactic.view.models;

import galactic.model.wares.ModelData;
import galactic.view.View;

import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.scenegraph.AmbientLight;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Line;

import cristiano.math.Vector3;


public class ShipView extends ItemView implements IShipView{
	AbstractItemView target=null;
	TransformGroup group_noRot=null;
	org.xith3d.scenegraph.primitives.Line lineVel=null;
	org.xith3d.scenegraph.primitives.Line lineDir=null;
	org.xith3d.scenegraph.primitives.Line lineFront=null;
	double roc_horiz=0;
	double roc_vert=0;
	
	
	double lineSize=3;
	public ShipView( View view) {
		super(view);
	}

	
	@Override
	public AbstractItemView getTarget() {
		return target;
	}

	@Override
	public void setTarget(AbstractItemView target) {
		this.target=target;
	}

	
	public void loadModel(ModelData md){
		super.loadModel(md);
		lineVel=new Line(new Tuple3f(0,0,0),2,Colorf.GREEN);
		lineDir=new Line(new Tuple3f(0,0,0),2,Colorf.RED);
		//lineFront=new Line(new Tuple3f((float)getTamX()*2,0,0),2,Colorf.BLUE);
		group.addChild( new AmbientLight(true,Colorf.WHITE));
		group_noRot= new TransformGroup();
		
		
testaLuz();
		
	    
		group_noRot.addChild(lineVel);
		group_noRot.addChild(lineDir);
		//group.addChild(lineFront);
		((View)view).getRootBranch().addChild(group_noRot);
		//return group;
		lineSize=lineSize*getTamX();
	}


	private void testaLuz() {
		/*		Tuple3f attenuation = new Tuple3f();
				PointLight pLight;
		        attenuation.set( 1f, 0.00008f, 0.00001f );
		        final Colorf lightColor = new Colorf( 0.95f, 1.0f, 0.8f );
		        pLight = new PointLight( lightColor, Point3f.ZERO, attenuation );
		        pLight.setEnabled( true );
		        
		        
		        view.getRootBranch().addChild( pLight );
		        pLight.setTrackedNode( view.getRootBranch() );
				
	     HeightMapSampler heightMap=null;
	     GridSurface surface;
	     float bias = 7f;
	      float x = -10000f / bias;
	      float z = -10000f / bias;
	      float scale = 20000f / bias;
	      float height = 4000f / bias;
	      
	     

		        try
		        {
		        	float[][] f=new float[5][5];
		        	for (int i=0;i<5;i++)
		        		for (int j=0;j<5;j++)
		        			f[i][j]=(float)Math.random();
		            heightMap = new HeightMapSampler(f  );
		        }
		        catch ( Exception e )
		        {
		            e.printStackTrace();
		            System.exit( -1 );
		        }
		        
		        Material material= new Material();
		        material.setAmbientColor( Colorf.GRAY75 );
		        material.setShininess( 0f );
		        material.setLightingEnabled( true );
		//        surface = new DetailTextureSurface( "coast/coast-texture_2x2k.jpg", material  );
		        surface = new DetailTextureSurface( "testpattern.png", material );
		        ChunkedTerrain chunkedTerrain = new ChunkedTerrain(  heightMap, surface, x, 0f, z, scale, height );
		        view.getRootBranch().addChild( chunkedTerrain );
	*/}
	
	
	 public void doMove() 
	    {   
		 super.doMove();
		 Vector3 theMove=new Vector3((coord.getX()-view.getRelativePosition().x),
				 coord.getY()-view.getRelativePosition().y,
				 coord.getZ()-view.getRelativePosition().z);
		 
		 Transform3D transform = new Transform3D();
		 	transform.set(theMove.getVector3f());
		 	group_noRot.setTransform(transform);
	    	
	  		
	    	
	   }

	
	public void draw(boolean direct){
		super.draw(direct);
		
		//if (!direct) shoot(new Vector3f((float)Math.random()*1000-500,(float)Math.random()*1000-500,(float)Math.random()*1000-500),group_noRot);
		
		Vector3 lookat=getPointInWorldSpace(new Vector3(lineSize,0,0));
		lookat.x=((lookat.x-getCoord().x));
    	lookat.y=((lookat.y-getCoord().y));
    	lookat.z=((lookat.z-getCoord().z));
    	
    //	Vector3f pos=getTransformDirection(velocity).getVector3f();
    	
    	

    	if (getTarget()!=null){
	    	Vector3 VectorResult;
	    	//double DotResult = Vector3.getDotProduct(rotation, getTarget().getRotation());
	    	Vector3 c=new Vector3(coord);
	    	c.x=c.x-view.getRelativePosition().x+0.0001;
	    	c.y=c.y-view.getRelativePosition().y+0.0001;
	    	c.z=c.z-view.getRelativePosition().z+0.0001;
	    	
	    	Vector3 tc=new Vector3(getTarget().getCoord());
	    	tc.x=tc.x-view.getRelativePosition().x+0.0001;
	    	tc.y=tc.y-view.getRelativePosition().y+0.0001;
	    	tc.z=tc.z-view.getRelativePosition().z+0.0001;
	    	
	    	
	    	double DotResult = Vector3.getDotProduct(tc, c);
	    	        if (DotResult > 0)
	    	        {
	    	            VectorResult = tc.getAddVector(c);
	    	        }
	    	        else
	    	        {
	    	        	VectorResult = tc.getSubVector(c);
	    	        }
	    	       // System.out.println("dotResult:"+Math.toDegrees(Math.acos(DotResult)));
	    	        VectorResult.normalise();
	    	    	Vector3 up=getTransformDirection(Vector3.UP);
	    	    	Vector3 right=getTransformDirection(Vector3.Z);
	    	    	Vector3 fwd=getTransformDirection(Vector3.X);

	    	    	
	    	        double dir_horiz=Vector3.angleDir(fwd, VectorResult, up);
	    	        double dir_vert=Vector3.angleDir(fwd, VectorResult, right);
	    	        double dir_2=Vector3.angleDir(up, VectorResult, right);
	    	        
	    	        //double dir_right=Vector3.angleDir(up, VectorResult, right);   <--- positivo=atrás  // neg = na frente
	    	        //double dif=1-(Math.abs(dir));
	    	        //double dif_right=1-(Math.abs(dir_right));
	    	        double dif_horiz=dir_horiz-roc_horiz;
	    	        double dif_vert=dir_vert-roc_vert;
	    	        
	    	        
//	    	        System.out.println("Angle up:"+Consts.format(Math.toDegrees(up.angle(tc)))+" Angle right:"+Consts.format(Math.toDegrees(right.angle(tc)))+" Angle fwd:"+Consts.format(Math.toDegrees(fwd.angle(tc))));
	    	        
	    	        
	    	      //  System.out.println("dir2:"+Consts.format(dir_2)+" dir_2:"+ Consts.format(Math.toDegrees(Math.acos(dir_2)))+" dir_horiz:"+Consts.format(Math.toDegrees(Math.acos(dir_horiz)))+" dir_vert:"+Consts.format(Math.toDegrees(Math.acos(dir_vert)))+" "+Consts.format(dir_vert*180)+" roc:"+Consts.format(roc_horiz)+" dif:"+dif_horiz);
	    	       // System.out.println("angle:"+dir_horiz+" "+Consts.format(dir_horiz*180)+" angleDir:"+Consts.format(dir_vert)+" "+Consts.format(dir_vert*180)+" roc:"+Consts.format(roc_horiz)+" dif:"+dif_horiz);
	    	     //   System.out.println(" angleDir:"+Consts.format(dir_vert)+" "+Consts.format(dir_vert*180)+" roc:"+Consts.format(roc_horiz)+" dif:"+dif_horiz);
	    	        double angle_vert=dir_vert*180;
	    	        double angle_horiz=dir_horiz*180;
	    	     //   System.out.println("dif_vert:"+dif_vert+" dif_vert:"+dif_vert);
	    	       
	    	        
	    	        if (Math.abs(angle_horiz)>110){
		    	        if (Math.abs(dir_horiz)>0.4){
		    	        	 if (dir_horiz>0) {//System.out.println("ESQUERDA "+dir_horiz);
		    	        	// activateControl(ControlManager.CTL_TURN_LEFT,Math.abs(dir_horiz)/10);
		    	        	 }
		 	    	        if (dir_horiz<0) {//System.out.println("DIREITA "+dir_horiz);
		 	    	     //  activateControl(ControlManager.CTL_TURN_RIGHT,Math.abs(dir_horiz)/10);
		 	    	        }
		    	        }
	    	        } else 
	    	        {
	    	        
		    	        	 if (dif_horiz>0) {//System.out.println("ESQUERDA "+dir_horiz);
		    	        //	 activateControl(ControlManager.CTL_TURN_LEFT,Math.abs(dir_horiz)/5);
		    	        	 }
		 	    	        if (dif_horiz<0) {//System.out.println("DIREITA "+dir_horiz);
		 	    	      // activateControl(ControlManager.CTL_TURN_RIGHT,Math.abs(dir_horiz)/5);
		 	    	        }
		    	        }
	    	        
	    	        roc_horiz=dir_horiz;
	    	        roc_vert=dir_vert;
	    	        
	    	        if (Math.abs(angle_vert)>160){
	    	        if (Math.abs(dir_vert)>0.4){
	    	        	 if (dir_vert<0) {//System.out.println("ACIMA "+dir_vert);
	    	        	 	//activateControl(ControlManager.CTL_TURN_UP,Math.abs(dir_vert)/10);
	    	        	 }
	 	    	        if (dir_vert>0) {//System.out.println("ABAIXO "+dir_vert);
	 	    	   //     	activateControl(ControlManager.CTL_TURN_DOWN,Math.abs(dir_vert)/10);
	 	    	        }
	    	        }
	    	        } else{
	    	        	 if (dif_vert>0) {//System.out.println("ACIMA "+dir_vert);
		    	     //   	 activateControl(ControlManager.CTL_TURN_UP,Math.abs(dir_vert)/5);
	    	        	 }
	 	    	        if (dif_vert<0) {//System.out.println("ABAIXO "+dir_vert);
	 	 	    	   //    activateControl(ControlManager.CTL_TURN_DOWN,Math.abs(dir_vert)/5);
	 	    	        }
	    	        }
    	
	    	      //returns -1 when to the left, 1 to the right, and 0 for forward/backward

	    	      //  Debug.DrawRay(transform.position, VectorResult * 100, Color.green);
	    	        lineDir.setCoordinates( 0,0,0, (float)(VectorResult.x*10),
	    	        		(float)(VectorResult.y*10),
	    	        		(float)(VectorResult.z*10));
    	}
	    	lineVel.setCoordinates( 0,0,0, (float)velocity.x,(float)velocity.y,(float)velocity.z);
	    	
    	
	//	lineDir.setCoordinates( 0,0,0, (float)lookat.x,(float)lookat.y,(float)lookat.z);
    	//lineDir.setCoordinates( 0,0,0, pos.x(),pos.y(),pos.z());
    
	}

	
	
}
