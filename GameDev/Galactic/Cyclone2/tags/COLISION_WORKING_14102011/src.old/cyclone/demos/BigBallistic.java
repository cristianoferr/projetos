package cyclone.demos;

import java.util.Date;
import java.util.Vector;

import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyPressedEvent;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.scenegraph.Group;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.StaticTransform;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.primitives.Box;
import org.xith3d.scenegraph.primitives.Sphere;
import org.xith3d.selection.BoundingBoxSelectable;
import org.xith3d.selection.Selectable;
import org.xith3d.utility.commandline.BasicApplicationArguments;

import cyclone.World;
import cyclone.collide.fine.CollisionBox;
import cyclone.collide.fine.CollisionSphere;
import cyclone.demos.xith3d.XithViewer;
import cyclone.entities.RigidBody;
import cyclone.math.Matrix3;
import cyclone.math.Vector3;

enum ShotType    {
    UNUSED ,
    PISTOL,
    ARTILLERY,
    FIREBALL,
    LASER,
    NOFORCE}

class AmmoRound extends CollisionSphere
{

    ShotType type;
    
    Date startTime;
    Sphere obj;
    TransformGroup sphereTransform;
    Vector3 oldPos=new Vector3(0,0,0);
    int ttl=500; 
    public World world;

    AmmoRound(World world)
    {
        	this.world=world;
        	
            obj = new Sphere( .25f, 16, 16,new Colorf((float)Math.random()*1,(float)Math.random()*1,(float)Math.random()*1));
            obj.setName( "Sphere" );
            obj.setUserData( Selectable.class, new BoundingBoxSelectable<Node>( obj ) );
            sphereTransform = new TransformGroup( 0, 0f, 0f );
            //topTransform.addChild( sphereTransform );
          //  System.out.println("AmmoRound()");
            reset();
        body = new RigidBody();
    }

    public void reset(){
//    	this.particle=(RigidBody) world.create(RigidBody.class);
    	//if (!usado)
    //	System.out.println("reset()");
    	sphereTransform.addChild( obj );
        //usado=true;
    	startTime=new Date();
    	//particle.setAlive(true);
    }
    
    public int getAliveTime(){
    	Date now=new Date();
		int time=(int)(now.getTime()-startTime.getTime());
		return time; 
    }
        
    
    public void doMove(TransformGroup transformGroup, Vector3f theMove) 
    {              
    	Transform3D transform3D = new Transform3D();
  		transformGroup.getTransform(transform3D);
  		Transform3D toMove = new Transform3D();
  		toMove.setTranslation(theMove);
  		transform3D.mul(toMove);
  		transformGroup.setTransform(transform3D);
  		
   }
    
    /** Draws the round. */
    
    public void render()
    {
    	
    	//if (moving) return;
    	
    	Vector3 position=body.getPosition();
    //	System.out.println("POS:"+position+" "+getAliveTime()+" " + particle.getVelocity() );
    	
    	int metodo=0;
    	//System.out.println("render()"+position);
    	
    	if (metodo==0){
    		doMove(sphereTransform,new Vector3f((float)position.x,(float)position.y,(float)position.z));
    		sphereTransform.setPosition((float)position.x, (float)position.y, (float)position.z);
    	}
    	
    	/*if (line!=null){
	    	line.setCoordinates( (float)shotConnect.particle.getPosition().x,  (float)shotConnect.particle.getPosition().y, (float)shotConnect.particle.getPosition().z ,
	    			 (float)position.x,  (float)position.y, (float)position.z );
    	}*/
    	/*obj.getTransformGroup().getPosition().setX((float)position.x);
    	obj.getTransformGroup().getPosition().setY((float)position.y);
    	obj.getTransformGroup().getPosition().setZ((float)position.z);
    	obj.getTransformGroup().updateTransform();
    	obj.getTransformGroup().updateWorldTransform();*/
    	
    	if (metodo==1){
    		double j=position.x-oldPos.x+(position.y-oldPos.y)+(position.z-oldPos.z);
    		if (j!=0 )
    			StaticTransform.translate( obj, (float)(position.x-oldPos.x), (float)(position.y-oldPos.y), (float)(position.z-oldPos.z) );
    	oldPos.x=position.x;
    	oldPos.y=position.y;
    	oldPos.z=position.z;
    	}
    	
    	
     
    }

    /** Sets the box to a specific location. */
    void setState(ShotType shotType)
    {
        type = shotType;

        // Set the properties of the particle
        switch(type)
        {
        case PISTOL:
            body.setMass(1.5f);
            body.setVelocity(0.0f, 0.0f, 20.0f);
            body.setAcceleration(0.0f, -0.5f, 0.0f);
            body.setDamping(0.99f, 0.8f);
            radius = 0.2f;
            break;

        case ARTILLERY:
            body.setMass(200.0f); // 200.0kg
            body.setVelocity(0.0f, 30.0f, 40.0f); // 50m/s
            body.setAcceleration(0.0f, -21.0f, 0.0f);
            body.setDamping(0.99f, 0.8f);
            radius = 0.4f;
            break;

        case FIREBALL:
            body.setMass(4.0f); // 4.0kg - mostly blast damage
            body.setVelocity(0.0f, -0.5f, 10.0); // 10m/s
            body.setAcceleration(0.0f, 0.3f, 0.0f); // Floats up
            body.setDamping(0.9f, 0.8f);
            radius = 0.6f;
            break;

        case LASER:
            // Note that this is the kind of laser bolt seen in films,
            // not a realistic laser beam!
            body.setMass(0.1f); // 0.1kg - almost no weight
            body.setVelocity(0.0f, 0.0f, 100.0f); // 100m/s
            body.setAcceleration(0.0f, 0.0f, 0.0f); // No gravity
            body.setDamping(0.99f, 0.8f);
            radius = 0.2f;
            break;
        }

        body.setCanSleep(false);
        body.setAwake(true);

        Matrix3 tensor=new Matrix3();
        double coeff = 0.4f*body.getMass()*radius*radius;
        tensor.setInertiaTensorCoeffs(coeff,coeff,coeff);
        body.setInertiaTensor(tensor);

        // Set the data common to all particle types
        body.setPosition(0.0f, 1.5f, 0.0f);
       // startTime = TimingData::get().lastFrameTimestamp;

        // Clear the force accumulators
        body.calculateDerivedData();
        calculateInternals();
    }
}


class PhysBox extends CollisionBox
{

public TransformGroup group;
double x,y,z;
public PhysBox(double x,double y,double z) {
        body = new RigidBody();
        this.x=x;
        this.y=y;
        this.z=z;
        setState();
    }

   

    /** Draws the box, excluding its shadow. */
   public void render()
    {
	   Transform3D transform = group.getTransform();
	   	transform.set(body.getPosition().getVector3f());
	   	transform.setRotation(body.getOrientation().getQuaternion4f());
	   	group.setTransform(transform);
    }


   
    /** Sets the box to a specific location. */
    void setState()
    {
        body.setPosition(x,y,z);
        body.setOrientation(1,0,0,0);
        body.setVelocity(0,0,0);
        body.setRotation(new Vector3(0,0,0));
        halfSize =new Vector3(1,1,1);

        double mass = halfSize.x * halfSize.y * halfSize.z * 8.0f;
        body.setMass(mass);

        Matrix3 tensor=new Matrix3();
        tensor.setBlockInertiaTensor(halfSize, mass);
        body.setInertiaTensor(tensor);

        body.clearAccumulators();

        body.setCanSleep(false);
        body.setAwake(true);

        body.calculateDerivedData();
        calculateInternals();
        
    }
    
    public Group createBox()
    {
    	group= new TransformGroup();
        Box b=new Box(1,1,1,Colorf.GRAY);
        group.addChild(b);
        
        
        return group;
    }
};

public class BigBallistic extends XithViewer {

	PhysBox box;
	Vector<PhysBox> boxes=new Vector<PhysBox>();
	Vector<AmmoRound> ammo=new Vector<AmmoRound>();
	World world;
	 /** Holds the current shot type. */
    ShotType currentShotType=ShotType.LASER;

    
	public BigBallistic(BasicApplicationArguments arguments) throws Throwable {
		super(arguments);
		// TODO Auto-generated constructor stub
		world=new World();
		box=new PhysBox(0,0,0);
		//world.getItems().add(box.body);
		world.registerBody(box.body);
		
		rootBranch.addChild( box.createBox());
		boxes.add(box);
		
		box=new PhysBox(0.5,2,0);
		//world.getItems().add(box.body);
		world.registerBody(box.body);
		rootBranch.addChild( box.createBox());
		boxes.add(box);
	}

    public static void main(String[] args) throws Throwable {
    	BigBallistic g=new BigBallistic(parseCommandLine( args ));
    	g.begin();
    }  
	
    public void draw(){
    	for (int i=0;i<boxes.size();i++){
    		boxes.get(i).render();
    	}
    	
    	for (int i=0;i<ammo.size();i++){
    		ammo.get(i).render();
    	}
    }
    
    public void update( long gameTime, long frameTime, TimingMode timingMode )
    {
    	super.update(gameTime, frameTime, timingMode);
    	world.update();
    	update();
    	draw();
//    	clean();
    	
    }
    public void update(){
    	
    }

    public void onKeyPressed( KeyPressedEvent e, Key key )
    {
 	   
 	   AmmoRound shot=null;
 	   int x=40;
 	   if (ammo.size()>0){
  		  shot=ammo.elementAt(0); 
  	   }
 	   super.onKeyPressed(e, key);
 	   if (console.isPoppedUp()) return;
        switch ( key.getKeyID() ){
        case _1:currentShotType=ShotType.ARTILLERY;
        System.out.println("Type:"+currentShotType);
        break;
        case _2:currentShotType=ShotType.FIREBALL;
        System.out.println("Type:"+currentShotType);
        break;
        case _3:currentShotType=ShotType.LASER;
        System.out.println("Type:"+currentShotType);
        break;
        case _4:currentShotType=ShotType.PISTOL;
        System.out.println("Type:"+currentShotType);
     	   break;
        case _5:currentShotType=ShotType.NOFORCE;
        System.out.println("Type:"+currentShotType);
     	   break;  
        case F:
     	   //criaCubo();
     	   break;
        case T:
            fire();
            break;
       }
    }
    
    void fire()
    {
        // Find the first available round.
        AmmoRound shot=new AmmoRound(world);
       rootBranch.addChild(shot.sphereTransform);
       world.registerBody(shot.body);
        // If we didn't find a round, then exit - we can't fire.
       // if (shot >= ammo+ammoRounds) return;

        // Set the shot
        shot.setState(currentShotType);
        
        ammo.add(shot);
    }
}
