package cristiano.behaviortree.execution;

import java.util.Vector;

import cristiano.behaviortree.Node;
import cristiano.behaviortree.Node.Status;

public class Pointer {
		private Executor exec;
		private Vector<Node> pilha;
		boolean lastResult=true;
		
		public Pointer(Executor exec,Node node){
			this.exec=exec;
			pilha=new Vector<Node>();
			addNode(node);
		}
		
		public void addNode(Node node){
			if (node==null)return;
			pilha.add(node);
		}
		
		public double executeAction(String action,double intensity){
			return getExec().executeAction(action,intensity);
		}
		
		public void turn(){
			if (pilha.size()<=0)return;
			Node n=pilha.get(pilha.size()-1);
			n.turn(this);
		}

		
		//go back to the previous node on the stack
		public void moveUp(){
			Node n=pilha.get(pilha.size()-1);
			//System.out.println("moveUp: Node="+n.getName()+" status="+n.getStatus());
			//n.setStatus(Status.NOT_STARTED);
			pilha.remove(pilha.size()-1);
		}

		public void addToStack(Node n) {
			n.reset();
			pilha.add(n);
		}

		public Node getCurrent(){
			if (pilha.size()==0) return null;
			return pilha.get(0);
		}

		public boolean getLastResult() {
			return lastResult;
		}

		public void setLastResult(boolean lastResult) {
			this.lastResult = lastResult;
		}

		public Executor getExec() {
			return exec;
		}
		
		public boolean isRunning(){
			return (pilha.size()>0);
		}

		
	}
