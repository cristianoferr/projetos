package cyclone.forceGenerator.actuators;

import java.util.Vector;

import cyclone.entities.RigidBody;

public class ActuatorGroup {
	Vector<Actuator> slots=new Vector<Actuator>();
	String type="";
	public ActuatorGroup(){
	}


	public void addSlot(Actuator s){
		for (int i=0;i<slots.size();i++){
			if (slots.get(i)==s) return;
		}
		slots.add(s);
	}


	/**
	 * @return the slots
	 */
	public Vector<Actuator> getActuators() {
		return slots;
	}
	
	public Actuator getActuator(int i){
		return slots.get(i);
	}
	
	public void activate(RigidBody body,double force){
		for (int i=0;i<slots.size();i++){
			slots.get(i).activate(body,force);
		}
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
}
