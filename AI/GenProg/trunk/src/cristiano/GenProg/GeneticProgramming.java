/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg;


import java.util.Vector;

import com.cristiano.comum.Formatacao;
import comum.Objeto;

import cristiano.GenProg.Node.DadoNode;
import cristiano.GenProg.Node.Node;
import cristiano.GenProg.Node.NodeVariavel;
import cristiano.GenProg.Node.Functions.NodeAtuador;
import cristiano.GenProg.Node.Functions.NodeTerminal;
import cristiano.GenProg.Node.Memory.NodeGetVar;
import cristiano.GenProg.Node.Memory.NodeSetVar;
import cristiano.GenProg.rules.FitnessRules;
import cristiano.GenProg.set.AbstractSet;
import cristiano.GenProg.set.LogicSet;
import cristiano.GenProg.set.MathSet;
import cristiano.GenProg.set.TrigonometrySet;
 
enum SortMethod {
	GREATER,LOWER
}

/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GeneticProgramming extends Objeto {
	private Objeto obj=null;
	private Vector<Node> nodes=new Vector<Node>();
	public static final int GA_POPSIZE=201;  // Números de nodes para fazer GP
    public static final int MAX_SIZE=10;   // Número máximo de sub-Nodes (em largura e profundidade)
    public static final int MAX_SIZE_LAYER=6; // Número máximos de sub-nodes por camada
    public static final int MAX_FITNESS=2000000000; // Erro maximo em fitness
    public static final int GA_MAXITER	=	136384;		// maximum iterations
    public static final double GA_ELITRATE	= 	0.05f;		// elitism rate
    public static final double GA_MUTATIONRATE	=0.89f;		// mutation rate
    private boolean zeroReached=false;  //Usado para determinar se o objetivo foi alcançado
    private final double GA_RANDOM_NODE_CHANCE=0.2f;
    
    private Vector<String> codigoFonte=new Vector<String>();
    
    private Vector<String> terminais=new Vector<String>();
    private Vector<String> funcoes=new Vector<String>();
    private Vector<NodeVariavel> variaveis=new Vector<NodeVariavel>();
    private Vector<Double> pilha=new Vector<Double>();
    private SortMethod sortMethod=SortMethod.GREATER;
    private Vector<AbstractSet> functionSets=new Vector<AbstractSet>();
    
    //private String nodeRaiz="sum";
    
    private FitnessRules fitnessRules;
    
	public GeneticProgramming() {
		super();
		fitnessRules=new FitnessRules(); 
	}
	
	public void addFitnessRule(String rule,String par,double penalty){
		fitnessRules.addFitnessRule(rule,par,penalty);
	}
	public void addFitnessRule(String rule,int par,double penalty){
		fitnessRules.addFitnessRule(rule,par,penalty);
	}
    
/*	public String getNodeRaiz() {
		return nodeRaiz;
	}
	public void setNodeRaiz(String nodeRaiz) {
		this.nodeRaiz = nodeRaiz;
	}*/
	
	
	
    public void pushG(double d){
    	pilha.add(Double.valueOf(d));
    }
    public double popG(){
    	if (pilha.size()==0) {
    		return 0;
    	}
    	double d=((Double)pilha.lastElement()).doubleValue();
    	pilha.setSize(pilha.size()-1);
    	return d;
    }
    public double topG(){
    	if (pilha.size()==0) return 0;
    	double d=((Double)pilha.lastElement()).doubleValue();
    	return d;
    }    
    
   

    
    public void loadMemory(){
    	funcoes.add("getVar");
    	funcoes.add("setVar");
    }
    

	
	/*
	 * Adiciona um set ao conjunto de sets 
	 */
	public void addFunctionSet(AbstractSet set){
		functionSets.add(set);
		set.initLoad(this);
	}
	
	public void defaultNodes(){
		addFunctionSet(new MathSet());
		addFunctionSet(new TrigonometrySet());
		addFunctionSet(new LogicSet());
		//loadFinanc();
		
		//loadAI();

	}
	public void addTerminal(String s){
		terminais.add(s);
	}	

	public void printCodigoFonte(){
		
		for (int i=0;i<getCodigoFonte().size();i++){
			if ((String)getCodigoFonte().elementAt(i)!="") 
				System.out.println(getCodigoFonte().elementAt(i));
		}
	}
	
	
	public void addFuncao(String s){
		funcoes.add(s);
	}	
	public void addVariavel(DadoNode d){
		terminais.add("var"+variaveis.size());
		variaveis.add(new NodeVariavel(d,variaveis.size()));
	}
	public void resetFitness(){
		for (int i=0;i<nodes.size();i++){
			((Node)nodes.elementAt(i)).setFitness(0);
		}
		//Node n=((Node)nodes.elementAt(0));
		//n.setDebugging(true);
		
	}
	
	/*
	 * Loop entre os nodes para determinar o fitness do node
	 * "y" é o valor esperado para esse "turn" 
	 * Ex: para uma formula x^2+10 com um x=2 então o valor y esperado seria 14
	 */
	public void calcFitness(double y){
		//out("initcalc n0:"+n.getFitness());
		for (int i=0;i<nodes.size();i++){
			calcFitnessSingleOld(y, i);


		}
	}
	public void calcFitnessSingleAlt(double y, int i) {
		Node node=((Node)nodes.elementAt(i));
		
		node.getValue();
		
		if (Double.isNaN(y)) {
			y=10000;
		}
		node.setFitness(node.getFitness()+y);
		if (node.getFitness()>MAX_FITNESS)  {
			node.setFitness(MAX_FITNESS);
		}
		
	}	
	
	
	public double calcFitnessSingle(int i,double penalty) {
		Node node=((Node)nodes.elementAt(i));
		
		double f=node.getFitness()+node.getValue();
		//out("f:"+f);
		if ((penalty!=-1) && (f==penalty)){
			f=-100000;
		}
		node.setFitness(f);
		if (node.getFitness()>MAX_FITNESS)  node.setFitness(MAX_FITNESS);
		return node.getFitness();
	}	
	
	public void calcFitnessSingleOld(double y, int i) {
		Node node=((Node)nodes.elementAt(i));
		double res=node.getValue();
		double dif=Math.abs(res-y);
		
		if (Double.isNaN(dif)) {dif=10000;}
		node.setFitness(node.getFitness()+dif);
		if (zeroReached) {
			node.setFitness(node.getFitness()+node.size()/4f);
		}
		if (node.getFitness()>MAX_FITNESS)  {
			node.setFitness(MAX_FITNESS);
		}
		
		
	}
	
	/*
	 * Metodo para ordenar os nodes de acordo com o fitness de cada um.
	 */
	public void endFitness(){
		
		
		for (int i=0;i<GA_POPSIZE;i++){
			Node nodei=((Node)nodes.elementAt(i));
			fitnessRules.checkRules(nodei);
		}
		
		
		// Ordenando por fitness: melhores resultados primeiro
		for (int i=0;i<GA_POPSIZE;i++){
			for (int j=i+1;j<GA_POPSIZE;j++){
				Node nodei=((Node)nodes.elementAt(i));
				Node nodej=((Node)nodes.elementAt(j));
				//out(i+"("+nodei.getFitness()+") "+j+" ("+nodej.getFitness()+")");
				
				if (((sortMethod==SortMethod.LOWER) &&(nodei.getFitness()<nodej.getFitness()))
						|| ((sortMethod==SortMethod.GREATER) &&(nodei.getFitness()>nodej.getFitness()))){
					nodes.setElementAt(nodei,j);
					nodes.setElementAt(nodej,i);
					//nodei=nodej;
					//nodej=nodeaux;
				}
				
			}
		}
		
		for (int i=0;i<nodes.size();i+=nodes.size()/10){
			double fit=((Node)nodes.elementAt(i)).getFitness();
			
			out("i:"+i+" fitness:"+Formatacao.format(fit));//+" value:"+((Node)nodes.elementAt(i)).getValue()+" zero:"+zeroReached);
		}
		
	}
	
	public void endTurn(){
		//out("endTurn()");
		Node.count=0;
		if (((Node)nodes.elementAt(0)).getFitness()==0) {
			zeroReached=true;
		}
		
		Vector<Node> nodesAux=new Vector<Node>();
		
		//Definindo elitismo
		int esize = (int) (GA_POPSIZE * GA_ELITRATE);
		//out("esize:"+esize);
		for (int i=0;i<esize;i++){
			nodesAux.add(nodes.elementAt(i).copyNode());
		}
		
		/*Criando o vetor de probabilidades: quanto melhor for o fitness, maiores
		 * as chances são do node de reproduzir.
		 */
		//int totMutations=0;
		int i1=0;
		int i2=0;
		int totX=GA_POPSIZE/3*3+GA_POPSIZE/3*2+GA_POPSIZE/3;
		int vetX[];
		vetX=new int[totX];
		for (int i=0;i<totX;i++){
			if (i<GA_POPSIZE/3*3) {
				vetX[i]=(int)(Math.random() * (GA_POPSIZE/3));
			} else if (i<GA_POPSIZE/3*3+GA_POPSIZE/3*2){ 
				vetX[i]=GA_POPSIZE/3+(int)(Math.random() * (GA_POPSIZE/3));
			} else {
				vetX[i]=GA_POPSIZE/3+GA_POPSIZE/3+(int)(Math.random() * (GA_POPSIZE/3));
			}
		}
		
		while (nodesAux.size()<GA_POPSIZE) {
			i1 = vetX[(int)(Math.random() * (totX/1.5))];
			i2 = vetX[(int)(Math.random() * (totX/1.5))];
			if (Math.random()<GA_RANDOM_NODE_CHANCE) {
				nodesAux.add(generateRandomNode());
			} else {
				generateOffspring(nodesAux,(Node)nodes.elementAt(i1),(Node)nodes.elementAt(i2));
			}
		}
		
		nodes=nodesAux;
	}	
	
	
	public Node mutate(Node node){
		int size=node.size();
		int rand=(int)Math.round(Math.random()*size);
		int i=0;
		mutate(node,i,rand);
		return node;
	}
	
	
	public int mutate(Node node, int i, int rand){
		
		for (int x=0;x<node.getNodesSize();x++){
			Node n=(Node)node.getNode(x);
			if ((!n.isFixo())&& (i==rand)) {
					//String code=n.getCodigo();
					n=mutateNode(n);
					node.setNodeAt(n,x);
					return i;
				//	out("Mutate: old:"+code+" novo:"+n.getCodigo());
				
			}
			i++;
			if (i<rand){
				i=mutate(n,i,rand);
			}
			
		}

		return i;
	}
	
	/*
	 * Esse método "muta" o node passado como parametro por um outro do mesmo tipo.
	 * ie: nó terminal continua sendo terminal e nó função continua sendo função. 
	 */
	public Node mutateNode(Node n){
		String code=n.getCodigo();
		int x=0;
		//int i2=0;
		String newCode=code;
		boolean nodeIsTerminal=false;
		
		if (Math.random()<0.2) {
			return translateNode(terminais.elementAt((int)Math.round(Math.random()*(terminais.size()-1))));
		}
		
		
		//Verifica se o Nó atual é terminal, se for ele muda o Nó para um próximo na lista
		if (n.sizeNode()==0){
			x=(int)(terminais.size()*Math.random());
			//i2=i;
			if (x<0) {
				x=0;
			}
			if (x>terminais.size()-1) {
				x=terminais.size()-1;
			}
				newCode=(String)terminais.elementAt(x);
				nodeIsTerminal=true;
		}
		Node nx=null;
		if (!nodeIsTerminal){
			nx = mutateNodeFunction(n, code);
		} else {
			nx=translateNode(newCode);
		}
		
		//Atribuo o conteúdo do node para o novo node (nx)
		n.copy2Node(nx);
		n.mutateNode();
		

		return nx;
	}
	
	
	private Node mutateNodeFunction(Node n, String code) {
		boolean sameMinNodes=true;
		int countLoop=0;
		//int i2;
		int x;
		Node nx=null;
		String newCode="";
		while (sameMinNodes){
			countLoop++;
			if (countLoop>1000) {
				out("ERRO: loop infinito=>mutatenode "+ countLoop);
			}
			x=(int)(Math.random()*funcoes.size());
			//i2=i;
			newCode=(String)funcoes.elementAt(x);

			//Cria um Nó baseado no código aleatório
			nx=translateNode(newCode);
			
			if (nx==null) {
				out("ERRO: Code:"+code+" newCode:"+newCode);
			}
			if (nx.getMinNodes()<=n.getMinNodes())	{
				sameMinNodes=false;
			}
		}
		return nx;
	}
	
	/*
	 * Cria um novo node baseado no node1 e node2
	 */
	private Node generateOffspring(Vector<Node> nodes,Node node1,Node node2){
		Node node= translateNode(node1.getCodigo());
		boolean ok=false;
		Node xn1=null;
		Node xn2=null;
		int n1=0;
		int n2=0;
		while (!ok) {
			
			n1=(int)(Math.random()*(node1.size()-1))+1;
			n2=(int)(Math.random()*(node2.size()-1))+1;
			
			xn1=node1.getSubNodeAt(new DadoNode(0),n1);
			xn2=node2.getSubNodeAt(new DadoNode(0),n2);
			
			int sz1=xn1.size();
			int sz2=xn2.size();
			if ((node1.size()-sz1+sz2<=MAX_SIZE*2) && (node2.size()-sz2+sz1<=MAX_SIZE*2)) {
				ok=true;
			}
		}
		
		Node sn1=xn1.copyNode();
		Node sn2=xn2.copyNode();
		//out("sn1: "+sn1+" sn2: "+sn2);
		
		node=node1.copyNode();
		node.setSubNodeAt(sn2,new DadoNode(0),n1);
		nodes.add(node);
		if (Math.random()<GA_MUTATIONRATE) {
			node=mutate(node);
		}
		  
		node=node2.copyNode();
		node.setSubNodeAt(sn1,new DadoNode(0),n2);
		nodes.add(node);
		if (Math.random()<GA_MUTATIONRATE) node=mutate(node);
	
		return node;
	}
	
	
	public Node copyNode(Node node){
		return node.copyNode();
	}
	
	
	public Node translateNode(String s){
		Node n=getNode(s);
		if (n==null){
			return null;
		}
		n.setGP(this);
		return n;
	}
	
	public Node getNode(String s){
		//out("tipo:"+s);
		if (s.startsWith("sensor")){
			//s=s.replaceAll("function:","");
			//out("s:"+s);
			return new NodeTerminal(s);
		}
		if (s.startsWith("atuador")){
			//s=s.replaceAll("function:","");
			//out("s:"+s);
			return new NodeAtuador(this,s);
		}				
		
		if (s.equals("getVar")){
			return new NodeGetVar();
		}
		if (s.equals("setVar")){
			return new NodeSetVar();
		}

		
		if (s.startsWith("var")){
			return (Node)variaveis.elementAt(Integer.parseInt(s.replaceAll("var","")));
		}		
		
		for (int i=0;i<functionSets.size();i++){
			Node n=functionSets.get(i).translateNode(s);
			if (n!=null)return n;
		}
		
		out("Erro não encontrado:"+obj+" s:"+s);
		return null;
	}
	
	public void initNodes() {
		for (int i=0;i<GA_POPSIZE;i++){
			//System.out.println("GP.init("+i+")");
			Node node=generateRandomNode();
			nodes.add(node);
			//out("Size:"+node.size()+" "+node.getValue());
		}
	}
	
	
	private  Node getCodeAtPos(int pos){
		if (pos<terminais.size()){
			return translateNode(terminais.elementAt(pos));
		} else{
			return translateNode(funcoes.elementAt(pos-terminais.size()));
		}

	}
	
	
	private Node generateRandomNode(Node pai,Node paizao,String st){
		
		Node node;
		int x;
		int minNodes=pai.getMinNodes();
		int maxNodes=pai.getMaxNodes();
		int totNodes=(int)(Math.random()*MAX_SIZE_LAYER);
		if (totNodes<minNodes) {
			totNodes=minNodes;
		}
		
		//Adiciona um node aleatório aleatoriamente (hehe)
		/*if ((Math.random()<0.2) && (pai.sizeNode()<maxNodes)) {
			
			//Terminais
			x=(int)Math.round(Math.random()*(terminais.size()-1));
			node=translateNode(terminais.elementAt(x));
			pai.addNode(node);
			totNodes--;
		}*/
		//System.out.println("generateRandomNode");
		
		//Adiciona totNodes aleatorios ao node pai 
		for (int j=0;j<totNodes;j++){
			if ((paizao.size()<MAX_SIZE) && (pai.sizeNode()<maxNodes)) {
				if (Math.random()<0.5) {
					//Terminais
					x=(int)Math.round(Math.random()*(terminais.size()-1));
					node=translateNode(terminais.elementAt(x));
					pai.addNode(node,(int)Math.round(pai.getNodesSize()*Math.random()));
			//		out(st+">T "+(String)(terminais.elementAt(x)));
				} else {
					//Funções
					x=(int)Math.round(Math.random()*(funcoes.size()-1));
					node=translateNode(funcoes.elementAt(x));
					pai.addNode(node,(int)Math.round(pai.getNodesSize()*Math.random()));
				//	out(st+">F "+(String)(funcoes.elementAt(x)));
					generateRandomNode(node,paizao,st+"=");
				//	out(funcoes.elementAt(x)+" v:"+node.getValue());
					
				}
			}
		}
		
		
		if (pai.sizeNode()<minNodes) {
			for (int j=pai.sizeNode();j<(minNodes);j++){
				//Terminais
				x=(int)Math.round(Math.random()*(terminais.size()-1));
				node=translateNode((String)(terminais.elementAt(x)));
				pai.addNode(node,(int)Math.round(pai.getNodesSize()*Math.random()));
			}
		}

		return pai;
	}
	
	
	/*
	 * Cria um node aleatório a partir de uma função aleatoria
	 */
	public Node generateRandomNode(){
		int p=(int)(funcoes.size()*Math.random());
		
		Node pai=translateNode(funcoes.get(p));		
		
		generateRandomNode(pai,pai,"=");
		
		/*int i=0;
		while (i<maxSize){
			for (int j=0;j<Math.random()*maxSizeLayer;j++){
				int x=(int)Math.round(Math.random()*(terminais.size()+funcoes.size()-1));
				out("x:"+x+" "+(terminais.size()+funcoes.size())+" "+Math.random());
				pai.addNode(getSetItem(x));
				i++;
			}
		}*/
		return pai;
	}

	
	public Node getNode(int i) {
		return nodes.get(i);
	}

	
	public Objeto getObj() {
		return obj;
	}
	public void setObj(Objeto obj) {
		this.obj = obj;
	}
	public NodeVariavel getVariavel(int i) {
		return variaveis.get(i);
	}
	public Vector<String> getCodigoFonte() {
		return codigoFonte;
	}
	public SortMethod getSortMethod() {
		return sortMethod;
	}
	public void setSortMethod(SortMethod sortMethod) {
		this.sortMethod = sortMethod;
	}
	
	


}
