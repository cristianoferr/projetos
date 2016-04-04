// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/05/2011 13:30:41
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.action;

/** ModelAction class created from MMPM action CTL_ENGINE_80P. */
public class CTL_ENGINE_80P extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of CTL_ENGINE_80P. */
	public CTL_ENGINE_80P(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a galactic.model.bt.execution.action.CTL_ENGINE_80P task that is
	 * able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.action.CTL_ENGINE_80P(this,
				executor, parent);
	}
}