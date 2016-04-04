package cyclone.forceGenerator;

import cristiano.math.PhysicsConsts;
import cristiano.math.Vector3;
import cyclone.entities.RigidBody;

public class SpaceGravity implements ForceGenerator {

	/**
     * A force generator that applies a gravitational force. One instance
     * can be used for multiple particles.
     */
    RigidBody emiter;

        /** Creates the generator with the given acceleration. */

	public SpaceGravity(RigidBody emiter){
		this.emiter=emiter;
	}
        /**
         * Overload this in implementations of the interface to calculate
         * and update the force applied to the given particle.
         */
        public void updateForce(RigidBody receiver, double duration){
            // Check that we do not have infinite mass
            if (!receiver.hasFiniteMass()) return;
            if (emiter==receiver) return;

            Vector3 dif=emiter.getPosition().getSubVector(receiver.getPosition());
    		double f=PhysicsConsts.Gforce* (emiter.getMass()*receiver.getMass())/Math.pow(dif.magnitude(),2);
    		dif.normalise();
    		//dif2.normalise();
    		receiver.addForce(dif.getMultiVector(f));
    		//b1.addForce(dif2.getMultiVector(f));
            
           
        }
		/**
		 * @return the emiter
		 */
		public RigidBody getEmiter() {
			return emiter;
		}
    

}
