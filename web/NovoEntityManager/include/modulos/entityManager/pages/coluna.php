<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

inicializa();
definePaginaAtual(PAGINA_PROPRIEDADE);

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();



$id_coluna = $_GET['coluna'];
$acao = acaoAtual();

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$controller = $controllerManager->getControllerForTable("coluna");


//echo "acao:$acao";
if ($acao == ACAO_INSERIR) {
    $painel = $painelManager->getPainel(PAINEL_FORM_INCLUSAO_COLUNA);
    $arr = $painel->getArrayWithPostValues();
    /*
      $nm_coluna=$_POST['nm_coluna'];
      $dbname_coluna=$_POST['dbname_coluna'];
      $prop_name_coluna=$_POST['prop_name_coluna'];
      $id_entidade_combo=$_POST['id_entidade_combo'];
      $id_sql_coluna=$_POST['id_sql_coluna'];
      $id_acesso_coluna=$_POST['id_acesso_coluna'];
      $id_relacao_datatype=$_POST['id_relacao_datatype'];
      $id_datatype =$_POST['id_datatype'];

      $arr=array( 'id_entidade_pai' => $id_entidade,
      'nm_coluna'=>$nm_coluna,
      'dbname_coluna'=>$dbname_coluna,
      'prop_name_coluna'=>$prop_name_coluna,
      'id_entidade_combo'=>$id_entidade_combo,
      'id_sql_coluna'=>$id_sql_coluna,
      'id_acesso_coluna'=>$id_acesso_coluna,
      'id_relacao_datatype'=>$id_relacao_datatype,
      'id_datatype'=>$id_datatype);
     */
    //printArray($arr);
    $id_coluna = $controller->insereRegistro($arr);

    redirect(getHomeDir() . "entity/$id_projeto/$id_entidade");
}

if ($acao == ACAO_EXCLUIR) {
    $controller->excluirRegistro($id_coluna);
    redirect(getHomeDir() . "entity/$id_projeto/$id_entidade");
}

escreveHeader();


$bread = $painelManager->getBread();
if ($id_coluna != "") {
    $nm = $controller->getDescricao($id_coluna);
    $bread->addLink($nm, "property/$id_projeto/$id_entidade/$id_coluna");
}


validaAcesso("coluna", $id_coluna);
validaAcesso("entidade", $id_entidade);


if ($acao == ACAO_NOVO) {

    $painel = $painelManager->getPainel(PAINEL_FORM_INCLUSAO_COLUNA);
    $painel->setController($controller);
    $painel->setTitulo(translateKey("txt_new_property"));
    $painel->adicionaLinkImportante(getHomeDir() . "entity/$id_projeto/$id_entidade", translateKey("txt_back"), false);

    $bread->addLink(translateKey("txt_new_property"), "property/new/$id_projeto/$id_entidade");
    $bread->mostra();

    $painel->mostra();
} else {
    $bread->mostra();
}




if (!$acao) {
    //Painel projeto
    $painel = $painelManager->getPainel(PAINEL_COLUNA);
    $painel->setController($controller);
    $model = $controller->loadSingle($id_coluna, $painel);
    $painel->setTitulo($model->getDescricao());
    $painel->adicionaLink(getHomeDir() . "property/new/$id_projeto/$id_entidade", translateKey("txt_new_property"), false);

    $painel->adicionaLinkImportante(getHomeDir() . "entity/$id_projeto/$id_entidade", translateKey("txt_back"), false);
    $painel->mostra();
}


escreveFooter();
?>