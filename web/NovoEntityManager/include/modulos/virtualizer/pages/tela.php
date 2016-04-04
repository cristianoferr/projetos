<?php

define("ABA_SCREEN_PREVIEW", 'P');
define("ABA_SCREEN_EDIT", 'E');



include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_VIRTUALIZER);
inicializa();
definePaginaAtual(PAGINA_SCREENS);
$controller = getControllerForTable("screen");


$id_projeto = projetoAtual();
$acao = acaoAtual();
$tela = telaAtual();

if ($acao == ACAO_INSERIR) {
    $painel = getPainelManager()->getPainel(PAINEL_FORM_INC_SCREEN);
    $painel->adicionaInputForm("projeto", $id_projeto);
    $arr = $painel->getArrayWithPostValues();

    $id = $controller->insereRegistro($arr);
    redirect(getHomeDir() . "screens/$id_projeto/$id");
}

if ($acao == ACAO_EXCLUIR) {
    $controller->excluirRegistro($tela);
    redirect(getHomeDir() . "screens/" . projetoAtual());
}


escreveHeader();
$bread = getPainelManager()->getBread();
$bread->addLink(translateKey("txt_screens"), "screens/$id_projeto");

if ($acao == ACAO_LISTAR) {
    $bread->mostra();

    getWidgetManager()->showWidget(WIDGET_LIST_SCREENS, $id_projeto);
}

if ($acao == ACAO_EDITAR) {


    $modelScreen = $controller->loadSingle($tela);
    $bread->addLink($modelScreen->getDescricao(), "screens/" . projetoAtual() . "/$tela");
    $bread->mostra();
    $ABA_SCREEN = desenhaAbas($tela);
    getWidgetManager()->showWidget(WIDGET_SHOW_SCREEN, $tela);
}

if ($acao == ACAO_NOVO) {
    $bread->addLink(translateKey("txt_new_screen"), "screens/new/" . projetoAtual());
    $bread->mostra();
    getWidgetManager()->showWidget(WIDGET_NEW_SCREEN, projetoAtual());
}

escreveFooter();

/////

function desenhaAbas($tela) {

    $viewAba = new ViewAba("modo_tela_" . $tela);
    $viewAba->setParam("modo");
    $viewAba->setDefaultValue(ABA_SCREEN_PREVIEW);
    //echo $abaAtual;
    $viewAba->ativaVisaoUnica(getHomeDir() . "screens/" . projetoAtual() . "/$tela/");
    $viewAba->adicionaAba(ABA_SCREEN_PREVIEW, translateKey("txt_screen_preview"), true);
    $viewAba->adicionaAba(ABA_SCREEN_EDIT, translateKey("txt_screen_edit"), false);
    $abaAtual = $viewAba->getAbaAtual();
    $viewAba->desenhaAbas();
    $viewAba->start();

    return $abaAtual;
}
?>