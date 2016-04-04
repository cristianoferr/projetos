<?

//id=tarefa
class WidgetListaTarefas extends WidgetTarefa {

    function generate($id_projeto) {
        $tarefaController = getControllerForTable("tarefa");
        $filtroSQL = $this->getFiltroSQL();
        $painel = getPainelManager()->getPainel(PAINEL_TAREFAS);
        $painel->setController($tarefaController);
        $tarefaController->loadRegistros("and tarefa.id_projeto=$id_projeto $filtroSQL", $painel);
        if (checaEscrita("projeto", $id_projeto)) {
            $painel->adicionaLink(getHomeDir() . "tasks/new/$id_projeto", translateKey("txt_new_task"), false);
            $painel->adicionaLink(getHomeDir() . "deliverables/new/$id_projeto", translateKey("txt_new_deliverable"), false);
        }
        $painel->adicionaLinkImportante(getHomeDir() . "project/$id_projeto", translateKey("txt_back"), false);

        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_tasks");
    }

    function getFiltro() {
        $userController = getControllerForTable("usuario");
        $filtro = $_GET['filtro'];
        if ($filtro) {
            $userController->atualizaInterfaceUsuario(FILTRO_TAREFA, $filtro);
        } else {
            $filtro = $userController->getEstadoInterface(FILTRO_TAREFA, "all");
        }
        return $filtro;
    }

    function getFiltroSQL() {

        $filtro = $this->getFiltro();
        $filtroSQL = "";
        if ($filtro == "open") {
            $filtroSQL = " and id_status_tarefa<" . STATUS_TAREFA_CONCLUIDO;
        }
        if ($filtro == "closed") {
            $filtroSQL = " and id_status_tarefa>=" . STATUS_TAREFA_CONCLUIDO;
        }
        if ($filtro == "canceled") {
            $filtroSQL = " and id_status_tarefa=" . STATUS_TAREFA_CANCELADO;
        }
        if ($filtro == "notstarted") {
            $filtroSQL = " and id_status_tarefa<=" . STATUS_TAREFA_A_FAZER;
        }
        if ($filtro == "started") {
            $filtroSQL = " and id_status_tarefa<" . STATUS_TAREFA_CONCLUIDO . " and id_status_tarefa>" . STATUS_TAREFA_A_FAZER;
        }
        return $filtroSQL;
    }

}

?>