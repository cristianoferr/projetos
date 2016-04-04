<?

//Tipo de Diagrama UML
define("TIPO_DIAGRAMA_ER", "1");
define("TIPO_DIAGRAMA_WBS", "3");

define("ACAO_ATUALIZA_ELEMENTOS", "atu_ele");

define("DEP_FK", "fk");
define("DEP_EXTENDS", "extends");


define("TIPO_COMPONENTE_PROJETO", "PROJ");
define("TIPO_COMPONENTE_ENTIDADE", "ENT");
define("TIPO_COMPONENTE_CAMADA", "CAMADA");
define("TIPO_COMPONENTE_PROPRIEDADE", "PROP");
define("TIPO_COMPONENTE_FUNCAO", "FUNC");
define("TIPO_COMPONENTE_ENTREGAVEL", "ENTREG");
define("TIPO_COMPONENTE_TITULO", "titulo");

define("WIDGET_EDIT_DIAGRAMA", 'EDIT_DIAGRAMA');


define("ICONE_PK", "key.png");
define("ICONE_OPCIONAL", "rectangle_empty.png");
define("ICONE_OBRIGATORIO", "rectangle_filled.png");

define("MAX_WIDTH", 3800);
define("MAX_HEIGHT", 2000);
define("MIN_HEIGHT", 100);
define("MIN_WIDTH", 50);

function diagramaAtual() {
    $id = $_GET["diagrama"];

    if ($id == "") {
        $id = $_POST["diagrama"];
    }
    return $id;
}

?>