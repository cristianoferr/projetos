// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/15/2011 15:42:36
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.condition;

/** ModelCondition class created from MMPM condition SelfTooMuchRotation. */
public class SelfTooMuchRotation extends
		jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of SelfTooMuchRotation. */
	public SelfTooMuchRotation(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a galactic.model.bt.execution.condition.SelfTooMuchRotation task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.condition.SelfTooMuchRotation(
				this, executor, parent);
	}
}