// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/09/2011 15:52:08
// ******************************************************* 
package mypackage.model.condition;

/** ModelCondition class created from MMPM condition LowDanger. */
public class LowDanger extends jbt.model.task.leaf.condition.ModelCondition {

	/** Constructor. Constructs an instance of LowDanger. */
	public LowDanger(jbt.model.core.ModelTask guard) {
		super(guard);
	}

	/**
	 * Returns a mypackage.execution.condition.LowDanger task that is able to
	 * run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new mypackage.execution.condition.LowDanger(this, executor,
				parent);
	}
}