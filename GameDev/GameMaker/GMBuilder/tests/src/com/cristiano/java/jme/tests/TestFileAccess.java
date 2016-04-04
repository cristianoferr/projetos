package com.cristiano.java.jme.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cristiano.java.gameObjects.tests.MockAbstractTest;
import com.cristiano.jme3.assets.GMAssets;

public class TestFileAccess extends MockAbstractTest{
	
	
	@BeforeClass
	public static void setUp() throws IOException {
		startHeadless();
	}
	
	@Test public void testFilePaths() throws IOException {
		String newFilePath="gen/art/testImg.png";
		String filePath="Interface/Images/source/test1.png";
		String fullFilePath=GMAssets.getFullFilePath(filePath);
		assertTrue("Error on fullFilePath:"+fullFilePath,fullFilePath.contains("GMAssets/assets/"));
		
		OutputStream outputStream = GMAssets.getOutputStream(newFilePath);
		//String jmeFilePath=GMAssets.getJMEFilePath(filePath);
		assertNotNull("Output null",outputStream);
		outputStream.close();
		
		InputStream inputStream = GMAssets.getInputStream(filePath);
		assertNotNull("inputStream null",inputStream);
		
		//URL readingURL = GMAssets.getReadingURL(filePath);
		//assertNotNull("readingURL null",readingURL);
		
	}
		
}