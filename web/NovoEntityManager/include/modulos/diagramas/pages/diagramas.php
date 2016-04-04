<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_DIAGRAMAS);
adicionaModuloCarga(MODULO_TAREFAS);
inicializa();
definePaginaAtual(PAGINA_DIAGRAMAS);

$id_projeto = projetoAtual();
$acao = acaoAtual();
$id_tarefa = $_GET['tarefa'];
$id_diagrama = diagramaAtual();


$controllerManager = getControllerManager();
$entidadeController = $controllerManager->getControllerForTable("entidade");
$diagramController = $controllerManager->getControllerForTable("diagrama");
$painelManager = getPainelManager();
escreveHeader();

$tipoDiagrama = TIPO_DIAGRAMA_ER;

validaAcesso("projeto", $id_projeto);

$bread = $painelManager->getBread();
$bread->addLink(translateKey("txt_diagrams"), "diagrams/$id_projeto");
if ($id_diagrama != "") {
    $nm = $diagramController->getName($id_diagrama);
    $bread->addLink($nm, "diagram/$id_projeto/$id_diagrama");
}
$bread->mostra();


$painel = $painelManager->getPainel(PAINEL_DIAGRAMAS_PROJETO);
$painel->setTitulo(translateKey("txt_diagrams"));
$painel->setController($diagramController);
$diagramController->loadRegistros("", $painel);
$painel->adicionaLink(getHomeDir() . "diagram/new/$id_projeto", translateKey("txt_new_diagram"), false);
$painel->mostra();



escreveFooter();
?>