<?php

class TestsKnowledge extends TestBase {

    function testPainelArtigo() {
        $controller = getControllerManager()->getControllerForTable("artigo");

        $painel = getWidgetManager()->generateEditButtonForWidget(WIDGET_EDIT_ARTIGO, ARTIGO_TESTE);
        $this->assertNotNull($painel, "widget vazio");
        $elroot = $painel->generate();
        $this->assertNotNull($elroot, "elroot vazio");



        $widget = getWidgetManager()->getWidgetFor(WIDGET_MOSTRA_ARTIGO);
        $this->assertNotNull($widget, "widget vazio");
        $painel = $widget->generate(ARTIGO_TESTE);
        $this->assertNotNull($painel, "painel vazio");
        $elEmpty = elMaker("");
        $model = $controller->loadSingle(ARTIGO_TESTE);
    }

    function testArtigoLinkIfOk() {
        $controller = getControllerForTable("artigo");
        $this->assertTrue($controller->linkIfOk(5, LINGUA_EN, "en_"), "Artigo 5 em ingles");
        $this->assertTrue($controller->linkIfOk(6, LINGUA_PT, "pt_"), "Artigo 6 em pt");


        $this->assertFalse($controller->linkIfOk(5, LINGUA_PT, "not_en_"), "!Artigo 5 em ingles");
        $this->assertFalse($controller->linkIfOk(6, LINGUA_EN, "not_pt_"), "!Artigo 6 em pt");
    }

    function getArrayTabelas() {
        return array("artigo");
    }

    function testCargaTabelas() {
        $this->validaTabelas();
    }

    function testArtigo() {
        $controller = getControllerForTable("artigo");

        $arr = array("elemento" => 52, "id_lingua" => LINGUA_IPSUM, "title_artigo" => "teste de inclusao");
        $html = "<b>Conteúdo do artigo</b>";
        $this->verificaInclusaoRemocao($arr, $controller, "artigo", $html);
    }

}

?>
