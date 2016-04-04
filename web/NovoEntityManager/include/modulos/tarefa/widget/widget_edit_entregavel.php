<?

//id=entregavel
class WidgetEditEntregavel extends WidgetTarefa {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("entregavel");
        $model = $controller->loadSingle($id, $painel);
        $painel = getPainelManager()->getPainel(PAINEL_ENTREGAVEL);
        $painel->setController($controller);
        $painel->setModal($this->isModal());
        $painel->adicionaLinkImportante(getHomeDir() . "deliverables/" . projetoAtual(), translateKey("txt_back"), false);
        $painel->setTitulo($this->getTitle());
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_deliverable");
    }

}

?>