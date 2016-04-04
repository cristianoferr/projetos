<?

///	$flagEdicao=verificaFlagDebug();
//tmp_verificaProjetoUsuario();

function getManager() {
    //echo isset($_SESSION["media_controller"]);
    //unset($_SESSION["media_controller"]);
    if (isset($GLOBALS["main_manager"]))
        return $GLOBALS["main_manager"];
    $controller = new MainManager();


    $GLOBALS["main_manager"] = $controller;

    return $GLOBALS["main_manager"];
}

function showError($codErro) {
    redirect(getHomeDir() . "error/$codErro");
}

function getPainelManager() {
    return getManager()->getPainelManager();
}

function getControllerManager() {
    return getManager()->getControllerManager();
}

function getTableManager() {
    return getManager()->getTableManager();
}

function getWidgetManager() {
    return getManager()->getWidgetManager();
}

function translateKey($key) {
    return getManager()->translateKey($key);
}

function getControllerForTable($tableName) {//coberto
    return getControllerManager()->getControllerForTable($tableName);
}

function modoAtual() {
    $id = $_GET["modo"];

    if ($id == "") {
        $id = $_POST["modo"];
    }

    if ($id == "") {
        $id = $GLOBALS["modo"];
    }

    return $id;
}

function parametroAtual() {
    $id = $_GET["parametro"];

    if ($id == "") {
        $id = $_POST["parametro"];
    }
    return $id;
}

function getCurrentOrderBy() {
    $id_entidade = entidadeAtual();
    return $_SESSION["orderBy$id_entidade"];
}

function getValor($id_registro, $id_coluna) {
    $rowValor = executaQuerySingleRow("select valor_coluna from valor where id_coluna_pai=$id_coluna and id_registro=$id_registro");
    return $rowValor['valor_coluna'];
}

?>