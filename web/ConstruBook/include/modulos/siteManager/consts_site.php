<?

define("PAGINA_PRINCIPAL", 1);
define("PAGINA_FORNECEDOR", 2);
define("PAGINA_DEFAULT", 3);
define("PAGINA_LOGIN", 4);
define("PAGINA_MANTER_FORNECEDOR", 5);
define("PAGINA_MANTER_CATEGORIA", 6);
define("PAGINA_MANTER_PRODUTO", 7);
define("PAGINA_MANTER_TABELA", 8);


define("RESET_EMAIL", "resetemail");
define("RESET_PASSWORD", "resetpass");
define("RESET_FORM", "resetForm");

define("ALERT_RESET_EMAIL_SENT", "1");
define("ALERT_PASSWORD_RESETED", "2");

$PAGINA_ATUAL = PAGINA_PRINCIPAL;

function definePaginaAtual($idPagina) {
    abTestPageReached($idPagina);
    $GLOBALS['PAGINA_ATUAL'] = $idPagina;
}

function getIDPaginaAtual() {
    return $GLOBALS['PAGINA_ATUAL'];
}

?>