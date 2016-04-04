<?

class ScreenController extends BaseController {

    function insereRegistro($array) {//coberto
        //echo "pagina: ".$array['pagina'];
        validaEscrita("projeto", $array["projeto"], true);
        $sql = "insert into screen (id_projeto,nm_screen,caption_screen) values (?,?,?)";
        $id = executaSQL($sql, array($array['projeto'], $array['nm_screen'], $array['caption_screen']));

        $controller = getControllerForTable("tile");
        $controller->addRootTile($id, TILE_ORIENT_HORIZONTAL, TILE_SIZE_MEDIUM);
        return $id;
    }

    function resetScreen($id) {//coberto
        $id = validaNumero($id);
        validaEscrita("screen", $id, true);
        executaSQL("delete from tile where id_screen=?", array($id));
    }

    function getTileTree($idScreen) {//coberto
        $idScreen = validaNumero($idScreen);
        $controller = getControllerForTable("tile");
        $controller->loadRegistros("and id_screen=$idScreen");
        return $controller->createModelTree("id_tile_parent");
    }

    function getProjeto($id_entidade) {//coberto
        return $this->getFieldFromModel($id_entidade, "id_projeto");
    }

    function excluirRegistro($id) {//coberto
        $id = validaNumero($id);
        validaEscrita("screen", $id, true);
        executaSQL("delete from tile where id_screen=?", array($id));
        executaSQL("delete from screen where id_screen=?", array($id));
    }

}

?>