package com.cristiano.java.product.params;



public class Parametro {
	String modificador="=";
	String param="";
	String identifier="";
	private String comentario;
	
	
	public Parametro(String param){
		defineComponents(param);
	}
	

	public void defineComponents(String param){
		String s="";
		int fase=0;
		this.param="";
		try {
			for (int i=0;i<param.length();i++){
				if (fase==0){
					if ((param.charAt(i)=='+') || (param.charAt(i)=='-') || (param.charAt(i)=='=') || (param.charAt(i)=='*')){
						identifier=s;
						s="";
						fase++;
					}
				}
				
				if (fase==2){
					if ((param.charAt(i)!='=') ){
						modificador=s;
						fase++;
						this.param=param.substring(i);
					}
				}
				if (fase==1){
					if ((param.charAt(i)=='+') || (param.charAt(i)=='-') || (param.charAt(i)=='=') || (param.charAt(i)=='*')){
						fase++;
					} else {
						
					}
				}
				s=s+param.charAt(i);
			}	
			
			if (param.trim().equals("")){
					throw new Exception("Erro: parametro sendo passado vazio. "+identifier);
			}
			identifier=identifier.trim();
			modificador=modificador.trim();
			this.param=this.param.trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String getModificador() {
		return modificador;
	}

	public String getValue() {
		return param;
	}

	public boolean isAddictive() {
		return (modificador.equals("+="));
	}
	
	public String toString(){
		String c="";
		if (comentario!=null){
			c=" //"+comentario;
		}
		return identifier+modificador+param+c;
	}


	public String getIdentifier() {
		return identifier;
	}


	public void changeParam(String param2) {
		defineComponents(param2);
		
	}


	public void setValue(String param) {
		this.param = param;
	}


	public void setComentario(String comentario) {
		this.comentario=comentario;
		
	}


	
}
