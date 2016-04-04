package cyclone.entities.geom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cristiano.math.Matrix4;
import cyclone.entities.RigidBody;

public abstract class Geom {
        
public	enum PrimitiveType    {
	    BOX ,
	    SPHERE,
	    POLYGON,
	    NONE}

private PrimitiveType type;

private double density;
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





            /**
             * The rigid body that is represented by this primitive.
             */
//public RigidBody  body;

           



			/**
             * The offset of this primitive from the given rigid body.
             */
public Matrix4 offset=new Matrix4();

            /**
             * Calculates the internals for the primitive.
             */
public  void calculateInternals(RigidBody body){
    body.setTransform(body.getTransformMatrix().getMulti(offset));
}


            
            /**
             * Returns the resultant transform of the primitive, calculated from
             * the combined offset of the primitive and the transform
             * (orientation + position) of the rigid body to which it is
             * attached.
             */
/*public Matrix4 getTransform() 
            {
                return transform;
            }*/



public void render() {
	
}


		public Element buildXML(Document testDoc) {
			 Element elGeom = testDoc.createElement("geom");
			 elGeom.setAttribute("density", Double.toString(density));
			 elGeom.setAttribute("type", type+"");
			 
			return elGeom;
		}

		public PrimitiveType getType() {
			return type;
		}

		public void setType(PrimitiveType t) {
			this.type=t;
			
		}
		public Matrix4 getOffset() {
			return offset;
		}
     
           
}
