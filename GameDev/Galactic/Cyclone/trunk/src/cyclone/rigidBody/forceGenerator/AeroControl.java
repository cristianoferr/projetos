package cyclone.rigidBody.forceGenerator;

import cyclone.entities.RigidBody;
import cyclone.math.Matrix3;
import cyclone.math.Vector3;

public class AeroControl extends Aero {
    /**
	    * A force generator with a control aerodynamic surface. This
	    * requires three inertia tensors, for the two extremes and
	    * 'resting' position of the control surface.  The latter tensor is
	    * the one inherited from the base class, the two extremes are
	    * defined in this class.
	    */
	        /**
	         * The aerodynamic tensor for the surface, when the control is at
	         * its maximum value.
	         */
	        Matrix3 maxTensor;

	        /**
	         * The aerodynamic tensor for the surface, when the control is at
	         * its minimum value.
	         */
	        Matrix3 minTensor;

	        /**
	        * The current position of the control for this surface. This
	        * should range between -1 (in which case the minTensor value
	        * is used), through 0 (where the base-class tensor value is
	        * used) to +1 (where the maxTensor value is used).
	        */
	        double controlSetting;

	        /**
	         * Calculates the final aerodynamic tensor for the current
	         * control setting.
	         */
	        Matrix3 getTensor(){
	            if (controlSetting <= -1.0f) return minTensor;
	            else if (controlSetting >= 1.0f) return maxTensor;
	            else if (controlSetting < 0)
	            {
	                return Matrix3.linearInterpolate(minTensor, tensor, controlSetting+1.0f);
	            }
	            else if (controlSetting > 0)
	            {
	                return Matrix3.linearInterpolate(tensor, maxTensor, controlSetting);
	            }
	            else return tensor;
	        }
	    
	        /**
	         * Creates a new aerodynamic control surface with the given
	         * properties.
	         */
	        public AeroControl( Matrix3 base,
	                     Matrix3 min,  Matrix3 max,
	                     Vector3 position,  Vector3 windspeed){
	        	super(base, position, windspeed);
	        	    minTensor = min;
	        	    maxTensor = max;
	        	    controlSetting = 0.0f;
	        	}


	        /**
	         * Sets the control position of this control. This * should
	        range between -1 (in which case the minTensor value is *
	        used), through 0 (where the base-class tensor value is used) *
	        to +1 (where the maxTensor value is used). Values outside that
	        * range give undefined results.
	        */
	        public void setControl(double value){
	            controlSetting = value;
	        }

	        /**
	         * Applies the force to the given rigid body.
	         */
	        public void updateForce(RigidBody body, double duration){
	            Matrix3 tensor = getTensor();
	            super.updateForceFromTensor(body, duration, tensor);
	        }
}
