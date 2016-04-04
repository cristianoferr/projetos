<?

define("ERROR_ID_EXISTS", translateKey("ERROR_ID_EXISTS"));
define("ERROR_EMAIL_EXISTS", translateKey("register_email_exists"));

// pega da tabela primitive type
define("PRIMITIVE_INT", 1);
define("PRIMITIVE_SHORT_TEXT", 2);
define("PRIMITIVE_TEXT", 3);
define("PRIMITIVE_BOOLEAN", 4);
define("PRIMITIVE_DATE", 5);
define("PRIMITIVE_FLOAT", 6);
define("PRIMITIVE_CHAR", 8);
define("PRIMITIVE_ENTITY", 9);

// pega da tabela RELACAO_DATATYPE
define("RELACAO_DATATYPE_PK", 1);
define("RELACAO_DATATYPE_OBRIGATORIO", 2);
define("RELACAO_DATATYPE_OPCIONAL", 3);

define("TIPO_BANCO_SQLITE", 1);
define("TIPO_BANCO_MYSQL", 2);

define("PAPEL_PROJETO_ADMIN", 1);
define("PAPEL_PROJETO_EDITOR", 2);

define("LINGUAGEM_PADRAO", 8);

define("CAMADA_MODEL", 1);
define("CAMADA_VIEW", 2);
define("CAMADA_CONTROLLER", 3);
define("CAMADA_PRESENTER", 4);

define("SQL_COLUNA_CRIA_EXPORTA", 1);
define("SQL_COLUNA_INFO", 2);
define("SQL_COLUNA_CRIA", 3);

define("ACESSO_COLUNA_NONE", 1);
define("ACESSO_COLUNA_LOCAL", 2);
define("ACESSO_COLUNA_PRIVATE", 3);
define("ACESSO_COLUNA_PUBLIC", 4);


define("ENTIDADE_DB_NAME", 'dbname_entidade');

function entidadeAtual() {
    $id_entidade = $_GET["entidade"]; //Glossario

    if ($id_entidade == "") {
        $id_entidade = $_POST["entidade"];
    }

    if ($id_entidade == "") {
        //	$id_entidade=$_SESSION["entidade"];
    }

    if ($_GET["orderBy"] != "") {
        $_SESSION["orderBy$id_entidade"] = $_GET["orderBy"];
    }
    //echo "entidadeatual: $id_entidade";
    writeDebug("entidadeatual: $id_entidade");
    return $id_entidade;
}

function funcaoAtual() {

    $id = $_GET["funcao"];

    if ($id == "") {
        $id = $_POST["funcao"];
    }
    return $id;
}

function projetoAtual() {
    if (isset($_GET["projeto"]))
        $id_projeto = $_GET["projeto"];

    if (!isset($id_projeto)) {
        if (isset($_POST["projeto"]))
            $id_projeto = $_POST["projeto"];
    }


    if (!isset($id_projeto)) {
        $id_projeto = $_SESSION["projeto"];
    }

    if ($id_projeto != "") {
        $id_projeto = validaNumero($id_projeto, "id_projeto do projetoAtual");
        $_SESSION["projeto"] = $id_projeto;
    }
    //writeDebug("Projeto Atual: $id_projeto");


    return $id_projeto;
}

function projetoMestreAtual() {
    $id_projeto_mestre = $_GET["pm"];

    if ($id_projeto_mestre == "") {
        $id_projeto_mestre = $_POST["projeto_mestre"];
    }
    if ($id_projeto_mestre == "") {
        //	$id_projeto_mestre=$_SESSION["projeto_mestre"];
    }

    if ($id_projeto_mestre == "") {
        $id_projeto_mestre = projetoAtual();
    }

    if ($id_projeto_mestre != "") {
        //	$_SESSION["projeto_mestre"]=$id_projeto_mestre;
    }


    return $id_projeto_mestre;
}

?>