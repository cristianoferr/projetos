<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';

inicializa();
definePaginaAtual(PAGINA_ERRO);

$id_projeto = projetoAtual();
$id_entidade = entidadeAtual();
$acao = acaoAtual();


escreveHeader();
$erro = $_GET['erro'];
if ($erro == ERROR_COD_PROJETO_EXISTE) {
    $msg = translateKey("txt_error_cod_projeto_existe");
}
if ($erro == ERROR_INVALID_TICKET) {
    $msg = translateKey("error_invalid_ticket_description");
}

Out::painel(translateKey("txt_error_page"), $msg);

escreveFooter();
?>