package com.cristiano.galactic.gen.solarsystem;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Formatacao;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.PlanetProperties;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;

public class StarGen extends AstroEntity{
	public final double MIN_RADIUS= PhysicsConsts.raioSOL*0.8;
	public final double MAX_RADIUS=MIN_RADIUS*4;

	int tipo=0;//0=red(fria), 1=yellow(normal), 2=blue(quente);
	String cor;
	
	public StarGen(String name,Vector3 systemPos){
		super(name);
		initializeStar(systemPos);
	}
	private void initializeStar(Vector3 systemPos) {
		props.setProperty(PropriedadesObjeto.PROP_TYPE, EntityType.ET_STAR);
		double radius=Simplex.simplexScaledNoise(2, 0.3, 100, MIN_RADIUS, MAX_RADIUS, systemPos.x, systemPos.y, systemPos.y, systemPos.z );
		double logRadius = Math.log(radius );
		double density=Simplex.simplexScaledNoise(2, 0.3, 100, 1000, 3700, systemPos.x, systemPos.x, systemPos.y, logRadius );
		density*=PhysicsConsts.massaKG;//densidade em kg
		props.setProperty(PlanetProperties.PP_DENSITY, density);
		double mass = (4/3) * Math.PI * radius * radius * radius * density;
		props.setProperty(PlanetProperties.PP_MASS, mass);


		props.setProperty(PlanetProperties.PP_RADIUS, radius);
		
		tipo=(int) (radius%3);
		props.setProperty(PlanetProperties.PP_ASTRO_TYPE, new Double(tipo));
		//tipo=(int)(Math.random()*3);
		if (tipo==0) cor="RED";
		if (tipo==1) cor="YELLOW";
		if (tipo==2) cor="BLUE";
		
		System.out.println("Star density:"+Formatacao.formatMass(density)+" radius:"+Formatacao.formatMass(radius)+"("+radius/PhysicsConsts.raioSOL+")"+" mass:"+Formatacao.formatDistance(mass)+" ("+mass/PhysicsConsts.massaSOL+")");
	}
	public double getRadius() {
		return props.getPropertyAsDouble(PlanetProperties.PP_RADIUS);
	}
	
	@Override
	public boolean isStar(){
		return true;
	}
	
	public Element buildXML(Document testDoc) {
		 Element element = super.buildXML(testDoc);
		 Element elPosition = testDoc.createElement("starProperties");
		 element.appendChild(elPosition);
		 
		Vector<String> properties=PlanetProperties.getStarProps();

		for (String string : properties) {
		//	System.out.println(string);
			elPosition.setAttribute(string, Double.toString(props.getPropertyAsDouble(string)));
		}

			
		// elPosition.setAttribute("orbitando", Integer.toString(orbit.getId()));
		 
		 return element;
}
}
