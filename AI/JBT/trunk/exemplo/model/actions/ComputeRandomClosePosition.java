// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 06/09/2011 15:52:08
// ******************************************************* 
package mypackage.model;

/** ModelAction class created from MMPM action ComputeRandomClosePosition. */
public class ComputeRandomClosePosition extends
		jbt.model.task.leaf.action.ModelAction {
	/**
	 * Value of the parameter "initialPosition" in case its value is specified
	 * at construction time. null otherwise.
	 */
	private float[] initialPosition;
	/**
	 * Location, in the context, of the parameter "initialPosition" in case its
	 * value is not specified at construction time. null otherwise.
	 */
	private java.lang.String initialPositionLoc;

	/**
	 * Constructor. Constructs an instance of ComputeRandomClosePosition.
	 * 
	 * @param initialPosition
	 *            value of the parameter "initialPosition", or null in case it
	 *            should be read from the context. If null,
	 *            <code>initialPositionLoc</code> cannot be null.
	 * @param initialPositionLoc
	 *            in case <code>initialPosition</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public ComputeRandomClosePosition(jbt.model.core.ModelTask guard,
			float[] initialPosition, java.lang.String initialPositionLoc) {
		super(guard);
		this.initialPosition = initialPosition;
		this.initialPositionLoc = initialPositionLoc;
	}

	/**
	 * Returns a mypackage.execution.action.ComputeRandomClosePosition task that
	 * is able to run this task.
	 */
	public jbt.execution.core.ExecutionTask createExecutor(
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent) {
		return new mypackage.execution.action.ComputeRandomClosePosition(this,
				executor, parent, this.initialPosition, this.initialPositionLoc);
	}
}