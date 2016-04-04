package cyclone.collide.fine;

import cyclone.entities.RigidBody;
import cyclone.math.Vector3;
import cyclone.math.Matrix4;

public class CollisionPrimitive {
        

/**
 * The resultant transform of the primitive. This is
 * calculated by combining the offset of the primitive
 * with the transform of the rigid body.
 */
Matrix4 transform;

            
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


     
           
}
