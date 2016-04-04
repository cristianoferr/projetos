<?

define("PAGINA_PRINCIPAL", 1);
define("PAGINA_PROJETOS", 2);
define("PAGINA_PROJETO", 3); //3
define("PAGINA_ENTIDADES", 13);
define("PAGINA_ENTIDADE", 13); //4
define("PAGINA_PROPRIEDADE", 5);
define("PAGINA_FUNCAO", 6);
define("PAGINA_ENTREGAVEL", 8); //7
define("PAGINA_ENTREGAVEIS", 8);
define("PAGINA_TAREFAS", 9);
define("PAGINA_TAREFA", 9); //10
define("PAGINA_DATATYPES", 11);
define("PAGINA_DATATYPE", 11); //12
define("PAGINA_BUILD", 14);
define("PAGINA_DIAGRAMA", 16); //15
define("PAGINA_DIAGRAMAS", 16);
define("PAGINA_ERRO", 17);
define("PAGINA_IMPORT", 18);
define("PAGINA_LOGIN", 19);
define("PAGINA_MATRIZ_DEPENDENCIA", 20);
define("PAGINA_MEMBROS", 21);
define("PAGINA_PARAMETRO_FUNCAO", 22);
define("PAGINA_HEADING_PRINCIPAL", 23);
define("PAGINA_VISUALIZA", 24);
define("PAGINA_USER_PROFILE", 25);
define("PAGINA_ABTESTS", 26);
define("PAGINA_ABTEST", 27);
define("PAGINA_TIPO_ABTEST", 28);
define("PAGINA_USER_REGISTER", 29);
define("PAGINA_USER_REGISTER_COMPLETE", 30);
define("PAGINA_ADMIN", 31);
define("PAGINA_ARTIGO", 32);
define("PAGINA_KNOWLEDGE_BASE", 33);
define("PAGINA_DEFAULT", 34);
define("PAGINA_TESTE", 35);
define("PAGINA_SCREENS", 36);
define("PAGINA_SCREEN", 37);
define("PAGINA_WIDGETS", 40);


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