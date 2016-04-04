<?

define("PAINEL_COLUNAS_ENTIDADE", 'p_COL_ENT');
define("PAINEL_FORM_INCLUSAO_COLUNA", 'p_FORM_INC_COL');
define("PAINEL_COLUNA", 'p_COL');
define("PAINEL_DIAGRAMA_ER", 'p_DIA_ER');

class PainelManagerColuna {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_DIAGRAMA_ER) {
            return $this->getDiagramaER();
        }
        if ($nome == PAINEL_COLUNAS_ENTIDADE) {
            return $this->getPainelColunasEntidade();
        }
        if ($nome == PAINEL_FORM_INCLUSAO_COLUNA) {
            return $this->getFormInclusaoColuna();
        }
        if ($nome == PAINEL_COLUNA) {
            return $this->getPainelColuna();
        }
    }

    function getDiagramaER() {
        $table = getTableManager()->getTabelaComNome("coluna");
        $painel = new ERDiagramView("der", $table);
        $painel->addColunaWithDBName("id_coluna");
        $painel->addColunaWithDBName("nm_coluna");
        $painel->addColunaWithDBName("dbname_coluna");
        $painel->addColunaWithDBName("prop_name_coluna");
        $painel->addColunaWithDBName("id_relacao_datatype");
        $painel->addColunaWithDBName("id_datatype");
        $painel->addColunaWithDBName("id_entidade_combo");
        $painel->addColunaWithDBName("id_sql_coluna");
        $painel->addColunaWithDBName("id_acesso_coluna");
        $painel->addColunaWithDBName("css_meta_type");

        return $painel;
    }

    function getPainelColunasEntidade() {
        $table = getTableManager()->getTabelaComNome("coluna");
        $painel = new PainelHorizontal("pce", $table);
        $painel->adicionaAba(ABA_COLUNA_GERAL, translateKey("txt_aba_geral"), true);
        $painel->adicionaAba(ABA_COLUNA_DOC, translateKey("txt_documentation"), false);
        $painel->adicionaAba(ABA_COLUNA_TABLE, translateKey("txt_aba_tabela"), true);
        $painel->adicionaAba(ABA_COLUNA_CLASS, translateKey("txt_aba_classe"), true);
        $painel->adicionaAba(ABA_COLUNA_REDIR, translateKey("txt_aba_redirect"), false);

        $painel->setIdentificadorVisual("prop_" . entidadeAtual());


        $painel->addColunaWithDBName("id_coluna");
        $painel->addColunaWithDBName("seq_coluna");
        $painel->addColunaWithDBName("nm_coluna");
        $painel->addColunaWithDBName("dbname_coluna", ABA_COLUNA_TABLE);
        $painel->addColunaWithDBName("prop_name_coluna", ABA_COLUNA_CLASS);
        $painel->addColunaWithDBName("id_relacao_datatype");
        $painel->addColunaWithDBName("id_datatype", ABA_COLUNA_GERAL);
        $painel->addColunaWithDBName("id_entidade_combo");
        /* $painel->addColunaWithDBName("id_sql_coluna", ABA_COLUNA_TABLE);
          $painel->addColunaWithDBName("id_acesso_coluna", ABA_COLUNA_CLASS); */
        $painel->modoEdicao();
        $painel->ativaAjax(); //testando

        $painel->setTitulo(translateKey("txt_properties"));

        $painel->setEditWidget(WIDGET_EDIT_COLUNA);
        $painel->setEditLink("property/" . projetoAtual() . "/" . entidadeAtual() . "/");
        $painel->setDeleteLink("property/delete/" . projetoAtual() . "/" . entidadeAtual() . "/");

        return $painel;
    }

    function getFormInclusaoColuna() {
        $table = getTableManager()->getTabelaComNome("coluna");
        $painel = new PainelVertical("fic", $table);
        $painel->addColunaWithDBName("nm_coluna");
        //$painel->addColunaWithDBName("dbname_coluna");
        //$painel->addColunaWithDBName("prop_name_coluna");
        $painel->addColunaWithDBName("id_relacao_datatype");
        $painel->addColunaWithDBName("id_datatype");
        $painel->addColunaWithDBName("id_entidade_combo");
        //$painel->addColunaWithDBName("id_sql_coluna");
        //$painel->addColunaWithDBName("id_acesso_coluna");
        $painel->adicionaInputForm("projeto", projetoAtual());
        $painel->adicionaInputForm("entidade", entidadeAtual());
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelColuna() {
        $table = getTableManager()->getTabelaComNome("coluna");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelVertical("pc", $table);

        $painel->adicionaAba(ABA_COLUNA_TABLE, translateKey("txt_aba_tabela"), true);
        $painel->adicionaAba(ABA_COLUNA_CLASS, translateKey("txt_aba_classe"), true);



        $painel->addColunaWithDBName("id_coluna");
        //$painel->addColunaWithDBName("seq_coluna");
        $painel->addColunaWithDBName("id_entidade_pai");
        $painel->addColunaWithDBName("nm_coluna");
        $painel->addColunaWithDBName("dbname_coluna", ABA_COLUNA_TABLE);
        $painel->addColunaWithDBName("prop_name_coluna", ABA_COLUNA_CLASS);
        $painel->addColunaWithDBName("id_relacao_datatype", ABA_COLUNA_TABLE);
        $painel->addColunaWithDBName("id_entidade_combo", ABA_COLUNA_TABLE);
        $painel->addColunaWithDBName("id_sql_coluna", ABA_COLUNA_TABLE);
        $painel->addColunaWithDBName("id_acesso_coluna", ABA_COLUNA_CLASS);

        $painel->adicionaAcaoEditar(getHomeDir() . "property/" . projetoAtual() . "/" . entidadeAtual() . "/");
        $painel->adicionaAcaoRemover(getHomeDir() . "property/delete/" . projetoAtual() . "/" . entidadeAtual() . "/");
        $painel->modoEdicao();
        return $painel;
    }

}

?>