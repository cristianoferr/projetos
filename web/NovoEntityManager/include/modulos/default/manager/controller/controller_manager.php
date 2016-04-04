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

    function getProjetoController() {
        return $this->getControllerForTable("projeto");
    }

    function getEntidadeController() {
        return $this->getControllerForTable("entidade");
    }

    function getRegistroController() {
        return $this->getControllerForTable("registro");
    }

    function getTarefaController() {
        return $this->getControllerForTable("tarefa");
    }

}

?>