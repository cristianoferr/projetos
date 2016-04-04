// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/05/2011 13:30:40
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.action;

/** ModelAction class created from MMPM action CTL_ENGINE_30P. */
public class CTL_ENGINE_30P extends jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of CTL_ENGINE_30P. */
	public CTL_ENGINE_30P(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a galactic.model.bt.execution.action.CTL_ENGINE_30P task that is
	 * able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.action.CTL_ENGINE_30P(this,
				executor, parent);
	}
}