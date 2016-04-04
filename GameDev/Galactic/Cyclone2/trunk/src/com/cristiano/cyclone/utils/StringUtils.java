package com.cristiano.cyclone.utils;

import java.util.ArrayList;
import java.util.StringTokenizer;

public abstract class StringUtils {

	
	public static ArrayList<String> tokenize(String opers,String str,boolean returnDelim){  

		StringTokenizer st = new StringTokenizer(str,opers,returnDelim); 

		//instancia um ArrayList para guardar as Strings 
		ArrayList<String> list = new ArrayList<String>(); 

		//Transfere os dados da StringTokenizer para o ArrayList 
		while (st.hasMoreTokens()) 
		list.add(st.nextToken());	

		//Imprime aleatorio usando o indice 

		return list;
		} 
}
