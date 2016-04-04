// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/13/2011 17:17:36
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.condition;

/** ModelCondition class created from MMPM condition SelfRotatingRight. */
public class SelfRotatingRight extends
		jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of SelfRotatingRight. */
	public SelfRotatingRight(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a galactic.model.bt.execution.condition.SelfRotatingRight task
	 * that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.condition.SelfRotatingRight(
				this, executor, parent);
	}
}