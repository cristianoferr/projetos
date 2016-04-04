package cyclone.entities.geom;

import cristiano.math.Matrix4;
import cristiano.math.PhysicsConsts;
import cristiano.math.Vector3;
import cyclone.entities.RigidBody;

public abstract class Geom {
        
public	enum PrimitiveType    {
	    BOX ,
	    SPHERE,
	    NONE}
/**
 * The resultant transform of the primitive. This is
 * calculated by combining the offset of the primitive
 * with the transform of the rigid body.
 */
Matrix4 transform;
public PrimitiveType type;

double density;
//double mass;



public double getDensity() {
	return density;
}

public void setDensity(double density) {
	this.density = density;
}

public double getRadius(){
	return 1;
}

public double calculateMass(){
	return 0;
}

/*public double getMass(){
	if (mass==0) calculateMass();
	return mass;
}*/

/*public void setMass(double mass) {
	this.mass = mass;
}*/



public Geom(PrimitiveType type,double density){
	this.type=type;
	this.density=density;
}

public Geom(PrimitiveType type){
	this.type=type;
	this.density=PhysicsConsts.massaKG;
}




            /**
             * The rigid body that is represented by this primitive.
             */
public RigidBody  body;

           



			/**
             * The offset of this primitive from the given rigid body.
             */
public Matrix4 offset=new Matrix4();

            /**
             * Calculates the internals for the primitive.
             */
public  void calculateInternals(){
    transform = body.getTransform().getMulti(offset);
}


            /**
             * This is a convenience function to allow access to the
             * axis vectors in the transform for this primitive.
             */
public Vector3 getAxis(int index) 
            {
			//if (transform==null)calculateInternals();
                return transform.getAxisVector(index);
            }

            /**
             * Returns the resultant transform of the primitive, calculated from
             * the combined offset of the primitive and the transform
             * (orientation + position) of the rigid body to which it is
             * attached.
             */
public Matrix4 getTransform() 
            {
                return transform;
            }

public RigidBody getBody() {
	return body;
}


public void setBody(RigidBody body) {
	this.body = body;
}

public void render() {
	
}
     
           
}
