package cerebelum.behaviors;

import cerebelum.managers.Manager;

import com.cristiano.galactic.model.Entity.Logic.representation.MemoryItem;
import com.cristiano.galactic.model.bt.queries.QueryCentral;

public class AlignBehavior extends BehaviorAbstract {
	MemoryItem alignTo;
	public AlignBehavior(Manager mng,MemoryItem alignTo){
		super(mng);
		this.alignTo=alignTo;
	}
	
	@Override
	public void update() {

		if (QueryCentral.checkQuery(getEntity(),QueryCentral.query_TargetAlmostAlignedHoriz)<=0){
			
			if (QueryCentral.checkQuery(getEntity(),QueryCentral.query_TargetToTheLeft)>0){
				addControl(Steering.STEER_TURN_RIGHT);
			} else {
				addControl(Steering.STEER_TURN_LEFT);
			}
		}
	}

}
