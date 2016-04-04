<?

class TileController extends BaseController {

    function insereRegistroMulti($array) {
        $model = $this->loadSingle($array["tile_pai"]);
        $screen = $model->getValorCampo("id_screen");
        validaEscrita("screen", $screen, true);
        executaSQL("delete from tile where id_tile_parent=?", array($array["tile_pai"]));
        for ($c = 0; $c < $array["qtd_tile"]; $c++) {
            $arr = array("id_screen" => $screen,
                "id_tile_orientation" => $array["id_tile_orientation"],
                "id_tile_size" => $array["id_tile_size"],
                "id_tile_parent" => $array["tile_pai"],
                "seq_tile" => $c);
            $this->insereRegistro($arr);
        }
        return $screen;
    }

    function insereRegistro($array) {//coberto
        //echo "pagina: ".$array['pagina'];
        validaEscrita("screen", $array["id_screen"], true);


        if (!$array['seq_tile']) {
            $array['seq_tile'] = $this->countTilesForScreen($array["id_screen"], $array['id_tile_parent']);
        }

        $sql = "insert into tile (id_tile_size,id_tile_orientation,id_screen,id_tile_parent,seq_tile,nm_tile) values (?,?,?,?,?,?)";
        $id = executaSQL($sql, array($array['id_tile_size'], $array['id_tile_orientation'], $array['id_screen'], $array['id_tile_parent'], $array['seq_tile'], $array['nm_tile']));
        return $id;
    }

    function assignWidget($id_tile, $id_widget = null) {//coberto
        $this->atualizaValor($id_tile, "id_widget", $id_widget);
    }

    function getWidgetAssignedTo($id_tile) {//coberto
        return $this->getFieldFromModel($id_tile, "id_widget");
    }

    function getTilePosition($id) {//coberto
        return $this->getFieldFromModel($id, "seq_tile", false);
    }

    function moveTileDown($id) {//coberto
        $posAtual = $this->getTilePosition($id);
        $posDest = $posAtual + 1;
        $this->moveTile($id, $posAtual, $posDest);
    }

    function moveTileUp($id) {//coberto
        $posAtual = $this->getTilePosition($id);
        if ($posAtual > 0) {
            $posDest = $posAtual - 1;
            $this->moveTile($id, $posAtual, $posDest);
        }
    }

    function moveTile($id, $posAtual, $posDest) {//coberto
        $parent = $this->getTileParent($id);
        $screen = $this->getScreen($id);
        $row = executaQuerySingleRow("select id_tile from tile where seq_tile=? and id_screen=? and id_tile_parent=?", array($posDest, $screen, $parent));
        if ($row) {
            $id_alt = $row["id_tile"];
            executaSQL("update tile set seq_tile=? where id_tile=?", array($posDest, $id));
            executaSQL("update tile set seq_tile=? where id_tile=?", array($posAtual, $id_alt));
        } else {
            //    erroFatal("Tile com pos $posDest e parent $parent e screen $scren não encontrados!");
        }
    }

    function addRootTile($id_screen, $orient, $size) {//coberto
        $arr = array("id_screen" => $id_screen, "id_tile_size" => $size, "id_tile_orientation" => $orient, "id_tile_parent" => null, "seq_tile" => null, "nm_tile" => "root");
        return $this->insereRegistro($arr);
    }

    function addTile($idTilePai, $orient, $size, $name = null) {//coberto
        $id_screen = $this->getScreen($idTilePai);
        $arr = array("id_screen" => $id_screen, "id_tile_size" => $size, "id_tile_orientation" => $orient, "id_tile_parent" => $idTilePai, "seq_tile" => null, "nm_tile" => $name);
        return $this->insereRegistro($arr);
    }

    function getScreen($id_tile) {//coberto
        return $this->getFieldFromModel($id_tile, "id_screen");
    }

    function getTileParent($id_tile) {//coberto
        return $this->getFieldFromModel($id_tile, "id_tile_parent");
    }

    function countTilesForScreen($id_screen, $tileParent = false) {//coberto
        $id_screen = validaNumero($id_screen);
        $tileParent = validaNumero($tileParent);
        validaLeitura("screen", $id_screen);
        $comp = "=";
        if ($tileParent == "null") {
            $comp = "is";
        }

        $row = executaQuerySingleRow("select count(*) as tot from tile where id_screen=? and id_tile_parent $comp $tileParent", array($id_screen));
        return $row['tot'];
    }

    function getProjeto($id) {//coberto
        $key = "tile_proj_$id";
        if ($this->getCacheInfo($key)) {
            return $this->getCacheInfo($key);
        }
        $id = validaNumero($id, "id getProjeto contr_tile");
        $row = executaQuerySingleRow("select id_projeto from screen s,tile t where s.id_screen=t.id_screen and t.id_tile=?", array($id));
        $this->setCacheInfo($key, $row['id_projeto']);
        return $row['id_projeto'];
    }

    function excluirRegistro($id) {//coberto
        $id = validaNumero($id);
        validaEscrita("tile", $id);
        executaSQL("delete from tile where id_tile=? and id_tile_parent is not null", array($id));
    }

    function getOrderBy() {
        return "id_tile_parent,seq_tile";
    }

}

?>