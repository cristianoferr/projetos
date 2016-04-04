<?

define("PAINEL_FINC_TAREFA", 'P_FINC_TAREFA');
define("PAINEL_TAREFA_PROJETO", 'P_TAR_PROJ');
define("PAINEL_TAREFAS", 'P_TARS');
define("PAINEL_TAREFA", 'P_TAR');
define("PAINEL_PREREQ_TAREFA", 'P_PREREQ_TAREFA');
define("PAINEL_PREREQS_TAREFA", 'P_PREREQS_TAREFA');

class PainelManagerTarefa {

    function getPainelComNome($nome) {//coberto
        if ($nome == PAINEL_FINC_TAREFA) {
            return $this->getFormInclusaoTarefa();
        }
        if ($nome == PAINEL_TAREFA_PROJETO) {
            return $this->getPainelTarefasProjeto();
        }
        if ($nome == PAINEL_TAREFAS) {
            return $this->getPainelTarefas();
        }
        if ($nome == PAINEL_TAREFA) {
            return $this->getPainelTarefa();
        }
        if ($nome == PAINEL_PREREQ_TAREFA) {
            return $this->getPainelPrereqTarefa();
        }
        if ($nome == PAINEL_PREREQS_TAREFA) {
            return $this->getPainelPrereqsTarefa();
        }
    }

    function getPainelPrereqsTarefa() {//coberto
        $table = getTableManager()->getTabelaComNome("prereq_tarefa");
        $painel = new PainelHorizontal("ppst", $table);

        //$painel->addColunaWithDBName("id_tarefa");
        // $painel->addColunaWithDBName("nm_tarefa");

        $painel->setTitulo(translateKey("txt_prereqs_task"));
        $painel->setNewWidget(WIDGET_NEW_PREREQ_TAREFA);
        $painel->setEditWidget(WIDGET_EDIT_TAREFA);
        $painel->setDeleteLink("task/prerequisite/delete/" . projetoAtual() . "/" . tarefaAtual() . "/");

        $painel->addColunaWithDBName("id_tarefa_prereq");

        return $painel;
    }

    function getPainelPrereqTarefa() {//coberto
        $table = getTableManager()->getTabelaComNome("prereq_tarefa");
        $painel = new PainelVertical("ppt", $table);

        $painel->addColunaWithDBName("id_tarefa_prereq");

        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getFormInclusaoTarefa() {//coberto
        $table = getTableManager()->getTabelaComNome("tarefa");
        $painel = new PainelVertical("fit", $table);

        $painel->addColunaWithDBName("nm_tarefa");
        $painel->addColunaWithDBName("desc_tarefa");
        $painel->addColunaWithDBName("id_entregavel");
        $painel->addColunaWithDBName("id_complexidade");
        $painel->addColunaWithDBName("id_urgencia");
        $painel->addColunaWithDBName("hh_tarefa");
        $painel->addColunaWithDBName("custo_tarefa");


        $painel->modoInclusao();
        $painel->desativaAjax();
        return $painel;
    }

    function getPainelTarefasProjeto() {//coberto
        $table = getTableManager()->getTabelaComNome("tarefa");
        //echo "tabela entidade: ".$table->getTableName();
        $painel = new PainelResumo("ptp", $table);
        $painel->addColunaWithDBName("id_tarefa");
        $painel->addColunaWithDBName("nm_tarefa");
        $painel->addColunaWithDBName("id_status_tarefa");
        $painel->addColunaWithDBName("id_usuario");
        $painel->setEditWidget(WIDGET_EDIT_TAREFA);
        $painel->setEditLink("task/" . projetoAtual() . "/");
        $painel->setDeleteLink("tasks/delete/" . projetoAtual() . "/");
        $painel->modoEdicao();
        return $painel;
    }

    function getPainelTarefas() {//coberto
        $table = getTableManager()->getTabelaComNome("tarefa");
        //echo "tabela entidade: ".$table->getTableName();

        $painel = new PainelHorizontal("pt", $table);
        $painel->addColunaWithDBName("id_tarefa");
        $painel->addColunaWithDBName("id_projeto");
        $painel->addColunaWithDBName("nm_tarefa");
        $painel->addColunaWithDBName("id_entregavel");
        $painel->addColunaWithDBName("id_status_tarefa");
        $painel->addColunaWithDBName("id_complexidade");
        $painel->addColunaWithDBName("id_urgencia");
        $painel->addColunaWithDBName("id_usuario");


        /* $link = $painel->adicionaAcaoCancelar(getHomeDir() . "tasks/cancel/" . projetoAtual() . "/");
          $link->setCondicao("id_status_tarefa", CONDITION_LOWER, STATUS_TAREFA_CONCLUIDO);
          $link = $painel->adicionaAcaoAvancar(getHomeDir() . "tasks/advance/" . projetoAtual() . "/");
          $link->setCondicao("id_status_tarefa", CONDITION_LOWER, STATUS_TAREFA_CONCLUIDO); */

        $painel->adicionaFiltro("all", "tasks/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_all_tasks"));
        $painel->adicionaFiltro("open", "tasks/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_open_tasks"));
        $painel->adicionaFiltro("closed", "tasks/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_closed_tasks"));
        $painel->adicionaFiltro("canceled", "tasks/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_canceled_tasks"));
        $painel->adicionaFiltro("notstarted", "tasks/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_not_started_tasks"));
        $painel->adicionaFiltro("started", "tasks/" . projetoAtual() . "/" . modoAtual() . "/", translateKey("txt_started_tasks"));

        $painel->setEditWidget(WIDGET_EDIT_TAREFA);
        $painel->setEditLink("task/" . projetoAtual() . "/");
        $painel->setDeleteLink("tasks/delete/" . projetoAtual() . "/");


        $painel->enableOrderBy();
        $painel->modoEdicao();

        return $painel;
    }

    function getPainelTarefa() {//coberto
        $table = getTableManager()->getTabelaComNome("tarefa");
        //echo "tabela entidade: ".$table->getTableName();

        $painel = new PainelVertical("pt", $table);
        $painel->addColunaWithDBName("id_tarefa");
        $painel->addColunaWithDBName("id_projeto");
        $painel->addColunaWithDBName("nm_tarefa");
        $painel->addColunaWithDBName("desc_tarefa");
        $painel->addColunaWithDBName("id_status_tarefa");
        $painel->addColunaWithDBName("id_complexidade");
        $painel->addColunaWithDBName("id_urgencia");
        $painel->addColunaWithDBName("id_entregavel");
        $painel->addColunaWithDBName("id_usuario");
        $painel->addColunaWithDBName("dtcriacao_tarefa");
        $painel->addColunaWithDBName("dtfinalizacao_tarefa");
        $painel->addColunaWithDBName("hh_tarefa");
        $painel->addColunaWithDBName("custo_tarefa");
        $painel->adicionaAcaoEditar(getHomeDir() . "task/" . projetoAtual() . "/");
        $painel->adicionaAcaoCancelar(getHomeDir() . "tasks/cancel/" . projetoAtual() . "/");
        $painel->modoEdicao();
        return $painel;
    }

}

?>