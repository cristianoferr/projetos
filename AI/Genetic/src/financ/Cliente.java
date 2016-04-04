package financ;
import comum.Objeto;

/*
 * Created on Jun 18, 2007
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class Cliente extends Objeto {
double saldo=0;
double saldoInicial=100000;
double qtdStocks=0;
double fitness=0;
int status=0; //0=pode comprar; 1=pode vender
int acaoFeita=0;
int acaoFeitaDia=0;
boolean compraFeita=false;
boolean vendaFeita=false;
final double custoOp=20;
boolean log=false;
int negFit=-1000;
double movDia=0;
double movAnt=0;
public static Stocks stocks=null;

public static Stocks getStocks() {
	return stocks;
}

public static void setStocks(Stocks stocks) {
	Cliente.stocks = stocks;
}

	public Cliente() {
		super();
		// TODO Auto-generated constructor stub
		if (stocks==null){
			out("OOPz");
			stocks=new Stocks();
		}
		reset();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public double getFunction(String function){
		//out("function:"+function);
		if (function.equals("qtdAcoes")) {return qtdStocks;}
		if (function.equals("saldo")) {return getSaldo();}
		if (function.equals("maxAcoes")) {return (saldo/stocks.getClose(0)*0.9);}
		return super.getFunction(function);
		
	}
	public double getCredito(){
		return getSaldo()+getQtdStocks()*stocks.getNextDayPrice();
	}
	public double getFitness(){
		return fitness;
	}
	public void showLog(boolean l){
		log=l;
	}
	public void inicioDia(){
		acaoFeitaDia=0;
		movAnt=movDia;
		movDia=0;
	}
	public void fimDia(){
		
		if (movDia!=0) saldo-=custoOp;
		if ((movDia>0) && (movAnt>0)) fitness-=negFit/10;
		if ((movDia<0) && (movAnt<0)) fitness-=negFit/10;
		//fitness-=custoOp*10;
		if ((movDia!=0)&&(log)) out("Dia:"+stocks.getDia()+" Movimentação:"+movDia+" Saldo:"+Math.round(getSaldo())+" Credito:"+Math.round(getCredito())+" Stocks:"+Math.round(getQtdStocks()));
	}
	
	public double getFit(){
		double d=0;
		double e=negFit*acaoFeita/1000;
		e=0;
		if (acaoFeita==0) e=negFit*1000;
		double c=negFit;
		if (compraFeita) c=10000;
		double v=negFit;
		if (vendaFeita) v=10000;
		d=negFit;
		if (saldo==saldoInicial) d=d*10000;
		if (saldo>saldoInicial) d=100000;
		return (saldo-saldoInicial)*100+getQtdStocks()*stocks.getNextDayPrice()+fitness+d+c+v+e;
	}
	public void reset(){
		saldo=saldoInicial;
		stocks.setDia(0);
		fitness=0;
		acaoFeita=0;
		compraFeita=false;
		vendaFeita=false;
		acaoFeitaDia=0;

	}
	public boolean buy(double vc){
		
		
		if ((vc<0)||(vc*stocks.getClose(0)>saldo)) {
			fitness-=Math.abs(vc);
			return false;
		}
		compraFeita=true;
		if (vc==0) return false;
		qtdStocks+=vc;
		
		saldo-=vc*stocks.getNextDayPrice();
		acaoFeita++;
		acaoFeitaDia++;
		movDia+=vc;
		//if (log) out("Dia:"+stocks.getDia()+" Buy:"+vc+" Credito:"+getCredito()+" Stocks:"+getQtdStocks());
		//status=1;
		fitness+=vc*10;
		return true;
	}
	public boolean sell(double vc){
		
		if ((vc<=0)||(vc>qtdStocks)) {
			fitness-=Math.abs(vc);
			return false;
		}
		vendaFeita=true;
		if (vc==0) return false;
	//	log+="\nSell:"+vc;
		qtdStocks-=vc;
		
		saldo+=vc*stocks.getNextDayPrice();
		acaoFeita++;
		acaoFeitaDia++;
		movDia-=vc;
		//if (log) out("Dia:"+stocks.getDia()+" Sell:"+vc+" Credito:"+getCredito()+" Stocks:"+getQtdStocks());
		fitness+=vc*10;
		return true;
	}	
	public void teste(){
	out("Inicio Teste");
	out("Saldo:"+getSaldo());
	out("Stocks:"+getQtdStocks());
	out("Fitness:"+fitness);
	out("op.Buy 100:"+buy(100));
	out("Saldo:"+getSaldo());
	out("Stocks:"+getQtdStocks());
	out("Fitness:"+fitness);
	out("op.Buy 100:"+buy(100));
	out("Saldo:"+getSaldo());
	out("Stocks:"+getQtdStocks());
	out("Fitness:"+fitness);
	out("op.Buy 10000:"+buy(10000));
	out("Saldo:"+getSaldo());
	out("Stocks:"+getQtdStocks());
	out("Fitness:"+fitness);
	out("op.Sell 100:"+sell(100));
	out("Saldo:"+getSaldo());
	out("Stocks:"+getQtdStocks());
	out("Fitness:"+fitness);
	out("op.Sell 100:"+sell(100));
	out("Saldo:"+getSaldo());
	out("Stocks:"+getQtdStocks());
	out("Fitness:"+fitness);
	out("op.Sell 10000:"+sell(10000));
	out("Saldo:"+getSaldo());
	out("Stocks:"+getQtdStocks());
	out("Fitness:"+fitness);
	
	out("Fim Teste");
	}
	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public double getQtdStocks() {
		return qtdStocks;
	}

	public void setQtdStocks(double qtdStocks) {
		this.qtdStocks = qtdStocks;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

}
