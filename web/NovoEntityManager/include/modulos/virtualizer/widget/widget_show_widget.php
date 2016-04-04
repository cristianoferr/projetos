<?

//ID=widget
class WidgetShowWidget extends WidgetManagerVirtualizer {

    function generate($id) {//coberto
        $controller = getControllerForTable("widget");

        $painel = $controller->createPainel($id);
        $virtualController = $painel->getController();
        $virtualController->loadRegistros(null, $painel);
        $elRoot = criaEl("div");

        $ret = $painel->generate();

        $elRoot->addElement($ret);
        return $elRoot;
    }

    function show($id) {//coberto
        $this->generate($id)->mostra();
    }

}

?>