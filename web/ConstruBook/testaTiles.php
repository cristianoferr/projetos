<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';

inicializa();

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();
$acao = acaoAtual();


escreveHeader();
?>
<style>
    div{
        border:black dotted 1px;
    }

    .row1 {
        display: table-row;
        border:black dotted 1px;
    }
    .col1 {
        display: table-cell;
        border:black dotted 1px;
    }


</style>
<hr>
<?

$screenController = getControllerForTable("screen");
getControllerForTable("screen")->resetScreen(1);
$controller = getControllerForTable("tile");


$idTileRoot = $controller->addRootTile(1, TILE_ORIENT_HORIZONTAL, TILE_SIZE_MEDIUM);
$tileTree = criaTiles($idTileRoot);
//$tileTree = $screenController->getTileTree(1);
$painelScreen = getPainelManager()->getPainel(PAINEL_SHOW_SCREEN);
$painelScreen->setController($controller);
foreach ($tileTree as $model) {
    $painelScreen->setTileTree($model);
}
$painelScreen->mostra();

function criaTiles($idTileRoot) {
    $controller = getControllerForTable("tile");
    $idTileLeft = $controller->addTile($idTileRoot, TILE_ORIENT_HORIZONTAL, TILE_SIZE_SMALL, "esquerda");

    $idTileCenter = $controller->addTile($idTileRoot, TILE_ORIENT_HORIZONTAL, TILE_SIZE_MEDIUM, "meio");
    $idTileCenterTop = $controller->addTile($idTileCenter, TILE_ORIENT_VERTICAL, TILE_SIZE_SMALL, "meio topo");

    $idTileCenterCenter = $controller->addTile($idTileCenter, TILE_ORIENT_VERTICAL, TILE_SIZE_MEDIUM, "meio meio");

    $idTileCenterBottom = $controller->addTile($idTileCenter, TILE_ORIENT_VERTICAL, TILE_SIZE_SMALL, "meio fundo");

    $idTileCenterBottom1 = $controller->addTile($idTileCenterBottom, TILE_ORIENT_HORIZONTAL, TILE_SIZE_SMALL, "meio fundo topo");
    $idTileCenterBottom2 = $controller->addTile($idTileCenterBottom, TILE_ORIENT_HORIZONTAL, TILE_SIZE_SMALL, "meio fundo meio");
    $idTileCenterBottom3 = $controller->addTile($idTileCenterBottom, TILE_ORIENT_HORIZONTAL, TILE_SIZE_SMALL, "meio fundo fundo");

    $idTileCenterBottomCenter1 = $controller->addTile($idTileCenterBottom2, TILE_ORIENT_VERTICAL, TILE_SIZE_SMALL, "meio fundo topo");
    $idTileCenterBottomCenter2 = $controller->addTile($idTileCenterBottom2, TILE_ORIENT_VERTICAL, TILE_SIZE_SMALL, "meio fundo meio");
    $idTileCenterBottomCenter3 = $controller->addTile($idTileCenterBottom2, TILE_ORIENT_VERTICAL, TILE_SIZE_SMALL, "meio fundo fundo");

    $idTileRight = $controller->addTile($idTileRoot, TILE_ORIENT_HORIZONTAL, TILE_SIZE_SMALL, "direita");

    $screenController = getControllerForTable("screen");
    $tileTree = $screenController->getTileTree(1);

    return $tileTree;
}

escreveFooter();
?>