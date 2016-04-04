package com.cristiano.java.gm.nifty;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.consts.GameProperties;
import com.cristiano.java.gm.consts.GameComps;
import com.cristiano.java.gm.ecs.EntityManager;
import com.cristiano.java.gm.ecs.comps.GameComponent;
import com.cristiano.java.gm.ecs.comps.art.ImageRequestComponent;
import com.cristiano.java.gm.ecs.comps.persists.InternationalComponent;
import com.cristiano.java.gm.ecs.comps.ui.AbstractUIComponent;
import com.cristiano.java.gm.ecs.comps.ui.ShortCutComponent;
import com.cristiano.java.gm.interfaces.IGameEntity;
import com.cristiano.java.gm.utils.ECS;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.jme3.assets.GMAssets;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

import de.lessvoid.nifty.builder.ElementBuilder;

public abstract class NiftyPropertyApplier {

	private final static List<IGameElement> _reuseProps=new ArrayList<IGameElement>();

	public static void aplicaProperties(ElementBuilder layerB, IGameElement element, AbstractUIComponent compUI, EntityManager entMan,
			InternationalComponent inter) {
		//Bench: desprezivel
		String elementName = element.getName();
		//Bench: desprezivel
		List<IGameElement> properties = element.getObjectList(GameProperties.PROPERTIES,_reuseProps);
		
		// layerB.backgroundColor("#0f08");
		checkImageArt(element, entMan, compUI);
		corrigeNomeImagem(compUI);
		
		//Bench: desprezivel
		for (IGameElement elProperty : properties) {
			String property = elProperty.getProperty(GameProperties.KEY);
			if (!property.equals("")) {
				aplicaPropriedade(layerB, elProperty, property, inter, elementName,compUI.getElement());
			}

			String action = elProperty.getProperty(GameProperties.ACTION);
			if (!action.equals("")) {
				linkaAcao(action, layerB, elProperty, compUI, entMan);
			}
		}

	}

	private static void corrigeNomeImagem(AbstractUIComponent comp) {
		if (!CRJavaUtils.isRelease()){
			return;
		}
		String imageTag=comp.getElement().getProperty(GameProperties.IMAGE_TAG);
		if (!imageTag.equals("")){
			imageTag=StringHelper.clear(imageTag);
			String val = GMAssets.GEN_FOLDER+"art/"+imageTag+".png";
			comp.getElement().setProperty(GameProperties.FILE_NAME, val);
			
			//List<IGameElement> objectList = comp.getElement().getObjectList(GameProperties.PROPERTIES);
		}
		return ;
	}
	

	public static ImageRequestComponent checkImageArt(IGameElement element, EntityManager entMan, AbstractUIComponent entity) {
		if (CRJavaUtils.isRelease()){
			return null;
		}
		String imageTag = StringHelper.removeChaves(element.getProperty(GameProperties.IMAGE_TAG));
		String baseImage = element.getProperty(GameProperties.FILE_NAME);
		String destImage = element.getProperty(GameProperties.FOLDER_DEST);
		if (!imageTag.equals(""))  {
			List<IGameEntity> ents = entMan.getEntitiesWithComponent(GameComps.COMP_REQUEST_IMAGE);
			ImageRequestComponent requestComp = ECS.checkIfImageIsRequested(ents, imageTag);

			// checking if there is already a art request...
			if (requestComp == null) {
				Log.info("Requesting image:" + imageTag);
				requestComp = (ImageRequestComponent) entMan.spawnComponent(GameComps.COMP_REQUEST_IMAGE);
				IGameElement elRequestImage = element.getElementManager().pickFinal(GameComps.COMP_REQUEST_IMAGE);
				if (elRequestImage!=null){
					requestComp.loadFromElement(elRequestImage);
				} else {
					Log.error("Element ImageRequestComponent is null");
				}
				if (baseImage.equals("")){
					Log.error("BaseImage is empty!");
				}
				requestComp.imageTag = imageTag;
				requestComp.imageSource=baseImage;
				requestComp.destinationFile=destImage+imageTag+".png";
				requestComp.applySourceImgAsFG=false;
				requestComp.destinationEntity=entity;
				requestComp.destinationProp=GameProperties.FILE_NAME;
			}
			// if yes, add to the uicomp so I can change it later...
			entity.attachComponent(requestComp);
			return requestComp;
		}
		return null;
	}

	private static void linkaAcao(String action, ElementBuilder layerB, IGameElement elProperty, GameComponent compUI, EntityManager entMan) {
		String value = elProperty.getProperty(GameProperties.VALUE);
		if (value.equals("")){
			return;
		}
		String method = action + "(" + compUI.getId() + "," + value + ")";
		layerB.interactOnClick(method);
		String shortCut = compUI.getElement().getProperty(GameProperties.SHORTCUT).replace("'", "");
		if (!shortCut.equals("")) {
			ShortCutComponent shortCutComp = (ShortCutComponent) entMan.addComponent(GameComps.COMP_SHORTCUT, compUI);
			shortCutComp.loadFromElement(compUI.getElement().getPropertyAsGE(GameProperties.SHORTCUT));
			shortCutComp.entityOriginator = compUI;
		}
	}

	private static void aplicaPropriedade(ElementBuilder layerB, IGameElement elProperty, String property, InternationalComponent inter, String elementName, IGameElement elComp) {
		String value = elProperty.getProperty(GameProperties.VALUE);
		if (value.equals("")) {
			return;
		}
		value = inter.translate(value);
		if (property.equals(GameProperties.FILE_NAME)){
			value=elComp.getProperty(GameProperties.FILE_NAME);
		}
		layerB.set(property, value);

		//Log.debug("Nifty: Aplicando propriedade " + property + " com valor '" + value + "' em:" + elementName);
	}
}
