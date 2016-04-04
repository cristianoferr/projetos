// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/05/2011 13:30:41
// ******************************************************* 
package com.cristiano.galactic.model.bt.execution.action;

/** ExecutionAction class created from MMPM action CMPT_FIND_NEAREST_ENEMY. */
public class CMPT_FIND_NEAREST_ENEMY extends
		jbt.execution.task.leaf.action.ExecutionAction {
	/**
	 * Value of the parameter "maxDistance" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.Float maxDistance;
	/**
	 * Location, in the context, of the parameter "maxDistance" in case its
	 * value is not specified at construction time. null otherwise.
	 */
	private java.lang.String maxDistanceLoc;

	/**
	 * Constructor. Constructs an instance of CMPT_FIND_NEAREST_ENEMY that is
	 * able to run a galactic.model.bt.model.action.CMPT_FIND_NEAREST_ENEMY.
	 * 
	 * @param maxDistance
	 *            value of the parameter "maxDistance", or null in case it
	 *            should be read from the context. If null,
	 *            <code>maxDistanceLoc<code> cannot be null.
	 * @param maxDistanceLoc
	 *            in case <code>maxDistance</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public CMPT_FIND_NEAREST_ENEMY(
			com.cristiano.galactic.model.bt.model.action.CMPT_FIND_NEAREST_ENEMY modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent,
			java.lang.Float maxDistance, java.lang.String maxDistanceLoc) {
		super(modelTask, executor, parent);

		this.maxDistance = maxDistance;
		this.maxDistanceLoc = maxDistanceLoc;
	}

	/**
	 * Returns the value of the parameter "maxDistance", or null in case it has
	 * not been specified or it cannot be found in the context.
	 */
	public java.lang.Float getMaxDistance() {
		if (this.maxDistance != null) {
			return this.maxDistance;
		} else {
			return (java.lang.Float) this.getContext().getVariable(
					this.maxDistanceLoc);
		}
	}

	protected void internalSpawn() {
		/*
		 * Do not remove this first line unless you know what it does and you
		 * need not do it.
		 */
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		System.out.println(this.getClass().getCanonicalName() + " spawned");
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		/*
		 * should only return Status.SUCCESS, Status.FAILURE or Status.RUNNING.
		 * No other values are allowed.
		 */
		return jbt.execution.core.ExecutionTask.Status.SUCCESS;
	}

	protected void internalTerminate() {
	}

	protected void restoreState(jbt.execution.core.ITaskState state) {
	}

	protected jbt.execution.core.ITaskState storeState() {
		return null;
	}

	protected jbt.execution.core.ITaskState storeTerminationState() {
		return null;
	}
}