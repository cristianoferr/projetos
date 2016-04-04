package com.cristiano.java.gm.ecs.comps.map;

import java.awt.Color;

import org.json.simple.JSONObject;

import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.CRJavaUtils;
import com.jme3.math.Vector3f;

public class RoomComponent extends GameComponent {

	private static final String POSITION = "position";
	private static final String FLOOR_HEIGHT = "floorHeight";
	private static final String DIMENSIONS = "dimensions";
	private static final String BUBBLE_FILTER = "bubbleFilter";
	private static final String ENDING_ROOM = "endingRoom";
	private static final String STARTING_ROOM = "startingRoom";
	private static final String ROOM_ID = "roomId";
	
	
	public boolean startingRoom = false;
	public boolean endingRoom = false;
	public int roomId;
	
	public int parentRoom = -1;
	final Vector3f dimensions = new Vector3f(0, 0, 0);// TODO: avancado: substituir
												// por componentes de posicao e
												// dimensao
	final Vector3f position = new Vector3f(0, 0, 0);
	public float floorHeight;
	public String bubbleFilter = null;// filter the bubble which will become the
										// room, used in roomSolverSystem
	public Vector3f edgePt0;
	public Vector3f edgePt1;

	public RoomComponent() {
		super(GameComps.COMP_ROOM);
	}

	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
	}

	@Override
	public IGameComponent clonaComponent() {
		RoomComponent room=new RoomComponent();
		room.bubbleFilter=bubbleFilter;
		room.dimensions.set(dimensions);
		room.position.set(position);
		room.endingRoom=endingRoom;
		room.floorHeight=floorHeight;
		room.parentRoom=parentRoom;
		return room;
	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(STARTING_ROOM,startingRoom);
		obj.put(ENDING_ROOM,endingRoom);
		obj.put(BUBBLE_FILTER,bubbleFilter);
		obj.put(ENDING_ROOM,endingRoom);
		obj.put(FLOOR_HEIGHT,floorHeight);
		obj.put(ROOM_ID,roomId);
		CRJsonUtils.exportVector3f(obj, POSITION, position);
		CRJsonUtils.exportVector3f(obj, DIMENSIONS, dimensions);
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		startingRoom=(boolean) obj.get(STARTING_ROOM);
		endingRoom=(boolean) obj.get(ENDING_ROOM);
		bubbleFilter=(String) obj.get(BUBBLE_FILTER);
		endingRoom=(boolean) obj.get(ENDING_ROOM);
		floorHeight=CRJsonUtils.getFloat(obj,FLOOR_HEIGHT);
		position.set(CRJsonUtils.importVector3f(obj, POSITION));
		dimensions.set(CRJsonUtils.importVector3f(obj, DIMENSIONS));
		roomId=CRJsonUtils.getInteger(obj,ROOM_ID);
	}

	public boolean isPointInside(Vector3f point) {
		int spacing = 10;
		if ((point.x >= position.x - dimensions.x / 2 - spacing)
				&& (point.x <= position.x + dimensions.x / 2 + spacing)) {
			if ((point.z >= position.z - dimensions.z / 2 - spacing)
					&& (point.z <= position.z + dimensions.z / 2 + spacing)) {
				return true;
			}
		}
		return false;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getX() {
		return position.x;
	}

	public float getZ() {
		return position.z;
	}

	public Vector3f getDimensions() {
		return dimensions;
	}

	public void setDimension(Vector3f roomSize) {
		dimensions.set(roomSize);
	}

	public float getWidth() {
		return dimensions.x;
	}

	public float getDepth() {
		return dimensions.z;
	}

	public float getHeight() {
		return dimensions.y;
	}

	public void debugDraw(CRDebugDraw draw) {
		draw.setColor(Color.red);
		draw.fillRect(position.x - dimensions.x / 2, position.z - dimensions.z
				/ 2, dimensions.x, dimensions.z);
		draw.setColor(Color.black);
		draw.drawString(getId()+"",position.x,position.z);
	}

	public void setHeight(float roomHeight) {
		dimensions.y = roomHeight;
	}

	public Vector3f getRandomPointInsideRoom() {
		Vector3f pt = new Vector3f(position.x + dimensions.x
				* CRJavaUtils.random()-dimensions.x/2, position.y, position.z + dimensions.z
				* CRJavaUtils.random()-dimensions.z/2);
		return pt;
	}
	@Override
	public void resetComponent() {
	}

	public void setPosition(Vector3f pos) {
		position.set(pos);
		
	}

	public void setPosition(float x, float y, int z) {
		position.set(x,y,z);
		
	}

	public void setDimension(float w, float h, float d) {
		dimensions.set(w,h,d);
		
	}

	public boolean hasEdges() {
		return (edgePt0!=null && edgePt1!=null);
	}

	
}
