<?

class ControllerManager_Tarefa implements IControllerManager {

    function getControllerForTable($tableName) {
        if ($tableName == "tarefa") {
            return $this->getTarefaController();
        }

        if ($tableName == "status_tarefa") {
            return $this->getStatusTarefaController();
        }

        if ($tableName == "prereq_tarefa") {
            return $this->getPrereqTarefaController();
        }

        if ($tableName == "status_entregavel") {
            return $this->getStatusEntregavelController();
        }

        if ($tableName == "urgencia") {
            return $this->getUrgenciaController();
        }
        if ($tableName == "complexidade") {
            return $this->getComplexidadeController();
        }
        if ($tableName == "area_tarefa") {
            return $this->getAreaTarefaController();
        }

        if ($tableName == "entregavel") {
            return $this->getEntregavelController();
        }
    }

    function getPrereqTarefaController() {
        $table = getTableManager()->getTabelaComNome("prereq_tarefa");
        $controller = new PrereqTarefaController($table, "txt_prereqs_task", "txt_prereq_task");
        return $controller;
    }

    function getTarefaController() {
        $table = getTableManager()->getTabelaComNome("tarefa");
        $controller = new TarefaController($table, "txt_task", "txt_tasks");
        return $controller;
    }

    function getEntregavelController() {
        $table = getTableManager()->getTabelaComNome("entregavel");
        $controller = new EntregavelController($table, "txt_deliverables", "txt_deliverable", new LinkKey("txt_new_deliverable", "deliverable/new/<projeto>"));
        return $controller;
    }

    function getComplexidadeController() {
        $table = getTableManager()->getTabelaComNome("complexidade");
        $controller = new ComplexidadeController($table, "txt_complexity", "txt_complexity");
        return $controller;
    }

    function getUrgenciaController() {
        $table = getTableManager()->getTabelaComNome("urgencia");
        $controller = new UrgenciaController($table, "txt_urgency", "txt_urgency");
        return $controller;
    }

    function getStatusTarefaController() {
        $table = getTableManager()->getTabelaComNome("status_tarefa");
        $controller = new StatusTarefaController($table, "txt_status", "txt_status");
        return $controller;
    }

    function getStatusEntregavelController() {
        $table = getTableManager()->getTabelaComNome("status_entregavel");
        $controller = new StatusEntregavelController($table, "txt_status", "txt_status");
        return $controller;
    }

}

?>