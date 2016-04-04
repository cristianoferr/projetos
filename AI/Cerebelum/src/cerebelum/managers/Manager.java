package cerebelum.managers;


import java.util.Vector;

import cerebelum.behaviors.BehaviorAbstract;

import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.Entity.Logic.representation.Memory;

public class Manager {
Memory memory;
Vector<BehaviorAbstract> activeBehaviors=new Vector<BehaviorAbstract>();

//Quando um controle é necessário, ele é adicionado nesse vetor...
Vector<String> activeControls=new Vector<String>();

public Manager(Memory owner){
	this.memory=owner;
}
public void addBehavior(BehaviorAbstract b){
	activeBehaviors.add(b);
}


public void addControl(String ctl){
	if (!activeControls.contains(ctl)) activeControls.add(ctl);
}
public Memory getMemory() {
	return memory;
}

public ArtificialEntity getEntity() {
	return (ArtificialEntity)memory.getSelf().getRefersTo();
}
}
