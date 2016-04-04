package cristiano.behaviortree;

import cristiano.behaviortree.decorator.LoopNode;
import cristiano.behaviortree.execution.Executor;
import cristiano.intel.FactManager;
import cristiano.intel.Query;
import cristiano.intel.QueryManager;

public class Tester {

	/**
	 * @param args

http://sourceforge.net/projects/jbt/files/JBTEditor/
	 */
	public static void main(String[] args) {
	LoopNode loopNode=new LoopNode("loop",5);
	
	Node nodeRoot=new Node("root");
	loopNode.addFilho(nodeRoot);

	
	
	
	//Node nodeF1=new Node("F1",nodeRoot);
	
	Node nodeF2=new Node("F2");
	nodeRoot.addFilho(nodeF2);
	//Selector nodeSel1=new Selector("Sel1",nodeF2);
	/*Node nodeSelF1=new Node("SelF1",nodeSel1);
	Node nodeSelF2=new Node("SelF2",nodeSel1);*/
	Node nodePar=new ParalelNode("Paralel");
	nodeRoot.addFilho(nodePar);
	//Selector nodeSel2=new Selector("Sel2",nodeSel1);
	//Selector nodeSel3=new Selector("Sel3",nodeSel1);
	Node nodeF1F1=new Node("F1F1");
	Node nodeF1F2=new Node("F1F2");
	nodePar.addFilho(nodeF1F1);
	nodePar.addFilho(nodeF1F1);
	/*Node nodeF1
	F3=new Node("F1F3",nodeF1);
	Node nodeF2F1=new Node("F2F1",nodeF2);
	Node nodeF2F1F1=new Node("F2F1F1",nodeF2F1);*/

	Executor exec=new Executor();
	//exec.addPointer(nodeRoot);
	exec.addPointer(loopNode);
	
	/*AssertionNode assertion=new AssertionNode("Assertion",nodeSel2,Executor.QRY_ENEMY_NEAR);
	AssertionNode assertion2=new AssertionNode("Assertion2",nodeSel3,Executor.QRY_ENEMY_NEAR);
*/
	FactManager fm=new FactManager();
	QueryManager qm=new QueryManager(fm);
	
	exec.setFm(fm);
	exec.setQm(qm);
	
	fm.createFact("FACT", 10);
	
//	Query query=new Query(Executor.QRY_ENEMY_NEAR,"FACT");
	//exec.setQuery(Executor.QRY_ENEMY_NEAR, query);
///	System.out.println("Query isOn:"+query.isOn(fm));

	
	int c=0;
	while (exec.isRunning()){
		c++;
		System.out.println("C:"+c);
		exec.turn();
	}
	
	}

}
