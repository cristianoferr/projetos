<?

//ID=SCREEN
class WidgetListScreens extends WidgetManagerVirtualizer {

    function generate($id_projeto) {//coberto
        $controller = getControllerForTable("screen");

        $painel = getPainelManager()->getPainel(PAINEL_LIST_SCREENS);
        $painel->setController($controller);
        $controller->loadRegistros("and screen.id_projeto=$id_projeto", $painel);
        if (checaEscrita("projeto", $id_projeto)) {
            $painel->adicionaLink(getHomeDir() . "screens/new/$id_projeto", translateKey("txt_new_screen"), false);
        }
        $painel->adicionaLinkImportante(getHomeDir() . "project/$id_projeto", translateKey("txt_back"), false);

        return $painel->generate();
    }

    function getTitle() {//coberto
        return translateKey("txt_screen");
    }

}

?>