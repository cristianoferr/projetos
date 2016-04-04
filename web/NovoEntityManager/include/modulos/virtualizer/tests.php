<?php

include("testWidget.php");
include("testSiteProjeto.php");
include("testPainel.php");

/**
 * Essa classe irá testar paineis e tiles, incluindo assignar widget a tile, mas não
 * irá testar os widgets em si
 */
class TestsVirtualizer extends TestBase {

    function testAssignWidget() {
        $tileController = getControllerForTable("tile");
        $this->assertNotNull($tileController, "tileController null");

        $tileController->assignWidget(TILE_TESTE);
        $id_widget = $tileController->getWidgetAssignedTo(TILE_TESTE);
        $this->assertFalse($id_widget, "getWidgetAssignedTo nulo falhou: $id_widget");

        $tileController->assignWidget(TILE_TESTE, WIDGET_TESTE_H);
        $id_widget = $tileController->getWidgetAssignedTo(TILE_TESTE);
        $this->assertEqual($id_widget, WIDGET_TESTE_H, "getWidgetAssignedTo WIDGET_TESTE_H falhou: $id_widget");

        $tileController->assignWidget(TILE_TESTE);
        $id_widget = $tileController->getWidgetAssignedTo(TILE_TESTE);
        $this->assertFalse($id_widget, "getWidgetAssignedTo nulo2 falhou: $id_widget");
    }

    function testScreen() {
        $controller = getControllerForTable("screen");
        $this->assertNotNull($controller, "controller screen vazio");
        $ret = $controller->getProjeto(SCREEN_TESTE_AUTO);
        $this->assertEqual($ret, PROJETO_TESTE, "projeto retornado da screen diferente do esperado:$ret");

        $controller->resetScreen(SCREEN_TESTE_AUTO);

        $this->assertEqual(countQuery("select * from tile where id_screen=?", array(SCREEN_TESTE_AUTO)), 0, "resetscreen falhou");
    }

    function verificaTileRoot($idTile, $controller) {
        $this->assertNotNull($idTile, "tile nulo");

        $ret = $controller->getScreen($idTile);
        $this->assertEqual($ret, SCREEN_TESTE_AUTO, "getScreen retornou diferente: '$ret'");

        $ret = $controller->getTileParent($idTile);
        $this->assertEqual($ret, null, "getTileParent idTile retornou diferente: $ret");

        $ret = $controller->countTilesForScreen(SCREEN_TESTE_AUTO);
        $this->assertEqual($ret, 1, "count tiles retornou algo diferente do esperado(1): $ret");
        $this->assertEqual($controller->getProjeto($idTile), PROJETO_TESTE, "projeto retornado diferente do esperado");
    }

    function verificaTileUpDown($idTile, $controller) {
        $idTileSub1 = $controller->addTile($idTile, TILE_ORIENT_VERTICAL, TILE_SIZE_MEDIUM);
        $this->assertNotNull($idTileSub1, "subtile1 nulo");

        $ret = $controller->getTileParent($idTileSub1);
        $this->assertEqual($ret, $idTile, "getTileParent retornou diferente: $ret");

        $idTileSub2 = $controller->addTile($idTile, TILE_ORIENT_VERTICAL, TILE_SIZE_MEDIUM);
        $this->assertNotNull($idTileSub2, "subtile2 nulo");

        $ret = $controller->getScreen($idTileSub2);
        $this->assertEqual($ret, SCREEN_TESTE_AUTO, "getScreen(idTileSub2) retornou diferente: $ret");

        $this->assertEqual($controller->getProjeto($idTileSub2), PROJETO_TESTE, "projeto retornado do sub2 diferente  do esperado");

        $pos2 = $controller->getTilePosition($idTileSub2);
        $this->assertEqual($pos2, 1, "POsicao do subtile2 devia ser 1 mas é $pos2");

        $controller->moveTileUp($idTileSub2);
        $pos2 = $controller->getTilePosition($idTileSub2);
        $this->assertEqual($pos2, 0, "POsicao do subtile2 após mover up devia ser 0 mas é $pos2");

        $controller->moveTileUp($idTileSub2);
        $pos2 = $controller->getTilePosition($idTileSub2);
        $this->assertEqual($pos2, 0, "POsicao do subtile2 após mover up novamente devia ser 0 mas é $pos2");

        $controller->moveTileDown($idTileSub2);
        $pos2 = $controller->getTilePosition($idTileSub2);
        $this->assertEqual($pos2, 1, "POsicao do subtile2 após mover down devia ser 1 mas é $pos2");

        $controller->moveTileDown($idTileSub2);
        $pos2 = $controller->getTilePosition($idTileSub2);
        $this->assertEqual($pos2, 1, "POsicao do subtile2 após mover down novamente devia ser 1 mas é $pos2");
    }

    function testTile() {
        $row = executaQuerySingleRow("select AUTO_INCREMENT as tot FROM information_schema.tables WHERE table_name =  'tile'");
        getControllerForTable("screen")->resetScreen(SCREEN_TESTE_AUTO);
        $id_max = 1;

        $controller = getControllerForTable("tile");
        $idTile = $controller->addRootTile(SCREEN_TESTE_AUTO, TILE_ORIENT_HORIZONTAL, TILE_SIZE_MEDIUM);
        $this->verificaTileRoot($idTile, $controller);

        $this->verificaTileUpDown($idTile, $controller);



        executaSQL("ALTER TABLE tile AUTO_INCREMENT = " . ($id_max ));
    }

    function testTileGeneration() {
        getControllerForTable("screen")->resetScreen(SCREEN_TESTE_AUTO);
        $controller = getControllerForTable("tile");
        $this->assertEqual(get_class($controller), "TileController", "classe diferente!");

        $idTileRoot = $controller->addRootTile(SCREEN_TESTE_AUTO, TILE_ORIENT_HORIZONTAL, TILE_SIZE_MEDIUM);
        $tileTree = $this->criaTiles($idTileRoot);

        $painelScreen = getPainelManager()->getPainel(PAINEL_SHOW_SCREEN);
        $painelScreen->setController($controller);
        foreach ($tileTree as $model) {
            $this->assertNotNull($model, "model null");
            $painelScreen->setTileTree($model);
        }
        $ret = $painelScreen->generate();
        $this->assertNotNull($ret, "retorno do painel screen null");
        //$ret->mostra();
    }

    function criaTiles($idTileRoot) {
        $controller = getControllerForTable("tile");
        $idTileLeft = $controller->addTile($idTileRoot, TILE_ORIENT_VERTICAL, TILE_SIZE_SMALL);
        $idTileCenter = $controller->addTile($idTileRoot, TILE_ORIENT_HORIZONTAL, TILE_SIZE_MEDIUM);
        $idTileCenterTop = $controller->addTile($idTileCenter, TILE_ORIENT_VERTICAL, TILE_SIZE_SMALL);
        $idTileCenterCenter = $controller->addTile($idTileCenter, TILE_ORIENT_VERTICAL, TILE_SIZE_MEDIUM);
        $idTileCenterBottom = $controller->addTile($idTileCenter, TILE_ORIENT_VERTICAL, TILE_SIZE_SMALL);
        $idTileRight = $controller->addTile($idTileRoot, TILE_ORIENT_VERTICAL, TILE_SIZE_SMALL);

        $this->assertNotNull($idTileLeft, "idTileLeft null");
        $this->assertNotNull($idTileCenter, "idTileCenter null");
        $this->assertNotNull($idTileRight, "idTileRight null");

        $screenController = getControllerForTable("screen");
        $tileTree = $screenController->getTileTree(SCREEN_TESTE_AUTO);
        $this->assertNotNull($tileTree, "tileTree null");

        $tot = $this->verificaTree($tileTree);
        $this->assertEqual($tot, 7, "Total de models diferente de 7:$tot");
        return $tileTree;
    }

    function verificaTree($array, $esp = "--") {
        $tot = sizeof($array);
        foreach ($array as $model) {
            $arr = $model->getChildren();
            $tot+= $this->verificaTree($arr, $esp . "--");
            // write($esp . " " . $model->getId() . " tot: $tot");
        }
        return $tot;
    }

    function testScreenDefault() {
        $controller = getControllerForTable("screen");

        $arr = array("projeto" => PROJETO_TESTE, "caption_screen" => "Caption da Screen", "nm_screen" => "teste de inclusao de tela");
        $this->verificaInclusaoRemocao($arr, $controller, "screen");
    }

    function testTileDefault() {
        $controller = getControllerForTable("tile");

        $arr = array("id_screen" => SCREEN_TESTE_AUTO, "id_tile_size" => TILE_SIZE_MEDIUM, "id_tile_orientation" => TILE_ORIENT_VERTICAL, "id_tile_parent" => null, "seq_tile" => null);
        $this->verificaInclusaoRemocao($arr, $controller, "tile");
    }

    function testPainel() {
        $controller = getControllerForTable("painel");
        $this->assertNotNull($controller, "controller null");

        $arr = $controller->getArrayActions(PAINEL_TESTE_H);
        $size = sizeof($arr);
        $this->assertTrue($size > 0, "size array actions PAINEL_TESTE_H=0");

        $arr = $controller->getArrayActions(PAINEL_TESTE_V);
        $size = sizeof($arr);
        $this->assertTrue($size == 0, "size array actions PAINEL_TESTE_V!=0:$size");

        $modelAction = $controller->getLinkWithId(ACAO_TP_EDIT);
        $this->assertNotNull($modelAction, "modelAction nulo");

        // $this->assertTrue($controller->checkPainelHasAction(PAINEL_TESTE_H, ACAO_TP_EDIT), "checkPainelHasAction(PAINEL_TESTE_H,ACAO_TP_EDIT)");
        // $this->assertFalse($controller->checkPainelHasAction(PAINEL_TESTE_V, ACAO_TP_EDIT), "checkPainelHasAction(PAINEL_TESTE_V,ACAO_TP_EDIT)");
    }

    function testPainelInclusao() {
        $controller = getControllerForTable("painel");
        $name = $controller->getDescricao(PAINEL_TESTE_H);
        $nameEsp = "painel_horizontal_default";
        $this->assertEqual($name, $nameEsp, "Nome Painel: $name diferente de: $nameEsp");


        $arr = array(
            "nm_painel" => "TestePainelAuto", "id_tipo_painel" => 1, "desc_painel" => "descricao do painel", "classe_painel" => "classe qualquer");
        $this->verificaInclusaoRemocao($arr, $controller, "painel");
    }

    function testScreenInclusao() {
        $controller = getControllerForTable("screen");

        $arr = array(
            "projeto" => PROJETO_TESTE, "nm_screen" => "ScreenAutoInsere", "caption_screen" => "Caption da screen");
        $this->verificaInclusaoRemocao($arr, $controller, "screen");
    }

}

?>
