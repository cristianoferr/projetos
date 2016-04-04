package com.cristiano.java.gm.builder.map.bsp;

import java.awt.Color;
import java.awt.Point;

import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.interfaces.map.ISolveMap;
import com.cristiano.java.gm.utils.ComponentRecipes;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.CRJavaUtils;

public class LeafBSP {

	public static  int MIN_LEAF_SIZE = 6;
	public static  float MIN_ROOM_SIZE = 6;
	public static  float MAX_ROOM_SIZE = 6;
	public int x, y;
	public int width;
	public int height;
	public LeafBSP leftChild;
	public LeafBSP rightChild;
	RoomComponent room;
	private ISolveMap solver;
	private EntityManager entMan;

	public LeafBSP(int x, int y, int width, int height, ISolveMap solver,EntityManager entMan) {
		this.x = x;
		this.y = y;
		this.entMan=entMan;
		this.width = width;
		this.height = height;
		this.solver = solver;
	}

	public boolean split() {
		if ((leftChild != null) || (rightChild != null)) {
			return false;
		}

		boolean splitH = CRJavaUtils.random() > 0.5f;
		if ((width > height) && (height / width >= 0.05f)) {
			splitH = false;
		} else if ((height > width) && (width / height >= 0.05f)) {
			splitH = true;
		}

		int max = (splitH ? height : width) - MIN_LEAF_SIZE;// determine the
															// maximum height or
															// width
		if (max <= MIN_LEAF_SIZE) {
			return false;// the area is too small to split any more...
		}

		int split = (int) CRJavaUtils.random(MIN_LEAF_SIZE, max); // determine
																	// where
																	// we're
																	// going to
																	// split

		// create our left and right children based on the direction of the
		// split
		if (splitH) {
			leftChild = new LeafBSP(x, y, width, split, solver,entMan);
			rightChild = new LeafBSP(x, y + split, width, height - split,
					solver,entMan);
		} else {
			leftChild = new LeafBSP(x, y, split, height, solver,entMan);
			rightChild = new LeafBSP(x + split, y, width - split, height,
					solver,entMan);
		}
		return true; // split successful!

	}

	public void createRooms() {
		// this function generates all the rooms and hallways for this Leaf and
		// all of its children.
		if ((leftChild != null) || (rightChild != null)) {
			// this leaf has been split, so go into the children leafs
			if (leftChild != null) {
				leftChild.createRooms();
			}
			if (rightChild != null) {
				rightChild.createRooms();
			}

			// if there are both left and right children in this Leaf, create a
			// hallway between them
			if ((leftChild != null) && (rightChild != null)) {
				createHall(leftChild.getRoom(), rightChild.getRoom());
			}

		} else {
			// this Leaf is the ready to make a room
			Point roomSize;
			Point roomPos;
			// the room can be between 3 x 3 tiles to the size of the leaf - 2.
			roomSize = new Point((int) CRJavaUtils.random(MIN_ROOM_SIZE,
					MAX_ROOM_SIZE - 2), (int) CRJavaUtils.random(MIN_ROOM_SIZE,
					MAX_ROOM_SIZE - 2));
			// place the room within the Leaf, but don't put it right
			// against the side of the Leaf (that would merge rooms together)
			roomPos = new Point((int) CRJavaUtils.random(1, width - roomSize.x
					- 1), (int) CRJavaUtils.random(1, height - roomSize.y - 1));
			room = solver.createNewRoom();
			room.setPosition(x + roomPos.x+roomSize.x/2, 0,y + roomPos.y+roomSize.y/2);
			room.setDimension(roomSize.x,0, roomSize.y);
		}
	}

	public RoomComponent getRoom() {
		// iterate all the way through these leafs to find a room, if one
		// exists.
		if (room != null){
			return room;
		}else {
			RoomComponent lRoom = null;
			RoomComponent rRoom = null;
			if (leftChild != null) {
				lRoom = leftChild.getRoom();
			}
			if (rightChild != null) {
				rRoom = rightChild.getRoom();
			}
			if ((lRoom == null) && (rRoom == null)){
				return null;}
			else if (rRoom == null){
				return lRoom;}
			else if (lRoom == null){
				return rRoom;}
			else if (CRJavaUtils.random() > .5){
				return lRoom;}
			else{
				return rRoom;}
		}
	}

	public void createHall(RoomComponent l, RoomComponent r) {
		ComponentRecipes.linkRooms(entMan,l, r);
	}

	// ////

	public void draw(CRDebugDraw draw) {
		if (!CRJavaUtils.IS_DEBUG){
			return;
		}
		draw.setColor(Color.black);
		draw.drawRect(x, y, width, height);

		if (room != null) {
			draw.setColor(Color.red);
			//g.fillRect(room.x, room.y, room.width, room.height);
			//g.setColor(Color.yellow);
			draw.fillRect((int)(room.getX()-room.getWidth()/2), (int)(room.getZ()-room.getDepth()/2), (int)(room.getWidth()), (int)(room.getDepth()));
			draw.setColor(Color.black);
			draw.drawString(room.getId()+"", (int)room.getX(),(int)room.getZ());
		}

	}
}
