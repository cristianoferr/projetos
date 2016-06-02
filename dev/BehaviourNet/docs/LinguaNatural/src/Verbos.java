package src;

import java.util.Vector;


public class Verbos extends ObjetoBasico {

	Vector verbos=new Vector();
	public Verbos(){
		addVerbo("cantar");
		addVerbo("acessar");
		//addVerbo("poder");
		addVerbo("escrever");
	}
	/**
	 * @param args
	 */
	public int size(){
		return verbos.size();
	}
	
	public void addVerbo(String v){
		if (!isVerbo(v))
			verbos.add(v);
	}
	
	
	
	public boolean isVerbo(String v){
		for (int c=0;c<verbos.size();c++){
			if (v.equals((String)verbos.elementAt(c)))
				return true;
		}
		return false;
			
	}
}
