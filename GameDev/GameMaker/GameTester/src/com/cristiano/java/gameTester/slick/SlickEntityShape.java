package com.cristiano.java.gameTester.slick;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import com.cristiano.java.blueprintManager.entidade.AbstractElement;
import com.cristiano.java.blueprintManager.entidade.GenericElement;
import com.cristiano.java.utils.Hull2D.BaseShape2d;
import com.cristiano.java.utils.Hull2D.GenericShape2d;
import com.cristiano.math.PhysicsConsts;
import com.cristiano.math.Vector3;

public class SlickEntityShape  extends GenericShape2d{

	
	//GameLayer
	private Shape gameShape;
	ArrayList<Vector3> pontos;

	
	public SlickEntityShape(GenericElement link,Vector3 posicao,SlickEntityShape objetoPai){
		super(link,posicao,objetoPai);
	}
	
	@Override
	public void inicializaShape(int width, int height) {
		super.inicializaShape(width, height);
		
		pontos = new ArrayList<Vector3>();
		for (int i=0;i<getShapePts().size();i++){
			Vector3 pt = getShapePts().get(i);
			pontos.add(new Vector3(pt.x,pt.y));
		}
	}
	
	@Override
	public void kill() {
		super.kill();
	}
	
	
	
	@Override
	public void atualizaGameShape(){
		
		atualizaRotacao();
		
		for (BaseShape2d subShape : getSubObjetos()) {
			subShape.atualizaGameShape();
		}
		
		gameShape=new Polygon();
		pontos = new ArrayList<Vector3>();
		for (int i=0;i<getShapePts().size();i++){
			Vector3 pt = getShapePts().get(i);
			((Polygon)gameShape).addPoint(pt.getXf(), pt.getYf());
		}
		gameShape.setLocation(getPosicaoAbsoluta().getXf(),getPosicaoAbsoluta().getYf());
		
		//gameShape = gameShape.transform(Transform.createRotateTransform((float) (getOrientationAbsoluta()*PhysicsConsts.deg2rad)));
	}
	
	public void atualizaRotacao() {
		if (isInvertYaxis()){
			//System.out.println("invertendo Y: "+getName());
			setInvertYaxis(false);
			inverteY();
		}
		if (isInvertXaxis()){
			//System.out.println("invertendo X: "+getName());
			setInvertXaxis(false);
			inverteX();
		}
		super.rotate(getOrientation());
		setOrientation(0);
	}
	
	
	public Shape getGameShape() {
		return gameShape;
	}
	
	@Override
	public boolean isValid(){
		if (!super.isValid())return false;
		
		for (int i=0;i<getSubObjetos().size()-1;i++) {
			for (int j=i+1;j<getSubObjetos().size();j++) {
				SlickEntityShape filhoI=(SlickEntityShape) get(i);
				SlickEntityShape filhoJ=(SlickEntityShape) get(j);
				Shape shapeI = filhoI.getGameShape();
				Shape shapeJ = filhoJ.getGameShape();
				if (shapeI.intersects(shapeJ)){
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	public void draw(Graphics g) {
		g.setColor(getColor());
		if (size()>1){
			if (gameShape!=null){
				if (SlickGame.debug_draw_line){
					g.draw(gameShape);
				} else {
					g.fill(gameShape);
				}
			}
		}
		
		desenhaFilhos(g);
		
		debugDraw(g);
	}

	private Color getColor() {
		switch (getNivel()){
			case 0:return Color.white;
			case 1:return Color.blue;
			case 2:return Color.green;
			case 3:return Color.red;
			case 4:return Color.lightGray;
			case 5:return Color.orange;
			case 6:return Color.magenta;
		
		}
		return Color.pink;
	}

	private void desenhaFilhos(Graphics g) {
		for (int i=0;i<getSubObjetos().size();i++) {
			SlickEntityShape shape=(SlickEntityShape) get(i);
			shape.draw(g);
		}
	}

	private void debugDraw(Graphics g) {
		g.setColor(getColor());
		for (BaseShape2d shape : getSubObjetos()) {
			//System.out.println(shape.getBaseElement());
			//System.out.println(getBaseElement().getIdentifier()+" " +shape.getBaseElement().getIdentifier() );
			g.setColor(Color.white);
			boolean desenha=false;
			if ((shape.getName().contains("ponto")) && (SlickGame.debug_names)){
				desenha=true;
			}
			if ((!shape.getName().contains("ponto")) && (SlickGame.debug_names_components)){
				desenha=true;
			}
			
				if (SlickGame.debug_draw_points)
					g.drawOval(+shape.getPosicaoAbsoluta().getXf()-2, shape.getPosicaoAbsoluta().getYf()-2, 4, 4);
				
				if (desenha){
				
				g.drawString(shape.getName(),shape.getPosicaoAbsoluta().getXf(), shape.getPosicaoAbsoluta().getYf()+10);
				
				g.drawString(shape.getName(),shape.getPosicaoAbsoluta().getXf(), shape.getPosicaoAbsoluta().getYf()+10);
			}
		}
	}

	@Override
	public void update(float dt){
		super.update(dt);
		atualizaGameShape();
	}

	@Override
	public GenericShape2d criaGameShape(AbstractElement base, Vector3 vet) {
		SlickEntityShape ret = new SlickEntityShape((GenericElement)base,vet,this);
		return ret;
	}
}
