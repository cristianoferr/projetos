<?php

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_VIRTUALIZER);
inicializa();
definePaginaAtual(PAGINA_WIDGETS);
$controller = getControllerForTable("widget");


$id_projeto = projetoAtual();
$acao = acaoAtual();
$widget = widgetAtual();

if ($acao == ACAO_EXCLUIR) {
    $controller->excluirRegistro($tela);
    redirect(getHomeDir() . "widgets/" . projetoAtual());
}

if ($widget) {
    validaAcesso("widget", $widget);
} else {
    validaAcesso("projeto", $id_projeto);
}


escreveHeader();
$bread = getPainelManager()->getBread();
$bread->addLink(translateKey("txt_widgets"), "widgets/$id_projeto");

if ($acao == ACAO_LISTAR) {
    $bread->mostra();
    getWidgetManager()->showWidget(WIDGET_LIST_WIDGETS, $id_projeto);
}

if ($acao == ACAO_EDITAR) {

    $modelScreen = $controller->loadSingle($widget);
    $bread->addLink($modelScreen->getDescricao(), "widgets/" . projetoAtual() . "/$widget");
    $bread->mostra();

    getWidgetManager()->showWidget(WIDGET_EDIT_WIDGET, $widget);
}

escreveFooter();
?>