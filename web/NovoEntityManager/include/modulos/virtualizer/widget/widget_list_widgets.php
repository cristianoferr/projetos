<?

//ID=projeto
class WidgetListWidgets extends WidgetManagerVirtualizer {

    function generate($id_projeto) {//coberto
        $controller = getControllerForTable("widget");

        $painel = getPainelManager()->getPainel(PAINEL_LIST_WIDGETS);
        $painel->setController($controller);

        $controller->loadRegistros("and widget.id_projeto=$id_projeto", $painel);
        if (checaEscrita("projeto", $id_projeto)) {
            $painel->adicionaLink(getHomeDir() . "widgets/new/$id_projeto", translateKey("txt_new_task"), false);
        }
        $painel->adicionaLinkImportante(getHomeDir() . "project/$id_projeto", translateKey("txt_back"), false);

        return $painel->generate();
    }

    function show($id_projeto) {//coberto
        $ret = $this->generate($id_projeto);
        $ret->mostra();
    }

    function getTitle() {//coberto
        return translateKey("txt_screen");
    }

}

?>