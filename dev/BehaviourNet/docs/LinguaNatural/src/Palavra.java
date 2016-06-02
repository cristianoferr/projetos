package src;


public class Palavra extends ObjetoBasico {
String id;
int tipoPalavra=-1;
int tipoAdjetivo=-1;
	public Palavra(String p){
		id=p;
	}
	
	public boolean possivelSujeito(){
		return ((getTipoPalavra()==Constantes.palSubst) || 
				(getTipoPalavra()==-1)|| 
				(getTipoPalavra()==Constantes.palPronomePessoal)|| 
				(getTipoPalavra()==Constantes.palArtDef));
	}
	
	public int getTipoPalavra() {
		return tipoPalavra;
	}
	public void setTipoPalavra(int tipoPalavra) {
		if (tipoPalavra!=-1)
			this.tipoPalavra = tipoPalavra;
	}
	public String getId() {
		return id;
	}
	
	public String toString(){
		return id;
	}
	public void setId(String id) {
		if (!id.equals(""))
		this.id = id;
	}
	public int getTipoAdjetivo() {
		return tipoAdjetivo;
	}
	public void setTipoAdjetivo(int tipoAdjetivo) {
		if (tipoAdjetivo!=-1)
			this.tipoAdjetivo = tipoAdjetivo;
	}
}
