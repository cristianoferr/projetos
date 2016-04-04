<?php

class VirtualizerSecurity extends SecurityBase implements SecurityCheck {

    function getTables() {//coberto
        return array("screen", "tile", "painel", "tipo_painel", "link", "widget", "widget_section", "widget_coluna");
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {//coberto
        if ($modulo == "screen") {
            return $this->checkPermScreen($id_modulo, $flagEdit);
        }
        if ($modulo == "tile") {
            return $this->checkPermTile($id_modulo, $flagEdit);
        }
        if ($modulo == "widget") {
            return $this->checkPermWidget($id_modulo, $flagEdit);
        }
        if ($modulo == "widget_section") {
            return $this->checkPermWidgetSection($id_modulo, $flagEdit);
        }
        if ($modulo == "widget_coluna") {
            return $this->checkPermWidgetColuna($id_modulo, $flagEdit);
        }
        if ($modulo == "painel") {
            return $this->readableOnly($flagEdit);
        }
        if ($modulo == "tipo_painel") {
            return $this->readableOnly($flagEdit);
        }
        if ($modulo == "link") {
            return $this->checkPermLink($id_modulo, $flagEdit);
        }
    }

    function checkPermScreen($id, $flagEdit) {//coberto
        $id_projeto = getControllerManager()->getControllerForTable("screen")->getProjeto($id);
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

    function checkPermLink($id, $flagEdit) {//coberto
        $id_projeto = getControllerManager()->getControllerForTable("link")->getProjeto($id);
        if (!$id_projeto) {
            return $this->readableOnly($flagEdit);
        }
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

    function checkPermTile($id, $flagEdit) {//coberto
        $id_projeto = getControllerManager()->getControllerForTable("tile")->getProjeto($id);
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

    function checkPermWidget($id, $flagEdit) {//coberto
        $id_projeto = getControllerManager()->getControllerForTable("widget")->getProjeto($id);
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

    function checkPermWidgetSection($id, $flagEdit) {//coberto
        $id_projeto = getControllerManager()->getControllerForTable("widget_section")->getProjeto($id);
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

    function checkPermWidgetColuna($id, $flagEdit) {//coberto
        $id_projeto = getControllerManager()->getControllerForTable("widget_coluna")->getProjeto($id);
        //write("checkPermWidgetColuna: $id | $id_projeto");
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

}

?>