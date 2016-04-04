<?php

class TestsConstrubook extends TestBase {

    function testaFeatureCheck() {
        
        $controller = getControllerForTable("feature_check");
        $id = $controller->getIdFeature("testeFeatureCheck");
        $this->assertNotNull($id, "id feature_check nulo");

        $id2 = $controller->getIdFeature("testeFeatureCheck");
        $this->assertEqual($id, $id2, "ids feature_check diferentes");
    }

}

?>
