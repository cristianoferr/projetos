<?

/*
  define("OPCAO_MURAL", "mural");
  define("OPCAO_PRODUTOS", "produtos");
  define("OPCAO_PARCEIROS", "parceiros"); */

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
inicializa();
definePaginaAtual(PAGINA_MANTER_TABELA);

$acao = acaoAtual();

$painelManager = getPainelManager();

validaFornecedor();

/*
  if ($acao == ACAO_INSERIR) {
  $painel = $painelManager->getPainel(PAINEL_FORM_INC_FORNECEDOR);
  $arr = $painel->getArrayWithPostValues();

  $id = $controller->insereRegistro($arr);
  redirect(getHomeDir() . "fornecedor/$id");
  }
 */
/*
  if ($acao == ACAO_EXCLUIR) {
  $controller->excluirRegistro($id_entidade);
  redirect(getHomeDir() . "abtest");
  }
 */
$idFornecedor = fornecedorAtual();
$idFornecedor = validaNumero($idFornecedor, "id em ManterFornecedor");

escreveHeader();

$bread = $painelManager->getBread();
$bread->addLink(translateKey('txt_fornecedor'), "fornecedor");
$bread->addLink(translateKey('txt_tabelas'), "fornecedor/tabelas");

manterTabela($bread, $idFornecedor);

function manterTabela($bread, $idFornecedor) {

    $idTabela = validaNumero($_GET['idTabela']);
    if ($idTabela != "null") {
        $bread->addLink(translateKey('txt_tabela_calculo'), "fornecedor/tabela/$idTabela");
        $bread->mostra();
        getWidgetManager()->showWidget(WIDGET_MANTER_TABELA, $idTabela);
    } else {
        $bread->mostra();
        getWidgetManager()->showWidget(WIDGET_MANTER_TABELAS, $idFornecedor);
    }
}

escreveFooter();
