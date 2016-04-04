// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/15/2011 15:17:14
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.action;

/** ModelAction class created from MMPM action CTL_MOVE_RIGHT. */
public class CTL_MOVE_RIGHT extends jbt.model.task.leaf.action.ModelAction {
	/**
	 * Value of the parameter "intensity" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.Float intensity;
	/**
	 * Location, in the context, of the parameter "intensity" in case its value
	 * is not specified at construction time. null otherwise.
	 */
	private java.lang.String intensityLoc;

	/**
	 * Constructor. Constructs an instance of CTL_MOVE_RIGHT.
	 * 
	 * @param intensity
	 *            value of the parameter "intensity", or null in case it should
	 *            be read from the context. If null, <code>intensityLoc</code>
	 *            cannot be null.
	 * @param intensityLoc
	 *            in case <code>intensity</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public CTL_MOVE_RIGHT(jbt.model.core.ModelTask guard,
			java.lang.Float intensity, java.lang.String intensityLoc) {
		super(guard);
		this.intensity = intensity;
		this.intensityLoc = intensityLoc;
	}

	/**
	 * Returns a galactic.model.bt.execution.action.CTL_MOVE_RIGHT task that is
	 * able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.action.CTL_MOVE_RIGHT(this,
				executor, parent, this.intensity, this.intensityLoc);
	}
}