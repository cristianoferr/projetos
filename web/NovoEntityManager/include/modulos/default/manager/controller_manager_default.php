<?

class ControllerManager_Default implements IControllerManager {

    function getControllerForTable($tableName) {
        //echo "getControllerForTable($tableName)";
        if ($tableName == "usuario") {
            return $this->getUsuarioController();
        }


        if ($tableName == "lingua") {
            return $this->getLinguaController();
        }
    }

    function getUsuarioController() {
        $table = getTableManager()->getTabelaComNome("usuario");
        $controller = new UsuarioController($table);
        return $controller;
    }

    function getLinguaController() {
        $table = getTableManager()->getTabelaComNome("lingua");
        $controller = new LinguaController($table);
        return $controller;
    }

}

?>