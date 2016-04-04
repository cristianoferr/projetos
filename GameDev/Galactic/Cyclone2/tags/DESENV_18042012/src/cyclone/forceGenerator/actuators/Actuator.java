package cyclone.forceGenerator.actuators;

import cristiano.math.Vector3;
import cyclone.entities.RigidBody;
import cyclone.entities.geom.GeomBox;
import cyclone.forceGenerator.ActuatorController;

public class Actuator {
	Vector3 orientation;
	Vector3 position;
	String type=ActuatorController.ACT_THRUSTER;
	boolean internal=true; //identifica quem deve rodar esse atuador: true=engine fisica, false=executado fora
	
	public Actuator(Vector3 position, Vector3 orientation){
		this.orientation=orientation;
		this.position=position;
	}
	
	public void applyForce(RigidBody body,double force){
		GeomBox geom=(GeomBox)body.getGeom();
		Vector3 f=body.getTransformMatrix().transformDirection(orientation.getMultiVector(force));
//System.out.println("ApplyForce: f:"+f+" position:"+position);
		body.addForceAtBodyPoint(f, geom.halfSize.getMultiVector(position));
	}
	
	public void activate(RigidBody body,double force){
		applyForce(body,force);
	}

	public Vector3 getOrientation() {
		return orientation;
	}

	public void setOrientation(Vector3 orientation) {
		this.orientation = orientation;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public boolean isInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	public String getType() {
		return type;
	}

	public void setGroup(String group) {
		this.type = group;
	}
}
