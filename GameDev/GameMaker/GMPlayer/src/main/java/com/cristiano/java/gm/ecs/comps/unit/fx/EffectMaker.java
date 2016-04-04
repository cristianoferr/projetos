package com.cristiano.java.gm.ecs.comps.unit.fx;

import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.utils.GMUtils;
import com.cristiano.jme3.interfaces.IMakeEffects;
import com.cristiano.jme3.noise.effects.EffectDefines;
import com.cristiano.utils.Log;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;

public class EffectMaker implements IMakeEffects {

	EntityManager entMan;
	private int type;
	private FXLibraryComponent lib;
	private EffectDefines elEffect;

	private float ttl = -10000;
	private AssetManager assetManager;

	// products
	private ParticleEmitter particleEmitter;
	private float lowLife;
	private float highLife;
	private AudioNode audio;
	private boolean hasSoundPlayer=false;

	public EffectMaker(int type, EntityManager entMan, FXLibraryComponent lib, AssetManager assetManager) {
		this.entMan = entMan;
		this.type = type;
		this.lib = lib;
		this.elEffect = lib.getEffect(type);
		if (elEffect==null){
			Log.error("No FX found for "+type);
		}
		this.assetManager = assetManager;
		lib.registerFX(this);

		startEffect();
	}

	private void startEffect() {
		if (elEffect.isVisual) {
			startVisualEffect();
		}
		if (elEffect.soundFile != null) {
			try {
			loadSoundFile();
			} catch (Exception e) {
				Log.warn("Exception loadSoundFile:"+e);
			}
		}
	}

	private void startVisualEffect() {
		String materialKey = elEffect.materialKey;
		String textureKey = elEffect.textureKey;
		Material material = lib.createMaterial(materialKey);
		Texture texture = lib.createTexture(textureKey);

		if (elEffect.isParticleEmitter) {
			createParticleEmitter(material, texture);
		}

	}

	private void createParticleEmitter(Material material, Texture texture) {
		particleEmitter = new ParticleEmitter("Emitter effect type:" + type, ParticleMesh.Type.Triangle, elEffect.numParticles);
		if (material != null) {
			particleEmitter = new ParticleEmitter("Emitter effect type:" + type, ParticleMesh.Type.Triangle, elEffect.numParticles);
			particleEmitter.setMaterial(material);
			if (texture != null) {
				material.setTexture("Texture", texture);
			}
		}

		

		if (elEffect.isDefineColors) {
			loadColors(particleEmitter);
		}
		lowLife = elEffect.lowLife;
		highLife = elEffect.highLife;
		particleEmitter.setImagesX(elEffect.imagesX);
		particleEmitter.setImagesY(elEffect.imagesY);
		particleEmitter.setSelectRandomImage(true);
		particleEmitter.getParticleInfluencer().setVelocityVariation(elEffect.velocityVariation);
		float rotateSpeed = elEffect.rotateSpeed;
		if (rotateSpeed != 0) {
			particleEmitter.setRotateSpeed(rotateSpeed);
		}
		float gravity = elEffect.gravity;
		if (gravity != 0) {
			particleEmitter.setGravity(0, gravity, 0);
		}
		float initialVelocity = elEffect.initialVelocity;
		if (initialVelocity != 0) {
			particleEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, initialVelocity, 0));
		}
		particleEmitter.setRandomAngle(true);

	}

	private void loadSoundFile() {
		//Log.debug("Loading sound: "+elEffect.soundFile);
		audio = new AudioNode(assetManager, elEffect.soundFile, false);
		audio.setVolume(elEffect.soundVolume);
		audio.setPositional(true);
		audio.setRefDistance(elEffect.soundRefDistance);
		audio.setMaxDistance(elEffect.soundMaxDistance);
		audio.setLooping(elEffect.soundLoop);
		audio.setPitch(elEffect.soundPitch);
	}

	private void loadColors(ParticleEmitter emitter) {
		ColorRGBA startColor = GMUtils.createColor(elEffect.startColor);
		ColorRGBA endColor = GMUtils.createColor(elEffect.endColor);
		emitter.setEndColor(endColor);
		emitter.setStartColor(startColor);

	}

	@Override
	public IMakeEffects requestFX(int type) {
		return new EffectMaker(type, entMan, lib, assetManager);
	}

	@Override
	public void attachToNode(Node node, Vector3f position) {
		if (particleEmitter != null) {
			if (particleEmitter.getParent() != null) {
				particleEmitter.removeFromParent();
			}
			if (node == null) {
				return;
			}
			particleEmitter.setLocalTranslation(position);
			node.attachChild(particleEmitter);

		}
		
		if (audio!=null){
			node.attachChild(audio);
		}

	}

	@Override
	public void emmit(boolean flag) {
		if (particleEmitter != null) {
			emitParticle(flag);
		}
		playAudio(flag);

	}

	private void playAudio(boolean flag) {
		if (audio != null) {
			if (flag) {
				if (hasSoundPlayer){
					return;
				}
				Log.trace("Playing audio:"+elEffect.soundFile);
				try {
					audio.play();
				} catch (Exception e) {
					Log.warn("Exception playing sound:"+e);
				}
				hasSoundPlayer=true;
			} else {
				if (audio.isLooping()){
					audio.stop();
				}
			}
		}
	}

	private void emitParticle(boolean flag) {
		if (flag) {
			particleEmitter.setLowLife(lowLife);
			particleEmitter.setHighLife(highLife);
		} else {
			particleEmitter.setLowLife(0);
			particleEmitter.setHighLife(0);
		}

	}

	@Override
	public void dieIn(int timeToEffectDie) {
		ttl = timeToEffectDie;
	}

	@Override
	public boolean isAlive(float tpf) {
		if (ttl == -10000) {
			return true;
		}
		ttl -= tpf;
		if (ttl < 0) {
			removeParticleEmitter();
			return false;
		}
		return true;

	}

	private void removeParticleEmitter() {
		if (particleEmitter == null) {
			return;
		}
		emitParticle(false);
		particleEmitter.removeFromParent();

	}
}
