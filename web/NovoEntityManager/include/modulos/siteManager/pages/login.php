<?php
$GLOBALS["login_page"] = true;
include $_SERVER['DOCUMENT_ROOT'].'/NovoEntityManager/include/include.php';

inicializa();
$acao = acaoAtual();
if ($acao == ACAO_NOVO) {
    definePaginaAtual(PAGINA_USER_REGISTER);
} else {
    definePaginaAtual(PAGINA_LOGIN);
}
$tela = $_POST['tela'];

$mostraFormLogin = true;

$painelManager = getPainelManager();
$controllerManager = getControllerManager();
$memberController = $controllerManager->getControllerForTable("usuario");



if ($acao == RESET_PASSWORD) {
    resetaSenha($memberController);
} else
if ($acao == RESET_EMAIL) {
    $ticketController = getControllerForTable("ticket_reset");
    $ticketController->sendResetTo($email);
} else if ($acao == 'logar') {
    checaLogin($memberController);
} else if ($acao == 'logout') {
    $memberController->efetuaLogout();
    redirect(getHomeDir());
} else if ($acao == ACAO_INSERIR) {
    insereUsuario($memberController);
}

escreveHeader();


writeDebug("acao:$acao");
if ($acao == RESET_FORM) {
    formularioResetSenha($memberController);
} else if ($acao == 'forgot') {
    $painel = getPainelManager()->getPainel(PAINEL_FORGOT_PASSWORD);
    $painel->mostra();
} else if ($acao == ACAO_NOVO) {
    formularioNovoUsuario();
} else {
    formularioLogin();
}

////////

function formularioLogin() {
    ?>

    <form class="form1" action="<? echo getHomeDir() ?>login" method=post>
        <input type=hidden name=acao value="logar">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title"><? echo translateKey("login_account"); ?></h3>
            </div>
            <? if ($_GET['erro'] == ERROR_INVALID_PASSWORD) { ?>
                <div class="alert alert-danger"><? echo translateKey("login_invalid_password"); ?></div>
            <? } ?>
            <div class="panel-body">





                <input type="email" class="form-control" name="email" required placeholder="<? echo translateKey("txt_email_placeholder"); ?>" value="<? echo $_GET['email']; ?>" />
                <input type="password" class="form-control" name="password" required placeholder="<? echo translateKey("txt_password_placeholder"); ?>"/>
                <br/><a href="<? echo getHomeDir() ?>login/forgot"><? echo translateKey("login_forgot_password"); ?></a>

                <div>
                    <a class="btn btn-default" href="<? echo getHomeDir() ?>"><? echo translateKey("txt_cancel"); ?></a>
                    <a class="btn btn-warning" href="<? echo getHomeDir() ?>login/new"><? echo translateKey("register_account"); ?></a>
                    <? getWidgetManager()->showWidget(WIDGET_LOGIN_GOOGLE, null); ?>
                    <input class="btn btn-primary" type="submit" name=enviar value="<? echo translateKey("txt_login"); ?>" />
                </div>


            </div>
        </div>
    </form>


    <?
}

function formularioResetSenha($memberController) {
    $ticketController = getControllerForTable("ticket_reset");


    $ticket = $_GET['ticket'];
    if ($ticketController->isTicketOK($ticket)) {
        writeDebug("ticket:$ticket");
        $mostraFormLogin = false;
        $painel = getPainelManager()->getPainel(PAINEL_RESET_PASSWORD);
        $painel->setController($memberController);
        $painel->adicionaInputForm("ticket", $ticket);
        $painel->mostra();
    } else {
        redirect(getHomeDir() . "error/" . ERROR_INVALID_TICKET);
    }
}

function formularioNovoUsuario() {
    definePaginaAtual(PAGINA_USER_REGISTER);
    Out::titulo(translateKey("register_account"));

    getWidgetManager()->showWidget(WIDGET_REGISTRO_USUARIO, null);
}

function checaLogin($memberController) {

    if ($_POST["cancelar"] != "") {
        redirect(getHomeDir());
        die();
    }

    $login = $_POST["email"];
    $password = $_POST["password"];
    if ($memberController->efetuaLogin($login, $password)) {
        redirect(getHomeDir() . 'projects/');
    }
}

function resetaSenha($memberController) {
    $painel = getPainelManager()->getPainel(PAINEL_RESET_PASSWORD);
    $painel->adicionaInputForm("ticket", $_POST[ticket]);
    $arr = $painel->getArrayWithPostValues();
    $err = 0;
    $memberController->resetPassword($arr, $err);

    if ($err == 0) {
        redirect(getHomeDir() . "alert/" . ALERT_PASSWORD_RESETED);
    } else {
        redirect(getHomeDir() . "error/$err");
    }
}

function insereUsuario($memberController) {
    definePaginaAtual(PAGINA_USER_REGISTER_COMPLETE);
    $painel = getPainelManager()->getPainel(PAINEL_FINC_USUARIO);
    $arr = $painel->getArrayWithPostValues();

    $err = 0;
    $id = $memberController->insereRegistro($arr, $err);
    if ($err == 0) {
        redirect(getHomeDir() . "projects");
    } else {
        redirect(getHomeDir() . "login/new/error_$err");
    }
}

escreveFooter();
?>