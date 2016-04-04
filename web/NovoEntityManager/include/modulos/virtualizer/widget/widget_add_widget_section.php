<?

//ID=widget
class WidgetAddWidgetSection extends WidgetManagerVirtualizer {

    function generate($id) {
        $controller = getControllerForTable("widget_section");
        $controller->createSectionFor($id, translateKey("txt_type_a_name"));
    }

    function show($id) {
        $this->generate($id);
    }

}

?>