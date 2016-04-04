package com.cristiano.cyclone.forceGenerator;

import java.util.HashMap;
import java.util.Vector;

import com.cristiano.cyclone.entities.RigidBody;
import com.cristiano.cyclone.forceGenerator.actuators.Actuator;
import com.cristiano.cyclone.forceGenerator.actuators.ActuatorGroup;
import com.cristiano.cyclone.forceGenerator.actuators.ActuatorRules;
import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Matrix3;
import com.cristiano.cyclone.utils.Vector3;



public class ActuatorController implements ForceGenerator {
	
	public final static String STEER_TURN_UP="STEER_TURN_UP";
	public final static String STEER_TURN_DOWN="STEER_TURN_DOWN";
	public final static String STEER_TURN_LEFT="STEER_TURN_LEFT";
	public final static String STEER_TURN_RIGHT="STEER_TURN_RIGHT";
	public final static String STEER_ROTATE_LEFT="STEER_ROTATE_LEFT";
	public final static String STEER_ROTATE_RIGHT="STEER_ROTATE_RIGHT";
	public final static String STEER_THRUST_UP="STEER_THRUST_UP";
	public final static String STEER_THRUST_DOWN="STEER_THRUST_DOWN";
	public final static String STEER_THRUST_LEFT="STEER_THRUST_LEFT";
	public final static String STEER_THRUST_RIGHT="STEER_THRUST_RIGHT";
	public final static String STEER_THRUST_FORWARD="STEER_THRUST_FORWARD";
	public final static String STEER_THRUST_BACKWARD="STEER_THRUST_BACKWARD";
	public static final String ACT_THRUSTER = "thruster";
	
	Vector<Actuator> actuators=new Vector<Actuator>();
	HashMap<String,ActuatorGroup> groups=new HashMap<String,ActuatorGroup>();
	
	public void addGroup(String group,ActuatorGroup ag){
		groups.put(group,ag);
	}
	
	public ActuatorGroup getGroup(String group){
		return groups.get(group);
	}
	
    /**
     * A force generator that applies an force in a direction .
     */
    
     

       

    
        /**
         * Creates a new aerodynamic force generator with the
         * given properties.
         */
        public ActuatorController( ){
        	initializeThrusters();
        	
        	new ActuatorRules(this).createGroups();
        	
        }

        public void addActuator(Vector3 position,Vector3 normal){
        	actuators.add(new Actuator(position,normal));
        }
        
        
        public void initializeThrusters(){
        	addActuator(new Vector3(0,0,-1),PhysicsConsts.ORIENT_EAST);//centro/centro/esquerda
        	addActuator(new Vector3(0,0,1),PhysicsConsts.ORIENT_WEST);//centro/centro/direita
        	addActuator(new Vector3(0,0,-0.98),PhysicsConsts.ORIENT_EAST);//centro/centro/esquerda
        	addActuator(new Vector3(0,0,0.98),PhysicsConsts.ORIENT_WEST);//centro/centro/direita
        	
        	addActuator(new Vector3(0.9,0.9,-0.9),PhysicsConsts.ORIENT_UP);//frente/alto/esquerda
        	addActuator(new Vector3(0.9,0.9,0.9),PhysicsConsts.ORIENT_UP);//frente/alto/direita
        	addActuator(new Vector3(0.9,-0.9,-0.9),PhysicsConsts.ORIENT_DOWN);//frente/baixo/esquerda
        	addActuator(new Vector3(0.9,-0.9,0.9),PhysicsConsts.ORIENT_DOWN);//frente/baixo/direita
        	
        	addActuator(new Vector3(-0.9,0.9,-0.9),PhysicsConsts.ORIENT_UP);//atras/alto/esquerda
        	addActuator(new Vector3(-0.9,0.9,0.9),PhysicsConsts.ORIENT_UP);//atras/alto/direita
        	addActuator(new Vector3(-0.9,-0.9,-0.9),PhysicsConsts.ORIENT_DOWN);//atras/baixo/esquerda
        	addActuator(new Vector3(-0.9,-0.9,0.9),PhysicsConsts.ORIENT_DOWN);//atras/baixo/direita
        	
        	addActuator(new Vector3(1,1,0),PhysicsConsts.ORIENT_UP);//frente/alto/meio
        	addActuator(new Vector3(1,-1,0),PhysicsConsts.ORIENT_DOWN);//frente/baixo/meio
        	addActuator(new Vector3(-1,1,0),PhysicsConsts.ORIENT_UP);//atras/alto/meio
        	addActuator(new Vector3(-1,-1,0),PhysicsConsts.ORIENT_DOWN);//atras/baixo/meio
        	addActuator(new Vector3(1,0,-1),PhysicsConsts.ORIENT_WEST);//frente/meio/esquerda
        	addActuator(new Vector3(1,0,1),PhysicsConsts.ORIENT_EAST);//frente/meio/direita
        	addActuator(new Vector3(1,0,-0.98),PhysicsConsts.ORIENT_WEST);//frente/meio/esquerda
        	addActuator(new Vector3(1,0,0.98),PhysicsConsts.ORIENT_EAST);//frente/meio/direita
        	addActuator(new Vector3(-1,0,-1),PhysicsConsts.ORIENT_WEST);//atras/meio/esquerda
        	addActuator(new Vector3(-1,0,1),PhysicsConsts.ORIENT_EAST);//atras/meio/direita
        	addActuator(new Vector3(-1,0,-0.98),PhysicsConsts.ORIENT_WEST);//atras/meio/esquerda
        	addActuator(new Vector3(-1,0,0.98),PhysicsConsts.ORIENT_EAST);//atras/meio/direita

        }
        
        /**
         * Applies the force to the given rigid body.
         */
        public void updateForce(RigidBody body, double duration){
        	   //this.updateForceFromTensor(body, duration, tensor);
        }

    
        /**
         * Uses an explicit tensor matrix to update the force on
         * the given rigid body. This is exactly the same as for updateForce
         * only it takes an explicit tensor.
         */
        void updateForceFromTensor(RigidBody body, double duration,
                                   Matrix3 tensor){
        	   // Calculate total velocity (windspeed and body's velocity).
            Vector3 velocity = body.getVelocity();
            //velocity.addVector(windspeed);

            // Calculate the velocity in body coordinates
            Vector3 bodyVel = body.getTransformMatrix().transformInverseDirection(velocity);

            // Calculate the force in body coordinates
            Vector3 bodyForce = tensor.transform(bodyVel);
            Vector3 force = body.getTransformMatrix().transformDirection(bodyForce);

            // Apply the force
            //body.addForceAtBodyPoint(force, position);
        }

		public Vector<Actuator> getActuators() {
			return actuators;
		}
		

		public void activateGroup(String slotGrouping,RigidBody body,double force) {
			getGroup(slotGrouping).activate(body,force);
			
		}
		
		public void activateGroup(ActuatorGroup slotGrouping,RigidBody body,double force) {
			slotGrouping.activate(body,force);
			
		}


}
