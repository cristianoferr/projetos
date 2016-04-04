// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/05/2011 13:30:41
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.condition;

/** ModelCondition class created from MMPM condition TargetIsClose. */
public class TargetIsClose extends jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of TargetIsClose. */
	public TargetIsClose(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a galactic.model.bt.execution.condition.TargetIsClose task that
	 * is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.condition.TargetIsClose(this,
				executor, parent);
	}
}