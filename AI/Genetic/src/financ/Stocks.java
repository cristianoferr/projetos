package financ;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import comum.Objeto;

/*
 * Created on Jun 18, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class Stocks extends Objeto {
public Vector open=new Vector();
public Vector close=new Vector();
public Vector high=new Vector();
public Vector low=new Vector();
public Vector volume=new Vector();
public Vector difOpenClose=new Vector();
public Vector difHighLow=new Vector();
public String acao="";

public static Connection conn;
	
int dia=0;

	public int getDia() {
	return dia;
}
public void setDia(int dia) {
	this.dia = dia;
}
	public Stocks() {
		super();
		url="jdbc:odbc:financ";
		initConn();  
		load("USIM5");

		
	}
	public Stocks(String acao) {
		super();
		this.acao=acao;
		url="jdbc:odbc:financ";
		initConn();  
		load(acao);
	}	
	private void initConn() {
		if (conn!=null) return;
		try {
			
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection(url,"","");
			
	}		
	catch (Exception e) {  
		System.err.println("Erro de SQL: ");  
		System.err.println(e.getMessage());  
	}
	}
	public void load(String acao){
		clear();
		out("Loading Stock: "+acao);
		try	{
			
			PreparedStatement ps=conn.prepareStatement("select * from stocks where acao='"+acao+"' order by seq");
			ResultSet rs=ps.executeQuery();
			
			while (rs.next()){
				double o=rs.getDouble("open");
				double c=rs.getDouble("close");
				double h=rs.getDouble("high");
				double l=rs.getDouble("low");
				double v=rs.getDouble("volume");
				open.add(new Double(o));
				close.add(new Double(c));
				high.add(new Double(h));
				low.add(new Double(l));
				volume.add(new Double(v));
				difOpenClose.add(new Double(c-o));
				difHighLow.add(new Double(h-l));
				
 
			}
		}
		catch (Exception e) {  
			System.err.println("Erro de SQL: \n");  
			System.err.println(e.getMessage()+"\n");
			e.printStackTrace();  
		}  

	}
	
public void loadRandom(){
	
	if (Math.random()<0.5) {
		load("ACES4");
	} else {
		load("USIM5");
	}
		
}
public void clear(){
	open.clear();
	close.clear();
	high.clear();
	low.clear();
	volume.clear();	
}
public int maxDias(){
	return open.size();
}
public double getOpen(int d){
	if (d<0) return -1;
	d=getDia()-d;
	if (d<0) return -1;
	Double v=(Double)open.elementAt(d);
	return v.doubleValue(); 
}
public double getNextDayPrice(){
	int d=getDia()+1;
	if (d>=maxDias()) d=getDia();
	//if (d==0) out("d:"+d+" "+open.size()+" "+close.size()+" "+volume.size()+" "+acao);
	Double v=(Double)open.elementAt(d);
	return v.doubleValue(); 
}


public double getDifOpenClose(int d){
	if (d<0) return -1;
	d=getDia()-d;
	if (d<0) return -1;
	Double v=(Double)difOpenClose.elementAt(d);
	return v.doubleValue(); 
}
public double getDifHighLow(int d){
	if (d<0) return -1;
	d=getDia()-d;
	if (d<0) return -1;
	Double v=(Double)difHighLow.elementAt(d);
	return v.doubleValue(); 
}
public double getClose(int d){
	if (d<0) return -1;
	d=getDia()-d;
	if (d<0) return -1;
	Double v=(Double)close.elementAt(d);
	return v.doubleValue(); 
}

public double getHigh(int d){
	if (d<0) return -1;
	d=getDia()-d;
	if (d<0) return -1;
	Double v=(Double)high.elementAt(d);
	return v.doubleValue(); 
}
public double getLow(int d){
	if (d<0) return -1;
	d=getDia()-d;
	if (d<0) return -1;
	Double v=(Double)low.elementAt(d);
	return v.doubleValue(); 
}

public double getVolume(int d){
	if (d<0) return -1;
	d=getDia()-d;
	if (d<0) return -1;
	Double v=(Double)volume.elementAt(d);
	return v.doubleValue(); 
}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
