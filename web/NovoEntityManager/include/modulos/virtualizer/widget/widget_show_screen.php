<?

//ID=SCREEN
class WidgetShowScreen extends WidgetManagerVirtualizer {

    function generate($id_screen) {//coberto
        $screenController = getControllerForTable("screen");
        $controller = getControllerForTable("tile");

        $tileTree = $screenController->getTileTree($id_screen);
        $painelScreen = getPainelManager()->getPainel(PAINEL_SHOW_SCREEN);
        $painelScreen->setController($controller);
        $painelScreen->editMode($GLOBALS["ABA_SCREEN"] == "E");
        foreach ($tileTree as $model) {
            $painelScreen->setTileTree($model);
        }

        return $painelScreen->generate();
    }

    function show($id_screen) {//coberto
        $this->generate($id_screen)->mostra();
    }

    function getTitle() {//coberto
        return translateKey("txt_screen");
    }

}

?>