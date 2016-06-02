package src;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Oracao oracao=new Oracao("eu quero ser feliz.");
		
		oracao.parser();
		
		System.out.println(" Oracao:"+oracao);
		
		//String s="iriamos";
		//System.out.println("ST:"+s.substring(s.length()-4));
		//s=s.substring(0,s.length()-4);
		//System.out.println("ST2:"+s);
		oracao.sumario();
		//Constantes.descTempo[Constantes.tempoFuturoPresente]+
	}

}
