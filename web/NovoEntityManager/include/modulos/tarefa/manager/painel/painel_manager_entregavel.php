<?

define("PAINEL_FINC_ENTREGAVEL", 'P_FINC_ENTR');
define("PAINEL_ENTR_PROJ", 'P_ENTR_PROJ');
define("PAINEL_ENTREGAVEIS", 'P_ENTREGS');
define("PAINEL_ENTR_HIERARQUICO", 'P_ENTR_HIER');
define("PAINEL_ENTREGAVEL", 'P_ENTR');
define("PAINEL_SELECT_DELIVERABLE", 'sel_deliv');

class PainelManagerEntregavel {

    function getPainelComNome($nome) {//coberto
        if ($nome == PAINEL_SELECT_DELIVERABLE) {
            return $this->getPainelSelectEntregavel();
        }
        if ($nome == PAINEL_FINC_ENTREGAVEL) {
            return $this->getFormInclusaoEntregavel();
        }
        if ($nome == PAINEL_ENTR_PROJ) {
            return $this->getPainelEntregaveisProjeto();
        }
        if ($nome == PAINEL_ENTREGAVEIS) {
            return $this->getPainelEntregaveis();
        }
        if ($nome == PAINEL_ENTR_HIERARQUICO) {
            return $this->getPainelEntregaveisHierarquico();
        }
        if ($nome == PAINEL_ENTREGAVEL) {
            return $this->getPainelEntregavel();
        }
    }

    function getPainelSelectEntregavel() {
        $table = getTableManager()->getTabelaComNome("entregavel");
        $painel = new RedirectPainel("pse", $table);

        $coluna = $painel->addColunaWithDBName("id_entregavel_pai");

        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getFormInclusaoEntregavel() {
        $table = getTableManager()->getTabelaComNome("entregavel");
        $painel = new PainelVertical("fie", $table);

        $painel->adicionaAba(ABA_COLUNA_CUSTOS, translateKey("txt_costs"), false, true);
        $painel->adicionaAba(ABA_COLUNA_DOC, translateKey("txt_documentation"), false, false);

        $painel->addColunaWithDBName("nm_entregavel");
        $painel->addColunaWithDBName("id_entregavel_pai");
        $painel->addColunaWithDBName("desc_entregavel", ABA_COLUNA_DOC);
        $painel->addColunaWithDBName("hh_entregavel", ABA_COLUNA_CUSTOS);
        $painel->addColunaWithDBName("custo_entregavel", ABA_COLUNA_CUSTOS);

        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelEntregaveisProjeto() {
        $table = getTableManager()->getTabelaComNome("entregavel");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelResumo("pesp", $table);
        $painel->addColunaWithDBName("id_entregavel");
        $painel->addColunaWithDBName("nm_entregavel");
        $painel->addColunaWithDBName("id_status_entregavel");
        //$painel->adicionaAcaoSeguir(getHomeDir()."deliverable/".projetoAtual()."/");
        $painel->modoEdicao();
        $painel->setEditWidget(WIDGET_EDIT_ENTREGAVEL);
        $painel->setEditLink("deliverable/" . projetoAtual() . "/");
        $painel->setDeleteLink("deliverable/delete/" . projetoAtual() . "/");
        return $painel;
    }

    function getPainelEntregaveis() {
        $table = getTableManager()->getTabelaComNome("entregavel");
        //echo "tabela entidade: ".$table->getTableName();

        $painel = new PainelHorizontal("pes", $table);
        $painel->addColunaWithDBName("id_entregavel");
        $painel->addColunaWithDBName("nm_entregavel");
        $painel->addColunaWithDBName("id_entregavel_pai");
        $painel->addColunaWithDBName("id_status_entregavel");
        $painel->addColunaWithDBName("hh_entregavel");
        $painel->addColunaWithDBName("custo_entregavel");

        $painel->adicionaFiltro("all", "deliverables/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_all_tasks"));
        $painel->adicionaFiltro("open", "deliverables/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_open_tasks"));
        $painel->adicionaFiltro("closed", "deliverables/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_closed_tasks"));
        $painel->adicionaFiltro("canceled", "deliverables/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_canceled_tasks"));
        $painel->adicionaFiltro("notstarted", "deliverables/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_not_started_tasks"));
        $painel->adicionaFiltro("started", "deliverables/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_started_tasks"));

        $painel->enableOrderBy();
        $painel->modoEdicao();

        $painel->setEditWidget(WIDGET_EDIT_ENTREGAVEL);
        $painel->setEditLink("deliverable/" . projetoAtual() . "/");
        $painel->setDeleteLink("deliverable/delete/" . projetoAtual() . "/");
        return $painel;
    }

    function getPainelEntregaveisHierarquico() {
        $table = getTableManager()->getTabelaComNome("entregavel");
        //echo "tabela entidade: ".$table->getTableName();

        $painel = new EntregavelPainel("peh", $table);
        $painel->addColunaWithDBName("id_entregavel");
        $painel->addColunaWithDBName("nm_entregavel");
        $painel->addColunaWithDBName("desc_entregavel");
        $painel->addColunaWithDBName("id_entregavel_pai");
        $painel->addColunaWithDBName("id_status_entregavel");
        return $painel;
    }

    function getPainelEntregavel() {
        $table = getTableManager()->getTabelaComNome("entregavel");
        //echo "tabela entidade: ".$table->getTableName();


        $painel = new PainelVertical("pet", $table);
        $painel->adicionaAba(ABA_COLUNA_CUSTOS, translateKey("txt_costs"), false, true);
        $painel->adicionaAba(ABA_COLUNA_DOC, translateKey("txt_documentation"), false, false);
        $painel->addColunaWithDBName("nm_entregavel");
        $painel->addColunaWithDBName("desc_entregavel", ABA_COLUNA_DOC);
        $painel->addColunaWithDBName("id_entregavel_pai");
        $painel->addColunaWithDBName("id_status_entregavel");
        $painel->addColunaWithDBName("hh_entregavel", ABA_COLUNA_CUSTOS);
        $painel->addColunaWithDBName("custo_entregavel", ABA_COLUNA_CUSTOS);
        $painel->adicionaAcaoEditar(getHomeDir() . "deliverable/" . projetoAtual() . "/");
        $painel->adicionaAcaoCancelar(getHomeDir() . "deliverable/cancel/" . projetoAtual() . "/");
        $painel->modoEdicao();
        return $painel;
    }

}

?>