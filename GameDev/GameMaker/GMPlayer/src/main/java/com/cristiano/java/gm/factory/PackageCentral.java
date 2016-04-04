package com.cristiano.java.gm.factory;

import java.util.ArrayList;
import java.util.HashMap;

import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;


class ObjectPackages {
	ArrayList<String> packages = new ArrayList<String>();

	public void add(String entityPackage) {
		packages.add(entityPackage);
	}

	public String getFullClassPath(String className) {
		for (String pkg:packages){
			if (CRJavaUtils.classExists(pkg + "." + className, this)) {
				return pkg;
			}
		} 
		Log.error("!!!Class not found:" + className);
		return "";
	}

}


public class PackageCentral {
	HashMap<String, ObjectPackages> objectPackages = new HashMap<String, ObjectPackages>();

	public void addPackage(String typeEntity, String entityPackage) {
		ObjectPackages pkgs = getPackagesFor(typeEntity);
		pkgs.add(entityPackage);
	}

	private ObjectPackages getPackagesFor(String typeEntity) {
		if ("".equals(typeEntity)){
			Log.fatal("Requesting an empty typeEntity!");
		}
		ObjectPackages objPkg = objectPackages.get(typeEntity);
		if (objPkg != null) {
			return objPkg;
		}
		objPkg = new ObjectPackages();
		objectPackages.put(typeEntity, objPkg);
		return objPkg;
	}

	private String getFullClassPathForObjectType(String objectType,
			String className) {
		ObjectPackages pkg = getPackagesFor(objectType);
		return pkg.getFullClassPath(className);
		
		
	}

	public Object instantiateClass(String className, String objectType) {
		boolean fullName = className.contains(".");
		Object obj = null;

		if (!className.equals("")) {
			if (!fullName) {
				className=className.replace("/", ".");
				className = getFullClassPathForObjectType(objectType, className)
						+ "." + className;
			}
			try {
				obj = CRJavaUtils.instanciaClasse(className);
				return obj;
			} catch (Exception e) {
				Log.error("Error instantiating '" + className + "': "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
		Log.error("Class '" + className +"' of type "+objectType + " is undefined!");
		return null;
	}
}
