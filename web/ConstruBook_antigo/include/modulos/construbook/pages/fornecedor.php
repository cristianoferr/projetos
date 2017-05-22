<?
include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';

inicializa();
definePaginaAtual(PAGINA_FORNECEDOR);

$acao = acaoAtual();

$painelManager = getPainelManager();
$controller = getControllerForTable("fornecedor");

$id = $_GET['fornecedor'];
$id = validaNumero($id, "id em fornecedor");

escreveHeader();

$bread = $painelManager->getBread();
$bread->addLink(translateKey('txt_fornecedores'), "fornecedor");

//ACAO_CONSULTA - mostra os fornecedores
if ($acao == ACAO_CONSULTA) {
    listaFornecedores($controller, $painelManager, $bread);
}
//ACAO_VISUALIZA
if ($acao == ACAO_VISUALIZA) {
    visualizaFornecedor($controller, $painelManager, $bread, $id);
}
//ACAO_NOVO
if ($acao == ACAO_NOVO) {
    
}
//ACAO_BUSCA
if ($acao == ACAO_BUSCA) {
    buscaFornecedores($controller, $painelManager, $bread);
}


function buscaFornecedores($controller, $painelManager, $bread) {
    $bread->addLink(translateKey('txt_buscar_fornecedores'), "fornecedor/busca");
    $bread->mostra();
    getWidgetManager()->showWidget(WIDGET_BUSCA_FORNECEDOR);
}
function listaFornecedores($controller, $painelManager, $bread) {
    $bread->mostra();
    $painel = $painelManager->getPainel(PAINEL_LISTA_FORNECEDORES);
    $painel->setController($controller);
    $controller->listaFornecedoresConectados($painel);
    $painel->mostra();
}

function visualizaFornecedor($controller, $painelManager, $bread, $id) {
    getWidgetManager()->showWidget(WIDGET_VISUALIZA_FORNECEDOR,$id);
    $opcao=opcaoAtual();
    if (!$opcao){
        $opcao=OPCAO_MURAL;
    }
    if ($opcao==OPCAO_MURAL){
        
        getWidgetManager()->showWidget(WIDGET_VISUALIZA_MURAL_FORNECEDOR,$id);
    }
    if ($opcao==OPCAO_PRODUTOS){
        getWidgetManager()->showWidget(WIDGET_LISTA_PRODUTOS_FORNECEDOR,$id);
    }
    if ($opcao==OPCAO_PARCEIROS){
        getWidgetManager()->showWidget(WIDGET_VISUALIZA_PARCEIROS_FORNECEDOR,$id);
    }
}

escreveFooter();
