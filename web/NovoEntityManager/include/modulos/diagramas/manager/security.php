<?php

class DiagramSecurity extends SecurityBase implements SecurityCheck {

    function getTables() {
        return array("diagrama", "tipo_diagrama", "componente_diagrama");
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {
        if ($modulo == "diagrama") {
            return $this->checkPermDiagrama($id_modulo, $flagEdit);
        }
    }

    function checkPermDiagrama($id_coluna, $flagEdit) {
        $id_projeto = getControllerManager()->getControllerForTable("diagrama")->getProjeto($id_coluna);
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

}

?>