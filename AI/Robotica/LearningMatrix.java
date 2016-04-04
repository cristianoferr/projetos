import java.util.Vector;

import RedeNeural.Config;
import RedeNeural.NetBP;


//import Red Config;

/*
 * Created on 24/11/2004
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
public class LearningMatrix extends Objeto {
ItemLearningMatrix matriz[];
ItemLearningMatrix matrizErro[];
int maxX=200;
int maxActions=0;
ItemLearningMatrix lastAction=null;
	public LearningMatrix (int totOutputs) {
		super();
		
		this.maxActions=(int)Math.pow(2,totOutputs)+1;
		matriz=new ItemLearningMatrix[maxX];
		matrizErro=new ItemLearningMatrix[maxX];
	}
	public ItemLearningMatrix searchMatrix(Vector input){
		
		for (int i=0;i<maxX;i++){
				if (matriz[i]!=null){
					int e=0;
					for (int k=0;k<input.size();k++){
						Double inp=(Double)input.elementAt(k);
						Double mat=(Double)matriz[i].getInputs().elementAt(k);
						if (inp.doubleValue()!=mat.doubleValue()) e++;
					}
					if (e==0) {
						return matriz[i];
					}
			}
		}		
		return null;
	}
	public int getAction(Vector input){
		ItemLearningMatrix learned=searchMatrix(input);
		if (learned==null) {
			lastAction=new ItemLearningMatrix();
			lastAction.setInputs(input);
			int a=(int)(Math.random()*getMaxActions()+2);
			lastAction.setAction(a);
			out("Random move");
			return a;
		}
		else {
			double action=learned.getAction();
			lastAction=learned;
			out("Learned move");
			return (int)action;
		}
	}
	public void checkPerformance(double perf){
		
		if (lastAction==null) {
		
			return;
		}
		out("perf:"+perf+" Action Performance:"+lastAction.getPerformance());
		if (perf>0){
			if (lastAction.getPerformance()<10) {
			  lastAction.setPerformance(lastAction.getPerformance()+1);
			
			}			
			if (lastAction.getAdded()==0){
				addLastAction();
			} else {
			//	out("item existente!");
			}
		} else{
			lastAction.setPerformance(lastAction.getPerformance()-1);
			if ((lastAction.getPerformance()<0) &&(lastAction.getAdded()!=0))
				removeElement(lastAction);
		}
	}
	public void removeElement(ItemLearningMatrix item){
//		item.removeFromNet(net);
//		matriz[item.getX()].removeFromNet(net);
		matriz[item.getX()]=null;
		item=null;
		out("Item removido!");
	}
	public ItemLearningMatrix addLastAction(){
		for (int i=0;i<maxX;i++){
				if (matriz[i]==null){
					matriz[i]=new ItemLearningMatrix(lastAction);
					matriz[i].setAdded(1);
					matriz[i].setX(i);
//					matriz[i].addToNet(net);
					out("Item adicionado!");
					return matriz[i];
			}
		}
		return null;
	}


	
/**
 * @return
 */
public int getMaxActions() {
	return maxActions;
}

/**
 * @return
 */
public int getMaxX() {
	return maxX;
}



/**
 * @param i
 */
public void setMaxActions(int i) {
	maxActions = i;
}

/**
 * @param i
 */
public void setMaxX(int i) {
	maxX = i;
}

/**
 * @param i
 */

}
