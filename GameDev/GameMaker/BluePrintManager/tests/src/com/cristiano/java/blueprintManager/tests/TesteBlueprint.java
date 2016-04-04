package com.cristiano.java.blueprintManager.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.Extras;
import com.cristiano.data.PropertyStore;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.bpM.entidade.blueprint.Blueprint;
import com.cristiano.java.bpM.entidade.blueprint.Factory;
import com.cristiano.java.bpM.entidade.blueprint.Mod;
import com.cristiano.java.bpM.params.ParamList;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.params.Parametro;
import com.cristiano.java.product.utils.StringHelper;

public class TesteBlueprint extends AbstractBlueprintTest{
	static ElementManager em = null;


	@BeforeClass
	public static void tearUp() throws IOException {
		em = new ElementManager();
		em.loadTestBlueprintsFromFile();
}
	
	
	
	@Test
	public void testModWithElements() {
		IGameElement testeMod = em.pickFinal("testeModProp leaf");
		assertNotNull(testeMod);
		String screens = testeMod.getProperty("screens");
		assertFalse(screens.contains("'"));
	}
	
	@Test
	public void testPropertyStore() {
		AbstractElement store = (AbstractElement) em.pickFinal("PropertyStore leaf");
		
		float vf=(float) 10.123;
		assertTrue(store.getPropertyAsFloat("vf")==vf);
		
		int vi=1004;
		assertTrue(store.getPropertyAsInt("vi")==vi);
		
		String vs="String de Teste";
		assertTrue(store.getProperty("vs").equals(vs));
		
		PropertyStore storeCopia=new PropertyStore();
		int va=1234;
		storeCopia.setProperty("orig", va);
		
		store.getPropertiesInto(storeCopia);
		assertTrue(storeCopia.getProperty("vs").equals(vs));
		assertTrue(storeCopia.getPropertyAsInt("vi")==vi);
		assertTrue(storeCopia.getPropertyAsFloat("vf")==vf);
		assertTrue(storeCopia.getPropertyAsInt("orig")==va);
	}
	
	
	@Test
	public void testModAncestral() {
		AbstractElement geFact = (AbstractElement) em.pickFinal("factTest leaf");
		
		verificaPropriedade(geFact,"valFactory","factory");
		verificaPropriedade(geFact,"valTheme","tema");
		verificaPropriedade(geFact,"valGoal","goal");
		verificaPropriedade(geFact,"value","123");//from acessoFilho
		verificaPropriedade(geFact,"valCommonMod","mod-comum");
	}

	private void verificaPropriedade(AbstractElement geFact, String prop, String expected) {
		String val=geFact.getProperty(prop);
		assertTrue(prop+" differs from expected: '"+val+"'<>'"+expected+"'",val.equals(expected));
	}

	@Test
	public void testFactoryProperties() {
		// validando factory direto
		int qtdElementsPre=em.size();
		IGameElement geFact = (AbstractElement) em.pickFinal("factoryProperties fact");
		String tag = geFact.getProperty("tag");
		assertTrue("tag devia ser acessoFilho:" + tag, tag.contains("acessoFilho"));
		IGameElement obj = geFact.getPropertyAsGE("obj");
		String ident = obj.getIdentifier();
		assertTrue("ident devia ser acessoFilho mas é:" + ident, ident.equals("acessoFilho"));
		List<IGameElement> controls = geFact.getPropertyAsGEList("controls");
		assertTrue("Devia ter 2:"+controls.size(),controls.size()==2);

		int qtdElementsPos=em.size();
		int dif=qtdElementsPos-qtdElementsPre;
		assertTrue("Only 4 elements should have been created:"+dif,dif==4);
		qtdElementsPre=em.size();
		
		
		// validando via pickfinal
		AbstractElement gePicker = (AbstractElement) em.pickFinal("factoryProperties pickfinal");
		geFact = gePicker.getPropertyAsGE("factoryObj");
		assertNotNull("factory is null",geFact);
		tag = geFact.getProperty("tag");
		assertTrue("tag devia ser acessoFilho:" + tag, tag.contains("acessoFilho"));
		obj = geFact.getPropertyAsGE("obj");
		ident = obj.getIdentifier();
		assertTrue("ident devia ser acessoFilho mas é:" + ident, ident.equals("acessoFilho"));
		
		controls = geFact.getPropertyAsGEList("controls");
		assertTrue("Devia ter 2:"+controls.size(),controls.size()==2);

		 qtdElementsPos=em.size();
		 dif=qtdElementsPos-qtdElementsPre;
		assertTrue("Only 6 elements should have been created:"+dif,dif==6);
		//elementos adicionados:
		//TestFactory_pickFinal
		//TestFactory_pickFinal.controls:elementoVazio1
		//TestFactory_Base (base da factory)
		//TestFactory_Base.controls:elementoVazio1
		//TestFactory_Base.controls:elementoVazio2
		//TestFactory_Base.tag:acessoFilho
		
	}

	@Test
	public void testTagSimilar() {
		IGameElement ge = em.pickOne("testValues leaf");
		String result = ge.resolveFunctionOf("{heightProp valignProp}+{heightProp alignProp}");
		String[] splitList = StringHelper.splitList(result);
		assertTrue("Length should be 3 but is: " + splitList.length, splitList.length == 3);
		validaTagRepetition(splitList);

	}

	private void validaTagRepetition(String[] splitList) {
		String tag = "heightProp";
		int c = 0;
		for (int i = 0; i < splitList.length; i++) {
			String st = splitList[i];
			if (st.equals(tag)) {
				c++;
			}
		}
		assertTrue("Tag '" + tag + "' error:" + c + " (should be 1)", c == 1);
	}

	

	@Test
	public void testParamList() {
		AbstractElement gePai = (AbstractElement) em.pickFinal("testParamList");
		String[] list = gePai.getParamAsList(Extras.LIST_PROPERTY, "value");
		assertTrue(list.length == 4);
		assertTrue(list[0].equals("a") || list[1].equals("a") || list[2].equals("a") || list[3].equals("a"));
		String[] list2 = gePai.getPropertyAsList("value");
		assertTrue(list2.length == 4);
	}

	@Test
	public void testReferenciaLista() {
		AbstractElement gePai = (AbstractElement) em.pickFinal("acessoLista");
		String function = "$this#lista1.valProp";
		String valDireto = gePai.resolveFunctionOf(function);
		assertTrue("Resultado difere:" + valDireto, valDireto.equals("1"));
		String val1 = gePai.getProperty("valLista1");
		assertTrue("val1:" + val1 + " difere de 1", val1.equals("1"));
		String val2 = gePai.getProperty("valLista2");
		assertTrue("val2:" + val2 + " difere de 2", val2.equals("2"));
	}

	

	
	@Test
	public void testAcessoPropriedadeFilho() {
		AbstractElement gePai = (AbstractElement) em.pickFinal("acessoPai teste");
		String valDireto = gePai.resolveFunctionOf("$this.obj.value");

		assertTrue("valDireto devia ser 123:" + valDireto, valDireto.equals("123"));
		String valDiretoComCalculo = gePai.resolveFunctionOf("param=$this.obj.value");
		assertTrue("valDiretoComCalculo devia ser param=123:" + valDiretoComCalculo, valDiretoComCalculo.equals("param=123"));
		valDiretoComCalculo = gePai.resolveFunctionOf("param=$this.obj.valueM");
		assertTrue("valDiretoComCalculo devia ser param=-123:" + valDiretoComCalculo, valDiretoComCalculo.equals("param=-123"));

		IGameElement geFilho = gePai.getPropertyAsGE("obj");
		assertNotNull("filho nulo", geFilho);
		String valFilho = gePai.getProperty("valFilho");
		assertTrue("valFilho devia ser 123:" + valFilho, valFilho.equals("123"));
		String valFilho123 = gePai.getProperty("valFilho123");
		assertTrue("valFilho123 devia ser 123:" + valFilho123, valFilho123.equals("123"));

	}

	@Test
	public void testNotacaoCientifica() {
		IGameElement ge = em.pickOne("entidade test");
		String ret = ge.resolveFunctionOf("1/1000000");
		assertTrue("erro notacao cientifica:" + ret, ret.equals("0.000001"));
	}

	@Test
	public void testChaves() {
		IGameElement ge = em.pickOne("entidade test");
		String ret = ge.resolveFunctionOf("{abc}+{!ab}");
		assertTrue("chaves erro:" + ret, ret.equals("{abc !ab}"));
	}

	@Test
	public void testHasTag() {
		AbstractElement ge = (AbstractElement) em.pickOne("entidade test");
		assertNotNull("entidade test null", ge);
		String paramDomain = "domainTag";
		ge.setParam(Extras.LIST_DOMAIN,paramDomain,"{tag1 tag2}");
		
		assertTrue(ge.hasTag(paramDomain,"tag1"));
		assertTrue(ge.hasTag(paramDomain,"tag2"));
		assertFalse(ge.hasTag(paramDomain,"tag3"));

		assertTrue(ge.hasTag("entidade"));
		assertTrue(ge.hasTag("entidade test"));
		assertFalse("ge possui tags:" + ge.getTagsAsText(), ge.hasTag("entidade testt"));
		assertFalse("erro: " + ge.getTagsAsText(), ge.hasTag("testt"));
		assertTrue("erro: " + ge.getTagsAsText(), ge.hasTag("test"));

		assertTrue(ge.hasAnyTag("entidade teste"));
		assertTrue(ge.hasAnyTag("entidad test"));
		assertTrue(ge.hasAnyTag("entidade test"));

		assertFalse(ge.hasTag("entidad teste"));
	}

	@Test
	// Problema: objeto tem 2 filhos, instanciados via pickfinal, cada filho
	// instancia 1 neto, com valores que deveriam ser identicos em ambos os
	// filhos
	public void testMirrorValue() {
		AbstractElement gePai = (AbstractElement) em.pickFinal("mirrorIdentico pai");
		assertNotNull(gePai);
		List<IGameElement> filhos = gePai.getObjectList("filho");
		IGameElement geFilho1 = filhos.get(0);
		IGameElement geFilho2 = filhos.get(1);
		assertNotNull(geFilho1);
		assertNotNull(geFilho2);
		IGameElement neto1 = (IGameElement) geFilho1.getObjectList("neto").get(0);
		IGameElement neto2 = (IGameElement) geFilho2.getObjectList("neto").get(0);
		assertNotNull(neto1);
		assertNotNull(neto2);
		assertTrue(geFilho1.getProperty("valorNeto1").equals(geFilho2.getProperty("valorNeto1")));
		assertTrue(geFilho1.getProperty("valorNeto3").equals(geFilho2.getProperty("valorNeto3")));

	}

	@Test
	public void testPickSingle() {
		IGameElement ge = em.pickOne("test");
		String pick1 = ge.resolveFunctionOf("pickSingle({test2})");
		String pick2 = ge.resolveFunctionOf("pickSingle({test2})");
		assertNotNull(pick1);
		assertTrue("pickSingle nao foi resolvido", !pick1.equals("pickSingle({test2})"));
		assertEquals("pickSingle nao retornou o mesmo objeto", pick1, pick2);
	}

	@Test
	public void testKeywords() {
		verificaKeywords("key1");
		verificaKeywords("key2");
		verificaKeywords("key3");

		String keyword = "keyInexiste";
		IGameElement ge = em.pickOne("palavraChave keyword:" + keyword);
		assertNotNull("elemento nulo com keyWord inexistente", ge);

	}

	private void verificaKeywords(String keyword) {
		IGameElement ge = em.pickOne("palavraChave keyword:" + keyword);
		assertNotNull("elemento com keyWord nulo", ge);
		String ret = ge.getProperty("name");
		assertTrue("Retorno diferente do esperado: '" + ret + "'<>'" + keyword + "'", ret.equals(keyword));
		ge = (AbstractElement) em.pickFinal("palavraChave keyword:" + keyword);
		assertNotNull("elemento final com keyWord nulo", ge);
		assertTrue("Retorno diferente do esperado: '" + ret + "'<>'" + keyword + "'", ret.equals(keyword));
	}

	@Test
	public void testTemplate() {
		ElementManager emLocal = new ElementManager();
		criaTestBlueprints(emLocal);
		AbstractElement ge;
		String tag;

		tag = "blueprint weapon";
		ge = (com.cristiano.java.bpM.entidade.AbstractElement) emLocal.pickFinal(tag);
		assertTrue("nenhum '" + tag + "' encontrado", ge != null);
		int valor = ge.getParamAsInt(Extras.LIST_PROPERTY, "valor55");
		assertTrue("Valor diferente de 55:" + valor, valor == 55);

		ge.setVar("teste", valor);
		int valRet = Integer.parseInt(ge.getVar("teste"));
		assertTrue("GetValor diferente de " + valor + ":" + valRet, valor == valRet);

		ge.setParam(Extras.LIST_PROPERTY, "valorTeste=123");
		valor = ge.getParamAsInt(Extras.LIST_PROPERTY, "valorTeste");
		assertTrue("Valor diferente de 123:" + valor, valor == 123);
		assertTrue("ge n���o possui param valorTeste", ge.hasParam(Extras.LIST_PROPERTY, "valorTeste"));
		ge.unsetParam(Extras.LIST_PROPERTY, "valorTeste");
		assertFalse("ge possui param valorTeste", ge.hasParam(Extras.LIST_PROPERTY, "valorTeste"));

		// ge = emLocal.pickOne(tag);
		IGameElement estende = ge.getEstende();
		assertNotNull("estende nulo", estende);

	}

	@Test
	public void testRefresh() throws IOException {

		GenericElement ge = (GenericElement) em.createFinalElementFromTag("testRefresh");
		assertNotNull(ge);
		String resultado = ge.getProperty("valorRef");
		String esperado = "111";
		assertTrue("Resultado Original diferente para: " + esperado + "<>" + resultado, resultado.equals(esperado));

		esperado = "222";
		ge.setProperty("valorOriginal", esperado);
		ge.refresh();
		resultado = ge.getProperty("valorRef");
		assertTrue("Resultado Original diferente para: " + esperado + "<>" + resultado, resultado.equals(esperado));
	}

	@Test
	public void testEnum() throws IOException {

		GenericElement ge = (GenericElement) em.createFinalElementFromTag("testuserfunction");
		assertNotNull(ge);
		String function = "enumValue(vertical childLayout)";
		String resultado = ge.resolveFunctionOf(function);
		String esperado = "vertical";
		assertTrue("Resultado diferente para '" + function + "': " + esperado + "<>" + resultado, resultado.equals(esperado));

		function = "enumValue(childLayout)";
		resultado = ge.resolveFunctionOf(function).replace("'", "");
		boolean ok = false;
		if (resultado.equals("center")) {
			ok = true;
		}
		if (resultado.equals("horizontal")) {
			ok = true;
		}
		if (resultado.equals("vertical")) {
			ok = true;
		}
		assertTrue("Resultado diferente para '" + function + "': " + resultado, ok);

	}

	@Test
	public void testDomainMaisculo() throws IOException {

		IGameElement pickOne = em.pickOne("dOmAiN");
		assertNotNull(pickOne);
	}

	@Test
	public void testAdicaoTagComKeyword() throws IOException {
		AbstractElement ge = em.createFinalElementFromTag("testuserfunction");
		verificaFunction(ge, "{a}+{b}", "{a b}");
		verificaFunction(ge, "{a}+{keyword:key1}", "{a keyword:key1}");
		verificaFunction(ge, "{keyword:key1}+{a}", "{a keyword:key1}");
		verificaFunction(ge, "{a}+{keyword:key1}+{b}", "{a b keyword:key1}");
		verificaFunction(ge, "{a keyword:key1}+{b}", "{a b keyword:key1}");
		verificaFunction(ge, "{a keyword:key1}+{b keyword:key2}", "{a b keyword:key1 key2}");
	}

	

	@Test
	public void testReplicateLista() throws IOException {

		AbstractElement gePai = em.createFinalElementFromTag("testreplicatelista");
		List<IGameElement> lista = gePai.getObjectList("testes");
		assertTrue("Tamanho da lista devia ser 3 but is " + lista.size(), lista.size() == 3);
	}

	@Test
	public void testaFactoryComChildPropertySimples() throws IOException {

		AbstractElement gePai = em.createFinalElementFromTag("simpleschildproperty factory");
		assertNotNull("gePai nulo", gePai);

		// testando propriedade do pai
		String propPai = gePai.getProperty("tagTest");
		assertTrue("PropPai errada:" + propPai, propPai.contains("testRefresh"));
		IGameElement objPai = gePai.getPropertyAsGE("objetoPai");
		assertNotNull("objPai nulo", objPai);
		assertTrue("objPai identifier errado:" + objPai.getIdentifier(), objPai.getIdentifier().equals("testaRefresh"));

		IGameElement geFilho = gePai.getParamAsGE(Extras.LIST_OBJECT, "filho#0");
		IGameElement geFilhoLista = gePai.getParamAsGE(Extras.LIST_OBJECT, "filhoLista#0");
		assertNotNull("geFilho nulo", geFilho);
		verificaChild(gePai, "filho#0", "5000", "valor1");
		verificaChild(gePai, "filho#0", "555", "valor2");
		verificaChild(gePai, "filho#0", "0", "valor3");
		verificaChild(gePai, "filho#0", "13245", "valor4");
		verificaChild(gePai, "filho#0", "0", "valor5");
		verificaChild(gePai, "filho#0", "123", "valor6");
		verificaChild(gePai, "filho#0", "555", "valor7");

		verificaChild(gePai, "filho#1", "5000", "valor1");
		verificaChild(gePai, "filho#1", "555", "valor2");
		verificaChild(gePai, "filho#1", "1", "valor3");
		verificaChild(gePai, "filho#1", "13245", "valor4");
		verificaChild(gePai, "filho#1", "2", "valor5");

		verificaChild(gePai, "filho#2", "5000", "valor1");
		verificaChild(gePai, "filho#2", "555", "valor2");
		verificaChild(gePai, "filho#2", "13245", "valor4");
		verificaChild(gePai, "filho#2", "2", "valor3");

		// Lista
		verificaChild(gePai, "filhoLista#0", "5000", "valor1");
		verificaChild(gePai, "filhoLista#0", "555", "valor2");
		verificaChild(gePai, "filhoLista#0", "123", "valor3");
		verificaChild(gePai, "filhoLista#0", "132451", "valor4");
		verificaChild(gePai, "filhoLista#0", "1231", "valor6");
		verificaChild(gePai, "filhoLista#0", "5551", "valor7");

		verificasChildPropertiesFilhos(gePai, true);
	}

	private void verificasChildPropertiesFilhos(AbstractElement gePai, boolean fromFactory) {
		List<IGameElement> listaFilhos = gePai.getObjectList("filhoLista");
		for (IGameElement ge : listaFilhos) {
			if (ge.hasTag("testef1")) {

				if (fromFactory) {
					String prop = "valor8";
					String esperado = "1";
					verificaChildFactory(ge, prop, esperado);

					String valor = ge.getProperty("enum").replace("'", "");
					assertTrue("Enum diferente do desejado:" + valor, valor.equals("{damageType}"));

					valor = ge.getProperty("enumValue").replace("'", "");
					assertTrue("Valor do Enum diferente do desejado:" + valor, !valor.equals("true") && !valor.equals("false"));
				}
			}
			if (ge.hasTag("testef2")) {
				String prop = "valor8";
				String esperado = "2";
				verificaChildFactory(ge, prop, esperado);

				String valor = ge.getProperty("enum").replace("'", "");
				assertTrue("Enum diferente do desejado:" + valor, valor.equals("{damageType}"));

				valor = ge.getProperty("enumValue").replace("'", "");
				assertTrue("Valor do Enum diferente do desejado:" + valor, !valor.equals("true") && !valor.equals("false"));
			}
			if (ge.hasTag("testef3")) {
				String prop = "valor8";
				String esperado = "3";
				verificaChildFactory(ge, prop, esperado);
			}
		}
	}

	private void verificaChildFactory(IGameElement ge, String prop, String esperado) {
		String valor = ge.getProperty(prop);
		assertTrue(prop + " should be " + esperado + " but is " + valor, valor.equals(esperado));
	}

	@Test
	public void testaChildPropertySimples() throws IOException {

		AbstractElement gePai = em.createFinalElementFromTag("simpleschildproperty pai");
		assertNotNull("gePai nulo", gePai);
		IGameElement geFilho = gePai.getParamAsGE(Extras.LIST_OBJECT, "filho#0");
		assertNotNull("geFilho nulo", geFilho);
		verificaChild(gePai, "filho#0", "5000", "valor1");
		verificaChild(gePai, "filho#0", "555", "valor2");
		verificaChild(gePai, "filho#0", "0", "valor3");

		verificaChild(gePai, "filho#1", "5000", "valor1");
		verificaChild(gePai, "filho#1", "555", "valor2");
		verificaChild(gePai, "filho#1", "1", "valor3");

		verificaChild(gePai, "filho#2", "5000", "valor1");
		verificaChild(gePai, "filho#2", "555", "valor2");
		verificaChild(gePai, "filho#2", "2", "valor3");

		verificasChildPropertiesFilhos(gePai, false);
	}

	@Test
	public void testaChildProperty() throws IOException {

		AbstractElement gePai = em.createFinalElementFromTag("childproperty pai");
		assertNotNull("gePai nulo", gePai);
		IGameElement geFilho = gePai.getParamAsGE(Extras.LIST_OBJECT, "filho#0");

		IGameElement filhoTest = geFilho.getPropertyAsGE("filho");
		assertNotNull("filhoTest nulo", filhoTest);
		assertTrue("nome errado:" + filhoTest.getName(), filhoTest.getName().equals("filho2"));

		verificaChild(gePai, "filho#0", "0", "valor1");
		verificaChild(gePai, "filho#0", "12345", "radius");
		verificaChild(gePai, "filho#0", "0", "var");
		verificaChild(gePai, "filho#0", "0", "var2");

		verificaChild(gePai, "filho#1", "1", "valor1");
		verificaChild(gePai, "filho#1", "120", "var");
		verificaChild(gePai, "filho#1", "240", "var2");
		verificaChild(gePai, "filho#1", "120", "valor2");

		verificaChild(gePai, "filho#2", "2", "valor1");
		verificaChild(gePai, "filho#2", "240", "var");
		verificaChild(gePai, "filho#2", "480", "var2");

		// verificaChild(gePai, "filho#3", "270", "var");

		verificaChild(geFilho, "ponto#0", "123", "valorTeste");
		IGameElement gePonto = geFilho.getParamAsGE(Extras.LIST_OBJECT, "ponto#0");
		verificaChild(geFilho, "ponto#0", "12345", "radius");

	}

	private void verificaChild(IGameElement geFilho2, String tagFilho, String esperado, String prop) {
		IGameElement geFilho = geFilho2.getParamAsGE(Extras.LIST_OBJECT, tagFilho);
		assertNotNull(tagFilho + " nulo", geFilho);
		verificaChildFactory(geFilho, prop, esperado);
	}

	@Test
	public void testaResolucaoVariaveis() throws IOException {
		Blueprint itemBase = em.createBlueprint(null);
		itemBase.setParam("@property valor=10");

		String esperado = "10";
		String valor = itemBase.getParamAsText("property", "valor");
		assertTrue("itemBase.valor devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));

		verificaFuncao(itemBase, "10", "10");
		verificaFuncao(itemBase, "(10)", "($this.valor)");

	}

	private void verificaFuncao(Blueprint itemBase, String esperado, String function) {
		String valor;
		valor = itemBase.resolveFunctionOf(function);
		assertTrue(function + " devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));
	}

	@Test
	public void testaReplicateComponent() throws IOException {

		AbstractElement gePai = em.createFinalElementFromTag("replicate teste");
		assertNotNull("gePai nulo", gePai);

		ParamList list = gePai.getListWithKey(Extras.LIST_OBJECT);
		assertNotNull("list nulo", list);
		int size = list.size();

		assertTrue("size=0:" + size, size > 0);

	}

	@Test
	public void testaOneForEach() throws IOException {

		AbstractElement gePai = em.createFinalElementFromTag("oneeach pai");
		assertNotNull("gePai nulo", gePai);

		String esperado = "pai";
		String valor = gePai.getParamAsText("property", "valorPai");
		assertTrue("gePai.valorPai devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));

		// filho0
		IGameElement geFilho0 = gePai.getParamAsGE(Extras.LIST_OBJECT, "filhos#0");
		assertNotNull("filhos#0 nulo", geFilho0);
		// usando isso para validar as propriedades dos filhos
		assertTrue("Tag errada:" + geFilho0.getProperty("filhoTag"), geFilho0.getProperty("filhoTag").contains("testfilho2"));
		IGameElement filhoDoFilho = geFilho0.getPropertyAsGE("filho");
		assertNotNull(filhoDoFilho);
		assertTrue("Nome errado:" + filhoDoFilho.getName(), filhoDoFilho.getName().equals("filho2"));

		//
		esperado = "pai";
		valor = geFilho0.getParamAsText("property", "valorPai");
		assertTrue("geFilho0.valorPai devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));

		esperado = "filho";
		valor = geFilho0.getParamAsText("property", "valorFilho");
		assertTrue("geFilho1.valorFilho devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));

		// filho1

		IGameElement geFilho1 = gePai.getParamAsGE(Extras.LIST_OBJECT, "filhos#1");
		assertNotNull("filhos#1 nulo", geFilho1);

		esperado = "pai";
		valor = geFilho1.getParamAsText("property", "valorPai");
		assertTrue("geFilho1.valorPai devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));
		esperado = "filho";
		valor = geFilho1.getParamAsText("property", "valorFilho");
		assertTrue("geFilho1.valorFilho devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));
		esperado = "pai";

		IGameElement geFilho2 = gePai.getParamAsGE(Extras.LIST_OBJECT, "filhos#2");
		assertNull("filhos#2 n���o nulo", geFilho2);

	}

	// referencia propriedade da entidade... entidade pode ser o "mundo", npc,
	// player, etc.
	@Test
	public void testaReferenciaEntidade() throws IOException {

		// testando mestre
		AbstractElement geMestre = em.createFinalElementFromTag("entidade ref mestre");
		assertNotNull("geMestre nulo", geMestre);

		String esperado = "12345";
		String valor = geMestre.getParamAsText("property", "valorMestre");
		assertTrue("geMestre.valorMestre devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));

		AbstractElement entity = geMestre.getEntity();
		assertTrue("entity do mestre devia ser o proprio mestre mas is " + entity, geMestre.equals(entity));

		assertTrue("geMestre devia ser entity...", geMestre.isEntity());

		// testando filho
		String idFilho = geMestre.getParamAsText("object", "refFilho");
		IGameElement geFilho = em.getElementWithID(StringHelper.removeColchetes(idFilho));
		assertNotNull("geFilho by id nulo", geFilho);
		geFilho = em.pickOne(idFilho);
		assertNotNull("geFilho nulo", geFilho);

		entity = ((AbstractElement) geFilho).getEntity();
		assertTrue("entity do filho devia ser o mestre mas ��� " + entity, geMestre.equals(entity));

		esperado = "12345";
		valor = geFilho.getParamAsText("property", "valorTeste");
		assertTrue("geMestre.valorMestre devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));

	}

	@Test
	public void testaReferenciaMestre() throws IOException {

		IGameElement ge = em.pickOne("verificanaopronto");
		assertNull("notready retornou algo...:" + ge, ge);
	}

	@Test
	public void testaNotReady() throws IOException {

		IGameElement ge = em.pickOne("verificanaopronto");
		assertNull("notready retornou algo...:" + ge, ge);
	}

	/*
	 * Uma blueprint filha deve ter sempre a tag "leaf" quando ela n���o tiver
	 * filhos... ���til para selecionar bps v���lidos sem precisar colocar
	 * "ready" nas tags
	 */
	@Test
	public void testaLeaf() throws IOException {

		IGameElement geLeaf = em.pickOne("testaleaf");
		assertNotNull("testaleaf nulo", geLeaf);
		assertTrue("testaleaf n���o possui a tag " + Extras.TAG_LEAF, geLeaf.hasTag(Extras.TAG_LEAF));

		IGameElement geFinal = em.createFinalElement(geLeaf);
		assertFalse("testaleafFinal possui a tag " + Extras.TAG_LEAF, geFinal.hasTag(Extras.TAG_LEAF));
	}

	@Test
	public void testaDefinePai() throws IOException {

		String testeTag = "teste defineobj pai";
		IGameElement gePai = em.createFinalElementFromTag(testeTag);
		assertNotNull("test define pai", gePai);

		testeTag = "teste defineobj filho";
		IGameElement geFilho = em.createFinalElementFromTag(testeTag);
		assertNotNull("test define filho", geFilho);

		String esperado = "1100";
		String valor = geFilho.getParamAsText("property", "valorFilho");
		String pai = geFilho.getParamAsText("property", "pai");
		assertTrue("defineMestre.valorDefine+100 devia ser " + esperado + " mas ��� " + valor, valor.equals(esperado));
	}

	@Test
	public void testaRemocao() throws IOException {

		String testeTag = "teste apagar";
		String testeTagFinal = testeTag + " " + Extras.TAG_FINAL;
		AbstractElement ge = em.createFinalElementFromTag(testeTag);
		assertNotNull("test apagar nulo", ge);

		IGameElement geExiste = em.pickOne(testeTagFinal);
		assertNotNull("objeto final n���o foi encontrado...", ge);

		em.removeElementsWithTag(testeTagFinal);

		geExiste = em.pickOne(testeTagFinal);
		assertNull("objeto final foi encontrado ap���s remov���-lo...", geExiste);
	}

	@Test
	public void testBlueprint() {
		/*
		 * Log.nivelWarn(); Log.debug("debug..."); Log.warn("warn");
		 * Log.erro("erro"); Log.info("info");
		 */
		Blueprint blue = criaBlueprint1();

		assertTrue(blue.getProperty(stTileset).equals(stTilesetValue));
		// Testa AbstractElement
	}

	@Test
	public void testDefine() throws IOException {

		IGameElement ge = em.pickOne("define teste");
		String result = ge.resolveFunctionOf("$defineTeste");
		String esperado = "$defineTeste";
		assertTrue("$defineTeste retornou '" + result + "' ao inves de '" + esperado + "' antes de carregar o testDefine", esperado.equals(result));

		IGameElement geFinal = em.createFinalElement(ge);
		esperado = "[" + geFinal.id() + "]";
		result = geFinal.resolveFunctionOf("$defineThis");
		assertTrue("$defineThis retornou '" + result + "' ao inves de '" + esperado + "'", esperado.equals(result));

	}

	@Test
	public void testAbstractElement() {
		Blueprint blue = criaBlueprint1();

		for (int i = 0; i < 10; i++) {
			AbstractElement templ = em.createElement(blue);
			int x = templ.getPropertyAsInt("numberOfRooms");
			// System.out.println(templ);
			assertTrue(x >= 5 && x <= 15);

		}

	}

	private Blueprint criaBlueprint1() {
		Blueprint blue = em.createBlueprint(null);
		blue.setProperty(stTileset + " = " + stTilesetValue);
		blue.setProperty("numberOfRooms = rand(5,15)");
		return blue;
	}

	

	@Test
	public void testParamBlueprint() {
		String ident = "@property name";
		String mod = "+=";
		String val = "rand(2=3)";
		Parametro pb = new Parametro(ident + " " + mod + " " + val);
		assertTrue("Ident:'" + pb.getIdentifier() + "' devia ser:'" + ident + "'", pb.getIdentifier().equals(ident));
		assertTrue(pb.getModificador().equals(mod));
		assertTrue(pb.getValue().equals(val));
	}

	

	private Object texto(String txt) {

		return "'" + txt + "'";
	}

	@Test
	public void testHerancaGameElement() {
		ElementManager emLocal = new ElementManager();
		criaTestBlueprints(emLocal);
		IGameElement ge;
		String tag;

		tag = "blueprint weapon";
		ge = emLocal.pickOne(tag);
		assertTrue("nenhum '" + tag + "' encontrado", ge != null);

	}

	@Test
	public void testMods() {
		ElementManager emLocal = new ElementManager();
		criaTestBlueprints(emLocal);

		createTestMods(emLocal);
		IGameElement itemBase = emLocal.pickOne("blueprint weapon spear");
		Mod modPreffix = (Mod) emLocal.pickOne("weapon preffix");

		IGameElement itemPrefixo = emLocal.createFinalElement(itemBase);
		modPreffix.aplicaParametrosEm(itemPrefixo);

		Mod modSuffix = (Mod) emLocal.pickOne("weapon suffix");
		IGameElement itemAmbos = emLocal.createFinalElement(itemPrefixo);
		modSuffix.aplicaParametrosEm(itemAmbos);

		assertTrue("tag mod foi para o AbstractElement", !itemAmbos.hasTag("mod"));
		assertTrue("tag blueprint foi para o AbstractElement", !itemAmbos.hasTag("blueprint"));

	}

	

	@Test
	public void testListFunctions() {
		ElementManager emLocal = new ElementManager();
		criaTestBlueprints(emLocal);

		// Item
		Blueprint item = emLocal.createBlueprint(null);
		item.setParam("@property item=pickOne(weapon blueprint)");
		item.setParam("@property items+=list(weapon blueprint)+list(all)");
		item.setParam("@property umDeItems=pickOne(list(weapon blueprint))");
		item.setParam("@property listaReferenciando=$this.items");
		item.setParam("@property umDaListaReferenciada=pickOne($this.listaReferenciando)");

		String result = item.resolveFunctionOf("list(weapon blueprint)+list(all)");
		assertTrue("result contem ',[':" + result, !result.contains(",["));

		AbstractElement templ = (AbstractElement) emLocal.createFinalElement(item);// createAbstractElement(
		// item);
		// System.out.println(templ);

		String lista = templ.getProperty("items");
		assertTrue("lista contem ',[':" + lista, !lista.contains(",["));

		String item1 = templ.getProperty("umDeItems");
		String listaReferenciada = templ.getPropertyH("listaReferenciando", true);
		String umDaListaReferenciada = templ.getPropertyH("umDaListaReferenciada", true);
		boolean checkContains = StringHelper.checkIfListContainsItem(lista, item1);
		assertTrue("ID " + item1 + " n���o presente em " + lista, checkContains);
		assertTrue("umDaListaReferenciada vazio " + lista, umDaListaReferenciada.length() > 0);
		assertTrue("listaReferenciada vazio " + lista, listaReferenciada.length() > 0);
		assertTrue("listaReferenciada(" + listaReferenciada + ") nao cont���m umDaListaReferenciada(" + umDaListaReferenciada + ")",
				StringHelper.checkIfListContainsItem(listaReferenciada, umDaListaReferenciada));

	}

	@Test
	public void testHeranca() {
		String rand = "rand(1,400)";
		Blueprint bp = em.createBlueprint(null);
		bp.setParam("@property numRodas=" + rand);

		assertTrue("numRodas do bp devia ser " + rand, bp.getProperty("numRodas").equals(rand));
		AbstractElement templ = (AbstractElement) em.createFinalElement(bp);// em.createAbstractElement(
																// bp);
		String saida = bp.getProperty("numRodas");
		assertTrue("apos criar AbstractElement, numRodas do bp devia ser " + rand + " mas ��� " + saida, saida.equals(rand));
		assertTrue("numRodas do AbstractElement n���o deve ser " + rand, !templ.getProperty("numRodas").equals(rand));

	}

	@Test
	public void testFactory() {
		ElementManager emLocal = new ElementManager();

		criaTestBlueprints(emLocal);
		createTestMods(emLocal);

		Factory f1 = emLocal.createFactory(null);
		f1.setParam("@generator base=pickOne({blueprint weapon spear})");
		f1.setParam("@mod prefix=pickOne({mod weapon preffix})");
		f1.setParam("@mod suffix=pickOne({mod weapon suffix})");
		f1.setParam("@property value*=1.2");
		f1.setParam("@property identifier=OriginalFactory");
		String mods = f1.getParamH("@mod", "prefix", true);
		String base = f1.getParamH("@generator", "base", true);

		/*mods = f1.resolveFunctionOf(mods);

		assertTrue("mods cont���m sinal de +:" + mods, !mods.contains("+"));
*/
		assertTrue("mods vazio:" + mods, mods.contains("MOD"));
		assertTrue("base vazio:" + base, base.contains("BP"));
		// System.out.println(f1);

		AbstractElement ge = f1.fabricaItem();
		assertTrue(ge.getProperty("name"), ge.getProperty("name").contains("Gnarled"));
		assertTrue(ge.getProperty("name"), ge.getProperty("name").contains("Whooping"));
		assertTrue(ge.getIdentifier().equals("SpearWeapon"));
		//
		ge = em.createFinalElementFromTag("factory testefilho");
		// System.out.println("factory:"+ge);

		String saida = ge.getParamAsText("property", "valorFactory");
		assertTrue("valorFactory diferente de 80:" + saida, saida.equals("80"));
		saida = ge.getParamAsText("property", "valorFilho");
		assertTrue("valorFilho diferente de 50:" + saida, saida.equals("50"));
	}

	private void criaBlueprintCarro() {
		Blueprint blueprintCarro = em.createBlueprint(null);
		blueprintCarro.addTag("carro");
		blueprintCarro.setName("carro");
		blueprintCarro.setParam("@property name='carro'");
		blueprintCarro.setParam("@object chassi=pickOne(blueprint chassi)");
		blueprintCarro.setParam("@position chassi=point(0.5,0.5,0.5)");
		blueprintCarro.setParam("@orientation chassi=rotation(0)");
		// blueprintCarro.addParam("@size chassi=point(1,1,1)");

		Blueprint chassi = em.createBlueprint(null);
		chassi.addTag("chassi");
		chassi.setDomain("type+={chassi}");
		chassi.setParam("@property model='chassi1.obj'");
		chassi.setParam("@property name='chassi'");
		chassi.setParam("@property numRodas=4");
		chassi.setParam("@replicate roda=$this.numRodas");
		chassi.setParam("@object roda=pickOne(blueprint roda)");
		chassi.setParam("@orientation roda=0");// orienta������o da roda em si:
												// 0=virado pra frente

	
		chassi.setParam("@orientation roda.angleVariation = if($index<$this.numRodas/2,30,0)");

		// angulo inicial da roda: 0=frente, position porque isso muda
		// a posi������o do objeto usando um angulo como base
		chassi.setParam("@position roda=$angle-45");
		chassi.setParam("@position roda.radius=1");

		// testando limite de angulo
		chassi.setParam("@property farolMaxAngle=90");
		chassi.setParam("@object farol=pickOne(blueprint roda)");
		chassi.setParam("@replicate farol=2");
		chassi.setParam("@position farol=45");
		chassi.setParam("@position farol.maxAngle=$this.farolMaxAngle");
		chassi.setParam("@position farol=$angle-$this.farolMaxAngle/2");

		Blueprint roda = em.createBlueprint(null);
		roda.addTag("roda");
		roda.setDomain("type+={roda}");
		roda.setParam("@property model='roda1.obj'");
		roda.setParam("@property name='roda'");
	}

	@Test
	public void testFinalElement() {
		ElementManager emLocal = new ElementManager();
		criaTestBlueprints(emLocal);

		IGameElement base = emLocal.pickOne("blueprint item !weapon");
		assertTrue("base nula", base != null);
		AbstractElement ge = emLocal.createFinalElement(base);

		assertTrue("elemento final nulo", ge != null);
		assertTrue("nome devia ser " + nameItem + " mas ��� " + ge.getName(), ge.getName().equals(nameItem));

		// System.out.println("item final:"+ge);
	}

	@Test
	public void testDomainNotacaoErrada() throws IOException {

		AbstractElement ge = em.createFinalElementFromTag("acessoFilho teste");
		String domain = ge.getParamAsText("domain", "type");
		assertTrue("Domain com + no nome: " + domain, !domain.contains("+"));

	}

	@Test
	public void testBlueprintDomain() {
		String nameItem = "Some Item";
		String nameWeapon = "";

		// Item
		Blueprint item = em.createBlueprint(null);
		item.setParam("@domain type={item}");
		item.setParam("@property name='" + nameItem + "'");
		assertTrue("Doesnt contains domain item:" + item, item.createdItemContainsDomain("item"));
		item.addTag("item");

		// Weapon
		Blueprint weapon = em.createBlueprint(item);
		weapon.setParam("@domain type+={weapon}");
		weapon.addTag("weapon");

		assertTrue("Doesnt contains domain weapon", weapon.createdItemContainsDomain("weapon"));

		IGameElement ge = em.createFinalElement(weapon);
		assertTrue("Doesnt contains tag weapon:" + ge.getTagsAsText(), ge.hasTag("weapon"));
		assertTrue("Doesnt contains tag item:" + ge.getTagsAsText(), ge.hasTag("item"));

		ge = em.pickOne("{all item}");
		assertTrue("{all item} retornou null", ge != null);

		// System.out.println("ge:"+ge);
		// System.out.println("weapon:"+em.pickOne("{all weapon}"));

		ge = em.pickOne("{all item !weapon}");
		assertTrue("{all item !weapon} retornou null", ge != null);

	}

	@Test
	public void testLoadDefine() throws IOException {

		IGameElement ge = em.pickOne("all");
		String result = ge.resolveFunctionOf("$testVar");
		String esperado = "\"OK\"";
		assertTrue("$testVar retornou '" + result + "' ao inv���s de '" + esperado + "'", esperado.equals(result));

	}

	@Test
	public void testaSoma() throws IOException {

		AbstractElement ge = em.createFinalElementFromTag("teste2");
		String valor = ge.resolveFunctionOf("sum(teste blueprint soma,valorSoma)");
		String esperado = "4.5";
		assertTrue("sum retornou '" + valor + "' ao inves de '" + esperado + "'", esperado.equals(valor));

		valor = ge.getProperty("total");
		assertTrue("sum retornou '" + valor + "' ao inv���s de '" + esperado + "'", esperado.equals(valor));

		valor = ge.getParamAsText("size", "filhos#0");
		// System.out.println(ge);
		assertTrue("size do filho#0 retornou '" + valor + "' ao inv���s de 100", valor.equals("100"));
		valor = ge.getParamAsText("size", "filhos#1");
		assertTrue("size do filho#1 retornou '" + valor + "' ao inv���s de 100", valor.equals("100"));

		IGameElement geFilho = ge.getParamAsGE("object", "filhos#0");
		assertTrue("filho#0 nulo", geFilho != null);
		geFilho = ge.getParamAsGE("object", "filhos#1");
		assertTrue("filho#1 nulo", geFilho != null);

		esperado = "100";
		valor = geFilho.getParamAsText("property", "valorFilho");
		assertTrue("valorFilho do filho#1 errado:" + valor, valor.equals(esperado));

		// testando child property
		geFilho = ge.getParamAsGE("object", "outroFilho#0");
		assertTrue("outroFilho#0 nulo", geFilho != null);
		valor = geFilho.getProperty("childprop");
		assertTrue("childprop valor errado:" + valor, valor.equals("100"));
		valor = geFilho.getProperty("childprop2");
		assertTrue("childprop2 valor errado:" + valor, valor.equals("120"));
	}

	@Test
	public void testCreatedBy() throws IOException {

		AbstractElement ge = em.createFinalElementFromTag("teste2");
		// System.out.println("ge:"+ge);
		IGameElement geFilho = ge.getParamAsGE("object", "filhos#0");
		// System.out.println("geFilho:"+geFilho);

		assertTrue("CreatedBy definido no pai (n���o deve estar)", !ge.hasProperty(Extras.PROPERTY_CREATED_BY));
		assertTrue("CreatedBy n���o definido", geFilho.hasProperty(Extras.PROPERTY_CREATED_BY));

		String saida = geFilho.resolveFunctionOf("$owner.fixo");
		assertTrue("$owner.fixo retornou:" + saida, saida.equals("120"));
		saida = geFilho.getProperty("fixoPai");
		assertTrue("$owner.fixo retornou:" + saida, saida.equals("120"));

	}

	@Test
	public void testImutabilidade() {
		String funcao = "rand(10,50)";
		Blueprint item = em.createBlueprint(null);
		item.setParam("@property function=" + funcao);

		for (int i = 0; i < 50; i++) {
			String saida = item.getParamH("property", "function", true);
			int valor = Integer.parseInt(saida);
			assertTrue("Valor fora do Range:" + valor, (valor >= 10) && (valor <= 50));
		}

		String param = item.getProperty("function");
		assertTrue("Valor do blueprint foi alterado:" + param, param.equals(funcao));

		item.setParam("@property functionRef=$this.function");
		String saidaRef = item.getParamH("property", "functionRef", true);

		// System.out.println("Blueprint final:"+item);

		param = item.getProperty("function");
		assertTrue("Valor do blueprint foi alterado:" + param, param.equals(funcao));

	}

	@Test
	public void testElementManagerVars() {

		String resultado = em.getVar("variavel");
		assertTrue("variavel inexistente nao retornou vazio:" + resultado, resultado.equals(""));
		String valor = "valor qualquer";

		// criando variavel e comparando
		em.setVar("variavel", valor);
		resultado = em.getVar("variavel");
		assertTrue("variavel inexistente nao retornou '" + valor + "':" + resultado, resultado.equals(valor));

		// apagando variavel
		em.setVar("variavel", null);
		resultado = em.getVar("variavel");
		assertTrue("variavel zerada nao retornou null:" + resultado, resultado.equals(""));
	}

	@Test
	public void testHierarquiaSimples() {
		Blueprint item = em.createBlueprint(null);
		item.setParam("@property prop='teste'");

		Blueprint itemFilho = em.createBlueprint(item);
		itemFilho.setParam("@property prop+=' inicio '");

		AbstractElement po = em.createElement(itemFilho);
		// System.out.println("po:"+item);
	}

	@Test
	public void testThis() throws IOException {
		Blueprint itemBase = em.createBlueprint(null);
		itemBase.setParam("@property valor=10");
		itemBase.setParam("@lista valor=15");

		String saida = itemBase.resolveFunctionOf("$this.valor");
		String resultado = "10";
		assertTrue("$this.valor saida devia ser " + resultado + " mas ��� " + saida, saida.equals(resultado));

		/*
		 * saida = itemBase.resolveFunctionOf("$this.lista.valor"); resultado =
		 * "15"; assertTrue("$this.lista.valor devia ser " + resultado +
		 * " mas ��� " + saida, saida.equals(resultado));
		 * 
		 * saida = itemBase.resolveFunctionOf("$this.property.valor"); resultado
		 * = "10"; assertTrue("$this.property.valor devia ser " + resultado +
		 * " mas ��� " + saida, saida.equals(resultado));
		 */

	}

	@Test
	public void testDefinicaoNiveis() throws IOException {

		// Blueprint 1o nivel
		Blueprint itemBase = em.createBlueprint(null);
		itemBase.setParam("@property valor=10");
		String fRand = "rand(10,20)";
		itemBase.setParam("@property funcao=" + fRand);
		String fCalc = "10+5";
		itemBase.setParam("@listqualquer valor=" + fCalc);
		itemBase.setParam("@property objetoBase=pickOne(teste)");
		itemBase.setParam("@object objeto=$this.objetoBase");
		itemBase.setParam("@replicate objeto=2");

		String resultado;

		// System.out.println("itemBase:"+itemBase);

		resultado = itemBase.getProperty("funcao");
		assertTrue("funcao deve ser " + fRand + " mas ��� " + resultado, resultado.equals(fRand));
		resultado = itemBase.getProperty("valor");
		assertTrue("valor deve ser 10 mas ��� " + resultado, resultado.equals("10"));
		resultado = itemBase.getParamAsText("listqualquer", "valor");
		assertTrue("valor deve ser " + fCalc + " mas ��� " + resultado, resultado.equals(fCalc));

		// Blueprint 2o nivel
		Blueprint itemFilho = em.createBlueprint(itemBase);
		itemFilho.setParam("@property valor+=10");
		String funcaoDesc = "rand(10,$var1)";
		itemFilho.setParam("@property funcaoDesc=" + funcaoDesc);
		// System.out.println("itemFilho:"+itemFilho);

		resultado = itemFilho.getProperty("valor");
		assertTrue("valor deve ser 10 mas ��� " + resultado, resultado.equals("10"));
		resultado = itemFilho.getPropertyH("valor", false);
		assertTrue("valor deve ser 10+10 mas ��� " + resultado, resultado.equals("10+10"));
		resultado = itemFilho.getPropertyH("valor", true);
		assertTrue("valor deve ser 20 mas ��� " + resultado, resultado.equals("20"));
		resultado = itemFilho.getProperty("funcaoDesc");
		assertTrue("valor deve ser " + funcaoDesc + " mas ��� " + resultado, resultado.equals(funcaoDesc));

		// AbstractElement
		AbstractElement templ = em.createFinalElement(itemFilho);
		// System.out.println("Templ:"+templ);

		resultado = templ.getProperty("valor");
		assertTrue("value should be 20 but is " + resultado, resultado.equals("20"));
		int r = templ.getPropertyAsInt("funcao");
		assertTrue("funcao deve ser um int entre 10 e 20 mas ��� " + r, r >= 10 && r <= 20);
		resultado = templ.getProperty("funcaoDesc");
		assertTrue("valor deve ser " + funcaoDesc + " mas ��� " + resultado, resultado.equals(funcaoDesc));

		// Final
		em.setVar("var1", "123");
		AbstractElement objFinal = (AbstractElement) em.createFinalElement(templ);
		// System.out.println("final:"+objFinal);
	}

	/*
	 * @Test public void testAllBlueprints() throws IOException {
	 * List<AbstractElement> elements = em.getElementsWithTag("all blueprint");
	 * for (AbstractElement element : elements) { try {
	 * 
	 * AbstractElement finalElement=em.createFinalElement(element);
	 * assertTrue("finalElement = null:"+element,finalElement!=null); } catch
	 * (Exception e) { fail("Excecao "+e.getMessage()+" no elemento"+element);
	 * e.printStackTrace(); } } }
	 */

	@Test
	public void testValidate() throws IOException {

		AbstractElement ge = em.createFinalElementFromTag("testevalidate");
		assertTrue("deveria ser valido: " + ge, ge.isValid());
	}

}
