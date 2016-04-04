package com.cristiano.galactic.gen.solarsystem;

import java.awt.Color;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cristiano.cyclone.math.PhysicsConsts;
import com.cristiano.cyclone.utils.Formatacao;
import com.cristiano.cyclone.utils.Vector3;
import com.cristiano.galactic.model.enums.EntityType;
import com.cristiano.galactic.model.enums.PlanetProperties;
import com.cristiano.gamelib.propriedades.PropriedadesObjeto;

public class PlanetGen extends AstroEntity{

	//Maior e menor raio que um planeta pode ter
	static final float MIN_RADIUS=1000000.0f;
	static final float MAX_RADIUS=90000000.0f;

	
	
	static final double MIN_RAIO_ATMOSPHERE=PhysicsConsts.raioLUA;
	static final double MIN_RAIO_GASOSO=PhysicsConsts.raioTERRA*5;
	
	final static float MAX_DISTANCE_MOON=(float)(5*PhysicsConsts.C); //Distancia maxima da lua
	final static int MAX_QTD_MOON=20;
	
	AstroEntity orbiting;//Qual astro esse astro está orbitando (pai)

	double tipo=0; //0=rochoso sem atmosfera, 1=rochoso com atmosfera, 2=gasoso
	
	Vector<PlanetGen> luas=new Vector<PlanetGen>();
	
	public PlanetGen(String name,double orbitalRadius,AstroEntity orbiting){
		super(name);
		this.orbiting=orbiting;
		props.setProperty(PropriedadesObjeto.PROP_TYPE, EntityType.ET_PLANET);
		props.setProperty(PlanetProperties.PP_ORBITAL_RADIUS, orbitalRadius);
	}
	
	public void initializePlanet(Vector3 systemPos,boolean generateMoons) {
		initializePlanetOrbit(systemPos,generateMoons);
		
		initializePlanetProperties();
		
	}

	/**
	 * Usado pelo JMEPlanet para compor os planetas
	 */
	private void initializePlanetProperties() {
		double raio=props.getPropertyAsDouble(PlanetProperties.PP_RADIUS);
		double mass=props.getPropertyAsDouble(PlanetProperties.PP_MASS);
		double orbitalradius=props.getPropertyAsDouble(PlanetProperties.PP_ORBITAL_RADIUS);
		System.out.println(getName()+" raio:"+Formatacao.formatDistance(raio)+" ("+Formatacao.format(raio/PhysicsConsts.raioTERRA)+") mass:"+Formatacao.formatMass(mass)+" ("+Formatacao.format(mass/PhysicsConsts.massaTERRA)+")" );
		//System.out.println("Orbital Radius:"+Formatacao.formatDistance(orbitalradius));
		
		double densidadeAtmosfera=mass/PhysicsConsts.massaTERRA; //1= densidade atmosferica terrestre
		double mult=(raio % 4)*0.5;//O planeta pode ter uma atmosfera mais ou menos densa...
		densidadeAtmosfera*=mult;
		if (densidadeAtmosfera<0.1)densidadeAtmosfera=0;
		
		int seed=(int)(raio%Math.pow(2,24)+mass%Math.pow(2,24)+orbitalradius%Math.pow(2,24));
		props.setProperty(PlanetProperties.PP_PLANET_SEED, seed);
		
		if (densidadeAtmosfera>0) tipo=1;
		if (densidadeAtmosfera>10) tipo=2; //gasoso
		props.setProperty(PlanetProperties.PP_ATMOS_DENSITY, densidadeAtmosfera);
		props.setProperty(PlanetProperties.PP_ASTRO_TYPE, tipo);
		
		//Cor da atmosfera
		Color atmosColor=new Color((int)(raio%256),(int)(mass%255),(int)(orbitalradius%256));
		props.setProperty(PlanetProperties.PP_ATMOS_COLOR, atmosColor);
		
		
		//Tem oceano?
		double flagOcean=0;
		if ((densidadeAtmosfera>1) && (tipo==1))flagOcean=flagOcean%2; //0=tem oceano, 1=nao
		props.setProperty(PlanetProperties.PP_OCEAN_FLAG, flagOcean);
		
		//quanto menor o planeta, maior o heighscale proporcional
		double razaoInv=1/(raio/PhysicsConsts.raioTERRA);
		
		double heightScale=PhysicsConsts.raioTERRA/100*razaoInv;
		if (heightScale>raio/10) heightScale=raio/10;
		props.setProperty(PlanetProperties.PP_PLANET_HEIGHTSCALE, heightScale);
	}
	
	
	
	public void initializePlanetOrbit(Vector3 systemPos,boolean generateMoons) {
		//Vector3 systemPos = new Vector3(orbit.getGalacticPos());
		// The real values can be too big for Simplex.
		double radius=-1;
		double orbitalRadius=props.getPropertyAsDouble(PlanetProperties.PP_ORBITAL_RADIUS);
		double logRadius = Math.log(orbitalRadius );


		// When generating the following values, we don't want them to all be based
		// on the same noise value.  By altering the parameters to each,
		// we can be certain that each returned value will be different.

		// Radius may be somewhat correlated with orbital radius -
		// in our solar system, the largest planets are far out from the sun.
		// Mercury's equatorial radius is 2440 km, Jupiter's is 71492 km.
		
		//Caso seja uma lua, o tamanho é bem menor...
		double minRadius=MIN_RADIUS;
		double maxRadius=MAX_RADIUS;
		//Caso seja uma lua orbitando um planeta, então o raio não pode ser maior que um 1/10 do planeta pai.
		if (!orbiting.isStar()){
			minRadius=50000;
			maxRadius=orbiting.getRadius()/10;
		}
		
		if (radius<0){
			radius = Simplex.simplexScaledNoise(2, 0.3, 100, minRadius, maxRadius, systemPos.x, systemPos.x, systemPos.x, logRadius );
			double razao=orbitalRadius/SolarSystemGenerator.MAX_DISTANCE;
			
			if ((orbiting.isStar()) && (radius*razao>minRadius)){
				radius*=razao;//Planetas estavam ficando muito grandes, tive que fazer uma proporção onde quanto mais longe, maior é o planeta (não é o ideal mas funciona.
			}
		}
		logRadius = Math.log(radius );

		props.setProperty(PlanetProperties.PP_RADIUS, radius);
		// Mass is not always entirely correlated with a planet's radius - some planets
		// have denser make-ups than others.  The density of earth-like planets is usually
		// 5.5 kg/m^3, while gas giants are closer to 1.6 kg/m^3.
		double density=Simplex.simplexScaledNoise(2, 0.3, 100, 1400, 5700, systemPos.x, systemPos.x, systemPos.y, logRadius );
		density=density*PhysicsConsts.massaKG; //densidade em KG
		props.setProperty(PlanetProperties.PP_DENSITY, density);
		double mass = (4/3) * Math.PI * radius * radius * radius * density;
		props.setProperty(PlanetProperties.PP_MASS, mass);

		// Rotation velocity seems to be uncorrelated with orbital radius.
		// The fastest planets will complete a revolution in 20 seconds.
		double rotationVelocity = Simplex.simplexScaledNoise(2, 0.3, 100, -0.036, 0.036, systemPos.x, systemPos.x, systemPos.z, logRadius );
		props.setProperty(PlanetProperties.PP_ROTATION_VELOCITY, rotationVelocity);
		
		// Axial tilt is not correlated with orbital radius.
		double axialTilt = Simplex.simplexScaledNoise(2, 0.3, 100, -70, 70, systemPos.x, systemPos.y, systemPos.x, logRadius );
		props.setProperty(PlanetProperties.PP_AXIAL_TILT, axialTilt);

		// Large orbital eccentricity is usually only possible for large orbital radii. Always positive and less than 1.
		double orbitalEccentricity = Simplex.simplexScaledNoise(2, 0.3, 100, 0, 0.5, systemPos.x, systemPos.y, systemPos.y, logRadius );//implementado
		props.setProperty(PlanetProperties.PP_ORBITAL_ECCENTRICITY, orbitalEccentricity);

		// Orbital inclination is not correlated with orbital radius.
		double orbitalInclination = Simplex.simplexScaledNoise(2, 0.3, 100, -10, 10, systemPos.x, systemPos.y, systemPos.z, logRadius );//implementado
		props.setProperty(PlanetProperties.PP_ORBITAL_INCLINATION, orbitalInclination);

		//Definindo rotation e orbital como um numero entre 0 e 360 baseado em massa e raio.
		props.setProperty(PlanetProperties.PP_ROTATION_POSITION, new Double(mass % 360));
		props.setProperty(PlanetProperties.PP_ORBITAL_POSITION, new Double(radius % 360));
		//props.setProperty(PlanetProperties.PP_CURRENT_ORBITAL_RADIUS, props.getPropertyAsDouble(PlanetProperties.PP_ORBITAL_RADIUS));

		// Orbital offset should be essentially random. Always positive.
		double orbitalOffset = Simplex.simplexScaledNoise(2, 0.3, 100, 0, 360, systemPos.x, systemPos.z, systemPos.x, logRadius );
		props.setProperty(PlanetProperties.PP_ORBITAL_OFFSET, orbitalOffset);
		
		
		
		//Planetas podem ter luas mas luas não podem ter sub-luas (teoricamente é possível mas não deve ser algo comum)
		if (generateMoons){
			int qtdMoons=(int) Simplex.simplexScaledNoise(2, 0.3, 100, 0, MAX_QTD_MOON, systemPos.x, systemPos.y, systemPos.z, logRadius );
			double razaoTamanho=radius/MAX_RADIUS;
			
			//Se o planeta for pequeno ele não pode ter muitas luas... certo?
			if (razaoTamanho<0.3)
				qtdMoons=(int) (qtdMoons*razaoTamanho);
			System.out.println("");
			System.out.println("qtd luas:"+qtdMoons+" logRadius:"+logRadius+" razao:"+razaoTamanho);
			
			Vector3 moonPos=new Vector3(Math.log(radius),Math.log(mass),Math.log(orbitalRadius)); //seed?

			
			for (int i=0;i<qtdMoons;i++){
				double orbitalRadiusMoon=Simplex.simplexScaledNoise(2, 0.3, 100, radius*5, (MAX_DISTANCE_MOON-radius*2), systemPos.x, systemPos.y, systemPos.y, i );
				System.out.println("Raio Orbital Lua("+i+"):"+Formatacao.formatDistance(orbitalRadiusMoon));
				
				PlanetGen lua=new PlanetGen(getName()+" "+RandomNameGenerator.getGreekLetter(i),orbitalRadiusMoon,this);
				lua.initializePlanet(moonPos, false);
				luas.add(lua);
				
			}
			System.out.println("Planeta:");

		}
		
		
		
		//double x=
	/*
	 t=angulo=PP_ORBITAL_POSITION
	 a=PP_ORBITAL_RADIUS
	 r
	  x(t)=a.cos(t);
	y(t)=b.sin(t);
	
	
	*/
		/*
		double rAP=(1+orbitalEccentricity)*radius;
		double rPER=(1-orbitalEccentricity)*radius;
		System.out.println("e:"+orbitalEccentricity+" radius:"+Formatacao.formatDistance(radius)+" rAP:"+Formatacao.formatDistance(rAP)+" rPER:"+Formatacao.formatDistance(rPER) );

		double angle=30;
		angle=angle%180;
		if (angle>90){
			angle=180-angle;	
		}
		double difR=rAP-rPER;
		double currentOrbitalRadius=angle*difR/90+rPER;
		
		Vector3 newCoord=new Vector3(currentOrbitalRadius,0,0).getRotateBy(Vector3.ZERO, Vector3.X, angle);
		newCoord=newCoord.getRotateBy(Vector3.ZERO, Vector3.Z, orbitalInclination);
		System.out.println("angle:"+angle+" currRadius:"+Formatacao.formatDistance(currentOrbitalRadius)+" newcoord:"+newCoord);
		//if (angle<90)
		*/
		
		
	}
	

	public Element buildXML(Document testDoc) {
		 Element element = super.buildXML(testDoc);
		 Element elPosition = testDoc.createElement("orbitProperties");
		 element.appendChild(elPosition);
		 
		Vector<String> properties=PlanetProperties.getOrbitProps();

		for (String string : properties) {
		//	System.out.println(string);
			elPosition.setAttribute(string, Double.toString(props.getPropertyAsDouble(string)));
		}
		
		
		 elPosition = testDoc.createElement("planetProperties");
		 element.appendChild(elPosition);
		 
		properties=PlanetProperties.getPlanetProps();

		for (String string : properties) {
		//	System.out.println(string);
			elPosition.setAttribute(string, props.getPropertyAsString(string));
		}
		
		
    	Element elEntities = testDoc.createElement("entities");
    	element.appendChild(elEntities);
    	for (int i=0;i<luas.size();i++){
    		elEntities.appendChild(luas.get(i).buildXML(testDoc));
    	}


			
		// elPosition.setAttribute("orbitando", Integer.toString(orbit.getId()));
		 
		 return element;
}

}
