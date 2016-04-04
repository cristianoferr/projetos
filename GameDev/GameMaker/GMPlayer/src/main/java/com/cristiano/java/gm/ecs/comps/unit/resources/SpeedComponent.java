package com.cristiano.java.gm.ecs.comps.unit.resources;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.rigidBody.CharDefines;
import com.jme3.math.Vector3f;

public class SpeedComponent extends ResourceComponent {

	public Vector3f velocity=null;//set by the physics sytem, pointing to the model, no need to update
	
	public CharDefines defines; //direct connection to how the rigidbody behaves...
	private float strafeStrength;
	private float rotationStrength;
	private float jumpStrength;
	private float movementStrength;

	public SpeedComponent(){
		super(GameComps.COMP_SPEED);
	}

	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		loadData(ge);
	}

	private void loadData(IGameElement ge) {
		this.strafeStrength=ge.getPropertyAsFloat(GameProperties.STRAFE_STR);
		this.rotationStrength=ge.getPropertyAsFloat(GameProperties.ROTATION_STR);
		this.jumpStrength=ge.getPropertyAsFloat(GameProperties.JUMP_STR);
		this.movementStrength=ge.getPropertyAsFloat(GameProperties.MOVEMENT_STR);
	}	

	@Override
	public IGameComponent clonaComponent() {
		SpeedComponent ret = new SpeedComponent();
		ret.strafeStrength=strafeStrength;
		finishClone(ret);
		ret.rotationStrength=rotationStrength;
		ret.jumpStrength=jumpStrength;
		ret.movementStrength=movementStrength;
		return ret;
	}

	public CharDefines createCharDefines() {
		CharDefines def=new CharDefines();
		this.defines=def;
		updateDefines();
		return def;
	}

	private void updateDefines() {
		defines.strafeStrength=currValue*strafeStrength;
		defines.movementStrength=currValue*movementStrength;
		defines.rotationStrength=currValue*rotationStrength;
		defines.jumpStrength=currValue*jumpStrength;
		
	}	
	
	@Override
	public void resetComponent() {
	}
}
