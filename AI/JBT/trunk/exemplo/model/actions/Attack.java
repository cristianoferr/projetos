// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/09/2011 15:52:08
// ******************************************************* 
package mypackage.model;

/** ModelAction class created from MMPM action Attack. */
public class Attack extends jbt.model.task.leaf.action.ModelAction {
	/**
	 * Value of the parameter "target" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.String target;
	/**
	 * Location, in the context, of the parameter "target" in case its value is
	 * not specified at construction time. null otherwise.
	 */
	private java.lang.String targetLoc;

	/**
	 * Constructor. Constructs an instance of Attack.
	 * 
	 * @param target
	 *            value of the parameter "target", or null in case it should be
	 *            read from the context. If null, <code>targetLoc</code> cannot
	 *            be null.
	 * @param targetLoc
	 *            in case <code>target</code> is null, this variable represents
	 *            the place in the context where the parameter's value will be
	 *            retrieved from.
	 */
	public Attack(jbt.model.core.ModelTask guard, java.lang.String target,
			java.lang.String targetLoc) {
		super(guard);
		this.target = target;
		this.targetLoc = targetLoc;
	}

	/**
	 * Returns a mypackage.execution.action.Attack task that is able to run this
	 * task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new mypackage.execution.action.Attack(this, executor, parent,
				this.target, this.targetLoc);
	}
}