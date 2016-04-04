<?

//determinando o ambiente atual
define("DOMAIN", "projetize.com");
define("AMBIENTE_PRODUCAO", "server");
define("AMBIENTE_LOCAL", "windows");

if ((strpos(getPaginaAtual(), "macmini"))) {
    define("AMBIENTE", "macmini");
} else if ((strpos(getPaginaAtual(), "127.0.0.1")) || (strpos(getPaginaAtual(), "localhost"))) {
    $url = "127.0.0.1";
    define("AMBIENTE", AMBIENTE_LOCAL);
} else if ((strpos(getPaginaAtual(), "192.168.1.109"))) {
    $url = "192.168.1.109";
    define("AMBIENTE", AMBIENTE_LOCAL);
} else {
    define("AMBIENTE", AMBIENTE_PRODUCAO);
}

if (AMBIENTE == "macmini") {
    define("SITE_URL", "http://macmini.local");
} else if (AMBIENTE == AMBIENTE_LOCAL) {
    define("SITE_URL", "http://$url".WEB_FOLDER);
} else {
    define("SITE_URL", "http://" . DOMAIN);
}
/*
  if (AMBIENTE == AMBIENTE_PRODUCAO) {
  define("USE_MINIFIED", true);
  } else { */
define("USE_MINIFIED", false);
//}


define("EMAIL_LOST_PASSWORD", "dont-reply@" . DOMAIN);

define("SITE_SALT", "4390DSFt309");
define("VERSAO_ATUAL", "0.2");

define("USUARIO_PESSOAL", 3);

define("PREF_INTERFACE_USUARIO", "interface_");


define("PROJETO_TESTE", 131);
define("REGISTRO_TESTE", 51);
define("ENTIDADE_TESTE", 388);
define("COLUNA_TESTE", 1379);
define("FUNCAO_TESTE", 3);
define("TAREFA_TESTE", 18);
define("ENTREGAVEL_TESTE", 139);
define("ENTREGAVEL_TESTE_RAIZ", 138);
define("PAGINA_TESTE", 35); //pagina principal
define("ELEMENTO_TESTE", 54); //pagina principal
define("ARTIGO_TESTE", 14); //pagina principal
define("SCREEN_TESTE_AUTO", 1);
define("SCREEN_TESTE", 2);
define("TILE_TESTE", 1);
define("WIDGET_TESTE_H", 1);
define("WIDGET_TESTE_V", 2);
define("WIDGET_TESTE_SECTION", 3);
define("PAINEL_TESTE_H", 1);
define("PAINEL_TESTE_V", 2);
define("ACAO_TP_EDIT", 1);
define("ACAO_TP_DELETE", 2);
define("ACAO_TP_NEW", 3);
//CHECK_PERM_PROJETO
//triangle up: &#9650;
//triangle down: &#9660;
?>