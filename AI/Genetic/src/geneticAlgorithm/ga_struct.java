package geneticAlgorithm;

import java.util.Vector;


import comum.BytePos;
import comum.Objeto;

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
public class ga_struct extends Objeto {
Vector str=new Vector();
int fitness=0; 
int showIn=tpHex;

	/**
	 * 
	 */
	public ga_struct() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ga_struct(ga_struct st) {
		super();
		str=st.str;
		fitness=st.fitness;
		
	}

public int getCRC(){
	int crc=0;
	for (int i=0;i<str.size();i++){
			crc=crc+((BytePos)str.elementAt(i)).value();
		}
	return crc;
}

public String toString(){
	String s="";
	for (int i=0;i<str.size();i++){
		if (showIn==tpHex)
			s=s+((BytePos)str.elementAt(i)).getHex();
		if (showIn==tpChr)
			s=s+((BytePos)str.elementAt(i)).getChar();
	}
		return s;	
}

public void initialize(int size,int min,int max){
	String s="";
	for (int i=0;i<size;i++){
		int rand=min+(int)(Math.random()*(max-min));
		str.add(new BytePos(rand));
		s=s+int2hex(rand);
	}
	//out("sinit:"+s);
	//printVector();
}
/*public void initialize(int size,Vector memory){
	String s="";
	for (int i=0;i<size;i++){
		int rand=(int)(Math.random()*256);
		str.add(new BytePos(rand));
		s=s+int2hex(rand);
	}
	for (int i=size;i<memory.size();i++)
	  str.add(memory.elementAt(i));
	//out("sinit:"+s);
	//printVector();
}*/
/**
 * @return
 */
public int getFitness() {
	return fitness;
}

/**
 * @return
 */
public Vector getStr() {
	return str;
}

/**
 * @param i
 */
public void setFitness(int i) {
	fitness = i;
}

/**
 * @param string
 */
public void setStr(Vector string) {
	str = string;
}

}
