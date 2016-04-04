package cerebelum.aicore;

import java.util.Vector;


/**
 * Como visto no livro Artificial Inteliggence for Games - pg. 700
 * @author cmm4
 *
 */

class BehaviorRecord{
	String functionToCall;
	int frequency;
	int phase;
			
}

public class FrequencyScheduler {
	// Holds the list of behavior records
	Vector<BehaviorRecord> behaviors;
	
	 // Holds the current frame number
	 int frame;
	 
	 
	 public FrequencyScheduler(){
		 behaviors=new Vector<BehaviorRecord>();
		 frame=0;
	 }
	
		 // Adds a behavior to the list
	 public void addBehavior(String function, int frequency, int phase){
	
		 // Compile the record
		 BehaviorRecord record = new BehaviorRecord();
		 record.functionToCall = function;
		 record.frequency = frequency;
		 record.phase = phase;
		
		 // Add it to the list
		 behaviors.add(record);
	 }
	
	 // Called once per frame
	 public void run(){
	
	 // Increment the frame number
	 frame++;
	
	 //Go through each behavior
	 if (frame>=behaviors.size()){
		 frame=0;
	 }
	 BehaviorRecord behavior=behaviors.get(frame);
	 //TODO:implementar 
	 //behavior.thingToRun();
	 
	 
	 /*
	 for (int i = 0; i < behaviors.size(); i++) {
		 BehaviorRecord behavior=behaviors.get(i);
	
		 if (behavior.frequency % (frame + behavior.phase)==0){
			 
			 //TODO:implementar 
			 //behavior.thingToRun();
		 }
	 }*/
	
	
	 //If it is due, run it
	
	 }
}
