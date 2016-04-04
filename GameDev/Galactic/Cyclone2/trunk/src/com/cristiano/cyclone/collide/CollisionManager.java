package com.cristiano.cyclone.collide;

import java.util.Vector;

import com.cristiano.cyclone.Cyclone;
import com.cristiano.cyclone.CycloneConfig;
import com.cristiano.cyclone.collide.fine.CollisionData;
import com.cristiano.cyclone.collide.fine.CollisionDetectorAbstract;
import com.cristiano.cyclone.contact.Contact;
import com.cristiano.cyclone.contactGenerator.ContactGenerator;
import com.cristiano.cyclone.entities.RigidBody;
import com.cristiano.cyclone.entities.GeomPoly.GeomOBJ;
import com.cristiano.cyclone.entities.GeomPoly.InternalPoint;
import com.cristiano.cyclone.entities.geom.CollisionPlane;
import com.cristiano.cyclone.entities.geom.Geom;
import com.cristiano.cyclone.entities.geom.Geom.PrimitiveType;
import com.cristiano.cyclone.entities.geom.GeomBox;
import com.cristiano.cyclone.utils.ClockWatch;
import com.cristiano.cyclone.utils.Formatacao;
import com.cristiano.cyclone.utils.Quaternion;
import com.cristiano.cyclone.utils.Vector3;


public class CollisionManager  {
private Cyclone cyclone;

private BVHNode bvnode;

/** Holds the collision data structure for collision detection. */
private CollisionData cData;
private int maxContacts;
private CollisionPlane floorPlane=null;

public CollisionManager(Cyclone cyc){
	this.cyclone=cyc;
	cData=new CollisionData();
	bvnode=new BVHNode(null,null,null);
	maxContacts=500;
	
	if (cyclone.isUseFloor()){
		floorPlane=new CollisionPlane();
	    floorPlane.direction = new Vector3(0,1,0);
	    floorPlane.offset = 0;
	}
}


public void setcData(CollisionData cData) {
	this.cData = cData;
}


public int generateContacts(Vector<ContactGenerator> contactGenerators)	{
    int limit = maxContacts;
    //RigidBodyContact nextContact[] = contacts;

    
    
    for (int i=0;i<contactGenerators.size();i++)
    {
    	ContactGenerator g=contactGenerators.elementAt(i);
    	int used =g.addContact(getcData().getContacts(), limit);
        limit -= used;
        
        //nextContact += used;

        // We've run out of contacts to fill. This means we're missing
        // contacts.
        if (limit <= 0) {
        	break;
        }
    }

    // Return the number of contacts used.
    return maxContacts - limit;
}

public void groundCollision(RigidBody body1) {
	if (cyclone.isUseFloor()){
		   if (body1.getGeom().getType()==Geom.PrimitiveType.BOX){
		     CollisionDetectorAbstract.boxAndHalfSpace(body1, floorPlane, cData);
		   }
		   if (body1.getGeom().getType()==Geom.PrimitiveType.SPHERE){
  		     CollisionDetectorAbstract.sphereAndHalfSpace(body1, floorPlane, cData);
		   }
	   }
}

public CollisionData getcData() {
	return cData;
}

public void setContactData(double friction,double restitution,double tolerance){
	cData.setFriction(friction);
    cData.setRestitution(restitution);
    cData.setTolerance(tolerance);
}


public void prepareUpdate() {
	bvnode=new BVHNode(null,null,null);
	cData.reset(maxContacts);
}


public void updateIterator(int i) {
	if (CycloneConfig.getCollisionSolver()==CollisionSolverEnum.BVH){
		   ClockWatch.startClock("Cyclone.update.insertBVH");
		   BoundingSphere bv=new BoundingSphere(cyclone.getItem(i).getPosition(),
				   cyclone.getItem(i).getGeom().getRadius());
		   //System.out.println("i:"+i);
		   bvnode.insert(cyclone.getItem(i), bv);
		   ClockWatch.stopClock("Cyclone.update.insertBVH");
	   }
	   
	   if (CycloneConfig.getCollisionSolver()==CollisionSolverEnum.BRUTE_FORCE){
		   ClockWatch.startClock("Cyclone.update.BRUTE_FORCE");
    	   verifyCollision(cyclone.getItem(i),i);
    	   ClockWatch.stopClock("Cyclone.update.BRUTE_FORCE");
       }
}

public void updateEnd() {
	if (CycloneConfig.getCollisionSolver()==CollisionSolverEnum.BVH){
		   ClockWatch.startClock("Cyclone.update.BVHCollision");
		   checkBVHCollision();
		   ClockWatch.stopClock("Cyclone.update.BVHCollision");
	   }
}


private void checkBVHCollision(){
	Vector<PotentialContact> contacts=new Vector<PotentialContact>();
	BVHNode.setChecks(0);
	BVHNode.setNodesQty(0);
	BVHNode.setErrors(0);
	
	bvnode.getPotentialContacts(contacts);
	for (int i=0;i<contacts.size();i++){
		checkCollisionBetweenBodies(contacts.get(i).getBody(0), contacts.get(i).getBody(1));
	}
	
	//if (conts>1)
	//double d=cyclone.getItems().size();
	//d=d*(d-1)/2;
	//System.out.println("BVH Checks:"+BVHNode.checks+" contacts:"+contacts.size()+" objects:"+getItems().size()+" bf:"+d+" errors:"+BVHNode.errors);
	
}


/*
 * Essa função verifica a colisão entre o objeto atual e os demais objetos.
 * Também aplica a gravidade entre os objetos.
 */
private void verifyCollision(RigidBody b1,int ini){
	// Check for collisions with each shot
	 for (int i=ini+1;i<getItems().size();i++){
           // if (!cData.hasMoreContacts()) return;
		 RigidBody b2=getItem(i);
		 checkCollisionBetweenBodies(b1,b2);
    }
}

public void checkCollisionBetweenBodies(RigidBody b1,RigidBody b2){
	Geom g1=b1.getGeom();
	Geom g2=b2.getGeom();

	
	//System.out.println("Checando entre "+b1+" e "+b2);
	
	 //Children dont collide with they owners and vice-versa... For example: bullets
	 //I also dont want bullets colliding with each other...
	 boolean checkBullets=((b1.isDestroyOnContact()) || (b2.isDestroyOnContact()));
	 if (((b2.getOwner()==b1) || (b1.getOwner()==b2)) && (checkBullets)){
		 return;
	 }
		 //if ((b1.checkIfEmitsGravity()) && (getItem(i).checkIfEmitsGravity()))
		//	 applyGravity(b1,getItem(i));
           // When we get a collision, remove the shot
		 Contact contact=null;
		 if ((g1.getType()==Geom.PrimitiveType.BOX) || (g1.getType()==Geom.PrimitiveType.POLYGON)){
			 if ((g2.getType()==Geom.PrimitiveType.BOX) || (g2.getType()==Geom.PrimitiveType.POLYGON)){
				 contact=CollisionDetectorAbstract.boxAndBox(b1,b2);
				
			 } else if (g2.getType()==Geom.PrimitiveType.SPHERE){
				 contact=CollisionDetectorAbstract.boxAndSphere(b1,b2);
				 
			 }
		 } else if (g1.getType()==Geom.PrimitiveType.SPHERE){
			 if ((g2.getType()==Geom.PrimitiveType.BOX) || (g2.getType()==Geom.PrimitiveType.POLYGON)){
				 contact=CollisionDetectorAbstract.boxAndSphere(b2,b1);
			 } else if (g2.getType()==Geom.PrimitiveType.SPHERE){
				 contact=CollisionDetectorAbstract.sphereAndSphere(b2,b1);
			 }
		 }
	 
		 if (contact!=null){
			// cData.addContact(contact);
			 ClockWatch.startClock("Cyclone.ColManager.CheckFineCollision");
			 contact=checkFineCollision(contact);
			 ClockWatch.stopClock("Cyclone.ColManager.CheckFineCollision");
			 
			 if (contact!=null){
				// if (contact.getPenetration()<1E-4) return;
				 
				 Cyclone.logDebug("Contact created: "+Formatacao.format(contact.getPenetration())+" between "+contact.getBody(0)+ " and "+contact.getBody(1));
				 cData.addContact(contact);
			 }
		 }
}



/*
 * Esse método vai verificar se houve colisão entre 2 poligonos...
 */
public Contact checkFineCollision(Contact contact){
	RigidBody b1=contact.getBody(0);
	RigidBody b2=contact.getBody(1);
	
	if (((b1.getGeom().getType()==Geom.PrimitiveType.BOX) || (b1.getGeom().getType()==Geom.PrimitiveType.SPHERE)) && 
			((b2.getGeom().getType()==Geom.PrimitiveType.BOX) || (b2.getGeom().getType()==Geom.PrimitiveType.SPHERE))) {
		return contact;
	}


	
	/*if (contact!=null){
		cData.addContact(contact);
		return false;
	}*/
	if ((b1.getGeom().getType()==PrimitiveType.POLYGON) && (b2.getGeom().getType()==PrimitiveType.POLYGON)){
			GeomOBJ gb1=(GeomOBJ)b1.getGeom();
			GeomOBJ gb2=(GeomOBJ)b2.getGeom();
			
			InternalPoint ipn1=null,ipn2=null;
			double minDist=-1;
			
			//Vou procurar os 2 pontos mais proximos
			
			
			for (int i1=0;i1<gb1.getPoints().size();i1++){
				InternalPoint ip1=gb1.getIP(i1);
				Vector3 pos1=b1.getPointInWorldSpace(ip1.getPosicao());
				
				for (int i2=0;i2<gb2.getPoints().size();i2++){
					InternalPoint ip2=gb2.getIP(i2);
					Vector3 pos2=b2.getPointInWorldSpace(ip2.getPosicao());
					double dist=pos2.getSubVector(pos1).magnitude();
					if ((dist<minDist) || (minDist==-1)){
						ipn1=ip1;
						ipn2=ip2;
						minDist=dist;
						
						
					}
				}
			}
			
			Vector3 posIP1=b1.getPointInWorldSpace(ipn1.getPosicao());
			Vector3 posIP2=b2.getPointInWorldSpace(ipn2.getPosicao());

			cyclone.drawDebugLine(posIP1, posIP2,2);
			
			Contact cont=checkCollisionIP(b1, b2, gb1, gb2, posIP1, posIP2);
			//Vector3 dif=posIP1.getSubVector(posIP2);
		//	System.out.println("Dif:"+dif.magnitude()+" "+dif);
			if (cont!=null){
				//contact.penetration=cont.penetration;
				
				cont.setBody(0,b1);
				cont.setBody(1,b2);
				
				if (cont.getPenetration()>10){
					cont.setContactNormal(contact.getContactNormal());
					//cont.
				}
				contact.setPenetration(cont.getPenetration());
				contact.setContactNormal(cont.getContactNormal());
				return contact;
			} 
			return null;
	}
	
	
	if (b1.getGeom().getType()==PrimitiveType.POLYGON){
		GeomOBJ gb1=(GeomOBJ)b1.getGeom();
		InternalPoint ip1=findNearestIPtoPosition(gb1,b1,b2.getPosition());
		Vector3 posIP1=b1.getPointInWorldSpace(ip1.getPosicao());
		RigidBody rb1 = createIPBody(b1, gb1, posIP1);
		contact=null;

		if (b2.getGeom().getType()==PrimitiveType.BOX){
			contact=CollisionDetectorAbstract.boxAndBox(rb1,b2);
		} else if (b2.getGeom().getType()==PrimitiveType.SPHERE){
			contact=CollisionDetectorAbstract.boxAndSphere(rb1,b2);
		}
		
		if (contact!=null){
			contact.setBody(0,b1);
			contact.setBody(1,b2);
		}
		return contact;
		
	}
	
	if (b2.getGeom().getType()==PrimitiveType.POLYGON){
		GeomOBJ gb2=(GeomOBJ)b2.getGeom();
		InternalPoint ip2=findNearestIPtoPosition(gb2,b2,b1.getPosition());
		Vector3 posIP2=b2.getPointInWorldSpace(ip2.getPosicao());
		RigidBody rb2 = createIPBody(b2, gb2, posIP2);
		contact=null;
		if (b1.getGeom().getType()==PrimitiveType.BOX){
			contact=CollisionDetectorAbstract.boxAndBox(rb2,b1);
		}else if (b1.getGeom().getType()==PrimitiveType.SPHERE){
			contact=CollisionDetectorAbstract.boxAndSphere(rb2,b1);
		}
		if (contact!=null){
			contact.setBody(0,b2);
			contact.setBody(1,b1);
		}
		return contact;
	}
	
	return null;
}

private InternalPoint findNearestIPtoPosition(GeomOBJ gb1,RigidBody b1,Vector3 posicao){
	InternalPoint ret=null;
	double minDist=-1;
	for (int i1=0;i1<gb1.getPoints().size();i1++){
		InternalPoint ip1=gb1.getIP(i1);
		Vector3 pos1=b1.getPointInWorldSpace(ip1.getPosicao());
		
		double dist=posicao.getSubVector(pos1).magnitude();//Distancia entre a face e o IP
		
		if ((dist<minDist) || (minDist==-1)){
			ret=ip1;
			minDist=dist;
			
			
		}
	}
	return ret;
}
	


private Contact checkCollisionIP(RigidBody b1, RigidBody b2, GeomOBJ gb1,
		GeomOBJ gb2, Vector3 posIP1, Vector3 posIP2) {
	Contact contact;
	RigidBody rb1 = createIPBody(b1, gb1, posIP1);
	
	RigidBody rb2=createIPBody(b2, gb2, posIP2);
	
	contact=CollisionDetectorAbstract.boxAndBox(rb1,rb2);
	if (contact!=null){
		contact.setBody(0,b1);
		contact.setBody(1,b2);
	}
	//contact=null;	
	return contact;
}


private RigidBody createIPBody(RigidBody b1, GeomOBJ gb1, Vector3 posIP1) {
	RigidBody rb1=new RigidBody(new GeomBox(gb1.halfSize.getMultiVector(1.0/gb1.getnPontos())));
	rb1.setPosition(new Vector3(posIP1));
	rb1.setOrientation(new Quaternion(b1.getOrientation()));
	rb1.setVelocity(new Vector3(b1.getVelocity()));
	rb1.calculateDerivedData();
	rb1.getTransform();
	
	return rb1;
}

/*
 * Esse Metodo procura a Face do IP mais proxima do IP do outro objeto
 */
/*
private Face findNearestFace2IP(InternalPoint ipn1, Vector3 posIP, RigidBody b1) {
	double mdist=-1;
	Face nearFace=null;
	
	
	ArrayList<Face> f1=ipn1.getFaces();//relacao de faces do IP1
	for (int i1=0;i1<f1.size();i1++){
		Face face=f1.get(i1);
		
		Vector3 facePos=b1.getPointInWorldSpace(face.getCentral()); //Posicao absoluta da face
		double dist=posIP.getSubVector(facePos).magnitude();//Distancia entre a face e o IP
		if ((dist<mdist) || (mdist==-1)){
			nearFace=face;
			mdist=dist;
		}
	}
	return nearFace;
}*/

public Vector<RigidBody> getItems(){
	return cyclone.getItems();
}

public RigidBody getItem(int i){
	return cyclone.getItem(i);
}



public void setCollisionSolver(CollisionSolverEnum collisionSolver) {
	CycloneConfig.setCollisionSolver(collisionSolver);
}

}