<?

//id=tile
class WidgetShowTile extends WidgetManagerVirtualizer {

    function generate($id) {
        $controller = getControllerManager()->getControllerForTable("tile");
        $model = $controller->loadSingle($id);

        $id_widget = $model->getValorCampo("id_widget");
        if ($id_widget) {
            return getWidgetManager()->generateWidget(WIDGET_SHOW_WIDGET, $id_widget);
        } else {
            return criaEl("");
        }
    }

    function getTitle() {
        return translateKey("txt_tile");
    }

}

?>