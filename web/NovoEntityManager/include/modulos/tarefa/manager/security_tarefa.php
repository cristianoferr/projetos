<?php

class TarefaSecurity extends SecurityBase implements SecurityCheck {

    function getTables() {
        return array("tarefa", "entregavel", "status_tarefa", "complexidade", "urgencia");
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {
        if ($modulo == "tarefa") {
            return $this->checkPermTarefa($id_modulo, $flagEdit);
        }

        if ($modulo == "entregavel") {
            return $this->checkPermEntregavel($id_modulo, $flagEdit);
        }

        if (($modulo == "status_tarefa") || ($modulo == "complexidade") || ($modulo == "urgencia")) {
            return $this->readableOnly($flagEdit);
        }
    }

    function checkPermEntregavel($id, $flagEdit) {
        $id_projeto = getControllerManager()->getControllerForTable("entregavel")->getProjeto($id);
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

    function checkPermTarefa($id_tarefa, $flagEdit) {
        $id_projeto = getControllerManager()->getControllerForTable("tarefa")->getProjeto($id_tarefa);
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

}

?>