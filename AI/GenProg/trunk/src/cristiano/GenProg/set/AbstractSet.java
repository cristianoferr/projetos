package cristiano.GenProg.set;

import java.util.Vector;

import comum.Objeto;

import cristiano.GenProg.GeneticProgramming;
import cristiano.GenProg.Node.Node;

public abstract class AbstractSet extends Objeto{
    private Vector<String> terminais=new Vector<String>();
    private Vector<String> funcoes=new Vector<String>();
    private GeneticProgramming gp;
    
    public void addFuncao(String f){
    	funcoes.add(f);
    }
    
    public void addTerminal(String f){
    	terminais.add(f);
    }
    
    public void initLoad(GeneticProgramming gp){
    	this.gp=gp;
    	for (int i=0;i<funcoes.size();i++){
    		gp.addFuncao(funcoes.get(i));
    	}
    	
    	for (int i=0;i<terminais.size();i++){
    		gp.addTerminal(terminais.get(i));
    	}
    }
	
    
    public Node translateNode(String s){
    	return null;
    }

	public GeneticProgramming getGP() {
		return gp;
	}
}
