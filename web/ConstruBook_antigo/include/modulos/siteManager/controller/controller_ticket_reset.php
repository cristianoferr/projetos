<?php

class TicketResetController extends BaseController {

    function insereRegistro($id_usuario) {
        $ticket = $this->geraNovoTicket();
        executaSQL("insert into ticket_reset (data_ticket_reset,id_usuario,cod_ticket_reset,flag_ativo) values (now(),?,?,'T') ", array($id_usuario, $ticket));
        return $ticket;
    }

    function geraNovoTicket() {
        $size = 15;
        $ticket = IOUtils::generateRandomString($size);
        if (existsQuery("select * from ticket_reset where cod_ticket_reset=?", array($ticket))) {
            return $this->geraNovoTicket();
        } else {
            return $ticket;
        }
    }

    /**
     * Envia um link para o usuário falando para o mesmo resetar a senha
     * Se não existir o email, envia um email para o mesmo convidando para se cadastrar.
     * 
     * @param type $email
     */
    function sendResetTo($email) {
        $userController = getControllerForTable("usuario");
        $email = $_POST['email_usuario_recovery'];

        writeDebug("email: $email");
        $id_usuario = $userController->emailExists($email);

        //if ($id_usuario) {
        $this->sendResetTicketTo($email, $id_usuario);
        // } else {
        // }

        die();
    }

    function isTicketOK($ticket) {
        $row = executaQuerySingleRow("select * from ticket_reset where cod_ticket_reset=? and flag_ativo='T'", array($ticket));
        if ($row) {
            // executaSQL("update ticket_reset set flag_ativo='F' where cod_ticket_reset=?", array($ticket));
            return true;
        }
        return false;
    }

    function invalidaTicket($ticket) {
        executaSQL("update ticket_reset set flag_ativo='F' where cod_ticket_reset=?", array($ticket));
    }

    function getIdUsuario($ticket) {
        $row = executaQuerySingleRow("select id_usuario from ticket_reset where cod_ticket_reset=?", array($ticket));
        return $row['id_usuario'];
    }

    function sendResetTicketTo($email, $id_usuario) {
        if ($id_usuario)
            $ticket = $this->insereRegistro($id_usuario);
        //write("ticket: $ticket");
        $title = translateKey("login_account_recovery");
        $body = translateKey(login_account_recovery_body) . "<br>";
        $url = getHomeDir() . "login/reset/$ticket";
        $body.="<a href='$url'>$url</a><br><br>";
        $body.="<a href='" . getHomeDir() . "'>" . translateKey("main_titulo_chamada") . "</a> - " . translateKey("site_subtitle");

        //echo $body;
        $urlAlerta = getHomeDir() . "alert/" . ALERT_RESET_EMAIL_SENT;
        if (AMBIENTE == AMBIENTE_PRODUCAO) {
            IOUtils::sendEmail($email, translateKey("main_titulo_chamada") . " <" . EMAIL_LOST_PASSWORD . ">", $title, $body);
            redirect($urlAlerta);
        } else {
            echo $body;
            echo "<br><a href='$urlAlerta'>Aviso</a>";
        }
    }

}

?>
