<?php

$contaQueries = 0;
$contaTables = 0;
$startTime = microtime();
$startTime = explode(' ', $startTime);
$startTime = $startTime[1] + $startTime[0];

define("WEB_FOLDER",'/ConstruBook');
$ROOT_FOLDER=$_SERVER['DOCUMENT_ROOT'] .WEB_FOLDER;

//Esse arquivo tem que ser o mais gen�rico poss�vel para f�cil reuso
session_start();
include $ROOT_FOLDER . '/include/include_utils.php';
include $ROOT_FOLDER . '/include/consts.php';
include $ROOT_FOLDER . '/include/configura_arquivos.php';


include $ROOT_FOLDER . '/include/include_banco_pdo.php';
include $ROOT_FOLDER . '/include/htmltext.php';
include $ROOT_FOLDER . '/include/include_especifico.php';

function getHomeDir() {
    return SITE_URL . "/";
}

function acaoAtual() {
    $v = $_GET['acao'] . $_POST['acao'];
    writeDebug("acao: $v");
    return $v;
}

function opcaoAtual() {
    $v = $_GET['opcao'] . $_POST['opcao'];
    writeDebug("opcao: $v");
    return $v;
}


function writeAdmin($titulo, $txt = null) {
    if (!isAdmin())
        return;
    write($titulo, $txt);
}

function write($titulo, $txt = null) {
    echo "<p><font color='red'>" . get_caller_info() . "-&gt;</font><b>$titulo</b>::$txt::</p>" . PHP_EOL;
}

function writeErro($titulo, $texto = null) {
    writeAdmin("ERRO:" . $titulo, $texto);
}

function printArray($arr) {
    $c = 0;
    foreach ($arr as $key => $value) {
        //writeAdmin("$c - $key",$value);
        echo "$c - $key::$value <br>";
        $c++;
    }
    echo "Tamanho:" . sizeof($arr) . " <br>";
}

function usuarioAtual() {
    if (!$_SESSION["id_usuario"])
        $_SESSION["id_usuario"] = GUEST_ID;
    return $_SESSION["id_usuario"];
}

function isGuest() {
    return (usuarioAtual() == GUEST_ID);
}

function isAdmin() {
    return ($_SESSION["id_usuario"] == 1);
}

function isExternalUser() {
    if (isAdmin())
        return false;
    if (AMBIENTE != AMBIENTE_PRODUCAO)
        return false;
    if (usuarioAtual() == USUARIO_PESSOAL)
        return false;
    return true;
}

function inicializa() {
    incluiArquivos();
    definePaginaAtual(PAGINA_DEFAULT);
    //carregaTableManager();
}

?>