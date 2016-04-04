package com.cristiano.java.gm.ecs.comps.macro;

import java.util.ArrayList;
import java.util.List;

import com.cristiano.jme3.assets.GMAssets;
import com.jme3.math.Vector3f;

public class ScreenShotOrganizer {
	private List<Resolution> resolutions=new ArrayList<Resolution>();
	private int resolutionIndex=0;
	private int situationIndex=0;
	
	private List<ScreenShotSituation> situations=new ArrayList<ScreenShotSituation>();

	public Resolution addResolution(String name, int w, int h) {
		Resolution resolution = new Resolution(name,w,h);
		resolutions.add(resolution);
		return resolution;
	}

	public ScreenShotSituation addSituation(String screen, Vector3f pos) {
		ScreenShotSituation screenShotSituation = new ScreenShotSituation(screen,pos);
		situations.add(screenShotSituation);
		return screenShotSituation;
	}

	public int size() {
		return situations.size()*resolutions.size();
	}

	public int pos() {
		return (situationIndex)+(resolutionIndex*resolutions.size());
	}

	public boolean next() {
		situationIndex++;
		if (situationIndex>=situations.size()){
			situationIndex=0;
			resolutionIndex++;
		}
		if (resolutionIndex>=resolutions.size()){
			resolutionIndex--;
			situationIndex=situations.size()-1;
			return false;
		}
		return true;
	}

	public void rewind() {
		resolutionIndex=0;
		situationIndex=0;
	}

	public Resolution getResolution() {
		return resolutions.get(resolutionIndex);
	}

	public ScreenShotSituation getSituation() {
		return situations.get(situationIndex);
	}

	public String getName() {
		String name=getResolution().getDevice()+"-"+situationIndex;
		return name;
	}

	public String getFilePath() {
		String path="gen/screenShots/"+getResolution().getDevice();
		GMAssets.checkFolder(path);
		return path;
	}

	public String getFileNamePath() {
		return getFilePath()+"/"+getFileName()+".png";
	}

	public String getFileName() {
		return "screenShot"+situationIndex;
	}
}
