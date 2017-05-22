<?php

class TestsMain_page extends TestBase {
    /*  function testModelWidth() {
      } */

    function testAssist() {
        $assist = new MainPageAssist();
        $elRet = $assist->criaJumboTron();
        $this->assertNotNull($elRet, "criaJumboTron vazio");

        $this->impersonateGuest();
        $elRet = $assist->registerNowLink();
        $this->assertNotNull($elRet, "registerNowLink vazio no guest");
        $this->impersonateAdmin();
        $elRet = $assist->registerNowLink();
        $this->assertNull($elRet, "registerNowLink não vazio quando usuario logado");

        $valor = translateKey("main_titulo_chamada");
        $this->assertNotNull($valor, "main_titulo_chamada vazio");
    }

    function testSampleProject() {
        $assist = new MainPageAssist();
        $el = $assist->sampleProjectLink();
        $this->assertNotNull($el, "testSampleProject link vazio");
    }

    function testSubcolumn() {
        $assist = new MainPageAssist();
        $ret = $assist->iniciaSubColumn();
        $this->assertNotNull($ret, "testSubcolumn vazio");

        $ret = $assist->iniciaSubColumnPT();
        $this->assertNotNull($ret, "testSubcolumnPt vazio");
        $ret = $assist->iniciaSubColumnEN();
        $this->assertNotNull($ret, "testSubcolumnEN vazio");
    }

    function testTexto() {
        $tituloEsp = "title";
        $textoEsp = "texto";
        $urlImagem = "urlImagem!";
        $url = "url...";

        $txt = new Texto($tituloEsp, $textoEsp, $url, $urlImagem);

        $this->assertEqual($tituloEsp, $txt->getTitulo(), "testTexto() getTitulo");
        $this->assertEqual($textoEsp, $txt->getTexto(), "testTexto() getTexto");
        $this->assertEqual($urlImagem, $txt->getUrlImagem(), "testTexto() getUrlImagem:" . $txt->getUrlImagem());
        $this->assertEqual($url, $txt->getUrl(), "testTexto() getUrl:" . $txt->getUrl());
    }

}

?>
