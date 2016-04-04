// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/05/2011 13:30:41
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.action;

/** ModelAction class created from MMPM action CMPT_FIND_NEAREST_ENEMY. */
public class CMPT_FIND_NEAREST_ENEMY extends
		jbt.model.task.leaf.action.ModelAction {
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
	 * Constructor. Constructs an instance of CMPT_FIND_NEAREST_ENEMY.
	 * 
	 * @param maxDistance
	 *            value of the parameter "maxDistance", or null in case it
	 *            should be read from the context. If null,
	 *            <code>maxDistanceLoc</code> cannot be null.
	 * @param maxDistanceLoc
	 *            in case <code>maxDistance</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public CMPT_FIND_NEAREST_ENEMY(jbt.model.core.ModelTask guard,
			java.lang.Float maxDistance, java.lang.String maxDistanceLoc) {
		super(guard);
		this.maxDistance = maxDistance;
		this.maxDistanceLoc = maxDistanceLoc;
	}

	/**
	 * Returns a galactic.model.bt.execution.action.CMPT_FIND_NEAREST_ENEMY task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.action.CMPT_FIND_NEAREST_ENEMY(
				this, executor, parent, this.maxDistance, this.maxDistanceLoc);
	}
}