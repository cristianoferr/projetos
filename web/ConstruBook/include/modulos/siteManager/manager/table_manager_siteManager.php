<?

class TableManagerSiteManager extends TableManager {

    function carregaTabelaComNome($tableName) {

        if ($tableName == "elemento") {
            return $this->carregaElemento();
        }

        if ($tableName == "pagina") {
            return $this->carregaPagina();
        }

        if ($tableName == "tipo_pagina") {
            return $this->carregaTipoPagina();
        }
        if ($tableName == "chave_lingua") {
            return $this->carregaChaveLingua();
        }

        if ($tableName == "valor_lingua") {
            return $this->carregaValorLingua();
        }
        if ($tableName == "usuario") {
            return $this->carregaUsuario();
        }
        if ($tableName == "ticket_reset") {
            return $this->carregaTicketReset();
        }
    }

    function carregaTicketReset() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("ticket_reset", "id_ticket_reset", "cod_ticket_reset");
        $coluna = $this->adicionaColunaNormal($tabela, "PK", "id_ticket_reset", false, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, "Ticket", "cod_ticket_reset", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);

        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_username"), "id_usuario", false, false, "usuario", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);


        return $tabela;
    }

    function carregaUsuario() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("usuario", "id_usuario", "nm_usuario");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Usuario", "id_usuario", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_email"), "email_usuario", true, TIPO_INPUT_EMAIL);
        $coluna->setDetalhesInclusao(true, false, "");
        $coluna = $this->adicionaColunaNormal($tabela, "Ultimo Acesso", "ultimo_acesso_usuario", false, TIPO_INPUT_DATA);
        $coluna = $this->adicionaColunaNormal($tabela, "Data Cadastro", "dtcadastro_usuario", true, TIPO_INPUT_DATA);
        $coluna->setDetalhesInclusao(true, false, null);

        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_username"), "nm_usuario", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, "Hit Count", "conta_acesso_usuario", false, TIPO_INPUT_INTEIRO);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_prefered_language"), "id_lingua", false, false, "lingua", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, LINGUA_EN);


        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_email"), "email_usuario_novo", false, TIPO_INPUT_UNIQUE_EMAIL);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_email_recover"), "email_usuario_recovery", false, TIPO_INPUT_EMAIL);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setHintKey("txt_email_recover_hint");
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_password"), "senha_usuario_novo", false, TIPO_INPUT_SENHA);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("login_repeat_password"), "senha_usuario_repeat", false, TIPO_INPUT_SENHA);
        $coluna->setDetalhesInclusao(true, true, null);


        $tabela->setPaginaInclusao(getHomeDir() . "login/");

        return $tabela;
    }

    function carregaValorLingua() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("valor_lingua", "id_valor_lingua", "nm_valor_lingua");
        $coluna = $this->adicionaColunaNormal($tabela, "PK", "id_valor_lingua", false, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, "Chave", "nm_valor_lingua", false, TIPO_INPUT_TEXTAREA);
        $coluna->setDetalhesInclusao(true, true, null);

        $coluna = $this->adicionaColunaComFK($tabela, "Lingua", "id_lingua", true, false, "lingua", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);

        $coluna = $this->adicionaColunaComFK($tabela, "Chave", "nm_chave_lingua", true, false, "chave_lingua", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);


        return $tabela;
    }

    function carregaChaveLingua() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("chave_lingua", "nm_chave_lingua", "nm_chave_lingua");
        $coluna = $this->adicionaColunaNormal($tabela, "Chave", "nm_chave_lingua", true, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, "txt_");

        $coluna = $this->adicionaColunaNormal($tabela, "Inglês", "valor_en_chave_lingua_novo", true, TIPO_INPUT_TEXTO_CURTO, 5000);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, "Portugues", "valor_pt_chave_lingua_novo", true, TIPO_INPUT_TEXTO_CURTO, 5000);
        $coluna->setDetalhesInclusao(true, true, null);

        $coluna = $this->adicionaColunaCalculada($tabela, "Inglês", "valor_en_chave_lingua", "select nm_valor_lingua as valor_en_chave_lingua from valor_lingua where id_lingua=1 and valor_lingua.nm_chave_lingua=");
        $coluna->setReadonly(false);
        $coluna->setTipoInput(TIPO_INPUT_TEXTAREA);
        $coluna = $this->adicionaColunaCalculada($tabela, "Portugues", "valor_pt_chave_lingua", "select nm_valor_lingua as valor_en_chave_lingua from valor_lingua where id_lingua=2 and valor_lingua.nm_chave_lingua=");
        $coluna->setReadonly(false);
        $coluna->setTipoInput(TIPO_INPUT_TEXTAREA);
        $tabela->setPaginaInclusao(getHomeDir() . "strings");

        return $tabela;
    }

    function carregaElemento() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("elemento", "id_elemento");
        $coluna = $this->adicionaColunaNormal($tabela, "ID elemento", "id_elemento", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaComFK($tabela, "id_projeto", "id_projeto", true, false, "projeto", TIPO_INPUT_SELECT_FK);
        $coluna = $this->adicionaColunaComFK($tabela, "id_entidade", "id_entidade", true, false, "entidade", TIPO_INPUT_SELECT_FK);
        $coluna = $this->adicionaColunaComFK($tabela, "id_tarefa", "id_tarefa", true, false, "tarefa", TIPO_INPUT_SELECT_FK);
        $coluna = $this->adicionaColunaComFK($tabela, "id_entregavel", "id_entregavel", true, false, "entregavel", TIPO_INPUT_SELECT_FK);
        $coluna = $this->adicionaColunaComFK($tabela, "id_coluna", "id_coluna", true, false, "coluna", TIPO_INPUT_SELECT_FK);
        $coluna = $this->adicionaColunaComFK($tabela, "id_funcao", "id_funcao", true, false, "funcao", TIPO_INPUT_SELECT_FK);
        $coluna = $this->adicionaColunaComFK($tabela, "id_datatype", "id_datatype", true, false, "datatype", TIPO_INPUT_SELECT_FK);
        $coluna = $this->adicionaColunaComFK($tabela, "id_artigo", "id_artigo", true, false, "artigo", TIPO_INPUT_SELECT_FK);
        $coluna = $this->adicionaColunaComFK($tabela, "pagina", "pagina", true, false, "pagina", TIPO_INPUT_SELECT_FK);

        return $tabela;
    }

    function carregaPagina() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("pagina", "id_pagina", "nminterno_pagina");
        $coluna = $this->adicionaColunaNormal($tabela, "ID pagina", "id_pagina", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, "Nome da Página (key)", "nminterno_pagina", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, "Tipo Pagina", "id_tipo_pagina", false, false, "tipo_pagina", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, null);
        $tabela->traduzDescricao();

        $tabela->setPaginaInclusao(getHomeDir() . "paginas");
        return $tabela;
    }

    function carregaTipoPagina() {
        //tipo de banco (mysql,oracle,etc)
        $tabela = new TabelaModel("tipo_pagina", "id_tipo_pagina", "nm_tipo_pagina");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Tipo pagina", "id_tipo_pagina", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, "Tipo pagina", "nm_tipo_pagina", true, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);

        return $tabela;
    }

}

?>