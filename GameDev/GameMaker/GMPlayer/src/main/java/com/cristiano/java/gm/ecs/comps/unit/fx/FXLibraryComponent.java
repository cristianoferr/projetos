package com.cristiano.java.gm.ecs.comps.unit.fx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import com.cristiano.consts.GameProperties;
import com.cristiano.data.CRJsonUtils;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.persists.ReuseManagerComponent;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.product.IGameElement;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.jme3.interfaces.IMakeEffects;
import com.cristiano.jme3.noise.effects.EffectDefines;
import com.cristiano.utils.Log;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

public class FXLibraryComponent extends GameComponent {
	private static final String VOLUME = "volume";
	private static final String REF_DISTANCE = "refDistance";
	private static final String MAX_DISTANCE = "maxDistance";
	private static final String LOOP = "loop";
	private static final String IMAGES_X = "imagesX";
	private static final String IMAGES_Y = "imagesY";
	private static final String VELOCITY_VARIATION = "velocityVariation";
	private static final String HIGH_LIFE = "highLife";
	private static final String LOW_LIFE = "lowLife";
	private static final String INITIAL_VELOCITY = "initialVelocity";
	private static final String GRAVITY = "gravity";
	private static final String ROTATE_SPEED = "rotateSpeed";
	private static final String PITCH ="pitch";

	// public HashMap<String,IGameElement> library =new
	// HashMap<String,IGameElement>();
	public final List<IGameElement> effects=new ArrayList<IGameElement>();
	public final HashMap<Integer, EffectDefines> effectData = new HashMap<Integer, EffectDefines>();

	public final List<IMakeEffects> effectsInUse = new ArrayList<IMakeEffects>();
	public ReuseManagerComponent _reuseComp;

	public FXLibraryComponent() {
		super(GameComps.COMP_FX_LIB);

	}

	@Override
	public void free() {
		super.free();
		effects.clear();
		effectData.clear();
		effectsInUse.clear();
		_reuseComp=null;
	}
	
	@Override
	public void loadFromElement(IGameElement ge) {
		super.loadFromElement(ge);
		ge.getObjectList(GameProperties.EFFECTS,effects);
		loadEffectDefines();
	}

	private void loadEffectDefines() {
		for (int i = 0; i < effects.size(); i++) {
			IGameElement elEffect = effects.get(i);
			EffectDefines ed = new EffectDefines();

			ed.lowLife = elEffect.getPropertyAsFloat(LOW_LIFE);
			ed.highLife = elEffect.getPropertyAsFloat(HIGH_LIFE);
			ed.imagesX = elEffect.getPropertyAsInt(IMAGES_X);
			ed.imagesY = elEffect.getPropertyAsInt(IMAGES_Y);
			ed.velocityVariation = elEffect.getPropertyAsFloat(VELOCITY_VARIATION);
			ed.isVisual = elEffect.getPropertyAsBoolean(GameProperties.IS_VISUAL);
			ed.isSFX = elEffect.getPropertyAsBoolean(GameProperties.IS_SFX);
			ed.materialKey = elEffect.getProperty(GameProperties.MATERIAL);
			ed.textureKey = elEffect.getProperty(GameProperties.TEXTURE);
			ed.isParticleEmitter = elEffect.getPropertyAsBoolean(GameProperties.IS_PARTICLE_EMITTER);
			ed.numParticles = elEffect.getPropertyAsInt(GameProperties.NUM_PARTICLES);
			ed.isDefineColors = elEffect.getPropertyAsBoolean(GameProperties.DEFINE_COLORS);
			ed.rotateSpeed = elEffect.getPropertyAsFloat(ROTATE_SPEED);
			ed.gravity = elEffect.getPropertyAsFloat(GRAVITY);
			ed.initialVelocity = elEffect.getPropertyAsFloat(INITIAL_VELOCITY);
			ed.startColor = elEffect.getProperty(GameProperties.START_COLOR);
			ed.endColor = elEffect.getProperty(GameProperties.END_COLOR);
			ed.value = elEffect.getPropertyAsInt(GameProperties.VALUE);
			
			IGameElement sfx = elEffect.getPropertyAsGE(GameProperties.SOUND_OBJ);
			if (sfx != null) {
				loadSoundData(ed, sfx);
			}
			effectData.put(ed.value, ed);
		}

	}
	
	@Override
	protected boolean exportComponentToJSON(JSONObject obj) {
		obj.put(GameProperties.EFFECTS, CRJsonUtils.exportList(effects));
		return true;
	}

	@Override
	protected void importComponentFromJSON(JSONObject obj) {
		CRJsonUtils.importList((JSONObject) obj.get(GameProperties.EFFECTS),factory,effects);
		loadEffectDefines();
	}

	private void loadSoundData(EffectDefines ed, IGameElement sfx) {
		ed.soundFile = GMAssets.requestAsset(sfx.getProperty(GameProperties.VALUE));
		ed.soundLoop = sfx.getPropertyAsBoolean(LOOP);
		ed.soundMaxDistance = sfx.getPropertyAsFloat(MAX_DISTANCE);
		ed.soundRefDistance = sfx.getPropertyAsFloat(REF_DISTANCE);
		ed.soundVolume = sfx.getPropertyAsFloat(VOLUME);
		ed.soundPitch= sfx.getPropertyAsFloat(PITCH);
	}

	@Override
	public IGameComponent clonaComponent() {
		FXLibraryComponent ret = new FXLibraryComponent();
		ret.effects.addAll(effects);
		return ret;
	}

	// Type is the same as deffined in GameEffects
	public EffectDefines getEffect(int type) {
		return effectData.get(type);
	}

	public Material createMaterial(String material) {
		if (_reuseComp == null) {
			Log.fatal("No ReuseComponent defined");
			return null;
		}
		return (Material) _reuseComp.getObjectWithKey(material);
	}

	public Texture createTexture(String textureKey) {
		if (_reuseComp == null) {
			Log.fatal("No ReuseComponent defined");
			return null;
		}
		return (Texture) _reuseComp.getObjectWithKey(textureKey);
	}

	@Override
	public void resetComponent() {
	}

	public void registerFX(IMakeEffects effectMaker) {
		effectsInUse.add(effectMaker);
	}

	public void unregisterFX(IMakeEffects effectMaker) {
		effectsInUse.remove(effectMaker);
	}

}
