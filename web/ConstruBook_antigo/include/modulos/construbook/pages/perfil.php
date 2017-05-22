<?

include $_SERVER['DOCUMENT_ROOT']."/dadoConstrubook.php";include $rootSite.'/include/include.php';
inicializa();
definePaginaAtual(PAGINA_FORNECEDOR);

$acao = acaoAtual();

$painelManager = getPainelManager();
$controller = getControllerForTable("fornecedor");

$id = $_GET['fornecedor'];
$id = validaNumero($id, "id em fornecedor");


if ($acao == ACAO_PERFIL_CLIENTE) {
    $controller->setPerfilAtual(PERFIL_CLIENTE);
}
if ($acao == ACAO_PERFIL_FORNECEDOR) {
    if ($controller->usuarioEhFornecedor($id)) {
        $controller->setPerfilAtual($id);
    } else {
        erroFatal("Usuário não é o fornecedor $id");
    }
}
redirect(getHomeDir());
