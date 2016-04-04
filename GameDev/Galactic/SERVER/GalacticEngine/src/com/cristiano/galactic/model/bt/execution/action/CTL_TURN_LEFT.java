// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                MUST BE CAREFULLY COMPLETED              
//                                                         
//           ABSTRACT METHODS MUST BE IMPLEMENTED          
//                                                         
// Generated on 06/15/2011 15:13:22
// ******************************************************* 
package com.cristiano.galactic.model.bt.execution.action;

import com.cristiano.galactic.controller.ItemsController;
import com.cristiano.galactic.model.Entity.Abstract.ArtificialEntity;
import com.cristiano.galactic.model.enums.ControlEnum;

/** ExecutionAction class created from MMPM action CTL_TURN_LEFT. */
public class CTL_TURN_LEFT extends
		jbt.execution.task.leaf.action.ExecutionAction {
	/**
	 * Value of the parameter "intensity" in case its value is specified at
	 * construction time. null otherwise.
	 */
	private java.lang.Float intensity;
	/**
	 * Location, in the context, of the parameter "intensity" in case its value
	 * is not specified at construction time. null otherwise.
	 */
	private java.lang.String intensityLoc;

	/**
	 * Constructor. Constructs an instance of CTL_TURN_LEFT that is able to run
	 * a galactic.model.bt.model.action.CTL_TURN_LEFT.
	 * 
	 * @param intensity
	 *            value of the parameter "intensity", or null in case it should
	 *            be read from the context. If null,
	 *            <code>intensityLoc<code> cannot be null.
	 * @param intensityLoc
	 *            in case <code>intensity</code> is null, this variable
	 *            represents the place in the context where the parameter's
	 *            value will be retrieved from.
	 */
	public CTL_TURN_LEFT(
			com.cristiano.galactic.model.bt.model.action.CTL_TURN_LEFT modelTask,
			jbt.execution.core.BTExecutor executor,
			jbt.execution.core.ExecutionTask parent, java.lang.Float intensity,
			java.lang.String intensityLoc) {
		super(modelTask, executor, parent);

		this.intensity = intensity;
		this.intensityLoc = intensityLoc;
	}

	/**
	 * Returns the value of the parameter "intensity", or null in case it has
	 * not been specified or it cannot be found in the context.
	 */
	public java.lang.Float getIntensity() {
		if (this.intensity != null) {
			return this.intensity;
		} else {
			return (java.lang.Float) this.getContext().getVariable(
					this.intensityLoc);
		}
	}

	protected void internalSpawn() {
		/*
		 * Do not remove this first line unless you know what it does and you
		 * need not do it.
		 */
		this.getExecutor().requestInsertionIntoList(
				jbt.execution.core.BTExecutor.BTExecutorList.TICKABLE, this);
		/*
		* Retrieve the identifier of the entity (Terran Marine) running
		* this action from the context. Here we are supposing that the
		* context will contain it.
		*/
		ArtificialEntity currentEntityID = (ArtificialEntity) this.getContext().
		getVariable("CurrentEntityID");
		/*
		* Now we assume that there is a generic "Game Engine" that can
		* be used to send generic orders to units of the game. Note that, in
		* order to retrieve the target position, we use the automatically
		* generated getter method.
		*/
		ItemsController.getItemsController().activateControl(currentEntityID, ControlEnum.CTL_TURN_LEFT,getIntensity(),0.5);
	}

	protected jbt.execution.core.ExecutionTask.Status internalTick() {
		/*
		 * should only return Status.SUCCESS, Status.FAILURE or Status.RUNNING.
		 * No other values are allowed.
		 */
		return jbt.execution.core.ExecutionTask.Status.SUCCESS;
	}

	protected void internalTerminate() {
	}

	protected void restoreState(jbt.execution.core.ITaskState state) {
	}

	protected jbt.execution.core.ITaskState storeState() {
		return null;
	}

	protected jbt.execution.core.ITaskState storeTerminationState() {
		return null;
	}
}