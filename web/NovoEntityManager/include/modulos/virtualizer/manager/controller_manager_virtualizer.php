<?

class ControllerManager_Virtualizer implements IControllerManager {

    function getControllerForTable($tableName) {//coberto
        if ($tableName == "screen") {
            return $this->getScreenController();
        }
        if ($tableName == "tile") {
            return $this->getTileController();
        }
        if ($tableName == "tile_size") {
            return $this->getTileSizeController();
        }
        if ($tableName == "tile_orientation") {
            return $this->getTileOrientationController();
        }
        if ($tableName == "widget") {
            return $this->getWidgetController();
        }
        if ($tableName == "widget_section") {
            return $this->getWidgetSectionController();
        }
        if ($tableName == "widget_coluna") {
            return $this->getWidgetColunaController();
        }
        if ($tableName == "painel") {
            return $this->getPanelController();
        }
        if ($tableName == "tipo_painel") {
            return $this->getPanelTypeController();
        }
        if ($tableName == "link") {
            return $this->getLinkController();
        }
        if ($tableName == "site_projeto") {
            return $this->getSiteProjetoController();
        }
    }

    function getSiteProjetoController() {//coberto
        $table = getTableManager()->getTabelaComNome("site_projeto");
        $controller = new SiteProjetoController($table, "txt_projects", "txt_project");
        return $controller;
    }

    function getScreenController() {//coberto
        $table = getTableManager()->getTabelaComNome("screen");
        $controller = new ScreenController($table, "txt_screens", "txt_screen");
        return $controller;
    }

    function getPanelController() {//coberto
        $table = getTableManager()->getTabelaComNome("painel");
        $controller = new PanelController($table, "txt_paineis", "txt_painel");
        return $controller;
    }

    function getPanelTypeController() {//coberto
        $table = getTableManager()->getTabelaComNome("tipo_painel");
        $controller = new PanelTypeController($table, "txt_tipo_paineis", "txt_tipo_painel");
        return $controller;
    }

    function getLinkController() {//coberto
        $table = getTableManager()->getTabelaComNome("link");
        $controller = new PanelTypeActionController($table, "txt_links", "txt_link");
        return $controller;
    }

    function getTileController() {//coberto
        $table = getTableManager()->getTabelaComNome("tile");
        $controller = new TileController($table, "txt_tiles", "txt_tile");
        return $controller;
    }

    function getTileSizeController() {//coberto
        $table = getTableManager()->getTabelaComNome("tile_size");
        $controller = new TileSizeController($table, "txt_tile_sizes", "txt_tile_size");
        return $controller;
    }

    function getTileOrientationController() {//coberto
        $table = getTableManager()->getTabelaComNome("tile_orientation");
        $controller = new TileOrientationController($table, "txt_tile_orientations", "txt_tile_orientation");
        return $controller;
    }

    function getWidgetController() {//coberto
        $table = getTableManager()->getTabelaComNome("widget");
        $controller = new WidgetController($table, "txt_widgets", "txt_widget");
        return $controller;
    }

    function getWidgetSectionController() {//coberto
        $table = getTableManager()->getTabelaComNome("widget_section");
        $controller = new WidgetSectionController($table, "txt_widget_sections", "txt_widget_section");
        return $controller;
    }

    function getWidgetColunaController() {//coberto
        $table = getTableManager()->getTabelaComNome("widget_coluna");
        $controller = new WidgetColunaController($table, "txt_widget_colunas", "txt_widget_coluna");
        return $controller;
    }

}

?>