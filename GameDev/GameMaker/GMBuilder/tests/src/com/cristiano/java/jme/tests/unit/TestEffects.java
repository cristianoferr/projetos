package com.cristiano.java.jme.tests.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.comps.unit.fx.EffectMaker;
import com.cristiano.java.gm.ecs.comps.unit.fx.FXLibraryComponent;
import com.cristiano.java.gm.ecs.systems.persists.AssetLoadRequestSystem;
import com.cristiano.java.gm.ecs.systems.unit.fx.FXLibrarySystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.jme3.interfaces.IMakeEffects;
import com.cristiano.jme3.noise.effects.EffectDefines;
import com.cristiano.jme3.noise.effects.GameEffects;
import com.jme3.material.Material;

public class TestEffects extends MockAbstractTest {

	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	@Test
	public void testEffectLibrary() {
		FXLibraryComponent lib = initFXLib();
		
		EffectDefines elFx=lib.getEffect(GameEffects.FX_EXHAUST);
		assertNotNull("elFx null...",elFx);
		assertTrue("fx type<>esperado:"+elFx.value,elFx.value==GameEffects.FX_EXHAUST);
		
		Material material1 = lib.createMaterial("Common/MatDefs/Misc/Particle.j3md");
		assertNotNull("Material null",material1);
		Material material2 = lib.createMaterial("Common/MatDefs/Misc/Particle.j3md");
		assertTrue("material1 must be equal to material1 (reusing)",material1==material2);
	}

	private FXLibraryComponent initFXLib() {
		FXLibraryComponent lib = mockFXLibComponent();
		startReuseComponent();
		AssetLoadRequestSystem loadS = initAssetLoadRequestSystem();
		
		FXLibrarySystem libS = initFXLibrarySystem();
		lib.firstTick=true;
		libS.iterateEntity(entity, lib, 0);
		
		List<IGameComponent> loadReqs = entity.getComponentsWithIdentifier(GameComps.COMP_ASSET_LOAD_REQUEST);
		assertTrue("LoadRequests empty",loadReqs.size()>0);
		for (IGameComponent req:loadReqs){
			loadS.iterateEntity(entity, req, 0);
		}
		
		return lib;
	}

	protected FXLibraryComponent mockFXLibComponent() {
		FXLibraryComponent lib = startFXLibraryComponent();
		
		return lib;
	}

	@Test
	public void testEffectMaker() {
		FXLibraryComponent lib = initFXLib();
		
		IMakeEffects fx=new EffectMaker(0,entMan,lib,game.getAssetManager());
		
		IMakeEffects fxChild = fx.requestFX(GameEffects.FX_EXHAUST);
		assertNotNull("requestFx retornou nulo",fxChild);

	}

}
