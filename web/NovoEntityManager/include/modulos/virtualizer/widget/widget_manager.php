<?

define("WIDGET_SHOW_SCREEN", 'SHOW_SCREEN');
define("WIDGET_NEW_SCREEN", 'NEW_SCREEN');
define("WIDGET_LIST_SCREENS", 'LIST_SCREENS');
define("WIDGET_LIST_WIDGETS", 'LIST_WIDGETS');
define("WIDGET_EDIT_SCREEN", 'EDIT_SCREEN');
define("WIDGET_EDIT_TILE", 'EDIT_TILE');
define("WIDGET_SHOW_TILE", 'W_SHOW_TILE');
define("WIDGET_SHOW_WIDGET", 'SHOW_WIDGET');
define("WIDGET_EDIT_WIDGET", 'EDIT_WIDGET');
define("WIDGET_WIDGET_PROPERTIES", 'W_W_PROPS');
define("WIDGET_ADD_WIDGET_SECTION", 'W_A_W_S');
define("WIDGET_ADD_WIDGET_COLUNA", 'W_A_W_C');
define("WIDGET_DELETE_WIDGET_SECTION", 'W_D_W_S');
define("WIDGET_DELETE_WIDGET_COLUNA", 'W_Del_Wid_Col');
define("WIDGET_ADD_TILE", 'W_add_tile');

class WidgetManagerVirtualizer extends WidgetBase {

    function getWidgetsArray() {//coberto
        return array(WIDGET_SHOW_SCREEN => SCREEN_TESTE,
            WIDGET_LIST_SCREENS => PROJETO_TESTE,
            WIDGET_LIST_WIDGETS => WIDGET_TESTE_H,
            WIDGET_SHOW_WIDGET => WIDGET_TESTE_H,
            WIDGET_EDIT_WIDGET => WIDGET_TESTE_H
        );
    }

    function getWidgetFor($nome) {//coberto
        if ($nome == WIDGET_SHOW_TILE) {
            return new WidgetShowTile();
        }
        if ($nome == WIDGET_ADD_TILE) {
            return new WidgetAddTile();
        }
        if ($nome == WIDGET_WIDGET_PROPERTIES) {
            return new WidgetWidgetProperties();
        }

        if ($nome == WIDGET_DELETE_WIDGET_SECTION) {
            return new WidgetDeleteWidgetSection();
        }
        if ($nome == WIDGET_DELETE_WIDGET_COLUNA) {
            return new WidgetDeleteWidgetColuna();
        }
        if ($nome == WIDGET_ADD_WIDGET_SECTION) {
            return new WidgetAddWidgetSection();
        }
        if ($nome == WIDGET_ADD_WIDGET_COLUNA) {
            return new WidgetAddWidgetColuna();
        }

        if ($nome == WIDGET_NEW_SCREEN) {
            return new WidgetNewScreen();
        }
        if ($nome == WIDGET_SHOW_SCREEN) {
            return new WidgetShowScreen();
        }
        if ($nome == WIDGET_SHOW_WIDGET) {
            return new WidgetShowWidget();
        }
        if ($nome == WIDGET_EDIT_WIDGET) {
            return new WidgetEditWidget();
        }
        if ($nome == WIDGET_LIST_SCREENS) {
            return new WidgetListScreens();
        }
        if ($nome == WIDGET_LIST_WIDGETS) {
            return new WidgetListWidgets();
        }
        if ($nome == WIDGET_EDIT_TILE) {
            return new WidgetEditTile();
        }
        /* if ($nome == WIDGET_EDIT_SCREEN) {
          return new WidgetEditScreen();
          } */
    }

}

?>