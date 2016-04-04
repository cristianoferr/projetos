<?

class PanelController extends BaseController {

    function insereRegistro($array) {//coberto
        //echo "pagina: ".$array['pagina'];
        validaAdmin();


        $sql = "insert into painel (nm_painel,id_tipo_painel,desc_painel,classe_painel) values (?,?,?,?)";
        $id = executaSQL($sql, array($array['nm_painel'], $array['id_tipo_painel'], $array['desc_painel'], $array['classe_painel']));
        return $id;
    }

    function excluirRegistro($id) {//coberto
        validaAdmin();
        executaSQL("delete from painel where id_painel=?", array($id));
    }

    function getArrayActions($id_painel) {//coberto
        $id_tipo_painel = $this->getFieldFromModel($id_painel, "id_tipo_painel");
        $controller = getControllerForTable("tipo_painel");
        return $controller->getArrayActions($id_tipo_painel);
    }

    function getLinkWithId($id_link) {//coberto
        $controller = getControllerForTable("link");
        return $controller->loadSingle($id_link);
    }

    function createPanelObjectForID($id_painel) {//coberto
        $model = $this->loadSingle($id_painel);
        $classe = $model->getValorCampo("classe_painel");

        if ($classe == "painel_horizontal") {
            return new PainelHorizontal();
        }
        if ($classe == "painel_vertical") {
            return new PainelVertical();
        }

        erroFatal("Unknown panel type: $classe");
    }

}

?>