package com.cristiano.galactic.model.enums;

import java.util.Vector;

public enum PlanetProperties {
PP_ORBITAL_POSITION,
PP_ORBITAL_RADIUS,
PP_MASS, 
//PP_CURRENT_ORBITAL_RADIUS,
PP_ROTATION_VELOCITY,
PP_AXIAL_TILT,
PP_ORBITAL_ECCENTRICITY,
PP_ORBITAL_INCLINATION,
PP_ROTATION_POSITION,
PP_ASTRO_TYPE,
PP_DENSITY,
PP_ORBITAL_OFFSET, 
PP_RADIUS,
PP_OCEAN_FLAG, 
PP_ATMOS_DENSITY,
PP_ATMOS_COLOR,
PP_PLANET_HEIGHTSCALE,
PP_PLANET_SEED;

public static Vector<String> getOrbitProps(){
	Vector<String> v=new Vector<String>();
	v.add(PP_ORBITAL_POSITION.toString());
	v.add(PP_MASS.toString());
	v.add(PP_ORBITAL_RADIUS.toString());
//	v.add(PP_CURRENT_ORBITAL_RADIUS.toString());
	v.add(PP_ROTATION_VELOCITY.toString());
	v.add(PP_AXIAL_TILT.toString());
	v.add(PP_ORBITAL_ECCENTRICITY.toString());
	v.add(PP_ORBITAL_INCLINATION.toString());
	v.add(PP_ROTATION_POSITION.toString());
	v.add(PP_ASTRO_TYPE.toString());
	v.add(PP_DENSITY.toString());
	v.add(PP_ORBITAL_OFFSET.toString());
	v.add(PP_RADIUS.toString());
	return v;
}

public static Vector<String> getPlanetProps(){
	Vector<String> v=new Vector<String>();
	v.add(PP_ATMOS_DENSITY.toString());
	v.add(PP_OCEAN_FLAG.toString());
	v.add(PP_ATMOS_COLOR.toString());
	v.add(PP_PLANET_HEIGHTSCALE.toString());
	v.add(PP_PLANET_SEED.toString());
	
	return v;
}

public static Vector<String> getStarProps(){
	Vector<String> v=new Vector<String>();
	v.add(PP_MASS.toString());
	v.add(PP_ASTRO_TYPE.toString());
	v.add(PP_DENSITY.toString());
	v.add(PP_RADIUS.toString());
	return v;
}
}
