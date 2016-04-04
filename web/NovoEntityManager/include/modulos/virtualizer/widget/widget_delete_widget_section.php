<?

//ID=widget_section
class WidgetDeleteWidgetSection extends WidgetManagerVirtualizer {

    function generate($id) {
        $controller = getControllerForTable("widget_section");
        $controller->excluirRegistro($id);
    }

    function show($id) {
        $this->generate($id);
    }

}

?>