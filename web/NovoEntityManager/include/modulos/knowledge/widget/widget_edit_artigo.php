<?

//id=artigo
class WidgetEditArtigo extends WidgetKnowledge {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("artigo");
        $painel = getPainelManager()->getPainel(PAINEL_EDIT_ARTIGO);
        $controller->loadSingle($id, $painel);
        $painel->setController($controller);
        $painel->setModal($this->isModal());
        $painel->setTitulo($this->getTitle());
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_edit_article");
    }

}

?>