<?

//ID=widget
class WidgetEditWidget extends WidgetManagerVirtualizer {

    function generate($id) {//coberto
        $controller = getControllerForTable("widget");
        $elRoot = criaEl("div");

        $painel = getPainelManager()->getPainel(PAINEL_EDIT_WIDGET);
        $painel->setTitulo($this->getTitle());
        $controller->loadSingle($id);
        $painel->setController($controller);

        $ret = $painel->generate();
        $retSection = getWidgetManager()->generateWidget(WIDGET_WIDGET_PROPERTIES, $id);

        $elRoot->addElement($ret);
        $elRoot->addElement($retSection);
        return $elRoot;
    }

    function show($id) {//coberto
        $this->generate($id)->mostra();
    }

    function getTitle() {//coberto
        return translateKey("txt_edit_widget");
    }

}

?>