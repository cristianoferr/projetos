<?

class UsuarioController extends BaseController {

//usado em selects (do html) para mostrar chave e valor
    function buscaUsuarios($term) {
        if (strlen($term) < 5) {
            return;
        }
        $dict = new Dicionario();
        $term = validaTexto($term);
        $sql = "select id_usuario,nm_usuario,email_usuario from usuario where nm_usuario like '%$term%' or email_usuario like '%$term%' ";
        $sql.="limit 5";
        $rsValor = executaQuery($sql, array("term" => $term));
        $ret = "";
        while ($rowValor = $rsValor->fetch()) {
            $dict->setValor($rowValor['id_usuario'], $rowValor);
            $count++;
        }
        return $dict;
    }

    function getUserLogin($id_usuario) {
        $id_usuario = validaNumero($id_usuario, "id_usuario getProjectName ProjetoController");
        $id = "nmusuario_$id_usuario";
        if ($this->getCacheInfo($id))
            return $this->getCacheInfo($id);

        $rs = executaQuery("select nm_usuario from usuario where id_usuario=?", array($id_usuario));
        if ($row = $rs->fetch()) {
            $v = $row['nm_usuario'];
            $this->setCacheInfo($id, $v);
            return $v;
        }
    }

    function filtrosExtras() {
        return "";
    }

    function excluirRegistro($id) {

        $id = validaNumero($id, "id excluirRegistro UsuarioController");
        validaEscrita("usuario", $id);

        executaSQL("delete from usuario where id_usuario=?", array($id));
    }

    function atualizaInterfaceUsuario($identificador, $valor) {
        $_SESSION[PREF_INTERFACE_USUARIO . $identificador] = $valor;

        if (existsQuery("select valor_interface from interface_usuario where id_usuario=? and id_interface_usuario=?", array(usuarioAtual(), $identificador))) {
            executaSQL("update interface_usuario set valor_interface=? where id_interface_usuario=? and id_usuario=?", array($valor, $identificador, usuarioAtual()));
        } else {
            executaSQL("insert into interface_usuario (id_interface_usuario,id_usuario,valor_interface) values (?,?,?)", array($identificador, usuarioAtual(), $valor));
        }
    }

    function getInterfaceUsuario($identificador) {
        if (isset($_SESSION[PREF_INTERFACE_USUARIO . $identificador])) {
            return $_SESSION[PREF_INTERFACE_USUARIO . $identificador];
        }
        $row = executaQuerySingleRow("select valor_interface from interface_usuario where id_usuario=? and id_interface_usuario=?", array(usuarioAtual(), $identificador));
        if ($row) {
            return $row['valor_interface'];
        }
        return false;
    }

    function getEstadoInterface($id, $default) {

        $v = $this->getInterfaceUsuario($id);
        if ($v) {
            return $v;
        }
        return $default;
    }

    function corrigeDados() {
        //echo "corrigeDados... ".usuarioAtual();
        if (isGuest())
            return;
        if (!isset($_SESSION['corrigeDados_usuario' . usuarioAtual()])) {
            $sql = "select * from interface_usuario where id_usuario=?";
            $rs = executaQuery($sql, array(usuarioAtual()));
            while ($row = $rs->fetch()) {
                $identificador = $row['id_interface_usuario'];
                $valor = $row['valor_interface'];
                $_SESSION[$identificador] = $valor;
            }

            $_SESSION['corrigeDados_usuario' . usuarioAtual()] = true;
        }
    }

    function efetuaLogin($login, $password, $openId = null) {

        $this->efetuaLogout();
        //echo "inicio<br>";
        $login = validaTexto($login);
        if ($openId == null) {
            //$password = validaTexto($password);
            $password = encripta($password);
            $arrLogin = executaQuerySingleRow("select id_usuario,nm_usuario,email_usuario,id_lingua,conta_acesso_usuario from usuario where email_usuario=? and senha_usuario=?", array($login, $password));
        } else {
            $password = $openId->identity;
            $arrLogin = executaQuerySingleRow("select id_usuario,nm_usuario,email_usuario,id_lingua,conta_acesso_usuario from usuario where email_usuario=?", array($login));
        }
        //echo "executando query<br>";
        //echo "arrLogin:$arrLogin<br>";
        if ($arrLogin) {
            $_SESSION["id_usuario"] = $arrLogin['id_usuario'];
            $_SESSION["nm_usuario"] = $arrLogin['nm_usuario'];
            $_SESSION["email_usuario"] = $arrLogin['email_usuario'];
            $_SESSION["id_lingua"] = $arrLogin['id_lingua'];
            $conta = $arrLogin['conta_acesso_usuario'] + 1;
            $this->atualizaValor($arrLogin['id_usuario'], "ultimo_acesso_usuario", date('Y-m-d H:i:s'));
            $this->atualizaValor($arrLogin['id_usuario'], "conta_acesso_usuario", $conta);
            return true;
        }

        redirect(getHomeDir() . 'login/error/' . ERROR_INVALID_PASSWORD);
        return false;
    }

    function loginOrRegister($email, $first, $openid) {
        if ($this->emailExists($email)) {
            $this->efetuaLogin($email, "...", $openid);
        } else {
            $lingua = getLingua();
            $arr = array('nm_usuario' => $first, 'email_usuario_novo' => $email, 'senha_usuario_novo' => '', 'id_lingua' => $lingua);
            $this->insereRegistro($arr, $err, $openid);
        }
        redirect(getHomeDir());
    }

    function insereRegistro($array, &$err, $openId = null) {
        //echo "aaaa".sizeof($array);
        //printArray($array);
        // printArray($array);
        //die();

        if ($this->emailExists($array['email_usuario_novo'])) {
            $err = ERROR_EMAIL_EXISTS;
            return;
        }
        if ($openId != null) {
            $senha = $openId->identity;
        } else {
            if (empty($array['senha_usuario_novo'])) {
                $err = ERROR_INVALID_PASSWORD;
                return;
            }
            if ($array['senha_usuario_novo'] != $array['senha_usuario_repeat']) {
                $err = ERROR_INVALID_PASSWORD;
            }
            $senha = $array['senha_usuario_novo'];
            $senha = encripta($senha);
        }
        $array['senha_usuario_novo'] = $senha;
        $id_usuario = executaSQL("insert into usuario (nm_usuario,email_usuario,senha_usuario,id_lingua,dtcadastro_usuario) values (:nm_usuario,:email_usuario_novo,:senha_usuario_novo,:id_lingua,now())", $array);


        $this->efetuaLogin($array['email_usuario_novo'], $senha, $openId);
        return $id_usuario;
    }

    function loginExists($login) {
        $login = validaTexto($login);
        return (existsQuery("select * from usuario where lower(nm_usuario)=?", array(strtolower($login))));
    }

    function resetPassword($arr, &$err) {
        $ticketController = getControllerForTable("ticket_reset");
        $ticket = $arr['ticket'];
        if (!$ticketController->isTicketOK($ticket)) {
            $erro = "ERROR_INVALID_TICKET";
            return;
        }
        if ($arr['senha_usuario_novo'] != $arr['senha_usuario_repeat']) {
            $err = ERROR_INVALID_PASSWORD;
            return;
        }
        $id_usuario = $ticketController->getIdUsuario($ticket);
        $password = encripta($arr['senha_usuario_novo']);
        $cryptPass = encripta($password);
        $ticketController->invalidaTicket($ticket);

        executaSQL("update usuario set senha_usuario=? where id_usuario=?", array($cryptPass, $id_usuario));
        $this->efetuaLogin($this->getEmailFromId($id_usuario), $password);
    }

    function emailExists($login) {
        $login = validaTexto($login);

        writeDebug("emailExists($login)");
        $row = executaQuerySingleRow("select id_usuario from usuario where lower(email_usuario)=?", array(strtolower($login)));
        if ($row) {
            return $row['id_usuario'];
        }
    }

    function getEmailFromId($id_usuario) {
        $login = validaTexto($email);

//        writeDebug("emailExists($login)");
        $row = executaQuerySingleRow("select email_usuario from usuario where id_usuario=?", array($id_usuario));
        if ($row) {
            return $row['email_usuario'];
        }
    }

    function efetuaLogout() {
        $_SESSION["id_usuario"] = '';
        foreach ($_SESSION as $k => $v) {
            unset($_SESSION[$k]);
        }
        //session_destroy();
    }

    function getOrderBy() {
        return "nm_usuario";
    }

}

?>