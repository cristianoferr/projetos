package cyclone.contact;

import cristiano.math.Matrix3;
import cristiano.math.Quaternion;
import cristiano.math.Vector3;
import cyclone.entities.RigidBody;

public class Contact {
	/**
     * A contact represents two bodies in contact. Resolving a
     * contact removes their interpenetration, and applies sufficient
     * impulse to keep them apart. Colliding bodies may also rebound.
     * Contacts can be used to represent positional joints, by making
     * the contact constraint keep the bodies in their correct
     * orientation.
     *
     * It can be a good idea to create a contact object even when the
     * contact isn't violated. Because resolving one contact can violate
     * another, contacts that are close to being violated should be
     * sent to the resolver; that way if one resolution moves the body,
     * the contact may be violated, and can be resolved. If the contact
     * is not violated, it will not be resolved, so you only loose a
     * small amount of execution time.
     *
     * The contact has no callable functions, it just holds the contact
     * details. To resolve a set of contacts, use the contact resolver
     * class.
     */
        // ... Other data as before ...


    
        /**
         * Holds the bodies that are involved in the contact. The
         * second of these can be NULL, for contacts with the scenery.
         */
    private RigidBody body[];
    /**
     * Holds the amount each particle is moved by during interpenetration
     * resolution.
     */
    

    private Vector3 particleMovement[];
        /**
         * Holds the lateral friction coefficient at the contact.
         */
    private double friction;

        /**
         * Holds the normal restitution coefficient at the contact.
         */
    private double restitution;

        /**
         * Holds the position of the contact in world coordinates.
         */
    private Vector3 contactPoint;

        /**
         * Holds the direction of the contact in world coordinates.
         */
        private Vector3 contactNormal;

        /**
         * Holds the depth of penetration at the contact point. If both
         * bodies are specified then the contact point should be midway
         * between the inter-penetrating points.
         */
        private double penetration;

        
        /**
         * A transform matrix that converts co-ordinates in the contact's
         * frame of reference to world co-ordinates. The columns of this
         * matrix form an orthonormal set of vectors.
         */
        private Matrix3 contactToWorld;

        /**
         * Holds the closing velocity at the point of contact. This is set
         * when the calculateInternals function is run.
         */
        private Vector3 contactVelocity;

        /**
         * Holds the required change in velocity for this contact to be
         * resolved.
         */
        private double desiredDeltaVelocity;

        /**
         * Holds the world space position of the contact point relative to
         * centre of each body. This is set when the calculateInternals
         * function is run.
         */
        private Vector3 relativeContactPosition[];

        
        public Contact(){
        	body=new RigidBody[2];
        	relativeContactPosition=new Vector3[2];
        	particleMovement=new Vector3[2];
        	particleMovement[0]=new Vector3(0,0,0);
        	particleMovement[1]=new Vector3(0,0,0);  
        	setContactToWorld(new Matrix3());
        	setContactPoint(new Vector3());
        }
        
        public RigidBody getBody(int i){
        	return body[i];
        }
        
        public void setBody(int i,RigidBody b){
        	body[i]=b;
        }
        
        public String toString(){
        	return "contPoint:"+getContactPoint()+" contNormal:"+getContactNormal()+" pen:"+getPenetration()+" b0:"+body[0]+" b1:"+body[1];
        }
        /**
         * Sets the data that doesn't normally depend on the position
         * of the contact (i.e. the bodies, and their material properties).
         */
        public void setBodyData(RigidBody one, RigidBody two,
                         double friction, double restitution){
        	    body[0] = one;
        	    body[1] = two;
        	    this.friction = friction;
        	    this.setRestitution(restitution);
        	}



        

        /**
         * Calculates internal data from state data. This is called before
         * the resolution algorithm tries to do any resolution. It should
         * never need to be called manually.
         */
        void calculateInternals(double duration)
        {
            // Check if the first object is NULL, and swap if it is.
            if (body[0]==null) {
            	swapBodies();
            }
            assert(body[0]==null);

            // Calculate an set of axis at the contact point.
            calculateContactBasis();

            // Store the relative position of the contact relative to each body
            relativeContactPosition[0] = getContactPoint().getSubVector(body[0].getPosition());
            if (body[1]!=null) {
                relativeContactPosition[1] = getContactPoint().getSubVector(body[1].getPosition());
            }

            // Find the relative velocity of the bodies at the contact point.
            setContactVelocity(calculateLocalVelocity(0, duration));
            if (body[1]!=null) {
                getContactVelocity().subVector(calculateLocalVelocity(1, duration));
            }

            // Calculate the desired change in velocity for resolution
            calculateDesiredDeltaVelocity(duration);
        }

        

        /**
         * Calculates the separating velocity at this contact.
         */
        double calculateSeparatingVelocity(){
            Vector3 relativeVelocity = new Vector3(body[0].getVelocity());
            if (body[1]!=null) relativeVelocity.subVector(body[1].getVelocity());
            return relativeVelocity.scalarProduct(getContactNormal());
        }
        
        /**
         * Reverses the contact. This involves swapping the two rigid bodies
         * and reversing the contact normal. The internal values should then
         * be recalculated using calculateInternals (this is not done
         * automatically).
         */
        void swapBodies(){
            getContactNormal().multiVectorScalar(-1);

            RigidBody temp = body[0];
            body[0] = body[1];
            body[1] = temp;
        }

        /**
         * Updates the awake state of rigid bodies that are taking
         * place in the given contact. A body will be made awake if it
         * is in contact with a body that is awake.
         */
        void matchAwakeState()
        {
            // Collisions with the world never cause a body to wake up.
            if (body[1]==null) return;

            boolean body0awake = body[0].isAwake();
            boolean body1awake = body[1].isAwake();

            // Wake up only the sleeping one
            if (body0awake ^ body1awake) {
                if (body0awake) body[1].setAwake(true);
                else body[0].setAwake(true);
            }
        }

        /**
         * Calculates and sets the internal value for the desired delta
         * velocity.
         */
        
        //Parece ok...
        void calculateDesiredDeltaVelocity(double duration){
             double velocityLimit = (double)0.25f;

            // Calculate the acceleration induced velocity accumulated this frame
            double velocityFromAcc = 0;

            /*if (body[0].getAwake())
            {

            	//No original:O que não faz nada...
            	body[0]->getLastFrameAcceleration() * duration * contactNormal;
            	
            }*/

            	//Vector3 v0=new Vector3(body[0].getLastFrameAcceleration());
            	//Vector3 v1=new Vector3();
            	//v1.getMultiVector(value)
             	//v1.addScaledVector(contactNormal, duration) ;
             	//v0=v0.componentProduct(contactNormal.getMultiVector(duration));
             	//double d//=v0.magnitude();
            
            
           // velocityFromAcc=body[0].getLastFrameAcceleration().getMultiVector(getContactNormal().getMultiVector(duration)).magnitude();
            // 	velocityFromAcc=v.magnitude();
             	            
            if (body[1]!=null)
            	if (body[1].isAwake())
            {
            	/*	Vector3 v=new Vector3(body[1].getLastFrameAcceleration());
                	v.addScaledVector(contactNormal, duration) ;
               	velocityFromAcc-=v.magnitude();*/
            		velocityFromAcc-=body[1].getLastFrameAcceleration().getMultiVector(getContactNormal().getMultiVector(duration)).magnitude();
            }

            // If the velocity is very slow, limit the restitution
            double thisRestitution = getRestitution();
            if (Math.abs(getContactVelocity().magnitude()) < velocityLimit)
            {
           //     thisRestitution = (double)0.0f;
            }

            // Combine the bounce velocity with the removed
            // acceleration velocity.
            
            //coloquei magnitude ao invés de "x"
            setDesiredDeltaVelocity(getContactVelocity().magnitude()
			-thisRestitution * (getContactVelocity().magnitude() - velocityFromAcc));
        }

        /**
         * Calculates and returns the velocity of the contact
         * point on the given body.
         */
        //ok
        Vector3 calculateLocalVelocity(int bodyIndex, double duration){
            RigidBody thisBody = body[bodyIndex];

            // Work out the velocity of the contact point.
            Vector3 velocity =
                thisBody.getRotation().getVectorProduct(relativeContactPosition[bodyIndex]);
            velocity.addVector(thisBody.getVelocity());

            // Turn the velocity into contact-coordinates.
            Vector3 contactVelocity=(getContactToWorld().transformTranspose(velocity));

            // Calculate the ammount of velocity that is due to forces without
            // reactions.
            Vector3 accVelocity = thisBody.getLastFrameAcceleration().getMultiVector(duration);

            // Calculate the velocity in contact-coordinates.
            accVelocity = getContactToWorld().transformTranspose(accVelocity);

            // We ignore any component of acceleration in the contact normal
            // direction, we are only interested in planar acceleration
            accVelocity.x = 0;

            // Add the planar velocities - if there's enough friction they will
            // be removed during velocity resolution
            contactVelocity.addVector(accVelocity);

            // And return it
            return contactVelocity;
        }

        /**
         * Calculates an orthonormal basis for the contact point, based on
         * the primary friction direction (for anisotropic friction) or
         * a random orientation (for isotropic friction).
         */
        //ok
        void calculateContactBasis()
        {
            Vector3 contactTangent[]=new Vector3[2];
            contactTangent[0]=new Vector3();
            contactTangent[1]=new Vector3();

            // Check whether the Z-axis is nearer to the X or Y axis
            if (Math.abs(getContactNormal().x) > Math.abs(getContactNormal().y)){
                // Scaling factor to ensure the results are normalised
                 double s = (double)1.0f/Math.sqrt(getContactNormal().z*getContactNormal().z +
                    getContactNormal().x*getContactNormal().x);

                // The new X-axis is at right angles to the world Y-axis
                contactTangent[0].x = getContactNormal().z*s;
                contactTangent[0].y = 0;
                contactTangent[0].z = -getContactNormal().x*s;

                // The new Y-axis is at right angles to the new X- and Z- axes
                contactTangent[1].x = getContactNormal().y*contactTangent[0].x;
                contactTangent[1].y = getContactNormal().z*contactTangent[0].x -
                    getContactNormal().x*contactTangent[0].z;
                contactTangent[1].z = -getContactNormal().y*contactTangent[0].x;
            }
            else
            {
                // Scaling factor to ensure the results are normalised
                 double s = (double)1.0/Math.sqrt(getContactNormal().z*getContactNormal().z +
                    getContactNormal().y*getContactNormal().y);

                // The new X-axis is at right angles to the world X-axis
                contactTangent[0].x = 0;
                contactTangent[0].y = -getContactNormal().z*s;
                contactTangent[0].z = getContactNormal().y*s;

                // The new Y-axis is at right angles to the new X- and Z- axes
                contactTangent[1].x = getContactNormal().y*contactTangent[0].z -
                    getContactNormal().z*contactTangent[0].y;
                contactTangent[1].y = -getContactNormal().x*contactTangent[0].z;
                contactTangent[1].z = getContactNormal().x*contactTangent[0].y;
            }

            // Make a matrix from the three vectors.
            getContactToWorld().setComponents(
                getContactNormal(),
                contactTangent[0],
                contactTangent[1]);
        }

        /**
         * Applies an impulse to the given body, returning the
         * change in velocities.
         */
     //   void applyImpulse( Vector3 impulse, RigidBody body,
//                           Vector3 velocityChange, Vector3 rotationChange);

        /**
         * Performs an inertia-weighted impulse based resolution of this
         * contact alone.
         * 
         * ok
         */
        void applyVelocityChange(Vector3 velocityChange[],
                                 Vector3 rotationChange[]){
            // Get hold of the inverse mass and inverse inertia tensor, both in
            // world coordinates.
            Matrix3 inverseInertiaTensor[]=new Matrix3[2];
            inverseInertiaTensor[0]=body[0].getInverseInertiaTensorWorld();
            
            if (body[1]!=null){
            	inverseInertiaTensor[1]=body[1].getInverseInertiaTensorWorld();
            }

            // We will calculate the impulse for each contact axis
            Vector3 impulseContact;
            
           
            if (friction == (double)0.0)
            {
                // Use the short format for frictionless contacts
                impulseContact = calculateFrictionlessImpulse(inverseInertiaTensor);
            }
            else
            {
                // Otherwise we may have impulses that aren't in the direction of the
                // contact, so we need the more complex version.
                impulseContact = calculateFrictionImpulse(inverseInertiaTensor);
            }

            // Convert impulse to world coordinates
            Vector3 impulse = getContactToWorld().transform(impulseContact);
           // impulse.normalise(100);

            // Split in the impulse into linear and rotational components
            Vector3 impulsiveTorque = relativeContactPosition[0].getVectorProduct(impulse);
            rotationChange[0] = inverseInertiaTensor[0].transform(impulsiveTorque);
            velocityChange[0].clear();
            velocityChange[0].addScaledVector(impulse, body[0].getInverseMass());

            // Apply the changes
            body[0].addVelocity(velocityChange[0]);
            body[0].addRotation(rotationChange[0]);

            if (body[1]!=null)
            {
                // Work out body one's linear and angular changes
                impulsiveTorque = impulse.getVectorProduct(relativeContactPosition[1]);
                if (inverseInertiaTensor[1]!=null) {
                	rotationChange[1] = inverseInertiaTensor[1].transform(impulsiveTorque);
                } else {
                	rotationChange[1] = body[1].getInverseInertiaTensor().transform(impulsiveTorque);
                }
                velocityChange[1].clear();
                velocityChange[1].addScaledVector(impulse, -body[1].getInverseMass());

                // And apply them.
                body[1].addVelocity(velocityChange[1]);
                body[1].addRotation(rotationChange[1]);
            }
        }

        /**
         * Performs an inertia weighted penetration resolution of this
         * contact alone.
         */
        void applyPositionChange(Vector3 linearChange[],
                                 Vector3 angularChange[],
                                 double penetration){
             double angularLimit = (double)0.2f;
            double angularMove[]=new double[2];
            double linearMove[]=new double[2];

            double totalInertia = 0;
            double linearInertia[]=new double[2];
            double angularInertia[]=new double[2];

            // We need to work out the inertia of each object in the direction
            // of the contact normal, due to angular inertia only.
            for (int i = 0; i < 2; i++) if (body[i]!=null)
            {
                Matrix3 inverseInertiaTensor;
                inverseInertiaTensor= body[i].getInverseInertiaTensorWorld();

                // Use the same procedure as for calculating frictionless
                // velocity change to work out the angular inertia.
                Vector3 angularInertiaWorld =
                    relativeContactPosition[i].getVectorProduct(getContactNormal());
                angularInertiaWorld =
                    inverseInertiaTensor.transform(angularInertiaWorld);
                angularInertiaWorld =
                    angularInertiaWorld.getVectorProduct(relativeContactPosition[i]);
                angularInertia[i] =
                    angularInertiaWorld.scalarProduct(getContactNormal());

                // The linear component is simply the inverse mass
                linearInertia[i] = body[i].getInverseMass();

                // Keep track of the total inertia from all components
                totalInertia += linearInertia[i] + angularInertia[i];

                // We break the loop here so that the totalInertia value is
                // completely calculated (by both iterations) before
                // continuing.
            }

            // Loop through again calculating and applying the changes
            for (int i = 0; i < 2; i++) if (body[i]!=null)
            {
                // The linear and angular movements required are in proportion to
                // the two inverse inertias.
                double sign = (i == 0)?1:-1;
                angularMove[i] =
                    sign * penetration * (angularInertia[i] / totalInertia);
                linearMove[i] =
                    sign * penetration * (linearInertia[i] / totalInertia);

                // To avoid angular projections that are too great (when mass is large
                // but inertia tensor is small) limit the angular move.
                Vector3 projection = new Vector3(relativeContactPosition[i]);
                projection.addScaledVector(
                    getContactNormal(),
                    -relativeContactPosition[i].scalarProduct(getContactNormal())
                    );

                // Use the small angle approximation for the sine of the angle (i.e.
                // the magnitude would be sine(angularLimit) * projection.magnitude
                // but we approximate sine(angularLimit) to angularLimit).
                double maxMagnitude = angularLimit * projection.magnitude();

                if (angularMove[i] < -maxMagnitude)
                {
                    double totalMove = angularMove[i] + linearMove[i];
                    angularMove[i] = -maxMagnitude;
                    linearMove[i] = totalMove - angularMove[i];
                }
                else if (angularMove[i] > maxMagnitude)
                {
                    double totalMove = angularMove[i] + linearMove[i];
                    angularMove[i] = maxMagnitude;
                    linearMove[i] = totalMove - angularMove[i];
                }

                // We have the linear amount of movement required by turning
                // the rigid body (in angularMove[i]). We now need to
                // calculate the desired rotation to achieve that.
                if (angularMove[i] == 0)
                {
                    // Easy case - no angular movement means no rotation.
                    angularChange[i].clear();
                }
                else
                {
                    // Work out the direction we'd like to rotate in.
                    Vector3 targetAngularDirection =
                        relativeContactPosition[i].getVectorProduct(getContactNormal());

                    Matrix3 inverseInertiaTensor;
                    inverseInertiaTensor=body[i].getInverseInertiaTensorWorld();

                    // Work out the direction we'd need to rotate to achieve that
                    angularChange[i] =
                        inverseInertiaTensor.transform(targetAngularDirection).getMultiVector(angularMove[i] / angularInertia[i]);
                }

                // Velocity change is easier - it is just the linear movement
                // along the contact normal.
                linearChange[i] = getContactNormal().getMultiVector(linearMove[i]);

                // Now we can start to apply the values we've calculated.
                // Apply the linear movement
                Vector3 pos;
                pos=body[i].getPosition();
                pos.addScaledVector(getContactNormal(), linearMove[i]);
               // body[i].setPosition(pos);

                // And the change in orientation
                Quaternion q;
                q= body[i].getOrientation();
                q.addScaledVector(angularChange[i], ((double)1.0));
                //body[i].setOrientation(q);

                // We need to calculate the derived data for any body that is
                // asleep, so that the changes are reflected in the object's
                // data. Otherwise the resolution will not change the position
                // of the object, and the next collision detection round will
                // have the same penetration.
                if (!body[i].isAwake()) body[i].calculateDerivedData();
            }
        }


        /**
         * Calculates the impulse needed to resolve this contact,
         * given that the contact has no friction. A pair of inertia
         * tensors - one for each contact object - is specified to
         * save calculation time: the calling function has access to
         * these anyway.
         */
        Vector3 calculateFrictionlessImpulse(Matrix3 inverseInertiaTensor[]){
            Vector3 impulseContact=new Vector3();

            // Build a vector that shows the change in velocity in
            // world space for a unit impulse in the direction of the contact
            // normal.
            Vector3 deltaVelWorld = relativeContactPosition[0].getVectorProduct(getContactNormal());
            deltaVelWorld = inverseInertiaTensor[0].transform(deltaVelWorld);
            deltaVelWorld = deltaVelWorld.getVectorProduct(relativeContactPosition[0]);

            // Work out the change in velocity in contact coordiantes.
            double deltaVelocity = deltaVelWorld.scalarProduct(getContactNormal());

            // Add the linear component of velocity change
            deltaVelocity += body[0].getInverseMass();

            // Check if we need to the second body's data
            if (body[1]!=null)
            {
                // Go through the same transformation sequence again
            	deltaVelWorld = relativeContactPosition[1].getVectorProduct(getContactNormal());
                deltaVelWorld = inverseInertiaTensor[1].transform(deltaVelWorld);
                deltaVelWorld = deltaVelWorld.getVectorProduct(relativeContactPosition[1]);

                // Add the change in velocity due to rotation
                deltaVelocity += deltaVelWorld.scalarProduct(getContactNormal());

                // Add the change in velocity due to linear motion
                deltaVelocity += body[1].getInverseMass();
            }

            // Calculate the required size of the impulse
            impulseContact.x = getDesiredDeltaVelocity() / deltaVelocity;
            impulseContact.y = 0;
            impulseContact.z = 0;
            return impulseContact;
        }


        /**
         * Calculates the impulse needed to resolve this contact,
         * given that the contact has a non-zero coefficient of
         * friction. A pair of inertia tensors - one for each contact
         * object - is specified to save calculation time: the calling
         * function has access to these anyway.
         */
        //ok aparentemente
        Vector3 calculateFrictionImpulse(Matrix3 inverseInertiaTensor[]){
            Vector3 impulseContact;
            double inverseMass = body[0].getInverseMass();

            // The equivalent of a cross product in matrices is multiplication
            // by a skew symmetric matrix - we build the matrix for converting
            // between linear and angular quantities.
            Matrix3 impulseToTorque=new Matrix3();
            impulseToTorque.setSkewSymmetric(relativeContactPosition[0]);

            // Build the matrix to convert contact impulse to change in velocity
            // in world coordinates.
            Matrix3 deltaVelWorld = new Matrix3(impulseToTorque);
            deltaVelWorld.multi(inverseInertiaTensor[0]);
            deltaVelWorld.multi(impulseToTorque);
            deltaVelWorld.multiScalar(-1);

            // Check if we need to add body two's data
            if (body[1]!=null)
            {
                // Set the cross product matrix
                impulseToTorque.setSkewSymmetric(relativeContactPosition[1]);

                // Calculate the velocity change matrix
                Matrix3 deltaVelWorld2 = new Matrix3(impulseToTorque);
                
	           // if (inverseInertiaTensor[1]!=null){
	            	deltaVelWorld2.multi(inverseInertiaTensor[1]);
	          //  } else{
	          //  	deltaVelWorld2.multi(body[1].getInverseInertiaTensor());
	           // }
                deltaVelWorld2.multi(impulseToTorque);
                deltaVelWorld2.multiScalar(-1);

                // Add to the total delta velocity.
                deltaVelWorld.addMatrix(deltaVelWorld2);

                // Add to the inverse mass
                inverseMass += body[1].getInverseMass();
            }

            // Do a change of basis to convert into contact coordinates.
            Matrix3 deltaVelocity = getContactToWorld().transpose();
            deltaVelocity.multi(deltaVelWorld);
            deltaVelocity.multi(getContactToWorld());

            // Add in the linear velocity change
            deltaVelocity.data[0] += inverseMass;
            deltaVelocity.data[4] += inverseMass;
            deltaVelocity.data[8] += inverseMass;

            // Invert to get the impulse needed per unit velocity
            Matrix3 impulseMatrix = deltaVelocity.inverse();

            // Find the target velocities to kill
            Vector3 velKill=new Vector3(getDesiredDeltaVelocity(),
                -getContactVelocity().y,
                -getContactVelocity().z);

            // Find the impulse to kill target velocities
            impulseContact = impulseMatrix.transform(velKill);

            // Check for exceeding friction
            double planarImpulse = Math.sqrt(
                impulseContact.y*impulseContact.y +
                impulseContact.z*impulseContact.z
                );
            if (planarImpulse > impulseContact.x * friction)
            {
                // We need to use dynamic friction
                impulseContact.y /= planarImpulse;
                impulseContact.z /= planarImpulse;

                impulseContact.x = deltaVelocity.data[0] +
                    deltaVelocity.data[1]*friction*impulseContact.y +
                    deltaVelocity.data[2]*friction*impulseContact.z;
                impulseContact.x = getDesiredDeltaVelocity() / impulseContact.x;
                impulseContact.y *= friction * impulseContact.x;
                impulseContact.z *= friction * impulseContact.x;
            }
            return impulseContact;
        }
        
        
        
        /**
         * Resolves this contact, for both velocity and interpenetration.
         */
       private void resolve(double duration){
           resolveVelocity(duration);
            resolveInterpenetration(duration);

        }
        
        
        /**
         * Handles the impulse calculations for this collision.
         */
        private void resolveVelocity(double duration){
        	
            // Find the velocity in the direction of the contact
            double separatingVelocity = calculateSeparatingVelocity();

            // Check if it needs to be resolved
            if (separatingVelocity >= 0)
            {
                // The contact is either separating, or stationary - there's
                // no impulse required.
                return;
            }

          /*  // Calculate the new separating velocity
            double newSepVelocity = -separatingVelocity * restitution;

            // Check the velocity build-up due to acceleration only
            Vector3 accCausedVelocity = body[0].getAcceleration();
            if (body[1]!=null) accCausedVelocity -= body[1].getAcceleration();
            double accCausedSepVelocity = accCausedVelocity * contactNormal * duration;

            // If we've got a closing velocity due to acelleration build-up,
            // remove it from the new separating velocity
            if (accCausedSepVelocity < 0)
            {
                newSepVelocity += restitution * accCausedSepVelocity;

                // Make sure we haven't removed more than was
                // there to remove.
                if (newSepVelocity < 0) newSepVelocity = 0;
            }*/

           // double deltaVelocity = newSepVelocity - separatingVelocity;
            double deltaVelocity =  -separatingVelocity;

            // We apply the change in velocity to each object in proportion to
            // their inverse mass (i.e. those with lower inverse mass [higher
            // actual mass] get less change in velocity)..
            double totalInverseMass = body[0].getInverseMass();
            if (body[1]!=null) totalInverseMass += body[1].getInverseMass();

            // If all particles have infinite mass, then impulses have no effect
            if (totalInverseMass <= 0) return;

            // Calculate the impulse to apply
            double impulse = deltaVelocity / totalInverseMass;

            // Find the amount of impulse per unit of inverse mass
            Vector3 impulsePerIMass = getContactNormal().getMultiVector(impulse);

            // Apply impulses: they are applied in the direction of the contact,
            // and are proportional to the inverse mass.
            body[0].getVelocity().addVector(
                impulsePerIMass.getMultiVector(body[0].getInverseMass()));
            if (body[1]!=null)
            {
                // Particle 1 goes in the opposite direction
                body[1].getVelocity().addVector(
                		impulsePerIMass.getMultiVector(-body[1].getInverseMass()));
            }      
            
        }

        /**
         * Handles the interpenetration resolution for this contact.
         */
        private void resolveInterpenetration(double duration){
        	
            // If we don't have any penetration, skip this step.
            if (getPenetration() <= 0) return;

            // The movement of each object is based on their inverse mass, so
            // total that.
            double totalInverseMass = body[0].getInverseMass();
            if (body[1]!=null) totalInverseMass += body[1].getInverseMass();

            // If all particles have infinite mass, then we do nothing
            if (totalInverseMass <= 0) return;

            // Find the amount of penetration resolution per unit of inverse mass
            Vector3 movePerIMass = getContactNormal().getMultiVector(getPenetration() / totalInverseMass);

            // Calculate the the movement amounts
            particleMovement[0]=movePerIMass.getMultiVector(body[0].getInverseMass());
            
            if (body[1]!=null) {
            	particleMovement[1] = movePerIMass.getMultiVector(-body[1].getInverseMass());
            } else {
            	particleMovement[1].clear();
            }

            // Apply the penetration resolution
            body[0].getPosition().addVector(particleMovement[0]);
            //body[0].setPosition(body[0].getPosition() + particleMovement[0]);
            if (body[1]!=null) {
            	body[1].getPosition().addVector(particleMovement[1]);
            }
        }

		public void setPenetration(double penetration) {
			this.penetration = penetration;
		}

		public double getPenetration() {
			return penetration;
		}

		public void setRestitution(double restitution) {
			this.restitution = restitution;
		}

		public double getRestitution() {
			return restitution;
		}

		public void setContactPoint(Vector3 contactPoint) {
			this.contactPoint = contactPoint;
		}

		public Vector3 getContactPoint() {
			return contactPoint;
		}

		public void setContactNormal(Vector3 contactNormal) {
			this.contactNormal = contactNormal;
		}

		public Vector3 getContactNormal() {
			return contactNormal;
		}

		public void setContactToWorld(Matrix3 contactToWorld) {
			this.contactToWorld = contactToWorld;
		}

		public Matrix3 getContactToWorld() {
			return contactToWorld;
		}

		public void setContactVelocity(Vector3 contactVelocity) {
			this.contactVelocity = contactVelocity;
		}

		public Vector3 getContactVelocity() {
			return contactVelocity;
		}

		public Vector3 getRelativeContactPosition(int b) {
			return relativeContactPosition[b];
		}

		public void setDesiredDeltaVelocity(double desiredDeltaVelocity) {
			this.desiredDeltaVelocity = desiredDeltaVelocity;
		}

		public double getDesiredDeltaVelocity() {
			return desiredDeltaVelocity;
		}

}
