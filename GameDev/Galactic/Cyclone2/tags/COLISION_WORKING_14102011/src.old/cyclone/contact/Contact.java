package cyclone.contact;

import cyclone.entities.RigidBody;
import cyclone.math.Matrix3;
import cyclone.math.Quaternion;
import cyclone.math.Vector3;

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
    public RigidBody body[];
    /**
     * Holds the amount each particle is moved by during interpenetration
     * resolution.
     */
    Vector3 particleMovement[];
        /**
         * Holds the lateral friction coefficient at the contact.
         */
        double friction;

        /**
         * Holds the normal restitution coefficient at the contact.
         */
        public double restitution;

        /**
         * Holds the position of the contact in world coordinates.
         */
        public Vector3 contactPoint;

        /**
         * Holds the direction of the contact in world coordinates.
         */
        public Vector3 contactNormal;

        /**
         * Holds the depth of penetration at the contact point. If both
         * bodies are specified then the contact point should be midway
         * between the inter-penetrating points.
         */
        public double penetration;

        
        public Contact(){
        	body=new RigidBody[2];
        	relativeContactPosition=new Vector3[2];
        	particleMovement=new Vector3[2];
        	particleMovement[0]=new Vector3(0,0,0);
        	particleMovement[1]=new Vector3(0,0,0);        	
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
        	    this.restitution = restitution;
        	}



        /**
         * A transform matrix that converts co-ordinates in the contact's
         * frame of reference to world co-ordinates. The columns of this
         * matrix form an orthonormal set of vectors.
         */
        Matrix3 contactToWorld;

        /**
         * Holds the closing velocity at the point of contact. This is set
         * when the calculateInternals function is run.
         */
        Vector3 contactVelocity;

        /**
         * Holds the required change in velocity for this contact to be
         * resolved.
         */
        double desiredDeltaVelocity;

        /**
         * Holds the world space position of the contact point relative to
         * centre of each body. This is set when the calculateInternals
         * function is run.
         */
        Vector3 relativeContactPosition[];


        /**
         * Calculates internal data from state data. This is called before
         * the resolution algorithm tries to do any resolution. It should
         * never need to be called manually.
         */
        void calculateInternals(double duration)
        {
            // Check if the first object is NULL, and swap if it is.
            if (body[0]==null) swapBodies();
            assert(body[0]==null);

            // Calculate an set of axis at the contact point.
            calculateContactBasis();

            // Store the relative position of the contact relative to each body
            relativeContactPosition[0] = contactPoint.getSubVector(body[0].getPosition());
            if (body[1]!=null) {
                relativeContactPosition[1] = contactPoint.getSubVector(body[1].getPosition());
            }

            // Find the relative velocity of the bodies at the contact point.
            contactVelocity = calculateLocalVelocity(0, duration);
            if (body[1]!=null) {
                contactVelocity.subVector(calculateLocalVelocity(1, duration));
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
            return relativeVelocity.scalarProduct(contactNormal);
        }
        
        /**
         * Reverses the contact. This involves swapping the two rigid bodies
         * and reversing the contact normal. The internal values should then
         * be recalculated using calculateInternals (this is not done
         * automatically).
         */
        void swapBodies(){
            contactNormal.multiVectorScalar(-1);

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

            boolean body0awake = body[0].getAwake();
            boolean body1awake = body[1].getAwake();

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
        void calculateDesiredDeltaVelocity(double duration){
             double velocityLimit = (double)0.25f;

            // Calculate the acceleration induced velocity accumulated this frame
            double velocityFromAcc = 0;

            if (body[0].getAwake())
            {
            	Vector3 v=new Vector3(body[0].getLastFrameAcceleration());
            	v.addScaledVector(contactNormal, duration) ;
            	velocityFromAcc=v.magnitude();
            }

            if (body[1]!=null)
            	if (body[1].getAwake())
            {
            		Vector3 v=new Vector3(body[1].getLastFrameAcceleration());
                	v.addScaledVector(contactNormal, duration) ;
                	velocityFromAcc-=v.magnitude();
            }

            // If the velocity is very slow, limit the restitution
            double thisRestitution = restitution;
            if (Math.abs(contactVelocity.x) < velocityLimit)
            {
                thisRestitution = (double)0.0f;
            }

            // Combine the bounce velocity with the removed
            // acceleration velocity.
            desiredDeltaVelocity =
                -contactVelocity.x
                -thisRestitution * (contactVelocity.x - velocityFromAcc);
        }

        /**
         * Calculates and returns the velocity of the contact
         * point on the given body.
         */
        Vector3 calculateLocalVelocity(int bodyIndex, double duration){
            RigidBody thisBody = body[bodyIndex];

            // Work out the velocity of the contact point.
            Vector3 velocity =
                thisBody.getRotation().getVectorProduct(relativeContactPosition[bodyIndex]);
            velocity.addVector(thisBody.getVelocity());

            // Turn the velocity into contact-coordinates.
            Vector3 contactVelocity = contactToWorld.transformTranspose(velocity);

            // Calculate the ammount of velocity that is due to forces without
            // reactions.
            Vector3 accVelocity = thisBody.getLastFrameAcceleration().getMultiVector(duration);

            // Calculate the velocity in contact-coordinates.
            accVelocity = contactToWorld.transformTranspose(accVelocity);

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
        void calculateContactBasis()
        {
            Vector3 contactTangent[]=new Vector3[2];

            // Check whether the Z-axis is nearer to the X or Y axis
            if (Math.abs(contactNormal.x) > Math.abs(contactNormal.y))
            {
                // Scaling factor to ensure the results are normalised
                 double s = (double)1.0f/Math.sqrt(contactNormal.z*contactNormal.z +
                    contactNormal.x*contactNormal.x);

                // The new X-axis is at right angles to the world Y-axis
                contactTangent[0].x = contactNormal.z*s;
                contactTangent[0].y = 0;
                contactTangent[0].z = -contactNormal.x*s;

                // The new Y-axis is at right angles to the new X- and Z- axes
                contactTangent[1].x = contactNormal.y*contactTangent[0].x;
                contactTangent[1].y = contactNormal.z*contactTangent[0].x -
                    contactNormal.x*contactTangent[0].z;
                contactTangent[1].z = -contactNormal.y*contactTangent[0].x;
            }
            else
            {
                // Scaling factor to ensure the results are normalised
                 double s = (double)1.0/Math.sqrt(contactNormal.z*contactNormal.z +
                    contactNormal.y*contactNormal.y);

                // The new X-axis is at right angles to the world X-axis
                contactTangent[0].x = 0;
                contactTangent[0].y = -contactNormal.z*s;
                contactTangent[0].z = contactNormal.y*s;

                // The new Y-axis is at right angles to the new X- and Z- axes
                contactTangent[1].x = contactNormal.y*contactTangent[0].z -
                    contactNormal.z*contactTangent[0].y;
                contactTangent[1].y = -contactNormal.x*contactTangent[0].z;
                contactTangent[1].z = contactNormal.x*contactTangent[0].y;
            }

            // Make a matrix from the three vectors.
            contactToWorld.setComponents(
                contactNormal,
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
         */
        void applyVelocityChange(Vector3 velocityChange[],
                                 Vector3 rotationChange[]){
            // Get hold of the inverse mass and inverse inertia tensor, both in
            // world coordinates.
            Matrix3 inverseInertiaTensor[]=new Matrix3[2];
            inverseInertiaTensor[0]=body[0].getInverseInertiaTensorWorld();
            if (body[1]!=null)
                body[1].getInverseInertiaTensorWorld(inverseInertiaTensor[1]);

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
            Vector3 impulse = contactToWorld.transform(impulseContact);

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
                rotationChange[1] = inverseInertiaTensor[1].transform(impulsiveTorque);
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
                    relativeContactPosition[i].getVectorProduct(contactNormal);
                angularInertiaWorld =
                    inverseInertiaTensor.transform(angularInertiaWorld);
                angularInertiaWorld =
                    angularInertiaWorld.getVectorProduct(relativeContactPosition[i]);
                angularInertia[i] =
                    angularInertiaWorld.scalarProduct(contactNormal);

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
                Vector3 projection = relativeContactPosition[i];
                projection.addScaledVector(
                    contactNormal,
                    -relativeContactPosition[i].scalarProduct(contactNormal)
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
                        relativeContactPosition[i].getVectorProduct(contactNormal);

                    Matrix3 inverseInertiaTensor;
                    inverseInertiaTensor=body[i].getInverseInertiaTensorWorld();

                    // Work out the direction we'd need to rotate to achieve that
                    angularChange[i] =
                        inverseInertiaTensor.transform(targetAngularDirection).getMultiVector(angularMove[i] / angularInertia[i]);
                }

                // Velocity change is easier - it is just the linear movement
                // along the contact normal.
                linearChange[i] = contactNormal.getMultiVector(linearMove[i]);

                // Now we can start to apply the values we've calculated.
                // Apply the linear movement
                Vector3 pos;
                pos=body[i].getPosition();
                pos.addScaledVector(contactNormal, linearMove[i]);
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
                if (!body[i].getAwake()) body[i].calculateDerivedData();
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
            Vector3 deltaVelWorld = relativeContactPosition[0].getVectorProduct(contactNormal);
            deltaVelWorld = inverseInertiaTensor[0].transform(deltaVelWorld);
            deltaVelWorld = deltaVelWorld.getVectorProduct(relativeContactPosition[0]);

            // Work out the change in velocity in contact coordiantes.
            double deltaVelocity = deltaVelWorld.scalarProduct(contactNormal);

            // Add the linear component of velocity change
            deltaVelocity += body[0].getInverseMass();

            // Check if we need to the second body's data
            if (body[1]!=null)
            {
                // Go through the same transformation sequence again
               deltaVelWorld = relativeContactPosition[1].getVectorProduct(contactNormal);
                deltaVelWorld = inverseInertiaTensor[1].transform(deltaVelWorld);
                deltaVelWorld = deltaVelWorld.getVectorProduct(relativeContactPosition[1]);

                // Add the change in velocity due to rotation
                deltaVelocity += deltaVelWorld.scalarProduct(contactNormal);

                // Add the change in velocity due to linear motion
                deltaVelocity += body[1].getInverseMass();
            }

            // Calculate the required size of the impulse
            impulseContact.x = desiredDeltaVelocity / deltaVelocity;
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
            Matrix3 deltaVelWorld = impulseToTorque;
            deltaVelWorld.multi(inverseInertiaTensor[0]);
            deltaVelWorld.multi(impulseToTorque);
            deltaVelWorld.multiScalar(-1);

            // Check if we need to add body two's data
            if (body[1]!=null)
            {
                // Set the cross product matrix
                impulseToTorque.setSkewSymmetric(relativeContactPosition[1]);

                // Calculate the velocity change matrix
                Matrix3 deltaVelWorld2 = impulseToTorque;
                deltaVelWorld2.multi(inverseInertiaTensor[1]);
                deltaVelWorld2.multi(impulseToTorque);
                deltaVelWorld2.multiScalar(-1);

                // Add to the total delta velocity.
                deltaVelWorld.addMatrix(deltaVelWorld2);

                // Add to the inverse mass
                inverseMass += body[1].getInverseMass();
            }

            // Do a change of basis to convert into contact coordinates.
            Matrix3 deltaVelocity = contactToWorld.transpose();
            deltaVelocity.multi(deltaVelWorld);
            deltaVelocity.multi(contactToWorld);

            // Add in the linear velocity change
            deltaVelocity.data[0] += inverseMass;
            deltaVelocity.data[4] += inverseMass;
            deltaVelocity.data[8] += inverseMass;

            // Invert to get the impulse needed per unit velocity
            Matrix3 impulseMatrix = deltaVelocity.inverse();

            // Find the target velocities to kill
            Vector3 velKill=new Vector3(desiredDeltaVelocity,
                -contactVelocity.y,
                -contactVelocity.z);

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
                impulseContact.x = desiredDeltaVelocity / impulseContact.x;
                impulseContact.y *= friction * impulseContact.x;
                impulseContact.z *= friction * impulseContact.x;
            }
            return impulseContact;
        }
        
        
        
        /**
         * Resolves this contact, for both velocity and interpenetration.
         */
        void resolve(double duration){
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
            Vector3 impulsePerIMass = contactNormal.getMultiVector(impulse);

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
            if (penetration <= 0) return;

            // The movement of each object is based on their inverse mass, so
            // total that.
            double totalInverseMass = body[0].getInverseMass();
            if (body[1]!=null) totalInverseMass += body[1].getInverseMass();

            // If all particles have infinite mass, then we do nothing
            if (totalInverseMass <= 0) return;

            // Find the amount of penetration resolution per unit of inverse mass
            Vector3 movePerIMass = contactNormal.getMultiVector(penetration / totalInverseMass);

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

}
