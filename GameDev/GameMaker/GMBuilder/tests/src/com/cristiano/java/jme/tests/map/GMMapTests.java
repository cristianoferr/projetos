package com.cristiano.java.jme.tests.map;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.cristiano.java.jme.tests.full.TestWithAllElements;


@RunWith(Suite.class)
@SuiteClasses({ TestRoadSolver.class,TestBubblePopper.class,TestWithAllElements.class,TestMapSolver.class,TestTerrainSystem.class})
public class GMMapTests {


}
