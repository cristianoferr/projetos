<?

class ControllerManager_VirtualEntity implements IControllerManager {

    function getControllerForTable($tableName) {
        if ($tableName == "virtual") {
            return $this->getVirtualController();
        }
    }

    function getVirtualController() {
        $table = getTableManager()->getTabelaComNome("virtual");
        $controller = new VirtualController($table);
        return $controller;
    }

}

?>