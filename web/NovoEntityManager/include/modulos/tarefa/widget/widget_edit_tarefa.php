<?

//id=tarefa
class WidgetEditTarefa extends WidgetTarefa {

    function generate($id) {
        $tarefaController = getControllerManager()->getControllerForTable("tarefa");
        $painel = getPainelManager()->getPainel(PAINEL_TAREFA);
        $tarefaController->loadSingle($id, $painel);
        $painel->setController($tarefaController);
        $painel->setModal($this->isModal());
        $painel->adicionaLinkImportante(getHomeDir() . "tasks/" . projetoAtual(), translateKey("txt_back"), false);
        $painel->setTitulo($this->getTitle());
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_task");
    }

}

?>