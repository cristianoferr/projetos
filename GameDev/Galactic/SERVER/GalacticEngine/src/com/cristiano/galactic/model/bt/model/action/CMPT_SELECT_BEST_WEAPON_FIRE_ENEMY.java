// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/21/2011 11:41:49
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.action;

/**
 * ModelAction class created from MMPM action
 * CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY.
 */
public class CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY extends
		jbt.model.task.leaf.action.ModelAction {
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
	 * Constructor. Constructs an instance of
	 * CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY.
	 * 
	 * @param target
	 *            value of the parameter "target", or null in case it should be
	 *            read from the context. If null, <code>targetLoc</code> cannot
	 *            be null.
	 * @param targetLoc
	 *            in case <code>target</code> is null, this variable represents
	 *            the place in the context where the parameter's value will be
	 *            retrieved from.
	 */
	public CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY(jbt.model.core.ModelTask guard,
			java.lang.String target, java.lang.String targetLoc) {
		super(guard);
		this.target = target;
		this.targetLoc = targetLoc;
	}

	/**
	 * Returns a
	 * galactic.model.bt.execution.action.CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.action.CMPT_SELECT_BEST_WEAPON_FIRE_ENEMY(
				this, executor, parent, this.target, this.targetLoc);
	}
}