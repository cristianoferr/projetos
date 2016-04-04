<?

//id=datatype
class WidgetEditDatatype extends WidgetEntidade {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("datatype");
        $painel = getpainelManager()->getPainel(PAINEL_DATATYPE);
        $painel->setController($controller);
        $painel->setTitulo($this->getTitle());
        $controller->loadSingle($id, $painel);
        return $painel->generate();
    }

    function getTitle() {
        return translateKey("txt_datatype");
    }

}

?>