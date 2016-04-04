package com.cristiano.java.blueprintManager.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.cristiano.java.product.tests.TestBlueprintOutput;
import com.cristiano.proc.nameGen.tests.ProceduralTests;
import com.cristiano.utils.tests.CommonTests;


@RunWith(Suite.class)
@SuiteClasses({TestBlueprintOutput.class,ProceduralTests.class,TestBlueprintPersistence.class,CommonTests.class,TesteBlueprint.class,TesteBlueprintFunctions.class,TesteHelpers.class,TesteKeySet.class})

public class BPTests {


}
