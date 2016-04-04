package com.cristiano.cyclone.utils;

import java.util.Vector;

public class MathUtils {
	public static final int tpDec=0;
	public static final int tpHex=1;
	public static final int tpBin=2;
	public static final int tpChr=3;
	public static final int tpIram=4;
	public static final int tpLabel=5;
	public static final int tpOperator=6;
	public static final int tpFunction=7;
/*
	public static Vector int2bin(BytePos valor){
		 return int2bin(valor.value(),valor.getBits());
	}*/
	
	public static double Round(double Rval, int Rpl) {
		double p = (double)Math.pow(10,Rpl);
		  Rval = Rval * p;
		  double tmp = Math.round(Rval);
		  return (double)tmp/p;
		    }
	
	
	public static String isHex(String p1){
		
		if ((p1.charAt(p1.length()-1)=='H')) {
			p1=p1.replaceFirst("H","");
			return hex2Byte(p1)+"";
		}
		return p1;
	}	
	public static String int2hex(int num){
		   String s=Integer.toHexString(num);
		   if (s.length()==1) s="0"+s;
			return s.toUpperCase();
		}
		public static int hex2Byte(String s) {
				int result = 0;
				for (int i = 0; i < s.length(); i++) {
					result += Math.pow(16, i) * hex2Int(s.charAt(s.length() - 1 - i));
				}
				return result;
			}
//		converts the hex base to integer values.
		  public static int hex2Int(char c) {
			  if (c == '0')
				  return 0;
			  if (c == '1')
				  return 1;
			  if (c == '2')
				  return 2;
			  if (c == '3')
				  return 3;
			  if (c == '4')
				  return 4;
			  if (c == '5')
				  return 5;
			  if (c == '6')
				  return 6;
			  if (c == '7')
				  return 7;
			  if (c == '8')
				  return 8;
			  if (c == '9')
				  return 9;
			  if (c == 'a' || c == 'A')
				  return 10;
			  if (c == 'b' || c == 'B')
				  return 11;
			  if (c == 'c' || c == 'C')
				  return 12;
			  if (c == 'd' || c == 'D')
				  return 13;
			  if (c == 'e' || c == 'E')
				  return 14;
			  if (c == 'f' || c == 'F')
				  return 15;

			  System.out.println("should not occur! " + c);

			  return -1;

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
			/*for (int i=vet.size()-1;i>casas;i--){
				out("i:"+i);
				vet.remove(i);
			}*/
			//out(vet.size()+" 1-> "+casas);
			while (vet.size()>casas){	
					vet.remove(0);
			}
			//out(vet.size()+" 2-> "+casas);
			
		//	System.out.println("valor:"+valor+" vet:"+vet);
			return vet;
		}
			public static int bin2int(String bin){
//	   the 2 specifies the base to convert from.  2 is binary, 16 would be hex
    char ch[]=bin.toCharArray();
	for (int i=0;i<ch.length;i++){
		if (ch[i]>'1') ch[i]='1';
	}
	bin=String.valueOf(ch);
	return Integer.valueOf(bin, 2).intValue();
	}
public static String vector2string(Vector v){
			String s="";
			for (int i=0;i<v.size();i++){
				Integer d=(Integer)v.elementAt(i);
				s=s+(int)d.intValue();
			}
		//out("v2s:"+s);
			return s;
		}		
}
