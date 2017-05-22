<?

function encripta($texto) {
    $c = crypt($texto, SITE_SALT);
    return $c;
}

function usuarioPodeEditarProjetoAtual() {
    $v = projetoAtual();
    if ((!isset($v)) || ($v == "")) {
        return false;
    }
    return checkPerm("projeto", projetoAtual(), true);
}

function isUserAdminProjeto($id_projeto) {
    return getControllerManager()->getProjetoController()->isUserAdminProjeto($id_projeto);
}

function projetoModificado($id_project) {
    executaSQL("update projeto set dtultima_acao=now() where id_projeto=?", array($id_project));
}

/**
 * verifica a permissão do modulo indicado
 * @param type $modulo
 * @param type $id_modulo
 * @param type $flagEdit
 * @return boolean
 */
function checkPerm($modulo, $id_modulo, $flagEdit) {
    return getFileManager()->checkPerm($modulo, $id_modulo, $flagEdit);
}

function validaAdmin() {
    if (!isAdmin()) {
        logaAcesso("Tentativa de acesso à pagina de admin", true);
        die("You cant access here... ");
    }
}

function validaAcesso($modulo, $id_modulo) {
    if (!isset($id_modulo)) {
        return;
    }
    if ($id_modulo == "") {
        return;
    }
    if (!checkPerm($modulo, $id_modulo, false)) {
        die("You dont have access here: $modulo<br>");
    }
}

function validaLeitura($modulo, $id_modulo) {
    return validaAcesso($modulo, $id_modulo);
}

/**
 * Valida a escrita: die se não tiver.
 * @param type $modulo
 * @param type $id_modulo
 * @return boolean
 */
function validaEscrita($modulo, $id_modulo) {
    if (!isset($id_modulo)) {
        return false;
    }
    if (!checkPerm($modulo, $id_modulo, true)) {
        erroFatal("You cant change data here: $modulo ($id_modulo)");
    }
}

function checaEscrita($modulo, $id_modulo) {

    if (!isset($id_modulo)) {
        return false;
    }
    return checkPerm($modulo, $id_modulo, true);
}

function checaLeitura($modulo, $id_modulo) {
    if (!isset($id_modulo)) {
        return false;
    }
    return checkPerm($modulo, $id_modulo, false);
}

function validaAdminProjeto($modulo, $id_modulo) {
    return validaEscrita($modulo, $id_modulo);
}

function addLog($log) {
    $log = str_replace("'", "#", $log);
    executaSQL("insert into log (nm_log,data_log,ip_log) values (?,now(),?) ", array($log, getRealIpAddr()));
}

function logaAcesso($desc, $flagUrgente) {
    if (!isAdmin()) {
        executaSQL("insert into log_site(pagina_log_site,ip_log_site,data_log_site,desc_log_site,id_media,flagurgente_log_site,id_usuario) values ('" . getPaginaAtual() . "','" . getRealIpAddr() . "',now(),'$desc.  Referrer:" . $_SERVER['HTTP_REFERER'] . "','.projetoAtual().','" . $flagUrgente . "','" . usuarioAtual() . "')");
    }
}

function writeDebug($v, $v2 = null) {
    if (verificaFlagDebug()) {
        write("debug:$v", $v2);
    }
}

?>