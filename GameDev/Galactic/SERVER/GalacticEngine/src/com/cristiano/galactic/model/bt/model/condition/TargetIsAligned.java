// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 10/20/2011 16:50:09
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.condition;

/** ModelCondition class created from MMPM condition TargetIsAligned. */
public class TargetIsAligned extends
		jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of TargetIsAligned. */
	public TargetIsAligned(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a galactic.model.bt.execution.condition.TargetIsAligned task that
	 * is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.condition.TargetIsAligned(this,
				executor, parent);
	}
}