<?

//ID=TAREFA
class WidgetPrereqsTarefa extends WidgetTarefa {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("prereq_tarefa");
        $painel = getPainelManager()->getPainel(PAINEL_PREREQS_TAREFA);
        //inicioDebug();
        $controller->loadRegistros("and prereq_tarefa.id_tarefa=$id", $painel);
        //fimDebug();
        $painel->setController($controller);
        $painel->setModal($this->isModal());
        $painel->setWidgetId($id);
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_prereq_task");
    }

}

?>