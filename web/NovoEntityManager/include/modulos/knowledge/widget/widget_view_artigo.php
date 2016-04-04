<?

class WidgetViewArtigo extends WidgetKnowledge {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("artigo");
        $model = $controller->loadSingle($id);
        $elRoot = elMaker("div");
        $elRoot->addElement(Out::tituloG($model->getDescricao()));
        $elDiv = elMaker("div", $elRoot)->setClass("panel-body")->setValue($model->getValorCampo("texto_artigo"));
        return $elRoot;
    }

    function show($id) {
        $this->generate($id)->mostra();
    }

    function isEditavel() {
        return false;
    }

    function getTitle() {
        return "";
    }

}

?>