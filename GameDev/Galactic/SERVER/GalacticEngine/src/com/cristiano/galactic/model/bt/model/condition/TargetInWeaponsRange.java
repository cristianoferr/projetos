// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 05/11/2012 16:44:50
// ******************************************************* 
package com.cristiano.galactic.model.bt.model.condition;

/** ModelCondition class created from MMPM condition TargetInWeaponsRange. */
public class TargetInWeaponsRange extends
		jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of TargetInWeaponsRange. */
	public TargetInWeaponsRange(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a com.cristiano.galactic.model.bt.execution.condition.
	 * TargetInWeaponsRange task that is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new com.cristiano.galactic.model.bt.execution.condition.TargetInWeaponsRange(
				this, executor, parent);
	}
}