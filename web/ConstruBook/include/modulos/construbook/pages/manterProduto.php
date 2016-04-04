<?



/*
  define("OPCAO_MURAL", "mural");
  define("OPCAO_PRODUTOS", "produtos");
  define("OPCAO_PARCEIROS", "parceiros"); */

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
inicializa();
definePaginaAtual(PAGINA_MANTER_PRODUTO);

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


manterProdutos($bread, $idFornecedor);

function manterProdutos($bread, $idFornecedor) {
    $bread->addLink(translateKey('txt_produtos'), "fornecedor/produtos");

    $idProduto = validaNumero($_GET['idProduto']);
    if ($idProduto != "null") {
        $bread->addLink(translateKey('txt_produto'), "fornecedor/produto/$idProduto");
        $bread->mostra();
        getWidgetManager()->showWidget(WIDGET_MANTER_PRODUTO, $idProduto);
    } else {
        $bread->mostra();
        getWidgetManager()->showWidget(WIDGET_MANTER_PRODUTOS, $idFornecedor);
    }
}

escreveFooter();
