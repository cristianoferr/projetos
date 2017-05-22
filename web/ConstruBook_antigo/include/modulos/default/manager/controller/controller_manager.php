<?

class ControllerManager extends BaseManager {

    function getControllerForTable($tableName) {//coberto
        $id = "controller_$tableName";
        if (isset($GLOBALS[$id])) {
            //  return $GLOBALS[$id];
        }

        $c = getFileManager()->getControllerForTable($tableName);
        if (!$c) {
            die("Tabela $tableName não possui controller.");
        }

        $GLOBALS[$id] = $c;
        return $c;
    }


    function getTarefaController() {
        return $this->getControllerForTable("tarefa");
    }

}

?>