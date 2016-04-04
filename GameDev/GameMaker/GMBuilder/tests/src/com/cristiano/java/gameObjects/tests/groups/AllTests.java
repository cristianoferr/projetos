package com.cristiano.java.gameObjects.tests.groups;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.cristiano.java.blueprintManager.tests.BPTests;
import com.cristiano.jme3.tests.JMETests;


@RunWith(Suite.class)
@SuiteClasses({ BPTests.class,JMETests.class,GMTests.class})

public class AllTests {


}
