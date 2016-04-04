package com.cristiano.java.gm.builder.map;

import java.util.ArrayList;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.builder.map.bsp.LeafBSP;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.GameGenreComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapLocationComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

/*
 * Baseado em http://gamedevelopment.tutsplus.com/tutorials/how-to-use-bsp-trees-to-generate-game-maps--gamedev-12268
 */
public class BSPTreeMapSolver extends AbstractSolveMap implements ISolveMap {

	enum BSPState {
		STARTING, SPLITING, ROOMS, DONE
	};

	BSPState currState = BSPState.STARTING;
	private ArrayList<LeafBSP> leafs;
	private float maxLeafSize;
	private ArrayList<LeafBSP> leafsToAdd;
	private LeafBSP root;

	@Override
	public void startFrom(MapComponent mapC,MapLocationComponent mapLocation, IGameElement elSolver,GameGenreComponent genre){
		super.startFrom(mapC,mapLocation, elSolver,genre);
		Log.info("BSP Tree Map Solver...");

		leafs = new ArrayList<LeafBSP>();
		leafsToAdd = new ArrayList<LeafBSP>();
		LeafBSP.MIN_LEAF_SIZE = (int) (mapC.minRoomSize * elSolver
				.getPropertyAsFloat(GameProperties.BSP_LEAF_SIZE_MULTI));
		LeafBSP.MIN_ROOM_SIZE = (mapC.minRoomSize)*mapC.length;
		LeafBSP.MAX_ROOM_SIZE = mapC.maxRoomSize*mapC.length;
		maxLeafSize = mapC.maxRoomSize*mapC.length
				* elSolver
						.getPropertyAsFloat(GameProperties.BSP_LEAF_SIZE_MULTI);

		root = new LeafBSP(0, 0, mapC.getLength(), mapC.getLength(), this,entMan);
		leafs.add(root);

		// we loop through every Leaf in our Vector over and over again, until
		// no more Leafs can be split.
		currState = BSPState.SPLITING;

	}

	@Override
	public boolean hasCompleted() {
		Log.debug("BSP Rooms: "+countRooms());
		
		if (currState == BSPState.SPLITING) {
			Log.debug("BSP Spliting:"+leafs.size()+" leafs.");
			boolean didSplit = split();
			if (!didSplit) {
				currState = BSPState.ROOMS;
			}
			return false;
		}
		if (currState == BSPState.ROOMS) {
			// next, iterate through each Leaf and create a room in each one.
			root.createRooms();

			finishDebugDraw();
			
			
			currState = BSPState.DONE;
			return true;

		}
		if (currState == BSPState.DONE) {
			return true;
		}
		return false;
	}

	private void finishDebugDraw() {
		for (LeafBSP l : leafs) {
			l.draw(draw);
		}
		draw.finishDebugDraw("BSPTreeMap");
		
	}

	boolean split() {
		boolean did_split = false;
		for (LeafBSP l : leafs) {
			if (l.leftChild == null && l.rightChild == null) // if this Leaf is
																// not already
																// split...
			{
				// if this Leaf is too big, or 75% chance...
				if ((l.width > maxLeafSize) || (l.height > maxLeafSize)
						|| (CRJavaUtils.random() > 0.25)) {
					if (l.split()) // split the Leaf!
					{
						// if we did split, push the child leafs to the
						// Vector so we can loop into them next
						leafsToAdd.add(l.leftChild);
						leafsToAdd.add(l.rightChild);
						did_split = true;
					}
				}
			}
		}
		leafs.addAll(leafsToAdd);
		leafsToAdd.clear();
		return did_split;
	}

}
