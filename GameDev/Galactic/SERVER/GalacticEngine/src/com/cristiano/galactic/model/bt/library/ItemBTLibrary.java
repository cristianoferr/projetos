// ******************************************************* 
//                   MACHINE GENERATED CODE                
//                       DO NOT MODIFY                     
//                                                         
// Generated on 05/11/2012 16:56:37
// ******************************************************* 
package com.cristiano.galactic.model.bt.library;

/**
 * BT library that includes the trees read from the following files:
 * <ul>
 * <li>../../../Galactic/db/behaviorTree\CMD_ORIENT_TO_guard.xbt</li>
 * <li>../../../Galactic/db/behaviorTree\CMD_MOVE_TO_TARGET.xbt</li>
 * <li>../../../Galactic/db/behaviorTree\CMD_ATTACK_TARGET.xbt</li>
 * </ul>
 */
public class ItemBTLibrary implements jbt.execution.core.IBTLibrary {
	/**
	 * Tree generated from file
	 * ../../../Galactic/db/behaviorTree\CMD_ORIENT_TO_guard.xbt.
	 */
	private static jbt.model.core.ModelTask CMD_ORIENT_TO;
	/**
	 * Tree generated from file
	 * ../../../Galactic/db/behaviorTree\CMD_MOVE_TO_TARGET.xbt.
	 */
	private static jbt.model.core.ModelTask CMD_MOVE_TO_TARGET;
	/**
	 * Tree generated from file
	 * ../../../Galactic/db/behaviorTree\CMD_ATTACK_TARGET.xbt.
	 */
	private static jbt.model.core.ModelTask CMD_ATTACK_TARGET;

	/* Static initialization of all the trees. */
	static {
		CMD_ORIENT_TO = new jbt.model.task.decorator.ModelUntilFail(
				null,
				new jbt.model.task.composite.ModelParallel(
						null,
						jbt.model.task.composite.ModelParallel.ParallelPolicy.SELECTOR_POLICY,
						new jbt.model.task.composite.ModelStaticPriorityList(
								null,
								new com.cristiano.galactic.model.bt.model.action.CTL_TURN_UP(
										new jbt.model.task.composite.ModelSelector(
												null,
												new jbt.model.task.composite.ModelSequence(
														null,
														new com.cristiano.galactic.model.bt.model.condition.SelfRotatingDown(
																null),
														new com.cristiano.galactic.model.bt.model.condition.TargetAlmostAlignedVert(
																null),
														new jbt.model.task.decorator.ModelInverter(
																null,
																new com.cristiano.galactic.model.bt.model.condition.SelfROCVertIsLow(
																		null))),
												new jbt.model.task.composite.ModelSequence(
														null,
														new com.cristiano.galactic.model.bt.model.condition.TargetIsAbove(
																null),
														new jbt.model.task.decorator.ModelInverter(
																null,
																new com.cristiano.galactic.model.bt.model.condition.TargetAlmostAlignedVert(
																		null)))),
										(float) 1, null),
								new com.cristiano.galactic.model.bt.model.action.CTL_TURN_DOWN(
										new jbt.model.task.composite.ModelSelector(
												null,
												new jbt.model.task.composite.ModelSequence(
														null,
														new com.cristiano.galactic.model.bt.model.condition.SelfRotatingUp(
																null),
														new com.cristiano.galactic.model.bt.model.condition.TargetAlmostAlignedVert(
																null),
														new jbt.model.task.decorator.ModelInverter(
																null,
																new com.cristiano.galactic.model.bt.model.condition.SelfROCVertIsLow(
																		null))),
												new jbt.model.task.composite.ModelSequence(
														null,
														new com.cristiano.galactic.model.bt.model.condition.TargetIsBelow(
																null),
														new jbt.model.task.decorator.ModelInverter(
																null,
																new com.cristiano.galactic.model.bt.model.condition.TargetAlmostAlignedVert(
																		null)))),
										(float) 1, null)),
						new jbt.model.task.composite.ModelStaticPriorityList(
								null,
								new com.cristiano.galactic.model.bt.model.action.CTL_TURN_RIGHT(
										new jbt.model.task.composite.ModelSelector(
												null,
												new jbt.model.task.composite.ModelSequence(
														null,
														new com.cristiano.galactic.model.bt.model.condition.TargetAlmostAlignedHoriz(
																null),
														new com.cristiano.galactic.model.bt.model.condition.SelfRotatingLeft(
																null),
														new jbt.model.task.decorator.ModelInverter(
																null,
																new com.cristiano.galactic.model.bt.model.condition.SelfROCHorizIsLow(
																		null))),
												new jbt.model.task.composite.ModelSequence(
														null,
														new jbt.model.task.decorator.ModelInverter(
																null,
																new com.cristiano.galactic.model.bt.model.condition.TargetAlmostAlignedHoriz(
																		null)),
														new com.cristiano.galactic.model.bt.model.condition.TargetToTheRight(
																null))),
										(float) 1, null),
								new com.cristiano.galactic.model.bt.model.action.CTL_TURN_LEFT(
										new jbt.model.task.composite.ModelSelector(
												null,
												new jbt.model.task.composite.ModelSequence(
														null,
														new com.cristiano.galactic.model.bt.model.condition.TargetAlmostAlignedHoriz(
																null),
														new com.cristiano.galactic.model.bt.model.condition.SelfRotatingRight(
																null),
														new jbt.model.task.decorator.ModelInverter(
																null,
																new com.cristiano.galactic.model.bt.model.condition.SelfROCHorizIsLow(
																		null))),
												new jbt.model.task.composite.ModelSequence(
														null,
														new jbt.model.task.decorator.ModelInverter(
																null,
																new com.cristiano.galactic.model.bt.model.condition.TargetAlmostAlignedHoriz(
																		null)),
														new com.cristiano.galactic.model.bt.model.condition.TargetToTheLeft(
																null))),
										(float) 1, null))));

		CMD_MOVE_TO_TARGET = new jbt.model.task.decorator.ModelUntilFail(null,
				new jbt.model.task.leaf.ModelSubtreeLookup(null,
						"CMD_ORIENT_TO"));

		CMD_ATTACK_TARGET = new jbt.model.task.decorator.ModelUntilFail(
				null,
				new jbt.model.task.composite.ModelParallel(
						null,
						jbt.model.task.composite.ModelParallel.ParallelPolicy.SELECTOR_POLICY,
						new jbt.model.task.leaf.ModelSubtreeLookup(null,
								"CMD_MOVE_TO_TARGET"),
						new com.cristiano.galactic.model.bt.model.action.CTL_SHOOT(
								new jbt.model.task.composite.ModelSelector(
										null,
										new jbt.model.task.composite.ModelSequence(
												null,
												new com.cristiano.galactic.model.bt.model.condition.TargetInShortRange(
														null),
												new com.cristiano.galactic.model.bt.model.condition.TargetIsAligned(
														null))), null, "TARGET")));

	}

	/**
	 * Returns a behaviour tree by its name, or null in case it cannot be found.
	 * It must be noted that the trees that are retrieved belong to the class,
	 * not to the instance (that is, the trees are static members of the class),
	 * so they are shared among all the instances of this class.
	 */
	public jbt.model.core.ModelTask getBT(java.lang.String name) {
		if (name.equals("CMD_ORIENT_TO")) {
			return CMD_ORIENT_TO;
		}
		if (name.equals("CMD_MOVE_TO_TARGET")) {
			return CMD_MOVE_TO_TARGET;
		}
		if (name.equals("CMD_ATTACK_TARGET")) {
			return CMD_ATTACK_TARGET;
		}
		return null;
	}

	/**
	 * Returns an Iterator that is able to iterate through all the elements in
	 * the library. It must be noted that the iterator does not support the
	 * "remove()" operation. It must be noted that the trees that are retrieved
	 * belong to the class, not to the instance (that is, the trees are static
	 * members of the class), so they are shared among all the instances of this
	 * class.
	 */
	public java.util.Iterator<jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask>> iterator() {
		return new BTLibraryIterator();
	}

	private class BTLibraryIterator
			implements
			java.util.Iterator<jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask>> {
		static final long numTrees = 3;
		long currentTree = 0;

		public boolean hasNext() {
			return this.currentTree < numTrees;
		}

		public jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask> next() {
			this.currentTree++;

			if ((this.currentTree - 1) == 0) {
				return new jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask>(
						"CMD_ORIENT_TO", CMD_ORIENT_TO);
			}

			if ((this.currentTree - 1) == 1) {
				return new jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask>(
						"CMD_MOVE_TO_TARGET", CMD_MOVE_TO_TARGET);
			}

			if ((this.currentTree - 1) == 2) {
				return new jbt.util.Pair<java.lang.String, jbt.model.core.ModelTask>(
						"CMD_ATTACK_TARGET", CMD_ATTACK_TARGET);
			}

			throw new java.util.NoSuchElementException();
		}

		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}
}
