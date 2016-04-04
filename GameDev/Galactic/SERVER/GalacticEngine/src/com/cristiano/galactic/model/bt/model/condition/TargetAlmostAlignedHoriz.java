// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/15/2011 17:09:51
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.condition;

/** ModelCondition class created from MMPM condition TargetAlmostAlignedHoriz. */
public class TargetAlmostAlignedHoriz extends
		jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of TargetAlmostAlignedHoriz. */
	public TargetAlmostAlignedHoriz(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a galactic.model.bt.execution.condition.TargetAlmostAlignedHoriz
	 * task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.condition.TargetAlmostAlignedHoriz(
				this, executor, parent);
	}
}