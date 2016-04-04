package com.cristiano.java.jme.tests.macro;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.ecs.comps.map.RoomComponent;
import com.cristiano.java.gm.ecs.comps.unit.TeamComponent;
import com.cristiano.java.gm.ecs.systems.macro.TeamSystem;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.Log;
import com.jme3.math.Vector3f;

public class TestTeamSystem extends MockAbstractTest {

	@Test
	public void testGridPosition() {
//		CRJavaUtils.IS_TEST=false;
		TeamSystem teamS=initTeamSystem();
		TeamComponent teamC = startTeamComponent();
		
		RoomComponent roomC=startMapComponent().getStartingRoom();
		assertNotNull(roomC);
		RoomComponent nextRoom = mockRoom(startMapComponent());
		nextRoom.setPosition(nextRoom.getPosition().add(0, 0, -50));
		
		
		assertNotNull(teamC.startingPosition);
		
		int cols=teamC.startingPosition.getPropertyAsInt(GameProperties.COLS);
		int depth=teamC.startingPosition.getPropertyAsInt(GameProperties.DEPTH);
		int width=teamC.startingPosition.getPropertyAsInt(GameProperties.WIDTH);
		assertTrue(cols>0);
		assertTrue(depth>0);
		assertTrue(width>0);
		cols=2;
		depth=5;
		width=2;
		teamC.startingPosition.setProperty(GameProperties.COLS,cols);
		teamC.startingPosition.setProperty(GameProperties.DEPTH,depth);
		teamC.startingPosition.setProperty(GameProperties.WIDTH,width);
		CRDebugDraw draw = new CRDebugDraw(400,400);
		
		Log.debug("Room:"+roomC.getPosition()+"  next:"+nextRoom.getPosition());
		
		draw.drawPointXZ(nextRoom.getPosition(),Color.red);
		
		List<Vector3f> positions=new ArrayList<Vector3f>();
		draw.setColor(Color.BLACK);
		int max = 10;
		for (int i=0;i<max;i++){
			
			Vector3f pos=teamS.getInitialPosition(teamC, roomC,nextRoom,max);
			positions.add(pos);
			Log.debug("pos:"+i+" == "+pos);
			//draw.drawString("i:"+i, pos.x, pos.z);
			Color color = debugColor(i);
			draw.drawPointXZ(pos,color);
				if (i%cols==1){
					//valido para posicoes em pé...
					assertTrue(pos.z==positions.get(i-1).z);
				}
			
		}
		draw.drawPointXZ(roomC.getPosition(),Color.green);
		draw.finishDebugDraw("testGridPosition");
		
	}

	private Color debugColor(int i) {
		Color color = Color.BLACK;
		if (i==0){
			color=Color.cyan;
		}
		if (i==1){
			color=Color.gray;
		}
		if (i==2){
			color=Color.magenta;
		}
		return color;
	}


}
