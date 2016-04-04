// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/09/2011 15:52:08
// ******************************************************* 
package mypackage.model;

/** ModelAction class created from MMPM action ComputeCharacterPosition. */
public class ComputeCharacterPosition extends
		jbt.model.task.leaf.action.ModelAction {

	/** Constructor. Constructs an instance of ComputeCharacterPosition. */
	public ComputeCharacterPosition(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a mypackage.execution.action.ComputeCharacterPosition task that
	 * is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new mypackage.execution.action.ComputeCharacterPosition(this,
				executor, parent);
	}
}