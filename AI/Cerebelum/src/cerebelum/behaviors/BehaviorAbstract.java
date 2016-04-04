package cerebelum.behaviors;

import cerebelum.managers.Manager;

import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;

public abstract class BehaviorAbstract {
Manager manager;
	
public BehaviorAbstract(Manager mng){
	this.manager=mng;
}
	public abstract void update();
	public void addControl(String ctl){
		manager.addControl(ctl);
	}
	public ArtificialEntity getEntity(){
		return manager.getEntity();
	}
}
