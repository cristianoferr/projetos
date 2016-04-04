package cyclone;

import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import cristiano.dados.GenericDTO;
import cristiano.math.PhysicsConsts;
import cristiano.math.Vector3;
import cristiano.performance.ClockWatch;
import cyclone.collide.CollisionManager;
import cyclone.collide.CollisionSolverEnum;
import cyclone.collide.fine.CollisionData;
import cyclone.contact.ContactResolver;
import cyclone.contactGenerator.ContactGenerator;
import cyclone.entities.RigidBody;
import cyclone.entities.GeomPoly.GeomOBJ;
import cyclone.entities.GeomPoly.ModelContainer;
import cyclone.entities.geom.Geom;
import cyclone.entities.geom.Geom.PrimitiveType;
import cyclone.forceGenerator.ActuatorController;
import cyclone.forceGenerator.SimpleGravity;
import cyclone.forceGenerator.SpaceGravity;
import cyclone.forceRegistry.ForceRegistry;

public class Cyclone {
	
private EntityFactory entFac;
private Date lastUpdate=new Date();
private ForceRegistry registry;


private Vector<SpaceGravity> gravityEmitters=new Vector<SpaceGravity>();



//This object will contain all the 3D geoms loaded from .OBJ files for easy retrieval and centralized storage
private ModelContainer modelContainer;

public static Vector<GenericDTO> debug_lines=new Vector<GenericDTO>();



private SimpleGravity gravity;  //Usado em conjunção com "CycloneConfig.useGravity": Aplica uma força gravitacional direcional em todos os corpos
//RigidBodyBuoyancy buoyancy;
//RigidBodyDrag drag;





private ActuatorController actuatorController;

private static Logger logger = Logger.getLogger(Cyclone.class);

/**
 * Holds the maximum number of contacts allowed (i.e. the
 * size of the contacts array).
 */

/**
 * Holds the resolver for contacts.
 */
private ContactResolver resolver;

private CollisionManager colManager;

/**
 * Contact generators.
 */
private Vector<ContactGenerator> contactGenerators=new Vector<ContactGenerator>();


/**
 * Holds the list of contacts.
 */

//private GroundContacts groundContacts;

public Cyclone(){
	entFac=new EntityFactory();
	registry=new ForceRegistry();
	
	actuatorController=new ActuatorController();
	modelContainer=new ModelContainer();
	//buoyancy=new Buoyancy(-30,40.0f, 3.0f, 0.6f);
	//drag=new Drag(0.05,0.05);
	resolver=new ContactResolver(CycloneConfig.getVelocityIterations(),CycloneConfig.getPositionIterations());
	//contacts = new RigidBodyContact[maxContacts];
	//getContactGenerators().add(groundContacts);
	
	
	
}

/*
 * Esse método deve ser chamado quando se quer inicializar a engine física
 */
public void initialize(){
	if (CycloneConfig.isUsingGravity()){
		gravity=new SimpleGravity(new Vector3(0.0f, -10f, 0.0f));
	}
	if (CycloneConfig.isUseFloor()){
		//groundContacts=new GroundContacts(getItems());
	}
	colManager=new CollisionManager(this); 
	setContactData();

}
public static void log(String msg){
	logger.info(msg);
}

public static void logDebug(String msg){
	logger.debug(msg);
}
public static void logFatal(String msg){
	logger.fatal(msg);
}


public EntityFactory getEntFac() {
	return entFac;
}

public CollisionData getcData() {
	return colManager.getcData();
}

public void setContactData(){
	colManager.setContactData(CycloneConfig.getFriction(), CycloneConfig.getRestitution(), CycloneConfig.getTolerance());

}

public GeomOBJ getObjData(String id){
	return getModelContainer().getObjData(id);
}

public GeomOBJ loadGLModel(String name,String fileName,double density){
	return getModelContainer().loadGLModel(name,fileName,1,density);
}

public GeomOBJ loadGLModel(String name,String fileName,float scale,double density){
	return getModelContainer().loadGLModel(name,fileName,scale,density);
}
/*
public RigidBody createRigidBody(Class c){
	RigidBody p=entFac.create(c);
	registry.remove(p);
	
	registerBody(p);
	//registry.add(p, gravity);
	//registry.add(p, buoyancy);
	
	//pfr.add(p, drag);
	
	return p;
}*/

public void registerGravityEmitter(RigidBody cp){
		SpaceGravity sg=new SpaceGravity(cp);
		gravityEmitters.add(sg);
}

public void registerBody(RigidBody cp){
	if (CycloneConfig.isUsingGravity()){
		registry.add(cp, gravity);
	}
	
	if (cp.getMass()>PhysicsConsts.gravityThreshold){
		registerGravityEmitter(cp);
	}
	cp.setSteering(actuatorController);
	
	//BoundingSphere node=new BoundingSphere(cp.getPosition(),cp.getGeom().getRadius());
	//bvnode.insert(cp, node);
	
	getItems().add(cp);
	//XithViewer.printLog("RegisterBody:"+cp,true);
}

public ForceRegistry getPfr() {
	return registry;
}


	public static void main(String[] args) {
//		System.out.println("t:"+ShotType.ARTILLERY.ordinal());
		new Cyclone();
	}
	


	
	public void update(){
		
		Date now=new Date();
		int time=(int)(now.getTime()-lastUpdate.getTime());
		if (time<=0) {
			time=1;
		}
		lastUpdate=now;

       // Find the duration of the last frame in seconds
       //float duration = (float)TimingData::get().lastFrameDuration * 0.001f;
       //if (duration <= 0.0f) return;

       // Update the physics of each RigidBody in turn
		float duration=(float)time/1000;
		update(duration);
	}
	
	
	public int generateAndSolveContacts(float duration)	{
		colManager.generateContacts(getContactGenerators());
		resolver.resolveContacts(
		           colManager.getcData(),
		           duration
		           );
		
		return 0;

	}
	
	
	private void applyGravityEmitters(RigidBody receiver,float duration){
		
		try {
			for (int i=0;i<gravityEmitters.size();i++){
				gravityEmitters.get(i).updateForce(receiver, duration);
				if (!gravityEmitters.get(i).getEmiter().isAlive()){gravityEmitters.remove(i);}
			}
		} catch (Exception e) {
			Cyclone.log(e.getMessage());
		}
		//if (!CycloneConfig.applyGravity)gravityEmitters.clear();

	}
	public void update(float duration){
		
		ClockWatch.startClock("Cyclone.Update");
		if (duration<=0) {return;}
		if (duration>CycloneConfig.MAX_UPDATE_TIME) {duration=CycloneConfig.MAX_UPDATE_TIME;}
		
		colManager.prepareUpdate();
		//temp
		//duration=0.05f;
		
		//System.out.println("cyclone.update() duration:"+duration);
		// First apply the force generators
		//ClockWatch.startClock("Cyclone.UpdateForces");
		registry.updateForces(duration);
		//ClockWatch.stopClock("Cyclone.UpdateForces");
		
	    // Set up the collision data structure
	    
	    ClockWatch.startClock("Cyclone.update.removeItems");
	    for (int i=0;i<getItems().size();i++){
	    	   if (!getItem(i).isAlive()) {
	    		   getItems().remove(i);
	    	   }
	    }
	    ClockWatch.stopClock("Cyclone.update.removeItems");
	    
	    ClockWatch.startClock("Cyclone.update.loop");
//	    System.out.println("initLoop");
       for (int i=0;i<getItems().size();i++){
    	   Geom geom=getItem(i).getGeom();

    	   colManager.groundCollision(getItem(i));

    	   //I will solve collision according to the method selected... 
    	   
    	   fixRotation(geom,i);
    	   
    	    
    	   applyGravityEmitters(getItem(i),duration);
    	   
    	   getItem(i).integrate(duration);
    	   geom.calculateInternals(getItem(i));
    	   
    	   colManager.updateIterator(i);
    	   
    	   //verifyCollision(getItem(i),i);
       }
    //   System.out.println("endLoop");
       colManager.updateEnd();
       
       ClockWatch.stopClock("Cyclone.update.loop");
    	   
    	//   System.out.println(i+"= pos:"+p.body.getPosition());
    	 //  if (!p.isAlive()){
    	//	   entFac.remove(p);
    	//   }
              
       ClockWatch.startClock("Cyclone.update.contacts");
    // Generate contacts

       generateAndSolveContacts(duration);
       // Resolve detected contacts
       
       ClockWatch.stopClock("Cyclone.update.contacts");
       ClockWatch.stopClock("Cyclone.Update");
   }



	private void fixRotation(Geom geom, int i) {
		RigidBody body=getItem(i);
		if (body.getRotation().magnitude()>CycloneConfig.getRotationFix()) {
			//System.out.println("fixRotation:"+getItem(i)+" "+getItem(i).getRotation().magnitude());
			body.getRotation().multiVectorScalar(0.9);
    	   } 
	//	if (body.getName().equals("Nave Jogador"))
	//		System.out.println("fixRotation:"+body+" "+body.getRotation().magnitude()+" ");
		if ((geom.getType()==PrimitiveType.POLYGON) && (body.getRotation().magnitude()>0) ){
			//System.out.println("fixRotation:"+body+" "+body.getRotation().magnitude());
			//CycloneConfig.polygonRotationFix=0.9985;
			body.getRotation().multiVectorScalar(CycloneConfig.getPolygonRotationFix());
			if (body.getRotation().magnitude()<1E-30)body.getRotation().clear();
			//body.actuatorRestart();
		}
		//body.actuatorRestart();
		
		
		
	}
	
	/*
	private void applyGravity(RigidBody b1,RigidBody b2){
		Vector3 dif=b1.getPosition().getSubVector(b2.getPosition());
		Vector3 dif2=b2.getPosition().getSubVector(b1.getPosition());
		//PhysicsConsts.Gforce
		double f=PhysicsConsts.Gforce* (b1.getMass()*b2.getMass())/Math.pow(dif.magnitude(),2);
		dif.normalise();
		dif2.normalise();
		b2.addForce(dif.getMultiVector(f));
		b1.addForce(dif2.getMultiVector(f));
		
	}
*/

	
	
	
	public Vector<RigidBody> getItems(){
		return entFac.getItems();
	}

	public RigidBody getItem(int i){
		return getItems().elementAt(i);
	}


	public void setContactGenerators(Vector<ContactGenerator> contactGenerators) {
		this.contactGenerators = contactGenerators;
	}


	public Vector<ContactGenerator> getContactGenerators() {
		return contactGenerators;
	}
	
	public boolean isUsingGravity() {
		return CycloneConfig.isUsingGravity();
	}

	public void setUseGravity(boolean useGravity) {
		CycloneConfig.setUseGravity(useGravity);
	}

	public boolean isUseFloor() {
		return CycloneConfig.isUseFloor();
	}

	public void setUseFloor(boolean useFloor) {
		CycloneConfig.setUseFloor(useFloor);
	}

	public ActuatorController getSteering() {
		return actuatorController;
	}


	public ModelContainer getModelContainer() {
		return modelContainer;
	}

	public void drawDebugLine(GenericDTO dto) {
		debug_lines.add(dto);
		
	}
	/*0=green;
	1=red;
	2=blue;
	3=orange;*/
	public void drawDebugLine(Vector3 pt1, Vector3 pt2, int cor) {
		
		GenericDTO dto=new GenericDTO();
		dto.addVector(pt1);
		dto.addVector(pt2);//ipn2.getPosicao().getAddVector(b2.getPosition()));
		dto.addInteger(cor);
		debug_lines.add(dto);
		
	}

	public void setCollisionSolver(CollisionSolverEnum cs) {
		CycloneConfig.setCollisionSolver(cs);
		
	}

}
