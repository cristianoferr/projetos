package com.cristiano.java.gm.ecs.systems.macro;

import java.util.List;

import com.cristiano.java.gm.builder.utils.ConsoleParserBuilder;
import com.cristiano.java.gm.builder.utils.ExportUtils;
import com.cristiano.java.gm.consts.GameActions;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.consts.GameConsts;
import com.cristiano.java.gm.ecs.comps.art.ImageRequestComponent;
import com.cristiano.java.gm.ecs.comps.macro.BuildManagerComponent;
import com.cristiano.java.gm.ecs.comps.macro.Resolution;
import com.cristiano.java.gm.ecs.comps.macro.ScreenShotSituation;
import com.cristiano.java.gm.ecs.comps.map.MapComponent;
import com.cristiano.java.gm.ecs.comps.materials.TextureComponent;
import com.cristiano.java.gm.ecs.comps.mechanics.MapWorldComponent;
import com.cristiano.java.gm.ecs.comps.persists.GameConstsComponent;
import com.cristiano.java.gm.ecs.comps.ui.NiftyComponent;
import com.cristiano.java.gm.ecs.comps.visual.BillboardComponent;
import com.cristiano.java.gm.ecs.comps.visual.CamComponent;
import com.cristiano.java.gm.ecs.comps.visual.PositionComponent;
import com.cristiano.java.gm.ecs.systems.BuilderAbstractSystem;
import com.cristiano.java.gm.ecs.systems.map.MapWorldSystem;
import com.cristiano.java.gm.ecs.systems.ui.UIActionListenerSystem;
import com.cristiano.java.gm.interfaces.IGameComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.jme3.visualizadores.CRSimpleGame;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.math.Vector3f;

public class BuildManagerSystem extends BuilderAbstractSystem {

	public BuildManagerSystem() {
		super(GameComps.COMP_BUILD_MANAGER);
	}

	@Override
	public void preTick(float tpf){
		super.preTick(tpf);
	}
	
	@Override
	// ent=room
	public void iterateEntity(IGameEntity ent, IGameComponent component,
			float tpf) {
		BuildManagerComponent comp = (BuildManagerComponent) component;

		MapWorldComponent mapWC = (MapWorldComponent) ECS
				.getMapWorldComponent(game.getGameEntity());
		

		NiftyComponent nifty = ECS.getNifty(ent);

		// entMan.getEntitiesWithComponent(GameComps.COMP_ORIENTATION);
		// comp.writeAndQuit=true;
		
		if (mapWC == null) {
			return;
		}
		
		
		if (UIActionListenerSystem.console == null) {
			UIActionListenerSystem.console = new ConsoleParserBuilder();
		}

		if (comp.isOnStage(BuildManagerComponent.STARTING)) {
			if (mapWC.isOnStage(MapWorldComponent.WAITING)
					&& nifty.screensStarted) {
				cleanAssets(comp);
				sendAction(ent, GameActions.ACTION_START_GAME);

				comp.screenShotState = new ScreenshotAppState();
				game.getStateManager().attach(comp.screenShotState);

				comp.nextStage();
			}
		}

		
		if (MapWorldSystem.currentMap==null){
			return;
		}

		
		if (comp.isOnStage(BuildManagerComponent.INIT_TO_RUN)) {
			if (MapWorldSystem.currentMap.isOnStage(MapComponent.RUNNING)) {
				comp.nextStage();
			}
		}

		if (comp.isOnStage(BuildManagerComponent.RUNNING)) {
			if (isGameComplete()) {
				comp.nextStage();

				addConsole(ent);
			} else {
				comp.countFails++;
				Log.info("Not complete... " + comp.countFails);
				if (comp.countFails > comp.maxFails) {
					Log.fatal("Builder failed generating assets...");
				}
			}
		}

		if (comp.isOnStage(BuildManagerComponent.GAME_GENERATED)) {
			if (BuildManagerComponent.writeAndQuit) {
				comp.nextStage();
			}
		}

		setupScreenshots(ent, comp);

		if (comp.isOnStage(BuildManagerComponent.REQUEST_ART)) {
			Log.debug("Request Art");
			requestIcon(comp);
		}
		if (comp.isOnStage(BuildManagerComponent.WAITING_ART)) {
			Log.debug("Waiting art...");
			if (GMAssets.assetExists(comp.getDestinationIcon())) {
				deliverIconsToAndroid(ent, comp);
				comp.nextStage();
			}
		}

		if (comp.isOnStage(BuildManagerComponent.SAVING_STATE)) {
			Log.info("Writing and quiting...");

			// comp.setStage(BuildManagerComponent.WAITING_SCREENSHOT);
			// GMAssets.deleteAsset(comp.getDesinationIcon());

			game.getIntegrationState().writeState();
			System.exit(0);
		}
	}

	// TODO: definir posições para tirar screenshots: mapa, jogador, etc, e
	// tirar screenshot para cada posição
	private void setupScreenshots(IGameEntity ent, BuildManagerComponent comp) {
		NiftyComponent nifty = ECS.getNifty(ent);
		
		if (comp.isOnStage(BuildManagerComponent.SETTING_SCREENSHOT)) {
			Resolution resolution = comp.screenShotOrganizer.getResolution();
			ScreenShotSituation situation = comp.screenShotOrganizer
					.getSituation();
			boolean hideBillboard = true;
			nifty.changeScreenTo(situation.screenName);

			takeScreenshot(comp, comp.screenShotOrganizer.getFilePath()+"/",comp.screenShotOrganizer.getFileName(), hideBillboard, situation.position,
					resolution.getWidth(), resolution.getHeight());
			comp.nextStage();
			return;
		}
		if (comp.isOnStage(BuildManagerComponent.WAITING_SCREENSHOT)) {
			String fileName = comp.screenShotOrganizer.getFileNamePath();//comp.screenShotFilePath + "-"
					//+ comp.screenShotOrganizer.getName();
			if (GMAssets.assetExists(fileName)) {
				if (!comp.screenShotOrganizer.next()) {
					comp.nextStage();
				} else {
					comp.previousStage();
				}

			}
			return;
		}

		if (comp.isOnStage(BuildManagerComponent.SETTING_ICONSHOT)) {
			String fileName = comp.screenShotFilePath;
			boolean hideBillboard = true;
			Vector3f pos = new Vector3f(CRJavaUtils.random(-5, -3),
					CRJavaUtils.random(1, 3), CRJavaUtils.random(5, 3));

			takeScreenshot(comp, "gen/",fileName, hideBillboard, pos,
					GameConsts.ICON_SIZE, GameConsts.ICON_SIZE);

			comp.nextStage();
		}

		if (comp.isOnStage(BuildManagerComponent.WAITING_ICONSHOT)) {
			if (GMAssets.assetExists(comp.getScreenShotAssetPath())) {
				resizeScreenShot(ent, comp);
				comp.nextStage();
			}
		}
	}

	private void addConsole(IGameEntity ent) {
	}

	private void cleanAssets(BuildManagerComponent comp) {
		GMAssets.deleteAsset(GMAssets.getPropertiesPath());
		GMAssets.deleteAsset(comp.getDestinationIcon());
		GMAssets.deleteAsset(comp.getScreenShotAssetPath());
		GMAssets.deleteAsset(comp.getIconSourcePath());
	}

	private void requestIcon(BuildManagerComponent comp) {
		comp.removeComponent(GameComps.COMP_REQUEST_IMAGE);
		ImageRequestComponent requestComp = (ImageRequestComponent) entMan
				.addDefaultComponent(GameComps.COMP_REQUEST_IMAGE, comp,
						getElementManager());
		requestComp.defaultSize = GameConsts.ICON_SIZE;
		requestComp.imageSource = comp.getIconSourcePath();
		requestComp.destinationFile = comp.getDestinationIcon();
		requestComp.applySourceImgAsFG = true;
		comp.nextStage();
		GameConstsComponent gameConstsComponent = getGameConstsComponent();
		requestComp.assetMaterial = gameConstsComponent.iconMaterial;
	}

	private void deliverIconsToAndroid(IGameEntity ent,
			BuildManagerComponent comp) {
		deliverIcon(comp.getDestinationIcon(), "hdpi", 72);
		deliverIcon(comp.getDestinationIcon(), "mdpi", 48);
		deliverIcon(comp.getDestinationIcon(), "xhdpi", 96);
		deliverIcon(comp.getDestinationIcon(), "xxhdpi", 144);
		deliverIcon(comp.getDestinationIcon(), "xxxhdpi", 192);

		String toPath = GameConsts.ANDROID_PATH + "/ic_launcher-web.png";
		ExportUtils.resizeImage(
				GMAssets.getFullFilePath(comp.getDestinationIcon()), toPath,
				GameConsts.ICON_SIZE);
		// toPath=GameConsts.ANDROID_PATH+"/res/drawable/startsplash.png";
		ExportUtils.resizeImage(
				GMAssets.getFullFilePath(comp.getDestinationIcon()), toPath,
				GameConsts.ICON_SIZE);
	}

	private void deliverIcon(String sourceIcon, String resFolder, int toSize) {
		ExportUtils.resizeImage(GMAssets.getFullFilePath(sourceIcon),
				GameConsts.ANDROID_PATH + "/res/drawable-" + resFolder
						+ "/ic_launcher.png", toSize);
	}

	private void resizeScreenShot(IGameEntity ent, BuildManagerComponent comp) {
		GMAssets.deleteAsset(comp.getIconSourcePath());
		ExportUtils.resizeImage(
				GMAssets.getFullFilePath(comp.getScreenShotAssetPath()),
				GMAssets.getFullFilePath(comp.getIconSourcePath()),
				GameConsts.ICON_SIZE);

		if (!GMAssets.assetExists(comp.getIconSourcePath())) {
			Log.fatal("Resized image not found.");
		}

	}

	private void takeScreenshot(BuildManagerComponent comp, String filePath,String fileName,
			boolean hideBillboard, Vector3f pos, int width, int height) {
		game.changeResolution(width, height);
		Log.info("Taking Screenshot: "+filePath+fileName+" at res:"+width+"x"+height);
		getPhysicsSpace().deactivate();
		((CRSimpleGame) game).setDisplayFps(false);
		((CRSimpleGame) game).setDisplayStatView(false);
		IGameEntity playerEnt = entMan.getEntitiesWithComponent(
				GameComps.COMP_PLAYER).get(0);
		PositionComponent posC = ECS.getPositionComponent(playerEnt);
		CamComponent cam = ECS.getCamComponent(playerEnt);

		pos = posC.getPos().add(pos);

		if (hideBillboard) {
			BillboardComponent billboardComponent = ECS
					.getBillboardComponent(playerEnt);
			if (billboardComponent.billboard != null) {
				billboardComponent.billboard.removeFromParent();
			}
		}

		// orient.setOrientation(new Quaternion());
		cam.setEnabled(false);
		cam.cam.setLocation(pos);
		cam.cam.lookAt(pos, Vector3f.UNIT_Y);
		// FlyByCamera flyByCamera = game.getFlyByCamera();
		// flyByCamera.

		comp.screenShotState.setFilePath(GMAssets.getFullFilePath(filePath) );
		comp.screenShotState.setIsNumbered(false);
		comp.screenShotState.setFileName(fileName);
		comp.screenShotState.takeScreenshot();
	}

	private boolean isGameComplete() {
		if (existsComponentActive(GameComps.COMP_BESTIARY_QUERY)) {
			return false;
		}
		if (existsComponentActive(GameComps.COMP_BUBBLE)) {
			return false;
		}
		if (existsComponentActive(GameComps.COMP_ASSET_LOAD_REQUEST)) {
			return false;
		}
		if (existsComponentActive(GameComps.COMP_MESH_LOADER)) {
			return false;
		}

		/*
		 * List<IGameEntity> matEnts =
		 * entMan.getEntitiesWithComponent(GameComps.COMP_MATERIAL); for
		 * (IGameEntity ent:matEnts){ List<IGameComponent> mats =
		 * ent.getComponents(GameComps.COMP_MATERIAL); for (IGameComponent
		 * comp:mats){ MaterialComponent matC=(MaterialComponent) comp; if
		 * (matC.mat==null){ Log.warn("Material is null... "+matC); for
		 * (IGameEntity ent1:matEnts){
		 * Log.warn("  Ent:"+ent1+" active? "+ent1.isActive()); } return false;
		 * } } }
		 */

		List<IGameComponent> matText = entMan
				.getComponentsWithIdentifier(GameComps.COMP_TEXTURE);
		for (IGameComponent comp : matText) {
			TextureComponent text = (TextureComponent) comp;
			if (text.isEmpty()) {
				Log.warn("Texture is empty (game not ready yet):");
				return false;
			}
		}

		Log.info("Game is complete.");
		return true;
	}

}
