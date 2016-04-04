// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/21/2011 11:41:48
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.action;

/** ModelAction class created from MMPM action CTL_SHOOT. */
public class CTL_SHOOT extends jbt.model.task.leaf.action.ModelAction {
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
	 * Constructor. Constructs an instance of CTL_SHOOT.
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
	public CTL_SHOOT(jbt.model.core.ModelTask guard, java.lang.String target,
			java.lang.String targetLoc) {
		super(guard);
		this.target = target;
		this.targetLoc = targetLoc;
	}

	/**
	 * Returns a galactic.model.bt.execution.action.CTL_SHOOT task that is able
	 * to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.action.CTL_SHOOT(this, executor,
				parent, this.target, this.targetLoc);
	}
}