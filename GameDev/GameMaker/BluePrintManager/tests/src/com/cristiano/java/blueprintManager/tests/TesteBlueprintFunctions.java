package com.cristiano.java.blueprintManager.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.consts.Extras;
import com.cristiano.consts.GameProperties;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.bpM.entidade.blueprint.Blueprint;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.extras.FunctionConsts;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.Log;

public class TesteBlueprintFunctions  extends AbstractBlueprintTest{
	static ElementManager em = null;


	@BeforeClass
	public static void tearUp() throws IOException {
		em = new ElementManager();
		em.loadTestBlueprintsFromFile();
	}
	
	
	
	@Test
	public void testValidaMeshFunction() {
		criaTestBlueprints(em);
		IGameElement ge = em.pickAnyOne("item");
		ge.setProperty("width","100");
		ge.setProperty("height","100");
		ge.setProperty("qtdWheels","4");
		ge.setProperty("width","100");
		ge.setProperty("wheelY","-0.5");
		ge.setProperty("wheelWidth","10");
		ge.setProperty("wheelRadius","10");
		ge.setProperty("mesh","10");
		ge.setVar("index", "1");
		
		String function="addMesh({mesh primitive},sizeZ=randf(0.1,0.4),merge=1,sizeRadius=$this.width*randf(0.05,0.2),posY=randf(-0.5,0.5))";
		String solved = ge.resolveFunctionOf(function,ge.getParametro(Extras.LIST_PROPERTY, "mesh"));
		assertFalse(solved.contains("{sizeZ"));
		
		//function=function.replace("#", "$");
		function="mod(#index,2)<1,0.5,-0.5";
		solved = ge.resolveFunctionOf(function);
		assertTrue(solved.equals(function));
		function="posZ=if(mod(#index,$this.qtdWheels/2)<$this.qtdWheels/4,0.5,-0.5)";
		solved = ge.resolveFunctionOf(function);
		assertTrue(solved.equals("posZ=if(mod(#index,2)<4/4,0.5,-0.5)"));
		function="{mesh primitive},maxAngle=180,isFrontWheel=if(mod(#index,2)<1,1,0),mirrorX=mod(#index,2),posX=if(isTopQuadrant(#index)==1,1,-1),posZ=if(-0.5),posY=-0.5,rotY=90,merge=1,replicate=4,sizeX=10*2,sizeY=10*2,sizeZ=10,sizeRadius=10*100";
		
		function="addMesh({mesh primitive},maxAngle=180,isFrontWheel=if(mod(#index,$this.qtdWheels/2)<$this.qtdWheels/4,1,0),mirrorX=mod(#index,2),posX=if(isTopQuadrant(#index)==1,1,-1),posZ=if(mod(#index,$this.qtdWheels/2)<$this.qtdWheels/4,0.5,-0.5),posY=$this.wheelY,rotY=90,merge=1,replicate=$this.qtdWheels,sizeX=$this.wheelRadius*2,sizeY=$this.wheelRadius*2,sizeZ=$this.wheelWidth,sizeRadius=$this.wheelRadius*$this.height)";
		//function=function.replace("#", "$");
		solved = ge.resolveFunctionOf(function);
		assertFalse(solved.contains("{sizeZ"));
	}

	@Test
	public void testAddMeshFunction() {
		int width=100;
		int height=20;
		int depth=30;
		Blueprint itemBase = em.createBlueprint(null);
		itemBase.setParam("@mesh mesh1=addMesh({mesh primitive box leaf},final=0,var1=#index*10,var2=if(#index==0,'a','b'),posX=1,posY=0,posZ=-0.5,merge=1,orientX=0,orientZ=0,orientY=#angle,replicate=2,minAngle=0,maxAngle=180,mirrorX=mod(#index,2),material={glass})");
		itemBase.setParam("@mesh mesh2=addMesh({mesh primitive box leaf},final=1,var1=#index*10,var2=if(#index==0,'a','b'),posX=1,posY=0,posZ=-0.5,merge=1,orientX=0,orientZ=0,orientX=#angle,replicate=2,minAngle=0,maxAngle=180,mirrorX=mod(#index,2),material={glass})");
		itemBase.setParam("@property width=" + width);
		itemBase.setParam("@property identifier=TestAddMesh");
		itemBase.setParam("@property height=" + height);
		itemBase.setParam("@property depth=" + depth);
		
		AbstractElement finalMesh = em.createFinalElement(itemBase);
		assertNotNull(finalMesh);
		String pointList=finalMesh.getProperty(Extras.PROPERTY_POINTLIST);
		assertTrue(pointList.contains("mesh1"));
		assertTrue(pointList.contains("mesh2"));
		validaMesh(width, depth, "mesh2", finalMesh);
		validaMesh(width, depth, "mesh1", finalMesh);
		
	}

	private void validaMesh(int width, int depth, String meshName, AbstractElement finalMesh) {
		List<IGameElement> subMeshs = finalMesh.getObjectList(meshName);
		assertTrue("No submeshes were generated",subMeshs.size()>0);
		
		
		
		float posX = finalMesh.getParamAsFloat(Extras.LIST_POSITION,meshName+"#0.x");
		float posY = finalMesh.getParamAsFloat(Extras.LIST_POSITION,meshName+"#0.y");
		float posZ = finalMesh.getParamAsFloat(Extras.LIST_POSITION,meshName+"#0.z");
		assertTrue("posX not what expected:"+posX,posX==width/2);
		assertTrue("posY not what expected:"+posY,posY==0);
		assertTrue("posZ not what expected:"+posZ,posZ==-depth*0.5f/2);
		
		IGameElement obj0=finalMesh.getParamAsGE("object",meshName+"#0");
		IGameElement obj1=finalMesh.getParamAsGE("object",meshName+"#1");
		assertTrue("Objects should not be the same: "+obj0.id()+"=="+obj1.id(),obj0.id()!=obj1.id());
		
		//Valida Material
		//obj0
		IGameElement geMat = obj0.getPropertyAsGE(GameProperties.MATERIAL_TYPE);
		assertNotNull("Material is null",geMat);
		assertTrue("Wrong material: "+geMat.getName(),geMat.getName().equals("Glass"));
		
		//obj1
		geMat = obj1.getPropertyAsGE(GameProperties.MATERIAL_TYPE);
		assertNotNull("Material is null",geMat);
		assertTrue("Wrong material: "+geMat.getName(),geMat.getName().equals("Glass"));
		
		String var1=obj0.getProperty("var1");
		String var2=obj0.getProperty("var2");
		assertFalse("index not solved on direct param (var1):"+var1,var1.contains("index"));
		assertFalse("index not solved on direct param (var2):"+var2,var2.contains("index"));
		assertTrue(var2.equals("a"));
		assertTrue(var1.equals("0"));
		
		
		var1=obj1.getProperty("var1");
		var2=obj1.getProperty("var2");
		assertTrue(var1.equals("10"));
		assertTrue(var2.equals("b"));
		
		checkParam(Extras.LIST_NODETYPE,meshName+"#0","merge",finalMesh);
		checkParam(Extras.LIST_NODETYPE,meshName+"#1","merge",finalMesh);
	}

	private void checkParam(String list, String param,String expected,AbstractElement finalMesh) {
		String value=finalMesh.getParamAsText(list, param);
		assertTrue(param+" is wrong:"+value,value.equals(expected));
	}
	
	@Test
	public void testFunctionDimensions() {
		AbstractElement gePai = (AbstractElement) em.pickFinal("dimensionFuncPai teste");
		String valDireto = gePai.resolveFunctionOf("pickFinal({acessoFilho},dimensions($this))");
		IGameElement elementWithID = em.getElementWithID(valDireto);
		IGameElement geFilho = gePai.getPropertyAsGE("obj");
		assertNotNull("filho nulo", geFilho);
		String valWidthF = geFilho.getProperty("width");
		String valWidthID = elementWithID.getProperty("width");
		String valWidthP = gePai.getProperty("width");
		assertTrue("valWidthID devia ser igual ao valWidthP:" + valWidthID, valWidthID.equals(valWidthP));
		assertTrue("valWidthP devia ser igual ao valWidthF:" + valWidthF, valWidthF.equals(valWidthP));
	}

	@Test
	public void testFunctionPickFromList() {
		AbstractElement gePai = (AbstractElement) em.pickFinal("testPickFromList teste");
		String function = "pickFromList({Key1 Key2 Key3},{palavraChave},1)";
		String valDireto = gePai.resolveFunctionOf(function);
		assertFalse("function unresolved: " + valDireto, function.equals(valDireto));

		List<IGameElement> pick1 = gePai.getPropertyAsGEList("pick1");
		assertTrue(pick1.size() == 1);

		List<IGameElement> pick2 = gePai.getPropertyAsGEList("pick2");
		assertTrue(pick2.size() == 2);
		IGameElement ge0 = pick2.get(0);
		IGameElement ge1 = pick2.get(1);
		assertFalse(ge0.getName().equals(ge1.getName()));

		List<IGameElement> pick3 = gePai.getPropertyAsGEList("pick3");
		assertTrue(pick3.size() == 3);
		ge0 = pick3.get(0);
		ge1 = pick3.get(1);
		IGameElement ge2 = pick3.get(2);

		assertTrue(ge0.getName().contains("key"));
		assertTrue(ge1.getName().contains("key"));
		assertTrue(ge2.getName().contains("key"));
		assertFalse(ge0.getName().equals(ge1.getName()));
		assertFalse(ge0.getName().equals(ge2.getName()));
		assertFalse(ge2.getName().equals(ge1.getName()));

		List<IGameElement> pick10 = gePai.getPropertyAsGEList("pick10");
		assertTrue(pick10.size() == 3);
		List<IGameElement> pick0 = gePai.getPropertyAsGEList("pick0");
		assertTrue(pick0.size() == 1);
	}
	
	@Test
	public void testFunctionSolving() throws IOException {

		String[] inputs = new String[39];
		String[] outputs = new String[39];
		inputs[0] = "+1";
		inputs[1] = "1+2";
		inputs[2] = "(1)+2";
		inputs[3] = "2*3";
		inputs[4] = "2+1-3";
		inputs[5] = " 12 + 3";
		inputs[6] = "2*3-1";
		inputs[7] = "6/2";
		inputs[8] = "15*2/(2+1)";
		inputs[9] = "15*2/2+1";
		inputs[10] = "(15*(2+1-1))/2+1";
		inputs[11] = "1/0";
		inputs[12] = "+1";
		inputs[13] = "-1";
		inputs[14] = "1/2";
		inputs[15] = "if(5>3,2,3)";
		inputs[16] = "if(2+2>3,1,0)";
		inputs[17] = "mod(1,2)";
		inputs[18] = "mod(2,2)";
		inputs[19] = "mod(10,2)";
		inputs[20] = "mod(21,2)";
		inputs[21] = "mod(11,3)";
		inputs[22] = "mod(10,3)";
		inputs[23] = "mod(21,3)";
		inputs[24] = "if(mod(10,2)=0,'par','impar')";
		inputs[25] = "if(mod(10,2)!=0,'impar','par')";
		outputs[25] = "'par'";
		inputs[26] = "abs(21)";
		inputs[27] = "abs(-20 - 1)";
		inputs[28] = "absf(21 + 0.5)";
		inputs[29] = "absf(-21 - 0.5)";
		inputs[30] = "absf(21.0)";
		inputs[31] = "absf(-21.0)";

		inputs[32] = "-5+1";
		outputs[32] = "-4";
		inputs[33] = "-5-1";
		outputs[33] = "-6";
		inputs[34] = "-5-1+1";
		outputs[34] = "-5";
		inputs[34] = "-5-1+1";
		outputs[34] = "-5";
		inputs[35] = "5-1+1";
		outputs[35] = "5";
		inputs[36] = "-5 * 2";
		outputs[36] = "-10";
		inputs[37] = "-5 * 2 +1";
		outputs[37] = "-9";
		inputs[38] = "(5*2)+30";
		outputs[38] = "40";

		outputs[0] = "1";
		outputs[1] = "3";
		outputs[2] = "3";
		outputs[3] = "6";
		outputs[4] = "0";
		outputs[5] = "15";
		outputs[6] = "5";
		outputs[7] = "3";
		outputs[8] = "10";
		outputs[9] = "16";
		outputs[10] = "16";
		outputs[11] = "0";
		outputs[12] = "1";
		outputs[13] = "-1";
		outputs[14] = "0.5";
		outputs[15] = "2";
		outputs[16] = "1";
		outputs[17] = "1";
		outputs[18] = "0";
		outputs[19] = "0";
		outputs[20] = "1";
		outputs[21] = "2";
		outputs[22] = "1";
		outputs[23] = "0";
		outputs[24] = "'par'";

		outputs[26] = "21";
		outputs[27] = "21";
		outputs[28] = "21.5";
		outputs[29] = "21.5";
		outputs[30] = "21";
		outputs[31] = "21";

		GenericElement ge = new GenericElement(em);
		ge.setProperty("numRodas=4");
		String saida = ge.resolveFunctionOf("$this.numRodas/2");
		assertTrue("Divis���o de uma property vindo com valor errado:" + saida + " devia ser 2", saida.equals("2"));

		saida = ge.resolveFunctionOf("if(2+2>3,1,0)");
		for (int i = 0; i < inputs.length; i++) {
			try {

				saida = ge.resolveFunctionOf(inputs[i]);
				// System.out.println(inputs[i]+" devia ser "+outputs[i]+
				// " retornou:"+saida);
				assertTrue("i:" + i + " " + inputs[i] + " devia ser " + outputs[i] + " mas retornou:" + saida, saida.equals(outputs[i]));
			} catch (Exception e) {
				Log.error("Erro ao executar fun������o:" + inputs[i] + " saida:" + saida);
				e.printStackTrace();
			}
		}

		saida = ge.resolveFunctionOf("rand(10,20) / rand(30,40)");
		assertTrue(saida != "");

		for (int i = 0; i < 100; i++) {
			saida = ge.resolveFunctionOf("randf(0.000001,0.000005)");
			if (saida.contains("E")) {
				System.out.println("saida com erro:" + saida);
			}
			float f = Float.parseFloat(saida);
			assertTrue("randf n���o retornou um n���mero entre -1 e 1:" + f, (f >= -1) && (f <= 1));

			saida = ge.resolveFunctionOf("randf(-1,1)");
			if (saida.contains("E")) {
				System.out.println("saida com erro:" + saida);
			}
			f = Float.parseFloat(saida);
			assertTrue("randf didnt return a number between -1 and 1:" + f, (f >= -1) && (f <= 1));
		}

		String result = ge.resolveFunctionOf("count(blueprint test)");
		int c = Integer.parseInt(result);
		// System.out.println("total:"+c);
		assertTrue("c<=0: " + c, c > 0);

		// testando rand com numero
		for (int mult = 1; mult <= 5; mult++) {
			for (int i = 0; i < 20; i++) {
				int min = 1;
				int max = 100;
				saida = ge.resolveFunctionOf("rand(" + min + "," + max + "," + mult + ")");
				int f = Integer.parseInt(saida);
				// System.out.println(mult+"=>"+f);
				assertTrue("rand n���o retornou numero multiplo de " + mult + ":" + f, f % mult == 0);
				assertTrue("rand retornou numero menor que min:" + f, f >= min);
				assertTrue("rand retornou numero maior que max:" + f, f <= max);
			}
		}

		// testando getElementosFuncao
		String functionName = "function";
		String par1 = "123";
		String par2 = "'par1'";
		String par3 = "(a+b)";
		String[] ef = StringHelper.getElementosFuncao(functionName + "(" + par1 + "," + par2 + "," + par3 + ")");
		assertTrue("functionname deveria ser " + functionName + " mas ��� " + ef[0], ef[0].equals(functionName));
		assertTrue("par1 deveria ser " + par1 + " mas ��� " + ef[1], ef[1].equals(par1));
		assertTrue("par2 deveria ser " + par2 + " mas ��� " + ef[2], ef[2].equals(par2));
		assertTrue("par3 deveria ser " + par3 + " mas ��� " + ef[3], ef[3].equals(par3));

	}

	
	@Test
	public void testUserFunction() throws IOException {

		AbstractElement ge = em.createFinalElementFromTag("testuserfunction");
		// random choice...
		String resultado = ge.resolveFunctionOf("rand(1,2)");
		assertTrue("RandomChoice deu erro:" + resultado, resultado.equals("1") || resultado.equals("2"));

		// demais funcoes:
		verificaFunction(ge, "-0.1059993+0.4230841","0.31708482");
		verificaFunction(ge, "if(-2>-3,-2,-3)", "-2");
		verificaFunction(ge, "if(-2>-3,-2,-3)", "-2");
		verificaFunction(ge, "maior(-2,-3)", "-2");
		verificaFunction(ge, "if(-1.1>1.1,-1.1,1.1)", "1.1");
		verificaFunction(ge, "isOdd(10)", "1");
		verificaFunction(ge, "isOdd(9)", "0");
		verificaFunction(ge, "isOdd(100)", "1");
		verificaFunction(ge, "isOdd(99)", "0");
		verificaFunction(ge, "isOdd(3*2)", "1");
		verificaFunction(ge, "hexColor(pickFinal({white color}))", "'#FFFFFF'");
		verificaFunction(ge, "hexColor(pickFinal({black color}))", "'#000000'");
		verificaFunction(ge, "'/a'+'/b'", "'/a/b'");
		verificaFunction(ge, "\"aa\"+\"bb\"", "'aabb'");
		verificaFunction(ge, "'aa'+'bb'", "'aabb'");
		verificaFunction(ge, "' aa '+' bb'", "' aa  bb'");
		verificaFunction(ge, "delta(5,-1)", "6");
		verificaFunction(ge, "delta(1,2)", "1");
		verificaFunction(ge, "delta(1,5)", "4");
		verificaFunction(ge, "delta(5,1)", "4");
		verificaFunction(ge, "delta(-1,5)", "6");
		verificaFunction(ge, "igual(1,2)", "0");
		verificaFunction(ge, "igual(1,1)", "1");
		verificaFunction(ge, "igual(2,2)", "1");
		verificaFunction(ge, "testValor1($this)", "2");
		verificaFunction(ge, "maior(15,10,25)", "25");
		verificaFunction(ge, "menor(15,10,25)", "10");
		verificaFunction(ge, "soma(1,2)", "3");
		verificaFunction(ge, "soma(0,2)", "2");
		verificaFunction(ge, "soma(1,2,3)", "6");
		verificaFunction(ge, "soma(1,2,3,4)", "10");
		verificaFunction(ge, "soma(1,2,3,4,5)", "15");
		verificaFunction(ge, "soma($this.valor1,$this.valor2)", "6");
		verificaFunction(ge, "soma($this.valor1,$this.valor2,$this.valor3)", "11");
		verificaFunction(ge, "soma(soma(1,2),2)", "5");
		verificaFunction(ge, "soma(soma(soma(2,3),2),2)", "9");
		verificaFunction(ge, "soma(soma(soma(2,3*3),2),2)", "15");
		verificaFunction(ge, "mult(5,10)", "50");
		verificaFunction(ge, "testFunction(5,10)", "25");
		verificaFunction(ge, "maior(1,2)", "2");
		verificaFunction(ge, "maior(5,10)", "10");
		verificaFunction(ge, "maior(15,10)", "15");
		verificaFunction(ge, "isEmpty('')", "1");
		verificaFunction(ge, "isEmpty({})", "1");
		verificaFunction(ge, "isEmpty([])", "1");
		verificaFunction(ge, "isEmpty(abc)", "0");
		verificaFunction(ge, "isEmpty('abc')", "0");
		
		verificaFunction(ge, "if(mod(10,2)==0,1,0)", "1");
		verificaFunction(ge, "if(mod(10,2)!=1,1,0)", "1");
		
		ge.setVar("index",1);
		ge.setProperty("qtdWheels",4);
		verificaFunction(ge, "if(mod(1,4/2)<4/4,0.5,-0.5)", "-0.5");
		verificaFunction(ge, "if(mod($index,$this.qtdWheels/2)<$this.qtdWheels/4,0.5,-0.5)", "-0.5");
		
		//trigonometria
		verificaFunction(ge, "sind(90)", "1");
		verificaFunction(ge, "sind(0)", "0");
		verificaFunction(ge, "sinr(0)", "0");
		verificaFunction(ge, "sinr(1)", "0.8414709",true);
		verificaFunction(ge, "cosd(90)", "0");
		verificaFunction(ge, "cosd(0)", "1");
		verificaFunction(ge, "cosr(0)", "1");
		verificaFunction(ge, "cosr(1)", "0.5403023",true);
		verificaFunction(ge, "tand(0)", "0");
		verificaFunction(ge, "tand(45)", "1");
		verificaFunction(ge, "tanr(0)", "0");
		verificaFunction(ge, "tanr(1)", "1.5574077",true);
		
	}

	
	@Test
	public void testUserFunctionMultiLevel() {
		IGameElement ge = em.pickOne("testValues leaf");
		String valToCheck = "pickFinal([" + ge.id() + "],val2=2,val3=3)";
		String valErroDireto = ge.resolveFunctionOf(valToCheck);
		assertTrue(valErroDireto.contains("TEMPL"));
		IGameElement gepickTest = em.getElementWithID(valErroDireto);
		assertTrue(gepickTest.getProperty("val2").equals("2"));
		assertTrue(gepickTest.getProperty("val3").equals("3"));
		List<IGameElement> params = gepickTest.getObjectList("params");
		assertTrue(params.size() > 0);
		IGameElement geParam = params.get(0);
		assertTrue(geParam.getProperty("val2").equals("2"));
		assertTrue(geParam.getProperty("val3").equals("3"));

		String valErroFunc = ge.resolveFunctionOf("pickFinal(pickTestElement(),val2=2,val3=3)");
		assertTrue("valErroFunc:" + valErroFunc, valErroFunc.contains("TEMPL"));
		String pickTest = ge.resolveFunctionOf("pickTestElement()");
		assertTrue(pickTest.contains("TEMPL"));
		gepickTest = em.getElementWithID(pickTest);
		assertNotNull(gepickTest);
		assertTrue(gepickTest.getProperty("val1").equals("1"));
		String testM = ge.resolveFunctionOf("testMultiplo(3)");
		assertTrue(testM.contains("TEMPL"));
		IGameElement geTestM = em.getElementWithID(testM);
		assertTrue(geTestM.getProperty("val1").equals("1"));
		assertTrue(geTestM.getProperty("val2").equals("2"));
		assertTrue(geTestM.getProperty("val3").equals("3"));

		params = geTestM.getObjectList("params");
		assertTrue(params.size() > 0);
		geParam = params.get(0);
		assertTrue(geParam.getProperty("val1").equals("1"));
		assertTrue(geParam.getProperty("val2").equals("2"));
		assertTrue(geParam.getProperty("val3").equals("3"));

	}

	@Test
	public void testSimplexNoise() {
		AbstractElement ge = (AbstractElement) em.pickFinal("test leaf");
		String funct = FunctionConsts.FUNCTION_SIMPLEX_2D + "(123,10,10,10,0,1)";
		String val = ge.resolveFunctionOf(funct);
		assertFalse(val.equals(funct));
		float v = Float.parseFloat(val);
		assertTrue(v >= -1 && v <= 1);

		String val2 = ge.resolveFunctionOf(funct);
		assertTrue(val.equals(val2));
	}

	@Test
	public void testRandomTag() {
		AbstractElement ge = (AbstractElement) em.pickFinal("testuserfunction leaf");
		int ca = 0;
		int cb = 0;
		int cc = 0;
		int cd = 0;
		int ce = 0;
		for (int i = 0; i < 1000; i++) {
			String val = ge.resolveFunctionOf("randomTag({a b c d e})");
			assertTrue(val.equals("a") || val.equals("b") || val.equals("c") || val.equals("d") || val.equals("e"));
			if (val.equals("a")) {
				ca++;
			}
			if (val.equals("b")) {
				cb++;
			}
			if (val.equals("c")) {
				cc++;
			}
			if (val.equals("d")) {
				cd++;
			}
			if (val.equals("e")) {
				ce++;
			}
		}
		assertTrue("nenhum a", ca > 0);
		assertTrue("nenhum b", cb > 0);
		assertTrue("nenhum c", cc > 0);
		assertTrue("nenhum d", cd > 0);
		assertTrue("nenhum e", ce > 0);

	}
}
