
package Robot3D;

/**
 * @author CMM4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

	
 


import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.vecmath.Vector3d;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.LampActuator;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import RedeNeural.Objeto;

public class SimbadRobot extends Agent {
	RangeSensorBelt sonars;
	RangeSensorBelt bumpers;
	CameraSensor camera;
    BufferedImage cameraImage;
	LampActuator lamp;
	SimbadPanel panel;
	Vector input=new Vector();
	Vector output=new Vector();
	LightSensor sensorLeft;
	LightSensor sensorRight;

	 

		public SimbadRobot (Vector3d position, String name) {     
			super(position,name);
			sonars = RobotFactory.addSonarBeltSensor(this,8);
			
			bumpers = RobotFactory.addBumperBeltSensor(this,8);
			//	add a camera on top of the robot
			camera = RobotFactory.addCameraSensor(this);
			// reserve space for image capture
			cameraImage = camera.createCompatibleImage();
			lamp=RobotFactory.addLamp(this);
			lamp.setOn(true);
			sensorLeft = RobotFactory.addLightSensorLeft(this);
			sensorRight = RobotFactory.addLightSensorRight(this);
			//RobotFactory.a

			panel=new SimbadPanel(this);
			setUIPanel(panel);



		}
		
		public void initBehavior() {}
    
    
		public Vector getInputs(){
		Vector vet=new Vector();
		
		if (collisionDetected()) {vet.add(new Integer(1));} 
		else {vet.add(new Integer(0));}
		
		vet=getInputsLightSensor(vet);
		vet=getInputsSonar(vet);		
		vet=getInputsBumper(vet);
		
		vet=getInputsVelocity(vet);
		
		vet=getInputsAngle(vet);
		
		//System.out.println("Size:"+vet.size());
		return vet;
		}
	/**
		 * @param vet
		 */
		private Vector getInputsAngle(Vector vet) {
			double angleVel=this.getRotationalVelocity();
			Vector v=Objeto.int2bin((int)0,10);
			if (angleVel>0) v.setElementAt(new Integer(1),0);
			if (angleVel>0.2) v.setElementAt(new Integer(1),1);
			if (angleVel>0.4) v.setElementAt(new Integer(1),2);
			if (angleVel>0.6) v.setElementAt(new Integer(1),3);
			if (angleVel>0.8) v.setElementAt(new Integer(1),4);
			if (angleVel<0) v.setElementAt(new Integer(1),5);
			if (angleVel<0.2) v.setElementAt(new Integer(1),6);
			if (angleVel<0.4) v.setElementAt(new Integer(1),7);
			if (angleVel<0.6) v.setElementAt(new Integer(1),8);
			if (angleVel<0.8) v.setElementAt(new Integer(1),9);
			for (int j=0;j<v.size();j++){
				vet.add(v.elementAt(j));
			}
			return vet;
		}

	/**
		 * @param vet
		 */
		private Vector getInputsVelocity(Vector vet) {
			double vel=this.getTranslationalVelocity();
			Vector v=Objeto.int2bin((int)0,2);
			if ((vel>0) && (vel<0.5)) v.setElementAt(new Integer(1),0);
			if ((vel>=0.5)) v.setElementAt(new Integer(1),1);
			for (int j=0;j<v.size();j++){
				vet.add(v.elementAt(j));
			}
			return vet;
		}

	private Vector getInputsLightSensor(Vector vet) {
		int size=5;
		
		float llum = sensorLeft.getAverageLuminance()*100;
		Vector v=Objeto.int2bin((int)0,size);
		if (llum>20) v.setElementAt(new Integer(1),0);
		if (llum>40) v.setElementAt(new Integer(1),1);
		if (llum>60) v.setElementAt(new Integer(1),2);
		if (llum>80) v.setElementAt(new Integer(1),3);
		if (llum>90) v.setElementAt(new Integer(1),4);

		
		for (int j=0;j<v.size();j++){
			vet.add(v.elementAt(j));
		}
		
		float rlum = sensorRight.getAverageLuminance()*100;
		v=Objeto.int2bin((int)0,size);
		
		if (rlum>20) v.setElementAt(new Integer(1),0);
		if (rlum>40) v.setElementAt(new Integer(1),1);
		if (rlum>60) v.setElementAt(new Integer(1),2);
		if (rlum>80) v.setElementAt(new Integer(1),3);
		if (rlum>90) v.setElementAt(new Integer(1),4);
		for (int j=0;j<v.size();j++){
			vet.add(v.elementAt(j));
		}
		//System.out.println("llum:"+llum+" rlum:"+rlum);
		return vet;
	}	
	private Vector getInputsBumper(Vector vet) {
				int size=5;
				for (int i=0;i< bumpers.getNumSensors();i++) {
					double range = bumpers.getMeasurement(i); 
					double angle = bumpers.getSensorAngle(i);
					boolean hit = bumpers.hasHit(i);
				
					if (hit) {
						vet.add(new Integer(1));
					}
					else { 
						vet.add(new Integer(0));
					} 
				}
				return vet;	
			}
		private Vector getInputsSonar(Vector vet) {
			int sonarSize=5;
			for (int i=0;i< sonars.getNumSensors();i++) {
				double range = sonars.getMeasurement(i); 
				double angle = sonars.getSensorAngle(i);
				boolean hit = sonars.hasHit(i);
				//System.out.println("i:"+i+" size:"+vet.size());
				if (hit) {
					vet.add(new Integer(1));
					
					double r=1/range;
					r=r*10;
					if (r>31) r=31;
					Vector v=Objeto.int2bin((int)0,sonarSize);
					String s="";
					if (r>5) v.setElementAt(new Integer(1),0);
					if (r>11) v.setElementAt(new Integer(1),1);
					if (r>17) v.setElementAt(new Integer(1),2);
					if (r>23) v.setElementAt(new Integer(1),3);
					if (r>29) v.setElementAt(new Integer(1),4);
				
					
					for (int j=0;j<v.size();j++){
						s=s+v.elementAt(j);
						vet.add(v.elementAt(j));
					}
					//System.out.println("Range:"+range+" 1/Range:"+1/range+" S:"+s );
				}
				else { 
					vet.add(new Integer(0));
					for (int j=0;j<sonarSize;j++) vet.add(new Integer(0));
				} 
			}	
			return vet;
		}
    
		public void performBehavior() {
			//printSonar();
			input=new Vector(getInputs());
			
			panel.setInput(input);
			panel.repaint();
			
			//printBumper();
			

			camera.copyVisionImage(cameraImage);

			if (collisionDetected()) {
				// stop the robot
				setTranslationalVelocity(0.0);
				setRotationalVelocity(0);
				this.resetPosition();
			} else {
				// progress at 0.5 m/s
				setTranslationalVelocity(0.5);
				// frequently change orientation 
				if ((getCounter() % 100)==0) 
				   setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
			}
		}
		private void printBumper() {
			//			every 20 frames
				  if (getCounter()%20==0){
					  // print each bumper state 
					  for (int i=0;i< bumpers.getNumSensors();i++) {
						  double angle = bumpers.getSensorAngle(i);
						  boolean hit = bumpers.hasHit(i);
						  System.out.println("Bumpers at angle "+ angle 
						  + " has hit something:"+hit); 
					  }
				  }
		}
		private void printSonar() {
			//every 20 frames
			if (getCounter()%20==0){
				// print each sonars measurement
				for (int i=0;i< sonars.getNumSensors();i++) {
					double range = sonars.getMeasurement(i); 
					double angle = sonars.getSensorAngle(i);
					boolean hit = sonars.hasHit(i);
					System.out.println("Sonar at angle "+ angle +
					"measured range ="+range+ " has hit something:"+hit); 
				}
			}
		}
	}

