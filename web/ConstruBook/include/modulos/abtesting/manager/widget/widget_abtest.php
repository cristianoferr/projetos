<?

class WidgetABTest extends WidgetBase {

    function getWidgetFor($nome) {
        /* 	if ($nome==WIDGET_EDIT_DIAGRAMA){
          return new WidgetEditDiagrama();
          } */
    }

    function getVars() {
        $vars = parent::getVars();
        return $vars . "&modulo=" . MODULO_ABTEST;
    }

}

?>