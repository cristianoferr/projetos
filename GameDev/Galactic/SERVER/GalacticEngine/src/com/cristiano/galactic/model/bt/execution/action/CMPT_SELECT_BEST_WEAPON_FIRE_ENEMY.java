// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 10/21/2011 11:41:49
// ******************************************************* 
package com.cristiano.galactic.model.bt.execution.action;

/**
 * ExecutionAction class created from MMPM action
 * CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY.
 */
public class CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY extends
		jbt.execution.task.leaf.action.ExecutionAction {
	/**
	 * Value of the parameter "target" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.String target;
	/**
	 * Location, in the context, of the parameter "target" in case its value is
	 * not specified at construction time. null otherwise.
	 */
	private java.lang.String targetLoc;

	/**
	 * Constructor. Constructs an instance of CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY
	 * that is able to run a
	 * galactic.model.bt.model.action.CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY.
	 * 
	 * @param target
	 *            value of the parameter "target", or null in case it should be
	 *            read from the context. If null,
	 *            <code>targetLoc<code> cannot be null.
	 * @param targetLoc
	 *            in case <code>target</code> is null, this variable represents
	 *            the place in the context where the parameter's value will be
	 *            retrieved from.
	 */
	public CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY(
			com.cristiano.galactic.model.bt.model.action.CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent, java.lang.String target,
			java.lang.String targetLoc) {
		super(modelTask, executor, parent);

		this.target = target;
		this.targetLoc = targetLoc;
	}

	/**
	 * Returns the value of the parameter "target", or null in case it has not
	 * been specified or it cannot be found in the context.
	 */
	public java.lang.String getTarget() {
		if (this.target != null) {
			return this.target;
		} else {
			return (java.lang.String) this.getContext().getVariable(
					this.targetLoc);
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