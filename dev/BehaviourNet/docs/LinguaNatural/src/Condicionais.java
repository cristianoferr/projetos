package src;

import java.util.Vector;

public class Condicionais extends ObjetoBasico {
Verbos verbos=null;
	public Verbos getVerbos() {
	return verbos;
}
public void setVerbos(Verbos verbos) {
	this.verbos = verbos;
}
	public Condicionais() {
		super();
		// TODO Auto-generated constructor stub
		
		//listaXML();
		
		addSubstantivo();
		addAdjetivo();
		addVerboAux();
		addArtigos();
		addPreposicao();
		addPessoa();
		addPronome();
		addVerbo();
		
		
	}
	private void listaXML() {
		addSubstantivo();
		saidaXML("substantivos");
		addAdjetivo();
		saidaXML("adjetivos");
		addVerboAux();
		saidaXML("verbos_auxiliares");
		addArtigos();
		saidaXML("substantivos");
		addPreposicao();
		saidaXML("preposicoes");
		addPessoa();
		saidaXML("pessoas");
		addPronome();
		saidaXML("pronomes");
		addVerbo();
		saidaXML("verbos");
		
	}
	private void saidaXML(String secao) {
		out("<"+secao+">");
		for (int i=0;i<condicionais.size();i++){
			Condicional c=(Condicional)condicionais.elementAt(i);
			out(c.toString());
		}
		out("</"+secao+">");
		condicionais.clear();
	}
	public static Vector condicionais=new Vector();

	public void checkCondicional(Frase frase){
		
		for (int i=0;i<condicionais.size();i++){
			Condicional c=(Condicional)condicionais.elementAt(i);
			Palavra p = (Palavra)frase.getPalavra();


			// Palavras Iguais
			if (c.getPalavra().equals(p.toString())){
				out("c:"+c.getPalavra());
				if ((c.getPrePalavra().equals("")) || 
						(c.getPrePalavra().equals(frase.getPalavraPrev().toString()))){
					if ((c.getRelacaoVerbo()==0) || ((c.getRelacaoVerbo()==-1) && (frase.getVerbo()==null)) ||((c.getRelacaoVerbo()==1) && (frase.getVerbo()!=null))){
						if ((!c.isEntreSujeitos()) || ((c.isEntreSujeitos()) && (frase.isEntreSujeitos()))){
						
						p.setTipoPalavra(c.getTipoPalavra());
						p.setTipoAdjetivo(c.getTipoAdjetivo());
						frase.setTempo(c.getTempoSujeito());
						frase.setTipoSujeito(c.getTipoSujeito());
						frase.setTipoPessoa(c.getTipoPessoa());
						p.setId(c.getNovaPalavra());
						
						if (c.getTipoPalavra()==Constantes.palVerbo) verbos.addVerbo(p.getId());
						break;
				}
					}
				}
			}
		}
		
		for (int i=0;i<condicionais.size();i++){
			Condicional c=(Condicional)condicionais.elementAt(i);
			Palavra p = (Palavra)frase.getPalavra();

			// Desconjugando verbos
			if (!c.getFimPalavra().equals("")) {
				int tam=c.getFimPalavra().length();
				String s=p.toString();
				if ((s.length()>tam)&& (p.getTipoPalavra()==-1)){
					s=s.substring(0,s.length()-tam)+c.getFimPalavraNovo();
					if ((tam>3) ||(verbos.isVerbo(s))){
						if (p.toString().length()>tam) {
							String aux=p.toString().substring(p.toString().length()-tam);
							if (aux.equals(c.getFimPalavra())){
								out(c.getFimPalavra()+" cond:"+p);
								p.setId(s);
								p.setTipoPalavra(c.getTipoPalavra());
								p.setTipoAdjetivo(c.getTipoAdjetivo());
								frase.setTempo(c.getTempoSujeito());
								frase.setTipoSujeito(c.getTipoSujeito());
								frase.setTipoPessoa(c.getTipoPessoa());
								if (c.getTipoPalavra()==Constantes.palVerbo) verbos.addVerbo(p.getId());
								break;
								
							}
						}
					}
				}
			}

		}
	}
	
	// Alguns Substantivos
	
	private void addSubstantivo(){
		Condicional c=new Condicional();
		c=new Condicional();c.setPalavra("maria");c.setTipoPalavra(Constantes.palSubst);condicionais.add(c);
		c=new Condicional();c.setPalavra("joão");c.setTipoPalavra(Constantes.palSubst);condicionais.add(c);
		c=new Condicional();c.setPalavra("cristiano");c.setTipoPalavra(Constantes.palSubst);condicionais.add(c);
		c=new Condicional();c.setPalavra("sistema");c.setTipoPalavra(Constantes.palSubst);condicionais.add(c);
	}
	
	// Alguns Adjetivos
	private void addAdjetivo(){
		Condicional c=new Condicional();
		
		//Cor
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjCor);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("vermelho");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjCor);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("vermelha");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjCor);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("amarelo");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjCor);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("amarela");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjCor);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("azul");
		condicionais.add(c);
		
		//Forma
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjForma);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("quadrado");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjForma);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("quadrada");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjForma);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("redondo");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjForma);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("redonda");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjForma);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("triangular");
		condicionais.add(c);
		
		// Temperatura
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjTemperatura);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("quente");
		condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjTemperatura);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("frio");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjTemperatura);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("fria");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjTemperatura);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("morno");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjTemperatura);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("morna");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjTemperatura);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("gelado");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjTemperatura);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("gelada");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		// Intensidade
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjIntensidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("forte");
		condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjIntensidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("fraco");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjIntensidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("fraca");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjIntensidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("moderado");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjIntensidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("moderada");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);

		// Adjetivo Proporcao
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjProporcao);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("grande");
		condicionais.add(c);

		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjProporcao);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("medio");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjProporcao);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("media");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjProporcao);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("pequeno");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjProporcao);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("pequena");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjProporcao);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("nanico");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjProporcao);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("nanica");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjProporcao);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("enorme");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		// Adjetivos de Qualidade
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjQualidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("bom");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjQualidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("bem");
		condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjQualidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("boa");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjQualidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("bonito");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjQualidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("amavel");
		condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjQualidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("bonita");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjQualidade);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("agradavel");
		condicionais.add(c);
		
		// Adjetivos de defeito
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjDefeito);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("mau");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjDefeito);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("ma");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjDefeito);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("ruim");
		condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjDefeito);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("feio");
		c.setGeneroSujeito(Constantes.genMasculino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjDefeito);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("feia");
		c.setGeneroSujeito(Constantes.genFeminino);condicionais.add(c);
		
		c=new Condicional();
		c.setTipoAdjetivo(Constantes.adjDefeito);
		c.setTipoPalavra(Constantes.palAdj);
		c.setPalavra("horrivel");
		condicionais.add(c);
		
		
	}
	
	// Verbo
	private void addVerbo(){
		Condicional c=new Condicional();
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("a");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ado");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(-1);	c.setTipoPessoa(-1);	c.setTipoSujeito(-1);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ais");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("am");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("amos");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ando");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(-1);	c.setTipoSujeito(-1);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ar");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ara");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ará");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("aram");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("aramos");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("arao");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("aras");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("arei");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("areis");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("arem");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("aremos");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("aria");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ariam");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ariamos");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("arias");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("arieis");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("armos");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("as");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("asse");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("assem");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("assemos");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("asses");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("aste");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("astes");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ava");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("avam");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("avamos");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("avas");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("e");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ei");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("eis");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("em");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("emos");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("endo");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(-1);	c.setTipoSujeito(-1);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("er");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("era");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("erá");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("eram");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("eramos");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("erao");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("eras");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("erdes");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("erei");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ereis");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("erem");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("eremos");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("eria");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("eriam");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("eriamos");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("erias");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("erieis");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ermos");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("es");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("esse");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("esseis");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("essem");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("essemos");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("esses");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("este");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("estes");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("eu");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("i");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ia");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iam");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iamos");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ias");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ido");	c.setFimPalavraNovo("er");	c.setTempoSujeito(-1);	c.setTipoPessoa(-1);	c.setTipoSujeito(-1);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ieis");	c.setFimPalavraNovo("er");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("imos");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ir");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ira");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iram");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iramos");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("irao");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iras");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("irdes");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("irei");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ireis");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoMQPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("irem");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iremos");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ires");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iria");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iriam");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iriamos");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("irias");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("irieis");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPreterito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("irmos");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoFuturoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("isse");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(0);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("isseis");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("issem");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("issemos");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("isses");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iste");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("istes");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("iu");	c.setFimPalavraNovo("ir");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("o");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPresente);	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);
		c=new Condicional();	c.setTipoPalavra(Constantes.palVerbo);	c.setFimPalavra("ou");	c.setFimPalavraNovo("ar");	c.setTempoSujeito(Constantes.tempoPreteritoPerfeito);	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujSimples);	condicionais.add(c);

	}
	
	//Pronome
	private void addPronome(){
		Condicional c=new Condicional();
		c=new Condicional();	c.setPalavra("e");	c.setEntreSujeitos(true);c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palAdverbio);	c.setGeneroSujeito(-1);	condicionais.add(c);
		
		c=new Condicional();	c.setPalavra("a");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("algo");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("alguem");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("algum");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("alguma");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("algumas");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("alguns");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("aquela");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("aquelas");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("aquele");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("aqueles");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("aqui");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("aquilo");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("as");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("bastante");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("bastantes");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("cada");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("certa");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("certas");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("certo");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("certos");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("comigo");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("como");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeInterrogativo);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("conosco");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("contigo");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("convosco");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("demais");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("ela");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("elas");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("ele");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("eles");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("essa");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("essas");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("esse");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("esses");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("estas");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("este");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("estes");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("eu");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("isso");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("isto");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("lhe");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("lhes");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("mais");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("me");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("menos");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("mesma");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("mesmas");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("mesmo");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("mesmos");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("meu");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("meus");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("mim");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("minha");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("minhas");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("muita");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("muitas");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("muito");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("muitos");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("na");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("nada");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("nenhum");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("nenhuma");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("nenhumas");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("nenhuns");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("ninguem");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(Constantes.genMasculino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("nós");	c.setTipoPessoa(1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("o");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("os");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("outra");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("outras");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("outrem");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("outro");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("outros");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("pouca");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("poucas");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("pouco");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("poucos");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("própria");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("próprias");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("próprio");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("próprios");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("quaisquer");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("qual");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("qualquer");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("quanta");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("quantas");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("quanto");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("quantos");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("que");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("quem");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("se");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("senhor");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("senhora");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("senhoras");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("senhores");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("seu");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("seus");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("si");	c.setTipoPessoa(3);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("sua");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("suas");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tais");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tal");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeDemonstrativo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tanta");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tantas");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tanto");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tantos");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("te");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("teu");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("teus");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("ti");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("toda");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("todas");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("todo");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("todos");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tu");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tua");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tuas");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePossessivo);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("tudo");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("várias");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(-1);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("vários");	c.setTipoPessoa(-1);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomeIndefinido);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("voce");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(Constantes.genFeminino);	condicionais.add(c);	
		c=new Condicional();	c.setPalavra("voces");	c.setTipoPessoa(2);	c.setTipoSujeito(Constantes.sujComposto);	c.setTipoPalavra(Constantes.palPronomePessoal);	c.setGeneroSujeito(-1);	condicionais.add(c);	
	
	}
	
	//Pessoa
	private void addPessoa(){
		Condicional c=new Condicional();
		  c.setPalavra("eu");
		    c.setTipoSujeito(Constantes.sujSimples);
		    c.setTipoPessoa(1);
		    c.setTipoPalavra(Constantes.palPronomePessoal);
		  condicionais.add(c);	
		  
		  c=new Condicional();
		  c.setPalavra("voce");
		    c.setTipoSujeito(Constantes.sujSimples);
		    c.setTipoPessoa(2);
		    c.setTipoPalavra(Constantes.palPronomePessoal);
		  condicionais.add(c);	
		  
		  c=new Condicional();
		  c.setPalavra("tu");
		    c.setTipoSujeito(Constantes.sujSimples);
		    c.setTipoPessoa(2);
		    c.setTipoPalavra(Constantes.palPronomePessoal);
		  condicionais.add(c);	
		  
		  c=new Condicional();
		  c.setPalavra("ele");
		    c.setTipoSujeito(Constantes.sujSimples);
		    c.setTipoPessoa(3);
		    c.setGeneroSujeito(Constantes.genMasculino);
		    c.setTipoPalavra(Constantes.palPronomePessoal);
		  condicionais.add(c);	
		  
		  c=new Condicional();
		  c.setPalavra("ela");
		    c.setTipoSujeito(Constantes.sujSimples);
		    c.setTipoPessoa(3);
		    c.setGeneroSujeito(Constantes.genFeminino);
		    c.setTipoPalavra(Constantes.palPronomePessoal);
		  condicionais.add(c);	
		  
		  c=new Condicional();
		  c.setPalavra("nos");
		    c.setTipoSujeito(Constantes.sujComposto);
		    c.setTipoPessoa(1);
		    c.setTipoPalavra(Constantes.palPronomePessoal);
		  condicionais.add(c);	
		  
		  c=new Condicional();
		  c.setPalavra("vos");
		    c.setTipoSujeito(Constantes.sujComposto);
		    c.setTipoPessoa(2);
		    c.setTipoPalavra(Constantes.palPronomePessoal);
		  condicionais.add(c);	
		  
		  c=new Condicional();
		  c.setPalavra("eles");
		    c.setTipoSujeito(Constantes.sujComposto);
		    c.setTipoPessoa(3);
		    c.setGeneroSujeito(Constantes.genMasculino);
		    c.setTipoPalavra(Constantes.palPronomePessoal);
		  condicionais.add(c);	
		  
		  c=new Condicional();
		  c.setPalavra("elas");
		    c.setTipoSujeito(Constantes.sujComposto);
		    c.setTipoPessoa(3);
		    c.setGeneroSujeito(Constantes.genFeminino);
		    c.setTipoPalavra(Constantes.palPronomePessoal);
		  condicionais.add(c);		
	}
	
	// Preposições
	private void addPreposicao(){
		Condicional c=new Condicional();
		c.setPalavra("ante");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);	c=new Condicional();
		c.setPalavra("após");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		
		c=new Condicional();
		c.setPalavra("até");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		
		c=new Condicional();
		c.setPalavra("com");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		
		c=new Condicional();
		c.setPalavra("contra");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		
		c=new Condicional();
		c.setPalavra("de");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		 
		c=new Condicional();
		c.setPalavra("desde");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		
		c=new Condicional();
		c.setPalavra("em");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		
		c=new Condicional();
		c.setPalavra("entre");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		
		c=new Condicional();
		c=new Condicional();
		c.setPalavra("para");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		c=new Condicional();
		c.setPalavra("per");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		c=new Condicional();
		c.setPalavra("perante");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		c=new Condicional();
		c.setPalavra("por");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		c=new Condicional();
		c.setPalavra("sem");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		c=new Condicional();
		c.setPalavra("sobre");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		c=new Condicional();
		c.setPalavra("trás");
		c.setTipoPalavra(Constantes.palPreposicao);
		condicionais.add(c);
		

	}
	
// Artigos
	
	private void addArtigos(){
		Condicional c=new Condicional();

		c.setPalavra("ela");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setGeneroSujeito(Constantes.genFeminino);
	    c.setTipoPalavra(Constantes.palArtDef);
	condicionais.add(c);c=new Condicional();

	  c.setPalavra("ele");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setGeneroSujeito(Constantes.genMasculino);
	    c.setTipoPalavra(Constantes.palArtDef);
	condicionais.add(c);c=new Condicional();

	   c.setPalavra("um");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setGeneroSujeito(Constantes.genMasculino);
	    c.setTipoPalavra(Constantes.palArtIndef);
	condicionais.add(c);c=new Condicional();

	   c.setPalavra("uma");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setGeneroSujeito(Constantes.genFeminino);
	    c.setTipoPalavra(Constantes.palArtIndef);
	condicionais.add(c);c=new Condicional();

	   c.setPalavra("ela");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setGeneroSujeito(Constantes.genMasculino);
	condicionais.add(c);c=new Condicional();

	  c.setPalavra("elas");
	c.setNovaPalavra("ela");
	    c.setTipoSujeito(Constantes.sujComposto);
	    c.setGeneroSujeito(Constantes.genFeminino);
	condicionais.add(c);c=new Condicional();

	  c.setPalavra("umas");
	c.setNovaPalavra("uma");
	    c.setTipoSujeito(Constantes.sujComposto);
	    c.setGeneroSujeito(Constantes.genFeminino);
	    c.setTipoPalavra(Constantes.palArtIndef);
	condicionais.add(c);c=new Condicional();

	    c.setPalavra("uns");
	c.setNovaPalavra("um");
	    c.setTipoSujeito(Constantes.sujComposto);
	    c.setGeneroSujeito(Constantes.genMasculino);
	    c.setTipoPalavra(Constantes.palArtIndef);
	condicionais.add(c);c=new Condicional();

	 c.setPalavra("eles");
	c.setNovaPalavra("ele");
	    c.setTipoSujeito(Constantes.sujComposto);
	    c.setGeneroSujeito(Constantes.genMasculino);
	condicionais.add(c);c=new Condicional();

	  c.setPalavra("as");
	c.setNovaPalavra("a");
	    c.setTipoSujeito(Constantes.sujComposto);
	    c.setGeneroSujeito(Constantes.genFeminino);
	    c.setTipoPalavra(Constantes.palArtDef);

	condicionais.add(c);
	c=new Condicional();
	  c.setPalavra("os");
	c.setNovaPalavra("o");
	    c.setTipoSujeito(Constantes.sujComposto);
	    c.setGeneroSujeito(Constantes.genMasculino);
	    c.setTipoPalavra(Constantes.palArtDef);
	    condicionais.add(c);
	}
	
//	 Verbos Auxiliares
	private  void addVerboAux(){
		Condicional c=new Condicional();
		c.setPalavra("sou");
		
		c.setTipoPalavra(Constantes.palVerboAux);
		c.setTipoSujeito(Constantes.sujSimples);
		c.setTempoSujeito(Constantes.tempoPresente);
		c.setTipoPessoa(1);
		c.setNovaPalavra("ser");
	    condicionais.add(c);

	    condicionais.add(c);c=new Condicional();
	    c.setPalavra("estou");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	    c.setPalavra("tenho");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	    c.setPalavra("hei");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("há");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	    c.setPalavra("és");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	    c.setPalavra("estas");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tens");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("é");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("está");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tem");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("há");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("somos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("temos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("havemos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("sois");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estais");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tendes");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haveis");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("sao");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estao");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tem");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("hao");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("era");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estava");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tinha");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("havia");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("eras");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estavas");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tinhas");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("havias");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("eramos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estavamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tinhamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haviamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("eram");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estavam");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tinham");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haviam");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("fui");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estive");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tive");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houve");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("foste");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estiveste");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tiveste");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houveste");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("foi");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("esteve");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("teve");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houve");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("fomos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estivemos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tivemos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houvemos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("fostes");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estivestes");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tivestes");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houvestes");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("foram");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estiveram");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tiveram");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houveram");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoPerfeitoSimples);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("fora");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estivera");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tivera");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houvera");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("foras");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estiveras");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tiveras");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houveras");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("foramos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estiveramos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tiveramos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houveramos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("foreis");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estivereis");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tivereis");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houvereis");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("foram");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estiveram");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tiveram");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houveram");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("serei");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estarei");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("terei");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haverei");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(1);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("seras");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estaras");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("teras");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haveras");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(2);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("sera");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estara");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tera");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("havera");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(3);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("seremos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estaremos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("teremos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haveremos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(1);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("sereis");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estareis");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tereis");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("havereis");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(2);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("serao");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estarao");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("terao");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haverao");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPresente);
	          c.setTipoPessoa(3);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("seria");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estaria");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("teria");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haveria");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(1);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("serias");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estarias");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("terias");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haverias");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(2);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("seriamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estariamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("teriamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haveriamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(1);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("seriam");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estariam");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("teriam");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haveriam");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoFuturoPreterito);
	          c.setTipoPessoa(3);
	     condicionais.add(c);c=new Condicional();
	        c.setPrePalavra("ter");
	        c.setPalavra("sido");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTempoSujeito(Constantes.tempoPreteritoComposto);

	        condicionais.add(c);c=new Condicional();
	        c.setPrePalavra("ter");
	        c.setPalavra("estado");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTempoSujeito(Constantes.tempoPreteritoComposto);

	        condicionais.add(c);c=new Condicional();
	        c.setPrePalavra("ter");
	        c.setPalavra("tido");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTempoSujeito(Constantes.tempoPreteritoComposto);

	        condicionais.add(c);c=new Condicional();
	        c.setPrePalavra("ter");
	        c.setPalavra("havido");

	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTempoSujeito(Constantes.tempoPreteritoComposto);

//	    Modo subjuntivo
	    condicionais.add(c);c=new Condicional();
	       c.setPalavra("seja");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("esteja");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tenha");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("haja");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("sejas");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estejas");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tenhas");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("hajas");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("sejamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estejamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);;
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tenhamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("hajamos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(1);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("sejais");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estejais");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);;
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tenhais");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("hajais");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(2);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("sejam");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estejam");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);;
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tenham");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("hajam");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	    c.setTempoSujeito(Constantes.tempoPresente);
	          c.setTipoPessoa(3);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("fosse");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estivesse");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tivesse");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houvesse");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("fosses");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estivesses");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tivesses");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houvesses");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	    c.setTipoSujeito(Constantes.sujSimples);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(2);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("fossemos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estivessemos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tivessemos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houvessemos");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(1);
	     condicionais.add(c);c=new Condicional();
	       c.setPalavra("fossem");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ser");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("estivessem");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("estar");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("tivessem");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("ter");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);

	        condicionais.add(c);c=new Condicional();
	       c.setPalavra("houvessem");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    c.setNovaPalavra("haver");
	          c.setTipoSujeito(Constantes.sujComposto);
	          c.setTempoSujeito(Constantes.tempoPreteritoImperfeito);
	          c.setTipoPessoa(3);
	     
	          condicionais.add(c);c=new Condicional();
	       c.setPrePalavra("se");
	     c.setPalavra("for");
	     c.setNovaPalavra("ser");
	     c.setTipoSujeito(Constantes.sujSimples);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("fores");
	     c.setNovaPalavra("ser");
	     c.setTipoSujeito(Constantes.sujSimples);
	           c.setTipoPessoa(3);

	           condicionais.add(c);c=new Condicional();
	     c.setPalavra("formos");
	         c.setNovaPalavra("ser");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(1);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("forem");
	     c.setNovaPalavra("ser");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(3);
	      condicionais.add(c);c=new Condicional();

	     c.setPalavra("estiver");
	     c.setNovaPalavra("estar");
	     c.setTipoSujeito(Constantes.sujSimples);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("estiveres");
	     c.setNovaPalavra("estar");
	     c.setTipoSujeito(Constantes.sujSimples);
	           c.setTipoPessoa(3);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("estivermos");
	     c.setNovaPalavra("estar");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(1);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("estiverem");
	     c.setNovaPalavra("estar");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(3);
	      condicionais.add(c);c=new Condicional();

	     c.setPalavra("tiver");
	     c.setNovaPalavra("ter");
	     c.setTipoSujeito(Constantes.sujSimples);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("tiveres");
	     c.setNovaPalavra("ter");
	     c.setTipoSujeito(Constantes.sujSimples);
	           c.setTipoPessoa(3);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("tivermos");
	     c.setNovaPalavra("ter");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(1);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("tiverem");
	     c.setNovaPalavra("ter");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(3);
	      condicionais.add(c);c=new Condicional();

	     c.setPalavra("houver");
	     c.setNovaPalavra("haver");
	     c.setTipoSujeito(Constantes.sujSimples);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("houveres");
	     c.setNovaPalavra("haver");
	     c.setTipoSujeito(Constantes.sujSimples);
	           c.setTipoPessoa(3);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("houvermos");
	     c.setNovaPalavra("haver");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(1);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("houverem");
	     c.setNovaPalavra("haver");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(3);

	      condicionais.add(c);c=new Condicional();

	     c.setPalavra("tiver");
	     c.setNovaPalavra("ter");
	     c.setTipoSujeito(Constantes.sujSimples);

	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("tiveres");
	     c.setNovaPalavra("ter");
	     c.setTipoSujeito(Constantes.sujSimples);
	           c.setTipoPessoa(3);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("tivermos");
	     c.setNovaPalavra("ter");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(1);
	      condicionais.add(c);c=new Condicional();
	     c.setPalavra("tiverem");
	     c.setNovaPalavra("ter");
	         c.setTipoSujeito(Constantes.sujComposto);
	         c.setTipoPessoa(3);

	    condicionais.add(c);c=new Condicional();
	     c.setPalavra("ser");
	    c.setTipoPalavra(Constantes.palVerboAux);

	    condicionais.add(c);c=new Condicional();
	     c.setPalavra("haver");
	    c.setTipoPalavra(Constantes.palVerboAux);

	    condicionais.add(c);c=new Condicional();
	     c.setPalavra("ter");
	    c.setTipoPalavra(Constantes.palVerboAux);

	    condicionais.add(c);
	    
	    c=new Condicional();
	     c.setPalavra("estar");
	    c.setTipoPalavra(Constantes.palVerboAux);
	    condicionais.add(c);
	    
	    
	}

}
