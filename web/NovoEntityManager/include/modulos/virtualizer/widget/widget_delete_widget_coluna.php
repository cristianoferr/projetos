<?

//ID=widget_coluna
class WidgetDeleteWidgetColuna extends WidgetManagerVirtualizer {

    function generate($id) {
        $controller = getControllerForTable("widget_coluna");
        $controller->excluirRegistro($id);
    }

    function show($id) {
        $this->generate($id);
    }

}

?>