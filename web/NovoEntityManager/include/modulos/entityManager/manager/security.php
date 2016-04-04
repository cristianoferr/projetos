<?php

class EntityManagerSecurity extends SecurityBase implements SecurityCheck {

    function getTables() {
        return array("entidade", "projeto", "coluna", "funcao", "primitive_type", "datatype", "valor", "sql_coluna", "acesso_coluna", "registro", "meta_type", "primitive_type_param");
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {

        if (($modulo == "meta_type") || ($modulo == "primitive_type") || ($modulo == "primitive_type_param") || ($modulo == "sql_coluna") || ($modulo == "acesso_coluna")) {
            return $this->readableOnly($flagEdit);
        }

        if ($modulo == "coluna") {
            return $this->checkPermColuna($id_modulo, $flagEdit);
        }
        if ($modulo == "entidade") {
            return $this->checkPermEntidade($id_modulo, $flagEdit);
        }
        if ($modulo == "datatype") {
            return $this->checkPermDatatype($id_modulo, $flagEdit);
        }
        if ($modulo == "funcao") {
            return $this->checkPermFuncao($id_modulo, $flagEdit);
        }
        if ($modulo == "valor") {
            return $this->checkPermValor($id_modulo, $flagEdit);
        }
        if ($modulo == "registro") {
            return $this->checkPermRegistro($id_modulo, $flagEdit);
        }
        if ($modulo == "projeto") {
            return $this->checkPermProjeto($id_modulo, $flagEdit);
        }
    }

    /* function checkPermUsuarioProjeto($id_project, $flagEdit) {
      return checkPermProjeto(projetoAtual(), $flagEdit);
      } */

    function checkPermProjeto($id_project, $flagEdit) {
        if ($flagEdit) {
            return $v = getControllerManager()->getProjetoController()->isUserAdminProjeto($id_project);
        } else {
            if (getControllerManager()->getProjetoController()->isProjetoPublico($id_project)) {
                return true;
            }
            return getControllerManager()->getProjetoController()->isUserNoProjeto($id_project);
        }
    }

    function checkPermFuncao($id_funcao, $flagEdit) {
        $id_projeto = getControllerManager()->getControllerForTable("funcao")->getProjeto($id_funcao);
        return $this->checkPermProjeto($id_projeto, $flagEdit);
    }

    function checkPermValor($id_valor, $flagEdit) {
        $id_projeto = getControllerManager()->getRegistroController()->getProjetoValor($id_valor);
        return $this->checkPermProjeto($id_projeto, $flagEdit);
    }

    function checkPermRegistro($id_valor, $flagEdit) {
        $id_projeto = getControllerManager()->getRegistroController()->getProjeto($id_valor);
        return $this->checkPermProjeto($id_projeto, $flagEdit);
    }

    function checkPermEntidade($id_entity, $flagEdit) {
        if ($flagEdit) {
            return getControllerForTable("entidade")->usuarioPodeExcluir($id_entity);
        } else {
            return getControllerForTable("entidade")->usuarioPodeAcessar($id_entity);
        }
    }

    function checkPermDatatype($id_coluna, $flagEdit) {
        $id_projeto = getControllerManager()->getControllerForTable("datatype")->getProjeto($id_coluna);
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

    function checkPermColuna($id_coluna, $flagEdit) {
        $id_projeto = getControllerManager()->getControllerForTable("coluna")->getProjeto($id_coluna);
        return checkPerm("projeto", $id_projeto, $flagEdit);
    }

}

?>