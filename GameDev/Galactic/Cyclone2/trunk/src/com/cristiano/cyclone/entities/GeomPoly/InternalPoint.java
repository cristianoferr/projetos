package com.cristiano.cyclone.entities.GeomPoly;

import java.util.ArrayList;

import com.cristiano.cyclone.utils.Vector3;


/*
 * Essa classe vai ser usado para determinar os pontos de contato do objeto... 
 * 
 */
public class InternalPoint {

private Vector3 posicao;
private ArrayList<Face> faces = new ArrayList<Face>();//Faces proximas a esse objeto
GeomOBJ geom;

public InternalPoint(double x, double y, double z,GeomOBJ geom) {
	setPosicao(new Vector3(x,y,z));
	this.geom=geom;
}

//Esse metodo verifica a distancia entre esse ponto e as faces
public ArrayList<Double> calcDistFace(Vector3 pos,ArrayList<Face> indices){
	ArrayList<Double> dist=new ArrayList<Double>();
	
	for (int i=0;i<indices.size();i++){
		Vector3 pf=indices.get(i).getCentral();
		dist.add(new Double(pos.getSubVector(pf).magnitude()));
	}
	
	return dist;
}

//Esse metodo verifica se o ponto é interno do objeto
public boolean isInternal(ArrayList<Face> indices) {
	ArrayList<Double> distCentro=calcDistFace(getPosicao(),indices);

	
	for (int i=0;i<8;i++){
		Vector3 newPos=new Vector3(getPosicao());
		double dif=0.01;
		if (i==0)newPos.x=newPos.x*(1+dif);
		if (i==1)newPos.x=newPos.x*(1-dif);
		if (i==2)newPos.y=newPos.y*(1+dif);
		if (i==3)newPos.y=newPos.y*(1-dif);
		if (i==4)newPos.z=newPos.z*(1+dif);
		if (i==5)newPos.z=newPos.z*(1-dif);
		if (i==6)newPos.multiVectorScalar(1+dif);
		if (i==7)newPos.multiVectorScalar(1-dif);
		
		
		/*if (i==5)newPos.x=newPos.x*(1+dif);
		if (i==4)newPos.x=newPos.x*(1-dif);
		if (i==3)newPos.y=newPos.y*(1+dif);
		if (i==2)newPos.y=newPos.y*(1-dif);
		if (i==1)newPos.z=newPos.z*(1+dif);
		if (i==0)newPos.z=newPos.z*(1-dif);*/
		ArrayList<Double> distNew=calcDistFace(newPos,indices);
		//Se falhar algum eixo, é porque é um ponto externo
		boolean flagProximo=comparaDistancias(distCentro,distNew,indices);
		//System.out.println("Compara:"+flagProximo+" i:"+i+" pos:"+posicao+" newPos:"+newPos );
		if (!flagProximo)
			return false;
	}
	
	return true;
}
//Verifica se alguma distancia diminuiu (se sim, o ponto foi na direcao de uma face, ou seja, é interno
//Se a distanciaNova for menor, entao é porque alguma face chegou mais perto da posicao nova... ou seja, tem alguma face na direcao nova... 
private boolean comparaDistancias(ArrayList<Double> d1,ArrayList<Double> d2,ArrayList<Face> indices){
	for (int i=0;i<d1.size();i++){
	//	System.out.println("i("+i+"): d2:"+(d2.get(i).doubleValue()+"<"+(d1.get(i).doubleValue())+":"+(d2.get(i).doubleValue()<d1.get(i).doubleValue())+" face:"+indices.get(i).getCentral()));
		if (d2.get(i).doubleValue()<d1.get(i).doubleValue())return true;
	}
	return false;
}

public void addFace(Face face) {
	faces.add(face);
	
}

public void setPosicao(Vector3 posicao) {
	this.posicao = posicao;
}

public Vector3 getPosicao() {
	return posicao;
}

public int facesCount() {
	return faces.size();
}

public ArrayList<Face> getFaces() {
	return faces;
}
}
