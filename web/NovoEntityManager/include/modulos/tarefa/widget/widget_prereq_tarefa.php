<?

//ID=TAREFA
class WidgetNewPrereqTarefa extends WidgetTarefa {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("prereq_tarefa");
        $painel = getPainelManager()->getPainel(PAINEL_PREREQ_TAREFA);
        $painel->adicionaInputForm("id_tarefa", $id);
        $painel->adicionaInputForm("projeto", projetoAtual());
        $painel->setController($controller);
        $painel->setWidgetId($id);
        $painel->setModal($this->isModal());
        $painel->setTitulo($this->getTitle());
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_prereq_task");
    }

}

?>