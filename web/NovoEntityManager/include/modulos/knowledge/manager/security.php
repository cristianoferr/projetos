<?php

class KnowledgeSecurity extends SecurityBase implements SecurityCheck {

    function getTables() {
        return array("artigo");
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {
        if ($modulo == "artigo") {
            return $this->checkPermArtigo($id_modulo, $flagEdit);
        }
    }

    function checkPermArtigo($id, $flagEdit) {
        return getControllerForTable("artigo")->validaPermissao($id, usuarioAtual(), $flagEdit);
    }

}

?>