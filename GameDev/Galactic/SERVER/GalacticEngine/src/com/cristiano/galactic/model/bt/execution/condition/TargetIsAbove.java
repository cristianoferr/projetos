// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 06/13/2011 17:17:36
// ******************************************************* 
package com.cristiano.galactic.model.bt.execution.condition;

import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.bt.queries.QueryCentral;

/** ExecutionCondition class created from MMPM condition TargetIsAbove. */
public class TargetIsAbove extends
		jbt.execution.task.leaf.condition.ExecutionCondition {

	/**
	 * Constructor. Constructs an instance of TargetIsAbove that is able to run
	 * a galactic.model.bt.model.condition.TargetIsAbove.
	 */
	public TargetIsAbove(
			com.cristiano.galactic.model.bt.model.condition.TargetIsAbove modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		super(modelTask, executor, parent);

	}

	protected void internalSpawn() {
		/*
		 * Do not remove this first line unless you know what it does and you
		 * need not do it.
		 */
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		/* todo: this method's implementation must be completed. */
	//	System.out.println(this.getClass().getCanonicalName() + " spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		/*
		 * todo: this method's implementation must be completed. This function
		 * should only return Status.SUCCESS, Status.FAILURE or Status.RUNNING.
		 * No other values are allowed.
		 */
		//return jbt.execution.core.ExecutionTask.Status.SUCCESS;
		ArtificialEntity currentEntityID = (ArtificialEntity) this.getContext().
		getVariable("CurrentEntityID");
		double res=QueryCentral.checkQuery(currentEntityID, QueryCentral.QUERY_TARGETISABOVE);
		this.getContext().setVariable("TargetIsAbove", res);
		if (res>0){
			this.getContext().setVariable("TargetAlignVert", (float)res);
			//System.out.println("TargetAbove");
			return jbt.execution.core.ExecutionTask.Status.SUCCESS;}
		else{
			return jbt.execution.core.ExecutionTask.Status.FAILURE;
			
		}
	}

	protected void internalTerminate() {
		/* todo: this method's implementation must be completed. */
	}

	protected void restoreState(jbt.execution.core.ITaskState state) {
		/* todo: this method's implementation must be completed. */
	}

	protected jbt.execution.core.ITaskState storeState() {
		/* todo: this method's implementation must be completed. */
		return null;
	}

	protected jbt.execution.core.ITaskState storeTerminationState() {
		/* todo: this method's implementation must be completed. */
		return null;
	}
}