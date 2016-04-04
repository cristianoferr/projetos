<?php

class TestsABTesting extends TestBase {

    function testaFeatureCheck() {
        $controller = getControllerForTable("feature_check");
        $id = $controller->getIdFeature("testeFeatureCheck");
        $this->assertNotNull($id, "id feature_check nulo");

        $id2 = $controller->getIdFeature("testeFeatureCheck");
        $this->assertEqual($id, $id2, "ids feature_check diferentes");
    }

    function testaIsAbTestAtivo() {
        $abtest = 1;
        isAbtestAtivo($abtest, 1);
        $controller = getControllerForTable("abtest");
        $cod_interno = $controller->getVariacaoAtiva($abtest);

        $this->assertTrue(($cod_interno >= 1), "cod_interno $cod_interno não é >= que 1");
        $cod_interno = $controller->getVariacaoUsuarioAtual($abtest);

        $this->assertTrue(($cod_interno >= 1), "cod_interno $cod_interno não é >= que 1");
    }

    function testRedirLink() {
        $abtestRedir = 1;
        $texto = "texto qualquer";
        $urlEsperada = getHomeDir() . "redir/$abtestRedir#" . ANCORA_INICIO;
        $elLink = abtest()->getRedirLink($abtestRedir, $texto);
        $this->assertNotNull($elLink, "testRedirLink elLink nulo");
        $this->assertEqual($elLink->getValue(), $texto, $elLink->getValue() . " <> $texto");
        $this->assertEqual($elLink->getAtributo("href"), $urlEsperada, $elLink->getAtributo("href") . " <> $urlEsperada");
    }

}

?>
