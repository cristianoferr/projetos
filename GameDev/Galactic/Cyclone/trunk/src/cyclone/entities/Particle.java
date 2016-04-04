package cyclone.entities;

import cyclone.math.Vector3;

public class Particle {
    /**
     * @name Characteristic Data and State
     *
     * This data holds the state of the particle. There are two
     * sets of data: characteristics and state.
     *
     * Characteristics are properties of the particle
     * independent of its current kinematic situation. This
     * includes mass, moment of inertia and damping
     * properties. Two identical particles will have the same
     * values for their characteristics.
     *
     * State includes all the characteristics and also includes
     * the kinematic situation of the particle in the current
     * simulation. By setting the whole state data, a particle's
     * exact game state can be replicated. Note that state does
     * not include any forces applied to the body. Two identical
     * rigid bodies in the same simulation will not share the same
     * state values.
     *
     * The state values make up the smallest set of independent
     * data for the particle. Other state data is calculated
     * from their current values. When state data is changed the
     * dependent values need to be updated: this can be achieved
     * either by integrating the simulation, or by calling the
     * calculateInternals function. This two stage process is used
     * because recalculating internals can be a costly process:
     * all state changes should be carried out at the same time,
     * allowing for a single call.
     *
     * @see calculateInternals
     */
    /*@{*/

	
	boolean alive=true;
    /**
     * Holds the inverse of the mass of the particle. It
     * is more useful to hold the inverse mass because
     * integration is simpler, and because in double time
     * simulation it is more useful to have objects with
     * infinite mass (immovable) than zero mass
     * (completely unstable in numerical simulation).
     */
    double inverseMass=0;

    /**
     * Holds the amount of damping applied to linear
     * motion. Damping is required to remove energy added
     * through numerical instability in the integrator.
     */
  //  double damping=1;

    /**
     * Holds the linear position of the particle in
     * world space.
     */
    Vector3 position=new Vector3(0,0,0);;

    /**
     * Holds the linear velocity of the particle in
     * world space.
     */
    private Vector3 velocity=new Vector3(0,0,0);

    /*@}*/

    /**
     * @name Force Accumulators
     *
     * These data members store the current force and
     * global linear acceleration of the particle.
     */

    /*@{*/

    /**
     * Holds the accumulated force to be applied at the next
     * simulation iteration only. This value is zeroed at each
     * integration step.
     */
    Vector3 forceAccum=new Vector3(0,0,0);;

    

    /*@}*/

public void integrate(double duration){
    // We don't integrate things with zero mass.
    if (inverseMass <= 0.0f) return;

    assert(duration > 0.0);

    // Update linear position.
    position.addScaledVector(velocity, duration);

    // Work out the acceleration from the force
    //Vector3 resultingAcc = acceleration;
    
   // resultingAcc.addScaledVector(forceAccum, inverseMass);
    forceAccum.multiVectorScalar(inverseMass);

    // Update linear velocity from the acceleration.
   // velocity.addScaledVector(resultingAcc, duration);
    velocity.addScaledVector(forceAccum, duration);
    

    // Impose drag.
  //  velocity.multiVectorScalar(Math.pow(damping, duration));

    // Clear the forces.
    clearAccumulator();
}

    /*@}*/


    /**
     * @name Accessor Functions for the Particle's State
     *
     * These functions provide access to the particle's
     * characteristics or state.
     */
    /*@{*/

    /**
     * Sets the mass of the particle.
     *
     * @param mass The new mass of the body. This may not be zero.
     * Small masses can produce unstable rigid bodies under
     * simulation.
     *
     * @warning This invalidates internal data for the particle.
     * Either an integration function, or the calculateInternals
     * function should be called before trying to get any settings
     * from the particle.
     */
   public void setMass( double mass){
	    if (mass!=0)
	    	inverseMass = ((double)1.0)/mass;
	    
    }    	

    /**
     * Gets the mass of the particle.
     *
     * @return The current mass of the particle.
     */
   public double getMass() {
	   if (inverseMass == 0) {
	        return Double.MAX_VALUE;
	    } else {
	        return ((double)1.0)/inverseMass;
	    }
   }
    /**
     * Sets the inverse mass of the particle.
     *
     * @param inverseMass The new inverse mass of the body. This
     * may be zero, for a body with infinite mass
     * (i.e. unmovable).
     *
     * @warning This invalidates internal data for the particle.
     * Either an integration function, or the calculateInternals
     * function should be called before trying to get any settings
     * from the particle.
     */
   public void setInverseMass( double inverseMass){
    	this.inverseMass=inverseMass;
    }

    /**
     * Gets the inverse mass of the particle.
     *
     * @return The current inverse mass of the particle.
     */
    public double getInverseMass() {
    	return inverseMass;
    }

    /**
     * Returns true if the mass of the particle is not-infinite.
     */
    public boolean hasFiniteMass() {
    	return inverseMass >= 0.0f;
    }

    /**
     * Sets both the damping of the particle.
     */
/*    public void setDamping( double damping){
    	this.damping = damping;
    }
*/
    /**
     * Gets the current damping value.
     */
 /*   public double getDamping() {
    	return damping;
    }*/

    /**
     * Sets the position of the particle.
     *
     * @param position The new position of the particle.
     */
    public void setPosition( Vector3 position){
    	this.position=position;
    }

    /**
     * Sets the position of the particle by component.
     *
     * @param x The x coordinate of the new position of the rigid
     * body.
     *
     * @param y The y coordinate of the new position of the rigid
     * body.
     *
     * @param z The z coordinate of the new position of the rigid
     * body.
     */
    public void setPosition( double x,  double y,  double z){
    	 position.x = x;
    	 position.y = y;
    	 position.z = z;
    }

    /**
     * Fills the given vector with the position of the particle.
     *
     * @param position A pointer to a vector into which to write
     * the position.
     */
    /*public void getPosition(Vector3 position) {
    	position.x=this.position.x;
    	position.y=this.position.y;
    	position.z=this.position.z;
    }*/

    /**
     * Gets the position of the particle.
     *
     * @return The position of the particle.
     */
    public Vector3 getPosition(){
    	return position;
    }

    /**
     * Sets the velocity of the particle.
     *
     * @param velocity The new velocity of the particle.
     */
    public void setVelocity( Vector3 velocity){
        this.velocity = velocity;
    }

    /**
     * Sets the velocity of the particle by component.
     *
     * @param x The x coordinate of the new velocity of the rigid
     * body.
     *
     * @param y The y coordinate of the new velocity of the rigid
     * body.
     *
     * @param z The z coordinate of the new velocity of the rigid
     * body.
     */
    public void setVelocity( double x,  double y,  double z){
    //	System.out.println("setVelocity:"+x+" "+y+" "+z);
        velocity.x = x;
        velocity.y = y;
        velocity.z = z;
    }

    /**
     * Fills the given vector with the velocity of the particle.
     *
     * @param velocity A pointer to a vector into which to write
     * the velocity. The velocity is given in world local space.
     */
    
    /**
     * Gets the velocity of the particle.
     *
     * @return The velocity of the particle. The velocity is
     * given in world local space.
     */
    public Vector3 getVelocity() {
   // 	System.out.println("getVelocity:"+velocity);
    	return velocity;
    }

    /**
     * Sets the ant acceleration of the particle.
     *
     * @param acceleration The new acceleration of the particle.
     */
    /*public void setAcceleration( Vector3 acceleration){
    	this.acceleration=acceleration;
    }*/

    /**
     * Sets the ant acceleration of the particle by component.
     *
     * @param x The x coordinate of the new acceleration of the rigid
     * body.
     *
     * @param y The y coordinate of the new acceleration of the rigid
     * body.
     *
     * @param z The z coordinate of the new acceleration of the rigid
     * body.
     */
    /*public void setAcceleration( double x,  double y,  double z){
    	acceleration.x=x;
    	acceleration.y=y;
    	acceleration.z=z;
    }*/

    /**
     * Fills the given vector with the acceleration of the particle.
     *
     * @param acceleration A pointer to a vector into which to write
     * the acceleration. The acceleration is given in world local space.
     */
    //public void getAcceleration(Vector3 *acceleration) ;

    /**
     * Gets the acceleration of the particle.
     *
     * @return The acceleration of the particle. The acceleration is
     * given in world local space.
     */
  /*  public Vector3 getAcceleration() {
    	return acceleration;    	
    }*/

    /*@}*/

    /**
     * @name Force Set-up Functions
     *
     * These functions set up forces to apply to the
     * particle.
     */
    /*@{*/

    /**
     * Clears the forces applied to the particle. This will be
     * called automatically after each integration step.
     */
    public void clearAccumulator()
    	{
    	    forceAccum.clear();
    	}

    /**
     * Adds the given force to the particle, to be applied at the
     * next iteration only.
     *
     * @param force The force to apply.
     */
    public void addForce( Vector3 force){
    	//System.out.println("AddForce:"+force);
        forceAccum.addVector(force);
    }

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
