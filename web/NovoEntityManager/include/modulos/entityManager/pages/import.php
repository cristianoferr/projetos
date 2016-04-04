<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

adicionaModuloCarga(MODULO_TAREFAS);

inicializa();
definePaginaAtual(PAGINA_IMPORT);

$acao = acaoAtual();
$projeto = projetoAtual();
$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$userController = $controllerManager->getControllerForTable("usuario");
$projetoController = $controllerManager->getControllerForTable("projeto");

$modo = modoAtual();
define("ABA_IMPORT_SQL", "sql");
define("MODO_IMPORT", "modo_import");
define("PK_TAG", "pk__");
define("FK_TAG", "fk__");

escreveHeader();


if ($acao == ACAO_INSERIR) {
    $sql_import = $_POST['sql_import'];
    $parser = new ImportParser($sql_import, $projeto);

    $parser->extraiElementos();
    $parser->trataTables();

    $pos = strpos($sql, "create table");
}

if (!$acao) {


    $painel = $painelManager->getPainel(PAINEL_IMP_PROJETO);
    $painel->adicionaInputForm("projeto", $projeto);
    $painel->setPaginaInclusao(getHomeDir() . "import");
    $painel->setController($projetoController);
    $painel->setTitulo(translateKey("txt_import"));
    $painel->mostra();
}

escreveFooter();
?>