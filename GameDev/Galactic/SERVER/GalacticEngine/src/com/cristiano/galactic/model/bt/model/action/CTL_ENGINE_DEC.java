// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/13/2011 17:17:36
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.action;

/** ModelAction class created from MMPM action CTL_ENGINE_DEC. */
public class CTL_ENGINE_DEC extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of CTL_ENGINE_DEC. */
	public CTL_ENGINE_DEC(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a galactic.model.bt.execution.action.CTL_ENGINE_DEC task that is
	 * able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.action.CTL_ENGINE_DEC(this,
				executor, parent);
	}
}