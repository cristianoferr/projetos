<?

define("PAINEL_ENTIDADE", 'P_ENTIDADE');
define("PAINEL_ENTIDADES_PROJETO", 'P_ENTS_PROJ');
define("PAINEL_ENTIDADES", 'P_ENTS');
define("PAINEL_FORM_INC_ENTIDADE", 'P_FINC_ENT');

class PainelManagerEntidade {

    function getPainelComNome($nome) {
        if ($nome == PAINEL_ENTIDADE)
            return $this->getPainelEntidade();
        if ($nome == PAINEL_ENTIDADES_PROJETO)
            return $this->getPainelEntidadesProjeto();
        if ($nome == PAINEL_ENTIDADES)
            return $this->getPainelEntidades();
        if ($nome == PAINEL_FORM_INC_ENTIDADE)
            return $this->getFormInclusaoEntidade();
    }

    function getPainelEntidade() {
        $table = getTableManager()->getTabelaComNome("entidade");
        $painel = new PainelVertical("pe", $table);

        $painel->adicionaAba(ABA_COLUNA_GERAL, translateKey("txt_aba_geral"), false, true);
        $painel->adicionaAba(ABA_COLUNA_TABLE, translateKey("txt_aba_tabela"), false, true);
        $painel->adicionaAba(ABA_COLUNA_CLASS, translateKey("txt_aba_classe"), false, true);
        $painel->adicionaAba(ABA_COLUNA_DOC, translateKey("txt_documentation"), false, false);



        $painel->addColunaWithDBName("id_entidade");
        $painel->addColunaWithDBName("nm_entidade");
        $painel->addColunaWithDBName("desc_entidade", ABA_COLUNA_DOC);
        $painel->addColunaWithDBName("flag_classe", ABA_COLUNA_CLASS);
        $painel->addColunaWithDBName("flag_banco", ABA_COLUNA_TABLE);
        $painel->addColunaWithDBName("dbname_entidade", ABA_COLUNA_TABLE);
        $painel->addColunaWithDBName("classname_entidade", ABA_COLUNA_CLASS);
        $painel->addColunaWithDBName("package_entidade", ABA_COLUNA_CLASS);
        $painel->addColunaWithDBName("id_coluna_pk", ABA_COLUNA_GERAL);
        $painel->addColunaWithDBName("id_coluna_desc", ABA_COLUNA_GERAL);
        $painel->addColunaWithDBName("id_entregavel", ABA_COLUNA_GERAL);
        $painel->addColunaWithDBName("id_entidade_extends", ABA_COLUNA_CLASS);

        $painel->addColunaWithDBName("id_camada", ABA_COLUNA_GERAL);

        $painel->modoEdicao();
        $painel->ativaAjax();


        return $painel;
    }

    function getPainelEntidadesProjeto() {
        $table = getTableManager()->getTabelaComNome("entidade");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelResumo("pep", $table);
        $painel->addColunaWithDBName("id_entidade");
        $painel->addColunaWithDBName("nm_entidade");
        $painel->addColunaWithDBName("id_camada");
        $painel->addColunaWithDBName("id_entregavel");
        $painel->addColunaWithDBName("cont_colunas");
        $painel->addColunaWithDBName("cont_registros");
        $painel->addColunaWithDBName("cont_funcoes");

        $painel->setEditWidget(WIDGET_EDIT_ENTIDADE);
        $painel->setEditLink("entity/" . projetoAtual() . "/");

        $painel->adicionaLinkImportante(getHomeDir() . "entities/" . projetoAtual(), translateKey("txt_entities"), false);
        $painel->modoEdicao();
        return $painel;
    }

    function getPainelEntidades() {
        $table = getTableManager()->getTabelaComNome("entidade");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelHorizontal("petds", $table);
        $painel->addColunaWithDBName("id_entidade");
        $painel->addColunaWithDBName("nm_entidade");
        $painel->addColunaWithDBName("id_camada");
        $painel->addColunaWithDBName("id_entregavel");
        $painel->addColunaWithDBName("cont_colunas");
        $painel->addColunaWithDBName("cont_registros");
        $painel->addColunaWithDBName("cont_funcoes");
        $painel->setEditWidget(WIDGET_EDIT_ENTIDADE);

        $painel->setEditWidget(WIDGET_EDIT_ENTIDADE);
        $painel->setEditLink("entity/" . projetoAtual() . "/");

        $painel->modoEdicao();
        $painel->enableOrderBy();
        return $painel;
    }

    function getFormInclusaoEntidade() {
        $table = getTableManager()->getTabelaComNome("entidade");
        $painel = new PainelVertical("pe", $table);
        $painel->addColunaWithDBName("nm_entidade");
        $painel->addColunaWithDBName("package_entidade");
        $painel->addColunaWithDBName("id_camada");
        $painel->addColunaWithDBName("id_entregavel");

        $painel->adicionaLinkImportante(getHomeDir() . "entity/" . projetoAtual() . "/" . entidadeAtual(), translateKey("txt_back"), false);
        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

}

?>