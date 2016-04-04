/*
 * Created on 15/03/2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package cristiano.GenProg.Node;

/*
 * Created on 10/08/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.util.Date;

import comum.Objeto;


/**
 * @author cmm4
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DadoNode extends Objeto{
Object dado=new Object();
String tipo="";

	/**
	 * 
	 */

	public DadoNode(String nome,String d) {
		super();
		dado=d;
		tipo=dado.getClass().getName();
	}
	public DadoNode(String nome,Date d) {
		super();
		dado=d;
		tipo=dado.getClass().getName();
	}			

	public DadoNode(String nome,int d) {
		super();
		dado=new Integer(d);
		tipo=dado.getClass().getName();
		}		
	public DadoNode(int d) {
		super();
		dado=new Integer(d);
		tipo=dado.getClass().getName();
		}	
	public DadoNode(String d) {
		super();
		dado=new String(d);
		tipo=dado.getClass().getName();
		}		
	public DadoNode(String nome,double d) {
			super();
			dado=new Double(d);
		tipo=dado.getClass().getName();
		}			

/**
 * @return
 */

public String getTipo(){
	return tipo;
}


/**
 * @return
 */
public Object getDado() {
  return dado;	
}

/**
 * @return
 */
public String getString() {
  return dado.toString();	
}
public int getInt(){
	if (tipo.indexOf("String")>-1) {
		  return Integer.parseInt((String)dado);
		 }
		if (tipo.indexOf("Double")>-1) {
			return (int)((Double)dado).doubleValue();
		 }  
		if (tipo.indexOf("Integer")>-1) {
			return ((Integer)dado).intValue();
		 }  	
	return -1;
}
public double getDouble(){
	if (tipo.indexOf("String")>-1) {
		  return Double.parseDouble((String)dado);
		 }
		if (tipo.indexOf("Double")>-1) {
			return ((Double)dado).doubleValue();
		 }  
		if (tipo.indexOf("Integer")>-1) {
			return ((Integer)dado).intValue();
		 }  
	return -1;
}
/**
 * @param string
 */
public void setString(String d) {
	if (tipo.indexOf("String")>-1) {
  	  dado=new String(d);
  	 }
	if (tipo.indexOf("Double")>-1) {
		dado=new Double(d);
	 }  
	if (tipo.indexOf("Integer")>-1) {
		dado=new Integer(d);
	 }  
}
public void setInt(int d) {
	if (tipo.indexOf("String")>-1) {
	  dado=new String(String.valueOf(d));
	 }
	if (tipo.indexOf("Double")>-1) {
		dado=new Double(d);
	 }  
	if (tipo.indexOf("Integer")>-1) {
		dado=new Integer(d);
	 }  
}
public void setDouble(double d) {
	if (tipo.indexOf("String")>-1) {
	  dado=new String(String.valueOf(d));
	 }
	if (tipo.indexOf("Double")>-1) {
		dado=new Double(d);
	 }  
	if (tipo.indexOf("Integer")>-1) {
		dado=new Integer((int)d);
	 }  
}

public String toString(){
	return getString();
}
/**
 * @return
 */

}

