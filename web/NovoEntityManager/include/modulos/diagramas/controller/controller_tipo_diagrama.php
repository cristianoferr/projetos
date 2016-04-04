<?

class TipoDiagramaController extends BaseController {

    function isShowingAllComponents($tipo) {

        $flag_todos = $this->getFieldFromModel($tipo, "flag_todos");
        return ($flag_todos == "T");
    }

    /*
      function geraPosicao($idDiagrama, Array $arrComponentes, BaseModel $modelTipoDiagrama) {
      write("TipoDiagrama: $modelTipoDiagrama");
      if ($modelTipoDiagrama->getId() == TIPO_DIAGRAMA_WBS) {
      $entController = getControllerForTable("entregavel");
      $entController->loadRegistros("and id_entregavel in (select id_entregavel from componente_diagrama where id_diagrama=$idDiagrama)");
      $arrTree = $entController->createModelTree("id_entregavel_pai");
      //printArray($arrTree);
      }
      return false;
      }
     */
}

?>