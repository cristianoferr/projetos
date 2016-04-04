import java.util.Vector;

/*
 * Created on 24/06/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Objeto {
	public static final int totLetras=28;
	
public static void out(String s){
	System.out.println(s);
}
public void debug(String s){
	System.out.println(s);
}

public static String vector2string(Vector v){
	String s="";
	for (int i=0;i<v.size();i++){
		s=s+v.elementAt(i)+";";
	}
	s=s.substring(0,s.length()-1);
	return s;
}
	
public static double double2char(double d){
	//System.out.println("d:"+d+" "+d*255);
	//0.035714
	double dif=(double)1/totLetras;
	double e=dif;
	for (int i=0;i<=totLetras;i++){
		if ((d>=i*dif-dif/2) && (d<=i*dif+dif/2)) {
			e=(double)i*dif;
		//	System.out.println("e="+e);
			} 
	}
	double res=(double)e*totLetras+95;
	return res;		
	}
		
		
public static String double2string(Vector v){
	String s="";
	for (int i=0;i<v.size();i++){
		Double d =(Double)v.elementAt(i);
		s=s+(char)double2char(d.doubleValue());
	}
	return s;		
}	
	
/*
 * This is method will be mainly used for filling the dataTypeIn and dataTypeOut 
 * with constants numbers
 */
	
public static Vector fillVector(double valor,int qtd){
	Vector ret=new Vector();
	for (int i=0;i<qtd;i++){
		ret.add(new Double(valor));
	}
	return ret;
}

public static void abort(int chance){
	if (Math.random()*100<chance){
	  int i=10/0;
	}

}
	
public static Vector int2bin(int valor,int casas){
	Vector vet=new Vector();
	//12->1100
	int d=valor;
	int r=0;
	String s="";
	while (d>0){
		r=d % 2;
		s=r+s;
		d=d/2;
		vet.add(0,new Integer(r));
	//	System.out.println("s:"+s+" r:"+r+" d:"+d);
	}
	for (int i=vet.size();i<casas;i++) {
	  vet.add(0,new Integer(0));
	}
		
//	System.out.println("valor:"+valor+" vet:"+vet);
	return vet;
}
	

}
