<?

define("PAINEL_MEMBROS_PROJETO", 'P_MEM_PROJ');
define("PAINEL_MEMBROS_PROJETO_RESUMO", 'P_MEM_PROJ_RES');
define("PAINEL_FINC_MEMBRO", 'P_FINC_MEMBRO');
define("PAINEL_FINC_USUARIO", 'P_FINC_USUARIO');
define("PAINEL_EDICAO_USUARIO", 'P_EDIT_USUARIO');
define("PAINEL_FORGOT_PASSWORD", 'P_FORGOT_PASS');
define("PAINEL_RESET_PASSWORD", 'P_RESET_PASS');

class PainelManagerUsuario {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_MEMBROS_PROJETO)
            return $this->getPainelMembrosProjeto(true);
        if ($nome == PAINEL_MEMBROS_PROJETO_RESUMO)
            return $this->getPainelMembrosProjeto(false);
        if ($nome == PAINEL_FINC_MEMBRO)
            return $this->getFormInclusaoMembro();
        if ($nome == PAINEL_FINC_USUARIO)
            return $this->getFormInclusaoUsuario();
        if ($nome == PAINEL_EDICAO_USUARIO)
            return $this->getPainelEdicaoUsuario();
        if ($nome == PAINEL_FORGOT_PASSWORD)
            return $this->getPainelForgotPassword();
        if ($nome == PAINEL_RESET_PASSWORD)
            return $this->getPainelResetPassword();
    }

    function getPainelResetPassword() {
        $table = getTableManager()->getTabelaComNome("usuario");
        $painel = new FormPainel("frs", $table);
        $painel->addColunaWithDBName("senha_usuario_novo");
        $painel->addColunaWithDBName("senha_usuario_repeat");
        $painel->setTitulo(translateKey("txt_reset_passwords_title"));
        $painel->modoInclusao(RESET_PASSWORD);
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelForgotPassword() {
        $table = getTableManager()->getTabelaComNome("usuario");
        $painel = new FormPainel("ffp", $table);
        $painel->setController(getControllerForTable("usuario"));
        $painel->addColunaWithDBName("email_usuario_recovery");
        $painel->setTitulo(translateKey("login_recover_password"));
        //$painel->adicionaInputForm("tela", RESET_EMAIL);
        $painel->modoInclusao(RESET_EMAIL);
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelMembrosProjeto($flagFull = true) {
        $table = getTableManager()->getTabelaComNome("usuario_projeto");
        //echo "tabela entidade: ".$table->getTableName();
        if ($flagFull) {
            $painel = new PainelHorizontal("pmp", $table);
        } else {
            $painel = new PainelResumo("pmp", $table);
        }
        $painel->addColunaWithDBName("nm_usuario");
        $painel->addColunaWithDBName("email_usuario_hidden");
        $painel->addColunaWithDBName("id_papel");
        //$painel->adicionaAcaoRemover(getHomeDir() . "members/delete/" . projetoAtual() . "/");

        $painel->modoEdicao();
        return $painel;
    }

    function getPainelEdicaoUsuario() {
        $table = getTableManager()->getTabelaComNome("usuario");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new FormPainel("peu", $table);
        $painel->addColunaWithDBName("nm_usuario");
        $painel->addColunaWithDBName("email_usuario");
        $painel->addColunaWithDBName("id_lingua");

        $painel->modoEdicao();
        return $painel;
    }

    function getFormInclusaoMembro() {
        $table = getTableManager()->getTabelaComNome("invite_usuario_projeto");
        $painel = new PainelVertical("fim", $table);
        $painel->addColunaWithDBName("id_usuario_novo");
        $painel->addColunaWithDBName("email_externo");
        $painel->addColunaWithDBName("id_papel");
        $painel->addColunaWithDBName("texto_invite");

        $painel->adicionaLinkImportante(getHomeDir() . "members/" . projetoAtual(), translateKey("txt_back"), false);
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getFormInclusaoUsuario() {
        $table = getTableManager()->getTabelaComNome("usuario");
        $painel = new FormPainel("fiu", $table);
        $erro = $_GET['erro'];
        if ($erro == ERROR_EMAIL_EXISTS) {
            $painel->setTextoErro(translateKey("register_email_exists"));
        }
        if ($erro == ERROR_INVALID_PASSWORD) {
            $painel->setTextoErro(translateKey("register_password_mismatch"));
        }
        $painel->addColunaWithDBName("nm_usuario");
        $painel->addColunaWithDBName("email_usuario_novo");
        $painel->addColunaWithDBName("senha_usuario_novo");
        $painel->addColunaWithDBName("senha_usuario_repeat");
        $painel->addColunaWithDBName("id_lingua");
        $painel->setTitulo(translateKey("txt_classic_signup"));


        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

}

?>