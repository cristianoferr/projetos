package com.cristiano.java.gameObjects.tests.groups;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.cristiano.java.gameObjects.tests.TesteGameObjects;
import com.cristiano.java.jme.models.GMModelTests;
import com.cristiano.java.jme.tests.TestEntitySystem;
import com.cristiano.java.jme.tests.TestFileAccess;
import com.cristiano.java.jme.tests.TestGameEvents;
import com.cristiano.java.jme.tests.builder.GMBuilderTests;
import com.cristiano.java.jme.tests.genres.GMGenreTests;
import com.cristiano.java.jme.tests.macro.GMMacroTests;
import com.cristiano.java.jme.tests.map.GMMapTests;
import com.cristiano.java.jme.tests.persistence.GMPersistenceTests;
import com.cristiano.java.jme.tests.texture.GMTextureTests;
import com.cristiano.java.jme.tests.ui.GMUITests;
import com.cristiano.java.jme.tests.unit.GMUnitTests;
import com.cristiano.java.jme.tests.visual.GMVisualTests;
import com.cristiano.utils.tests.TestStage;

@RunWith(Suite.class)
@SuiteClasses({ GMBuilderTests.class,GMPersistenceTests.class, GMTextureTests.class,GMModelTests.class, GMUnitTests.class,GMMacroTests.class,GMVisualTests.class,GMUITests.class,
	GMGenreTests.class,
   TesteGameObjects.class,
		  TestEntitySystem.class, TestGameEvents.class,
		TestFileAccess.class,  TestStage.class,GMMapTests.class
		 })
public class GMTests {

}
