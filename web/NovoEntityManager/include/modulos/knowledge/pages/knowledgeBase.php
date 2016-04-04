<?
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

inicializa();
definePaginaAtual(PAGINA_KNOWLEDGE_BASE);
$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();
$acao = acaoAtual();


escreveHeader();


Out::titulo(translateKey("know_knowledge_base"));

$controller = getControllerForTable("pagina");
$controller->loadPaginasComArtigos();
while ($model = $controller->next()) {
?><h2><? echo translateKey($model->getValorCampo("nminterno_pagina")); ?></h3><?
    getWidgetManager()->showWidget(WIDGET_LISTA_ARTIGOS_PAGINA_SUMARIO, $model->getValorCampo("id_pagina"));
}

escreveFooter();
?>