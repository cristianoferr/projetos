<?

define("WIDGET_VIEW_VIRTUAL_ENTITY", 'WIDGET_VIEW_VIRTUAL_ENTITY');

class WidgetManagerEntidadeVirtual extends WidgetBase {

    function getWidgetsArray() {//coberto
        return array(WIDGET_VIEW_VIRTUAL_ENTITY => ENTIDADE_TESTE);
    }

    function getWidgetFor($nome) {//coberto
        if ($nome == WIDGET_VIEW_VIRTUAL_ENTITY) {
            return new WidgetVisualizaEntidadeVirtual();
        }
    }

    function getVars() {
        $vars = parent::getVars();
        return $vars . "&modulo=" . MODULO_TAREFAS;
    }

}

?>