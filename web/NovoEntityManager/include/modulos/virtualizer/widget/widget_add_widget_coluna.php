<?

//ID=widget_section
class WidgetAddWidgetColuna extends WidgetManagerVirtualizer {

    function generate($id) {
        $controller = getControllerForTable("widget_coluna");
        $controller->addColunaNaSection($id);
    }

    function show($id) {
        $this->generate($id);
    }

}

?>