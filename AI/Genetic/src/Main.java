

import geneticAlgorithm.GeneticAlgorithm;
import geneticProgramming.Node;
import geneticProgramming.Functions.NodeTerminal;
import geneticProgramming.SimpleMath.NodeAddiction;
import geneticProgramming.SimpleMath.NodeDivision;
import geneticProgramming.SimpleMath.NodeMin;

import comum.Objeto;

import cristiano.GenProg.GeneticProgramming;
import cristiano.dados.Dado;
import cristiano.objsPlano.NewtonEngine;
import cristiano.objsPlano.ObjetoBasico;
 
/*
 * Created on 07/07/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author CMM4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Main extends Objeto  {


	public static void main(String[] args) {
		GeneticAlgorithm ga=new GeneticAlgorithm();
		
//		ObjetoBasico obj=new ObjetoBasico(null,100,100,6,6);
//		GeneticProgramming gp=new GeneticProgramming();
//		
//		Node nodeMin=new NodeMin();
//		Node nodeAdd=new NodeAddiction();
//		Node nodeDiv=new NodeDivision();
//		Node node2=new NodeTerminal(gp,"2");
//		Node node3=new NodeTerminal(gp,"3");
//		nodeMin.addNode(node2);
//		nodeMin.addNode(node3);
//		nodeMin.addNode(nodeAdd);
//		nodeAdd.addNode(nodeDiv);
//		nodeDiv.addNode(node2);
//		nodeDiv.addNode(node2);
//		nodeDiv.addNode(node2);
//		out("0:"+nodeMin.getSubNodeAt(new Dado(0),0));
//		out("1:"+nodeMin.getSubNodeAt(new Dado(0),1));
//		out("2:"+nodeMin.getSubNodeAt(new Dado(0),2));
//		out("3:"+nodeMin.getSubNodeAt(new Dado(0),3));
//		out("4:"+nodeMin.getSubNodeAt(new Dado(0),4));
//		out("5:"+nodeMin.getSubNodeAt(new Dado(0),5));
//		out("6:"+nodeMin.getSubNodeAt(new Dado(0),6));
//		
//		NewtonEngine n=new NewtonEngine();
//		out("180,50>>");
//		n.calcPower(178,10);
		//out("45,100>>");
		//n.calcPower(315,100);
		//out("-45,100>>");
		//n.calcPower(315,100);
		
		/*Dado var1=new Dado("var1",10,"");
		gp.addVariavel(var1);
		gp.init();
		
		//out("getNode:"+gp.getNode().getValue());
		for (int i=0;i<100;i++){
			gp.initFitness();
			for (int x=-100;x<100;x++){
				var1.setDouble(x);
				// x*x+x+1
				gp.calcFitness(x*x+x+1);
			}
			gp.endFitness();
		}*/
	}
}
