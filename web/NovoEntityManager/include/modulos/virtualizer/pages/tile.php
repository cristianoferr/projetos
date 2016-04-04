<?php

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_VIRTUALIZER);
inicializa();
definePaginaAtual(PAGINA_SCREENS);
$controller = getControllerForTable("tile");


$id_projeto = projetoAtual();
$acao = acaoAtual();

if ($acao == ACAO_INSERIR) {
    $painel = getPainelManager()->getPainel(PAINEL_ADD_TILE);
    $painel->adicionaInputForm("projeto", $id_projeto);
    $painel->adicionaInputForm("tile_pai", $_POST['tile_pai']);
    $arr = $painel->getArrayWithPostValues();

    //printArray($arr);
    $id_screen = $controller->insereRegistroMulti($arr);
    redirect(getHomeDir() . "screens/$id_projeto/" . $id_screen);
}

if ($acao == ACAO_EXCLUIR) {
    $model = $controller->loadSingle($_GET['tile']);
    $screen = $model->getValorCampo("id_screen");
    $controller->excluirRegistro($_GET['tile']);
    redirect(getHomeDir() . "screens/" . projetoAtual() . "/$screen");
}
?>