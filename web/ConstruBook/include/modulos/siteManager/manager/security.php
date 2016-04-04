<?php

class SiteManagerSecurity extends SecurityBase implements SecurityCheck {

    function getTables() {
        return array("chave_lingua", "elemento", "lingua", "pagina", "papel", "tipo_pagina", "usuario", "valor_lingua");
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {
        if ($modulo == "usuario") {
            return (usuarioAtual() == $id_modulo) ? true : $this->readableOnly($flagEdit);
        }

        if ($modulo == "elemento") {
            return $this->checkPermElemento($id_modulo, $flagEdit);
        }

        if (($modulo == "pagina") || ($modulo == "lingua") || ($modulo == "pagina") || ($modulo == "papel") || ($modulo == "tipo_pagina") || ($modulo == "valor_lingua")) {
            return $this->readableOnly($flagEdit);
        }
    }

    function checkPermElemento($id, $flagEdit) {
        return getControllerForTable("elemento")->validaPermissao($id, usuarioAtual(), $flagEdit);
    }

}

?>