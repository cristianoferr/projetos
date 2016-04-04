package com.cristiano.java.jme.tests;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.blueprint.Blueprint;
import com.cristiano.java.gm.builder.utils.BuilderUtils;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;
import com.cristiano.utils.CRDebugDraw;
import com.cristiano.utils.Log;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class VerificaLineFunction {

	int max=200;
	CRDebugDraw draw=new CRDebugDraw(max);
	private ElementManager em;

	public static void main(String[] args) throws IOException {
		new VerificaLineFunction();
		
	}
	
	public VerificaLineFunction() throws IOException {
		em = new ElementManager();
		em.loadBlueprintsFromFile();
		
		
		String tagCurve="line curved functions leaf t1";
		String tagLine="line straight functions leaf";
		String tagCurve2="line curved functions leaf t2";
		
		Log.debug("Teste 1");
		adicionaLinhas( tagCurve, tagLine,tagCurve2,10,20,150,20);
		Log.debug("Teste 2");
		adicionaLinhas( tagCurve, tagLine,tagCurve2,180,150,10,60);
		Log.debug("Teste 3");
		adicionaLinhas( tagCurve, tagLine,tagCurve2,30,100,30,160);
		Log.debug("Teste 4");
		adicionaLinhas( tagCurve, tagLine,tagCurve2,120,35,180,140);
		Log.debug("Teste 5");
		adicionaLinhas( tagCurve, tagLine,tagCurve2,20,185,180,180);
		
		
		//ImageWriter.colorWriteImage(data);
		draw.finishDebugDraw("verificaLineFunction");
		System.out.println("fim");
	}

	private void adicionaLinhas( String tagCurveT1, String tagLine,String tagCurveT2, int x1,int y1,int x2,int y2) {
		Blueprint lineBP;
		//float ini=-10f;
		//float mult=2f;
		draw.drawCircle(new Vector2f(x1,y1), 2, Color.green);
		draw.drawCircle(new Vector2f(x2,y2), 2, Color.blue);
		IGameElement pickT1 = em.pickOne(tagCurveT1);
		IGameElement pickT2 = em.pickOne(tagCurveT2);
		for (int i=0;i<5;i++){
			lineBP = em.createBlueprint(pickT1,false);
			defineProperties(x1, y1, x2, y2, lineBP);
			//	lineBP.setProperty("fatPerc = "+(ini+mult*i));
			generateLine( lineBP, Color.red);
			
			lineBP = em.createBlueprint(pickT2,false);
			defineProperties(x1, y1, x2, y2, lineBP);
			generateLine( lineBP, Color.blue);
		}
		
		lineBP = em.createBlueprint(em.pickOne(tagLine));
		defineProperties(x1, y1, x2, y2, lineBP);
		generateLine( lineBP, Color.black);
		
		
	
		
		
	}

	private void defineProperties(int x1, int y1, int x2, int y2, Blueprint lineBP) {
		lineBP.setProperty("p0X = "+x1);
		lineBP.setProperty("p0Y = "+y1);
		lineBP.setProperty("p0Z = 0");
		lineBP.setProperty("p1X = "+x2);
		lineBP.setProperty("p1Y = "+y2);
		lineBP.setProperty("p1Z = 0");
		lineBP.setProperty("midPoint = 25");
		lineBP.setProperty("qtdPts = 50");
	}

	private void generateLine(Blueprint lineBP, Color cor) {
		IGameElement elLine = em.createFinalElement(lineBP);
		List<IGameElement> lines = elLine.getObjectList("line");
		if (lines.size()>0){
			for (IGameElement line:lines){
				generateLine( cor, line);
			}
		} else {
			generateLine( cor, elLine);
		}
	}

	private void generateLine( Color cor, IGameElement elLine) {
		int qtdPontos=elLine.getPropertyAsInt("qtdPts");
		String pointList=StringHelper.clear(elLine.getProperty("pointList"));
		String objName = pointList + "#0" ;
		Vector3f ptAnt = BuilderUtils.calculaPonto(elLine, elLine,objName,Vector3f.ZERO,Vector3f.ZERO);
		draw.setColor(cor);
		for (int i=1;i<=qtdPontos;i++){
			objName = pointList + "#" + i;
			Vector3f ponto = BuilderUtils.calculaPonto(elLine, elLine,objName,Vector3f.ZERO,Vector3f.ZERO);
			//draw.drawPoint(ponto.x, ponto.y, cor);
			
			draw.drawLineXY(ptAnt, ponto);
			ptAnt=ponto;
			//data[(int) ponto.x][(int) ponto.y]=cor;
			
		}
	}
}

