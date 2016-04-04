package com.cristiano.java.gameTester.slick.tests;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.cristiano.java.blueprintManager.ElementManager;
import com.cristiano.java.blueprintManager.entidade.AbstractElement;
import com.cristiano.java.blueprintManager.entidade.GenericElement;
import com.cristiano.java.blueprintManager.entidade.blueprint.Blueprint;
import com.cristiano.java.blueprintManager.entidade.blueprint.Factory;
import com.cristiano.java.blueprintManager.entidade.blueprint.Mod;
import com.cristiano.java.blueprintManager.entidade.blueprint.Template;
import com.cristiano.java.blueprintManager.extras.Extras;
import com.cristiano.java.blueprintManager.extras.Log;
import com.cristiano.java.blueprintManager.extras.Extras.ENUM_ESTILO_DESENHO;
import com.cristiano.java.blueprintManager.params.Parametro;
import com.cristiano.java.blueprintManager.utils.PhysicsHelper;
import com.cristiano.java.blueprintManager.utils.StringHelper;
import com.cristiano.java.gameTester.slick.SlickEntity;
import com.cristiano.java.gameTester.slick.SlickEntityShape;
import com.cristiano.java.gameTester.slick.SlickGame;
import com.cristiano.java.utils.Hull2D.GenericShape2d;
import com.cristiano.math.PhysicsConsts;
import com.cristiano.math.Vector3;


public class TesteShape {
	ElementManager em=null;
	int height=100;
	int width=100;

	@Before
	public void tearUp() {
		em=new ElementManager();
		Log.nivelDebug();
	}
	
	
	@Test public void testGeracaoShape() throws IOException {
		em.loadBlueprintsFromFile();
		
		SlickEntity entity = criaNovaEntidade("entidade test",100,100);

		SlickEntityShape shape=(SlickEntityShape) entity.get(0);
		assertTrue("entity null",entity!=null);
		assertTrue("shape null",shape!=null);
		System.out.println("entity:"+entity.getBaseElement());
		System.out.println("shape:"+shape.getBaseElement());
		
		ENUM_ESTILO_DESENHO tipoDesenho = shape.getTipoDesenho();
		assertTrue("tipoDesenho=null",tipoDesenho!=null);
		assertTrue("tipoDesenho <> polygon:"+tipoDesenho,tipoDesenho==ENUM_ESTILO_DESENHO.POLYGON);
		
		int qtdFilhos=shape.getBaseElement().getParamAsInt(Extras.LIST_PROPERTY, "numFilhos");
		
		testaPosicao(shape, qtdFilhos);
	}


	private void testaPosicao(SlickEntityShape shape, int qtdFilhos) {
		float saida;
		float esperado;
		String obj;
		for (int i=0;i<qtdFilhos;i++){
			obj="triang#"+i;
			saida=shape.getBaseElement().getParamAsFloat(Extras.LIST_POSITION, obj);
			esperado=i*360/qtdFilhos;
			assertTrue("Position do obj "+obj+" devia ser "+esperado+" mas é "+saida,saida==esperado);
			
			saida=shape.getBaseElement().getParamAsFloat(Extras.LIST_ORIENTATION, obj);
			assertTrue("Orientation do obj "+obj+" devia ser "+esperado+" mas é "+saida,saida==esperado);
			
			float radius=shape.getBaseElement().getParamAsFloat(Extras.LIST_POSITION, obj+Extras.SUFFIXO_POSITION_RADIUS);
			
			Vector3 posicaoEsperada=calculaPosicaoAngulo(esperado,radius);
			
			
			SlickEntityShape shapeFilho=(SlickEntityShape) shape.get(i);
			Vector3 posicaoFilho = shapeFilho.getPosicao();
			assertTrue("Posicao do obj "+obj+"  ("+posicaoFilho+") diferente da posicao esperada ("+posicaoEsperada+")",posicaoFilho.equals(posicaoEsperada));
		}
	}

	private Vector3 calculaPosicaoAngulo(float angle,float radius){
		
		float offsetX=0;
		float offsetY=0;
		float largura=1;
		float comprimento=1;

		double x = offsetX+largura*radius*width*Math.cos(PhysicsConsts.deg2rad*angle);
		double y = offsetY+comprimento*radius*height*Math.sin(PhysicsConsts.deg2rad*angle);
		return new Vector3(x,y,0);
	}

	
	private SlickEntity criaNovaEntidade(String tag, int atX, int atY) {
		GenericElement ge = (GenericElement)em.createFinalElementFromTag(tag);
		
		//System.out.println("GE:"+ge);
		SlickEntity entity=new SlickEntity(ge,new Vector3(atX,atY));
		entity.setName("teste");
		entity.inicializaShape(height, width);
		entity.atualizaGameShape();
		return entity;
	}
}


