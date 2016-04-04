
import java.util.Vector;

import RedeNeural.Config;
import RedeNeural.InputDado;
import RedeNeural.NetBP;

/*
 * Created on 03/01/2005
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
public class LearningNet extends Objeto {
	Config cfgRede=null;
	NetBP net=null;
	int maxActions=0;
	Vector learnedItems=new Vector();
	int maxItems=60;
	ItemLearningMatrix lastAction=null;
	int totOutputs=0;
	int totTurns=0;
	int saveEveryTurns=2000;
	Vector display;
	double lastPerf=0;
	
 /* (non-Javadoc)
 * @see java.lang.Object#finalize()
 */



	public LearningNet (Vector display,int totOutputs) {
		super();
		this.display=display;
		this.maxActions=(int)Math.pow(2,totOutputs)+1;
		this.totOutputs=totOutputs;
		//out("totInputs:"+maxActions);
		//abort(100);	
	}
	
	public ItemLearningMatrix searchMatrix(Vector input){
		getConfig(input.size());
		
		for (int i=0;i<learnedItems.size();i++){
			ItemLearningMatrix item = (ItemLearningMatrix)learnedItems.elementAt(i);
				if (item!=null){
					int e=0;
					for (int k=0;k<input.size();k++){
						Double inp=(Double)input.elementAt(k);
						Double mat=(Double)item.getInputs().elementAt(k);
						if (inp.doubleValue()!=mat.doubleValue()) e++;
					}
					if (e==0) {
						return item;
					}
			}
		}		
		
		return null;
	}
	public void treina(){
		if (net!=null)
		net.turn();
		totTurns++;
		if (totTurns%saveEveryTurns==0){
			net.dbSave();
		}

	}
	public void generateInputs(){
		if (net==null) return;
		net.input.inputs.clear();
		for (int i=0;i<learnedItems.size();i++){
			ItemLearningMatrix item=(ItemLearningMatrix)learnedItems.elementAt(i);
			InputDado d=new InputDado();
			d=item.getNetData(net);
			d.setPerf(item.getActionPerf());
			net.input.inputs.add(d);
		}
	}

	public int getAction(Vector input){
			ItemLearningMatrix learned=searchMatrix(input);
			if (learned==null) {
				lastAction=new ItemLearningMatrix();
				lastAction.setInputs(input);
				int a;
				if ((Math.random()*100<80)&&(learnedItems.size()>maxItems/2)){
				a=(int)(lastAction.getAction(net));//*getMaxActions()+2);
				out("Net move.  learnedItems: "+learnedItems.size()+" out:"+a );
				} else {
				a=(int)(Math.random()*getMaxActions()+2);
				out("Random move.  learnedItems: "+learnedItems.size());
				}
				lastAction.setAction(a);
				
				return a;
			}
			else {
				double action=learned.getAction(net);
				lastAction=learned;
			//	out("Learned move.  learnedItems: "+learnedItems.size());
				return (int)action;
			}
		}
		
		
	public void checkPerformance_new(double perf){
		
		if (lastAction==null) {
		
			return;
		}
		lastAction.setActionPerf(perf);
		
		out("perf:"+Math.round(perf)+" NetError:"+net.totError+" Action Perf:"+lastAction.getPerformance()+" LRR:"+net.getLearningRate());
		if (perf>10){
		
			if (lastAction.getAdded()==0){
				addLastAction();
				//out("item Adicionado!");
			}
		}
	}
		
	public void turn(){
		
		display.add(new String("NetError: "+net.totError));
		display.add(new String("Action Perf: "+lastAction.getPerformance()));
		display.add(new String("LR: "+net.getLearningRate()));
		display.add(new String("Input Perf: "+net.inp.getPerf()));
		display.add(new String("Perf:"+Math.round(lastPerf)));
	}
		
	public void checkPerformance(double perf){
		
		if (lastAction==null) {
		
			return;
		}
		lastPerf=perf;
		lastAction.setActionPerf(lastPerf);
		
		//out("perf:"+Math.round(perf)+" NetError:"+net.totError+" Action Perf:"+lastAction.getPerformance()
		//+" LR:"+net.getLearningRate()+" IP:"+net.inp.getPerf()  );
		if (perf!=0){
			if (learnedItems.size()>1)
			net.config.LearningRate=2*net.totError/learnedItems.size();
			if (net.config.LearningRate>1) net.config.LearningRate=0.99;
			if (lastAction.getPerformance()<10) {
			  lastAction.setPerformance(lastAction.getPerformance()+1);
			
			}			
			if (lastAction.getAdded()==0){
				addLastAction();
				//out("item Adicionado!");
			} else {
			//	out("item existente!");
			}
		} else{
			lastAction.setPerformance(lastAction.getPerformance()-1);
			if ((lastAction.getPerformance()<0))
				removeElement(lastAction);
		}
	}


	public void removeElement(ItemLearningMatrix input){
		for (int i=0;i<learnedItems.size();i++){
			ItemLearningMatrix item = (ItemLearningMatrix)learnedItems.elementAt(i);
				if (item!=null){
					int e=0;
					for (int k=0;k<input.getInputs().size();k++){
						Double inp=(Double)input.getInputs().elementAt(k);
						Double mat=(Double)item.getInputs().elementAt(k);
						if (inp.doubleValue()!=mat.doubleValue()) e++;
					}
					if (e==0) {
						learnedItems.removeElementAt(i);
						out("Item removido!");
					}
			}
		}		
		
	}
	public void addLastAction(){
/*		learnedItems.add(new ItemLearningMatrix(lastAction));
		if (learnedItems.size()>maxItems)
		  learnedItems.removeElementAt(0);*/
		  
		if (learnedItems.size()<maxItems){
		}
  		else {
			learnedItems.remove(0);
  		}
		learnedItems.add(new ItemLearningMatrix(lastAction));
	}

	public Config getConfig(int sizeInp){
		if (cfgRede!=null) return cfgRede;
		out("getconfig()");
		cfgRede=new Config();
		cfgRede.inputSource=1000;
		cfgRede.runs=1;
		cfgRede.LearningRate=0.5;
		cfgRede.LearningType=1;
		//config.dataTypeIn=new Vector(Config.fillVector(0,35));
		//config.dataTypeOut=new Vector(Config.fillVector(0,4));
		cfgRede.inputSize=sizeInp;
		cfgRede.outputSize=totOutputs;
		cfgRede.innerLayers=2;
		cfgRede.layersSize=60;
		cfgRede.Training=true;
		cfgRede.name="Robot";
		
		net=new NetBP(cfgRede);
		//net.dbLoad();
		return cfgRede;
	}
	/**
	 * @return
	 */
	public Vector getLearnedItems() {
		return learnedItems;
	}

	/**
	 * @return
	 */
	public int getMaxActions() {
		return maxActions;
	}

	/**
	 * @param vector
	 */
	public void setLearnedItems(Vector vector) {
		learnedItems = vector;
	}

}
