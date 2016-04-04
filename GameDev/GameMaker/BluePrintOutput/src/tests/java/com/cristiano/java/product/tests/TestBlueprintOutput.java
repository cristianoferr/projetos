package com.cristiano.java.product.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.Extras;
import com.cristiano.java.product.ElementStore;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.IManageElements;
import com.cristiano.java.product.utils.BPUtils;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.tests.TestData;

public class TestBlueprintOutput {

	@BeforeClass
	public static void setUp() {
		BPUtils.em=new ElementStore();
	}
	@Test
	public void testPropertyStore() {
		
		IGameElement store = BPUtils.createGameElement();
		IGameElement storeCopia = BPUtils.createGameElement();
		IGameElement storeJSON= BPUtils.createGameElement();
		TestData.validaPropertyStore(store, storeCopia,storeJSON);
		
		
		validateExportImportJSON(store,storeCopia,BPUtils.em);
	}
	
	@Test
	public void testElementManager() {
		validataElementManager(BPUtils.em);
	}
	
	public static void validataElementManager(IManageElements em) {
		IGameElement el1 = em.createElement();
		IGameElement el2 = em.createElement();
		String name1 = "element1";
		el1.setName(name1);
		String name2 = "element2";
		el2.setName(name2);
		assertTrue(em.containsElement(el1));
		assertTrue(em.containsElement(el2));
		String tag1 = "tag1aa";
		String tag2 = "taga tagb tagc tagd";
		String tag3 = "tage tagb tagc tagd";
		el1.addTag(tag1);
		el2.addTag(tag2);
		IGameElement pel1 = em.pickFinal(tag1);
		IGameElement pel2= em.pickFinal(tag2);
		IGameElement pel3= em.pickFinal(tag3);
		assertTrue(pel1.getName().equals(el1.getName()));
		assertTrue(pel1.getName().equals(name1));
		assertTrue(pel2.getName().equals(el2.getName()));
		assertTrue(pel2.getName().equals(name2));
		assertNull(pel3);
		
		//test validate
		IGameElement elVal = em.createElement();
		elVal.setProperty("par1","ok");
		assertTrue(elVal.validate());
		elVal.setProperty("par2","$notOk");
		assertFalse(elVal.validate());
		elVal.setProperty("par2","OkAgain");
		assertTrue(elVal.validate());
	}
	@Test public void testIGameElement() {
		IGameElement ge=BPUtils.createGameElement();
		IGameElement gePar=BPUtils.createGameElement();
		
		validateIGenericElement(ge, gePar);
	}

	
	public static void validateExportImportJSON(IGameElement ge, IGameElement geTo,IManageElements emTo) {
		float valF=123.45f;
		String valS="String qualquer";
		String valS2="Outra String qualquer";
		String parF = "valFjson";
		ge.setProperty(parF, valF);
		String parS = "valSjson";
		ge.setProperty(parS, valS);
		String parS2 = "valS2json";
		ge.setParam(Extras.LIST_ACTUATOR,parS2, valS2);
		String tag1="tagabc";
		String tag2="tagabc123";
		ge.addTag(tag1);
		
		JSONObject exportToJSON = ge.exportToJSON();
		geTo.importFromJSON(exportToJSON);
		
		validateElement(ge, geTo, valF, valS, valS2, parF, parS, parS2, tag1,
				tag2);
		
		IGameElement geLoaded=emTo.importElementFromJSON(exportToJSON);
		validateElement(ge, geLoaded, valF, valS, valS2, parF, parS, parS2, tag1,
				tag2);
	}
	private static void validateElement(IGameElement ge, IGameElement geTo,
			float valF, String valS, String valS2, String parF, String parS,
			String parS2, String tag1, String tag2) {
		assertTrue(geTo.getPropertyAsFloat(parF)==valF);
		assertTrue(geTo.getProperty(parS).equals(valS));
		assertTrue(geTo.getParamAsText(Extras.LIST_ACTUATOR,parS2).equals(valS2));
		assertTrue(geTo.id().equals(ge.id()));
		assertTrue(geTo.hasTag(tag1));
		assertFalse(geTo.hasTag(tag2));
	}
	
	public static void validateIGenericElement(IGameElement ge, IGameElement gePar) {
		String namePar="nomeParam";
		gePar.setName(namePar);
		
		assertNotNull(ge.getElementManager());
		assertNotNull(gePar.getElementManager());
		
		String tagTrue="tagTrue";
		String tagFalse="tagFalse";
		String outraTag="outraTag";
		ge.addTag(tagTrue);
		ge.addTag(outraTag);
		String name="nome!";
		ge.setName(name);
		String parProp1="prop1";
		String parProp2="prop2";
		String valProp1="val1";
		String var1="var1";
		String valProp2="valProp2";
		ge.setParam(Extras.LIST_DOMAIN, parProp1, valProp1);
		ge.setParam(Extras.LIST_DOMAIN, parProp2, gePar);
		String parFloat1="parFloat1";
		float valFloat1=123.456f;
		ge.setProperty(parFloat1, valFloat1);
		ge.setVar(var1, valProp2);
		ge.setPropertyTag("propTag", "{abc} {cde}");
		
		Object obj1="tssst";
		ge.setProperty("objProp",obj1);
		assertTrue(ge.getProperty("objProp").equals(obj1));
		//assertTrue(ge.getProperty("objProp")==obj1);
		
		assertTrue(ge.getTagsAsText().contains(tagTrue+" "+outraTag));
		
		String valBool="valBool";
		ge.setProperty(valBool, true);
		ge.setParam(var1, valBool, false);
		assertTrue(ge.getPropertyAsBoolean(valBool));
		assertFalse(ge.getParamAsBoolean(var1, valBool));
		
		
		assertTrue(ge.hasTag(tagTrue));
		assertFalse(ge.hasTag(tagFalse));
		assertTrue(ge.getName().equals(name));
		assertTrue(ge.getParamAsText(Extras.LIST_DOMAIN, parProp1)+"<>"+valProp1,ge.getParamAsText(Extras.LIST_DOMAIN, parProp1).equals(valProp1));
		assertTrue(ge.getParamAsGE(Extras.LIST_DOMAIN, parProp2)==gePar);
		assertTrue(ge.getPropertyAsFloat( parFloat1)+"<>"+valFloat1,ge.getPropertyAsFloat( parFloat1)==valFloat1);
		assertTrue(ge.getVar(var1).equals(valProp2));
		assertTrue(ge.getProperty("propTag").contains("abc cde"));
		assertTrue(ge.hasProperty("propTag"));
		assertFalse(ge.hasProperty("aaasaas"));
		
		validaObjectList(ge,gePar);
		
		validaParamList(ge,gePar);
	}
	private static void validaParamList(IGameElement ge, IGameElement gePar) {
		IGameElement geObj0=gePar.getElementManager().createElement();
		geObj0.setName("obj0");
		IGameElement geObj1=gePar.getElementManager().createElement();
		geObj1.setName("obj1");
		String objName="propObjList";
		
		
		gePar.setProperty(objName, StringHelper.concatenaLista(geObj0.id(),geObj1.id()));
		List<IGameElement> list = gePar.getPropertyAsGEList(objName);
		assertNotNull(list);
		assertTrue(list.size()==2);
		assertTrue(list.contains(geObj0));
		assertTrue(list.contains(geObj1));
		
		gePar.setProperty(objName,geObj0 );
		list = gePar.getPropertyAsGEList(objName);
		assertNotNull(list);
		assertTrue(list.size()==1);
		assertTrue(list.contains(geObj0));
		
		gePar.setProperty(objName,geObj0.id() );
		list = gePar.getPropertyAsGEList(objName);
		assertNotNull(list);
		assertTrue(list.size()==1);
		assertTrue(list.contains(geObj0));
		
		gePar.setProperty(objName, "");
		list = gePar.getPropertyAsGEList(objName);
		assertNotNull(list);
		assertTrue(list.size()==0);
		
	}
	private static void validaObjectList(IGameElement ge, IGameElement gePar) {
		IGameElement geObj0=gePar.getElementManager().createElement();
		geObj0.setName("obj0");
		IGameElement geObj1=gePar.getElementManager().createElement();
		geObj1.setName("obj1");
		String objName="obj";
		
		gePar.setObject(objName,0,geObj0);
		gePar.setParam(Extras.LIST_OBJECT,objName+"#1", geObj1);
		
		List<IGameElement> objectList = gePar.getObjectList(objName);
		assertTrue(objectList.size()==2);
		IGameElement geObj0get0=objectList.get(0);
		IGameElement geObj0get1=objectList.get(1);
		assertTrue(geObj0get0==geObj0);
		assertTrue(geObj0get1==geObj1);
	}

}
