<?

class PanelTypeController extends BaseController {
    /*  function insereRegistro($array) {
      //echo "pagina: ".$array['pagina'];
      validaEscrita("screen", $array["id_screen"], true);

      if (!$array['seq_tile']) {
      $array['seq_tile'] = $this->countTilesForScreen($array["id_screen"], $array['id_tile_parent']);
      }

      $sql = "insert into tile (id_tile_size,id_tile_orientation,id_screen,id_tile_parent,seq_tile,nm_tile) values (?,?,?,?,?,?)";
      $id = executaSQL($sql, array($array['id_tile_size'], $array['id_tile_orientation'], $array['id_screen'], $array['id_tile_parent'], $array['seq_tile'], $array['nm_tile']));
      return $id;
      } */

    /*
     * Retorna um array com as ações mandatórios(requeridas) pelo tipo de painel)
     */

    function getArrayActions($id_tipo_painel) {//coberto
        $arr = array();
        $controller = getControllerForTable("link");
        $controller->loadRegistros("and id_tipo_painel=$id_tipo_painel");
        while ($model = $controller->next()) {
            array_push($arr, $model);
        }
        return $arr;
    }

}

?>