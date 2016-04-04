package com.cristiano.java.jme.tests.mocks;

import com.cristiano.java.blueprintManager.tests.TestStrings;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.interfaces.state.IIntegrationGameState;
import com.cristiano.java.gm.states.BPBuilderState;
import com.cristiano.java.gm.visualizadores.IFinalGame;
import com.cristiano.jme3.consts.JMEConsts;
import com.cristiano.jme3.utils.JMESnippets;
import com.cristiano.jme3.visualizadores.ReadWriteGame;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.shadow.AbstractShadowRenderer;
import com.jme3.system.JmeContext;

public class MockGame extends ReadWriteGame implements IFinalGame {

	private IGameEntity entity;
	private JMESnippets snippets;
	private FilterPostProcessor fpp;
	private BPBuilderState integr;

	public MockGame(IGameEntity ent) {
		this.entity = ent;
	}

	@Override
	public void initBlueprintIntegration() {
		Log.debug("MockGame.initBlueprintIntegration()");
		if (integr == null) {
			this.integr = new BPBuilderState();
		}
		stateManager.attach(integr);
	}

	@Override
	public IIntegrationGameState getIntegrationState() {
		return integr;
	}

	@Override
	public InputManager getInputManager() {
		if (inputManager == null) {
			inputManager = new InputManager(new MouseInput() {

				@Override
				public void update() {
					// TODO Auto-generated method stub

				}

				@Override
				public void setInputListener(RawInputListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean isInitialized() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void initialize() {
					// TODO Auto-generated method stub

				}

				@Override
				public long getInputTimeNanos() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public void destroy() {
					// TODO Auto-generated method stub

				}

				@Override
				public void setNativeCursor(JmeCursor cursor) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setCursorVisible(boolean visible) {
					// TODO Auto-generated method stub

				}

				@Override
				public int getButtonCount() {
					// TODO Auto-generated method stub
					return 0;
				}
			}, new KeyInput() {

				@Override
				public void update() {
					// TODO Auto-generated method stub

				}

				@Override
				public void setInputListener(RawInputListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean isInitialized() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void initialize() {
					// TODO Auto-generated method stub

				}

				@Override
				public long getInputTimeNanos() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public void destroy() {
					// TODO Auto-generated method stub

				}
			}, null, null);
		}
		return inputManager;

	}

	@Override
	public IGameEntity getGameEntity() {
		return entity;
	}

	@Override
	public void setGameEntity(IGameEntity entity) {
		this.entity = entity;

	}

	public void startHeadless() {
		start(JmeContext.Type.Headless);
		// simpleInitApp();
		if (integr==null){
			this.integr = new BPBuilderState();
		}
	}

	@Override
	public JMESnippets getSnippets() {
		return snippets;
	}

	@Override
	public Camera getCamera() {
		return cam;
	}

	@Override
	public Node getRootNode() {
		return rootNode;
	}

	@Override
	public AssetManager getAssetManager() {
		return assetManager;
	}

	@Override
	public FlyByCamera getFlyByCamera() {
		return null;
	}

	@Override
	public Application getApplication() {
		return this;
	}

	@Override
	public void stop() {

	}

	

	@Override
	public void loadingComplete() {

	}

	@Override
	public void addDebugBox(Vector3f add, String string) {

	}

	@Override
	public void simpleInitApp() {
		if (CRJavaUtils.isDesktop()) {
			assetManager.registerLocator(JMEConsts.assetsRoot, FileLocator.class.getName());
		}
		initSnippets();
		Log.debug("MockGame.simpleInitApp()");
		initBlueprintIntegration();
	}

	private void initSnippets() {
		if (snippets!=null){
			return;
		}
		this.snippets = new JMESnippets(this, rootNode, cam, inputManager, assetManager);
	}

	@Override
	public String getMacroDefinitionTag() {
		return TestStrings.TEST_MACRODEF_TAG;
	}

	@Override
	public FilterPostProcessor getFieldPostProcessor() {
		if (fpp == null) {
			fpp = new FilterPostProcessor(assetManager);
			viewPort.addProcessor(fpp);
			int numSamples = getContext().getSettings().getSamples();
			if (numSamples > 0) {
				fpp.setNumSamples(numSamples);
			}
		}
		return fpp;
	}

	@Override
	public void addShadowRender(AbstractShadowRenderer dlsr) {

	}

	@Override
	public int getWidth() {
		return settings.getWidth();
	}

	@Override
	public int getHeight() {
		return settings.getHeight();
	}

	@Override
	public void getScreenCoordinates(Vector3f pos3d, Vector3f pos2d) {
		pos2d.set(pos3d);
	}

	public void setIntegrationState(BPBuilderState integr) {
		this.integr = integr;

	}

	@Override
	public void changeResolution(int width, int height) {
		// TODO Auto-generated method stub
		
	}
}
