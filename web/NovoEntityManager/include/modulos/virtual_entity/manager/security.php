<?php

class VirtualEntitySecurity extends SecurityBase implements SecurityCheck {

    function getTables() {
        //return array("entidade", "projeto", "coluna", "funcao", "primitive_type", "datatype", "valor", "sql_coluna", "acesso_coluna", "registro", "meta_type", "primitive_type_param");
    }

    function checkPerm($modulo, $id_modulo, $flagEdit) {
        
    }

    /*  function checkPermColuna($id_coluna, $flagEdit) {
      $id_projeto = getControllerManager()->getControllerForTable("coluna")->getProjeto($id_coluna);
      return $this->checkPermProjeto($id_projeto, $flagEdit);
      } */
}

?>