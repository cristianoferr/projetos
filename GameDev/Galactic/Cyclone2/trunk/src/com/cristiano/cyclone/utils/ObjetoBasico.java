package com.cristiano.cyclone.utils;

import java.text.NumberFormat;

public class ObjetoBasico implements Cloneable  {
public static final int tpString=1;
public static final int tpInteger=2;
static NumberFormat nf = NumberFormat.getCurrencyInstance();
	/**
	 * @param args
	 */

	public Object clone() 
	  { 
	    try {
		  return super.clone(); 
	    } catch (CloneNotSupportedException e) { // Dire trouble!!!
	         throw new InternalError("But we are Cloneable!!!");
	    }
	  }
	
	
	
	public static boolean isNumber( String input )  
	{  
	   try  
	   {  
	      Float.parseFloat( input );  
	      return true;  
	   }  
	   catch( Exception e)  
	   {  
	      return false;  
	   }  
	} 
	
	public static String formatCurrency(double v){
		return nf.format(v).replace("R$ ", "");
	}
	
	public static void out(String s){
		System.out.println(s);
	}	
	public static double arredonda(double num){
//		double r=num*100;
//		double res=(double)(((int)r)/100f);
//		out("num:"+num+" r:"+r+" round:"+Math.round(r)+" result:"+res);
		return num;
	}
	
	


	
}
