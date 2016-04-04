package jbt.model.task.decorator;

import jbt.execution.core.BTExecutor;
import jbt.execution.core.ExecutionTask;
import jbt.execution.task.decorator.ExecutionSucceeder;
import jbt.model.core.ModelTask;

/**
 * A ModelSucceeder is a decorator that makes its child succeeds no matter it it actually fails.
 * 
 * @author Ricardo Juan Palma Durán
 * 
 */
public class ModelSucceeder extends ModelDecorator {
	/**
	 * Constructor.
	 * 
	 * @param guard
	 *            the guard of the ModelSucceeder, which may be null.
	 * @param child
	 *            the child task.
	 */
	public ModelSucceeder(ModelTask guard, ModelTask child) {
		super(guard, child);
	}

	/**
	 * Returns an ExecutionSucceeder that is able to run this ModelSucceeder.
	 * 
	 * @see jbt.model.core.ModelTask#createExecutor(jbt.execution.core.BTExecutor,
	 *      ExecutionTask)
	 */
	public ExecutionTask createExecutor(BTExecutor executor, ExecutionTask parent) {
		return new ExecutionSucceeder(this, executor, parent);
	}
}
