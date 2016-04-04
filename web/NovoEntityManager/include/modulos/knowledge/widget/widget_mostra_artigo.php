<?

class WidgetMostraArtigo extends WidgetKnowledge {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("artigo");
        $painel = getPainelManager()->getPainel(PAINEL_LISTA_ARTIGOS);
        $controller->loadRegistros("and id_elemento=$id", $painel);
        $painel->setController($controller);
        $painel->setIdElemento($id);
        $painel->setFixo(true);
        $painel->setModal($this->isModal());
        $painel->setEditavel($this->isEditavel());
        $painel->setTitulo($this->getTitle());
        return $painel->generate();
    }

    function isEditavel() {
        return false;
    }

    function getTitle() {
        return "";
    }

}

?>