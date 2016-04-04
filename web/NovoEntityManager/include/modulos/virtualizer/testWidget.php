<?php

class TestsWidget extends TestBase {

    function testWidgetPainel() {
        $controller = getControllerForTable("widget");
        $painel = $controller->createPainel(WIDGET_TESTE_SECTION);
        $this->assertNotNull($painel, "painel nulo");
        $this->assertEqual(get_class($painel), "PainelVertical", "Classe PainelVertical errado:" . get_class($painel));

        $abas = $painel->getAbas();
        $this->assertEqual(sizeof($abas), 3, "Quantidade de abas errado:" . sizeof($abas));
    }

    function testWidget() {
        $widgetController = getControllerForTable("widget");
        $this->assertNotNull($widgetController, "widgetController null");

        $id_painel = $widgetController->getPainel(WIDGET_TESTE_H);

        $painelController = getControllerForTable("painel");
        $arrAcoes = $painelController->getArrayActions($id_painel);
        $modelAction = $painelController->getLinkWithId(ACAO_TP_EDIT);
    }

    function testWidgetDefault() {
        $controller = getControllerForTable("widget");

        $arr = array(
            "nm_widget" => "TesteWidgetRootDefault", "id_entidade" => ENTIDADE_TESTE, "id_painel" => PAINEL_TESTE_V,
            "id_projeto" => PROJETO_TESTE);
        $this->verificaInclusaoRemocao($arr, $controller, "widget");
    }

    // - criar secao root do widget (default) ao criar o widget
    function testRootWidgetSection() {
        $controller = getControllerForTable("widget");
        $sectionController = getControllerForTable("widget_section");

        $arr = array(
            "nm_widget" => "TesteWidgetRootAuto", "id_entidade" => ENTIDADE_TESTE, "id_painel" => PAINEL_TESTE_V,
            "id_projeto" => PROJETO_TESTE);
        $idWidget = $controller->insereRegistro($arr);

        $projW = $controller->getProjeto($idWidget);
        $this->assertEqual($projW, PROJETO_TESTE, "Projeto widget diferente: $projW");

        $count = $sectionController->countForWidget($idWidget);
        $this->assertEqual($count, 1, "countForWidget<>1=$count");

        $sectionController->loadRegistros("and id_widget=$idWidget");
        while ($model = $sectionController->next()) {
            $projWS = $sectionController->getProjeto($model->getId());
            $this->assertEqual($projWS, PROJETO_TESTE, "Projeto widget section diferente: $projWS");
        }

        executaSQL("delete from widget_section where id_widget=?", array($idWidget));
        executaSQL("delete from widget where id_widget=?", array($idWidget));
    }

    // - criar secoes
    function testCreateSections() {
        $controller = getControllerForTable("widget");
        $sectionController = getControllerForTable("widget_section");
        $count = $sectionController->countForWidget(WIDGET_TESTE_V);
        $this->assertEqual($count, 0, "countForWidget WIDGET_TESTE_V<>0=$count");
        $idSection = $controller->createSection(WIDGET_TESTE_V, "teste secao");
        $this->verifyFieldAssignment($idSection);
        $count = $sectionController->countForWidget(WIDGET_TESTE_V);
        $this->assertEqual($count, 1, "countForWidget WIDGET_TESTE_V apos criar secao<>1=$count");
        $this->assertNotNull($idSection, "id widget_section nulo");


        executaSQL("delete from widget_section where id_widget=?", array(WIDGET_TESTE_V));
    }

    // - atribuir colunas a secoes do widget
    function verifyFieldAssignment($idSection) {
        $sectionController = getControllerForTable("widget_section");

        $idFieldColuna = $sectionController->assignField($idSection, COLUNA_TESTE);
        $this->assertNotNull($idFieldColuna);

        //não permitir multiplos
        $idFieldColuna = $sectionController->assignField($idSection, COLUNA_TESTE);
        $this->assertFalse($idFieldColuna, "Assignfield deve retornar nulo caso já exista coluna: '$idFieldColuna'");


        executaSQL("delete from widget_coluna where id_widget_section=?", array($idSection));
    }

    // - mudar a ordem das secoes
    // - mudar a ordem das colunas nas secoes
}

?>
