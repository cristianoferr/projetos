/*
 * Created on 28/10/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node;


import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import comum.Objeto;

import cristiano.GenProg.GeneticProgramming;





/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class Node extends Objeto {
	private Vector<Node> nodes=new Vector<Node>();
private double fitness=0;
private int minNodes=1,maxNodes=100;
private ParameterNode param=null;
private boolean fixo=false;
private String codigo="";
private boolean isDebugging=false;
public static int count=0;
private GeneticProgramming gp;
private  int id=0; 
private  String comma="+";
private int ly=-1000;
private int lv=-1000;
private int lx=-1000;
  
  public void setFixo(boolean fixo) {
		this.fixo = fixo;
	}

	public boolean isFixo() {
		return fixo;
	}
  
	/**
	 * 
	 */
	public Node() {
		super();
		count++;
		id=count;
	}
	
	  public void setMaxNodes(int maxNodes) {
			this.maxNodes = maxNodes;
		}

		public int getMaxNodes() {
			return maxNodes;
		}

	
	public void setParamNodes(){
		for (int i=0;i<getNodes().size();i++){
			if (param!=null)((Node)getNodes().elementAt(i)).setParam(param.getClone());
			//debug("i:"+i+" x:"+x);
		}
		
		if (getNodes().size()<minNodes){
			System.out.println("ERRO: só 1 param:"+getCodigo());
			int x=1/0;
		}
	}
	
	public String toString(){
		return codigo;
	}
	
	public void drawGraph(Graphics g,int x,int y,int resx,int resy){
		if ((ly==-1000) && (lv==-1000)){
			ly=resy;
			lv=(int)getValue();
			lx=resx;
		}
		int zoom=4;
		g.setColor(Color.BLUE);
		
		g.drawLine(x+lx/zoom,y+ly/zoom,x+resx/zoom,y+resy/zoom);
		if (resx%10==0)	{
		//	g.drawString(""+(int)resy,x+resx/zoom+20,y+resy/zoom);
		}
		
		g.setColor(Color.RED);
		g.drawLine(x+lx/zoom,y+lv/zoom,(int)x+resx/zoom,(int)(y+getValue()/zoom));
		//g.drawOval(x+resx,y+(int)getValue(),2,2);
		if (resx%30==0)	{
		//	g.drawString(""+(int)getValue(),x+resx/zoom+20,y+(int)getValue()/zoom);
		}
		g.setColor(Color.BLACK);
		ly=resy;
		lx=resx;
		lv=(int)getValue();
	}
	
	public void drawImage(Graphics g,int x,int y){
		drawNode(g,x,y);
		
/*		g.drawLine(10,10,20,20);
		for (int i=0;i<maxComponentX;i++){
			for (int j=0;j<maxComponentY;j++){
				Image image = world.getApplet().createImage (size,size);
				
				g.drawImage (comps[i][j][0].getFeed(image,size), size*i, size*j, world.getApplet());
			}
	
		}*/
		

	  	
	}
	
	public Node getSide(Node n,int side){
		int max=getNodesSize()/2;
		for (int i=0;i<getNodesSize();i++){
			if ((side==1)&&(i<max)){
				n.addNode(((Node)getNodes().elementAt(i)).copyNode());
			}
			if ((side==2)&&(i>=max)){
				n.addNode(((Node)getNodes().elementAt(i)).copyNode());
			}
		}
		return n;
	}
	public Node getLeft(Node n){
		return getSide(n,1);
	}
	public Node getRight(Node n){
		return getSide(n,2);
	}	
	
	private void drawNode(Graphics g,int x,int y) {
		int size=16;
		g.setColor(Color.BLACK);
		g.drawOval(x,y,size,size);
		
		g.drawString(codigo,x+5,y+13);
		double d=(double)Math.round(getValue()*100)/(double)100;
		//g.drawString(Formatacao.format(d),x+25,y+18);
		int dist=50;
		int xx=x-dist*(getNodesSize()-1)/2;
		//if (getNodesSize())
		
		for (int i=0;i<getNodesSize();i++){
			Node n=(Node)getNodes().elementAt(i);
			g.drawLine(x+size/2,y+size,xx+size/2,y+dist);
			
			n.drawNode(g,xx,y+dist);
			xx+=dist;
		}
		
	}	
	public Node copyNode(){
//		Node n=copyNode(new Node());
		return null;
	}
	
	public void clear(){
		getNodes().clear();
	}
	public void mutateNode(){
		
	}
	/*
	 * Atribuo todos os valores do nó atual para o node n.
	 */
	public Node copy2Node(Node n){
		
		n.setFitness(getFitness());
//		n.refersTo=refersTo;
		count++;
		n.setGP(getGP());
		n.id=count;
		//n.id=id;
		//n.setCodigo(codigo);
		n.setParam(getParam());
		for (int i=0;i<getNodes().size();i++){
			Node ni=(Node)getNodes().elementAt(i);
			n.addNode(ni.copyNode());
		}
		return n;
	}
	
	public int getNodesSize(){
		return getNodes().size();
	}
	public Node getNode(int i){
		return (Node)getNodes().elementAt(i);
	}
	public Node getSubNodeAt(DadoNode x,int dest){
		if (x.getInt()==dest) {
			return this;
		}
		for (int i=0;i<getNodes().size();i++){
			x.setInt(x.getInt()+1);
			Node ni=(Node)getNodes().elementAt(i);
			Node n=ni.getSubNodeAt(x,dest);
			if (n!=null){
				return n;
			}
		}
		return null;
	}
	
	public boolean containsCode(String code){
		for (int i=0;i<getNodes().size();i++){
			Node ni=(Node)getNodes().elementAt(i);
			if (ni.getCodigo().equals(code)) {
				return true;
			}
			if (ni.containsCode(code)){
				return true;
			}
		}
		return false;
	}
	public void setSubNodeAt(Node node,DadoNode x,int dest){
		for (int i=0;i<getNodes().size();i++){
			x.setInt(x.getInt()+1);
			if (x.getInt()==dest) {
				getNodes().setElementAt(node,i);
			} else{ 
				Node ni=(Node)getNodes().elementAt(i);
				ni.setSubNodeAt(node,x,dest);
			}
		}

	}	
	public void setNodeAt(Node n,int i){
		if (n!=null){
			getNodes().setElementAt(n,i);
		} else {
			if (getNodes().size()>1)  getNodes().removeElementAt(i);
		}
	}

	public int sizeNode(){
		return getNodes().size();
	}
	
	/*
	 * Retorna o tamanho do node total, contando os filhos
	 */
	public int size(){
		int x=getNodes().size();
		for (int i=0;i<getNodes().size();i++){
			x+=((Node)getNodes().elementAt(i)).size();
		}
		return x;
	}

	public String getID(){
		if (getNodes().size()>0) {
			return "node"+id;
		} else {
			return getCodigo();
		}
	}
	
	public String getCodigoFonte(){
		String s="node"+id+" = ";
		if (getNodes().size()>0) {
			s=s+codigo+"(";
			for (int i=0;i<getNodes().size();i++){
				getGP().getCodigoFonte().add(((Node)getNodes().elementAt(i)).getCodigoFonte());
				s=s+((Node)getNodes().elementAt(i)).getID()+getComma();
			}
			//s=s+")";
			s=s.substring(0,s.length()-1);
			s=s+");";
		} else {
			//s=s+getCodigo()+";";
			s="";
		}
		return s;
		
	}	
	
	public double getValue(){
		double x=0;
		//for (int i=0;i<nodes.size();i++){
		//	x=x+((Node)nodes.elementAt(i)).getValue();
		//	//debug("i:"+i+" x:"+x);
		//}		
	//	if (refersTo!=null){
		  //Sensores
	//	  if (refersTo.totOutputs>0) return refersTo.getFeed();
		  
		  //Atuadores
//		  Double d=(Double)refersTo.getInputs().elementAt(0);
		  
//		}
		
		
		
		return x;
	}
	
	public void addNode(Node node){
		getNodes().add(node);
	}
	public void addNode(Node node,int p){
		getNodes().add(p,node);
	}	
/**
 * @return
 */
public double getFitness() {
	return fitness;
}

/**
 * @return
 */
/*public Component getRefersTo() {
	return refersTo;
}*/

/**
 * @param i
 */
public void setFitness(double i) {
	if (isDebugging){
		out("setFitness:"+i+" old:"+getFitness());
	}
	fitness = i;
}

/**
 * @param component
 */
/*public void setRefersTo(Component component) {
	refersTo = component;
}*/
public String getCodigo() {
	return codigo;
}
public void setCodigo(String codigo) {
	this.codigo = codigo;
}

public boolean isDebugging() {
	return isDebugging;
}

public void setDebugging(boolean isDebugging) {
	this.isDebugging = isDebugging;
}


public  double getFunction(String function){
	if (isNumber(function)) return Double.parseDouble(function);
	if (function.equals("rand")) return Math.random(); 
	if (function.equals("pi")) return Math.PI;
	return -1;
}
public double setFunction(String function,double value){
	return value;
}


public int getMinNodes() {
	return minNodes;
}

public void setMinNodes(int minNodes) {
	this.minNodes = minNodes;
}

public ParameterNode getParam() {
	return param;
}

public void setParam(ParameterNode param) {
	this.param = param;
}

public void setComma(String comma) {
	this.comma = comma;
}

public String getComma() {
	return comma;
}

public void setNodes(Vector<Node> nodes) {
	this.nodes = nodes;
}

public Vector<Node> getNodes() {
	return nodes;
}

public void setGP(GeneticProgramming gp) {
this.gp=gp;
	
}



public GeneticProgramming getGP() {
	return gp;
}
}
