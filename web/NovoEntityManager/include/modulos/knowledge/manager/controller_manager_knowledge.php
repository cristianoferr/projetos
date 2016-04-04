<?

class ControllerManager_Knowledge implements IControllerManager {

    function getControllerForTable($tableName) {
        if ($tableName == "artigo") {
            return $this->getArtigoController();
        }
    }

    function getArtigoController() {
        $table = getTableManager()->getTabelaComNome("artigo");
        $controller = new ArtigoController($table, "txt_articles", "txt_nm_conhecimento");
        return $controller;
    }

}

?>