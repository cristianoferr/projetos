<?

include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';
adicionaModuloCarga(MODULO_DIAGRAMAS);
adicionaModuloCarga(MODULO_TAREFAS);

inicializa();
definePaginaAtual(PAGINA_MATRIZ_DEPENDENCIA);

$id_projeto = projetoAtual();

escreveHeader();

$controller = new MatrizDependenciaController(null);
$controller->criaMatrizDependencia($id_projeto);

$painel = new MatrizDependenciaView();
$painel->setTitulo(translateKey("txt_matriz_dependencia"));
$painel->setController($controller);
$painel->mostra();

escreveFooter();
?>