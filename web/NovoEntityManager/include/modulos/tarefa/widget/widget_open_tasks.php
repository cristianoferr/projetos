<?

//id=projeto
class WidgetOpenTasks extends WidgetTarefa {

    function generate($id_projeto) {
        $tarefaController = getControllerManager()->getControllerForTable("tarefa");
        $painel = getPainelManager()->getPainel(PAINEL_TAREFA_PROJETO);
        $painel->setTitulo(translateKey("txt_open_tasks"));
        $painel->setController($tarefaController);
        $tarefaController->loadRegistros("and tarefa.id_projeto=$id_projeto and tarefa.id_status_tarefa<" . STATUS_TAREFA_CONCLUIDO, $painel);
        if (checkPerm("projeto", $id_projeto, true)) {
            $painel->adicionaLink(getHomeDir() . "tasks/new/$id_projeto", translateKey("txt_new_task"), false);
        }
        $painel->adicionaLink(getHomeDir() . "tasks/$id_projeto/all", translateKey("txt_go_to_tasks"), false);
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_open_tasks");
    }

}

?>