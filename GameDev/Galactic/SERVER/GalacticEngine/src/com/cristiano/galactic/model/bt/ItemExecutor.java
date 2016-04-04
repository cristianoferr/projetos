package com.cristiano.galactic.model.bt;

import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;

public class ItemExecutor   {
	private ArtificialEntity item;
	private ModelTask currBT=null ;
	private IBTExecutor btExecutor =null;
	
	String lastBT=null;
	public ItemExecutor(ArtificialEntity item){
		this.item=item;	
		
	}
	
	/*public double executeAction(String action,double intensity) {
		item.activateControl(action,intensity);
		return 0;
	}*/

	/*public Vector3 getCMDTargetAsVector3(int index){
		if (currCMD==null) return null;
		if (currCMD.getParams().size()>=index) return null;
		Param par1=currCMD.getParams().get(index);
		return par1.getAsVector3();
	}*/
	
	
	public ModelTask getCurrBT() {
		return currBT;
	}

	public void setCurrBT(String cBT) {
		if (cBT==null) return;
		this.lastBT=cBT;
		currBT=item.getDataManager().getBtLibrary().getBT(cBT);
		/* Then we create the BT Executor to run the tree. */
		btExecutor = BTExecutorFactory.createBTExecutor(currBT, item.getMemory().getContext());				

	}
	
	public void turn(){
			/*if ((currBT==null) && (currCMD!=null)){
				currBT=item.getDataManager().getBtLibrary().getBT(currCMD.getName());
				// Then we create the BT Executor to run the tree. 
				btExecutor = BTExecutorFactory.createBTExecutor(currBT, item.getMemory().getContext());				
			}*/
		if (btExecutor!=null){
			btExecutor.tick();
			if (btExecutor.getStatus() != Status.RUNNING){
				currBT=null;
				btExecutor=null;
				setCurrBT(lastBT);
			}
		}
	}
		

	public ArtificialEntity getItem() {
		return item;
	}

	


}
