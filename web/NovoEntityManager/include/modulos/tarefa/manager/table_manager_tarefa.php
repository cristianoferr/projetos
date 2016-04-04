<?

class TableManagerTarefa extends TableManager {

    function carregaTabelaComNome($tableName) {

        if ($tableName == "complexidade") {
            return $this->carregaComplexidade();
        }
        if ($tableName == "urgencia") {
            return $this->carregaUrgencia();
        }

        if ($tableName == "status_tarefa") {
            return $this->carregaStatusTarefa();
        }
        if ($tableName == "tarefa") {
            return $this->carregaTarefa();
        }

        if ($tableName == "prereq_tarefa") {
            return $this->carregaPrereqTarefa();
        }
        if ($tableName == "status_entregavel") {
            return $this->carregaStatusEntregavel();
        }
        if ($tableName == "entregavel") {
            return $this->carregaEntregavel();
        }
    }

    function carregaComplexidade() {
        $tabela = new TabelaModel("complexidade", "id_complexidade", "nm_complexidade");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Complexidade", "id_complexidade", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $tabela->traduzDescricao();
        $tabela->setSortBy('id_complexidade');
        $this->adicionaColunaNormal($tabela, translateKey("txt_complexity"), "nm_complexidade", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaUrgencia() {
        $tabela = new TabelaModel("urgencia", "id_urgencia", "nm_urgencia");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Urgencia", "id_urgencia", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $tabela->traduzDescricao();
        $tabela->setSortBy('id_urgencia');
        $this->adicionaColunaNormal($tabela, translateKey("txt_urgency"), "nm_urgencia", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaStatusTarefa() {
        $tabela = new TabelaModel("status_tarefa", "id_status_tarefa", "nm_status_tarefa");
        $coluna = $this->adicionaColunaNormal($tabela, "ID status_tarefa", "id_status_tarefa", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $tabela->traduzDescricao();
        $tabela->setSortBy('id_status_tarefa');
        $this->adicionaColunaNormal($tabela, translateKey("txt_status"), "nm_status_tarefa", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaStatusEntregavel() {
        $tabela = new TabelaModel("status_entregavel", "id_status_entregavel", "nm_status_entregavel");
        $coluna = $this->adicionaColunaNormal($tabela, "ID status_entregavel", "id_status_entregavel", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $tabela->traduzDescricao();
        $tabela->setSortBy('id_status_entregavel');
        $this->adicionaColunaNormal($tabela, translateKey("txt_status"), "nm_status_entregavel", false, TIPO_INPUT_TEXTO_CURTO);
        return $tabela;
    }

    function carregaEntregavel() {
        $tabela = new TabelaModel("entregavel", "id_entregavel", "nm_entregavel");
        $coluna = $this->adicionaColunaNormal($tabela, "ID entregavel", "id_entregavel", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_deliverable"), "nm_entregavel", false, TIPO_INPUT_TEXTO_CURTO);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna->setHintKey("txt_deliverable_hint");
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_desc_deliverable"), "desc_entregavel", false, TIPO_INPUT_HTML, 500000);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna->setHintKey("txt_desc_deliverable_hint");
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_deliverable_pai"), "id_entregavel_pai", false, false, "entregavel", TIPO_INPUT_SELECT_FK);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $coluna->setHintKey("txt_deliverable_pai_hint");
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_status"), "id_status_entregavel", false, false, "status_entregavel", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_cost_hh_total"), "hh_entregavel", false, TIPO_INPUT_INTEIRO);
        $coluna->setDetalhesInclusao(true, false, 1);
        $coluna->totaliza();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_cost_money_total"), "custo_entregavel", false, TIPO_INPUT_CURRENCY);
        $coluna->setDetalhesInclusao(true, false, 0);
        $coluna->totaliza();
        //$coluna->setCSSClassId("status_del_");
        $tabela->setPaginaInclusao(getHomeDir() . "deliverables");

        return $tabela;
    }

    function carregaPrereqTarefa() {

        $tabela = new TabelaModel("prereq_tarefa", "id_tarefa_prereq");
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_prereq_task"), "id_tarefa_prereq", true, false, "tarefa", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_task"), "id_tarefa_prereq", true, false, "tarefa", TIPO_INPUT_RADIO_SELECT);

        //campos calculados...
        $this->adicionaColunaCalculada($tabela, translateKey("txt_task"), "nm_tarefa", " select nm_tarefa from tarefa  where tarefa_prereq.id_tarefa=" . tarefaAtual() . " and id_tarefa_prereq=");


        $tabela->setPaginaInclusao(getHomeDir() . "task/prerequisite");

        return $tabela;
    }

    function carregaTarefa() {

        $tabela = new TabelaModel("tarefa", "id_tarefa", "nm_tarefa");
        $coluna = $this->adicionaColunaNormal($tabela, "ID Tarefa", "id_tarefa", true, TIPO_INPUT_INTEIRO);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_task"), "nm_tarefa", false, TIPO_INPUT_TEXTO_CURTO, 150);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_description"), "desc_tarefa", false, TIPO_INPUT_TEXTAREA, 500000);
        $coluna->setDetalhesInclusao(true, false, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_project"), "id_projeto", false, false, "projeto", TIPO_INPUT_RADIO_SELECT);
        $coluna->setInvisible();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_creation_date"), "dtcriacao_tarefa", true, TIPO_INPUT_DATA);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_finish_date"), "dtfinalizacao_tarefa", false, TIPO_INPUT_DATA);
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_complexity"), "id_complexidade", false, false, "complexidade", TIPO_INPUT_RADIO);
        $coluna->setDetalhesInclusao(true, true, 3);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_urgency"), "id_urgencia", false, false, "urgencia", TIPO_INPUT_RADIO);
        $coluna->setDetalhesInclusao(true, true, 3);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_status"), "id_status_tarefa", false, false, "status_tarefa", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, 1);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_assigned_to"), "id_usuario", false, true, "usuario", TIPO_INPUT_RADIO_SELECT);
        $coluna->setFiltroSelect("and usuario.id_usuario in (select id_usuario from usuario_projeto where id_projeto=" . projetoAtual() . ")");
        $coluna->setDetalhesInclusao(true, true, null);
        $coluna = $this->adicionaColunaComFK($tabela, translateKey("txt_entregavel_tarefa"), "id_entregavel", false, false, "entregavel", TIPO_INPUT_RADIO_SELECT);
        $coluna->setDetalhesInclusao(true, true, FIRST_OPTION);
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_cost_hh_total"), "hh_tarefa", false, TIPO_INPUT_INTEIRO);
        $coluna->setDetalhesInclusao(true, false, 0);
        $coluna->totaliza();
        $coluna = $this->adicionaColunaNormal($tabela, translateKey("txt_cost_money_total"), "custo_tarefa", false, TIPO_INPUT_CURRENCY);
        $coluna->setDetalhesInclusao(true, false, 0);
        $coluna->totaliza();

        $tabela->setPaginaInclusao(getHomeDir() . "tasks");

        return $tabela;
    }

}

?>